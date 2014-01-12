package de.perdoctus.starbound.base;

import com.sun.org.apache.bcel.internal.classfile.Code;
import de.perdoctus.starbound.base.dialogs.ProgressDialog;
import de.perdoctus.starbound.base.dialogs.SettingsDialog;
import de.perdoctus.starbound.mod.ModDialog;
import de.perdoctus.starbound.types.base.*;
import de.perdoctus.starbound.types.codex.Codex;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.controlsfx.control.TextFields;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @author Christoph Giesche
 */
public class MainViewController {


	public static final String ASSETS_FOLDER = "assets";
	public static final String STARBOUND_VERSIONS_SOURCE = "https://raw.github.com/cgiesche/starbound-editor/master/resources/base/starbound-versions.json";
	private final static Logger log = Logger.getLogger(MainViewController.class.getName());
	private static final File SETTINGS_FILE = new File(System.getProperty("user.home") + File.separatorChar + "perdoctus-sb-editor.json");
	@Deprecated
	private final MainViewModel model = new MainViewModel();
	@FXML
	public TreeView tvAssets;
	@FXML
	private VBox vBoxNavigation;
	@FXML
	private Menu mnuNew;
	@FXML
	private Label lblStatus;
	@FXML
	private Menu mnuMods;
	private ToggleGroup tgMods = new ToggleGroup();
	@FXML
	private MenuItem mnuSave;
	@FXML
	private MenuItem mnuClose;
	@FXML
	private TabPane tabPane;
	private List<AssetType> availableAssetTypes;
	private AssetManager assetManager;
	private AssetTreeviewController assetTreeviewController;
	private ApplicationContext applicationContext;

	public MainViewController() {
		this.applicationContext = ApplicationContext.getInstance();
	}

	public void initialize() throws Exception {
		tabPane.tabClosingPolicyProperty().setValue(TabPane.TabClosingPolicy.ALL_TABS);
		mnuSave.disableProperty().bind(tabPane.getSelectionModel().selectedItemProperty().isNull());

		/*
		TODO: Event! ========================================================================
		Modwechsel findet bisher noch direkt statt. Dies wird das neue Vorgehen.
		 */
		model.activeModProperty().addListener(new ChangeListener<Mod>() {
			@Override
			public void changed(final ObservableValue<? extends Mod> observableValue, final Mod mod, final Mod mod2) {
				log.fine("Active Mod changed: " + mod2);
			}
		});

		applicationContext.getSettings().starboundHomeProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
				if (!newValue.isEmpty()) {
					rescanCoreAssetsDirectory();
					refreshModList();
				}
			}
		});

		assetTreeviewController = new AssetTreeviewController(tvAssets, model.getAssets());
		assetTreeviewController.setOnAssetSelected(this::openEditor);

		final TextField searchField = TextFields.createSearchField();
		searchField.setOnAction(event -> assetTreeviewController.filter(searchField.getText()));
		vBoxNavigation.getChildren().add(searchField);

		this.availableAssetTypes = readEditorTypes();
		rebuildNewMenu();
	}

	private void rebuildNewMenu() {
		mnuNew.getItems().clear();
		for (AssetType assetType : availableAssetTypes) {
			final MenuItem menuItem = new MenuItem(assetType.getAssetTitle() + " (" + assetType.getAssetGroup() + ")");
			menuItem.setOnAction(event -> createNewAsset(assetType));
			mnuNew.getItems().add(menuItem);
		}
	}

	private void createNewAsset(final AssetType assetType) {
		final Asset newAsset;
		try {
			final Class assetClass = Class.forName(assetType.getAssetClass());
			newAsset = (Asset) assetClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		newAsset.setAssetType(assetType);

		openEditor(newAsset);
	}

	private Void openEditor(final Asset asset) {
		final Tooltip editorTabTooltip = new Tooltip();
		editorTabTooltip.textProperty().bind(asset.assetLocationProperty().asString());

		final Tab editorTab = new Tab();
		editorTab.setTooltip(editorTabTooltip);
		editorTab.textProperty().bind(asset.assetTitleProperty());

		final Constructor constructor;
		final AssetEditor assetEditor;
		try {
			final Class editorClass = Class.forName(asset.getAssetType().getEditorClass());
			final Class assetClass = Class.forName(asset.getAssetType().getAssetClass());
			constructor = editorClass.getConstructor(assetClass);

			final Object castedAsset = assetClass.cast(asset);
			assetEditor = (AssetEditor) constructor.newInstance(castedAsset);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		editorTab.setContent(assetEditor);
		editorTab.setOnCloseRequest(event -> onEditorTabCloseRequest(assetEditor, event));
		assetEditor.disableProperty().bind(model.activeModProperty().isNull());
		tabPane.getTabs().add(editorTab);
		tabPane.getSelectionModel().select(editorTab);

		return null;
	}

	/**
	 * Is called when an EditorTab is requested to close. Time to save Asset?
	 *
	 * @param assetEditor The AssetEdtior of the Tab.
	 * @param event       The event. May be consumed.
	 */
	private void onEditorTabCloseRequest(final AssetEditor assetEditor, final Event event) {
		if (assetEditor.isDirty()) {
			final Asset asset = assetEditor.getAsset();
			final Action action = Dialogs.create().title(asset.assetTitleProperty().get()).message("The asset has beeen changed. Save changes to mod?").showConfirm();

			if (action == Dialog.Actions.CANCEL) {
				event.consume();
			} else if (action == Dialog.Actions.YES) {
				final File assetLocation = asset.getAssetLocation();
				if (assetLocation == null) {
					saveAssetAs(asset);
				}
				try {
					assetManager.saveAsset(assetLocation, asset);
				} catch (IOException e) {
					e.printStackTrace(); //TODO: error message
					event.consume();
				}
			} else if (action == Dialog.Actions.NO && asset.getAssetLocation() != null) {
				reloadAsset(asset);
			}
		}
	}

	private void saveAssetAs(final Asset asset) {
		//if ()
	}

	private void reloadAsset(final Asset asset) {
		try {
			assetManager.reloadAsset(asset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<AssetType> readEditorTypes() {
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			final List<AssetType> assetTypes = objectMapper.readValue(getClass().getResource("/base/editors.json"), new TypeReference<List<AssetType>>() {
			});

			return assetTypes;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Called, directly after the main window became visible.
	 */
	public void onShow() {
		initApplicationContext();
	}

	private void initApplicationContext() {
		loadStarboundVersions();
		loadSettings();
	}

	private void loadSettings() {
		if (SETTINGS_FILE.exists()) {
			final ObjectMapper objectMapper = new ObjectMapper();
			final Settings settings = applicationContext.getSettings();
			try {
				objectMapper.readerForUpdating(settings).readValue(SETTINGS_FILE);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e); //TODO: Handle
			}
		}
	}

	private void loadStarboundVersions() {
		//final URL remoteSbVersionsURL = new URL(STARBOUND_VERSIONS_SOURCE);
		final URL remoteSbVersionsURL = getClass().getResource("/base/starbound-versions.json"); // FIXME: s.o.
		final JsonDownloadTask<StarboundVersion[]> task = new JsonDownloadTask<>(remoteSbVersionsURL, StarboundVersion[].class);

		ProgressDialog.getInstance().execute(task);

		task.setOnSucceeded(event -> {
			applicationContext.getStarboundVersions().clear();
			applicationContext.getStarboundVersions().addAll(task.getValue());
		});
	}

	private void refreshModList() {
		final ModsScanTask modsScanTask = new ModsScanTask(new File(applicationContext.getSettings().getStarboundHome() + File.separatorChar + "mods"));
		modsScanTask.setOnSucceeded(event -> modsChanged(modsScanTask.getValue()));

		ProgressDialog.getInstance().owner(tabPane.getScene().getWindow()).execute(modsScanTask);
	}

	private void modsChanged(final List<Mod> value) {
		rebuildModsMenu(value);
	}

	private void rebuildModsMenu(final List<Mod> value) {
		changeActiveMod(null);
		mnuMods.getItems().clear();

		final RadioMenuItem nullMod = new RadioMenuItem("None");
		nullMod.setMnemonicParsing(false);
		nullMod.setOnAction(event -> changeActiveMod(null));
		nullMod.setToggleGroup(tgMods);
		nullMod.setSelected(true);
		mnuMods.getItems().add(nullMod);
		for (Mod mod : value) {
			addModMenuEntry(mod);
		}
	}

	private void addModMenuEntry(final Mod mod) {
		final RadioMenuItem radioMenuItem = new RadioMenuItem(mod.getModInfo().getName());
		radioMenuItem.setMnemonicParsing(false);
		radioMenuItem.setToggleGroup(tgMods);
		radioMenuItem.setOnAction(event -> {
			if (radioMenuItem.isSelected()) {
				changeActiveMod(mod);
			}
		});
		mnuMods.getItems().add(radioMenuItem);
	}

	private void changeActiveMod(final Mod mod) {
		model.activeModProperty().setValue(mod);

		if (mod != null) {
			rescanModDirectory(mod.getModLocation());
		} else {
			model.getAssets().removeIf(asset -> asset.getAssetOrigin() == AssetOrigin.MOD);
		}
	}

	@Deprecated
	private void reloadSettings() {
		if (SETTINGS_FILE.exists()) {
			final ObjectMapper mapper = new ObjectMapper();
			try {
				final Settings settings = mapper.readValue(SETTINGS_FILE, Settings.class);
				if (settings.getStarboundHome() != null) {
//					model.setSettings(settings);
					settingsLoaded();
				} else {
					forceSettings();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			forceSettings();
		}
	}

	private void settingsLoaded() {
		this.assetManager = new AssetManager(availableAssetTypes);
		rescanCoreAssetsDirectory();
		refreshModList();
	}

	private void forceSettings() {
		final Action action = Dialogs.create().owner(tabPane.getScene().getWindow()).message("First startup: Setup the editor now?").showConfirm();
		if (action == Dialog.Actions.YES) {
			showSettingsDialog(true);
		} else {
			exitApplication();
		}
	}

	public void showOpenFileDialog(ActionEvent actionEvent) {
		Dialogs.create().message("hallo").masthead("Dies ist ein Titel").showInformation();
	}

	public void saveCurrentEditorTab(ActionEvent actionEvent) {
		// yoyo
	}

	public void closeActiveEditor(ActionEvent actionEvent) {
//		closeEditor();
	}

	public void mnuShowSettingsDialog() {
		showSettingsDialog(false);
	}

	private void showSettingsDialog(boolean forceReload) {
		final boolean settingsChanged = SettingsDialog.create(SETTINGS_FILE).owner(tabPane.getScene().getWindow()).show();
		if (settingsChanged || forceReload) {
			loadSettings();
		}
	}

	private void rescanCoreAssetsDirectory() {
		final boolean b = model.getAssets().removeIf(asset -> asset.getAssetOrigin() == AssetOrigin.CORE);

		final File coreAssetsDirectory = new File(applicationContext.getSettings().getStarboundHome() + File.separatorChar + ASSETS_FOLDER);
		final SupportedAssetsScanTask supportedAssetsScanTask = new SupportedAssetsScanTask(coreAssetsDirectory, availableAssetTypes);
		supportedAssetsScanTask.setOnSucceeded(event -> availableCoreAssetsChanged(supportedAssetsScanTask.getValue()));
		ProgressDialog.getInstance().owner(tabPane.getScene().getWindow()).execute(supportedAssetsScanTask);
	}

	private void rescanModDirectory(final File modDirectory) {
		final boolean b = model.getAssets().removeIf(asset -> asset.getAssetOrigin() == AssetOrigin.MOD);

		final SupportedAssetsScanTask supportedAssetsScanTask = new SupportedAssetsScanTask(modDirectory, availableAssetTypes);
		supportedAssetsScanTask.setOnSucceeded(event -> availableModAssetsChanged(supportedAssetsScanTask.getValue()));
		ProgressDialog.getInstance().owner(tabPane.getScene().getWindow()).execute(supportedAssetsScanTask);
	}

	private void availableCoreAssetsChanged(final List<Asset> coreAssets) {
		coreAssets.forEach(asset -> asset.setAssetOrigin(AssetOrigin.CORE));
		model.getAssets().addAll(coreAssets);
	}

	private void availableModAssetsChanged(final List<Asset> modAssets) {
		modAssets.forEach(asset -> asset.setAssetOrigin(AssetOrigin.MOD));
		model.getAssets().addAll(modAssets);
	}

	public void exitApplication() {
		//TODO: Check unsaved files
		Platform.exit();
	}

	public void showCreateModDialog(ActionEvent actionEvent) {
//		ModDialog.create(tabPane.getScene().getWindow()).show();

		final Asset asset = model.getAssets().get(0);
		model.getAssets().add(asset);
	}
}

package de.perdoctus.starbound.base;

import de.perdoctus.starbound.base.dialogs.ProgressDialog;
import de.perdoctus.starbound.base.dialogs.SettingsDialog;
import de.perdoctus.starbound.codex.CodexEditor;
import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.EditorType;
import de.perdoctus.starbound.types.base.Mod;
import de.perdoctus.starbound.types.base.Settings;
import de.perdoctus.starbound.types.codex.Codex;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * @author Christoph Giesche
 */
public class MainViewController {

	public static final String ASSETS_FOLDER = "assets";
	private final static Logger log = Logger.getLogger(MainViewController.class.getName());
	private static final File SETTINGS_FILE = new File(System.getProperty("user.home") + File.separatorChar + "perdoctus-sb-editor.json");

	@FXML
	private Accordion accAssetList;
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
	private Map<Tab, DefaultController> tabEditorControllers = new HashMap<>(20);
	private Settings settings;
	private ObjectProperty<Mod> activeMod = new SimpleObjectProperty<>();
	private List<EditorType> availableEditors;
	private AssetAccordionCtrl accordionCtrl;

	public void initialize() throws Exception {
		tabPane.tabClosingPolicyProperty().setValue(TabPane.TabClosingPolicy.ALL_TABS);
		mnuSave.disableProperty().bind(tabPane.getSelectionModel().selectedItemProperty().isNull());
		lblStatus.textProperty().bind(Bindings.concat("Active mod: ", activeMod));
		accordionCtrl = new AssetAccordionCtrl(accAssetList);
		accordionCtrl.setOnAssetSelected(asset -> openEditor(asset));

		this.availableEditors = readEditorTypes();
	}

	private Void openEditor(final Asset asset) {
		final Tab editorTab = new Tab(asset.getAssetFile().getName());

		final Constructor constructor;
		final AssetEditor assetEditor;
		try {
			final Class editorClass = Class.forName(asset.getEditorType().getEditorClass());
			final Class assetClass = Class.forName(asset.getEditorType().getAssetClass());
			constructor = editorClass.getConstructor(assetClass);

			final Object castedAsset = assetClass.cast(asset);
			assetEditor = (AssetEditor) constructor.newInstance(castedAsset);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		editorTab.setContent(assetEditor);
		tabPane.getTabs().add(editorTab);
		tabPane.getSelectionModel().select(editorTab);

		return null;
	}

	private List<EditorType> readEditorTypes() {
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			final List<EditorType> editorTypes = objectMapper.readValue(getClass().getResource("/base/editors.json"), new TypeReference<List<EditorType>>() {
			});

			return editorTypes;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Called, directly after the main window became visible.
	 */
	public void onShow() {
		reloadSettings();
	}

	private void refreshModList() {
		final ModsScanTask modsScanTask = new ModsScanTask(new File(settings.getStarboundHome() + File.separatorChar + "mods"));
		modsScanTask.setOnSucceeded(event -> modsChanged(modsScanTask.getValue()));

		ProgressDialog.create().owner(tabPane.getScene().getWindow()).execute(modsScanTask);
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
		activeMod.setValue(mod);

		if (mod != null) {
			rescanModDirectory(mod.getModLocation());
		} else {
			availableModAssetsChanged(Collections.emptyList());
		}
	}

	private void reloadSettings() {
		if (SETTINGS_FILE.exists()) {
			final ObjectMapper mapper = new ObjectMapper();
			try {
				this.settings = mapper.readValue(SETTINGS_FILE, Settings.class);
				if (settings.getStarboundHome() != null) {
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
		// jojo
	}


	public void saveCurrentEditorTab(ActionEvent actionEvent) {
		final Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
		if (currentTab != null) {
			tabEditorControllers.get(currentTab).save();
		}
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
			reloadSettings();
		}
	}

	private void rescanCoreAssetsDirectory() {
		final File assetsRootDirectory = new File(settings.getStarboundHome() + File.separatorChar + ASSETS_FOLDER);
		final SupportedAssetsScanTask supportedAssetsScanTask = new SupportedAssetsScanTask(assetsRootDirectory, availableEditors);
		supportedAssetsScanTask.setOnSucceeded(event -> availableCoreAssetsChanged(supportedAssetsScanTask.getValue()));
		ProgressDialog.create().owner(tabPane.getScene().getWindow()).execute(supportedAssetsScanTask);
	}

	private void rescanModDirectory(final File modDirectory) {
		final SupportedAssetsScanTask supportedAssetsScanTask = new SupportedAssetsScanTask(modDirectory, availableEditors);
		supportedAssetsScanTask.setOnSucceeded(event -> availableModAssetsChanged(supportedAssetsScanTask.getValue()));
		ProgressDialog.create().owner(tabPane.getScene().getWindow()).execute(supportedAssetsScanTask);
	}

	private void availableCoreAssetsChanged(final List<Asset> coreAssets) {
		for (Asset coreAssetInformation : coreAssets) {
			System.out.println(coreAssetInformation);
		}
		accordionCtrl.updateView(coreAssets);
	}

	private void availableModAssetsChanged(final List<Asset> modAssets) {
		for (Asset modAssetInformation : modAssets) {
			System.out.println(modAssetInformation);
		}
	}

	public void exitApplication() {
		//TODO: Check unsaved files
		Platform.exit();
	}
}

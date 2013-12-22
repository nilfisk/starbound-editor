package de.perdoctus.starbound.base;

import de.perdoctus.starbound.base.dialogs.ProgressDialog;
import de.perdoctus.starbound.base.dialogs.SettingsDialog;
import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.EditorType;
import de.perdoctus.starbound.types.base.Mod;
import de.perdoctus.starbound.types.base.Settings;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static org.controlsfx.dialog.Dialog.Actions.CANCEL;
import static org.controlsfx.dialog.Dialog.Actions.YES;

/**
 * @author Christoph Giesche
 */
public class MainViewController {

	public static final String ASSETS_FOLDER = "assets";
	private final static Logger log = Logger.getLogger(MainViewController.class.getName());
	private static final File SETTINGS_FILE = new File(System.getProperty("user.home") + File.separatorChar + "perdoctus-sb-editor.json");
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

	public void initialize() throws Exception {
		tabPane.tabClosingPolicyProperty().setValue(TabPane.TabClosingPolicy.ALL_TABS);
		mnuSave.disableProperty().bind(tabPane.getSelectionModel().selectedItemProperty().isNull());
		lblStatus.textProperty().bind(Bindings.concat("Active mod: ", activeMod));

		this.availableEditors = readEditorTypes();
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

	private void parseModsFolder() {
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
		parseModsFolder();
	}

	private void forceSettings() {
		final Action action = Dialogs.create().owner(tabPane.getScene().getWindow()).message("First startup: Setup the editor now?").showConfirm();
		if (action == Dialog.Actions.YES) {
			showSettingsDialog(true);
		} else {
			exitApplication();
		}
	}

	private void createEditorTab(final File file) throws IOException {
		if (file.getName().endsWith(".codex")) {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("/codex/CodexEditorView.fxml"), ResourceBundle.getBundle("codex.codex"));
			final Node editorView = loader.load();
			final DefaultController controller = loader.getController();
			controller.load(file);

			final Tab tab = new Tab();
			tab.textProperty().bind(Bindings.concat(file.getName(), " ", controller.dirtyProperty()));
			tab.setContent(editorView);

			tab.setClosable(true);
			tab.setOnCloseRequest(event -> {
				if (controller.isDirty()) {
					final Action action = Dialogs.create().owner(tab.getTabPane().getScene().getWindow()).title("%save").message("%savemessage").showConfirm();

					if (action == YES) {
						controller.save();
					} else if (action == CANCEL) {
						event.consume();
					}
				}
			});

			tab.setOnClosed(event -> {
				tabEditorControllers.remove(tab);
			});

			tabPane.getTabs().add(tab);
			tabEditorControllers.put(tab, controller);
		}
	}

	public void showOpenFileDialog(ActionEvent actionEvent) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Supported Statbound Data File", "*.codex"));
		final List<File> selectedFiles = fileChooser.showOpenMultipleDialog(tabPane.getScene().getWindow());

		if (selectedFiles != null) {
			try {
				openEditorTabs(selectedFiles);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void openEditorTabs(final List<File> selectedFiles) throws IOException {
		for (File selectedFile : selectedFiles) {
			openEditorTab(selectedFile);
		}
	}

	private void openEditorTab(final File selectedFile) throws IOException {
		createEditorTab(selectedFile);
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
		for (Asset coreAsset : coreAssets) {
			System.out.println(coreAsset);
		}

	}

	private void availableModAssetsChanged(final List<Asset> assets) {
		for (Asset asset : assets) {
			System.out.println(asset);
		}
	}

	public void exitApplication() {
		//TODO: Check unsaved files
		Platform.exit();
	}
}

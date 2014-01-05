package de.perdoctus.starbound.base.dialogs;

import de.perdoctus.starbound.base.DefaultController;
import de.perdoctus.starbound.types.base.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Christoph Giesche
 */
public class SettingsDialog extends DefaultController<Settings> {

	private final Stage settingsStage;
	private boolean settingsChanged = false;
	@FXML
	private TextField txtStarboundDir;
	@FXML
	private BorderPane rootPane;

	private SettingsDialog(final File settingsFile) {
		super();

		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/base/Settings.fxml"), ResourceBundle.getBundle("base.base"));
		loader.setController(this);

		final Parent view;
		try {
			view = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		settingsStage = new Stage();
		final Scene settingsScene = new Scene(view);
		settingsStage.setScene(settingsScene);
		settingsStage.initModality(Modality.APPLICATION_MODAL);

		if (settingsFile.exists()) {
			load(settingsFile);
		} else {
			save(settingsFile);
		}
	}

	public static SettingsDialog create(final File settingsFile) {
		return new SettingsDialog(settingsFile);
	}

	@FXML
	private void initialize() {
		txtStarboundDir.textProperty().addListener(this);
	}

	@Override
	protected Class<Settings> getModelClass() {
		return Settings.class;
	}

	@Override
	protected void modelChanged(final Settings oldModel, final Settings newModel) {
		if (oldModel != null) {
			txtStarboundDir.textProperty().unbindBidirectional(oldModel.starboundHomeProperty());
		}
		txtStarboundDir.textProperty().bindBidirectional(newModel.starboundHomeProperty());
	}

	public SettingsDialog owner(final Window owner) {
		settingsStage.initOwner(owner);
		settingsStage.initModality(Modality.WINDOW_MODAL);
		return this;
	}

	/**
	 * Shows the Settings-Dialog.
	 *
	 * @return If settings were changed.
	 */
	public boolean show() {
		settingsStage.showAndWait();
		return settingsChanged;
	}

	public void saveSettings() {
		if (isDirty()) {
			save();
			settingsChanged = true;
		}
		closeDialog();
	}

	public void closeDialog() {
		settingsStage.close();
	}

	@FXML
	private void showBrowseSbDir() {
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		final File selectedDirectory = directoryChooser.showDialog(rootPane.getScene().getWindow());

		if (selectedDirectory != null) {
			txtStarboundDir.textProperty().setValue(selectedDirectory.getAbsolutePath());
		}
	}

}

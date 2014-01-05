package de.perdoctus.starbound.mod;

import de.perdoctus.starbound.types.base.ModInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * @author Christoph Giesche
 */
public class ModDialog {

	@FXML
	private TextField txtName;
	@FXML
	private ComboBox<String> cmbVersion;
	@FXML
	private TextField txtPath;
	@FXML
	private ListView<String> lstDependencies;

	private Dialog dialog;
	private ModInfo dialogModel;
	private ResourceBundle dialogResourceBundle;

	private ModDialog(final Window owner) {
		this.dialogResourceBundle = ResourceBundle.getBundle("mod.createmod");
		this.dialog = new Dialog(owner, "foo", false, true);
	}

	private Node loadDialogContent() {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/mod/createmod.fxml"), dialogResourceBundle);
		loader.setController(this);
		try {
			return loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static ModDialog create(final Window owner) {
		return new ModDialog(owner);
	}

	@FXML
	private void initialize() {
		dialogModel.nameProperty().bindBidirectional(txtName.textProperty());
		dialogModel.pathProperty().bindBidirectional(txtPath.textProperty());
		dialogModel.versionProperty().bind(cmbVersion.getSelectionModel().selectedItemProperty());
		dialogModel.dependenciesProperty().bindBidirectional(lstDependencies.itemsProperty());
	}

	public Action show(final ModInfo modInfo) {
		this.dialogModel = modInfo;

		dialog.setMasthead(dialogResourceBundle.getString("moddialog.edit.masthead"));
		dialog.setTitle(dialogResourceBundle.getString("moddialog.edit.title"));
		return showDialog();
	}

	public Action show() {
		this.dialogModel = new ModInfo();
		dialog.setMasthead(dialogResourceBundle.getString("moddialog.create.masthead"));
		dialog.setTitle(dialogResourceBundle.getString("moddialog.create.title"));
		return showDialog();
	}

	private Action showDialog() {
		dialog.getActions().addAll(Dialog.Actions.OK, Dialog.Actions.CANCEL);
		dialog.setContent(loadDialogContent());

		return dialog.show();
	}


}

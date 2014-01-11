package de.perdoctus.starbound.mod;

import de.perdoctus.starbound.base.ApplicationContext;
import de.perdoctus.starbound.types.base.ModInfo;
import de.perdoctus.starbound.types.base.StarboundVersion;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.util.StringConverter;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Christoph Giesche
 */
public class ModDialog {

	@FXML
	private TextField txtName;
	@FXML
	private ComboBox<StarboundVersion> cmbVersion;
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

	public static ModDialog create(final Window owner) {
		return new ModDialog(owner);
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

	@FXML
	private void initialize() {
		final ListProperty<StarboundVersion> starboundVersions = ApplicationContext.getInstance().starboundVersionsProperty();
		cmbVersion.itemsProperty().bind(starboundVersions);
		final FilteredList<StarboundVersion> currentVersion = starboundVersions.filtered(StarboundVersion::getCurrent);
		if (!currentVersion.isEmpty()) {
			cmbVersion.getSelectionModel().select(currentVersion.get(0));
		}

		dialogModel.nameProperty().bindBidirectional(txtName.textProperty());
		dialogModel.pathProperty().bindBidirectional(txtPath.textProperty());
		dialogModel.versionProperty().bind(cmbVersion.getSelectionModel().selectedItemProperty().asString());
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

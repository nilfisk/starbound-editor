package de.perdoctus.starbound.base;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Christoph Giesche
 */
public class MainViewController {

	public Pane editorPane;
	public ListView<File> lstFiles;
	private Map<String,Editor> editors = new HashMap<>();

	public void initialize() throws Exception {
		editors.put(".codex", createEditor("/objecteditor/ObjectEditor.fxml", "codex.te"));

		lstFiles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<File>() {
			@Override
			public void changed(final ObservableValue<? extends File> observableValue, final File file, final File file2) {
				final Editor editor = editors.get(".codex");
				open(editor, file2);
			}
		});
	}

	private void open(Editor editor, File file) {
		editorPane.getChildren().clear();
		editorPane.getChildren().add(editor.getView());
		try {
			editor.getController().load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Editor createEditor(final String fxmlResourceLocation, final String bundleName) throws IOException {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResourceLocation), ResourceBundle.getBundle(bundleName));
		final EditorController controller = loader.getController();
		final Node load = (Node) loader.load();
		return new Editor(controller, load);
	}

	public void showOpenFileDialog(ActionEvent actionEvent) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Supported Statbound Data File", "*.codex"));
		final List<File> selectedFiles = fileChooser.showOpenMultipleDialog(editorPane.getScene().getWindow());

		if (selectedFiles != null) {
			lstFiles.getItems().addAll(selectedFiles);
		}

	}
}

package de.perdoctus.starbound.base;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
	public MenuItem mnuSave;
	public MenuItem mnuClose;
	private Map<String, Editor> editors = new HashMap<>();
	private Editor activeEditor;

	public void initialize() throws Exception {
		editors.put(".codex", createEditor("/codex/CodexEditorView.fxml", "codex.te"));

		lstFiles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<File>() {
			@Override
			public void changed(final ObservableValue<? extends File> observableValue, final File file, final File file2) {
				final Editor editor = editors.get(".codex");
				openEditor(editor, file2);
			}
		});
	}

	private void openEditor(final Editor editor, final File file) {
		editorPane.getChildren().clear();
		activeEditor = null;
		try {
			editor.getController().load(file);
			editorPane.getChildren().add(editor.getView());
			activeEditor = editor;
			mnuSave.disableProperty().bind(Bindings.not(activeEditor.getController().dirtyProperty()));
			mnuClose.disableProperty().setValue(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void closeEditor() {
		if (activeEditor != null) {
			editorPane.getChildren().clear();
			mnuSave.disableProperty().unbind();
		}
		mnuClose.disableProperty().setValue(true);
	}

	private Editor createEditor(final String fxmlResourceLocation, final String bundleName) throws IOException {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResourceLocation), ResourceBundle.getBundle(bundleName));
		final Node load = (Node) loader.load();
		final EditorController controller = loader.getController();
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

	public void saveActiveEditor(ActionEvent actionEvent) {
		if (activeEditor != null) {
			try {
				activeEditor.getController().save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeActiveEditor(ActionEvent actionEvent) {
		closeEditor();
	}
}

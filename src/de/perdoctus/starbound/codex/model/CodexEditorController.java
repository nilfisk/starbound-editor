package de.perdoctus.starbound.codex.model;

import de.perdoctus.starbound.types.codexEditor.Codex;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph Giesche
 */
public class CodexEditorController {


	public ListView<JsonFile<Codex>> lstCodexItems;
	public TextField txtCodexId;
	public TextField txtCodexTitle;
	public ListView<StringProperty> lstPages;
	public TextArea txtPageContent;
	public MenuItem mnuAddPage;
	public MenuItem mnuDeletePage;
	public ProgressBar progressbar;
	public BorderPane paneMain;
	private List<JsonFile<Codex>> codexItemJsonFiles;

	public void initialize() {

		mnuAddPage.disableProperty().bind(lstCodexItems.getSelectionModel().selectedItemProperty().isNull());
		mnuDeletePage.disableProperty().bind(lstPages.getSelectionModel().selectedItemProperty().isNull());

		lstPages.setCellFactory(new Callback<ListView<StringProperty>, ListCell<StringProperty>>() {
			@Override
			public ListCell<StringProperty> call(final ListView<StringProperty> param) {
				return new ListCell<StringProperty>() {
					@Override
					protected void updateItem(final StringProperty item, final boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							final String format;
							if (item.get().length() > 40) {
								format = "%.40s [...]";
							} else {
								format = "%s";
							}
							textProperty().bind(Bindings.format(format, item));
						} else {
							textProperty().unbind();
							setText("");
						}
					}
				};
			}
		});
		lstPages.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StringProperty>() {
			@Override
			public void changed(final ObservableValue<? extends StringProperty> observableValue, final StringProperty valueBefore, final StringProperty valueNow) {
				if (valueBefore != null) {
					txtPageContent.textProperty().unbindBidirectional(valueBefore);
				}
				if (valueNow != null) {
					txtPageContent.textProperty().bindBidirectional(valueNow);
				}
			}
		});

		lstCodexItems.setCellFactory(new Callback<ListView<JsonFile<Codex>>, ListCell<JsonFile<Codex>>>() {
			@Override
			public ListCell<JsonFile<Codex>> call(final ListView<JsonFile<Codex>> codexItemListView) {
				ListCell<JsonFile<Codex>> listCell = new ListCell<JsonFile<Codex>>() {
					@Override
					protected void updateItem(final JsonFile<Codex> codexJsonFile, final boolean b) {
						super.updateItem(codexJsonFile, b);

						if (codexJsonFile != null) {
							final Codex content = codexJsonFile.getContent();
							final StringExpression concat = Bindings.concat(content.titleProperty(), " (", content.idProperty(), ")");
							textProperty().bind(concat);
							setTooltip(new Tooltip(codexJsonFile.getFile().getAbsolutePath()));
						}
					}
				};
				return listCell;
			}
		});
		lstCodexItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<JsonFile<Codex>>() {
			@Override
			public void changed(final ObservableValue<? extends JsonFile<Codex>> observableValue, final JsonFile<Codex> codexJsonFileBefore, final JsonFile<Codex> codexJsonFileAfter) {
				if (codexJsonFileBefore != null) {
					Bindings.unbindBidirectional(txtCodexId.textProperty(), codexJsonFileBefore.getContent().idProperty());
					Bindings.unbindBidirectional(txtCodexTitle.textProperty(), codexJsonFileBefore.getContent().titleProperty());
					Bindings.unbindBidirectional(lstPages.itemsProperty(), codexJsonFileBefore.getContent().contentPagesProperty());
				}
				if (codexJsonFileAfter != null) {
					Bindings.bindBidirectional(txtCodexId.textProperty(), codexJsonFileAfter.getContent().idProperty());
					Bindings.bindBidirectional(txtCodexTitle.textProperty(), codexJsonFileAfter.getContent().titleProperty());
					System.out.println("Jetzt kommen " + codexJsonFileAfter.getContent().getContentPages().size() + " Seiten!");
					lstPages.itemsProperty().bindBidirectional(codexJsonFileAfter.getContent().contentPagesProperty());
				}
			}
		});
	}

	public void scanFolder(final ActionEvent actionEvent) {
		final DirectoryChooser dc = new DirectoryChooser();
		final File folder = dc.showDialog(null);

		if (folder != null && folder.exists()) {
			final File[] codexItemFiles = folder.listFiles(
					new FileFilter() {
						@Override
						public boolean accept(final File pathname) {
							return pathname.isFile() && pathname.getName().endsWith(".codex");
						}
					});

			this.codexItemJsonFiles = loadCodexFiles(codexItemFiles);
			lstCodexItems.setItems(FXCollections.observableList(codexItemJsonFiles));
		}
	}

	private List<JsonFile<Codex>> loadCodexFiles(final File... codexFiles) {
		ArrayList<JsonFile<Codex>> loadedCodexFiles = new ArrayList<>(codexFiles.length);
		for (final File codexItemFile : codexFiles) {
			try {
				loadedCodexFiles.add(loadCodexFile(codexItemFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return loadedCodexFiles;
	}

	private JsonFile<Codex> loadCodexFile(final File codexItemFile) throws IOException {
		return JsonFile.fromFile(codexItemFile, Codex.class);
	}

	public void addCodexPage(ActionEvent actionEvent) {
		lstPages.itemsProperty().get().add(new SimpleStringProperty("New Page"));
	}

	public void removeCodexPage(ActionEvent actionEvent) {
		lstPages.itemsProperty().get().remove(lstPages.getSelectionModel().getSelectedItem());
	}

	public void saveChanges(ActionEvent actionEvent) {

		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				updateMessage("Jo, geht los!");
				long items = codexItemJsonFiles.size();
				long progress = 0;
				for (JsonFile<Codex> codexJsonFile : codexItemJsonFiles) {
					try {
						codexJsonFile.save();
						progress++;
						updateProgress(progress, items);
					} catch (IOException e) {
						e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
					}
				}
				return null;
			}
		};

		paneMain.disableProperty().bind(task.runningProperty());
		progressbar.progressProperty().bind(task.progressProperty());

		final Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();

	}
}

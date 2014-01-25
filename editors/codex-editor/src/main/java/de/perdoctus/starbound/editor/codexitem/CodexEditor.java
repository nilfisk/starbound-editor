package de.perdoctus.starbound.editor.codexitem;

import de.perdoctus.starbound.editor.codexitem.mapping.Codex;
import de.perdoctus.starbound.editor.core.api.AssetEditor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

/**
 * @author Christoph Giesche
 */
public class CodexEditor extends AssetEditor<Codex> {

	public MenuItem mnuAddPage;
	public MenuItem mnuDeletePage;
	public ListView<StringProperty> lstPages;
	public TextArea txtPageContent;
	public TextField txtCodexId;
	public TextField txtCodexTitle;

	public CodexEditor(final Codex asset) throws Exception {
		super(asset);

		Bindings.bindBidirectional(txtCodexId.textProperty(), asset.idProperty());
		Bindings.bindBidirectional(txtCodexTitle.textProperty(), asset.titleProperty());
		Bindings.bindBidirectional(lstPages.itemsProperty(), asset.contentPagesProperty());

		// Disable delete page menu, if no page is selected.
		mnuDeletePage.disableProperty().bind(lstPages.getSelectionModel().selectedItemProperty().isNull());

		lstPages.setCellFactory(new Callback<ListView<StringProperty>, ListCell<StringProperty>>() {
			@Override
			public ListCell<StringProperty> call(final ListView<StringProperty> param) {
				return new ListCell<StringProperty>() {
					@Override
					protected void updateItem(final StringProperty item, final boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText("Page " + (getIndex() + 1));
						} else {
							setText("");
						}
					}
				};
			}
		});
		lstPages.getSelectionModel().selectedItemProperty().addListener((observableValue, valueBefore, valueNow) -> {
			final boolean wasDirty = isDirty();
			if (valueBefore != null) {
				txtPageContent.textProperty().unbindBidirectional(valueBefore);
			}
			if (valueNow != null) {
				txtPageContent.textProperty().bindBidirectional(valueNow);
			}
			setDirty(wasDirty);
		});

		lstPages.getSelectionModel().selectFirst();

		// Add this as ChangeListener (sets dirty flag)
		txtCodexId.textProperty().addListener(this);
		txtCodexTitle.textProperty().addListener(this);
		txtPageContent.textProperty().addListener(this);
		lstPages.itemsProperty().addListener(this);
	}

	@FXML
	private void addCodexPage(ActionEvent actionEvent) {
		lstPages.getItems().add(new SimpleStringProperty(""));
		setDirty(true);
	}

	@FXML
	private void removeCodexPage(ActionEvent actionEvent) {
		final ObservableList<StringProperty> selectedItems = lstPages.getSelectionModel().getSelectedItems();
		for (StringProperty selectedItem : selectedItems) {
			lstPages.getItems().remove(selectedItem);
		}
		setDirty(true);
	}
}

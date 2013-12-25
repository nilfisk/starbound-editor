package de.perdoctus.starbound.codex;

import de.perdoctus.starbound.base.AssetEditor;
import de.perdoctus.starbound.base.DefaultController;
import de.perdoctus.starbound.types.codex.Codex;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;

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

		lstPages.getSelectionModel().selectFirst();
	}

	public void initialize() {
		mnuDeletePage.disableProperty().bind(lstPages.getSelectionModel().selectedItemProperty().isNull());

		txtCodexId.textProperty().addListener(this);
		txtCodexTitle.textProperty().addListener(this);
		txtPageContent.textProperty().addListener(this);
		lstPages.itemsProperty().addListener(this);

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
		lstPages.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StringProperty>() {
			@Override
			public void changed(final ObservableValue<? extends StringProperty> observableValue, final StringProperty valueBefore, final StringProperty valueNow) {
				final boolean wasDirty = isDirty();
				if (valueBefore != null) {
					txtPageContent.textProperty().unbindBidirectional(valueBefore);
				}
				if (valueNow != null) {
					txtPageContent.textProperty().bindBidirectional(valueNow);
				}
				setDirty(wasDirty);
			}
		});
	}

	public void addCodexPage(ActionEvent actionEvent) {
		lstPages.getItems().add(new SimpleStringProperty());
		setDirty(true);
	}

	public void removeCodexPage(ActionEvent actionEvent) {
		final ObservableList<StringProperty> selectedItems = lstPages.getSelectionModel().getSelectedItems();
		for (StringProperty selectedItem : selectedItems) {
			lstPages.getItems().remove(selectedItem);
		}
		setDirty(true);
	}
}

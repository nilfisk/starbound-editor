package de.perdoctus.starbound.codex;

import de.perdoctus.starbound.base.EditorController;
import de.perdoctus.starbound.types.codexEditor.Codex;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.util.Callback;

/**
 * @author Christoph Giesche
 */
public class CodexEditorController extends EditorController<Codex> {

	public MenuItem mnuAddPage;
	public MenuItem mnuDeletePage;
	public ListView<StringProperty> lstPages;
	public TextArea txtPageContent;
	public TextField txtCodexId;
	public TextField txtCodexTitle;

	public CodexEditorController() throws Exception {
		super();
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

	@Override
	protected Class<Codex> getModelClass() {
		return Codex.class;
	}

	@Override
	protected void modelChanged(final Codex oldModel, final Codex newModel) {
		if (oldModel != null) {
			Bindings.unbindBidirectional(txtCodexId.textProperty(), oldModel.idProperty());
			Bindings.unbindBidirectional(txtCodexTitle.textProperty(), oldModel.titleProperty());
			Bindings.unbindBidirectional(lstPages.itemsProperty(), oldModel.contentPagesProperty());
		}
		if (newModel != null) {
			Bindings.bindBidirectional(txtCodexId.textProperty(), newModel.idProperty());
			Bindings.bindBidirectional(txtCodexTitle.textProperty(), newModel.titleProperty());
			Bindings.bindBidirectional(lstPages.itemsProperty(), newModel.contentPagesProperty());
		}
		lstPages.getSelectionModel().selectFirst();
	}

	public void addCodexPage(ActionEvent actionEvent) {

	}

	public void removeCodexPage(ActionEvent actionEvent) {

	}
}

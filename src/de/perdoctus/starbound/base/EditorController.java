package de.perdoctus.starbound.base;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * @author Christoph Giesche
 */
public abstract class EditorController<M> implements ChangeListener {

	private BooleanProperty dirty = new SimpleBooleanProperty(false);
	private File modelFile;
	private M model;

	/**
	 * Creates a new instance of the Controller.
	 *
	 * @throws Exception When creating a new Instance of the model type fails.
	 */
	public EditorController() throws Exception {
		model = getModelClass().newInstance();
	}

	public BooleanProperty dirtyProperty() {
		return dirty;
	}

	public void load(final File file) throws IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getJsonFactory().configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

		final M oldModel = getModel();

		this.model = objectMapper.readValue(file, getModelClass());
		this.modelFile = file;

		modelChanged(oldModel, getModel());
		setDirty(false);
	}

	public void save() throws IOException {
		save(modelFile);
		setDirty(false);
	}

	public void save(final File file) throws IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(file, model);
		this.modelFile = file;
	}

	/**
	 * @return If the model of this editor is dirty (unsaved changes).
	 */
	public boolean isDirty() {
		return dirty.get();
	}

	/**
	 * @param dirty sets the dirty flag of the model.
	 */
	protected void setDirty(final boolean dirty) {
		this.dirty.set(dirty);
	}

	@Override
	public void changed(final ObservableValue observableValue, final Object o, final Object o2) {
		if (!isDirty()) {
			setDirty(true);
		}
	}

	/**
	 * @return The model.
	 */
	protected M getModel() {
		return model;
	}

	/**
	 * @returns The model's type class of this controller.
	 */
	protected abstract Class<M> getModelClass();

	/**
	 * Is called when the model-instance changes.
	 *
	 * @param oldModel The old model. May be null.
	 * @param newModel The new model.
	 */
	protected abstract void modelChanged(M oldModel, M newModel);
}

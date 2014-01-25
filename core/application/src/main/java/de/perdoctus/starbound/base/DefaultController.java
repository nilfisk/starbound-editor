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
 * @deprecated This is sh***. Extend {@link de.perdoctus.starbound.base.AssetEditor} instead (for Assets).
 */
@Deprecated
public abstract class DefaultController<M> implements ChangeListener {

	private BooleanProperty dirty = new SimpleBooleanProperty(false);
	private File modelFile;
	private M model;

	/**
	 * Creates a new instance of the Controller.
	 */
	public DefaultController() {
		try {
			model = getModelClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public BooleanProperty dirtyProperty() {
		return dirty;
	}

	public void load(final File file) {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getJsonFactory().configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

		final M oldModel = getModel();

		try {
			this.model = objectMapper.readValue(file, getModelClass());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.modelFile = file;

		modelChanged(oldModel, getModel());
		setDirty(false);
	}

	public void save() {
		save(modelFile);
		setDirty(false);
	}

	public void save(final File file) {
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(file, model);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.modelFile = file;
	}

	public File getModelFile() {
		return modelFile;
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
	 * @return The model's type class of this controller.
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

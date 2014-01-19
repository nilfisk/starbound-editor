package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * A de.perdoctus.starbound.base implementation for Asset-Editors.
 *
 * @author Christoph Giesche
 */
public abstract class AssetEditor<T extends Asset> extends AnchorPane implements ChangeListener {

	private T asset; // model
	private BooleanProperty dirty = new SimpleBooleanProperty(false);

	public AssetEditor(final T asset) throws IOException {
		super();
		this.asset = asset;

		final Node view = createView(asset.getAssetType());
		setBottomAnchor(view, 0.0);
		setTopAnchor(view, 0.0);
		setLeftAnchor(view, 0.0);
		setRightAnchor(view, 0.0);
		getChildren().add(view);
	}

	private Node createView(final AssetType assetType) throws IOException {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource(assetType.getEditorFXML()), ResourceBundle.getBundle(assetType.getResourceBundle()));
		loader.setController(this);
		return loader.load();
	}

	/**
	 * @return The asset edited by this editor.
	 */
	public T getAsset() {
		return asset;
	}

	public boolean isDirty() {
		return dirty.get();
	}

	protected void setDirty(final boolean dirty) {
		this.dirty.set(dirty);
	}

	public BooleanProperty dirtyProperty() {
		return dirty;
	}

	@Override
	public void changed(final ObservableValue observable, final Object oldValue, final Object newValue) {
		if (!isDirty()) {
			setDirty(true);
		}
	}
}

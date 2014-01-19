package de.perdoctus.starbound.types.base;

import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.io.File;

/**
 * @author Christoph Giesche
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public abstract class Asset {

	private AssetType assetType;
	private ObjectProperty<File> assetLocation = new SimpleObjectProperty<>(null);
	private AssetOrigin assetOrigin = AssetOrigin.UNKNOWN;
	private BooleanProperty overwritten =  new SimpleBooleanProperty(false);

	/**
	 * @return A human readable title for this asset.
	 */
	public abstract StringExpression assetTitleProperty();

	/**
	 * @return The ID of this asset or {@code null} if there is none.
	 */
	public abstract StringExpression assetIdProperty();

	/**
	 * @return The location of the icon image or {@code null}.
	 */
	public abstract StringExpression iconImageProperty();

	@XmlTransient
	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(final AssetType assetType) {
		this.assetType = assetType;
	}

	@XmlTransient
	public File getAssetLocation() {
		return assetLocation.get();
	}

	public ObjectProperty<File> assetLocationProperty() {
		return assetLocation;
	}

	public void setAssetLocation(final File assetLocation) {
		this.assetLocation.set(assetLocation);
	}

	/**
	 * The origin of this asset.
	 *
	 * @return The origin of this asset.
	 */
	@XmlTransient
	public AssetOrigin getAssetOrigin() {
		return assetOrigin;
	}

	public void setAssetOrigin(final AssetOrigin assetOrigin) {
		this.assetOrigin = assetOrigin;
	}

	/**
	 * Indicates if this asset is overwritten by a modded asset. Can only be true if {@code AssetOrigin} is {@code CORE}.
	 *
	 * @return {@code true} if this asset is overwritten by a mod.
	 */
	@XmlTransient
	public boolean isOverwritten() {
		return overwritten.get();
	}

	public BooleanProperty overwrittenProperty() {
		return overwritten;
	}

	public void setOverwritten(final boolean overwritten) {
		this.overwritten.setValue(overwritten);
	}

}

package de.perdoctus.starbound.types.base;

import javax.xml.bind.annotation.XmlTransient;
import java.io.File;

/**
 * @author Christoph Giesche
 */
public abstract class Asset {

	private EditorType editorType;
	private File assetFile;

	/**
	 * @return A human readable title for this asset.
	 */
	public abstract String assetTitle();

	/**
	 * @return The ID of this asset or {@null} if there is none.
	 */
	public abstract String assetId();

	/**
	 * @return The location of the icon image or {@code null}.
	 */
	public abstract String iconImage();

	@XmlTransient
	public EditorType getEditorType() {
		return editorType;
	}

	public void setEditorType(final EditorType editorType) {
		this.editorType = editorType;
	}

	@XmlTransient
	public File getAssetFile() {
		return assetFile;
	}

	public void setAssetFile(final File assetFile) {
		this.assetFile = assetFile;
	}
}

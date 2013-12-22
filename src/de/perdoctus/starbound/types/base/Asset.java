package de.perdoctus.starbound.types.base;

import java.io.File;

/**
 * @author Christoph Giesche
 */
public class Asset {

	private final EditorType editorType;
	private final File baseDirectory;
	private final String assetLocation;

	public Asset(final EditorType editorType, final File baseDirectory, final String assetLocation) {
		this.editorType = editorType;
		this.baseDirectory = baseDirectory;
		this.assetLocation = assetLocation;
	}

	public File getBaseDirectory() {
		return baseDirectory;
	}

	public EditorType getEditorType() {
		return editorType;
	}

	public String getAssetLocation() {
		return assetLocation;
	}

	@Override
	public String  toString() {
		return "Asset{" +
				"editorType=" + editorType +
				", baseDirectory=" + baseDirectory +
				", assetLocation='" + assetLocation + '\'' +
				'}';
	}
}

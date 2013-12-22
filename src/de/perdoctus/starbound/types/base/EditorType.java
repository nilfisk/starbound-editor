package de.perdoctus.starbound.types.base;

/**
 * @author Christoph Giesche
 */
public class EditorType {

	private String fileSuffix;
	private String editorFXML;
	private String resourceBundle;
	private AssetGroup assetGroup;

	public EditorType() {
	}

	public EditorType(final String fileSuffix, final String editorFXML, final String resourceBundle) {
		this.fileSuffix = fileSuffix;
		this.editorFXML = editorFXML;
		this.resourceBundle = resourceBundle;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(final String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getEditorFXML() {
		return editorFXML;
	}

	public void setEditorFXML(final String editorFXML) {
		this.editorFXML = editorFXML;
	}

	public String getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(final String resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public AssetGroup getAssetGroup() {
		return assetGroup;
	}

	public void setAssetGroup(final AssetGroup assetGroup) {
		this.assetGroup = assetGroup;
	}

	@Override
	public String toString() {
		return "EditorType{" +
				"fileSuffix='" + fileSuffix + '\'' +
				", editorFXML='" + editorFXML + '\'' +
				", resourceBundle='" + resourceBundle + '\'' +
				", assetGroup=" + assetGroup +
				'}';
	}
}

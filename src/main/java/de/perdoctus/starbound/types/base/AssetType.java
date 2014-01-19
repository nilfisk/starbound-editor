package de.perdoctus.starbound.types.base;

/**
 * @author Christoph Giesche
 */
public class AssetType {

	private String assetTitle;
	private String fileSuffix;
	private String editorFXML;
	private String editorClass;
	private String resourceBundle;
	private AssetGroup assetGroup;
	private String assetClass;

	public AssetType() {
	}

	public String getAssetTitle() {
		return assetTitle;
	}

	public void setAssetTitle(final String assetTitle) {
		this.assetTitle = assetTitle;
	}

	public String getEditorClass() {
		return editorClass;
	}

	public void setEditorClass(final String editorClass) {
		this.editorClass = editorClass;
	}

	public String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(final String assetClass) {
		this.assetClass = assetClass;
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
		return "AssetType{" +
				"fileSuffix='" + fileSuffix + '\'' +
				", editorFXML='" + editorFXML + '\'' +
				", editorClass='" + editorClass + '\'' +
				", resourceBundle='" + resourceBundle + '\'' +
				", assetGroup=" + assetGroup +
				", assetClass='" + assetClass + '\'' +
				'}';
	}
}

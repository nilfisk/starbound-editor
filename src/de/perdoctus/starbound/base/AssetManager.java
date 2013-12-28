package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetType;
import de.perdoctus.starbound.types.base.utils.FileUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Christoph Giesche
 */
public class AssetManager {

	private final List<AssetType> knownAssetTypes;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public AssetManager(final List<AssetType> knownAssetTypes) {
		this.knownAssetTypes = knownAssetTypes;

		// since  some of Starbound's JSON files are not fully spec conform.
		objectMapper.getJsonFactory().configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		// to produce human readable JSON.
		objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

	}

	public boolean isKnown(final File file) {
		return detectEditorType(file) != null;
	}

	/**
	 * Detects and returns the AssetType compatible with the given file.
	 */
	private AssetType detectEditorType(final File file) {
		if (file.isFile()) {
			for (AssetType knownAssetType : knownAssetTypes) {
				if (file.getName().endsWith(knownAssetType.getFileSuffix())) {
					return knownAssetType;
				}
			}
		}
		return null;
	}

	/**
	 * Reloads the given asset from disk.
	 * @param asset The asset to reload.
	 * @return The reloaded asset.
	 */
	public void reloadAsset(final Asset asset) throws IOException {
		objectMapper.readerForUpdating(asset).readValue(asset.getAssetLocation());
	}

	/**
	 * Loads the {@link de.perdoctus.starbound.types.base.Asset} for the given file.
	 * @param assetFile The file to load the asset from.
	 * @return The {@link de.perdoctus.starbound.types.base.Asset} or {@code null} if not compatible.
	 */
	public Asset loadAsset(final File assetFile) throws IOException {
		final AssetType assetAssetType = detectEditorType(assetFile);
		if (assetAssetType == null) {
			return null;
		}
		final Class<?> assetClass;
		try {
			assetClass = Class.forName(assetAssetType.getAssetClass());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}


		final Asset asset = (Asset) objectMapper.readValue(assetFile, assetClass);
		asset.setAssetLocation(assetFile);
		asset.setAssetType(assetAssetType);

		return asset;
	}

	public void saveAsset(final File assetFile, final Asset asset) throws IOException {
		final File assetFolder = assetFile.getParentFile();
		if (!assetFolder.exists() || !assetFolder.isDirectory()) {
			assetFolder.mkdirs();
		}
		objectMapper.writeValue(assetFile, asset);
	}
}

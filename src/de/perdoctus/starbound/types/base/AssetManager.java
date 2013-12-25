package de.perdoctus.starbound.types.base;

import com.sun.xml.internal.ws.developer.SerializationFeature;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Christoph Giesche
 */
public class AssetManager {

	private final File coreAssetsDirectory;
	private final List<EditorType> knownEditorTypes;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public AssetManager(final File coreAssetsDirectory, final List<EditorType> knownEditorTypes) {
		this.coreAssetsDirectory = coreAssetsDirectory;
		this.knownEditorTypes = knownEditorTypes;

		// since  some of Starbound's JSON files are not fully spec conform.
		objectMapper.getJsonFactory().configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		// to produce human readable JSON.
		objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

	}

	public boolean isKnown(final File file) {
		return detectEditorType(file) != null;
	}

	/**
	 * Detects and returns the EditorType compatible with the given file.
	 */
	private EditorType detectEditorType(final File file) {
		if (file.isFile()) {
			for (EditorType knownEditorType : knownEditorTypes) {
				if (file.getName().endsWith(knownEditorType.getFileSuffix())) {
					return knownEditorType;
				}
			}
		}
		return null;
	}

	/**
	 * Loads the {@link de.perdoctus.starbound.types.base.Asset} for the given file.
	 * @param assetFile The file to load the asset from.
	 * @return The {@link de.perdoctus.starbound.types.base.Asset} or {@code null} if not compatible.
	 */
	public Asset loadAsset(final File assetFile) throws IOException {
		final EditorType assetEditorType = detectEditorType(assetFile);
		if (assetEditorType == null) {
			return null;
		}
		final Class<?> aClass;
		try {
			aClass = Class.forName(assetEditorType.getAssetClass());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		final Asset asset = (Asset) objectMapper.readValue(assetFile, aClass);
		asset.setAssetFile(assetFile);
		asset.setEditorType(assetEditorType);

		return asset;
	}
}

package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetManager;
import de.perdoctus.starbound.types.base.AssetType;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Scans a given
 *
 * @author Christoph Giesche
 */
public class SupportedAssetsScanTask extends Task<List<Asset>> {

	private final File assetsFolder;
	private final List<AssetType> assetTypes;
	private final AssetManager assetManager;

	/**
	 * @param assetsFolder The folder, the scan will be start at.
	 * @param assetTypes A list containing all EditorTypes, supported files will be searched for.
	 */
	public SupportedAssetsScanTask(final File assetsFolder, final List<AssetType> assetTypes) {
		this.assetTypes = assetTypes;
		this.assetsFolder = assetsFolder;
		this.assetManager = new AssetManager(assetTypes);
	}

	@Override
	protected List<Asset> call() throws Exception {
		if (assetsFolder.exists() || assetsFolder.isDirectory()) {
			return scanDirectory(assetsFolder);
		} else {
			throw new RuntimeException("Folder '" + assetsFolder.getAbsolutePath() + "' does not exist.");
		}
	}

	private List<Asset> scanDirectory(final File directory) {
		updateTitle("Scanning Assets");
		final List<Asset> result = new LinkedList<>();
		for (final File file: directory.listFiles()) {
			updateMessage(file.getAbsolutePath());
			if (file.isDirectory()) {
				result.addAll(scanDirectory(file));
			} else {
				if (assetManager.isKnown(file)) {
					try {
						result.add(assetManager.loadAsset(file));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}


}

package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetOrigin;
import de.perdoctus.starbound.types.base.Mod;
import de.perdoctus.starbound.types.base.Settings;
import de.perdoctus.starbound.types.base.utils.FileUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

/**
 * @author Christoph Giesche
 */
public class MainViewModel {

	private Settings settings;
	private ObjectProperty<Mod> activeMod = new SimpleObjectProperty<>();
	private ListProperty<Asset> assets = new SimpleListProperty<>(FXCollections.<Asset>observableArrayList());

	public MainViewModel() {
		assets.addListener(new ListChangeListener<Asset>() {
			@Override
			public void onChanged(final Change<? extends Asset> change) {
				while (change.next()) {
					if (change.wasAdded()) {
						for (final Asset addedAsset : change.getAddedSubList()) {
							if (addedAsset.getAssetOrigin() == AssetOrigin.MOD) {
								detectAndUpdateOverwrittenState(addedAsset);
							}
						}
					}
					if (change.wasRemoved()) {

					}
				}
			}
		});
	}

		private void detectAndUpdateOverwrittenState(final Asset newCoreAsset, final ObservableList<Asset> modAssets) {

	}

	private void detectAndUpdateOverwrittenState(final Asset modAsset) {
		final File modAssetsDirectory = activeMod.getValue().getModLocation();
		final File coreAssetsDirectory = new File(settings.getStarboundHome() + File.separatorChar + "assets");
		final ObservableList<Asset> coreAssets = assets.filtered(asset -> asset.getAssetOrigin() == AssetOrigin.CORE);
		final String modAssetRelPath = FileUtils.relativize(modAssetsDirectory, modAsset.getAssetLocation());
		for (Asset coreAsset : coreAssets) {
			final String coreAssetRelPath = FileUtils.relativize(coreAssetsDirectory, coreAsset.getAssetLocation());

			if (modAssetRelPath.equals(coreAssetRelPath)) {
				coreAsset.setOverwritten(true);
			}
		}
	}

	public ObservableList<Asset> getAssets() {
		return assets.get();
	}

	public ListProperty<Asset> assetsProperty() {
		return assets;
	}

	public void setAssets(final ObservableList<Asset> assets) {
		this.assets.set(assets);
	}

	public Mod getActiveMod() {
		return activeMod.get();
	}

	public ObjectProperty<Mod> activeModProperty() {
		return activeMod;
	}

	public void setActiveMod(final Mod activeMod) {
		this.activeMod.set(activeMod);
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(final Settings settings) {
		this.settings = settings;
	}
}

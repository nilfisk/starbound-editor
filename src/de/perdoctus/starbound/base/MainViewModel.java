package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetOrigin;
import de.perdoctus.starbound.types.base.Mod;
import de.perdoctus.starbound.types.base.Settings;
import de.perdoctus.starbound.types.base.utils.FileUtils;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.File;

/**
 * @author Christoph Giesche
 */
public class MainViewModel {

	private Settings settings;
	private ObjectProperty<Mod> activeMod = new SimpleObjectProperty<>();
	private ListProperty<Asset> coreAssets = new SimpleListProperty<>(FXCollections.<Asset>observableArrayList());
	private ListProperty<Asset> modAssets = new SimpleListProperty<>(FXCollections.<Asset>observableArrayList());

	public MainViewModel() {

	}

	public ObservableList<Asset> getModAssets() {
		return modAssets.get();
	}

	public void setModAssets(final ObservableList<Asset> modAssets) {
		this.modAssets.set(modAssets);
	}

	public ListProperty<Asset> modAssetsProperty() {
		return modAssets;
	}

	public ObservableList<Asset> getCoreAssets() {
		return coreAssets.get();
	}

	public void setCoreAssets(final ObservableList<Asset> coreAssets) {
		this.coreAssets.set(coreAssets);
	}

	public ListProperty<Asset> coreAssetsProperty() {
		return coreAssets;
	}

	public Mod getActiveMod() {
		return activeMod.get();
	}

	public void setActiveMod(final Mod activeMod) {
		this.activeMod.set(activeMod);
	}

	public ObjectProperty<Mod> activeModProperty() {
		return activeMod;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(final Settings settings) {
		this.settings = settings;
	}
}

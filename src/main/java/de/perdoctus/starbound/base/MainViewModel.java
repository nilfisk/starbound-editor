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

	private ObjectProperty<Mod> activeMod = new SimpleObjectProperty<>();
	private ObservableList<Asset> assets = FXCollections.observableArrayList();

	public ObservableList<Asset> getAssets() {
		return assets;
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

}

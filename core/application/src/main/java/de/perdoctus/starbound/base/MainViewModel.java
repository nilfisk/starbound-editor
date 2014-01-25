package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.Mod;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

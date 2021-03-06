package de.perdoctus.starbound.editor.core;

import de.perdoctus.starbound.editor.core.mod.mappings.StarboundVersion;
import de.perdoctus.starbound.editor.core.settings.mappings.Settings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Christoph Giesche
 */
public class ApplicationContext {

	private static final ApplicationContext instance = new ApplicationContext();
	private ListProperty<StarboundVersion> starboundVersions = new SimpleListProperty<>(FXCollections.observableArrayList());
	private final Settings settings = new Settings();

	private ApplicationContext() {
		// hidden
	}

	public static ApplicationContext getInstance() {
		return instance;
	}

	public Settings getSettings() {
		return settings;
	}

	public ObservableList<StarboundVersion> getStarboundVersions() {
		return starboundVersions.get();
	}

	public void setStarboundVersions(final ObservableList<StarboundVersion> starboundVersions) {
		this.starboundVersions.set(starboundVersions);
	}

	public ListProperty<StarboundVersion> starboundVersionsProperty() {
		return starboundVersions;
	}
}

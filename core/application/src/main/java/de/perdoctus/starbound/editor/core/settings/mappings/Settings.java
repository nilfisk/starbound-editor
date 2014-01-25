package de.perdoctus.starbound.editor.core.settings.mappings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author Christoph Giesche
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Settings {

	private StringProperty starboundHome = new SimpleStringProperty();

	public String getStarboundHome() {
		return starboundHome.get();
	}

	public void setStarboundHome(final String starboundHome) {
		this.starboundHome.set(starboundHome);
	}

	public StringProperty starboundHomeProperty() {
		return starboundHome;
	}
}

package de.perdoctus.starbound.types.base;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

	public StringProperty starboundHomeProperty() {
		return starboundHome;
	}

	public void setStarboundHome(final String starboundHome) {
		this.starboundHome.set(starboundHome);
	}
}

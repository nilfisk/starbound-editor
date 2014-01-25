package de.perdoctus.starbound.types.base;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author Christoph Giesche
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class StarboundVersion {

	private StringProperty version = new SimpleStringProperty();
	private BooleanProperty current = new SimpleBooleanProperty(false);

	public String getVersion() {
		return version.get();
	}

	public void setVersion(final String version) {
		this.version.set(version);
	}

	public StringProperty versionProperty() {
		return version;
	}

	public boolean getCurrent() {
		return current.get();
	}

	public void setCurrent(final boolean current) {
		this.current.set(current);
	}

	public BooleanProperty currentProperty() {
		return current;
	}

	@Override
	public String toString() {
		return getVersion();
	}
}

package de.perdoctus.starbound.types.base;

import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author Christoph Giesche
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ModInfo {

	private StringProperty			name			= new SimpleStringProperty();
	private StringProperty			version			= new SimpleStringProperty();
	private StringProperty			path			= new SimpleStringProperty();
	private ListProperty<String>	dependencies	= new SimpleListProperty<>();

	public String getName() {
		return name.get();
	}

	public void setName(final String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public String getVersion() {
		return version.get();
	}

	public void setVersion(final String version) {
		this.version.set(version);
	}

	public StringProperty versionProperty() {
		return version;
	}

	public String getPath() {
		return path.get();
	}

	public void setPath(final String path) {
		this.path.set(path);
	}

	public StringProperty pathProperty() {
		return path;
	}

	public List<String> getDependencies() {
		return dependencies.get();
	}

	public ListProperty<String> dependenciesProperty() {
		return dependencies;
	}

	public void setDependencies(final List<String> dependencies) {
		this.dependencies.set(FXCollections.observableArrayList(dependencies));
	}
}

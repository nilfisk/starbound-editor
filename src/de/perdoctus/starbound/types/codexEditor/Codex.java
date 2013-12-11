package de.perdoctus.starbound.types.codexEditor;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph Giesche
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Codex {

	private StringProperty id = new SimpleStringProperty();
	private StringProperty title = new SimpleStringProperty();
	private ListProperty<StringProperty> contentPages;

	public String getId() {
		return id.get();
	}

	public StringProperty idProperty() {
		return id;
	}

	public void setId(final String id) {
		this.id.set(id);
	}

	public String getTitle() {
		return title.get();
	}

	public StringProperty titleProperty() {
		return title;
	}

	public void setTitle(final String title) {
		this.title.set(title);
	}

	public List<String> getContentPages() {
		final List<String> result = new ArrayList<>(contentPages.size());
		final ObservableList<StringProperty> stringProperties = contentPages.get();
		for (StringProperty stringProperty : stringProperties) {
			result.add(stringProperty.getValue());
		}

		return result;
	}

	public ListProperty<StringProperty> contentPagesProperty() {
		return contentPages;
	}

	public void setContentPages(final List<String> contentPages) {
		final List<StringProperty> stringProperties = new ArrayList<>(contentPages.size());
		for (String contentPage : contentPages) {
			stringProperties.add(new SimpleStringProperty(contentPage));
		}
		this.contentPages = new SimpleListProperty<>(FXCollections.observableList(stringProperties));
	}

}

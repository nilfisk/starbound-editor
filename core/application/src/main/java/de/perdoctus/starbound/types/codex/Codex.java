package de.perdoctus.starbound.types.codex;

import de.perdoctus.starbound.types.base.Asset;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph Giesche
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Codex extends Asset {

	private StringProperty id = new SimpleStringProperty();
	private StringProperty title = new SimpleStringProperty();
	private ListProperty<StringProperty> contentPages = new SimpleListProperty<>(
			FXCollections.<StringProperty>observableArrayList());

	public String getId() {
		return id.get();
	}

	public void setId(final String id) {
		this.id.set(id);
	}

	public StringProperty idProperty() {
		return id;
	}

	public String getTitle() {
		return title.get();
	}

	public void setTitle(final String title) {
		this.title.set(title);
	}

	public StringProperty titleProperty() {
		return title;
	}

	public List<String> getContentPages() {
		final List<String> result = new ArrayList<>(contentPages.size());
		final ObservableList<StringProperty> stringProperties = contentPages.get();
		for (StringProperty stringProperty : stringProperties) {
			result.add(stringProperty.getValue());
		}

		return result;
	}

	public void setContentPages(final List<String> contentPages) {
		final List<StringProperty> stringProperties = new ArrayList<>(contentPages.size());
		for (String contentPage : contentPages) {
			stringProperties.add(new SimpleStringProperty(contentPage));
		}
		this.contentPages = new SimpleListProperty<>(FXCollections.observableList(stringProperties));
	}

	public ListProperty<StringProperty> contentPagesProperty() {
		return contentPages;
	}

	@Override
	public StringExpression assetTitleProperty() {
		final StringBinding titleBinding = Bindings.when(idProperty().isNotEmpty().and(titleProperty().isNotEmpty()))
				.then(Bindings.concat(titleProperty(), " (", idProperty(), ")")).otherwise("");
		return titleBinding;
	}

	@Override
	public StringExpression assetIdProperty() {
		return id;
	}

	@Override
	public StringExpression iconImageProperty() {
		return null;
	}
}

package de.perdoctus.starbound.codexEditor.model;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;
import java.io.IOException;

/**
 * @param <T> Mapped content type.
 * @author Christoph Giesche
 */
public class JsonFile<T> {

	private boolean dirty;
	private File file;
	private T content;

	public JsonFile(final File file, final T content) {
		this.file = file;
		this.content = content;
		dirty = true;
	}

	public static <T> JsonFile<T> fromFile(final File source, final Class<T> clazz) throws IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getJsonFactory().configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		final T content = objectMapper.readValue(source, clazz);

		final JsonFile<T> jsonFile = new JsonFile<>(source, content);
		jsonFile.dirty = false;

		return jsonFile;
	}

	public void save() throws IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(file, content);
	}

	public void saveCopy(final File newFile) throws IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		objectMapper.writeValue(newFile, content);
	}

	public T getContent() {
		return content;
	}

	public File getFile() {
		return file;
	}

	public boolean isDirty() {
		return dirty;
	}
}

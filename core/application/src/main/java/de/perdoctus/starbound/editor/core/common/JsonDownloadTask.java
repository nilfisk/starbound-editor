package de.perdoctus.starbound.editor.core.common;

import javafx.concurrent.Task;
import org.codehaus.jackson.map.ObjectMapper;

import java.net.URL;

/**
 * @author Christoph Giesche
 */
public class JsonDownloadTask<T> extends Task<T> {

	private final URL url;
	private final Class<T> clazz;

	public JsonDownloadTask(final URL url, final Class<T> clazz) {
		this.url = url;
		this.clazz = clazz;
	}

	@Override
	protected T call() throws Exception {
		updateTitle("Downloading Updates ...");
		updateMessage(url.toString());

		return new ObjectMapper().readValue(url, clazz);
	}

}

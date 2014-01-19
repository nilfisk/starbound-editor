package de.perdoctus.starbound.types.base.utils;

import java.io.File;
import java.net.URI;

/**
 * @author Christoph Giesche
 */
public final class FileUtils {

	public static String relativize(final File baseDirectory, final File absolutePath) {
		final URI baseUri = baseDirectory.toURI();
		final URI fullUri = absolutePath.toURI();
		final URI relativeUri = baseUri.relativize(fullUri);

		return relativeUri.toString();
	}

}

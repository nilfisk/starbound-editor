package de.perdoctus.starbound.editor.types.base;

import de.perdoctus.starbound.editor.core.mod.mappings.StarboundVersion;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.net.URL;

/**
 * @author Christoph Giesche
 */
public class StarboundVersionTest {

	@Test
	public void testStarboundVersionDeserialization() throws Exception {
		final ObjectMapper objectMapper = new ObjectMapper();
		final URL resource = getClass().getResource("/de/perdoctus/starbound/editor/core/mod/starbound-versions.json");

		// Test, if mapping works.
		objectMapper.readValue(resource, StarboundVersion[].class);
	}

}

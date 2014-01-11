package de.perdoctus.starbound.types.base;

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
		final URL resource = getClass().getResource("/base/starbound-versions.json");

		// Test, if mapping works.
		objectMapper.readValue(resource, StarboundVersion[].class);
	}

}

package de.perdoctus.starbound.types.objecteditor.frames.common;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

/**
 * @author Christoph Giesche
 */
public class DimensionDeserializer extends JsonDeserializer<Dimension> {

	@Override
	public Dimension deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonToken xToken = jp.nextValue();
		Integer x = jp.readValueAs(Integer.class);
		JsonToken yToken = jp.nextValue();
		Integer y = jp.readValueAs(Integer.class);
		jp.nextToken();

		Dimension d = new Dimension(x, y);
		return d;
	}
}

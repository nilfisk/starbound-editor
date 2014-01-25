package de.perdoctus.starbound.types.objecteditor.frames.common;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * @author Christoph Giesche
 */
public class DimensionSerializer extends JsonSerializer<Dimension> {


	@Override
	public void serialize(final Dimension value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
		jgen.writeStartArray();
		jgen.writeNumber(value.getX());
		jgen.writeNumber(value.getY());
		jgen.writeEndArray();
	}


}

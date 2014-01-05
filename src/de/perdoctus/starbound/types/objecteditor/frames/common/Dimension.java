package de.perdoctus.starbound.types.objecteditor.frames.common;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Christoph Giesche
 */
@JsonSerialize(using = DimensionSerializer.class)
@JsonDeserialize(using = DimensionDeserializer.class)
public class Dimension {

	private int x;
	private int y;

	public Dimension() {
		super();
	}

	public Dimension(final int x, final int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Dimension{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}

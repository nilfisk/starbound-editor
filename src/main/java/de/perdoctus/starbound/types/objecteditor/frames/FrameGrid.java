package de.perdoctus.starbound.types.objecteditor.frames;

import de.perdoctus.starbound.types.objecteditor.frames.common.Dimension;

import java.util.List;

/**
 * @author Christoph Giesche
 */
public class FrameGrid {
	private Dimension size;
	private Dimension dimensions;
	private List<String> names;

	public Dimension getSize() {
		return size;
	}

	public void setSize(final Dimension size) {
		this.size = size;
	}

	public Dimension getDimensions() {
		return dimensions;
	}

	public void setDimensions(final Dimension dimensions) {
		this.dimensions = dimensions;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(final List<String> names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return "FrameGrid{" +
				"size=" + size +
				", dimensions=" + dimensions +
				", names=" + names +
				'}';
	}
}

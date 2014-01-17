package de.perdoctus.starbound.types.objecteditor.frames;

/**
 * @author Christoph Giesche
 */
public class FramesDefinition {

	private FrameGrid frameGrid;

	public FramesDefinition() {
		super();
	}

	public FramesDefinition(final FrameGrid frameGrid) {
		super();
		this.frameGrid = frameGrid;
	}

	public FrameGrid getFrameGrid() {
		return frameGrid;
	}

	public void setFrameGrid(final FrameGrid frameGrid) {
		this.frameGrid = frameGrid;
	}

	@Override
	public String toString() {
		return "FramesDefinition{" +
				"frameGrid=" + frameGrid +
				'}';
	}
}

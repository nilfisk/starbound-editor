package de.perdoctus.starbound.types.base;

import java.io.File;

/**
 * @author Christoph Giesche
 */
public class Mod {

	private File modLocation;
	private ModInfo modInfo;

	public File getModLocation() {
		return modLocation;
	}

	public void setModLocation(final File modLocation) {
		this.modLocation = modLocation;
	}

	public ModInfo getModInfo() {
		return modInfo;
	}

	public void setModInfo(final ModInfo modInfo) {
		this.modInfo = modInfo;
	}

	@Override
	public String toString() {
		return "Mod{" +
				"modLocation=" + modLocation +
				", modInfo=" + modInfo +
				'}';
	}
}

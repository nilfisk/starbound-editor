package de.perdoctus.starbound.editor.core.api;

/**
 * @author Christoph Giesche
 */
public enum AssetOrigin {
	/**
	 * Asset's origin is Starbound's asset folder. Assets of this origin should not be changed.
	 */
	CORE,
	/**
	 * Asset's origin is a Mod.
	 */
	MOD,
	/**
	 * Asset's origin is unknown. (May be opened by File->Open)
	 */
	UNKNOWN
}

package de.perdoctus.starbound.editor.core.mod;

import de.perdoctus.starbound.editor.core.mod.mappings.ModInfo;
import javafx.concurrent.Task;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Christoph Giesche
 */
public class ModsScanTask extends Task<List<Mod>> {

	public static final String MODINFO_EXTENSION = ".modinfo";
	private final static Logger LOGGER = Logger.getLogger(ModsScanTask.class.getName());
	private File modsDirectory;

	public ModsScanTask(final File modsDirectory) {
		this.modsDirectory = modsDirectory;
	}

	@Override
	protected List<Mod> call() throws Exception {
		final List<Mod> modList = new LinkedList<>();
		updateTitle("Searching for Mods");
		final File[] files = modsDirectory.listFiles();
		if (files != null) {
			for (final File file : files) {
				updateMessage(file.getAbsolutePath());
				if (file.isDirectory()) {
					try {
						final Mod mod = readMod(file);
						modList.add(mod);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return modList;
	}

	private Mod readMod(final File modFolder) throws IOException {
		final String expectedModinfoPath = modFolder.getAbsolutePath() + File.separatorChar + modFolder.getName() + MODINFO_EXTENSION;
		final File modinfoFile = new File(expectedModinfoPath);
		final Mod result;
		if (modinfoFile.exists()) {
			final ModInfo parsedModInfo = parseModInfo(modinfoFile);
			result = new Mod();
			result.setModLocation(modFolder);
			result.setModInfo(parsedModInfo);
		} else {
			throw new FileNotFoundException(modinfoFile.getAbsolutePath() + " does not exist. Skipping folder.");
		}
		return result;
	}

	private ModInfo parseModInfo(final File modinfoFile) throws IOException {
		LOGGER.info(modinfoFile.toString());
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(modinfoFile, ModInfo.class);
	}
}

package de.perdoctus.starbound.codexEditor;

import de.perdoctus.starbound.base.EditorController;
import de.perdoctus.starbound.types.codexEditor.Codex;

/**
 * @author Christoph Giesche
 */
public class CodexEditorController extends EditorController<Codex> {

	public CodexEditorController() throws Exception {
		super();
	}

	@Override
	protected Class<Codex> getModelClass() {
		return Codex.class;
	}

	@Override
	protected void updateModel() {

	}

	@Override
	protected void updateView() {

	}
}

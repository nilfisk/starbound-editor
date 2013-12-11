package de.perdoctus.starbound.base;

import javafx.scene.Node;

/**
 * @author Christoph Giesche
 */
public class Editor<M> {

	private EditorController<M> controller;
	private Node view;

	public Editor(final EditorController<M> controller, final Node view) {
		this.controller = controller;
		this.view = view;
	}

	public EditorController<M> getController() {
		return controller;
	}

	public Node getView() {
		return view;
	}
}

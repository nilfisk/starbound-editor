package de.perdoctus.starbound.editor.core.controls;

import javafx.beans.binding.BooleanBinding;

/**
 * @author Christoph Giesche
 */
public abstract class Validator<T> {

	public abstract BooleanBinding validate(T value);

}

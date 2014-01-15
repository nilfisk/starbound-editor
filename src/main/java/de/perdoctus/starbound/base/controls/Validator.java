package de.perdoctus.starbound.base.controls;

import javafx.beans.binding.BooleanBinding;

/**
 * @author Christoph Giesche (extern)
 */
public abstract class Validator<T> {

	public abstract BooleanBinding validate(T value);

}

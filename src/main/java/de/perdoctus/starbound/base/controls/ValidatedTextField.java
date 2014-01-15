package de.perdoctus.starbound.base.controls;

import java.util.List;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

/**
 * @author Christoph Giesche
 */
public class ValidatedTextField extends TextField {

	private final BooleanProperty					valid	= new SimpleBooleanProperty();
	private final List<Validator<StringProperty>>	validators;

	public ValidatedTextField(final List<Validator<StringProperty>> validators) {
		super();
		this.validators = validators;
		initValidators();
	}

	public ValidatedTextField(final String text, final List<Validator<StringProperty>> validators) {
		super(text);
		this.validators = validators;
		initValidators();
	}

	private void initValidators() {
		BooleanBinding binding = textProperty().isNotEmpty();
		for (final Validator<StringProperty> validator : validators) {
			binding = binding.and(validator.validate(textProperty()));
		}
		valid.bind(binding);
	}
}

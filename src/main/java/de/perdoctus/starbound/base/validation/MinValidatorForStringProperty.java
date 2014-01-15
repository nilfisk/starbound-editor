package de.perdoctus.starbound.base.validation;

import javafx.beans.property.StringProperty;
import org.hibernate.validator.internal.constraintvalidators.MinValidatorForCharSequence;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Min;

/**
 * @author Christoph Giesche
 */
public class MinValidatorForStringProperty implements ConstraintValidator<Min, StringProperty> {

	private final MinValidatorForCharSequence validator = new MinValidatorForCharSequence();

	@Override
	public void initialize(Min min) {
		validator.initialize(min);
	}

	@Override
	public boolean isValid(StringProperty stringProperty, ConstraintValidatorContext constraintValidatorContext) {
		return validator.isValid(stringProperty.get(), constraintValidatorContext);
	}

}

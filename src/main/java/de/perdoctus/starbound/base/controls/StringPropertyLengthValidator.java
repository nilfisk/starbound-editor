package de.perdoctus.starbound.base.controls;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.StringProperty;

/**
 * @author Christoph Giesche
 */
public class StringPropertyLengthValidator extends Validator<StringProperty> {

	private int	minLength;
	private int	maxLength;

	public StringPropertyLengthValidator(final int minLength, final int maxLength) {
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	@Override
	public BooleanBinding validate(final StringProperty value) {
		final BooleanBinding minLenBinding = value.length().greaterThanOrEqualTo(minLength);
		final BooleanBinding maxLenBinding = value.length().lessThanOrEqualTo(maxLength);

		return minLenBinding.and(maxLenBinding);
	}

}

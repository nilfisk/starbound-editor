package de.perdoctus.starbound.base.controls;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.scene.control.TextField;

/**
 * @author Christoph Giesche
 */
public class ValidatedTextField extends TextField {

	private final BooleanProperty valid     = new SimpleBooleanProperty();
	private       IntegerProperty minLength = new SimpleIntegerProperty(0);
	private       IntegerProperty maxLength = new SimpleIntegerProperty(Integer.MAX_VALUE);
	private       StringProperty  regexp    = new SimpleStringProperty();

	public ValidatedTextField() {
		super();
		initValidation();
	}

	public ValidatedTextField(final String text) {
		super(text);
		initValidation();
	}

	private void initValidation() {
		final BooleanBinding length = textProperty().length().greaterThanOrEqualTo(minLength).and(textProperty().length().lessThanOrEqualTo(maxLength));
		final BooleanBinding regexpBinding = new RegexBinding(textProperty(), regexp);
		valid.bind(length.and(regexpBinding));
	}

	public int getMinLength() {
		return minLength.get();
	}

	public void setMinLength(int minLength) {
		this.minLength.set(minLength);
	}

	public IntegerProperty minLengthProperty() {
		return minLength;
	}

	public int getMaxLength() {
		return maxLength.get();
	}

	public void setMaxLength(int maxLength) {
		this.maxLength.set(maxLength);
	}

	public IntegerProperty maxLengthProperty() {
		return maxLength;
	}

	public String getRegexp() {
		return regexp.get();
	}

	public StringProperty regexpProperty() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp.set(regexp);
	}

	public boolean isValid() {
		return valid.get();
	}

	public ReadOnlyBooleanProperty validProperty() {
		return valid;
	}
}

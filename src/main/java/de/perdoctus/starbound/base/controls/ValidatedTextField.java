package de.perdoctus.starbound.base.controls;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

/**
 * @author Christoph Giesche
 */
public class ValidatedTextField extends TextField {

	private final BooleanProperty valid = new SimpleBooleanProperty();
	private final ObjectProperty<Effect> invalidEffect = new SimpleObjectProperty<>(new DropShadow(5, Color.RED));
	private IntegerProperty minLength = new SimpleIntegerProperty(0);
	private IntegerProperty maxLength = new SimpleIntegerProperty(Integer.MAX_VALUE);
	private StringProperty regexp = new SimpleStringProperty();

	public ValidatedTextField() {
		this("");
	}

	public ValidatedTextField(final String text) {
		super(text);
		initValidation();
	}

	private void initValidation() {
		final BooleanBinding length = textProperty().length().greaterThanOrEqualTo(minLength).and(textProperty().length().lessThanOrEqualTo(maxLength));
		final BooleanBinding regexpBinding = new RegexBinding(textProperty(), regexp);
		valid.bind(length.and(regexpBinding));

		valid.addListener((observableValue, oldValue, newValue) -> {
			if (newValue) {
				setEffect(null);
			} else {
				setEffect(invalidEffect.get());
			}
		});
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

	public void setRegexp(String regexp) {
		this.regexp.set(regexp);
	}

	public StringProperty regexpProperty() {
		return regexp;
	}

	public boolean isValid() {
		return valid.get();
	}

	public ReadOnlyBooleanProperty validProperty() {
		return valid;
	}

	public Effect getInvalidEffect() {
		return invalidEffect.get();
	}

	public void setInvalidEffect(Effect invalidEffect) {
		this.invalidEffect.set(invalidEffect);
	}

	public ObjectProperty<Effect> invalidEffectProperty() {
		return invalidEffect;
	}
}

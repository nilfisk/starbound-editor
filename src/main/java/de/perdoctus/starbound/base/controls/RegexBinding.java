package de.perdoctus.starbound.base.controls;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableStringValue;

/**
 * @author Christoph Giesche
 */
public class RegexBinding extends BooleanBinding {

	private final ObservableStringValue	stringValue;
	private final ObservableStringValue	patternValue;

	public RegexBinding(final ObservableStringValue stringValue, final ObservableStringValue patternValue) {
		this.stringValue = stringValue;
		this.patternValue = patternValue;
		super.bind(stringValue, patternValue);
	}

	@Override
	protected boolean computeValue() {
		final boolean result;

		final String string = stringValue.get();
		final String pattern = this.patternValue.get();

		if (string == null) {
			result = false;
		} else if (pattern == null) {
			result = true;
		} else {
			result = string.matches(pattern);
		}

		return result;
	}
}

package de.perdoctus.starbound.base.controls;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Christoph Giesche
 */
public class StringPropertyLengthValidatorTest {

	@Test
	public void testMinMax() throws Exception {
		final StringPropertyLengthValidator validator = new StringPropertyLengthValidator(5, 10);
		final StringProperty testedStringProperty = new SimpleStringProperty();
		final BooleanBinding validationResult = validator.validate(testedStringProperty);

		Assert.assertFalse(validationResult.get());

		testedStringProperty.set("abcd");
		Assert.assertFalse(validationResult.get());

		testedStringProperty.set("abcds");
		Assert.assertTrue(validationResult.get());

		testedStringProperty.set("eeeeeeeeee");
		Assert.assertTrue(validationResult.get());

		testedStringProperty.set("eeeeeeeeese");
		Assert.assertFalse(validationResult.get());
	}

	@Test
	public void testMinMaxUnlimited() throws Exception {
		final StringPropertyLengthValidator validator = new StringPropertyLengthValidator(-1, Integer.MAX_VALUE);
		final StringProperty testedStringProperty = new SimpleStringProperty();
		final BooleanBinding validationResult = validator.validate(testedStringProperty);

		Assert.assertTrue(validationResult.get());

		testedStringProperty.set("");
		Assert.assertTrue(validationResult.get());

		testedStringProperty.set("rgrgrgrgrggdrgergegr");
		Assert.assertTrue(validationResult.get());
	}
}

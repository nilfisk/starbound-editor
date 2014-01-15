package de.perdoctus.starbound.base.controls;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author Christoph Giesche (extern)
 */
public class ValidatedTextFieldTest {

	@Test
	public void testValidMinMax() throws Exception {
		StringPropertyLengthValidator stringPropertyLengthValidator = new StringPropertyLengthValidator(5,10);
		ValidatedTextField validatedTextField = new ValidatedTextField(Arrays.asList(stringPropertyLengthValidator));

	}
}

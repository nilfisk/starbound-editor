package de.perdoctus.starbound.base.controls;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Christoph Giesche (extern)
 */
public class ValidatedTextFieldTest extends Application {

	@BeforeClass
	public static void initJFX() {
		final Thread thread = new Thread() {
			@Override
			public void run() {
				Application.launch(ValidatedTextFieldTest.class);
			}
		};
		thread.start();
	}

	@Test
	public void testValidMinMax() throws Exception {
		final ValidatedTextField validatedTextField = new ValidatedTextField();
		validatedTextField.setMaxLength(10);
		validatedTextField.setMinLength(5);

		validatedTextField.setText("1");
		Assert.assertFalse(validatedTextField.isValid());

		validatedTextField.setText("12345");
		Assert.assertTrue(validatedTextField.isValid());

		validatedTextField.setText("1234567890");
		Assert.assertTrue(validatedTextField.isValid());

		validatedTextField.setText("12345678901");
		Assert.assertFalse(validatedTextField.isValid());
	}

	@Override
	public void start(Stage stage) throws Exception {
		//noop
	}

	@AfterClass
	public static void shutdownJFX() {
		Platform.exit();
	}
}

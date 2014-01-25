package de.perdoctus.starbound.editor.core.controls;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Christoph Giesche (extern)
 */
public class RegexBindingTest {

	private int changeEvents = 0;

	@Test
	public void testName() throws Exception {
		final StringProperty stringProperty = new SimpleStringProperty();
		final StringProperty pattern = new SimpleStringProperty();
		final BooleanBinding binding = new RegexBinding(stringProperty, pattern);


		binding.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
				changeEvents++;
				System.out.println(aBoolean);
			}
		});

		Assert.assertFalse(binding.get());

		pattern.set(null);
		stringProperty.set("Test");
		Assert.assertTrue(binding.get());

		pattern.set("Test");
		stringProperty.set(null);
		Assert.assertFalse(binding.get());

		pattern.set("H.*");
		stringProperty.set("Hallo");
		Assert.assertTrue(binding.get());

		stringProperty.set("Gestern");
		Assert.assertFalse(binding.get());

		stringProperty.set("Harald");
		Assert.assertTrue(binding.get());

		pattern.set("G.*");
		Assert.assertFalse(binding.get());

		stringProperty.set("Gestern");
		Assert.assertTrue(binding.get());

		Assert.assertEquals("Less change-events than expected.", 7, changeEvents);
	}
}

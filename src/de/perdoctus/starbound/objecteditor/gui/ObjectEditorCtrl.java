package de.perdoctus.starbound.objecteditor.gui;

import com.sun.javafx.collections.ObservableListWrapper;
import de.perdoctus.starbound.types.objecteditor.object.Category;
import de.perdoctus.starbound.types.objecteditor.object.Race;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class ObjectEditorCtrl {

	// ObjectDefinition
	public TextField txtObjectName;
	public ComboBox<Race> comboRace;
	public ComboBox<Category> comboCategory;
	public TextField txtPrice;
	public TextField txtShortDescription;
	public TextField txtDescriptionGeneral;
	public TextField txtDescriptionApex;
	public TextField txtDescriptionAvian;
	public TextField txtDescriptionFloran;
	public TextField txtDescriptionGlitch;
	public TextField txtDescriptionHuman;
	public TextField txtDescriptionHylotl;
	// FramesDefinition
	public TextField txtFrameSizeX;
	public TextField txtFrameSizeY;
	public TextField txtAnimationDimensionsX;
	public TextField txtAnimationDimensionsY;
	public TableColumn tableFrameNames;
	public ComboBox comboRarity;
	public TextField txtFilePath;
	public Label lblImageSize;
	public ScrollPane imageScrollPane;
	public Slider sliderImageZoom;
	public ImageView imageAnimation;

	private FileChooser fileChooser;

	public void initialize() {
		comboRace.setItems(new ObservableListWrapper<>(Arrays.asList(Race.values())));
		comboRace.setValue(Race.generic);

		comboCategory.setItems(new ObservableListWrapper<>(Arrays.asList(Category.values())));
		comboCategory.setValue(Category.decorative);

		sliderImageZoom.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(final ObservableValue<? extends Number> observableValue, final Number number, final Number number2) {
				imageAnimation.setScaleX(number.doubleValue());
				imageAnimation.setScaleY(number.doubleValue());
			}
		});
	}

	public void foo(ActionEvent actionEvent) {
		System.out.println("hallo");
	}

	public void loadImage(ActionEvent actionEvent) {
		if (fileChooser == null) {
			fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));
		}

		final File selectedFile = fileChooser.showOpenDialog(txtFilePath.getScene().getWindow());
		if (selectedFile != null) {
			try {
				final Image image = new Image(new FileInputStream(selectedFile));
				double height = image.getHeight();
				double width = image.getWidth();
				lblImageSize.setText((int)width + " x " + (int) height);
				imageAnimation.setImage(image);
				imageAnimation.setFitWidth(width);
				imageAnimation.setFitHeight(height);
				imageScrollPane.setContent(null);
				imageScrollPane.setContent(imageAnimation);
				txtFilePath.setText(selectedFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
	}
}

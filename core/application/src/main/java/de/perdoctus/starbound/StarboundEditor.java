package de.perdoctus.starbound;

import de.perdoctus.starbound.base.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;
import java.util.logging.LogManager;

public class StarboundEditor extends Application {

	public static void main(String[] args) {

		try {
			LogManager.getLogManager().readConfiguration(StarboundEditor.class.getResourceAsStream("/logging.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/perdoctus/starbound/base/MainView.fxml"), ResourceBundle.getBundle("de.perdoctus.starbound.base.base"));
		final Parent root = loader.load();
		final MainViewController controller = loader.getController();
		primaryStage.setTitle("Starbound Editor");
		primaryStage.setScene(new Scene(root));
		primaryStage.setHeight(768);
		primaryStage.setWidth(1024);
		primaryStage.setOnShown(event -> controller.onShow());
		primaryStage.show();

	}
}

package sample;

import de.perdoctus.starbound.base.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/base/MainView.fxml"), ResourceBundle.getBundle("base.base"));
        Parent root = loader.load();
	    MainViewController controller = loader.getController();
        primaryStage.setTitle("Starbound Editor");
        primaryStage.setScene(new Scene(root));
	    primaryStage.setOnShown(event -> controller.onShow());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

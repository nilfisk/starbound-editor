package sample;

import de.perdoctus.starbound.base.JsonDownloadTask;
import de.perdoctus.starbound.base.MainViewController;
import de.perdoctus.starbound.base.dialogs.ProgressDialog;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
	    final FXMLLoader loader = new FXMLLoader(getClass().getResource("/base/MainView.fxml"), ResourceBundle.getBundle("base.base"));
	    final Parent root = loader.load();
	    final MainViewController controller = loader.getController();
	    primaryStage.setTitle("Starbound Editor");
	    primaryStage.setScene(new Scene(root));
	    primaryStage.setHeight(768);
	    primaryStage.setWidth(1024);
	    primaryStage.show();

	    final JsonDownloadTask<String[]> task = new JsonDownloadTask<>(new URL("http://www.google.de"), String[].class);
	    task.setOnSucceeded(event -> {
		    for (String s : task.getValue()) {
			    System.out.println(s);
		    }
	    });
	    task.setOnFailed(event -> {
		    task.getException().printStackTrace();
	    });

	    ProgressDialog.getInstance().execute(task);

	    //primaryStage.setOnShown(event -> controller.onShow());
	    controller.onShow();


    }


    public static void main(String[] args) {

        launch(args);
    }
}

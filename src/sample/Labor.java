package sample;

import de.perdoctus.starbound.base.dialogs.ProgressDialog;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class Labor extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {

		final Task<String> superTask = new Task<String>() {

			@Override
			protected String call() throws Exception {
				updateTitle("Hallo");
				for (int i = 0; i < 120; i++) {
					updateProgress(i, 119);
					updateMessage("Scanning Starbound Directory " + i);

					Thread.sleep(75);
				}
				return "Hallo";
			}
		};

		superTask.setOnSucceeded(event -> {
			printResult(superTask.getValue());
		});

		ProgressDialog.create().execute(superTask);


	}

	private void printResult(final String value) {
		System.out.println(value);
	}
}

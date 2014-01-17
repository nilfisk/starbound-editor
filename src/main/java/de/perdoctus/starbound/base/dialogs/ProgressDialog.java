package de.perdoctus.starbound.base.dialogs;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.*;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author Christoph Giesche
 */
public class ProgressDialog {

	private final Stage progressDialogStage;
	@FXML
	private Label lblTasks;
	@FXML
	private ProgressIndicator progressBar;
	@FXML
	private Label lblStatusMessage;
	@FXML
	private Label lblStatusDetails;

	private static final ProgressDialog instance = new ProgressDialog();
	private IntegerProperty tasks = new SimpleIntegerProperty(0);
	private final ExecutorService executorService = Executors.newFixedThreadPool(1, runnable -> {
		final Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		return thread;
	});

	private ProgressDialog() {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/perdoctus/starbound/base/ProgressDialog.fxml"));
		final Parent progressDialogView;
		try {
			loader.setController(this);
			progressDialogView = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		final Scene progressDialogScene = new Scene(progressDialogView);
		progressDialogStage = new Stage(StageStyle.UNDECORATED);
		progressDialogStage.setScene(progressDialogScene);
		progressDialogStage.initModality(Modality.APPLICATION_MODAL);
		progressDialogStage.setOnCloseRequest(WindowEvent::consume);

		lblTasks.visibleProperty().bind(tasks.greaterThan(1));
		lblTasks.textProperty().bind(Bindings.concat("Remaining Tasks: ", tasks));
		tasks.addListener((observable, oldValue, newValue) -> {
			if (oldValue.intValue() == 0 && newValue.intValue() == 1) {
				progressDialogStage.show();
			} else if (oldValue.intValue() > 0 && newValue.intValue() == 0) {
				progressDialogStage.close();
			}

		});
	}

	public static ProgressDialog getInstance() {
		return instance;
	}

	@Deprecated
	public ProgressDialog owner(final Window owner) {
		return this;
	}

	public synchronized void execute(final Task task) {
		task.setOnRunning(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(final WorkerStateEvent event) {
				lblStatusMessage.textProperty().bind(task.titleProperty());
				lblStatusDetails.textProperty().bind(task.messageProperty());
				progressBar.progressProperty().bind(task.progressProperty());
			}
		});

		task.runningProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				tasks.set(tasks.get() - 1);
			}
		});

		executorService.submit(task);
		tasks.set(tasks.get() + 1);
	}

}

package de.perdoctus.starbound.base.dialogs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;

import java.io.IOException;

/**
 * @author Christoph Giesche
 */
public class ProgressDialog {

	@FXML
	private ProgressIndicator progressBar;
	@FXML
	private Label lblStatusMessage;
	@FXML
	private Label lblStatusDetails;

	private final Stage progressDialogStage;

	private ProgressDialog() {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/base/ProgressDialog.fxml"));
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
	}

	public static ProgressDialog create() {
		return new ProgressDialog();
	}

	public ProgressDialog owner(final Window owner) {
		progressDialogStage.initOwner(owner);
		progressDialogStage.initModality(Modality.WINDOW_MODAL);
		return this;
	}

	public void execute(final Task task) {
		lblStatusMessage.textProperty().bind(task.titleProperty());
		lblStatusDetails.textProperty().bind(task.messageProperty());
		progressBar.progressProperty().bind(task.progressProperty());
		progressDialogStage.show();

		task.runningProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				progressDialogStage.close();
			}
		});

		final Thread thread = new Thread(task);
		thread.setDaemon(false);
		thread.start();

	}
}

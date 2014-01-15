package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Labor extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {

		final ObservableList<String> stringList = FXCollections.observableArrayList("Hans", "Peter");

		final VBox rootPane = new VBox();
		final HBox listHbox = new HBox();
		final Button addButton = new Button("Add name");
		final ListView<String> listViewOnlyH = new ListView<>();
		final ListView<String> listViewOthers = new ListView<>();
		final TextField textField = new TextField();

		addButton.setOnAction(event -> stringList.add(textField.getText()));

		listHbox.getChildren().addAll(listViewOnlyH, listViewOthers);
		rootPane.getChildren().add(listHbox);
		rootPane.getChildren().add(textField);
		rootPane.getChildren().add(addButton);

		listViewOnlyH.setItems(stringList.filtered(s -> s.startsWith("H")));
		listViewOthers.setItems(stringList.filtered(s -> !s.startsWith("H")));

		primaryStage.setScene(new Scene(rootPane));
		primaryStage.show();
	}

}

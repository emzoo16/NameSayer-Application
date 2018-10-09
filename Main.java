package NameSayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	// This is the main class from which the program is run
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage introStage) throws Exception {

		// This block of code loads and shows the menu Scene as described by the fxml
		// file.
		Parent root = FXMLLoader.load(getClass().getResource("splashScreen.fxml"));
		Scene introScene = new Scene(root);
		introStage.setTitle("NameSayer");
		introStage.setScene(introScene);
		introStage.show();
	}
}

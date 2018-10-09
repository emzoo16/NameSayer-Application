package NameSayer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class splashScreenController implements Initializable {
	/*
	 * FXML variables
	 */

	@FXML
	Button enterButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	/*
	 * This method is called when the enter button is clicked on the splash screen.
	 * It directs the user to the menu screen.
	 */
	@FXML
	public void enterButtonClicked(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("menuScene.fxml"));
		Scene recordingScene = new Scene(root);

		Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		currentStage.setScene(recordingScene);
		currentStage.show();
	}

}

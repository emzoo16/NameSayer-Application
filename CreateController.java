package NameSayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateController implements Initializable {

	/**
	 * FXML variables.
	 */
	static String creationName;
	@FXML
	Button createConfirmButton;

	@FXML
	TextField textField;

	@FXML
	Label recordingLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		creationName = null;
		// TODO Auto-generated method stub

	}

	/**
	 * This is the method that is called when the confirm button on the creation
	 * window is pressed.
	 */
	@FXML
	public void createConfirmButtonClicked(ActionEvent event) {

		try {
			// Removing starting and trailing spaces from the user entered text.
			creationName = (textField.getText()).trim();

			/*
			 * Setting a regex to check if any other characters are used apart from letters,
			 * numbers spaces and underscores
			 */
			Pattern validPattern = Pattern.compile("[^a-zA-Z0-9 _-]");
			
			if (!(validPattern.matcher(creationName).find()) && creationName.length() > 0) {

				/*
				 * In order to retrieve files using bash commands, replace all white space
				 * characters with unused character @.
				 */
				creationName = creationName.replaceAll("\\s", "@");
				/*
				 * Check if the specified name already exists as a file. If not, create a new
				 * file using the given name. Otherwise, check if the user wants to overwrite
				 * the existing creation.
				 */
				if (fileExists(creationName)) {
					// Create the video file for the creation
					createVideo();

					// Change the scene to the recording Closing program scene in order to record the audio for the
					// creation.
					Parent root = FXMLLoader.load(getClass().getResource("recordingScene.fxml"));
					Scene recordingScene = new Scene(root);
					Stage menuStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					menuStage.setScene(recordingScene);
					menuStage.show();

				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText(null);
					alert.setContentText("This creation already exists. Do you want to overwrite?");
					Optional<ButtonType> action = alert.showAndWait();

					// If the user chooses to overwrite the creation, take them back to the
					// recording scene
					// to record their new audio.
					if (action.get() == ButtonType.OK) {
						Parent root = FXMLLoader.load(getClass().getResource("recordingScene.fxml"));
						Scene recordingScene = new Scene(root);
						Stage menuStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						menuStage.setScene(recordingScene);
						menuStage.show();
					}

				}

			} 
			
			else {
				// If the user input contains illegal characters, send an alert to warn the user
				// and ask
				// them to try again.
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText(null);
				alert.setContentText("This is not valid input. Please try again");
				Optional<ButtonType> action = alert.showAndWait();
			}
		} catch (IOException e) {
			recordingLabel.setText("Error!");
		}

	}


	// This method creates the video aspect of the creation using bash commands.
	public void createVideo() {
		String videoCommand = "ffmpeg -f lavfi -i color=c=white:s=320x240:d=5 -vf"
				+ " \"drawtext=fontfile=/path/to/font.ttf:fontsize=20: " + "box=1:boxborderw=5:boxcolor=white@0.25: "
				+ "fontcolor=black:x=(w-text_w)/2:y=(h-text_h)/2:text='" + creationName.replaceAll("@", " ") + "'\" "
				+ creationName + "Video.mp4";
		ProcessBuilder vidBuilder = new ProcessBuilder("bash", "-c", videoCommand);
		try {
			Process vidProcess = vidBuilder.start();
		} catch (IOException e) {
			recordingLabel.setText("Error Creating");
			e.printStackTrace();
		}
	}

	/*
	 * This method iterated through the files in the current directory to check if a
	 * file with a given name is already exists.
	 */
	public boolean fileExists(String name) {
		for (final File fileEntry : new File(".").listFiles()) {
			if (fileEntry.getName().equals(name + ".mp4")) {
				return false;
			}
		}
		return true;
	}
	

}

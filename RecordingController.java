package NameSayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class RecordingController implements Initializable {

	Stage currentStage;

	@FXML
	Label recordingLabel;

	@FXML
	Button startButton;

	@FXML
	Button hearRecordingButton;

	boolean recording = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hearRecordingButton.setDisable(true);
		// TODO Auto-generated method stub

	}

	@FXML
	public void startButtonClicked(ActionEvent event) {

		try {
			recordingLabel.setText("Recording...");
			startButton.setDisable(true);
			recordAudio();
			Thread.sleep(5000);
			hearRecordingButton.setDisable(false);
			recordingLabel.setText("Successfully Recorded");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			recordingLabel.setText("Uh oh, there was an error");
			e.printStackTrace();
		}

	}

	@FXML
	public void hearRecordingButtonClicked(ActionEvent event) throws InterruptedException, IOException {
		AudioClip audioFile = new AudioClip(new File(CreateController.creationName + ".wav").toURI().toString());
		audioFile.play();

		Thread.sleep(5500);
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Do you want to save this creation?");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK) {
			currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			meshFiles();
			Thread.sleep(500);
			removeAudioFile();
			Thread.sleep(500);
			removeVideoFile();

			currentStage.close();

		} else {
			Parent root = FXMLLoader.load(getClass().getResource("recordingScene.fxml"));
			Scene recordingScene = new Scene(root);

			currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setScene(recordingScene);
			currentStage.show();

		}
	}

	public void recordAudio() throws IOException {
		String command = "ffmpeg -f pulse -loglevel quiet -i default -t 5 " + CreateController.creationName + ".wav";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
		recording = true;
		try {
			Process recordingProcess = builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void meshFiles() {
		String meshCommand = "ffmpeg -loglevel quiet -i " + CreateController.creationName + "Video.mp4 -i "
				+ CreateController.creationName + ".wav" + " -shortest -strict -2 " + CreateController.creationName
				+ ".mp4";

		ProcessBuilder meshBuilder = new ProcessBuilder("bash", "-c", meshCommand);

		try {
			Process meshProcess = meshBuilder.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removeAudioFile() {
		String removeAudioCommand = "rm " + CreateController.creationName + ".wav";
		ProcessBuilder removeAudioBuilder = new ProcessBuilder("bash", "-c", removeAudioCommand);
		try {
			Process removeAudioProcess = removeAudioBuilder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removeVideoFile() {
		String removeVideoCommand = "rm " + CreateController.creationName + "Video.mp4";
		ProcessBuilder removeVideoBuilder = new ProcessBuilder("bash", "-c", removeVideoCommand);
		try {
			Process removeVideoProcess = removeVideoBuilder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cleanUp(String name) {
		for (final File fileEntry : new File(".").listFiles()) {
			if (fileEntry.getName().equals(name + "Video.mp4")) {
				removeVideoFile();
			}
			if (fileEntry.getName().equals(name + ".wav")) {
				removeAudioFile();
			}
		}
		
	}
}

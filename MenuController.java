package NameSayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class MenuController implements Initializable {

	/*
	 * FXML variables.
	 */

	public File _curDir = new File(".");
	@FXML
	public ListView<String> listView;

	public static ObservableList<String> _creationList = FXCollections.observableArrayList();

	@FXML
	public Button refreshButton;

	@FXML
	public Button deleteButton;

	@FXML
	public Button playButton;

	@FXML
	public Button createButton;

	@FXML
	public Label statusLabel;

	@FXML
	public MediaView mediaview;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cleanUp(CreateController.creationName);
		refreshList();
		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listView.setPlaceholder(new Label("No Creations Available"));
	}

	/*
	 * This method is called when the delete button is clicked. It creates an alert
	 * for the user to confirm if they want to delete the creation.
	 */
	@FXML
	public void menuDeleteButtonClicked(ActionEvent event) throws IOException {
		if (_creationList.size() > 0) {
			// Creating the confirmation alert
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Confirmation");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to delete?");
			Optional<ButtonType> action = alert.showAndWait();

			if (action.get() == ButtonType.OK) {
				// If there is currently a selected creation on the listview, delete that
				// creation.
				if (listView.getSelectionModel().getSelectedItems() != null) {
					String fileString = listView.getSelectionModel().getSelectedItem();
					for (File fileEntry : _curDir.listFiles()) {
						// Converting between the actual file name and the file name shown to the user.
						if ((fileEntry.getName()).equals(fileString.replaceAll("\\s", "@") + ".mp4")) {
							fileEntry.delete();
							_creationList.remove(fileString);
							statusLabel.setText(fileString + " was successfully deleted... ");
							refreshList();
							cleanUp(CreateController.creationName);
						}
					}
				}
			}
		} else {
			// If there are no creations selected, create an alert to warn the user.
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Confirmation");
			alert.setHeaderText(null);
			alert.setContentText("No creations to delete");
			Optional<ButtonType> action = alert.showAndWait();
		}
	}

	/*
	 * This method is called once the create button is clicked in the menu scene. It
	 * switches the scene to the create Scene.
	 */
	@FXML
	public void menuCreateButtonClicked(ActionEvent event) throws IOException {
		refreshList();
		Parent createSceneParent = FXMLLoader.load(getClass().getResource("createScene.fxml"));
		Scene createScene = new Scene(createSceneParent);

		Stage createStage = new Stage();
		createStage.setScene(createScene);
		createStage.show();
	}

	/*
	 * This method is called once the play button is clicked in the menu scene. It
	 * plays the selected creation in the mediaPlayer.
	 */
	@FXML
	public void menuPlayButtonClicked(ActionEvent event) {

		if (listView.getSelectionModel().getSelectedItem() != null) {
			String fileString = listView.getSelectionModel().getSelectedItem();
			fileString = fileString.replaceAll("\\s", "@") + ".mp4";
			Media media = new Media(new File(fileString).toURI().toString());
			MediaPlayer player = new MediaPlayer(media);
			player.setAutoPlay(true);
			mediaview.setMediaPlayer(player);
			player.play();

		} else {
			// If no creations are selected, create an alert to warn the user.
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Please select a creation to play");
			Optional<ButtonType> action = alert.showAndWait();
		}
	}

	/*
	 * This method refreshes the listView.
	 */
	@FXML
	public void menuRefreshButtonClicked(ActionEvent event) {
		cleanUp(CreateController.creationName);
		refreshList();
	}

	public void refreshList() {
		List<String> list = new ArrayList<String>();
		for (final File fileEntry : _curDir.listFiles()) {
			if (fileEntry.getName().endsWith(".mp4")) {
				String listName = fileEntry.getName().substring(0, fileEntry.getName().length() - 4);
				list.add(listName.replaceAll("@", " "));
			}
		}
		_creationList = FXCollections.observableArrayList(list);
		listView.setItems(_creationList);
	}
	
	/*
	 * This method deletes any audio or video files that have not been deleted.
	 */
	
	public void cleanUp(String name) {
		for (final File fileEntry : new File(".").listFiles()) {
			System.out.println();
			if (fileEntry.getName().equals(name + "Video.mp4")) {
				fileEntry.delete();
			}
			if (fileEntry.getName().equals(name + ".wav")) {
				fileEntry.delete();
			}
		}
	}
	

}

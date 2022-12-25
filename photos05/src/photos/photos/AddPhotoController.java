package photos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photos.Entities.Album;
import photos.Entities.Photo;
import photos.Entities.Tag;
import photos.Entities.User;
import photos.Entities.UserInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;
/**
 * Controller for the AddPhoto scene, dealing with adding a photo to an album.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class AddPhotoController {
	/**
	 * Takes in a user-inputted string that is the name of the photo to be added.
	 */
	@FXML
	private TextField pictureNameInput;
	/**
	 * Triggers photo addition.
	 */
	@FXML
	private Button add;
	/**
	 * Takes in input for the caption of the photo to be added.
	 */
	@FXML
	private TextField captionInput;
	/**
	 * Takes in input for the name of the tag for the photo to be added.
	 */
	@FXML
	private TextField tagInput;
	/**
	 * Takes in user-selected tag type from the current selection of tag types for the photo to be added.
	 */
	@FXML
	private ComboBox tagTypeInput;
	/**
	 * Takes in input to create a new tag type for the photo to be added.
	 */
	@FXML
	private TextField newTypeInput;
	/**
	 * Discards inputted information and changes the scene back.
	 */
	@FXML
	private Button cancel;
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	/**
	 * Information of all users.
	 */
	public static UserInfo userInfoInstance = Photos.infoBase;
	/**
	 * The currently active user.
	 */
	public static User user = userInfoInstance.getCurrentUser();
	/**
	 * The currently active album. The photo will be added to this album.
	 */
	public static Album album = user.getCurrentAlbum();
	/**
	 * Initializes the scene.
	 */
	public void initialize() {
		user = userInfoInstance.getCurrentUser();
		album = user.getCurrentAlbum();
	    ArrayList<String> tagTypeList = user.getTagTypes();
	    for(int i = 0; i < tagTypeList.size(); i++) {
	    	tagTypeInput.getItems().addAll(tagTypeList.get(i));
	    }
	}
	/**
	 * Adds the photo to the currently open album. This method will display a pop-up error if there is an issue in adding the photo,
	 * such as the photo already being in the album, invalid tags, illegal input, etc.
	 * Otherwise, a confirmation pop-up appears, and the photo is added after this confirmation is accepted.
	 * @param event the Add button is pressed
	 * @throws IOException
	 */
	@FXML
	public void addPhoto(ActionEvent event) throws IOException {
		Alert alert1 = new Alert(AlertType.CONFIRMATION);
		alert1.setTitle("Add Photo");
		alert1.setContentText("Do you wish to add this photo?");
		Optional<ButtonType> result1 = alert1.showAndWait();
		if(result1.get() == ButtonType.OK) {
			if(user.tagExists(newTypeInput.getText().trim())) {
				Alert alertT = new Alert(AlertType.ERROR);
			    alertT.setTitle("Tag adding error");
			    alertT.setContentText("Tag type already exists");
			    alertT.showAndWait();
			    return;
			}
			String pName = pictureNameInput.getText().trim();
			File test = new File(pName);
			if(!test.exists()) {
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Invalid input");
				alert2.setContentText("Please enter a valid path name for your photo");
				Optional<ButtonType> result2 = alert2.showAndWait();
			}
			else {
				Photo photo = new Photo(pName);
				for(int i = 0; i < album.getPhotos().size(); i++) {
					Photo p = album.getPhotos().get(i);
					if(pName.equals(p.getName())) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Photo adding error");
						alert.setContentText("Photo already exists in current album");
						Optional<ButtonType> result = alert.showAndWait();
						return;
					}
				}
				
				if(tagTypeInput.getValue() != null && !newTypeInput.getText().equals("")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Too many Fields");
					alert.setContentText("Please provide only 1 tag type field");
					Optional<ButtonType> result = alert.showAndWait();
					tagTypeInput.valueProperty().set(null);
					return;
				}
				if((tagTypeInput.getValue()) == null && newTypeInput.getText().equals("") && !tagInput.getText().equals("")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Empty Fields");
					alert.setContentText("Please provide a tag type");
					Optional<ButtonType> result = alert.showAndWait();
					return;
				}
				if(((tagTypeInput.getValue()) != null || !newTypeInput.getText().equals("")) && tagInput.getText().equals("")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Empty Fields");
					alert.setContentText("Please provide a tag name");
					Optional<ButtonType> result = alert.showAndWait();
					return;
				}
				
				if(!tagInput.getText().trim().equals("")) {
					Tag newTag;
					if(newTypeInput.getText().equals("")) {
						newTag = new Tag((String)tagTypeInput.getValue(), tagInput.getText().trim());
					}
					else {
						user.getTagTypes().add(newTypeInput.getText().trim());
						newTag = new Tag(newTypeInput.getText().trim(), tagInput.getText().trim());
					}
					photo.addTag(newTag);
				}
				if(!captionInput.getText().equals(""))
					photo.setCaption(captionInput.getText().trim());
				user.addPhotoCollection(photo);
				album.addPhoto(user.getPhotoIndex(photo));
				
				switchToOpenAlbum(event);
			}
		}
	}
	/**
	 * Cancels the adding operation and changes the scene back to the currently opened album menu
	 * @param event clicking on the cancel button
	 * @throws IOException
	 */
	@FXML
	public void cancel(ActionEvent event) throws IOException {
		switchToOpenAlbum(event);
	}
	
	/**
	 * Switches the scene back to OpenAlbum.
	 * @param event an event that triggers a cancellation of the add
	 * @throws IOException
	 */
	@FXML
	public void switchToOpenAlbum(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("OpenAlbum.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

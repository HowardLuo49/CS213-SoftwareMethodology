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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photos.Entities.*;

/**
 * Controller for the OpenAlbumController scene, dealing with OpenAlbumController functions.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class OpenAlbumController {
	/**
	 * Title name of album
	 */
	@FXML
	private Label albumNameL;
	/**
	 * The desired album to copy the photo to
	 */
	@FXML
	private TextField copyInput;
	/**
	 * Exiting back to list of albums
	 */
	@FXML
	private Button exitButton;
	/**
	 * Adding a photo
	 */
	@FXML
	private Button addButton;
	/**
	 * Deleting a photo
	 */
	@FXML
	private Button deleteButton;
	/**
	 * Caption of the photo
	 */
	@FXML
	private Label captionL;
	/**
	 * The desired album to move the photo to
	 */
	@FXML
	private TextField moveInput;
	/**
	 * Renaming or giving a photo a caption
	 */
	@FXML
	private Button captionButton;
	/**
	 * Bring up scene to display photo and information
	 */
	@FXML
	private Button displayButton;
	/**
	 * Copy the photo to another album
	 */
	@FXML
	private Button copyButton;
	/**
	 * Caption of the photo
	 */
	@FXML
	private Label selectCaptionL;
	/**
	 * Path of the photo
	 */
	@FXML
	private Label selectPNameL;
	/**
	 * Confirm move the photo to another album
	 */
	@FXML
	private Button moveConfButton;
	/**
	 * Confirm copy the photo to another album
	 */
	@FXML
	private Button copyConfButton;
	/**
	 * Move the photo to another album
	 */
	@FXML
	private Button moveButton;
	/**
	 * Pull up a manual slideshow of all the photos in the current album
	 */
	@FXML
	private Button slideshowButton;
	/**
	 * Name of album to which the photo belongs
	 */
	@FXML
	private Label selectANameL;
	/**
	 * Date of photo
	 */
	@FXML
	private Label selectDateL;
	/**
	 * Album to move/copy to
	 */
	@FXML
	private Label newAlbumL;
	/**
	 * Currently selected photo display
	 */
	@FXML
	private ImageView selectedPhoto;
	/**
	 * List of all photos in album
	 */
	@FXML
	private ListView photoList;
	/**
	 * Add a tag to a photo
	 */
	@FXML
	private Button addTagButton;
	/**
	 * Delete a tag from a photo
	 */
	@FXML
	private Button deleteTagButton;
	/**
	 * Type of tag to add or delete
	 */
	@FXML
	private ComboBox tagTypeInput;
	/**
	 * Name of tag to add or delete
	 */
	@FXML
	private TextField tagNameInput;
	/**
	 * Prompt for tag type
	 */
	@FXML
	private Label tagTypeL;
	/**
	 * Prompt for tag name
	 */
	@FXML
	private Label tagNameL;
	/**
	 * Confirm add tag to photo
	 */
	@FXML
	private Button addConfirmTagButton;
	/**
	 * Confirm delete tag from photo
	 */
	@FXML
	private Button deleteConfirmTagButton;
	/**
	 * New type of tag introduced
	 */
	@FXML
	private TextField newTypeInput;
	/**
	 * New caption input from user
	 */
	@FXML
	private TextField editCaptionInput;
	/**
	 * Confirm changing the caption
	 */
	@FXML
	private Button editCapConfButton;

	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	
	/**
	 * Decides either single image display or slideshow setup
	 */
	private int theaterMode;
	
	/**
	 * User database for all stored information
	 */
	public static UserInfo userInfoInstance = Photos.infoBase;
	
	/**
	 * User database for current active user
	 */
	public static User user = userInfoInstance.getCurrentUser();
	
	/**
	 * User database for current opened album
	 */
	public static Album album = user.getCurrentAlbum();
	
	/**
	 * Stores list of photos
	 */
	ArrayList<Photo> photos = album.getPhotos();
	
	/**
	 * Stores list of photos for viewing purposes
	 */
	public ObservableList<String> obsList;
	
	/**
	 * Initializes the scene
	 * @throws FileNotFoundException
	 */
	public void initialize() throws FileNotFoundException {
		user = userInfoInstance.getCurrentUser();
		album = user.getCurrentAlbum();
		photos = album.getPhotos();
		albumNameL.setText(album.getName());
		//if(photoList != null) {
			updateList();
			displayPhotoInfo();
		//}
	    
	    //tagTypeInput.getItems().addAll("Location", "Person");
	    ArrayList<String> tagTypeList = user.getTagTypes();
	    for(int i = 0; i < tagTypeList.size(); i++) {
	    	tagTypeInput.getItems().addAll(tagTypeList.get(i));
	    }
	    
	   // if(photoList!=null) {
			photoList.getSelectionModel().selectedItemProperty().addListener((Info, oldInfo, newInfo) -> {
				try {
					displayPhotoInfo();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			});
	    //}
	}
	
	/**
	 * Updates the list of photos.
	 */
	public void updateList() throws FileNotFoundException {
		photos = album.getPhotos();
		
		obsList = FXCollections.observableArrayList();
		for(int i = 0; i < photos.size(); i++) {
			if(photos.isEmpty())
				break;
			obsList.add(photos.get(i).getName());
		}
		photoList.setItems(obsList);
		if(!obsList.isEmpty())
			photoList.getSelectionModel().select(0);
		
		if(!photos.isEmpty()) {
			displayPhotoInfo();
		}
		else {
			selectedPhoto.setImage(null);
			selectANameL.setText("");
		    selectDateL.setText("");
		    selectCaptionL.setText("");
		    selectPNameL.setText(""); 
			
		}
	}
	
	/**
	 * Displays correct photos as they are selected
	 * @throws FileNotFoundException
	 */
	public void displayPhotoInfo() throws FileNotFoundException {
		Photo currentPhoto = album.getPhoto((String) photoList.getSelectionModel().getSelectedItem()); 
		if(currentPhoto != null && !photos.isEmpty()) {
			FileInputStream inputstream = new FileInputStream(currentPhoto.getName());
			Image image = new Image(inputstream);
			selectedPhoto.setImage(image);
			
			selectedPhoto.setX(0);
			selectedPhoto.setY(0);
			selectedPhoto.setFitHeight(331); 
		    selectedPhoto.setFitWidth(390); 
		    
		    selectedPhoto.setPreserveRatio(true);
		    selectANameL.setText(album.getName());
		    selectDateL.setText(currentPhoto.getDate());
		    selectCaptionL.setText(currentPhoto.getCaption());
		    selectPNameL.setText(currentPhoto.getName()); 
		}
	}
	
	/**
	 * Exits the scene, returns to the list of albums
	 * @param event After the user presses the exit button
	 * @throws IOException
	 */
	@FXML
	public void exit(ActionEvent event) throws IOException {
		switchToAlbumSelection(event);
	}
	/**
	 * Diverts the user to enter credentials to add a photo to the current album
	 * @param event After the user presses the button to add a photo
	 * @throws IOException
	 */
	@FXML
	public void addPhoto(ActionEvent event) throws IOException {
		switchToAddPhoto(event);
	}
	
	/**
	 * Removes the photo from the current album
	 * @param event After the user presses the button to delete a photo
	 */
	@FXML
	public void deletePhoto(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Photo Deletion");
		alert.setContentText("Do you wish to delete this photo?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			try {
				String name = (String) photoList.getSelectionModel().getSelectedItem();
				photos.remove(album.photoIndex(name));
				album.deletePhoto(album.photoIndex(name));
				user.removePhotoCollection(name);
				updateList();
			}
			catch (Exception exception){
				Alert selError = new Alert(AlertType.ERROR);
				selError.setTitle("Photo Deletion Error");
				selError.setContentText("Please select a photo");
				selError.showAndWait();
			}
		}
	}
	
	/**
	 * Directs the user to a scene with the selected photo and its credentials
	 * @param event After the user presses the button to see an individual display
	 * @throws IOException
	 */
	@FXML
	public void displayPhoto(ActionEvent event) throws IOException {
		theaterMode=7;
		if(!obsList.isEmpty())
			switchToPhotoTheater(event);
	}
	
	/**
	 * Prepares to copy the photo into another album
	 * @param event After the user requests to copy a selected photo
	 */
	@FXML
	public void copyPhoto(ActionEvent event) {
		copyConfButton.setVisible(!copyConfButton.isVisible());
		newAlbumL.setVisible(!newAlbumL.isVisible());
		copyInput.setVisible(!copyInput.isVisible());
		copyInput.clear();
		copyInput.requestFocus();
		
	}
	
	/**
	 * Moves the photo into another album if able
	 * @param event After the user confirms to move a selected photo
	 */
	@FXML
	public void confirmMove(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Move Photo");
		alert.setContentText("Do you wish to move this photo?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			try {
				String name = (String) photoList.getSelectionModel().getSelectedItem();
				System.out.println(name);
				Album destAlbum = user.getAlbum(moveInput.getText().trim());
				if(destAlbum == null) {
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Invalid destination");
					alert2.setContentText("Please enter a valid album");
					Optional<ButtonType> result2 = alert2.showAndWait();
				}
				else if(destAlbum.getName().equalsIgnoreCase(album.getName())) {
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Invalid destination");
					alert2.setContentText("Destination album cannot be current album");
					Optional<ButtonType> result2 = alert2.showAndWait();
				}
				else {
					
					for(int i = 0; i < destAlbum.getPhotos().size(); i++) {
						Photo p = destAlbum.getPhotos().get(i);
						if(name.equalsIgnoreCase(p.getName())) {
							Alert alert1 = new Alert(AlertType.ERROR);
							alert1.setTitle("Photo adding error");
							alert1.setContentText("Photo already exists in destination album");
							Optional<ButtonType> result1 = alert1.showAndWait();
							return;
						}
					}
					
					System.out.println("AI:"+album.getPhoto(name).getName());
					System.out.println("LI:"+user.getPhotoIndex(album.getPhoto(name)));
					
//					destAlbum.addPhoto(album.photoIndex(name));
//					album.deletePhoto(album.photoIndex(name));
					
					destAlbum.addPhoto(user.getPhotoIndex(album.getPhoto(name)));
//					System.out.println("middle");
//					album.deletePhoto(user.getPhotoIndex(album.getPhoto(name)));
					album.deletePhoto(album.photoIndex(name));
					
					moveConfButton.setVisible(!moveConfButton.isVisible());
					newAlbumL.setVisible(!newAlbumL.isVisible());
					moveInput.setVisible(!moveInput.isVisible());
					updateList();
				}

			}
			catch (Exception exception){
				Alert selError = new Alert(AlertType.ERROR);
				selError.setTitle("Photo Moving Error");
				selError.setContentText("Please select a photo");
				selError.showAndWait();
			}

		}	
	}
	
	/**
	 * Copies the photo into another album if able
	 * @param event After the user confirms to copy a selected photo
	 */
	@FXML
	public void confirmCopy(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Copy Photo");
		alert.setContentText("Do you wish to copy this photo?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			try {
				String name = (String) photoList.getSelectionModel().getSelectedItem();
				Album destAlbum = user.getAlbum(copyInput.getText().trim());
				
				if(destAlbum == null) {
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Invalid destination");
					alert2.setContentText("Please enter a valid album");
					Optional<ButtonType> result2 = alert2.showAndWait();
				}
				else if(destAlbum.getName().equalsIgnoreCase(album.getName())) {
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Invalid destination");
					alert2.setContentText("Destination album cannot be current album");
					Optional<ButtonType> result2 = alert2.showAndWait();
				}
				else {
					for(int i = 0; i < destAlbum.getPhotos().size(); i++) {
						Photo p = destAlbum.getPhotos().get(i);
						if(name.equalsIgnoreCase(p.getName())) {
							Alert alert1 = new Alert(AlertType.ERROR);
							alert1.setTitle("Photo adding error");
							alert1.setContentText("Photo already exists in destination album");
							Optional<ButtonType> result1 = alert1.showAndWait();
							return;
						}
					}
					
					destAlbum.addPhoto(user.getPhotoIndex(album.getPhoto(name)));
					user.getPhotoCollection().get(user.getPhotoIndex(album.getPhoto(name))).changeNumAlbumsContaining(1);
					//System.out.println(user.getPhotoCollection().get(album.photoIndex(name)).getNumAlbumsContaining());
					
					copyConfButton.setVisible(!copyConfButton.isVisible());
					newAlbumL.setVisible(!newAlbumL.isVisible());
					copyInput.setVisible(!copyInput.isVisible());
					updateList();
				}

			}
			catch (Exception exception){
				Alert selError = new Alert(AlertType.ERROR);
				selError.setTitle("Photo Copying Error");
				selError.setContentText("Please select a photo");
				selError.showAndWait();
			}
		}
	}
	
	/**
	 * Prepares to copy the photo into another album
	 * @param event After the user requests to copy a selected photo
	 */
	@FXML
	public void movePhoto(ActionEvent event) {
		moveConfButton.setVisible(!moveConfButton.isVisible());
		newAlbumL.setVisible(!newAlbumL.isVisible());
		moveInput.setVisible(!moveInput.isVisible());
		moveInput.clear();
		moveInput.requestFocus();	
	}
	
	/**
	 * Directs the user to a scene where they can interact with a slideshow to go through all the photos
	 * @param event After the user presses the button to see the slideshow display
	 * @throws IOException
	 */
	@FXML
	public void slideshow(ActionEvent event) throws IOException {
		theaterMode=8;
		if(!obsList.isEmpty())
			switchToPhotoTheater(event);
	}
	
	/**
	 * Prepares to edit the caption of a photo
	 * @param event After the user requests to edit a photo's caption
	 */
	@FXML
	public void editCaption(ActionEvent event) {
		editCapConfButton.setVisible(!editCapConfButton.isVisible());
		editCaptionInput.setVisible(!editCaptionInput.isVisible());
		editCaptionInput.clear();
		editCaptionInput.requestFocus();
	}
	
	/**
	 * Confirms the modification of the caption of a photo
	 * @param event After the user confirms to edit a photo's caption
	 */
	@FXML
	public void confirmEditCaption(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Edit Caption");
		alert.setContentText("Do you wish to edit this caption?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			String newCaptionName = editCaptionInput.getText().trim();
			
			editCapConfButton.setVisible(!editCapConfButton.isVisible());
			editCaptionInput.setVisible(!editCaptionInput.isVisible());
			
			try {
				String selectedPhoto = (String) photoList.getSelectionModel().getSelectedItem();
				
				album.getPhotos().get(album.photoIndex(selectedPhoto)).setCaption(newCaptionName);
				updateList();
				
			} catch (Exception e) {
				Alert selError = new Alert(AlertType.ERROR);
				selError.setTitle("Caption Renaming Error");
				selError.setContentText("Please select a photo");
				selError.showAndWait();
			}
			
			
		}	
	}
	
	/**
	 * Prepares to add a tag a photo
	 * @param event After the user requests to add a tag to a photo
	 */
	@FXML
	public void addTag(ActionEvent event) {
		tagTypeL.setVisible(!tagTypeL.isVisible());
		tagTypeInput.setVisible(!tagTypeInput.isVisible());
		tagNameL.setVisible(!tagNameL.isVisible());
		tagNameInput.setVisible(!tagNameInput.isVisible());
		addConfirmTagButton.setVisible(!addConfirmTagButton.isVisible());
		newTypeInput.setVisible(!newTypeInput.isVisible());
		tagNameInput.clear();
		tagTypeInput.requestFocus();
	}
	
	/**
	 * Confirms the addition of a new photo tag
	 * @param event After the user confirms to add a new photo tag
	 */
	@FXML
	public void confirmAddTag(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Tag Addition");
		alert.setContentText("Do you wish to add this tag?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			tagTypeL.setVisible(!tagTypeL.isVisible());
			tagTypeInput.setVisible(!tagTypeInput.isVisible());
			tagNameL.setVisible(!tagNameL.isVisible());
			tagNameInput.setVisible(!tagNameInput.isVisible());
			addConfirmTagButton.setVisible(!addConfirmTagButton.isVisible());
			newTypeInput.setVisible(!newTypeInput.isVisible());
			
			String tagType = (String) tagTypeInput.getValue();
			String newTagType = (String) newTypeInput.getText().trim();
			String tagName = (String) tagNameInput.getText().trim();
			
			Tag newTag;
			if(tagType!=null && !newTagType.equals(""))
			{
				Alert alertT = new Alert(AlertType.ERROR);
			    alertT .setTitle("Tag adding error");
			    alertT .setContentText("Please provide only one tag type");
			    alertT .showAndWait();
			    tagTypeInput.valueProperty().set(null);
			    newTypeInput.clear();
			    return;
			}
			
			if(newTagType.equals("")) {
				newTag = new Tag(tagType, tagName);
			}
			else {
				if(user.tagExists(newTagType)) {
					Alert alertT = new Alert(AlertType.ERROR);
				    alertT .setTitle("Tag adding error");
				    alertT .setContentText("Tag type already exists");
				    alertT .showAndWait();
				    return;
				}
				user.getTagTypes().add(newTagType);
				newTag = new Tag(newTagType, tagName);
			}
			
			if(tagType==null)
				tagType = newTagType;
			
			String selectedPhoto = (String) photoList.getSelectionModel().getSelectedItem();
			if(selectedPhoto == null) {
                Alert selError = new Alert(AlertType.ERROR);
                selError.setTitle("Tag adding error");
                selError.setContentText("Please select a photo");
                selError.showAndWait();
                return;
            }
			Photo p = album.getPhotos().get(album.photoIndex(selectedPhoto));
			boolean dupe = false;
			for(int i = 0; i < p.getTags().size(); i++) {
				if(tagName.equalsIgnoreCase(p.getTags().get(i).getName()) && tagType.equalsIgnoreCase(p.getTags().get(i).getType())) {
					dupe = true;
					break;
				}
			}
			
			if(dupe) {
				Alert selError = new Alert(AlertType.ERROR);
				selError.setTitle("Tag adding error");
				selError.setContentText("Tag already exists");
				selError.showAndWait();
			}
			else {
				try {
					album.getPhotos().get(album.photoIndex(selectedPhoto)).addTag(newTag);
					updateList();
					if(!newTagType.equals(""))
                        tagTypeInput.getItems().addAll(newTagType);
					tagTypeInput.valueProperty().set(null);
					
				} catch (Exception e) {
					Alert selError = new Alert(AlertType.ERROR);
					selError.setTitle("Tag adding error");
					selError.setContentText("Please select a photo");
					selError.showAndWait();
				}
			}
		}	
	}
	
	/**
	 * Prepares to delete a tag a photo
	 * @param event After the user requests to delete a tag to a photo
	 */
	@FXML
	public void deleteTag(ActionEvent event) {
		tagTypeL.setVisible(!tagTypeL.isVisible());
		tagTypeInput.setVisible(!tagTypeInput.isVisible());
		tagNameL.setVisible(!tagNameL.isVisible());
		tagNameInput.setVisible(!tagNameInput.isVisible());
		deleteConfirmTagButton.setVisible(!deleteConfirmTagButton.isVisible());
		tagNameInput.clear();
		tagTypeInput.requestFocus();
	}
		
	/**
	 * Confirms the deletion of a new photo tag
	 * @param event After the user confirms to delete a new photo tag
	 */
	@FXML
	public void confirmDeleteTag(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Tag Deletion");
		alert.setContentText("Do you wish to delete this tag?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			tagTypeL.setVisible(!tagTypeL.isVisible());
			tagTypeInput.setVisible(!tagTypeInput.isVisible());
			tagNameL.setVisible(!tagNameL.isVisible());
			tagNameInput.setVisible(!tagNameInput.isVisible());
			deleteConfirmTagButton.setVisible(!deleteConfirmTagButton.isVisible());
			
			String tagType = (String) tagTypeInput.getValue();
			String tagName = (String) tagNameInput.getText().trim();
			
			try {
				String selectedPhoto = (String) photoList.getSelectionModel().getSelectedItem();
				
				if(album.getPhotos().get(album.photoIndex(selectedPhoto)).tagIndex(tagType, tagName) == -1) {
					Alert selError = new Alert(AlertType.ERROR);
					selError.setTitle("Tag deleting error");
					selError.setContentText("Tag does not exist");
					selError.showAndWait();
				}
				else {
					Photo temp = album.getPhotos().get(album.photoIndex(selectedPhoto));
					temp.getTags().remove(temp.tagIndex(tagType, tagName));
					updateList();
				}
				
			} catch (Exception e) {
				Alert selError = new Alert(AlertType.ERROR);
				selError.setTitle("Tag deleting error");
				selError.setContentText("Please select a photo");
				selError.showAndWait();
			}
		}
	}
	
	/**
	 * Switches the scene to the AlbumSelection scene
	 * @param event an event that exits the opened album
	 * @throws IOException
	 */
	@FXML
	public void switchToAlbumSelection(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumSelection.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Switches the scene to the AddPhoto scene
	 * @param event an event that asks to add a photo to the album
	 * @throws IOException
	 */
	@FXML
	public void switchToAddPhoto(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPhoto.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Switches the scene to the photoTheater scene
	 * @param event an event that asks to view the photo in either display or slideshow format
	 * @throws IOException
	 */
	@FXML
	public void switchToPhotoTheater(ActionEvent event) throws IOException {
		album.setCurrentPhoto((String) photoList.getSelectionModel().getSelectedItem());
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PhotoTheater.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		PhotoTheaterController pTControl = loader.getController();
		pTControl.setTheaterMode(theaterMode);
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

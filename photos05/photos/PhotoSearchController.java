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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.ListView;

import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import photos.Entities.*;

/**
 * Controller for the PhotoSearch scene, dealing with photo search functionalities.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class PhotoSearchController {
	/**
	 * Button that exits the scene.
	 */
	@FXML
	private Button exitButton;
	/**
	 * List of result photos.
	 */
	@FXML
	private ListView photoList;
	/**
	 * Button for creating an album from the search results.
	 */
	@FXML
	private Button createButton;
	/**
	 * Button for searching via tags.
	 */
	@FXML
	private Button searchTagButton;
	/**
	 * Input for the name of the new album that will be created from the search results.
	 */
	@FXML
	private TextField newAlbumName;
	/**
	 * Button for searching via date range.
	 */
	@FXML
	private Button searchDateButton1;
	/**
	 * Input for lower end of date range search.
	 */
	@FXML
	private DatePicker startDateInput;
	/**
	 * Input for upper end of date range search.
	 */
	@FXML
	private DatePicker endDateInput;
	/**
	 * Input for name of first tag.
	 */
	@FXML
	private TextField tag1Input;
	/**
	 * Input for name of second tag.
	 */
	@FXML
	private TextField tag2Input;
	/**
	 * Selection of And/Or, for searching two tags with And/Or logic
	 */
	@FXML
	private ComboBox andOr;
	/**
	 * Input for tag type of first tag.
	 */
	@FXML
	private ComboBox tagTypeInput1;
	/**
	 * Input for tag type of second tag.
	 */
	@FXML
	private ComboBox tagTypeInput2;
	/**
	 * Image associated with currently selected photo.
	 */
	@FXML
	private ImageView selectedPhoto;
	/**
	 * Button to reset selections made in tag search.
	 */
	@FXML
	private Button resetButton;
	
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	/**
	 * Keeps a list of the result items.
	 */
	public ObservableList<String> obsList;
	
	/**
	 * Stores all photos under current user
	 */
	public ArrayList<Photo> photos = new ArrayList<Photo>();
	
	/**
	 * User database for all stored information
	 */
	public static UserInfo userInfoInstance = Photos.infoBase;
	
	/**
	 * Data revolving current active user
	 */
	public static User user = userInfoInstance.getCurrentUser();
	
	/**
	 * Initializes the scene
	 */
	public void initialize() {
		user = userInfoInstance.getCurrentUser();
		andOr.getItems().addAll("And", "Or");
	    ArrayList<String> tagTypeList1 = user.getTagTypes();
	    for(int i = 0; i < tagTypeList1.size(); i++) {
	    	tagTypeInput1.getItems().addAll(tagTypeList1.get(i));
	    }
	    ArrayList<String> tagTypeList2 = user.getTagTypes();
	    for(int i = 0; i < tagTypeList2.size(); i++) {
	    	tagTypeInput2.getItems().addAll(tagTypeList2.get(i));
	    }
	    
	    photoList.getSelectionModel().selectedItemProperty().addListener((Info, oldInfo, newInfo) -> {
			try {
				displayPhotoInfo();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * Updates the list to show appropriate search results
	 * @throws FileNotFoundException
	 */
	public void updateList() throws FileNotFoundException {
		obsList = FXCollections.observableArrayList();
		for(int i = 0; i < photos.size(); i++) {
			if(photos.isEmpty())
				break;
			obsList.add(photos.get(i).getName());
		}
		photoList.setItems(obsList);
		if(!obsList.isEmpty())
			photoList.getSelectionModel().select(0);
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No results");
			alert.setContentText("No photos match the provided criterion");
			Optional<ButtonType> result = alert.showAndWait();
		}
		
		if(!photos.isEmpty()) {
			displayPhotoInfo();
		}
		else {
			selectedPhoto.setImage(null);
		}
		
	}
	
	/**
	 * Displays the currently selected photo
	 * @throws FileNotFoundException
	 */
	public void displayPhotoInfo() throws FileNotFoundException {
		Photo currentPhoto = null;
		for(int i = 0; i < user.getPhotoCollection().size(); i++) {
			if(user.getPhotoCollection().get(i).getName().equals((String) photoList.getSelectionModel().getSelectedItem())) {
				currentPhoto = user.getPhotoCollection().get(i);
				break;
			}
		}
		
		if(currentPhoto != null && !photos.isEmpty()) {
			FileInputStream inputstream = new FileInputStream(currentPhoto.getName());
			Image image = new Image(inputstream);
			selectedPhoto.setImage(image);
			
			selectedPhoto.setX(0);
			selectedPhoto.setY(0);
			selectedPhoto.setFitHeight(348); 
		    selectedPhoto.setFitWidth(240);
		}
	}
	
	/**
	 * Sends the user back to their list of albums
	 * @param event After exit is pressed
	 * @throws IOException
	 */
	@FXML
	public void exit(ActionEvent event) throws IOException {
		switchToAlbumSelection(event);
	}
	
	/**
	 * Creates an album using the photos in the results of the search
	 * @param event After the button to create an album is pressed
	 * @throws FileNotFoundException
	 */
	@FXML
	public void createAlbum(ActionEvent event) throws FileNotFoundException {
		Alert alert1 = new Alert(AlertType.CONFIRMATION);
		alert1.setTitle("Create Album");
		alert1.setContentText("Do you wish to create an album from these photos?");
		Optional<ButtonType> result1 = alert1.showAndWait();
		if(result1.get() == ButtonType.OK) {
			String albumName = newAlbumName.getText().trim();
			
			if(user.albumIndex(albumName) != -1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Album Name already exists");
				alert.setContentText("Please provide a valid album name");
				Optional<ButtonType> result = alert.showAndWait();
			}
			else if(albumName.equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Album Name is empty");
				alert.setContentText("Please provide a valid album name");
				Optional<ButtonType> result = alert.showAndWait();
			}
//			else {
//				Album album = new Album(albumName);
//				for(int i = 0; i < photos.size(); i++) {
//					album.addPhoto(photos.get(i));
//				}
//				user.getAlbums().add(album);
//				updateList();
//			}
			
			//new
			else if (photos.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("No available photos");
				alert.setContentText("No photos to make the album with");
				Optional<ButtonType> result = alert.showAndWait();
			}
			else {
				Album album = new Album(albumName);
				for(int i = 0; i < photos.size(); i++) {
					user.addPhotoCollection(photos.get(i));
					album.addPhoto(user.getPhotoCollection().indexOf(photos.get(i)));
				}
				user.getAlbums().add(album);
				updateList();
			}
			
		}
	}
	
	/**
	 * Searches for photos under the user based on tag conditions
	 * @param event After the button to search by tag is pressed
	 * @throws FileNotFoundException
	 */
	@FXML
	public void searchTag(ActionEvent event) throws FileNotFoundException {
		Alert alert1 = new Alert(AlertType.CONFIRMATION);
		alert1.setTitle("Search By Tag");
		alert1.setContentText("Do you wish to search?");
		Optional<ButtonType> result1 = alert1.showAndWait();
		if(result1.get() == ButtonType.OK) {
			if(andOr.getValue() == null) {
				if(tagTypeInput1.getValue() == null || tag1Input.getText().trim().equals("")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Missing fields");
					alert.setContentText("Please fill out the first tag's information");
					Optional<ButtonType> result = alert.showAndWait();
					andOr.valueProperty().set(null);
				}
				else if(tagTypeInput2.getValue() != null || !tag2Input.getText().trim().equals("")) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Missing fields");
                    alert.setContentText("Please use And/Or, or fill out only the first tag's information");
                    Optional<ButtonType> result = alert.showAndWait();
                    andOr.valueProperty().set(null);
                }
				else {
					String tagType = (String)tagTypeInput1.getValue();
					String tagName = tag1Input.getText().trim();
					Tag t = new Tag(tagType, tagName);
					photos = user.getPhotosByTag(t);
					updateList();
				}
			}
			else if(andOr.getValue().equals("Or")) {
				if(tagTypeInput1.getValue() == null || tag1Input.getText().trim().equals("") || tagTypeInput2.getValue() == null || tag2Input.getText().trim().equals("")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Missing fields");
					alert.setContentText("When using Or, all fields are necessary");
					Optional<ButtonType> result = alert.showAndWait();
					andOr.valueProperty().set(null);
				}
				else {
					String tagType1 = (String)tagTypeInput1.getValue();
					String tagName1 = tag1Input.getText().trim();
					Tag t1 = new Tag(tagType1, tagName1);
					
					String tagType2 = (String)tagTypeInput2.getValue();
					String tagName2 = tag2Input.getText().trim();
					Tag t2 = new Tag(tagType2, tagName2);
					
					photos = user.getPhotosByTagOr(t1, t2);
					updateList();
				}
			}
			else if(andOr.getValue().equals("And")) {
				if(tagTypeInput1.getValue() == null || tag1Input.getText().trim().equals("") || tagTypeInput2.getValue() == null || tag2Input.getText().trim().equals("")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Missing fields");
					alert.setContentText("When using And, all fields are necessary");
					Optional<ButtonType> result = alert.showAndWait();
					andOr.valueProperty().set(null);
				}
				else {
					String tagType1 = (String)tagTypeInput1.getValue();
					String tagName1 = tag1Input.getText().trim();
					Tag t1 = new Tag(tagType1, tagName1);
					
					String tagType2 = (String)tagTypeInput2.getValue();
					String tagName2 = tag2Input.getText().trim();
					Tag t2 = new Tag(tagType2, tagName2);
					
					photos = user.getPhotosByTagAnd(t1, t2);
					updateList();
				}
			}
		}
	}
	
	/**
	 * Searches for photos under the user based on date conditions
	 * @param event After the button to search by date is pressed
	 * @throws FileNotFoundException
	 */
	@FXML
	public void searchDate(ActionEvent event) throws FileNotFoundException {
		Alert alert1 = new Alert(AlertType.CONFIRMATION);
		alert1.setTitle("Search By Date");
		alert1.setContentText("Do you wish to search?");
		Optional<ButtonType> result1 = alert1.showAndWait();
		if(result1.get() == ButtonType.OK) {
			
			LocalDate start = startDateInput.getValue();
			LocalDate end = endDateInput.getValue();
			
			if(start == null || end == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Missing input");
				alert.setContentText("One or more dates are blank");
				Optional<ButtonType> result = alert.showAndWait();
			}
			else if(end.isBefore(start)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid inputs");
				alert.setContentText("Start date should be before end date");
				Optional<ButtonType> result = alert.showAndWait();
			}
			else {
				photos = user.getPhotosByDate(start, end);
				updateList();
			}
		}
	}
	
	/**
	 * Resets tag fields clearing their input contents
	 * @param event After the user clicks reset
	 * @throws IOException
	 */
	@FXML
	public void resetFields(ActionEvent event) throws IOException{
		tagTypeInput1.valueProperty().set(null);
        tagTypeInput2.valueProperty().set(null);
        andOr.valueProperty().set(null);
        tag1Input.clear();
        tag2Input.clear();
	}
	
	/**
	 * Switches the scene to AlbumSelection
	 * @param event An event that exits the search
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
}

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
import photos.Entities.User;
import photos.Entities.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.ListView;

import javafx.scene.control.Label;
/**
 * Controller for the AlbumSelection scene, dealing with album functions.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class AlbumSelectionController {
	/**
	 * User input for the name of an album to be created.
	 */
	@FXML
	private TextField createInput;
	/**
	 * Button that triggers logout.
	 */
	@FXML
	private Button logoutButton;
	/**
	 * Displays the list of albums for the user.
	 */
	@FXML
	private ListView albumList;
	/**
	 * Button that toggles visibility of fields to allow the user to enter a name for the new album.
	 */
	@FXML
	private Button createButton;
	/**
	 * Button that deletes the currently selected album.
	 */
	@FXML
	private Button deleteButton;
	/**
	 * Label with format "Date Range: "
	 */
	@FXML
	private Label dateL;
	/**
	 * Label with format "Number of Photos: "
	 */
	@FXML
	private Label numPhotosL;
	/**
	 * User input for the new name of the currently selected album.
	 */
	@FXML
	private TextField renameInput;
	/**
	 * Button that toggles visibility of field to allow the user to enter an new name that the currently selected album will be renamed to.
	 */
	@FXML
	private Button renameButton;
	/**
	 * Button that opens the currently selected album.
	 */
	@FXML
	private Button openButton;
	/**
	 * Button that changes the scene so the user can access search operations.
	 */
	@FXML
	private Button searchButton;
	/**
	 * The number of photos that the currently selected album has.
	 */
	@FXML
	private Label selectPhotoL;
	/**
	 * The date range of the currently selected album.
	 */
	@FXML
	private Label selectDateL;
	/**
	 * The name of the currently selected album.
	 */
	@FXML
	private Label selectNameL;
	/**
	 * Button that appears during creation state that initiates creation of the album.
	 */
	@FXML
	private Button createConfButton;
	/**
	 * Button that appears during the renaming state that initiates renaming of the album.
	 */
	@FXML
	private Button renameConfButton;
	
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	/**
	 * User database for all stored information.
	 */
	public static UserInfo userInfoInstance = Photos.infoBase;
	/**
	 * Data revolving current active user.
	 */
	public static User user = userInfoInstance.getCurrentUser();
	
	private ObservableList<String> obsList;
	/**
	 * Initializes the scene.
	 */
	public void initialize() {
		user = userInfoInstance.getCurrentUser();
		updateList();
		displayAlbumInfo();
		albumList.getSelectionModel().selectedItemProperty().addListener((Info, oldInfo, newInfo) -> displayAlbumInfo());
	}
	/**
	 * Updates the list of albums.
	 */
	public void updateList() {
		obsList = FXCollections.observableArrayList();
		for(int i = 0; i < user.getAlbums().size(); i++) {
			if(user.getAlbums().isEmpty())
				break;
			obsList.add(user.getAlbums().get(i).getName());
		}
		albumList.setItems(obsList);
		if(!obsList.isEmpty()) {
			albumList.getSelectionModel().select(0);
		}
		else {
			selectNameL.setText("");
			selectPhotoL.setText("");
			selectDateL.setText("");
		}
	}
	/**
	 * Switches the scene to the Login scene.
	 * @param event clicking on the Logout button
	 * @throws IOException
	 */
	@FXML
	public void logout(ActionEvent event) throws IOException {
		switchToLogin(event);
	}
	
	/**
	 * Changes visibility of certain fields to allow the user to input a name for a new album.
	 * Clicking on the Create button again switches the visibilities back to the default state.
	 * @param event clicking on the Create button on the right hand side of album operations
	 */
	@FXML
	public void createAlbum(ActionEvent event) {
		
		dateL.setVisible(!dateL.isVisible());
		numPhotosL.setVisible(!numPhotosL.isVisible());
		selectPhotoL.setVisible(!selectPhotoL.isVisible());
		selectDateL.setVisible(!selectDateL.isVisible());
		selectNameL.setVisible(!selectNameL.isVisible());
		createInput.setVisible(!createInput.isVisible());
		createConfButton.setVisible(!createConfButton.isVisible());
		createInput.clear();
		createInput.requestFocus();
	}
	
	/**
	 * Creates an album with the inputted name. Creates pop-ups for confirmation and errors in album creation.
	 * @param event clicking on the Create button next to the TextField 
	 */
	@FXML
	public void confirmCreate(ActionEvent event) {
		
		String albumName = createInput.getText().trim();
		
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
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Album creation");
			alert.setContentText("Do you wish to create a new album?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {
				dateL.setVisible(!dateL.isVisible());
				numPhotosL.setVisible(!numPhotosL.isVisible());
				selectPhotoL.setVisible(!selectPhotoL.isVisible());
				selectDateL.setVisible(!selectDateL.isVisible());
				selectNameL.setVisible(!selectNameL.isVisible());
				createInput.setVisible(!createInput.isVisible());
				createConfButton.setVisible(!createConfButton.isVisible());
				
				Album album = new Album(albumName);
				user.getAlbums().add(album);
				updateList();
				try {
					User.save(user);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
	}
		
	/**
	 * Deletes the currently selected album. Creates a pop-up for confirmation or if an album was not selected.
	 * @param event
	 */
	@FXML
	public void deleteAlbum(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Album Deletion");
		alert.setContentText("Do you wish to delete this album?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			try {
				ArrayList<Photo> remPhotos = user.getAlbums().get(user.albumIndex((String) albumList.getSelectionModel().getSelectedItem())).getPhotos();
				for(int i=0; i< remPhotos.size(); i++)
				{
					user.removePhotoCollection(remPhotos.get(i).getName());
				}
				user.getAlbums().remove(user.albumIndex((String) albumList.getSelectionModel().getSelectedItem()));
				updateList();
				User.save(user);
			}
			catch (Exception exception){
				Alert selError = new Alert(AlertType.ERROR);
				selError.setTitle("Album Deletion Error");
				selError.setContentText("Please select an album");
				selError.showAndWait();
			}
		}
	}
	
	/**
	 * Changes visibility of certain fields to allow the user to input a new name for the selected album.
	 * Clicking the Rename button again with change the visibilities back to the default.
	 * @param event clicking the Rename button on the right hand side of album operations
	 */
	@FXML
	public void renameAlbum(ActionEvent event) {
		selectNameL.setVisible(!selectNameL.isVisible());
		createInput.clear();
		createInput.setVisible(!createInput.isVisible());
		renameConfButton.setVisible(!renameConfButton.isVisible());
	}
	
	/**
	 * Changes the name of the selected album to the inputted name. Creates pop-ups for confirmation or errors in renaming/selection.
	 * @param event clicking the Rename button next to the TextField
	 */
	@FXML
	public void confirmRename(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Rename Album");
		alert.setContentText("Do you wish to rename this album?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			
			String newAlbumName = createInput.getText().trim();
			
			selectNameL.setVisible(!selectNameL.isVisible());
			createInput.setVisible(!createInput.isVisible());
			renameConfButton.setVisible(!renameConfButton.isVisible());
			
			try {
				String selectedAlbum = (String) albumList.getSelectionModel().getSelectedItem();
				
				if(user.albumIndex(newAlbumName) != -1) {
					Alert selError = new Alert(AlertType.ERROR);
					selError.setTitle("Album Name Already Exists");
					selError.setContentText("Please enter a valid album name");
					selError.showAndWait();
				}
				else {
					user.getAlbums().get(user.albumIndex(selectedAlbum)).setName(newAlbumName);
					updateList();
					User.save(user);
					albumList.getSelectionModel().select(user.albumIndex(newAlbumName));
				}
			} catch (Exception e) {
				Alert selError = new Alert(AlertType.ERROR);
				selError.setTitle("Album Renaming Error");
				selError.setContentText("Please select an Album");
				selError.showAndWait();
			}
		}
	}
	
	/**
	 * Opens the currently selected album.
	 * @param event clicking the Open Album button
	 * @throws IOException
	 */
	@FXML
	public void openAlbum(ActionEvent event) throws IOException {
		if(obsList.isEmpty()) {
			Alert selError = new Alert(AlertType.ERROR);
			selError.setTitle("Album Opening Error");
			selError.setContentText("Please select an Album");
			selError.showAndWait();
		}
		else {
			String selectedAlbum = (String) albumList.getSelectionModel().getSelectedItem();
			user.setCurrentAlbum(selectedAlbum);
			switchToOpenAlbum(event);
		}
	}
	
	/**
	 * Changes the scene to the PhotoSearch scene.
	 * @param event clicking on the Search button
	 * @throws IOException
	 */
	@FXML
	public void search(ActionEvent event) throws IOException {
		switchToPhotoSearch(event);
	}
	/**
	 * Populates the TextLabels with the corresponding information about the currently selected album.
	 */
	public void displayAlbumInfo() {
		Album selectedAlbum = user.getAlbum((String) albumList.getSelectionModel().getSelectedItem()); 
		if(selectedAlbum != null) {
			selectNameL.setText(selectedAlbum.getName());
			selectPhotoL.setText(Integer.toString(selectedAlbum.getNumberOfPhotos()));
			if(selectedAlbum.getNumberOfPhotos() > 0)
				selectDateL.setText(selectedAlbum.getDateRange());
			else
				selectDateL.setText("");
		}
	}
	/**
	 * Switches the scene to the Login scene
	 * @param event logout was triggered
	 * @throws IOException
	 */
	@FXML
	public void switchToLogin(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Switches the scene to the PhotoSearch scene
	 * @param event the Search button was pressed
	 * @throws IOException
	 */
	@FXML
	public void switchToPhotoSearch(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PhotoSearch.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	/**
	 * Switches the scene to the OpenAlbum scene
	 * @param event the Open Album button was pressed
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

package photos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photos.Entities.User;
import photos.Entities.UserInfo;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;

/**
 * Controller for the Admin scene, dealing with admin functions.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class AdminController {
	/**
	 * New username input for the creation of a new user
	 */
	@FXML
	private TextField usernameInput;
	/**
	 * Exits the scene, back into the login page
	 */
	@FXML
	private Button logoutButton;
	/**
	 * List of all registered users
	 */
	@FXML
	private ListView userList;
	/**
	 * Button to create a new user
	 */
	@FXML
	private Button createButton;
	/**
	 * Button to delete an existing user
	 */
	@FXML
	private Button deleteButton;

	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	
	/**
	 * User database for all stored information
	 */
	public static UserInfo userInfoInstance = Photos.infoBase;
	
	/**
	 * Stores list of registered users
	 */
	private ObservableList<String> obsList;
	
	/**
	 * Initializes the scene.
	 */
	public void initialize() {
		updateList();
	}
	/**
	 * Updates the list of usernames.
	 */
	public void updateList() {
		obsList = FXCollections.observableArrayList();
		for(int i = 0; i < userInfoInstance.getUsers().size(); i++) {
			if(userInfoInstance.getUsers().isEmpty())
				break;
			obsList.add(userInfoInstance.getUsers().get(i).getName());
		}
		userList.setItems(obsList);
		if(!obsList.isEmpty())
			userList.getSelectionModel().select(0);
	}
	
	/**
	 * Creates a user given an inputted username. Creates pop-ups for confirmation and errors with user creation.
	 * @param event clicking the Create button
	 */
	@FXML
	public void createUser(ActionEvent event) {
		
		String username = usernameInput.getText().trim();
		
		if(userInfoInstance.userIndex(username) != -1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Username already exists");
			alert.setContentText("Please provide a valid username");
			Optional<ButtonType> result = alert.showAndWait();
		}
		else if(username.equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Username is empty");
			alert.setContentText("Please provide a valid username");
			Optional<ButtonType> result = alert.showAndWait();
		}
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("User Creation");
			alert.setContentText("Do you wish to create this new user?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {
				User user = new User(username);
				userInfoInstance.getUsers().add(user);
				updateList();
			}
		}
		try {
			UserInfo.save(userInfoInstance);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes the selected user. Admin and stock cannot be deleted. Createss pop-ups for confirmation and if there are errors with deletion.
	 * @param event clicking the Delete button
	 */
	@FXML
	public void deleteUser(ActionEvent event) {
		
		//if attempting to delete admin or stock
		if(((String) userList.getSelectionModel().getSelectedItem()).equalsIgnoreCase("Admin")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Illegal Action");
			alert.setContentText("Admin cannot be deleted");
			Optional<ButtonType> result = alert.showAndWait();
			return;
		}
		else if(((String) userList.getSelectionModel().getSelectedItem()).equalsIgnoreCase("stock")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Illegal Action");
			alert.setContentText("stock cannot be deleted");
			Optional<ButtonType> result = alert.showAndWait();
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("User Deletion");
		alert.setContentText("Do you wish to delete this user?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			try {
				String name = (String) userList.getSelectionModel().getSelectedItem();
				
				userInfoInstance.getUsers().remove(userInfoInstance.userIndex(name));
				updateList();
				UserInfo.save(userInfoInstance);
			}
			catch (Exception exception){
				Alert selError = new Alert(AlertType.ERROR);
				selError.setTitle("User Deletion Error");
				selError.setContentText("Please select a user");
				selError.showAndWait();
			}
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
	 * Switches the scene to the Login scene
	 * @param event an event that triggers logout
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
}

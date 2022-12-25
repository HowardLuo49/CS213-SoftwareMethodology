package photos;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import photos.Entities.Album;
import photos.Entities.User;
import photos.Entities.UserInfo;
import photos.Entities.Photo;

/**
 * Controller for the Login scene, dealing with logging in a user.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class LoginController {
	/**
	 * The input for the username to log in with.
	 */
	@FXML
	private TextField usernameInput;
	/**
	 * Button that logs the user in.
	 */
	@FXML
	private Button loginButton;

	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	
	/**
	 * User database for all stored information
	 */
	public static UserInfo userInfoInstance = Photos.infoBase;
	
	/**
	 * Initializes the scene
	 */
	public void initialize() {
		//add admin if not exists
		boolean adminPresent = false;
		for(int i = 0; i < userInfoInstance.getUsers().size(); i++) {
			if(userInfoInstance.getUsers().get(i).getName().equalsIgnoreCase("Admin")) {
				userInfoInstance.setCurrentUser("Admin");
				adminPresent = true;
			}
		}
		if(!adminPresent) {
			User adminUser = new User("Admin");
			userInfoInstance.getUsers().add(adminUser);
		}
		
		//add stock if not exists
		boolean stockPresent = false;
		for(int i = 0; i < userInfoInstance.getUsers().size(); i++) {
			if(userInfoInstance.getUsers().get(i).getName().equalsIgnoreCase("stock")) {
				userInfoInstance.setCurrentUser("stock");
				stockPresent = true;
			}
		}
		if(!stockPresent) {
			User stockUser = new User("stock");
			userInfoInstance.getUsers().add(stockUser);
			userInfoInstance.setCurrentUser("stock");
			
			User u = userInfoInstance.getCurrentUser();
			
			Album stockAlbum = new Album("stock");
			
			Photo a = new Photo("data/wedge.jpg");
			a.setCaption("caption a");
			Photo b = new Photo("data/blue 1.png");
			b.setCaption("caption b");
			Photo c = new Photo("data/blue 2.png");
			c.setCaption("caption c");
			Photo d = new Photo("data/blue 3.jpg");
			d.setCaption("caption d");
			Photo e = new Photo("data/blue 4.jpg");
			e.setCaption("caption e");
			Photo f = new Photo("data/blue 5.jpg");
			f.setCaption("caption f");
			
			ArrayList<Photo> stockPhotos = new ArrayList<>();
			stockPhotos.add(a);
			stockPhotos.add(b);
			stockPhotos.add(c);
			stockPhotos.add(d);
			stockPhotos.add(e);
			stockPhotos.add(f);
			
			for(int i = 0; i < stockPhotos.size(); i++) {
				u.addPhotoCollection(stockPhotos.get(i));
	            stockAlbum.addPhoto(u.getPhotoIndex(stockPhotos.get(i)));
			}
			
			u.getAlbums().add(stockAlbum);
		}
		
		try {
			UserInfo.save(userInfoInstance);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the username is valid, and if so redirects the user into their album list
	 * @param event After log in is pressed by the user
	 * @throws IOException
	 */
	@FXML
	public void login(ActionEvent event) throws IOException{
		
		String username = usernameInput.getText().trim();
		
		if(username.equalsIgnoreCase("admin")) {
			switchToAdmin(event);
		}
		else {
			for(int i = 0; i < userInfoInstance.getUsers().size(); i++) {
				if(userInfoInstance.getUsers().get(i).getName().equalsIgnoreCase(username)) {
					userInfoInstance.setCurrentUser(username);
					switchToAlbumSelection(event);
					return;
				}
			}
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Incorrect Username");
			alert.setContentText("Please provide a valid username");
			Optional<ButtonType> result = alert.showAndWait();
		}
		
		
	}
	
	/**
	 * Switches the scene to AlbumSelection
	 * @param event An event after the user has hit login with a valid username input
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
	 * Switches the scene to Admin
	 * @param event An event after the user has hit login with Admin as the username input
	 * @throws IOException
	 */
	@FXML
	public void switchToAdmin(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

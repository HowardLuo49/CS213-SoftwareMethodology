package photos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photos.Entities.Album;
import photos.Entities.Photo;
import photos.Entities.User;
import photos.Entities.UserInfo;
/**
 * Controller for the PhotoTheater scene, dealing with display and slideshow.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class PhotoTheaterController{
	/**
	 * Exits the scene.
	 */
	@FXML
	private Button exitButton;
	/**
	 * The time of capture of the photo.
	 */
	@FXML
	private Label timeL;
	/**
	 * The caption of the photo.
	 */
	@FXML
	private Label captionL;
	/**
	 * The tags of the photo.
	 */
	@FXML
	private Label tagsL;
	/**
	 * The date of capture of the photo.
	 */
	@FXML
	private Label dateL;
	/**
	 * The image corresponding to the <a href="#{@link}">{@link Photo}</a>
	 */
	@FXML
	private ImageView selectedPhoto;
	/**
	 * Changes the selected photo to the previous photo in the album (invisible during display)
	 */
	@FXML
	private Button leftButton;
	/**
	 * Changes the selected photo to the next photo in the album (invisible during display)
	 */
	@FXML
	private Button rightButton;
	/**
	 * Time: prompt
	 */
	@FXML
	private Label leftTimeL;
	/**
	 * Caption: prompt
	 */
	@FXML
	private Label leftCaptionL;
	/**
	 * Tags: prompt
	 */
	@FXML
	private Label leftTagsL;
	/**
	 * Date: prompt
	 */
	@FXML
	private Label leftDateL;
	
	@FXML
	private Stage stage;
	@FXML
	private Scene scene;
	
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
	 * User database for current viewing photo
	 */
	public static Photo photo = album.getCurrentPhoto();
	
	/**
	 * Stores all photos under current user
	 */
	ArrayList<Photo> photos = album.getPhotos();
	
	/**
	 * Stores the number of the current photo being viewed
	 */
	private int photoNum = album.getCurrentPhotoIndex();
	
	/**
	 * Initializes the scene.
	 * @throws FileNotFoundException
	 */
	public void initialize() throws FileNotFoundException {
		user = userInfoInstance.getCurrentUser();
		album = user.getCurrentAlbum();
		photo = album.getCurrentPhoto();
        photos = album.getPhotos();
        photoNum = album.getCurrentPhotoIndex();
		displayPhotoInfo(photoNum);
	}
	/**
	 * Displays the information of the currently selected photo.
	 * @param index index of the currently selected photo in the album's list of photos
	 * @throws FileNotFoundException
	 */
	public void displayPhotoInfo(int index) throws FileNotFoundException {
		if(index >= 0 && index < album.getPhotos().size()) {
			Photo currentPhoto = album.getPhotos().get(index);
			if(currentPhoto != null && !photos.isEmpty()) {
				FileInputStream inputstream = new FileInputStream(currentPhoto.getName());
				Image image = new Image(inputstream);
				selectedPhoto.setImage(image);
				
				selectedPhoto.setX(0);
				selectedPhoto.setY(0);
				selectedPhoto.setFitHeight(331); 
			    selectedPhoto.setFitWidth(820); 
			    
			    selectedPhoto.setPreserveRatio(true);
			    captionL.setText(currentPhoto.getCaption());
			    dateL.setText(currentPhoto.getDate());
			    timeL.setText(currentPhoto.getTime());
			    
			    String tags = "";
			    for(int i = 0; i < currentPhoto.getTags().size(); i++) {
                    if(i != 0)
                        tags += " | ";
                    tags += currentPhoto.getTags().get(i).getType();
                    tags += ": ";
                    tags += currentPhoto.getTags().get(i).getName();
                }
			    tagsL.setText(tags);
			    
			}
		}
	}
	
	/**
	 * Ends the display/slideshow and changes the scene back to OpenAlbum
	 * @param event clicking on the Exit button
	 * @throws IOException
	 */
	@FXML
	public void exit(ActionEvent event) throws IOException {
		switchToOpenAlbum(event);
	}
	/**
	 * Changes the currently selected photo to the previous photo in the album (slideshow)
	 * @param event clicking on the left arrow button
	 * @throws FileNotFoundException
	 */
	@FXML
	public void left(ActionEvent event) throws FileNotFoundException {
		if(photoNum > 0) {
			photoNum--;
			displayPhotoInfo(photoNum);
		}
	}
	/**
	 * Changes the currently selected photo to the next photo in the album (slideshow)
	 * @param event clicking on the right arrow button
	 * @throws FileNotFoundException
	 */	@FXML
	public void right(ActionEvent event) throws FileNotFoundException {
		if(photoNum < album.getPhotos().size() - 1) {
			photoNum++;
			displayPhotoInfo(photoNum);
		}
	}
	/**
	 * Changes the scene to OpenAlbum
	 * @param event an event that ends display/slideshow
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
	/**
	 * Sets specific fields to visible or invisible depending on whether PhotoTheater is called for display or slideshow
	 * @param mode int that determines if the mode is display(7) or slideshow(not 7)
	 */
	public void setTheaterMode(int mode)
	{
		boolean modeType = mode==7;
		timeL.setVisible(modeType);
		captionL.setVisible(modeType);
		tagsL.setVisible(modeType);
		dateL.setVisible(modeType);
		leftTimeL.setVisible(modeType);
		leftCaptionL.setVisible(modeType);
		leftTagsL.setVisible(modeType);
		leftDateL.setVisible(modeType);

		rightButton.setVisible(!modeType);
		leftButton.setVisible(!modeType);
	}
}

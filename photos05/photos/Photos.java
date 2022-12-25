package photos;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photos.Entities.UserInfo;

/**
 * Main class.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class Photos extends Application {
	
	/**
	 * Central database containing all information to be stored
	 */
	public static UserInfo infoBase = new UserInfo();
	
	public Stage stage;
	
	/**
	 * Runs the application, saves all user data upon closing.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(root,650,400);
			scene.getStylesheets().add(getClass().getResource("photos.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent w) {
				try {
					UserInfo.save(infoBase);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Loads all stored user data into the current session
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			infoBase = UserInfo.load();
		} catch (ClassNotFoundException e) {
			
		} catch (IOException e) {
			
		} catch(ClassCastException e) {
	}
		
		launch(args);
	}
}

package photos.Entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * Class that is responsible for storing information about all users.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class UserInfo implements Serializable{
	/**
	 * <code>String</code> used for saving user information.
	 */
	public static final String storeDirectory = "data";
	/**
	 * <code>String</code> used for saving user information.
	 */
	public static final String storeFile = "users.data";
	/**
	 * <code>Integer</code> that represents the index of the currently active user in <a href="#{@link}">{@link users}</a>.
	 */
	private int currentUserIndex = -1;
	/**
	 * <code>ArrayList</code> of <a href="#{@link}">{@link User}</a>s that holds all the users.
	 */
	private ArrayList<User> users = new ArrayList<User>();
	/**
	 * Given a user’s name, assigns that user as the current active user. Adjusts currentUserIndex accordingly.
	 * @param name the username of the specified user
	 */
	public void setCurrentUser(String name) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getName().equalsIgnoreCase(name)) {
				currentUserIndex = i;
				return;
			}
		}
	}
	/**
	 * Returns an arraylist of users currently registered in the system.
	 * @return value of <a href="#{@link}">{@link users}</a>
	 */
	public ArrayList<User> getUsers() {
		return users;
	}
	/**
	 * Returns the index in the arraylist of users of the current active user.
	 * @return value of <a href="#{@link}">{@link currentUserIndex}</a>
	 */
	public int getCurrentUserIndex() {
		return currentUserIndex;
	}
	/**
	 * Returns the current active user.
	 * @return the <a href="#{@link}">{@link User}</a> associated with <a href="#{@link}">{@link currentUserIndex}</a>
	 */
	public User getCurrentUser() {
		return users.get(currentUserIndex);
	}
	/**
	 * Given a user’s name, returns the matching user from the arraylist of users.
	 * @param username the specified username
	 * @return the <a href="#{@link}">{@link User}</a> with the corresponding name, null if the user does not exist
	 */
	public User getUser(String username) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getName().equalsIgnoreCase(username)) {
				return users.get(i);
			}
		}
		return null;
	}
	/**
	 * Given a user’s name, returns the matching index in the arraylist of users.
	 * @param username the specified username
	 * @return the index of the corresponding <a href="#{@link}">{@link User}</a> in <a href="#{@link}">{@link users}</a>, -1 if it does not exist
	 */
	public int userIndex(String username) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getName().equalsIgnoreCase(username))
				return i;
		}
		return -1;
	}
	/**
	 * Serializes all the current session’s information and stores it into a data file.
	 * @param pdApp user information
	 * @throws IOException
	 */
	public static void save(UserInfo pdApp) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDirectory + File.separator + storeFile));
		oos.writeObject(pdApp);
		oos.close();
	}
	/**
	 * Reads all the information stored in the data file and brings it into the current session.
	 * @return the list of users
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static UserInfo load() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDirectory + File.separator + storeFile));
		UserInfo userList = (UserInfo) ois.readObject();
		ois.close();
		return userList;
	}
}
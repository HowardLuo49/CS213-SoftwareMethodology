package photos.Entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/**
 * Class that is responsible for the information and behaviors associated with a user.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class User implements Serializable{
	/**
	 * <code>String</code> used for saving user information.
	 */
	public static final String storeDirectory = "data";
	/**
	 * <code>String</code> used for saving user information.
	 */
	public static final String storeFile = "users.data";
	/**
	 * <code>Integer</code> that represents the index of the currently selected album in <a href="#{@link}">{@link albums}</a>.
	 */
	private int currentAlbumIndex = -1;
	/**
	 * <code>String</code> that represents the username of the user.
	 */
	private String name = "";
	/**
	 * <code>ArrayList</code> of <a href="#{@link}">{@link Album}</a>s that holds the albums associated with the user.
	 */
	private ArrayList<Album> albums = new ArrayList<Album>();
	/**
	 * <code>ArrayList</code> of <code>String</code>s that holds the tag types associated with the user.
	 * Used to keep track of custom-created tags for the user.
	 */
	private ArrayList<String> tagTypes = new ArrayList<>();
	/**
	 * <code>ArrayList</code> of <a href="#{@link}">{@link Photos}</a>s that holds the unique photos associated with the user.
	 * <a href="#{@link}">{@link Album}</a> references photos from this <code>ArrayList</code>, so changes to a photo are reflected
	 * across all albums.
	 */
	private ArrayList<Photo> photoCollection = new ArrayList<Photo>();
	/**
	 * This methods returns the tag types associated with the user.
	 * @return value of <a href="#{@link}">{@link tagTypes}</a>
	 */
	public ArrayList<String> getTagTypes() {
		return this.tagTypes;
	}
	/**
	 * This method sets the current album to the specified album.
	 * @param name the name of the specified album
	 */
	public void setCurrentAlbum(String name) {
		for(int i = 0; i < albums.size(); i++) {
			if(albums.get(i).getName().equalsIgnoreCase(name)) {
				currentAlbumIndex = i;
				return;
			}
		}
	}
	/**
	 * This methods returns the current album.
	 * @return <a href="#{@link}">{@link Album}</a> associated with the value of <a href="#{@link}">{@link currentAlbumIndex}</a> 
	 */
	public Album getCurrentAlbum() {
		return albums.get(currentAlbumIndex);
	}
	/**
	 * This method finds a specific album. 
	 * @param name the name of the specified album
	 * @return <a href="#{@link}">{@link Album}</a> with the specified name, null if the album does not exist
	 */
	public Album getAlbum(String name) {
		for(int i = 0; i < albums.size(); i++) {
			if(albums.get(i).getName().equalsIgnoreCase(name)) {
				return albums.get(i);
			}
		}
		return null;
	}
	/**
	 * Constructor that creates a user with the given username.
	 * The default tag types for a user are "Location" and "Person"
	 * @param name the specified username of the user to be created
	 */
	public User(String name) {
		this.name = name;
		this.tagTypes.add("Location");
		this.tagTypes.add("Person");
	}
	/**
	 * This method returns the username of the user.
	 * @return value of <a href="#{@link}">{@link name}</a>
	 */
	public String getName() {
		return name;
	}
	/**
	 * This method changes the username of the user.
	 * @param namne the specified username
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * This method returns the albums associated with the user.
	 * @return <code>ArrayList</code> of <a href="#{@link}">{@link Album}</a>s
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}
	/**
	 * This method returns the index of the specified album.
	 * @param albumName the name of the specified album
	 * @return the index of the corresponding album in <a href="#{@link}">{@link albums}</a>, or -1 if it does not exist
	 */
	public int albumIndex(String albumName) {
		for(int i = 0; i < albums.size(); i++) {
			if(albums.get(i).getName().equalsIgnoreCase(albumName))
				return i;
		}
		return -1;
	}
	/**
	 * This method returns all photos associated with the user that fall within a specified range of dates.
	 * @param start lower limit of the date range
	 * @param end upper limit of the date range
	 * @return <code>ArrayList</code> of <a href="#{@link}">{@link Photos}</a>s that have a date that falls within the specified range
	 */
	public ArrayList<Photo> getPhotosByDate(LocalDate start, LocalDate end) {
		ArrayList<Photo> result = new ArrayList<Photo>();
		
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.set(Calendar.MILLISECOND,0);
		endDate.set(Calendar.MILLISECOND,0);
		
		startDate.set(start.getYear(), start.getMonthValue() - 1, start.getDayOfMonth());
		endDate.set(end.getYear(), end.getMonthValue() - 1,  end.getDayOfMonth());
		for(int i = 0; i < photoCollection.size(); i++) {
			Photo photo = photoCollection.get(i);
			if(!photo.getName().equals("")) {
				Date date = photo.getUnformattedDate();
				Calendar photoDate = Calendar.getInstance();
				photoDate.set(Calendar.MILLISECOND,0);
				photoDate.setTime(date);
					
				Calendar comparisonDate = Calendar.getInstance();
				comparisonDate.set(Calendar.MILLISECOND,0);
					
				int year = photoDate.get(Calendar.YEAR);
				int month = photoDate.get(Calendar.MONTH);
				int day = photoDate.get(Calendar.DAY_OF_MONTH);
					
				comparisonDate.set(year, month, day);
				
				if((comparisonDate.compareTo(startDate) > 0 && comparisonDate.compareTo(endDate) < 0) || comparisonDate.equals(startDate) || comparisonDate.equals(endDate))
					result.add(photo);
			}
		}
		return result;
	}
	/**
	 * This method returns all photos associated with the user that have the specified <a href="#{@link}">{@link Tag}</a>.
	 * @param tag specified <a href="#{@link}">{@link Tag}</a>
	 * @return <code>ArrayList</code> of <a href="#{@link}">{@link Photos}</a>s that have the corresponding <a href="#{@link}">{@link Tag}</a>
	 */
	public ArrayList<Photo> getPhotosByTag(Tag tag) {
		ArrayList<Photo> result = new ArrayList<Photo>();
		for(int i = 0; i < photoCollection.size(); i++) {
				Photo photo = photoCollection.get(i);
				if(!photo.getName().equals("")) {
					for(int k = 0; k < photo.getTags().size(); k++) {
						Tag t = photo.getTags().get(k);
						if(t.getType().equalsIgnoreCase(tag.getType()) && t.getName().equalsIgnoreCase(tag.getName())) {
							result.add(photo);
							break;
						}
					}
				}
		}
		return result;
	}
	/**
	 * This method returns all photos associated with the user that have both specified <a href="#{@link}">{@link Tag}</a>s.
	 * @param tag1 a specified <a href="#{@link}">{@link Tag}</a>
	 * @param tag2 a specified <a href="#{@link}">{@link Tag}</a>
	 * @return <code>ArrayList</code> of <a href="#{@link}">{@link Photos}</a>s that have both corresponding <a href="#{@link}">{@link Tag}</a>s
	 */
	public ArrayList<Photo> getPhotosByTagAnd(Tag tag1, Tag tag2) {
		ArrayList<Photo> result = new ArrayList<Photo>();
		for(int i = 0; i < photoCollection.size(); i++) {
				Photo photo = photoCollection.get(i);
				if(!photo.getName().equals("")) {
					boolean tag1Satisfied = false;
					boolean tag2Satisfied = false;
					for(int k = 0; k < photo.getTags().size(); k++) {
						Tag t = photo.getTags().get(k);
						if(t.getType().equalsIgnoreCase(tag1.getType()) && t.getName().equalsIgnoreCase(tag1.getName())) {
							tag1Satisfied = true;
						}
						if(t.getType().equalsIgnoreCase(tag2.getType()) && t.getName().equalsIgnoreCase(tag2.getName())) {
							tag2Satisfied = true;
						}
					}
					if(tag1Satisfied && tag2Satisfied)
						result.add(photo);
				}
		}
		return result;
	}
	/**
	 * This method returns all photos associated with the user that have either of the specified <a href="#{@link}">{@link Tag}</a>s.
	 * @param tag1 a specified <a href="#{@link}">{@link Tag}</a>
	 * @param tag2 a specified <a href="#{@link}">{@link Tag}</a>
	 * @return <code>ArrayList</code> of <a href="#{@link}">{@link Photos}</a>s that have either of the corresponding <a href="#{@link}">{@link Tag}</a>s
	 */
	public ArrayList<Photo> getPhotosByTagOr(Tag tag1, Tag tag2) {
		ArrayList<Photo> result = new ArrayList<Photo>();
		for(int i = 0; i < photoCollection.size(); i++) {
				Photo photo = photoCollection.get(i);
				if(!photo.getName().equals("")) {
					boolean tag1Satisfied = false;
					boolean tag2Satisfied = false;
					for(int k = 0; k < photo.getTags().size(); k++) {
						Tag t = photo.getTags().get(k);
						if(t.getType().equalsIgnoreCase(tag1.getType()) && t.getName().equalsIgnoreCase(tag1.getName())) {
							tag1Satisfied = true;
						}
						if(t.getType().equalsIgnoreCase(tag2.getType()) && t.getName().equalsIgnoreCase(tag2.getName())) {
							tag2Satisfied = true;
						}
					}
					if(tag1Satisfied || tag2Satisfied)
						result.add(photo);
				}
		}
		return result;
	}
	/**
	 * Serializes all the current session’s information and stores it into a data file.
	 * @param pdApp instace of user information
	 * @throws IOException
	 */
	public static void save(User pdApp) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDirectory + File.separator + storeFile));
		oos.writeObject(pdApp);
		oos.close();
	}
	/**
	 * This method returns the unique photos associated with the user.
	 * @return the value of <a href="#{@link}">{@link photoCollection}</a>
	 */
	public ArrayList<Photo> getPhotoCollection(){
		return photoCollection;
	}
	/**
	 * This method updates <a href="#{@link}">{@link photoCollection}</a> when a photo is added to an album.
	 * If a photo is added with a new caption or new tags to an album, the photo in <a href="#{@link}">{@link photoCollection}</a> is updated
	 * to reflect these changes. <a href="#{@link}">{@link Photo#numAlbumsContaining}</a> is incremented by 1, as this method is only ever called when a
	 * photo is being added to an album for the first time.
	 * @param photo the <a href="#{@link}">{@link Photo}</a> that was added
	 */
	public void addPhotoCollection(Photo photo) {
		for(int i = 0; i < photoCollection.size(); i++) {
			Photo currentPhoto = photoCollection.get(i);
			if(currentPhoto.getName().equalsIgnoreCase(photo.getName())) {
				ArrayList<Tag> cTags = currentPhoto.getTags();
				if(photo.getCaption()!=null)
					currentPhoto.setCaption(photo.getCaption());
				if(photo.getTags().isEmpty()) {
					
				}
				else if((!photo.getTags().isEmpty() && cTags.isEmpty()) || (!cTags.isEmpty() && !cTags.contains(photo.getTags().get(0))))
					currentPhoto.addTag(photo.getTags().get(0));
				currentPhoto.changeNumAlbumsContaining(1);
				return;
			}
		}
		photo.changeNumAlbumsContaining(1);
		photoCollection.add(photo);
	}
	/**
	 * This method updates <a href="#{@link}">{@link photoCollection}</a> when a photo is deleted from an album.
	 * This method is run if a photo is deleted from an album or an album containing photos is deleted.
	 * <a href="#{@link}">{@link Photo#numAlbumsContaining}</a> is decremented by 1. If this value hits 0,
	 * then no albums contain the photo, so its information is wiped from <a href="#{@link}">{@link photoCollection}</a>.
	 * This will "reset" the photo, if it is added again in the future.
	 * @param name of the <a href="#{@link}">{@link Photo}</a> that was deleted
	 */
	public void removePhotoCollection(String name) {
		for(int i = 0; i < photoCollection.size(); i++)
		{
			Photo currentPhoto = photoCollection.get(i);
			if(currentPhoto.getName().equalsIgnoreCase(name)) {
				currentPhoto.changeNumAlbumsContaining(-1);
				if(currentPhoto.getNumAlbumsContaining()==0)
					currentPhoto.setName("");
				return;
			}
		}
	}
	/**
	 * This method returns the index of a specified photo in <a href="#{@link}">{@link photoCollection}</a>.
	 * @param photo the specified <a href="#{@link}">{@link Photo}</a>
	 * @return the index that the photo is located at, or -1 if it does not exist.
	 */
	public int getPhotoIndex(Photo photo){
		for(int i = 0; i < photoCollection.size(); i++) {
			Photo currentPhoto = photoCollection.get(i);
			if(currentPhoto.getName().equalsIgnoreCase(photo.getName()))
					return i;
		}
		return -1;
	}
	/**
	 * This method searches for the a specified tag type in <a href="#{@link}">{@link tagTypes}</a>.
	 * @param tagName the specified tag type
	 * @return true if the tag type already exists, false if it does not
	 */
	public boolean tagExists(String tagName) {
	     for(int i = 0; i < tagTypes.size(); i++) {
	          if(tagTypes.get(i).equalsIgnoreCase(tagName))
	               return true;
	     }
	     return false;
	}
}

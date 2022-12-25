package photos.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import photos.Photos;
/**
 * Class that is responsible for the information and behaviors associated with an album.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class Album implements Serializable{
	/**
	 * <code>String</code> that represents the given name of the album.
	 */
	private String name;
	/**
	 * <code>Integer</code> that represents the number of photos that the album contains.
	 */
	private int numberOfPhotos;
	/**
	 * <code>Integer</code> that represents the index of a specific photo in <a href="#{@link}">{@link photos}</a>
	 * @see getCurrentPhoto
	 */
	private int currentPhotoIndex;
	/**
	 * <a href="#{@link}">{@link UserInfo}</a> object that represents the information of all users.
	 * Needed to access the information of the user that owns the album.
	 */
	public UserInfo userInfoInstance = Photos.infoBase;
	/**
	 * <a href="#{@link}">{@link User}</a> object that represents the user that owns the album.
	 */
	public User user = userInfoInstance.getCurrentUser();
	/**
	 * <code>Integer</code> <code>ArrayList</code> that holds references to the photos that the album contains.
	 * The photos are not actually contained in each album, but rather under the user. This <code>ArrayList</code> stores
	 * the index in <a href="#{@link}">{@link User#photoCollection}</a> of each associated photo. 
	 * @see <a href="#{@link}">{@link User#photoCollection}</a>
	 */
	private ArrayList<Integer> photos = new ArrayList<Integer>();
	/**
	 * Constructor that creates an empty album with the specified name.
	 * @param name the desired name of the album
	 */
	public Album(String name) {
		this.name = name;
		this.numberOfPhotos = 0;
	}
	/**
	 * This method adds a reference for a specific photo in <a href="#{@link}">{@link User#photoCollection}</a> to <a href="#{@link}">{@link photos}</a>.
	 * The album now "contains" the specified photo. 
	 * @param photoIndex the index of the specified photo in <a href="#{@link}">{@link User#photoCollection}</a>
	 * @see <a href="#{@link}">{@link User#photoCollection}</a>
	 */
	public void addPhoto(int photoIndex) {
		photos.add(photoIndex);
		numberOfPhotos++;
	}
	/**
	 * This method removes the reference for a specific photo in <a href="#{@link}">{@link User#photoCollection}</a> from <a href="#{@link}">{@link photos}</a>.
	 * The album no longer "contains" the specified photo. 
	 * @param photoIndex the index of the specified photo in <a href="#{@link}">{@link User#photoCollection}</a>
	 * @see <a href="#{@link}">{@link User#photoCollection}</a>
	 */
	public void deletePhoto(int photoIndex) {
		photos.remove(photoIndex);
		numberOfPhotos--;
	}
	/**
	 * This method returns the index in <a href="#{@link}">{@link photos}</a> that is associated with a specific photo.
	 * @param photoName the name of the specified photo
	 * @return the index in <a href="#{@link}">{@link photos}</a> for the specified photo, or -1 if it is not contained in this album
	 */
	public int photoIndex(String photoName) {
		for(int i = 0; i < photos.size(); i++) {
			if(user.getPhotoCollection().get(photos.get(i)).getName().equalsIgnoreCase(photoName))
				return i;
		}
		return -1;
	}
	/**
	 * This method returns a <a href="#{@link}">{@link Photo}</a> with the specified name, if it the album contains it.
	 * @param photoName the specified name of the photo
	 * @return the corresponding <a href="#{@link}">{@link Photo}</a> if the album contains it, null if it does not
	 */
	public Photo getPhoto(String photoName) {
		for(int i = 0; i < photos.size(); i++) {
			if(user.getPhotoCollection().get(photos.get(i)).getName().equals(photoName)) {
				return user.getPhotoCollection().get(photos.get(i));
			}
		}
		return null;
	}
	/**
	 * Method that returns the date range of all the photos contained in the album.
	 * All photos in the album will have a date that falls within this range.
	 * @return <code>String</code> formatted "OldestDate - NewestDate" to represent the date range of the album
	 */
	public String getDateRange() {
		String start = "";
		String end = "";

		Calendar startDate = null;
		Calendar endDate = null;

		for(int i = 0; i < photos.size(); i++) {
			Photo photo = user.getPhotoCollection().get(photos.get(i));

			if(!photo.getName().equals("")) {
				Date date = photo.getUnformattedDate();
				Calendar photoDate = Calendar.getInstance();
				photoDate.set(Calendar.MILLISECOND,0);
				photoDate.setTime(date);

				Calendar comparisonDate = Calendar.getInstance();
				comparisonDate.set(Calendar.MILLISECOND,0);

				int y = photoDate.get(Calendar.YEAR);
				int m = photoDate.get(Calendar.MONTH);
				int d = photoDate.get(Calendar.DAY_OF_MONTH);

				comparisonDate.set(y, m, d);

				if(startDate == null || comparisonDate.compareTo(startDate) < 0)
					startDate = comparisonDate;
				if(endDate == null || comparisonDate.compareTo(endDate) > 0)
					endDate = comparisonDate;
			}
		}

		if(startDate == null || endDate == null)
			return "";

		int year = startDate.get(Calendar.YEAR);
		int month = startDate.get(Calendar.MONTH) + 1;
		int day = startDate.get(Calendar.DAY_OF_MONTH);

		start += month + "/" + day + "/" + year;

		year = endDate.get(Calendar.YEAR);
		month = endDate.get(Calendar.MONTH) + 1;
		day = endDate.get(Calendar.DAY_OF_MONTH);

		end += month + "/" + day + "/" + year;

		return start + " - " + end;
	}
	/**
	 * This method gets the value of <a href="#{@link}">{@link currentPhotoIndex}</a>.
	 * @return value of <a href="#{@link}">{@link currentPhotoIndex}</a>
	 */
	public int getCurrentPhotoIndex() {
		return currentPhotoIndex;
	}
	/**
	 * This method returns the photo specified with <a href="#{@link}">{@link currentPhotoIndex}</a>.
	 * @return the <a href="#{@link}">{@link Photo}</a> that is associated with the <a href="#{@link}">{@link currentPhotoIndex}</a>
	 */
	public Photo getCurrentPhoto() {
		return user.getPhotoCollection().get(photos.get(currentPhotoIndex));
	}
	/**
	 * This method returns the name of the album.
	 * @return value of <a href="#{@link}">{@link name}</a>
	 */
	public String getName() {
		return name;
	}
	/**
	 * This method returns the number of photos that the album contains.
	 * @return value of <a href="#{@link}">{@link numberOfPhotos}</a>
	 */
	public int getNumberOfPhotos() {
		return numberOfPhotos;
	}
	/**
	 * This method returns the photos that the album contains.
	 * These photos are only copies of the original photos, so any changes performed on them are not reflected on the actual photos. 
	 * @return <code>ArrayList</code> of <a href="#{@link}">{@link Photo}</a>s that the album contains.
	 */
	public ArrayList<Photo> getPhotos() {
		ArrayList<Photo> result = new ArrayList<Photo>();
		for(int i = 0; i < photos.size(); i++)
		{
			result.add(user.getPhotoCollection().get(photos.get(i)));
		}
		return result;
	}
	/**
	 * This method changes the name of the album.
	 * @param name the desired name of the album
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * This method changes <a href="#{@link}">{@link currentPhotoIndex}</a> to the index associated with a
	 * specific photo.
	 * @param name the name of the specified photo
	 */
	public void setCurrentPhoto(String name) {
		for(int i = 0; i < photos.size(); i++) {
			if(user.getPhotoCollection().get(photos.get(i)).getName().equalsIgnoreCase(name)) {
				currentPhotoIndex = i;
				return;
			}
		}
	}
}

package photos.Entities;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class that is responsible for the information and behaviors associated with a photo.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class Photo implements Serializable{
	/**
	 * <code>String</code> that represents the given name of the photo. The name is the pathname of the corresponding photo file.
	 */
	private String name;
	/**
	 * <code>ArrayList</code> of <a href="#{@link}">{@link Tag}</a>s that holds the tags associated with the photo.
	 */
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	/**
	 * <code>String</code> that represents the date and time that the photo was "captured" (actually the time the photo file was last modified)
	 * in format "YYYY/MM/DD HH:MM:SS".
	 */
	private String dateTime;
	/**
	 * <code>Date</code> that represents the date that the photo was "captured"
	 */
	private Date date;
	/**
	 * <code>String</code> that represents the given caption of the photo.
	 */
	private String caption;
	/**
	 * <code>Integer</code> that represents the number of albums that the photo is in.
	 */
	private int numAlbumsContaining=0;
	/**
	 * Constructor that creates a photo with the given name and sets its date and time of "capture".
	 * @param name the specified name of the photo to be created
	 */
	public Photo(String name){
		this.name = name;
		
		File file = new File(name);
		Date lastMod= new Date(file.lastModified());
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
	    String formattedDateString = formatter.format(lastMod); 
		
		this.dateTime=formattedDateString;
		this.date=lastMod;
	}
	/**
	 * This method returns the name of the photo.
	 * @return value of <a href="#{@link}">{@link name}</a>
	 */
	public String getName() {
		return name;
	}
	/**
	 * This method returns the tags of the photo.
	 * @return <code>ArrayList</code> of <a href="#{@link}">{@link Tag}</a>s
	 * @see <a href="#{@link}">{@link Tag}</a>
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}
	/**
	 * This method returns the caption of the photo.
	 * @return value of <a href="#{@link}">{@link caption}</a>
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * This method returns the date and time of "capture" of the photo.
	 * @return value of <a href="#{@link}">{@link date}</a>
	 */
	public Date getUnformattedDate() {
		return date;
	}
	/**
	 * This method returns the date of "capture" of the photo.
	 * @return value of <a href="#{@link}">{@link dateTime}</a> in format "YYYY/MM/DD"
	 */
	public String getDate() {
		return dateTime.substring(0,10);
	}
	/**
	 * This method returns the time of "capture" of the photo.
	 * @return value of <a href="#{@link}">{@link dateTime}</a> in format "HH:MM:SS"
	 */	
	public String getTime() {
		return dateTime.substring(11);
	}
	
	/**
	 * This method returns the number of albums that this photo is in.
	 * @return value of <a href="#{@link}">{@link numAlbumsContainin}</a>
	 */
	public int getNumAlbumsContaining(){
		return numAlbumsContaining;
	}
	/**
	 * This method adds a tag to the photo.
	 * @see <a href="#{@link}">{@link Tag}</a>
	 */
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
	/**
	 * This method removes a tag from the photo.
	 * @see <a href="#{@link}">{@link Tag}</a>
	 */
	public void deleteTag(Tag tag) {
		this.tags.remove(tag);
	}
	/**
	 * This method changes the name of the photo.
	 * @param name the desired name of the photo
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * This method changes the caption of the photo.
	 * @param name the desired caption of the photo
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * This method changes the number of albums that this photo is in.
	 * @param i 1 or -1, signifying adding the photo to an album or deleting a photo from an album
	 */
	public void changeNumAlbumsContaining(int i){
		this.numAlbumsContaining += i;
	}
	/**
	 * This method returns the index of specified <a href="#{@link}">{@link Tag}</a> in <a href="#{@link}">{@link tags}</a>.
	 * @param tagType the type of the specified tag
	 * @param tagName the name of the specified tag
	 * @return the index if the tag was found, -1 if the photo does not have the specified tag
	 */
	public int tagIndex(String tagType, String tagName) {
		for(int i = 0; i < tags.size(); i++) {
			if(tags.get(i).getType().equalsIgnoreCase(tagType) && tags.get(i).getName().equalsIgnoreCase(tagName)) {
				return i;
			}
		}
		return -1;
	}

}

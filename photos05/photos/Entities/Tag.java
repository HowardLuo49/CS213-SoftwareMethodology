package photos.Entities;

import java.io.Serializable;
/**
 * Class that is responsible for the information and behaviors associated with a tag.
 * @author Prajit Kundu
 * @author Howard Luo
 *
 */
public class Tag implements Serializable{
	private String type;
	private String name;
	
	public Tag(String type, String name){
		this.type = type;
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
}

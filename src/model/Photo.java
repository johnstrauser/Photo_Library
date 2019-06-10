package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.scene.image.Image;
/**
 * 
 * @author John Strauser Josh Pineda
 * <p>Class for photos. Contains a path to the image, a caption (name), a date, and tags. Date is initialized to the time the image is added to the program, but can be easily changed by the user.</p>
 */
public class Photo implements java.io.Serializable{
	public String path;
	public String name;
	public Calendar date;
	public List<Tag> tags;
	
	/**
	 * <p>Creates a new photo with the path parameter. Makes the caption (name) blank and provides the time of creation for date.</p>
	 * @param path
	 */
	public Photo(String path) {
		this.path = path;
		name = "";
		date = Calendar.getInstance();
		date.set(Calendar.MILLISECOND, 0);
		
		tags = new ArrayList<Tag>();
	}
	/**
	 * <p>Similar to one arg constructor, but sets the name to the name param.</p>
	 * @param path
	 * @param name
	 */
	public Photo(String path, String name){
		this.path = path;
		this.name = name;
		date = Calendar.getInstance();
		date.set(Calendar.MILLISECOND, 0);
		
		tags = new ArrayList<Tag>();
	}
	public Tag getTag(int index){
		return tags.get(index);
	}
	public void addTag(Tag t){
		if(!tagPresent(t)){
			tags.add(t);
		}
	}
	public void addTag(String name, String value){
		Tag t = new Tag(name,value);
		if(!tagPresent(t)){
			tags.add(t);
		}
	}
	public boolean tagPresent(Tag t){
		for(int i=0; i<tags.size(); i++){
			Tag temp = tags.get(i);
			if(t.getName().equals(temp.getName())){
				if(t.getValue().equals(temp.getValue())){
					return true;
				}
			}
		}
		
		return false;
	}
	public String getPath() {
		return path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	
}

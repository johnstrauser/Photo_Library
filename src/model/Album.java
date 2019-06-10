package model;

import java.util.*;
/**
 * 
 * @author John Strauser Josh Pineda
 * <p>Class for albums. Contains the name of the album and a list of photos.</p>
 */
public class Album implements java.io.Serializable{
	public List<Photo> photos;
	public String name;
	/**
	 * <p>Creates a new album with an empty name and the default photo.</p>
	 */
	public Album() {
		photos = new ArrayList<Photo>();
		//Add default photo
		addDefault();
		name = "";
	}
	/**
	 * <p>Creates a new album with the name param and the default photo.</p>
	 * @param name
	 */
	public Album(String name) {
		photos = new ArrayList<Photo>();
		//Add default photo
		addDefault();
		this.name = name;
	}
	/**
	 * <p>Creates a new album with the name param and contains photos in the photos param.</p>
	 * @param name
	 * @param photos
	 */
	public Album(String name, List<Photo> photos) {
		this.photos = photos;
		this.name=name;
	}
	/**
	 * <p>Adds the default image to this album. Default image is meant to show the user that no images are present in the album, and prevent a nullPointerException in homePageController.</p>
	 */
	public void addDefault() {
		photos.add(new Photo("/model/default.jpg","DefaultImgNameOnlyWeWouldPut"));
	}
	public void addPhoto(Photo p) {
		photos.add(p);
	}
	public Photo getPhoto(int i) {
		return photos.get(i);
	}
	public List<Photo> getPhotos(){
		return photos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String toString(){
		return name;
	}
}

package model;

import java.util.*;

/**
 * 
 * @author John Strauser John Pineda
 * <p>Class for users. Contains the username and list of albums associated to a specfic user.</p>
 */
public class User implements java.io.Serializable{
	protected String username;
	public List<Album> albums;
	
	/**
	 * @param username
	 * <p>Creates a new user with an empty list of albums and the username provided as a parameter</p>
	 */
	public User(String username) {
		this.username = username;
		albums = new ArrayList<Album>();
	}
	
	public String getUsername() {
		return username;
	}
	public Album getAlbum(int i) {
		return albums.get(i);
	}
	public List<Album> getAlbums(){
		return albums;
	}
	
	public String toString() {
		return username;
	}
}

package model;

/**
 * Joshua Pineda
 * John Strauser
 */
import controller.LoginController;
import controller.PhotoViewController;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import controller.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * @author John
 * <p>Main for the program. Manages all aspects of the program and the list of users.</p>
 */
public class Photos extends Application {
	private LoginController logincontroller;
	private static Stage stage;
	public static List<User> users = FXCollections.observableArrayList();
	
	public static User currUser;
	public static Album currAlbum;
	public static Photo currPhoto;
	
	/**
	 * <p>Creates the initial stage. Sets that stage to be the login screen. Then waits for input from login screen.</p>
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Login.fxml"));		
		AnchorPane root = (AnchorPane)loader.load();

		logincontroller = loader.getController();
		logincontroller.start(users);
		
		Scene scene = new Scene(root,400,400);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		stage = primaryStage;
	}
	/**
	 * <p>Serializes the data of all users before closing the program.</p>
	 */
	@Override
	public void stop() {
		//Serialize the users data
		String fileName = "save.ser";
		try {
			FileOutputStream file = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(file);
			
			for(int i = 0; i<users.size(); i++) {
				out.writeObject(users.get(i));
			}
			out.close();
			file.close();
		}catch(FileNotFoundException e) {
		}catch(Exception e) {
			//e.printStackTrace();
		}
	}
	/**
	 * <p>De-serializes the user data from save.ser. Checks if the stock account is present. If the account is not present, it creates the stock account.</p>
	 * @param args
	 */
	public static void main(String[] args) {
		//De-serialize the users data
		String fileName = "save.ser";
		try {
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file);
			
			User u = (User)in.readObject();
			
			while(u != null) {
				users.add(u);
				u = (User)in.readObject();
			}
			
			in.close();
			file.close();
		}catch(FileNotFoundException e) {
			//not an issue
			//on first time running the save.ser has not been created yet
		}catch(EOFException e) {
			//not an issue
			//just that there is no more data in the file to read
		}catch(InvalidClassException e) {
			//not an issue
			//old .ser used an old version of some class
			//just ignore it
		}catch(Exception e) {
			//e.printStackTrace();
		}
		//users.add(new User("test"));
		
		//Check if users contains "STOCK"
		boolean stockExists = false;
		for(int i=0; i<users.size(); i++){
			if(users.get(i).getUsername().equals("stock")){
				stockExists = true;
			}
		}
		
		//If not, make stock with 10 stock images
		if(!stockExists){
			makeStock(10);
		}
		launch(args);
	}
	/**
	 * <p>Creates the stock account.</p>
	 * @param numPhotos
	 */
	public static void makeStock(int numPhotos){
		User stock = new User("stock");
		Album album = new Album("Stock");
		try {
			for(int i=0; i<numPhotos; i++){
				Photo p = new Photo("/StockImages/"+(i%6)+".png","Photo"+i);
				File temp = new File(p.getPath());
				album.addPhoto(p);
			}
			album.getPhotos().remove(0);
			stock.getAlbums().add(album);
			users.add(stock);
		}catch(Exception e) {
			//Don't do anything here, if stock fails to create stock leave it be
		}
		
	}
	/**
	 * <p>Changes the scene to a new screen provided by the fxml file param. The object param is then used to be passed to the fxml controller's start() method.</p>
	 * @param fxmlFile
	 * @param o1
	 */
	public static void changeScene(String fxmlFile, Object o1) {
		 FXMLLoader loader = new FXMLLoader(URL.class.getResource(fxmlFile));
		 AnchorPane root;
		 try 
		 {
		     root = (AnchorPane)loader.load();

		     if(fxmlFile.equals("/view/Login.fxml")){
		         LoginController controller = (LoginController)loader.getController();
		         controller.start(users);
		     }else if(fxmlFile.equals("/view/HomePage.fxml")){
		    	 //determine which user to send to the homepage
		    	 currUser = (User)o1;
		         HomePageController controller = (HomePageController)loader.getController();
		         controller.start(currUser);
		     }else if(fxmlFile.equals("/view/AlbumView.fxml")) {
		    	 currAlbum = (Album)o1;
		    	 AlbumViewController controller = (AlbumViewController)loader.getController();
		         controller.start(currUser, currAlbum);
		     }else if(fxmlFile.equals("/view/PhotoView.fxml")) {
		    	 currPhoto = (Photo)o1;
		    	 PhotoViewController controller = (PhotoViewController)loader.getController();
		         controller.start(currUser, currAlbum, currPhoto);
		     }else if(fxmlFile.equals("/view/AdminPage.fxml")) {
		    	 AdminPageController controller = (AdminPageController)loader.getController();
		         controller.start(users);
		     }else if(fxmlFile.equals("/view/CreateAlbum.fxml")) {
		    	 currUser = (User)o1;
		    	 CreateAlbumController controller = (CreateAlbumController)loader.getController();
		         controller.start(currUser);
		     }else if(fxmlFile.equals("/view/Slideshow.fxml")){
		    	 SlideshowController controller = (SlideshowController)loader.getController();
		         controller.start(currAlbum);
		     }else if(fxmlFile.equals("/view/Search.fxml")) {
		    	 SearchController controller = (SearchController)loader.getController();
		    	 currUser = (User)o1; 
		    	 controller.start(currUser);
		     }
		     stage.setScene(new Scene(root));
		     stage.show();
		 } 
		 catch (Exception e){
			 //e.printStackTrace();
		 }
	}
}

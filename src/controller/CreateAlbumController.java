package controller;

import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import model.Photo;
import model.Photos;
import model.User;


public class CreateAlbumController {
	@FXML TilePane tilePane;
	@FXML TextField SearchBox;
	@FXML TextField AlbumName;
	@FXML ScrollPane scrollPane;
	public User u;
	public void start(User user) {
		u = user;
		int size = u.getAlbums().size();
		for(int i=0; i<size; i++){
			//get first image in album
			Photo p = user.getAlbum(i).getPhoto(0);
			//create imageView from image
			ImageView iv = ControllerTools.createImageView(p.getPath());
			
			//Create Label
			Label l = new Label(user.getAlbum(i).getName(),iv);
			
			l.setMaxSize(75, 70);
			l.setContentDisplay(ContentDisplay.TOP);
			//add to obslist within tilePane
			tilePane.getChildren().add(l);
		}
		
	}
	
	public void searchHandler() {
		
	}
	public void gobackHandler() {
		Photos.changeScene("/view/HomePage.fxml",u);
	}
	public void confirmHandler() {
		Photos.changeScene("/view/HomePage.fxml",u);
	}
	public void logoutHandler() {
		Photos.changeScene("/view/Login.fxml",u);
	}

}

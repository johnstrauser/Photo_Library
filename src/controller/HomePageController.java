package controller;
import model.Album;
import model.Photo;
import model.Photos;
import model.User;
import javafx.fxml.FXML;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

public class HomePageController {
	@FXML TilePane tilePane;
	@FXML ScrollPane scrollPane;
	@FXML TextField SearchBox;
	
	
	public User user;
	
	private void CreateAlbumDialogBox() {
		TextInputDialog dialog = new TextInputDialog("Album Name");
		dialog.setTitle("Create Album");
		dialog.setHeaderText("Album Creation");
		dialog.setContentText("Please enter name for your album");

		// Traditional way to get the response value.
		Optional<String> albumname = dialog.showAndWait();
		if (albumname.isPresent()){
			if (albumname.get().equals(null)) {
				ControllerTools.showInputError(1);
				return;
			}
			if(!checkAlbumName(albumname.get())) {
				Album tempalbum = new Album(albumname.get());
			    user.albums.add(tempalbum);
			    tilePane.getChildren().clear();
			    start(user);
			}else {
				//TODO: error dialog for album name already in use
				return;
			}
		}
	}
	public boolean checkAlbumName(String name) {
		for(int i=0; i<user.getAlbums().size(); i++) {
			if(user.getAlbum(i).getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	private void DeleteAlbumDialogBox() {
		List<String> choices = new ArrayList<>();
		for (int i = 0; i < user.getAlbums().size();i++) {
			
			choices.add(user.getAlbum(i).name);
		}

		ChoiceDialog<String> dialog = new ChoiceDialog<>("Select an album name", choices);
		dialog.setTitle("Delete an Album");
		dialog.setHeaderText("Album Deletion");
		dialog.setContentText("Choose an album to delete:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			if (result.get().equals("Select an album name")) {
				ControllerTools.showInputError(2);
			}else {
				for (int i = 0; i < user.getAlbums().size(); i++) {
					if (user.getAlbum(i).name.equals(result.get())) {
						user.albums.remove(i);
					}
				}
			}
		}
	    tilePane.getChildren().clear();
	    start(user);
	}
	
	public void start(User u) {
		//Get user
		user=u;
		
		//Tilepane setup
		tilePane.setPadding(new Insets(15,15,15,15));
		tilePane.setHgap(15);
		
		//Set images
		int size = user.getAlbums().size();
		for(int i=0; i<size; i++){
			//get first image in album
			Photo p = user.getAlbum(i).getPhoto(0);
			//create imageView from image
			ImageView iv = createImageView(p.getPath());
			
			//Create Label
			Label l = new Label(user.getAlbum(i).getName(),iv);
			l.setOnMouseClicked(new EventHandler<MouseEvent>(){

				@Override
				public void handle(MouseEvent arg0) {
					Label temp = (Label)arg0.getSource();
					int index = tilePane.getChildren().indexOf(temp);
					arg0.consume();
					albumViewHandler(index);
				}
				
			});
			
			l.setMaxSize(75, 70);
			l.setContentDisplay(ContentDisplay.TOP);
			//add to obslist within tilePane
			tilePane.getChildren().add(l);
		}
		//scrollPane setup
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setFitToWidth(true);
		scrollPane.setContent(tilePane);
	}
	public ImageView createImageView(String path){
		Image image = new Image(path);
		ImageView iv = new ImageView(image);
		
		iv.setFitWidth(50);
		iv.setFitHeight(50);
		
		return iv;
	}
	//Might not need this, unless we implement search for photos globally
	public void searchHandler() {
		Photos.changeScene("/view/Search.fxml",user);
	}
	public void logoutHandler() {
		//Do stuff before logout (maybe save)
		
		
		Photos.changeScene("/view/Login.fxml",0);
	}
	public void createAlbumHandler() {
		CreateAlbumDialogBox();
	}
	public void deleteAlbumHandler() {
		DeleteAlbumDialogBox();
		
	}
	public void albumViewHandler(int index) {
		Photos.changeScene("/view/AlbumView.fxml", user.getAlbum(index));
	}
}

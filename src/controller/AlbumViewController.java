package controller;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import model.*;

public class AlbumViewController {
	@FXML TilePane tilePane;
	@FXML ScrollPane scrollPane;
	@FXML Text albName;
	
	public User user;
	public Album album;
	
	private void RenameDialogBox() {
		TextInputDialog dialog = new TextInputDialog("Enter new album name");
		dialog.setTitle("Album Modification");
		dialog.setHeaderText("Renaming " + album.name);
		dialog.setContentText("Please enter a new name for your album");

		// Traditional way to get the response value.
		Optional<String> newalbumname = dialog.showAndWait();
		if (newalbumname.isPresent()){
			if (newalbumname.get().equals("Enter new album name")) {
				ControllerTools.showInputError(1);
			}else {
				if(!checkAlbumName(newalbumname.get())) {
					album.name = newalbumname.get();
					albName.setText(album.getName());
				}else {
					// error dialog here
					return;
				}
				
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
	public void start(User u, Album a) {
		user=u;
		album=a;
		albName.setText(album.getName());
		
		tilePane.getChildren().clear();
		tilePane.setPadding(new Insets(15,15,15,15));
		tilePane.setHgap(15);
		
		int size = album.getPhotos().size();
		for(int i=0; i<size; i++){
			//get image i
			Photo p = album.getPhoto(i);
			if(!p.getName().equals("DefaultImgNameOnlyWeWouldPut")) {
				//create imageView from image
				ImageView iv = createImageView(p.getPath());
				
				//Create Label
				Label l = new Label(p.getName(),iv);
				l.setOnMouseClicked(new EventHandler<MouseEvent>(){

					@Override
					public void handle(MouseEvent arg0) {
						Label temp = (Label)arg0.getSource();
						int index = tilePane.getChildren().indexOf(temp);
						arg0.consume();
						photoViewHandler(index);
					}
					
				});
				
				l.setMaxSize(75, 70);
				l.setContentDisplay(ContentDisplay.TOP);
				//add to obslist within tilePane
				tilePane.getChildren().add(l);
			}
		}
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
	public void homePageHandler() {
		Photos.changeScene("/view/HomePage.fxml",user);
	}
	public void logoutHandler() {
		//: Do stuff before logout
		
		
		Photos.changeScene("/view/Login.fxml",0);
	}
	public void addPhotoHandler() {
		Dialog<Pair<String,String>> dialog = new Dialog<>();
		dialog.setTitle("Import Photo");
		dialog.setHeaderText("Enter the path of a photo to import and its caption");
		
		ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField path = new TextField();
		path.setPromptText("Path");
		TextField caption = new TextField();
		caption.setPromptText("Caption");

		grid.add(new Label("Path:"), 0, 0);
		grid.add(path, 1, 0);
		grid.add(new Label("Caption:"), 0, 1);
		grid.add(caption, 1, 1);
		
		Node loginButton = dialog.getDialogPane().lookupButton(submitButtonType);
		loginButton.setDisable(true);

		path.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		Platform.runLater(() -> path.requestFocus());

		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == submitButtonType) {
		        return new Pair<>(path.getText(), caption.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		String pathOut = "";
		String captionOut = "";
		try{
			Pair<String,String> res = result.get();
			pathOut = res.getKey();
			captionOut = res.getValue();
		}catch(NoSuchElementException e){
			return;
		}
		
		if(pathOut.equals("") || captionOut.equals("")) {
			// popup error dialog
			return;
		}
		
		//Got the path to import
		//create photo and add to album
		Photo p = new Photo(pathOut,captionOut);
		/*
		File temp = new File(p.getPath());
		if(temp.exists()) {
			album.addPhoto(p);
		}else {
			//TODO: add error dialog stating file provided in path does not exist
			System.out.println("Path doesn't exist");
		}
		*/
		album.addPhoto(p);
		if(album.getPhoto(0).getName().equals("DefaultImgNameOnlyWeWouldPut") && album.getPhotos().size() > 1){
			album.getPhotos().remove(0);
		}
		start(user, album);
	}
	public void deletePhotoHandler() {
		//TODO: popup dialog box with list of photos
		/*
		final Stage dialog = new Stage();
		dialog.initModality(Modality.WINDOW_MODAL);
		GridPane gridPane = new GridPane();
		gridPane.add(scrollPane, 0, 0);
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dialog.close();
			}		
		});
		gridPane.add(cancelButton, 0, 1);
		
		dialog.getDialogPane().setContent(gridPane);
		
		dialog.show();
		*/
	}
	public void photoViewHandler(int index) {
		//pass photo to second arg
		Photos.changeScene("/view/PhotoView.fxml",album.getPhoto(index));
	}
	public void slideshowHandler() {
		Photos.changeScene("/view/Slideshow.fxml",0);
	}
	public void renameHandler() {
		RenameDialogBox();
	}
}

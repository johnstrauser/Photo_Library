package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.*;

public class PhotoViewController {
	@FXML Text Name;
	@FXML Text Date;
	@FXML Text Tags;
	@FXML HBox Hbox;
	
	public User user;
	public Album album;
	public Photo photo;
	
	public void start(User u, Album a, Photo p) {
		user=u;
		album = a;
		photo = p;
		
		//Load Photo and details and display in proper place
		updateNameText();
		Name.maxWidth(180.0);
		
		updateDateText();
		Date.maxWidth(180.0);
		
		updateTagsText();
		Tags.maxWidth(180.0);
		Tags.setWrappingWidth(180.0);
		
		ImageView iv = createImageView(p.getPath());
		Hbox.getChildren().add(iv);
	}
	public ImageView createImageView(String path){
		Image image = new Image(path);
		ImageView iv = new ImageView(image);
		
		iv.setFitWidth(Hbox.getMaxWidth());
		iv.setFitHeight(Hbox.getMaxHeight());
		
		return iv;
	}
	public void albumHandler() {
		Photos.changeScene("/view/AlbumView.fxml", album);
	}
	public void logoutHandler() {
		//Do stuff before logout (maybe save)
		Photos.changeScene("/view/Login.fxml", 0);
	}
	public void copyPhotoHandler() {
		ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(user.getAlbum(0), user.getAlbums());
		dialog.setTitle("Copy Photo");
		dialog.setHeaderText("Select the album to copy the photo to.");
		 
		Optional<Album> result = dialog.showAndWait();
		if(!result.isPresent()){
			return;
		}
		Album toCopy = result.get();
		if (toCopy.equals(album)) {
			ControllerTools.showInputError(4);
			return;
		}
		toCopy.addPhoto(photo);
		if(toCopy.getPhoto(0).getName().equals("DefaultImgNameOnlyWeWouldPut")){
			toCopy.getPhotos().remove(0);
		}
	}
	public void movePhotoHandler() {
		ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(user.getAlbum(0), user.getAlbums());
		dialog.setTitle("Move Photo");
		dialog.setHeaderText("Select the album to move the photo to.");
		 
		Optional<Album> result = dialog.showAndWait();
		if(!result.isPresent()){
			return;
		}
		Album toMove = result.get();
		
		//remove photo from current album
		album.getPhotos().remove(photo);
		if(album.getPhotos().size() == 0){
			album.addDefault();
		}
		
		//add photo to selected album
		toMove.addPhoto(photo);
		if(toMove.getPhoto(0).getName().equals("DefaultImgNameOnlyWeWouldPut")){
			toMove.getPhotos().remove(0);
		}
		album = toMove;
	}
	public void editDetailsHandler() {
		//Popup dialog that returns photo
		Dialog<Photo> dialog = new Dialog<Photo>();
		dialog.setTitle("Edit Details");
		dialog.setHeaderText("Change the Caption and Date of the photo.");
		 
		Label captionLabel = new Label("Caption: ");
		Label dateLabel = new Label("Date: ");
		TextField captionText = new TextField();
		TextField dateText = new TextField();
		         
		captionText.setText(photo.getName());
		dateText.setText(""+photo.getDate().getTime());
		
		GridPane grid = new GridPane();
		grid.add(captionLabel, 1, 1);
		grid.add(captionText, 2, 1);
		grid.add(dateLabel, 1, 2);
		grid.add(dateText, 2, 2);
		dialog.getDialogPane().setContent(grid);
		         
		ButtonType buttonTypeOk = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		 
		dialog.setResultConverter(new Callback<ButtonType, Photo>() {
		    @Override
		    public Photo call(ButtonType b) {
		 
		        if (b == buttonTypeOk) {
		        	Photo temp = photo;
		        	if(captionText.getText().equals("") || dateText.getText().equals("")){
		        		//TODO: popup error dialog
		        		
		        		return null;
		        	}else{
		        		temp.setName(captionText.getText());
		        		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		        		try {
							temp.getDate().setTime(sdf.parse(dateText.getText()));
						} catch (ParseException e) {
							//TODO: creare popup error dialog 
							
							return null;
						}
		        	}
		            return temp;
		        }
		 
		        return null;
		    }
		});
		         
		Optional<Photo> result = dialog.showAndWait();
		         
		if (result.isPresent() && result.get() != null) {
			Photo p =result.get();
			int index = album.getPhotos().indexOf(photo);
			album.getPhotos().set(index, p);
			photo = p;
		}
		updateNameText();
		updateDateText();
	}
	public void addTagHandler() {
		Dialog<Tag> dialog = new Dialog<Tag>();
		dialog.setTitle("Add Tag");
		dialog.setHeaderText("Enter a tag name and value. Then click 'Add'.");
		 
		Label nameLabel = new Label("Name: ");
		Label valueLabel = new Label("Value: ");
		TextField nameText = new TextField();
		TextField valueText = new TextField();
		         
		GridPane grid = new GridPane();
		grid.add(nameLabel, 1, 1);
		grid.add(nameText, 2, 1);
		grid.add(valueLabel, 1, 2);
		grid.add(valueText, 2, 2);
		dialog.getDialogPane().setContent(grid);
		         
		ButtonType buttonTypeOk = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		 
		dialog.setResultConverter(new Callback<ButtonType, Tag>() {
		    @Override
		    public Tag call(ButtonType b) {
		 
		        if (b == buttonTypeOk) {
		 
		            return new Tag(nameText.getText(), valueText.getText());
		        }
		 
		        return null;
		    }
		});
		         
		Optional<Tag> result = dialog.showAndWait();
		         
		if (result.isPresent()) {
			Tag t = result.get();
			if(t.getName().equals("") || t.getValue().equals("")){
				ControllerTools.showInputError(3);
				return;
			}
			photo.addTag(t);
		}
		updateTagsText();
	}
	public void deleteTagHandler() {
		if (photo.tags.size() == 0) {
			ControllerTools.showInputError(4);
		}
		ChoiceDialog<Tag> dialog = new ChoiceDialog<Tag>(photo.tags.get(0), photo.tags);
		dialog.setTitle("Delete Tag");
		dialog.setHeaderText("Select the tag to delete");
		 
		Optional<Tag> result = dialog.showAndWait();
		Tag toDelete; 
		         
		toDelete = result.get();
		 
		photo.tags.remove(toDelete);
		updateTagsText();
		
	}
	public void deletePhotoHandler() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirmation");
		alert.setContentText("Are you ok with deleting this photo?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			album.photos.remove(photo);
			if(album.getPhotos().size()==0){
				album.addDefault();
			}
			Photos.changeScene("/view/AlbumView.fxml", album);
		}
		
		
	}
	public void updateTagsText(){
		String tagsText = "Tags: ";
		for(int i=0; i<photo.tags.size(); i++){
			Tag temp = photo.getTag(i);
			if(i==0){
				tagsText += temp.toString();
			}else{
				tagsText += " | "+temp.toString();
			}
			
		}
		Tags.setText(tagsText);
	}
	public void updateNameText(){
		Name.setText("Name: "+photo.getName());
	}
	public void updateDateText(){
		Date.setText("Date: "+photo.getDate().getTime());
	}
}

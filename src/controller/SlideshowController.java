package controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.*;

public class SlideshowController {
	@FXML AnchorPane anchorPane;
	public Album album;
	public int index;
	public int size;
	ImageView iv;
	
	public void start(Album a){
		album = a;
		index = 0;
		size = album.getPhotos().size();
		
		//Set imageview to first photo in album
		iv = new ImageView(album.getPhoto(0).getPath());
		iv.setFitHeight(250.0);
		iv.setFitWidth(250.0);
		
		iv.setX(0);
		iv.setY(25);
		
		anchorPane.getChildren().add(iv);
	}
	public void albumHandler(){
		Photos.changeScene("/view/AlbumView.fxml",album);
	}
	public void nextHandler(){
		if(index >= size-1){
			return;
		}
		anchorPane.getChildren().remove(anchorPane.getChildren().indexOf(iv));
		index++;
		iv = new ImageView(album.getPhoto(index).getPath());
		iv.setFitHeight(250.0);
		iv.setFitWidth(250.0);
		iv.setX(0);
		iv.setY(25);
		anchorPane.getChildren().add(iv);
	}
	public void prevHandler(){
		if(index <= 0){
			return;
		}
		anchorPane.getChildren().remove(anchorPane.getChildren().indexOf(iv));
		index--;
		iv = new ImageView(album.getPhoto(index).getPath());
		iv.setFitHeight(250.0);
		iv.setFitWidth(250.0);
		iv.setX(0);
		iv.setY(25);
		anchorPane.getChildren().add(iv);
	}
}

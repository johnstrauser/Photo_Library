package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Photos;

public class ControllerTools {
	public static void showUserError(int errorType, String username) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("There was an error!");
		switch(errorType) {
			
			case 1:
				alert.setContentText("Error, username " + username + " cannot be found");
				alert.showAndWait();
				break;
			case 2:
				alert.setContentText("Error, username is null");
				alert.showAndWait();
				break;
			case 3:
				alert.setContentText("Error, username " + username + " already exists.");
				alert.showAndWait();
				break;
			case 4:
				alert.setContentText("Error, cannot copy photo to the same album");
				alert.showAndWait();
				break;

			
		}
	}
	public static void showInputError(int errorType) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("There was an error!");
		switch(errorType) {
			case 1:
				alert.setContentText("Please enter an album name");
				alert.showAndWait();
				break;
			case 2:
				alert.setContentText("Please select an album to delete");
				alert.showAndWait();
				break;
			case 3:
				alert.setContentText("Input fields are blank");
				alert.showAndWait();
				break;
			case 4:
				alert.setContentText("There are no tags to delete");
				alert.showAndWait();
				break;
		}
	}
	public static ImageView createImageView(String path){
		Image image = new Image(path);
		ImageView iv = new ImageView(image);
		
		iv.setFitWidth(50);
		iv.setFitHeight(50);
		
		return iv;
	}
	public static void Logout() {
		//TODO: Shouldn't need to save here, check for anything else that needs to be done
		//TODO: Entire function might be redundant
		Photos.changeScene("/view/Login.fxml",0);
	}
}

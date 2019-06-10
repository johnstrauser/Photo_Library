package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import model.Album;
import model.Photo;
import model.Photos;
import model.Tag;
import model.User;

public class SearchController {
	@FXML ComboBox<String> andor, fromMonth, toMonth;
	@FXML ComboBox<Integer> fromDay, toDay, fromYear, toYear, fromHour, toHour, fromMin, toMin, fromSec, toSec;
	@FXML TextField captionField;
	@FXML TextField tagField1;
	@FXML TextField tagField2;
	@FXML ScrollPane scrollPane;
	@FXML TilePane tilePane;
	
	public User user;
	public List<Photo> allPhotos = new ArrayList<Photo>();
	public void start(User u) {
		user = u;
		//Create list of all photos
		//setAllPhotos();
		
		//Handle scrollPane and tilePane creation basics
		tilePane.setPadding(new Insets(15,15,15,15));
		tilePane.setHgap(15);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setFitToWidth(true);
		scrollPane.setContent(tilePane);
		
		//Fill all combo boxes with values
		//Fill fromMonth and toMonth
		fromMonth.getItems().add("Jan");
		toMonth.getItems().add("Jan");
		fromMonth.getItems().add("Feb");
		toMonth.getItems().add("Feb");
		fromMonth.getItems().add("Mar");
		toMonth.getItems().add("Mar");
		fromMonth.getItems().add("Apr");
		toMonth.getItems().add("Apr");
		fromMonth.getItems().add("May");
		toMonth.getItems().add("May");
		fromMonth.getItems().add("Jun");
		toMonth.getItems().add("Jun");
		fromMonth.getItems().add("Jul");
		toMonth.getItems().add("Jul");
		fromMonth.getItems().add("Aug");
		toMonth.getItems().add("Aug");
		fromMonth.getItems().add("Sep");
		toMonth.getItems().add("Sep");
		fromMonth.getItems().add("Oct");
		toMonth.getItems().add("Oct");
		fromMonth.getItems().add("Nov");
		toMonth.getItems().add("Nov");
		fromMonth.getItems().add("Dec");
		toMonth.getItems().add("Dec");
		//Fill fromDay and toDay
		for(int i=1; i<=31; i++) {
			fromDay.getItems().add(i);
			toDay.getItems().add(i);
		}
		//Fill fromYear and toYear
		for(int i=1900; i<2020; i++) {
			fromYear.getItems().add(i);
			toYear.getItems().add(i);
		}
		//Fill fromHour and toHour
		for(int i=0; i<24; i++) {
			fromHour.getItems().add(i);
			toHour.getItems().add(i);
		}
		//Fill fromMin and toMin
		for(int i=0; i<60; i++) {
			fromMin.getItems().add(i);
			toMin.getItems().add(i);
		}
		//Fill fromSec and toSec
		for(int i=0; i<60; i++) {
			fromSec.getItems().add(i);
			toSec.getItems().add(i);
		}
		
	}
	public void setAllPhotos() {
		List<Album> albums = user.getAlbums();
		for(int i=0; i<albums.size(); i++) {
			for(int j=0; j<albums.get(i).getPhotos().size(); j++) {
				Photo temp = albums.get(i).getPhoto(j);
				if(!allPhotos.contains(temp) && !temp.getName().equals("DefaultImgNameOnlyWeWouldPut")) {
					allPhotos.add(temp);
				}
			}
		}
	}
	public void createAlbumHandler() {
		if(allPhotos.size() == 0) {
			//: display error for attempting to create album from empty search results.
			return;
		}
		
		TextInputDialog dialog = new TextInputDialog("Album Name");
		dialog.setTitle("Create Album");
		dialog.setHeaderText("Album Creation");
		dialog.setContentText("Please enter name for your album");

		// Traditional way to get the response value.
		Optional<String> albumname = dialog.showAndWait();
		if (albumname.isPresent()){
			if (albumname.get().equals(null)) {
				ControllerTools.showInputError(1);
			}
			if(!checkAlbumName(albumname.get())) {
				Album tempalbum = new Album(albumname.get(),allPhotos);
			    user.albums.add(tempalbum);
			    tilePane.getChildren().clear();
			}else {
				//: error dialog for album name already in use
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
	private void nameSearch(String capInput){
		if(allPhotos.size() == 0) {
			return;
		}
		for(int i=0; i<allPhotos.size(); i++) {
			if(allPhotos.get(i).getName().indexOf(capInput) == -1) {
				allPhotos.remove(i);
				i--;
			}
		}
	}
	private void tagSearch(String tag1, String val1, String tag2, String val2, String operator) {
		if(allPhotos.size() == 0) {
			return;
		}
		if(operator == null) {
			for(int i=0; i<allPhotos.size(); i++) {
				boolean tagSame = false;
				for(int j=0; j<allPhotos.get(i).tags.size(); j++) {
					Tag t = allPhotos.get(i).getTag(j);
					if(t.getName().equals(tag1) && t.getValue().equals(val1)) {
						tagSame = true;
					}
				}
				if(!tagSame) {
					allPhotos.remove(i);
					i--;
				}
			}
		}else {
			if(operator.equals("And")) {
				for(int i=0; i<allPhotos.size(); i++) {
					boolean tag1Same = false, tag2Same = false;
					for(int j=0; j<allPhotos.get(i).tags.size(); j++) {
						Tag t = allPhotos.get(i).getTag(j);
						if((t.getName().equals(tag1) && t.getValue().equals(val1))) {
							tag1Same = true;
						}
						if((t.getName().equals(tag2) && t.getValue().equals(val2))) {
							tag2Same = true;
						}
					}
					if(!tag1Same || !tag2Same) {
						allPhotos.remove(i);
						i--;
					}
				}
			}else {
				for(int i=0; i<allPhotos.size(); i++) {
					boolean tag1Same = false, tag2Same = false;
					for(int j=0; j<allPhotos.get(i).tags.size(); j++) {
						Tag t = allPhotos.get(i).getTag(j);
						if((t.getName().equals(tag1) && t.getValue().equals(val1))) {
							tag1Same = true;
						}
						if((t.getName().equals(tag2) && t.getValue().equals(val2))) {
							tag2Same = true;
						}
					}
					if(!tag1Same && !tag2Same) {
						allPhotos.remove(i);
						i--;
					}
				}
			}
		}
	}
	private void dateSearch(Calendar fromDate, Calendar toDate) {
		if(allPhotos.size() == 0) {
			return;
		}
		for(int i=0; i<allPhotos.size(); i++) {
			if(allPhotos.get(i).getDate().compareTo(fromDate) < 0 || allPhotos.get(i).getDate().compareTo(toDate) > 0) {
				allPhotos.remove(i);
				i--;
			}
		}
	}
	public void searchHandler() {
		setAllPhotos();
		boolean c=false, t=false, d=false;
		//Eliminate all photos that don't match caption from allPhotos
		//Get caption from text field
		String caption = captionField.getText();
		//If present, send it through nameSearch()
		if(!caption.equals("")) {
			c=true;
			nameSearch(caption);
		}
		//Eliminate all photos that don't match tags from allPhotos
		//Get tag1, tag2, and opertator
		String tag1 = tagField1.getText();
		String tag2 = tagField2.getText();
		String operator = andor.getValue();
		//Make sure tag1 is a valid tag
		boolean tag1Valid = false;
		int eqIndex1 = tag1.indexOf("=");
		if(eqIndex1 > 0 || eqIndex1 < tag1.length()-1) {
			tag1Valid = true;
			t=true;
			int eqIndex2 = tag2.indexOf("=");
			if(eqIndex2 > 0 || eqIndex2 < tag2.length()-1) {
				tagSearch(tag1.substring(0, eqIndex1), tag1.substring(eqIndex1+1, tag1.length()), tag2.substring(0, eqIndex2), tag2.substring(eqIndex2+1, tag2.length()), operator);
			}else {
				tagSearch(tag1.substring(0, eqIndex1), tag1.substring(eqIndex1+1, tag1.length()), null, null, null);
			}
		}
		
		//Eliminate all photos that don't match date range from allPhotos
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		Calendar fromDate = Calendar.getInstance(), toDate = Calendar.getInstance();
		
		boolean setTo = false, setFrom = false;
		
		String fString="Mon";
		
		if(!fromMonth.getSelectionModel().isEmpty()) {
			setFrom = true;
			fString+=" "+fromMonth.getValue();
		}else {
			fString+=" Jan";
		}
		if(!fromDay.getSelectionModel().isEmpty()) {
			setFrom = true;
			fString+=" "+fromDay.getValue();
		}else {
			fString+=" 01";
		}
		if(!fromHour.getSelectionModel().isEmpty()) {
			setFrom = true;
			fString+=" "+fromHour.getValue();
		}else {
			fString+=" 00";
		}
		if(!fromMin.getSelectionModel().isEmpty()) {
			setFrom = true;
			fString+=":"+fromMin.getValue();
		}else {
			fString+=":00";
		}
		if(!fromSec.getSelectionModel().isEmpty()) {
			setFrom = true;
			fString+=":"+fromSec.getValue();
		}else {
			fString+=":00";
		}
		fString+=" EDT";
		if(!fromYear.getSelectionModel().isEmpty()) {
			setFrom = true;
			fString+=" "+fromYear.getValue();
		}else {
			fString+=" 1900";
		}
		
		String tString="Mon";
		if(!toMonth.getSelectionModel().isEmpty()) {
			setTo = true;
			tString+=" "+toMonth.getValue();
		}else {
			tString+=" Dec";
		}
		if(!toDay.getSelectionModel().isEmpty()) {
			setTo = true;
			tString+=" "+toDay.getValue();
		}else {
			tString+=" 31";
		}
		if(!toHour.getSelectionModel().isEmpty()) {
			setTo = true;
			tString+=" "+toHour.getValue();
		}else {
			tString+=" 23";
		}
		if(!toMin.getSelectionModel().isEmpty()) {
			setTo = true;
			tString+=":"+toMin.getValue();
		}else {
			tString+=":59";
		}
		if(!toSec.getSelectionModel().isEmpty()) {
			setTo = true;
			tString+=":"+toSec.getValue();
		}else {
			tString+=":59";
		}
		tString+=" EDT";
		if(!toYear.getSelectionModel().isEmpty()) {
			setTo = true;
			tString+=" "+toYear.getValue();
		}else {
			tString+=" 2019";
		}
		
		try {
			fromDate.setTime(sdf.parse(fString));
			toDate.setTime(sdf.parse(tString));
		} catch (ParseException e) {
			//: create parse error dialog
			
			//Do not perform search
			return;
		}
		
		if(setTo || setFrom) {
			d=true;
			dateSearch(fromDate, toDate);
		}
		//Display search results in tilePane
		if(!c && !t && !d) {
			//: error dialog for pressing search with no parameters completely input
		}else {
			//clear tilepane's list
			tilePane.getChildren().clear();
			int size = allPhotos.size();
			for(int i=0; i<size; i++){
				//get image i
				Photo p = allPhotos.get(i);
				if(!p.getName().equals("DefaultImgNameOnlyWeWouldPut")) {
					//create imageView from image
					ImageView iv = createImageView(p.getPath());
					
					//Create Label
					Label l = new Label(p.getName(),iv);
					
					l.setMaxSize(75, 70);
					l.setContentDisplay(ContentDisplay.TOP);
					//add to obslist within tilePane
					tilePane.getChildren().add(l);
				}
			}
		}
	}
	public ImageView createImageView(String path){
		Image image = new Image(path);
		ImageView iv = new ImageView(image);
		
		iv.setFitWidth(50);
		iv.setFitHeight(50);
		
		return iv;
	}
	public void homeHandler() {
		Photos.changeScene("/view/HomePage.fxml",user);
	}
}

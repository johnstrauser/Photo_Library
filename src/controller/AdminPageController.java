package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.User;

public class AdminPageController {
	@FXML Button AddUser;
	@FXML Button DeleteUser;
	@FXML Button ListUsers;
	@FXML Button Logout;
	@FXML TextField User;
	@FXML ListView<User> UserList;
	
	private ObservableList<User> obslist;
	private File saveFile;

	public void start(List<model.User> users) {
		//obslist = FXCollections.observableArrayList();
		obslist = (ObservableList<model.User>) users;
		
		UserList.setItems(obslist);
	}
	/**
	 * @author Josh Pineda
	 * @param user
	 * @return index of the searched user in list of users, returns -1 if cannot be found
	 */
	private int search(String user) {
		for (int i = 0; i < obslist.size();i++) {
			if (user.equalsIgnoreCase(obslist.get(i).getUsername())) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * @author Josh Pineda
	 * @param user
	 * @return true if user is in the list of users, false if the user is not in the list of users
	 */
	private boolean isDuplicate(String user) {
		if(search(user) != -1) {
			return true;
		}
		return false;
	}

	
	public void ListUserHandler() {
		/*
		try {
			saveFile = new File("model/saveFile.txt");
			BufferedReader br = new BufferedReader(new FileReader(saveFile));
			for(String line; (line = br.readLine()) != null; ){
				User user = new User(line);
				obslist.add(user);
			}
		}catch(FileNotFoundException e){
			
		}catch(Exception e) {
			
		}
		*/
	}
	/**
	 * @author John Strauser
	 * <p>Adds user entered into the admin's textfield</p>
	 */
	public void AddUserHandler() {
		if (User.getText().equals("")) {
			ControllerTools.showUserError(2,"");
			return;
		}
		if (!isDuplicate(User.getText())) {
			obslist.add(new User(User.getText()));
			UserList.refresh();
		}else {
			ControllerTools.showUserError(3, User.getText());
			return;
		}
		
		
	}
	/**
	 * @author Joshua Pineda
	 * <p>Deletes user entered into the admin's textfield</p>
	 */
	public void DeleteUserHandler() {
		if (User.getText().equals("")) {
			ControllerTools.showUserError(2,"");
			return;
		}
		// If its already in the list, delete
		int userIndex = search(User.getText());
		if (userIndex != -1) {
			obslist.remove(userIndex);
		}
		else {
			ControllerTools.showUserError(1,User.getText());
			return;
		}
	}
	public void LogoutHandler() {
		//This should be handled in the ControllerTools for efficiency
		ControllerTools.Logout();
		
	}
	

}

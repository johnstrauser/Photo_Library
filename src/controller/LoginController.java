package controller;
import model.Photos;
import model.User;
import javafx.fxml.FXML;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class LoginController {
	@FXML TextField Username;
	@FXML Button login;
	
	public List<User> users;
	
	public void start(List<User> u) {
		users=u;
	}
	public void loginHandler() {
		String user = Username.getText();
		//admin login, lead to admin page
		if (user.equals("admin")) {
			Photos.changeScene("/view/AdminPage.fxml", 0);
		}else {
			//Handle null case 
			if (user.equals(null) || user.equals("")) {
				ControllerTools.showUserError(2,"");
				return;
			}
			//regular user
			else {
				//If user is in list of users, allow
				int index = containsUser(user);
				if(index != -1) {
					Photos.changeScene("/view/HomePage.fxml",users.get(index));
				}else {
					ControllerTools.showUserError(1, user);
					return;
				}
			}
		}
	}
	public int containsUser(String user) {
		for(int i = 0; i<users.size(); i++) {
			User u = users.get(i);
			if(u.getUsername().equals(user)) {
				return i;
			}
		}
		return -1;
	}
}

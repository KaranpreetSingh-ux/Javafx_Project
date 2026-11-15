package Starter_App.Starter_Ap;

import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Login {

	
		@FXML private TextField usernameField;
		@FXML private PasswordField passwordField;
		@FXML private Button loginButton;
		
		
		public void LoginButton(ActionEvent e) {
			
			if(usernameField == null || passwordField == null) {
				System.out.println("The fxml for the login is not working properly");
			}
			
			
			String username = usernameField.getText();
			String password = passwordField.getText();
			//Button clickedButton = (Button) e.getSource();
			//if(clickedButton == loginButton) {
			if(username.equalsIgnoreCase("Admin") && password.equals("alpha")) {
				System.out.println("The login for the admin worked");
				openOrderPage();
				
			}
			else if(username.equalsIgnoreCase("user") && password.equals("Beta")) {
				System.out.println("The user is working");
				openuserpage();
			}
			else {
				System.out.println("The login is not working");
			}
		}
		
		private void openOrderPage() {
			try {
				App.setRoot(App.Views.Dashboard);
			}
			catch(IOException e) {
				System.out.println("The order is not working");
				e.printStackTrace();
			}
		}
		private void openuserpage() {
			try {
				App.setRoot(App.Views.userDashboard);
			}
			catch(IOException e) {
				System.out.println("The user is not working");
				e.printStackTrace();
			}
		}
	
}

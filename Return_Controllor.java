package Starter_App.Starter_Ap.Controller;

import java.io.IOException;

import Starter_App.Starter_Ap.App;
import Starter_App.Starter_Ap.App.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Return_Controllor extends ProcessingData {
	@FXML private Button Back;
	@FXML private Button ProcessingData;
	@FXML private Button ShippedData;
	@FXML private Button ALL;
	@FXML private Button DeliveryData;
	@Override
	protected String statusFilter() {
		return "Returned";
	} 
	
	@FXML private void ReturningTable(ActionEvent e) {
		goBack();
	}
	
	@FXML
	private void Table(ActionEvent ef) {
		Button clickedButton = (Button) ef.getSource() ;
		try {
			if(clickedButton ==  Back) {
				System.out.println("Back button that take back to the order page has been arrived");
				App.setRoot(App.Views.Order);
			}
			else if(clickedButton == ProcessingData ) {
				App.setRoot(App.Views.Processing);
			}
			else if(clickedButton == ShippedData) {
				System.out.println("Processing data shown");
				App.setRoot(App.Views.Shipped);
			}
			else if(clickedButton == ALL) {
				System.out.println("Delivered dashboard shown");
				App.setRoot(App.Views.All);
			}
			else if(clickedButton == DeliveryData) {
				System.out.println("Return Dashbord shown");
				App.setRoot(App.Views.Delivery);
		}
			}
		catch(IOException eg) {
			eg.printStackTrace();
			
			System.out.println("It didnot worked");
		}
	}}
	
package Starter_App.Starter_Ap.Controller;

import java.io.IOException;

import Starter_App.Starter_Ap.App;
import Starter_App.Starter_Ap.App.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AllControllor extends ProcessingData {
	@FXML private Button Back;
	@FXML private Button ProcessingData;
	@FXML private Button ShippedData;
	@FXML private Button DeliveredData;
	@FXML private Button ReturnData;

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
			else if(clickedButton == DeliveredData) {
				System.out.println("Delivered dashboard shown");
				App.setRoot(App.Views.Delivery);
			}
			else if(clickedButton == ReturnData) {
				System.out.println("Return Dashbord shown");
				App.setRoot(App.Views.Return);
		}
			}
		catch(IOException eg) {
			eg.printStackTrace();
			
			System.out.println("It didnot worked");
		}

	}
	
@Override
protected String statusFilter() {
	return null;} 


}

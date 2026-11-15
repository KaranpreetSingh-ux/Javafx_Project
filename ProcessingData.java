package Starter_App.Starter_Ap.Controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Starter_App.Starter_Ap.App;
import Starter_App.Starter_Ap.tablesearch;
import Starter_App.Starter_Ap.App.Views;
import Starter_App.Starter_Apmodel.TableViewmodel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView; 
import javafx.scene.control.TextField;

import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProcessingData {
	
	
		//Buttons
		@FXML private Button ALL;
		@FXML private Button ProcessingData;
		@FXML private Button DeliveryData;
		@FXML private Button ShippedData;
		@FXML private Button ReturnData;
		@FXML private Button Back;
		
		//Model working
		
		@FXML protected TextField searchField;
		@FXML protected TableView<TableViewmodel> TableDelivery;

		@FXML protected TableColumn<TableViewmodel, Integer> OrderId;
		@FXML protected TableColumn<TableViewmodel, String> Customer;
		@FXML protected TableColumn<TableViewmodel, String> ProductName;
		@FXML protected TableColumn<TableViewmodel, String> Date;
		@FXML protected TableColumn<TableViewmodel, String> Status;
		
		// FIX 1: Aligned type to TableViewmodel
		@FXML private TableColumn<TableViewmodel, Void> Action; 
		
		@FXML private TableColumn<TableViewmodel, Double> Total;
		
		// FIX 2: Aligned type to TableViewmodel and Boolean for standard Checkboxes
		@FXML private TableColumn<TableViewmodel, Boolean> CB; 
		
		@FXML private TableView<TableViewmodel> TableAll;
		protected final ObservableList<TableViewmodel> orderList1 =  FXCollections.observableArrayList();
		
		protected String statusFilter() {
			return "Processing";
		}	
	@FXML
	private void ProcessingTable(ActionEvent e) {
		Button clickedButton = (Button) e.getSource();
		try {
			if(clickedButton == Back) {
				System.out.println("Back Button work");
				App.setRoot(App.Views.Order);
			}
		}
			catch(IOException ef) {
				ef.printStackTrace();
				System.out.println("It didnot work");
				}
			}
	//ArrayList
    private ObservableList<TableViewmodel> orderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        CB.setCellValueFactory(cellData -> cellData.getValue(). checkboxProperty());
		CB.setCellFactory(CheckBoxTableCell.forTableColumn(CB));
	    CB.setEditable(true);
	    
	    OrderId.setCellValueFactory(row -> row.getValue().orderIdProperty().asObject());
	    Customer.setCellValueFactory(row -> row.getValue().customerProperty());
	    Date.setCellValueFactory(row -> row.getValue().dateProperty());
	    ProductName.setCellValueFactory(row -> row.getValue().productProperty());
	    Status.setCellValueFactory(row -> row.getValue().statusProperty());
	    Total.setCellValueFactory(row -> row.getValue().totalProperty().asObject());
	    
	    loadOrders();
	    tablesearch.connectSearch(searchField,
	    		TableAll,
	    		orderList1, 
	    		TableViewmodel::getOrderId, 
	    		TableViewmodel:: getCustomer,
	    		TableViewmodel:: getProduct,
	    		TableViewmodel::getDate,
	    		TableViewmodel::getStatus);
    }
    private void loadOrders() {
    	String sql = "SELECT Order_id, Customer_Name, Product, Date, Status, Total FROM Orders ";
    					
    
    if(statusFilter() != null) {
    	sql += " WHERE Status = ? ";
    	}
    try (PreparedStatement statement = App.getConnection().prepareStatement(sql)){
    	
    	if(statusFilter() != null) {
    		statement.setString(1, statusFilter());
    		
    	}
    	try(ResultSet results = statement.executeQuery()){
    orderList1.clear();
    		
    while(results.next())
    	{
    	orderList1.add(new TableViewmodel(
    	results.getInt("Order_id"),
    	results.getString("Customer_Name"),
    	results.getString("Date"),
    	false,
    	results.getString("Product"),
    	results.getString("Status"),
    	results.getDouble("Total"),
    	"N/A"
    	));
    	}}
    }
	catch(SQLException e) {
		e.printStackTrace();
	}
    }
@FXML
protected void goBack() {
    try {
        App.setRoot(App.Views.Order);
    } catch (IOException e) {
        e.printStackTrace();
    }
}



@FXML
private void Table(ActionEvent ef) {
	Button clickedButton = (Button) ef.getSource() ;
	try {
		if(clickedButton ==  Back) {
			System.out.println("Back button that take back to the order page has been arrived");
			App.setRoot(App.Views.Order);
		}
		else if(clickedButton == ShippedData ) {
			App.setRoot(App.Views.Shipped);
		}
		else if(clickedButton == ReturnData) {
			System.out.println("Processing data shown");
			App.setRoot(App.Views.Return);
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

	}
}	
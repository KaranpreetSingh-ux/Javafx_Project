package Starter_App.Starter_Ap;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Starter_App.Starter_Ap.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    public static Connection databaseConnection;

    // because these statements are constant that why enum
    public enum Views{
    	Dashboard("/Dashboard.fxml"),
    	Order("/Orders.fxml"),
    	OrderData("/OrderData.fxml"),
    	Delivery("/DeliveryData.fxml"),
    	Cancel("/Cancelorder.fxml"),
    	Processing("/ProcessingData.fxml"),
    	Return("/ReturnData.fxml"),
    	Shipped("/Shippeddata.fxml"),
    	ReturnedOrder("/Return_order.fxml"),
    	All("/All.fxml"),
    	AdminOV("/AdminorderView"),
    	login("/login.fxml"),
    	user("/order_User.fxml"),
    	userDashboard("/userDashboard.fxml");

    	private final String path;

    	Views(String path){
    		this.path = path;
    	}
    	public String getPath() {
    		return this.path;
    	}
    }

    @Override
    public void init() throws IOException {

    	//Connection of the database to the app.java

    	try {
    		Class.forName("org.sqlite.JDBC");
    		String url = "jdbc:sqlite:src/main/resources/Data.db";
    		databaseConnection = DriverManager.getConnection(url);
    		System.out.println("It is connected");
    	}
    	catch(ClassNotFoundException | SQLException e) {
    		System.out.println("It is not connected");
    		e.printStackTrace();
    	}}


        // Load FXML file
    	public void start(Stage stage) throws IOException{
        scene  = new Scene(loadFXML(Views.login));
        stage.setTitle("JavaFx App");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(Views view) throws IOException{
    	scene.setRoot(loadFXML(view));
    }
    public static Connection getConnection() {
        return databaseConnection;
    }




    // Load FXML from resources
    private static Parent loadFXML(Views view) throws IOException {
        FXMLLoader Loader = new FXMLLoader(
        		App.class.getResource(view.getPath())
        );
        return Loader.load();
    }

    @Override
    public void stop() throws Exception{
    	if(databaseConnection != null && !databaseConnection.isClosed()) { // if(databaseConnection != null && !databaseConnection.isClosed()){databaseConnection.isclosed}
    		databaseConnection.close();
    		System.out.println("Database connection end");
    	}
    	super.stop();//Send it to the java fx 
    }

   public static void main(String[] args) {

    	launch();	}
}



package Starter_App.Starter_Ap;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		App.main(args);
	}

}
package Starter_App.Starter_Ap;

import java.util.Locale;//with this i donot need to put the path again and again 
import java.util.function.Function;
import javafx.collections.ObservableList;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public final class tablesearch {
	private tablesearch() {
		
	}
	@SafeVarargs
	public static<T> FilteredList<T>connectSearch(TextField searchField,
	TableView<T> tableView,
	ObservableList<T> sourceData,
	Function <T, ?> ... searchableFields){
    FilteredList<T> filteredData = new FilteredList<>(sourceData, row -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String query = normalize(newValue);

            filteredData.setPredicate(row -> {
                if (query.isEmpty()) {
                    return true;
                }

                for (Function<T, ?> field : searchableFields) {
                    Object value = field.apply(row);
                    if (value != null && normalize(String.valueOf(value)).contains(query)) {
                        return true;
                    }
                }

                return false;
            });
        });

        tableView.setItems(filteredData);
        return filteredData;
    }
	private static String normalize(String text) {
		if(text == null) {
			return "";
		}

        return text.trim().toLowerCase(Locale.ROOT);
    }
	
	}package Starter_App.Starter_Ap.Controller;

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
package Starter_App.Starter_Ap.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Starter_App.Starter_Ap.App;
import Starter_App.Starter_Ap.App.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;                 // Added: Needed for @FXML
import javafx.scene.control.Button;    // Added: Needed for Button
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

public class DashboardController {

	//By using of the @FXML method i am telling the java that this program as to be seen and made the work by the fxml of that file 
	@FXML private Button Dashboard;
    @FXML private Button Customer;
    @FXML private Button Product;
    @FXML private Button OrdersManagement;
    @FXML private Button Delivery;
    @FXML private Button Inventory;
    @FXML private Button Review;
    
    @FXML private VBox Diagram; 

    //Image label data
    @FXML private Label TotalOrderID;
    @FXML private Label ShippedorderID;
    @FXML private Label Returnedorderid;
    @FXML private Label Deliveredorderid;
    @FXML private Label ProcessingorderID;
    
    @FXML private VBox topProductsChart;
    @FXML private CategoryAxis categoryAxis;
    @FXML private NumberAxis numberAxis;
    
    
    
    private void loadorderCount() {
		String sql = """ 
				SELECT 
COUNT(Status) AS total_orders,
SUM(CASE WHEN Status = 'Processing' THEN 1 ELSE 0 END) AS processing_orders,
        SUM(CASE WHEN Status = 'Delivered' THEN 1 ELSE 0 END) AS delivered_orders,
        SUM(CASE WHEN Status = 'Returned' THEN 1 ELSE 0 END) AS returned_orders,
        SUM(CASE WHEN Status = 'Shipping' THEN 1 ELSE 0 END) AS shipped_orders
    FROM Orders
    """;
		
		try (PreparedStatement statement =
	             App.getConnection().prepareStatement(sql);
	         ResultSet result = statement.executeQuery()) {

	        if (result.next()) {
	        	TotalOrderID.setText("" +
	                    result.getInt("total_orders"));

	        	ProcessingorderID.setText("" +
	                    result.getInt("processing_orders"));

	            ShippedorderID.setText("" +
	                    result.getInt("shipped_orders"));

	            Deliveredorderid.setText("" +
	                    result.getInt("delivered_orders"));

	            Returnedorderid.setText("" +
	                    result.getInt("returned_orders"));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
    
    
    @FXML
    public void initialize() {
    	
    	loadorderCount();
    	
        if (topProductsChart != null) {
        	 // Wipe out any mockup /preview rectangles left from Scene Builder
            TopProduct();
            // Pull fresh data using the database connection from App.java
        }
    }
    
    
    private void TopProduct() {
    	String sql = "SELECT Product, SUM(quantity) AS total "
    			  + "FROM Orders "
    			  + "GROUP BY Product "
    			  + "ORDER BY total DESC "
    			  + "LIMIT 5";
    	Connection c = App.getConnection();
    
    	
    	if(c == null) {
    		System.out.println("An error in the connection between the dashboard and the app.java");
    		return;
    	}
    	topProductsChart.getChildren().clear();

        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String product = rs.getString("Product");
                int total = rs.getInt("total");

                Label productLabel = new Label(product);
                productLabel.setTextFill(javafx.scene.paint.Color.WHITE);
                productLabel.setPrefWidth(170);

                Label totalLabel = new Label(String.valueOf(total));
                totalLabel.setTextFill(javafx.scene.paint.Color.WHITE);
                totalLabel.setPrefWidth(40);
                totalLabel.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

                javafx.scene.layout.HBox row = new javafx.scene.layout.HBox();
                row.setPrefWidth(240);
                row.setSpacing(10);
                row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                row.getStyleClass().add("top-product-row");

                row.getChildren().addAll(productLabel, totalLabel);
                topProductsChart.getChildren().add(row);
            }

        } catch (SQLException e) {
            System.out.println("Error loading top products:");
            e.printStackTrace();
        }

    }
    	    @FXML
    private void handleDashboardButton(ActionEvent e) {
        try {
            App.setRoot(App.Views.Dashboard);
        } catch (IOException eb) {
            eb.printStackTrace();
            System.out.println("Error loading Dashboard screen.");
        }
    }


    @FXML
    private void handleOrdersButton(ActionEvent e) {
        try {
            App.setRoot(App.Views.Order);
        } catch (IOException eb) {
            eb.printStackTrace();
            System.out.println("Error loading Orders screen.");}}


            @FXML
            private void Sidebar(ActionEvent e ) {
            	Button clickedButton = (Button) e.getSource();

            	try {
            		if(clickedButton == Dashboard) {
            			System.out.println("Dashboard is here");
            			App.setRoot(App.Views.Dashboard);
            		}
            		else if(clickedButton == OrdersManagement) {
            			System.out.println("The Order is found");//With the use of the App.set Root i am changing the file of one fxml to the another
            			App.setRoot(App.Views.Order);// The Root : The actual layout or picture currently being displayed on that canvas.
            			}
            		}
            	catch(IOException eb) {
            		eb.printStackTrace();
            		System.out.println("Sidebar not found");
            	}
            	}}
package Starter_App.Starter_Ap.Controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Starter_App.Starter_Ap.App;
import Starter_App.Starter_Ap.tablesearch;
import Starter_App.Starter_Ap.App.Views;
import Starter_App.Starter_Apmodel.Ordermodel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.collections.transformation.FilteredList;

public class OrderControllor {
	
	
	  //Sidebar
		@FXML private Button Dashboard;
	    @FXML private Button Customer;
	    @FXML private Button Product;
	    @FXML private Button OrdersManagement;
	    @FXML private Button Delivery;
	    @FXML private Button Inventory;
	    @FXML private Button Review;
	    
	    //The tables working
	    @FXML private Button ReturnData;
	    @FXML private Button DeliveredData;
	    @FXML private Button ProcessingData;
	    @FXML private Button ShippedData;
	    @FXML private Button ALLData;
	    @FXML private Button Back;
	    
	     //Number of Orders.
	    @FXML private Label totalOrdersLabel;
	    @FXML private Label processingLabel;
	    @FXML private Label deliveryLabel;
	    @FXML private Label returnLabel;
	    @FXML private Label shippedLabel;
	    
	    @FXML private TextField searchField; // Your search text box
	    
	  
	    // Table
	    @FXML private TableView<Ordermodel> orderTable;
	    @FXML private TableColumn<Ordermodel, Integer> OrderIDcolumn;
	    @FXML private TableColumn<Ordermodel, String> Customercolumn;
	    @FXML private TableColumn<Ordermodel, String> Productcolumn;
	    @FXML private TableColumn<Ordermodel, String> Status;
	    @FXML private TableColumn<Ordermodel, Double> Total;
	    @FXML private TableColumn<Ordermodel, Boolean> checkboxcolumn;


	
	// Observable array list to house row data dynamically
	//Plus the data was not visually going there i found that it was processing but not visually showing there 
	//SQL database -> app.java -> Ordermodel -> OrderList -> Order Table 
	
	//ArrayList
    private ObservableList<Ordermodel> orderList = FXCollections.observableArrayList();
    private Ordermodel selectedOrder; 
    
    private void loadorderCount() {
		String sql = """ 
				SELECT 
COUNT(Status) AS total_orders,
COUNT(Processing) AS processing_orders,
COUNT(Delivery) AS delivered_orders,
COUNT(Return) AS returned_orders,
COUNT(Shipping) AS shipped_orders
FROM Orders
""";
		
		try (PreparedStatement statement =
	             App.getConnection().prepareStatement(sql);
	         ResultSet result = statement.executeQuery()) {

	        if (result.next()) {
	            totalOrdersLabel.setText("" +
	                    result.getInt("total_orders"));

	            processingLabel.setText("" +
	                    result.getInt("processing_orders"));

	            shippedLabel.setText("" +
	                    result.getInt("shipped_orders"));

	            deliveryLabel.setText("" +
	                    result.getInt("delivered_orders"));

	            returnLabel.setText("" +
	                    result.getInt("returned_orders"));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


    @FXML
    public void initialize() {
    	
    	orderTable.setEditable(true);
    	checkboxcolumn.setCellValueFactory(cellData -> cellData.getValue(). checkboxProperty());
    	checkboxcolumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxcolumn));
    	checkboxcolumn.setEditable(true);
    	
   
//Learn from : GEMINI
// I cannot use the WebView here because here my tables are small and also with the use of the webview 
// it just not show the data with there accurate data type
        // Link columns to their respective property methods inside Ordermodel.java
    	//setCellValueFactory tells each column exactly which property of the object it should look at to grab its 
    	//value here the set is use for making the connection between them .
    	
    	loadorderCount();
  	
    	OrderIDcolumn.setCellValueFactory(cellData -> cellData.getValue().order_idProperty().asObject());
        Customercolumn.setCellValueFactory(cellData -> cellData.getValue().customerProperty());
        Productcolumn.setCellValueFactory(cellData -> cellData.getValue().ProductProperty());
        Status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        Total.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        // Attach our data collection list directly to the TableView component
        

        // Fetch values instantly when window loads
        dataloading();
        loadorderCount();
        
        setupSearchBar();
        
        setupActionColumn();
    	
	loadActivityChart();
    	loadRecommendations();
    }

	@FXML
	public void dataloading() {
		try {
			loadOrderData();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void loadOrderData() throws ClassNotFoundException, SQLException {
		orderList.clear();
		String sql = "SELECT * FROM Orders LIMIT 6";
				
		try {
			if(App.databaseConnection != null) {
			PreparedStatement stmnt = App.databaseConnection.prepareStatement(sql);
		ResultSet results = stmnt.executeQuery();

		while(results.next()) {

			int order_id = results.getInt("Order_id");
			String customerData = results.getString("Customer_Name");
			String productData = results.getString("Product"); 
			String statusData = results.getString("Status");
			double totalData = results.getDouble("Total");
			
			Ordermodel rowData = new Ordermodel(order_id, customerData, productData, statusData, totalData);
			orderList.add(rowData);
		}
		
		results.close();
		stmnt.close();}
			else {
				System.out.println("Database connection null");}
			}
		
		catch(SQLException e) {
			System.out.println("Not found");
			e.printStackTrace();
		}
	}

   
//Learn from : JAvafx Bro code
      //Changing from the orders to dashboard
    @FXML
    private void Sidebar(ActionEvent e ) {
    	Button clickedButton = (Button) e.getSource();

    	try {
    		if(clickedButton == Dashboard) {
    			System.out.println("Dashboard is here");
    			App.setRoot(App.Views.Dashboard);
    		}
    		else if(clickedButton == OrdersManagement) {
    			System.out.println("The Order is found");//With the use of the App.set Root i am changing the file of one fxml to the another
    			App.setRoot(App.Views.Order);// The Root : The actual layout or picture currently being displayed on that canvas.
    			}
    		}
    	catch(IOException eb) {
    		eb.printStackTrace();
    		System.out.println("Sidebar not found");
    	}
    }
    
    
    @FXML
    	private void Table(ActionEvent ef) {
    		Button clickedButton = (Button) ef.getSource();
    		try {
    			if(clickedButton == ALLData) {
    				System.out.println("All button work");
    				App.setRoot(App.Views.All);
    			}
    			else if(clickedButton == ProcessingData) {
    				System.out.println("Processing button is working");
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
    		
    private void setupSearchBar() {
	    tablesearch.connectSearch(
	        searchField,
	        orderTable,
	        orderList,
	        Ordermodel::getorder_id,
	        Ordermodel::getcustomer,
	        Ordermodel::getProduct,
	        Ordermodel::getstatus
	    );}
    
    
    private void setupActionColumn() {
        // TODO: Implement your action checkbox column here
    }private void loadActivityChart() {
        // TODO: Implement your chart rendering here
    }

    private void loadRecommendations() {
        // TODO: Implement recommendations display logic here
    }

    private void closePopups() {
        // TODO: Implement any modal/popup closure logic here
    }}



    


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
}	package Starter_App.Starter_Ap.Controller;

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
package Starter_App.Starter_Ap.Controller;

import java.io.IOException;

import Starter_App.Starter_Ap.App;
import Starter_App.Starter_Ap.App.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Shipping_Controllor extends ProcessingData {
	@FXML private Button Back;
	@FXML private Button ProcessingData;
	@FXML private Button ReturnData;
	@FXML private Button ALL;
	@FXML private Button DeliveryData;
	
	@Override
	protected String statusFilter() {
		return "Shipping";
	}
	
	@FXML private void ShippedTable(ActionEvent e) {
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
}package Starter_App.Starter_Ap.Controlloruser;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Starter_App.Starter_Ap.App;
import Starter_App.Starter_Ap.tablesearch;
import Starter_App.Starter_Ap.App.Views;
import Starter_App.Starter_Ap.Model.Order_usermodel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class Order_User {

	 @FXML private TextField searchField;
	    @FXML private Label totalOrdersLabel;
	    @FXML private BarChart<String, Number> activityBarChart;
	    @FXML private Label recommendationOneLabel;
	    @FXML private Label recommendationTwoLabel;
	    @FXML private Label recommendationThreeLabel;
	    @FXML private Label recommendationFourLabel;
	    
	    @FXML private Button userDashboard;
	    @FXML private Button userorders;
	    
	    @FXML private TableView<Order_usermodel> ordersTable;
	    @FXML private TableColumn<Order_usermodel, Void> actionColumn;
	    @FXML private TableColumn<Order_usermodel, Integer> OrderIDcolumn;
	    @FXML private TableColumn<Order_usermodel, String> Customercolumn;
	    @FXML private TableColumn<Order_usermodel, String> Productcolumn;
	    @FXML private TableColumn<Order_usermodel, String> Status;
	    @FXML private TableColumn<Order_usermodel, Double> Total;
	    @FXML private TableColumn<Order_usermodel, String> Address;
	    
	    @FXML private AnchorPane popupOverlay;
	    @FXML private VBox orderActionPopup;
	    @FXML private VBox confirmOrderPopup;
	    @FXML private VBox cancelOrderPopup;
	    @FXML private Label selectedOrderLabel;
	    @FXML private Label confirmOrderTextLabel;
	    @FXML private Label cancelOrderTextLabel;
	    @FXML private ComboBox<String> cancelReasonComboBox;
	    @FXML private TextArea cancelNoteTextArea;
	    
	    
	    //private UserOrder selectedOrder;
	    
	    //private final ObservableList<UserOrder> allOrder = FXCollectio
	    
	    
	    private ObservableList<Order_usermodel> orderList = FXCollections.observableArrayList();
	    private Order_usermodel selectedOrder;
	   
	    
	    @FXML private void initialize() {
	    	
	    	//OrderIDcolumn and all are the 
	    	//fxid and the irder_id are the intialization 
	    	//that i have put in the model which are connected with the database
	    	OrderIDcolumn.setCellValueFactory(
	    		    cell -> cell.getValue().order_idProperty().asObject()
	    		);
	    		Customercolumn.setCellValueFactory(
	    		    cell -> cell.getValue().customerProperty()
	    		);
	    		Productcolumn.setCellValueFactory(
	    		    cell -> cell.getValue().ProductProperty()
	    		);
	    		Status.setCellValueFactory(
	    		    cell -> cell.getValue().statusProperty()
	    		);
	    		Total.setCellValueFactory(
	    		    cell -> cell.getValue().totalProperty().asObject()
	    		);
	    		Address.setCellValueFactory(
	    		    cell -> cell.getValue().addressProperty()
	    		);
	    	Customercolumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
	    	Productcolumn.setCellValueFactory(new PropertyValueFactory<>("product"));
	    	Status.setCellValueFactory(new PropertyValueFactory<>("status"));
	    	Total.setCellValueFactory(new PropertyValueFactory<>("total"));
	    	Address.setCellValueFactory(new PropertyValueFactory<>("address"));
	    	cancelReasonComboBox.setItems(FXCollections.observableArrayList(
	    			"Ordered by mistake",
	    			"Wronge product selected",
	    	    	"Need to change address",
	    	    	"Price is to high",
	    	    	"Other"));
	    	
	    	setupActionColumn();
	    	ordersTable.setItems(orderList);
	    	
	    	dataloading();
	    	setupSearchBar();
	    	
	    	totalOrdersLabel.setText(String.valueOf(orderList.size()));
	    	loadActivityChart();
	    	loadRecommendations();
	    }

		private void dataloading() {
			// TODO Auto-generated method stub
			try {
				loadOrderData();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		private void loadOrderData() {
			// TODO Auto-generated method stub
			
			orderList.clear();
			String sql = "SELECT * FROM users LIMIT 10";
					
			try {
				if(App.databaseConnection != null) {
				PreparedStatement stmnt = App.databaseConnection.prepareStatement(sql);
			ResultSet results = stmnt.executeQuery();
			
			while(results.next()) {
				
			int Order_is = results.getInt("Order_is");
			String Customer = results.getString("customer");
			String Product = results.getString("product");
			String Status = results.getString("status");
			String Address = results.getString("address");
			Double Total = results.getDouble("total");
			
			Order_usermodel rowData = new Order_usermodel(Order_is, Customer, Product,  Status, Address, Total);
			orderList.add(rowData);
			}
			results.close();
			stmnt.close();			
			}
				else {
					System.out.println("Database Connection is not working in the user table");
					}
				}
			
			catch(SQLException s) {
				System.out.println("The sql s not able to found");
				s.printStackTrace();
			} 
		
		}
		
		
		private void setupActionColumn() {
	        actionColumn.setCellFactory(column -> new TableCell<>() {
	            private final Button actionButton = new Button("...");

	            {
	                actionButton.getStyleClass().add("table-action-button");
	                actionButton.setOnAction(event -> {
	                    Order_usermodel order = getTableView().getItems().get(getIndex());
	                    openOrderActionPopup(order);
	                });
	            }

	            @Override
	            protected void updateItem(Void item, boolean empty) {
	                super.updateItem(item, empty);
	                setGraphic(empty ? null : actionButton);
	            }
	        });
	    }

	    private void openOrderActionPopup(Order_usermodel order) {
	        selectedOrder = order;
	        String orderText = readValue(order, "orderid");
	        String productText = readValue(order, "product");

	        selectedOrderLabel.setText("Order " + orderText + " - " + productText);
	        confirmOrderTextLabel.setText("Confirm " + productText + " order?");
	        cancelOrderTextLabel.setText("Cancel " + productText + " order?");
	        showPopup(orderActionPopup);
	    }

	    @FXML
	    private void openConfirmOrderPopup() {
	        showPopup(confirmOrderPopup);
	    }

	    @FXML
	    private void openCancelOrderPopup() {
	    	
	    	showPopup(cancelOrderPopup);
	    	}
	    
	    @FXML void cancelSlectedOrder()  {
	    	//Order_usermodel selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
	    	if(selectedOrder == null) {
	    		System.out.println("Please select the order first");
	    		return;
	    	}
	    	String sql = """
	    			UPDATE users
      SET Status = 'Cancelled'
       WHERE Order_is = ?
       AND Status IN ('Delivered', 'Processing', 'Shipped')
         
         AND datetime(Date) BETWEEN datetime('now', '-36 hours') AND datetime('now')
	    			""";	
	    	
	    	try(PreparedStatement statement = App.getConnection().prepareStatement(sql)){
	    		statement.setInt(1, this.selectedOrder.getOrder_id());
	    		
	    		int updateRows = statement.executeUpdate();
	    		
	    		if(updateRows == 1) {
	                selectedOrder.setStatus("Cancelled");
	                System.out.println("Order cancelled successfully.");
	                dataloading();
	                closePopups();
	            } else {
	                System.out.println(
	                    "Cancellation failed. The order is older than 36 hours, "
	                    + "already cancelled, or was not found."
	                );
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    private boolean updateSelectedOrderStatus(String newStatus) {
	    	if(selectedOrder == null) {
	        	return false;
	        }
	        String sql = "Update users SET Status = ? WHERE Order_is = ?";
		       try(PreparedStatement statement = App.getConnection().prepareStatement(sql)){
		    	   
		    	   statement.setString(1, "Cancelled");
		    	   statement.setInt(2,  selectedOrder.getOrder_id());
		    	   
		    	   int updateRows = statement.executeUpdate();
		    	   
		    	   if(updateRows == 1) {
		    		   selectedOrder.setStatus("Cancel_Order");
		    		   totalOrdersLabel.setText(String.valueOf(orderList.size()));
		    		   closePopups();
		    		   return true;
		    	   }
		    	   else {
		    		   System.out.println("The order was not found in the user table");
		    		   return true;
		    	   }
		    	   
		       }
		       catch(SQLException e) {
		    	   e.printStackTrace();
		       }
			   return false;
		       		
	    }
	    @FXML
	    private void handleCancelOrder() {
	    	
	    	cancelSlectedOrder();
	    	
	    }
	
	    @FXML
	    private void closePopups() {
	        selectedOrder = null;
	        popupOverlay.setVisible(false);
	        popupOverlay.setManaged(false);
	        orderActionPopup.setVisible(false);
	        orderActionPopup.setManaged(false);
	        confirmOrderPopup.setVisible(false);
	        confirmOrderPopup.setManaged(false);
	        cancelOrderPopup.setVisible(false);
	        cancelOrderPopup.setManaged(false);
	        cancelReasonComboBox.getSelectionModel().clearSelection();
	        cancelNoteTextArea.clear();
	    }

	    private void showPopup(VBox popup) {
	        popupOverlay.setVisible(true);
	        popupOverlay.setManaged(true);
	        orderActionPopup.setVisible(false);
	        orderActionPopup.setManaged(false);
	        confirmOrderPopup.setVisible(false);
	        confirmOrderPopup.setManaged(false);
	        cancelOrderPopup.setVisible(false);
	        cancelOrderPopup.setManaged(false);
	        popup.setVisible(true);
	        popup.setManaged(true);
	    }
	    
	    
	    private void setupSearchBar() {
	    	tablesearch.connectSearch(
	    			searchField,
	    			ordersTable,
	    			orderList,
	    			Order_usermodel:: getOrder_id,
	    			Order_usermodel::getCustomer,
	    			Order_usermodel::getProduct,
	    			Order_usermodel:: getStatus
	    			);
	    }

	    private void filterOrders(String searchText) {
	        if (searchText == null || searchText.trim().isEmpty()) {
	            ordersTable.setItems(orderList);
	            return;
	        }

	        String query = searchText.toLowerCase();
	        ObservableList<Order_usermodel> filteredOrders = orderList.filtered(order ->
	            readValue(order, "orderid").toLowerCase().contains(query) ||
	            readValue(order, "customer").toLowerCase().contains(query) ||
	            readValue(order, "product").toLowerCase().contains(query)
	        );
	        ordersTable.setItems(filteredOrders);
	    }

	    private void loadActivityChart() {
	        XYChart.Series<String, Number> series = new XYChart.Series<>();
	        series.getData().add(new XYChart.Data<>(" Mouse", 4));
	        series.getData().add(new XYChart.Data<>("Notebook", 3));
	        series.getData().add(new XYChart.Data<>("Lamp", 2));
	        series.getData().add(new XYChart.Data<>("Bottle", 2));
	        activityBarChart.getData().setAll(series);
	    }

	    private void loadRecommendations() {
	        recommendationOneLabel.setText("Wiered Keyboard");
	        recommendationTwoLabel.setText("Notebook Pack");
	        recommendationThreeLabel.setText("Desktop");
	        recommendationFourLabel.setText("R-box");
	    }  

	    private String readValue(Order_usermodel order, String propertyName) {
	        try {
	            String getterName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	            Method getter = order.getClass().getMethod(getterName);
	            Object value = getter.invoke(order);
	            return value == null ? "" : String.valueOf(value);
	        } catch (Exception e) {
	            return "";
	        }
	    }

	    @FXML
	    private void Sidebar() {
	        // Add your sidebar navigation code here.
	    }
	    
	    @FXML
	    private void Sidebar(ActionEvent e ) {
	    	Button clickedButton = (Button) e.getSource();

	    	try {
	    		if(clickedButton == userDashboard) {
	    			System.out.println("Dashboard is here");
	    			App.setRoot(App.Views.userDashboard);
	    		}
	    		else if(clickedButton == userorders) {
	    			System.out.println("The Order is found");//With the use of the App.set Root i am changing the file of one fxml to the another
	    			App.setRoot(App.Views.user);// The Root : The actual layout or picture currently being displayed on that canvas.
	    			}
	    		}
	    	catch(IOException eb) {
	    		eb.printStackTrace();
	    		System.out.println("Sidebar not found");
	    	}
	    	}
	}
package Starter_App.Starter_Ap.Controlloruser;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Starter_App.Starter_Ap.App;
import Starter_App.Starter_Ap.App.Views;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class user_Dashboard {
	
	@FXML private Button userorders;
	@FXML private Button userDashboard;

    @FXML private Label processingCountLabel;
    @FXML private Label returnedCountLabel;
    @FXML private Label shippedCountLabel;
    @FXML private Label deliveredCountLabel;
    @FXML private Label totalProductsCountLabel;

    @FXML private Label marketProduct1Label;
    @FXML private Label marketProduct2Label;
    @FXML private Label marketProduct3Label;
    @FXML private Label marketProduct4Label;
    @FXML private Label marketProduct5Label;
    @FXML private Label marketProduct1SalesLabel;
    @FXML private Label marketProduct2SalesLabel;
    @FXML private Label marketProduct3SalesLabel;
    @FXML private Label marketProduct4SalesLabel;
    @FXML private Label marketProduct5SalesLabel;

    @FXML private Label recommendedProduct1Label;
    @FXML private Label recommendedProduct2Label;
    @FXML private Label recommendedProduct3Label;
    @FXML private Label recommendedProduct4Label;
    @FXML private Label recommendedProduct5Label;

    private List<Label> marketNames;
    private List<Label> marketSales;
    private List<Label> recommendationNames;
    
    
    
    private void loadorderCount() {
		String sql = """ 
				SELECT 
COUNT(Status) AS total_orders,
SUM(CASE WHEN Status = 'Processing' THEN 1 ELSE 0 END) AS processing_orders,
        SUM(CASE WHEN Status = 'Delivered' THEN 1 ELSE 0 END) AS delivered_orders,
        SUM(CASE WHEN Status = 'Return' THEN 1 ELSE 0 END) AS returned_orders,
        SUM(CASE WHEN Status = 'Shipping' THEN 1 ELSE 0 END) AS shipped_orders
    FROM users
    """;
		
		try (PreparedStatement statement =
	             App.getConnection().prepareStatement(sql);
	         ResultSet result = statement.executeQuery()) {

	        if (result.next()) {
	        	totalProductsCountLabel.setText("" +
	                    result.getInt("total_orders"));

	            processingCountLabel.setText("" +
	                    result.getInt("processing_orders"));

	            shippedCountLabel.setText("" +
	                    result.getInt("shipped_orders"));

	            deliveredCountLabel.setText("" +
	                    result.getInt("delivered_orders"));

	            returnedCountLabel.setText("" +
	                    result.getInt("returned_orders"));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

    @FXML
    private void initialize() {
    	
    	loadorderCount();
    
    	
        marketNames = List.of(marketProduct1Label, marketProduct2Label, marketProduct3Label,
                marketProduct4Label, marketProduct5Label);
        marketSales = List.of(marketProduct1SalesLabel, marketProduct2SalesLabel, marketProduct3SalesLabel,
                marketProduct4SalesLabel, marketProduct5SalesLabel);
        recommendationNames = List.of(recommendedProduct1Label, recommendedProduct2Label,
                recommendedProduct3Label, recommendedProduct4Label, recommendedProduct5Label);

        // Temporary electronic products. Replace these later with database values if needed.
        setMarketProducts(List.of(
                new ProductRank("Wireless Headphones", 250),
                new ProductRank("iPhone 17", 220),
                new ProductRank("iPad Air", 180),
                new ProductRank("AirPods Pro", 165),
                new ProductRank("Samsung Galaxy Watch", 140)));

        setRecommendationProducts(List.of(
                new ProductRank("MacBook Air", 0),
                new ProductRank("JBL Bluetooth Speaker", 0),
                new ProductRank("PlayStation 5", 0),
                new ProductRank("Nintendo Switch", 0),
                new ProductRank("Anker Power Bank", 0)));
    }

    public void setDashboardData(int processing, int returned, int shipped, int delivered,
            int userProductTotal, List<ProductRank> marketTopFive,
            List<ProductRank> recommendations) {
        processingCountLabel.setText(String.valueOf(processing));
        returnedCountLabel.setText(String.valueOf(returned));
        shippedCountLabel.setText(String.valueOf(shipped));
        deliveredCountLabel.setText(String.valueOf(delivered));
        totalProductsCountLabel.setText(String.valueOf(userProductTotal));
        setMarketProducts(marketTopFive);
        setRecommendationProducts(recommendations);
    }

    private void setMarketProducts(List<ProductRank> products) {
        for (int i = 0; i < marketNames.size(); i++) {
            ProductRank product = i < products.size() ? products.get(i) : null;
            marketNames.get(i).setText(product == null ? "—" : product.name());
            marketSales.get(i).setText(product == null ? "0 sold" : product.sales() + " sold");
        }
    }

    private void setRecommendationProducts(List<ProductRank> products) {
        for (int i = 0; i < recommendationNames.size(); i++) {
            recommendationNames.get(i).setText(i < products.size() ? products.get(i).name() : "—");
        }
    }

    @FXML private void refreshDashboard() { }
    @FXML private void showDashboard() { }
    @FXML private void showProducts() { }
    @FXML private void showOrders() { }
    @FXML private void showDelivery() { }
    @FXML private void showCart() { }
    @FXML private void showProfile() { }
    @FXML private void showReview() { }
    @FXML private void logout() { }
    @FXML private void viewRecommendedProduct(ActionEvent event) { }

    public record ProductRank(String name, int sales) { }
    
    @FXML
    private void Sidebar(ActionEvent e ) {
    	Button clickedButton = (Button) e.getSource();

    	try {
    		if(clickedButton == userDashboard) {
    			System.out.println("Dashboard is here");
    			App.setRoot(App.Views.userDashboard);
    		}
    		else if(clickedButton == userorders) {
    			System.out.println("The Order is found");//With the use of the App.set Root i am changing the file of one fxml to the another
    			App.setRoot(App.Views.user);// The Root : The actual layout or picture currently being displayed on that canvas.
    			}
    		}
    	catch(IOException eb) {
    		eb.printStackTrace();
    		System.out.println("Sidebar not found");
    	}
    	}
}package Starter_App.Starter_Ap.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
//---------------------
//----Unit- Testing----
//---------------------


public class Order_usermodel {
	
	    	private final IntegerProperty Order_is = new SimpleIntegerProperty();
	    	private final StringProperty Customer = new SimpleStringProperty();
	    	private final StringProperty Product = new SimpleStringProperty(); 
	    	private final StringProperty Address = new SimpleStringProperty();
	    	private final StringProperty Status = new SimpleStringProperty();
	    	private final  DoubleProperty Total = new SimpleDoubleProperty();
	    	
	    	
	    	public Order_usermodel(int order_id, String customer, String product, String status, String address, double total) {
	 			this.Order_is.set(order_id);
	 			this.Customer.set(customer);
	 			this.Product.set(product);
	 			this.Status.set(status);
	 			this.Address.set(address);
	 			this.Total.set(total);
	 		}
	    
	 public IntegerProperty order_idProperty() {
			return Order_is;
		}
		public StringProperty customerProperty() {
			return Customer;
		}
		
		public StringProperty ProductProperty() {
			return Product;
		}
		public StringProperty statusProperty() {
			return Status;
		}
		public StringProperty addressProperty() {
			return Address;
		}
		public DoubleProperty totalProperty() {
			return Total;
		}

		public int getOrder_id() {
			return Order_is.get();
		}
		public String getCustomer() {
			return Customer.get();
		}
		public String getProduct() {
			return Product.get();
		}
		
		public String getAddress() {
			return Address.get();
		}
		public String getStatus() {
			return Status.get();
		}
		public double getTotal() {
			return Total.get();
		}
		public void setStatus(String newStatus) {
			Status.set(newStatus);
		}
		}
package Starter_App.Starter_Apmodel;
//This is use for changing the value
//And also for the table view
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//---------------------
//----Unit- Testing----
//---------------------


/*private int order_id;
private String customer;
private double total;  with the use of this the program dose not become properly the javafx */

public class Ordermodel {
	private final IntegerProperty order_id = new SimpleIntegerProperty();
	private final StringProperty customer = new SimpleStringProperty();
	private final StringProperty Product = new SimpleStringProperty();
	private final StringProperty status = new SimpleStringProperty();
	private final DoubleProperty total = new SimpleDoubleProperty();
	private SimpleBooleanProperty checkbox= new SimpleBooleanProperty(false);


	public Ordermodel(int order_id, String customer, String Product, String status, double total) {
		this.order_id.set(order_id);
		this.customer.set(customer);
		this.status.set(status);
		this.total.set(total);
		this.Product.set(Product);
		
		
	}

	public IntegerProperty order_idProperty() {
		return order_id;
	}
	public StringProperty customerProperty() {
		return customer;
	}
	public StringProperty statusProperty() {
		return status;
	}
	public StringProperty ProductProperty() {
		return Product;
	}
	public DoubleProperty totalProperty() {
		return total;
	}
	public boolean isCheckbox() { return checkbox.get(); }
  public void setCheckbox(boolean value) { checkbox.set(value); }
  public SimpleBooleanProperty checkboxProperty() { return checkbox; }

	public int getorder_id() {
		return order_id.get();
	}
	public String getcustomer() {
		return customer.get();
	}
	public String getProduct() {
		return Product.get();
	}
	public String getstatus() {
		return status.get();
	}
	public double gettotal() {
		return total.get();
	}


}
package Starter_App.Starter_Apmodel;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

//---------------------
//----Unit- Testing----
//---------------------

public class TableViewmodel{
	private SimpleIntegerProperty order_id;
	private SimpleStringProperty customer;
	private SimpleStringProperty Product;
	private SimpleStringProperty date;
	private SimpleStringProperty status;
	private SimpleDoubleProperty Total;
	private SimpleBooleanProperty checkbox;
	private final SimpleStringProperty payment;
	
	
	public TableViewmodel(int orderId, String Customer, String Date, Boolean CB, String product, String Status, Double total, String payment){
		this.order_id = new SimpleIntegerProperty(orderId);
		this.customer = new SimpleStringProperty(Customer);
		this.Product = new SimpleStringProperty(product);
		this.date = new SimpleStringProperty(Date);
		this.status = new SimpleStringProperty(Status);
		this.Total = new SimpleDoubleProperty(total);
		this.checkbox = new SimpleBooleanProperty(CB);
		this.payment = new SimpleStringProperty(payment);
		
	}
	//---Order---
	public int getOrderId() {return order_id.get();}
	public void setOrderId(int value) {order_id.set(value);}
	public SimpleIntegerProperty orderIdProperty() {return order_id; }
	 
	    // --- Customer ---
	public String getCustomer() {return customer.get();}
	public void setCustomer(String value) {customer.set(value);}
	public SimpleStringProperty customerProperty() {return customer;}
	
	    //--Product---
	    public String getProduct() { return Product.get(); }
	    public void setProduct(String value) { Product.set(value); }
	    public SimpleStringProperty productProperty() { return Product; }

	  //---Date---
	    public String getDate() { return date.get(); }
	    public void setDate(String value) { date.set(value); }
	    public SimpleStringProperty dateProperty() { return date; }

	    //---Status---
	    public String getStatus() { return status.get(); }
	    public void setStatus(String value) { status.set(value); }
	    public SimpleStringProperty statusProperty() { return status; }

	    //---Total---
	    public double getTotal() { return Total.get(); }
	    public void setTotal(double value) { Total.set(value); }
	    public SimpleDoubleProperty totalProperty() { return Total; }

	   //---Checkbox---
	    public boolean isCheckbox() { return checkbox.get(); }
	    public void setCheckbox(boolean value) { checkbox.set(value); }
	    public SimpleBooleanProperty checkboxProperty() { return checkbox; }
	    
	    //---Payment---
	    public String getPayment() { return payment.get(); }
	    public void setPayment(String value) { payment.set(value); }
	    public SimpleStringProperty paymentProperty() { return payment; }
} package Starter_App.Starter_Apmodel;
import javafx.beans.property.DoubleProperty;  //Douuble object
import javafx.beans.property.IntegerProperty;  // Integer object 
import javafx.beans.property.SimpleDoubleProperty;  // To make the use of the new variable 
import javafx.beans.property.SimpleIntegerProperty;// To make the use of the new variable 
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;      // String Object 

//---------------------
//----Unit- Testing----
//---------------------


public class TableWorkmodel {
	
	private final IntegerProperty order_id = new SimpleIntegerProperty();
	private final StringProperty customer = new SimpleStringProperty();
	private final StringProperty Product = new SimpleStringProperty();
	private final StringProperty status = new SimpleStringProperty();
	private final DoubleProperty total = new SimpleDoubleProperty();
	
	
	public TableWorkmodel(int order_id, String customer, String Product, String status, double total) {
		this.order_id.set(order_id);
		this.customer.set(customer);
		this.status.set(status);
		this.total.set(total);
		this.Product.set(Product);
	}

	public IntegerProperty order_idProperty() {
		return order_id;
	}
	public StringProperty customerProperty() {
		return customer;
	}
	public StringProperty statusProperty() {
		return status;
	}
	public StringProperty ProductProperty() {
		return Product;
	}
	public DoubleProperty totalProperty() {
		return total;
	}

	public int getorder_id() {
		return order_id.get();
	}
	public String getcustomer() {
		return customer.get();
	}
	public String getProduct() {
		return Product.get();
	}
	public String getstatus() {
		return status.get();
	}
	public double gettotal() {
		return total.get();
	}
}

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
}
package Starter_App.Starter_Ap;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DashboardController {
    @FXML private Button Dashboard;
    @FXML private Button OrdersManagement;

    // These fx:id values must match the labels inside the five picture cards.
    @FXML private Label TotalOrderID;
    @FXML private Label ProcessingorderID;
    @FXML private Label ShippedorderID;
    @FXML private Label Deliveredorderid;
    @FXML private Label Returnedorderid;

    @FXML private VBox topProductsChart;

    @FXML
    public void initialize() {
        loadOrderCount();
        if (topProductsChart != null) {
            loadTopProducts();
        }
    }

    private void loadOrderCount() {
        /*
         * COUNT(Processing) does not count rows whose Status is Processing.
         * This query counts each Status value separately and gives the Total Orders card
         * its value at the same time.
         */
        String sql = """
            SELECT COUNT(*) AS total_orders,
                   SUM(CASE WHEN Status = 'Processing' THEN 1 ELSE 0 END) AS processing_orders,
                   SUM(CASE WHEN Status = 'Shipping' THEN 1 ELSE 0 END) AS shipped_orders,
                   SUM(CASE WHEN Status = 'Delivery' THEN 1 ELSE 0 END) AS delivered_orders,
                   SUM(CASE WHEN Status = 'Return' THEN 1 ELSE 0 END) AS returned_orders
            FROM Orders
            """;

        try (PreparedStatement statement = App.getConnection().prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                TotalOrderID.setText(String.valueOf(result.getInt("total_orders")));
                ProcessingorderID.setText(String.valueOf(result.getInt("processing_orders")));
                ShippedorderID.setText(String.valueOf(result.getInt("shipped_orders")));
                Deliveredorderid.setText(String.valueOf(result.getInt("delivered_orders")));
                Returnedorderid.setText(String.valueOf(result.getInt("returned_orders")));
            }
        } catch (SQLException e) {
            System.out.println("Error loading order counts:");
            e.printStackTrace();
        }
    }

    private void loadTopProducts() {
        String sql = "SELECT Product, SUM(quantity) AS total "
                + "FROM Orders GROUP BY Product ORDER BY total DESC LIMIT 5";
        Connection connection = App.getConnection();
        if (connection == null) return;

        topProductsChart.getChildren().clear();
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sql)) {
            while (results.next()) {
                Label product = new Label(results.getString("Product"));
                product.setTextFill(javafx.scene.paint.Color.WHITE);
                product.setPrefWidth(170);

                Label total = new Label(String.valueOf(results.getInt("total")));
                total.setTextFill(javafx.scene.paint.Color.WHITE);
                total.setPrefWidth(40);
                total.setAlignment(Pos.CENTER_RIGHT);

                HBox row = new HBox(10, product, total);
                row.setPrefWidth(240);
                row.setAlignment(Pos.CENTER_LEFT);
                row.getStyleClass().add("top-product-row");
                topProductsChart.getChildren().add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboardButton(ActionEvent event) {
        try {
            App.setRoot(App.Views.Dashboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrdersButton(ActionEvent event) {
        try {
            App.setRoot(App.Views.Order);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Sidebar(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (clickedButton == Dashboard) handleDashboardButton(event);
        if (clickedButton == OrdersManagement) handleOrdersButton(event);
    }
}

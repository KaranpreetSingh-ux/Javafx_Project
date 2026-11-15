package Starter_App.Starter_Ap;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    @FXML private TableView<Order_usermodel> ordersTable;
    @FXML private TableColumn<Order_usermodel, Void> actionColumn;
    @FXML private TableColumn<Order_usermodel, String> Order_idcolumn;
    @FXML private TableColumn<Order_usermodel, String> Customercolumn;
    @FXML private TableColumn<Order_usermodel, String> Productcolumn;
    @FXML private TableColumn<Order_usermodel, String> Status;
    @FXML private TableColumn<Order_usermodel, String> Total;
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

    private final ObservableList<Order_usermodel> orderList = FXCollections.observableArrayList();
    private Order_usermodel selectedOrder;

    @FXML
    private void initialize() {
        Order_idcolumn.setCellValueFactory(new PropertyValueFactory<>("orderid"));
        Customercolumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        Productcolumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        Total.setCellValueFactory(new PropertyValueFactory<>("total"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));

        cancelReasonComboBox.setItems(FXCollections.observableArrayList(
            "Ordered by mistake",
            "Wrong product selected",
            "Need to change address",
            "Price is too high",
            "Other"
        ));

        setupActionColumn();
        loadOrderData();
        totalOrdersLabel.setText(String.valueOf(orderList.size()));
        loadActivityChart();
        loadRecommendations();
        setupSearchBar();
    }

    private void loadOrderData() {
        orderList.clear();
        String sql = "SELECT * FROM users LIMIT 6";

        try {
            if (App.databaseConnection != null) {
                PreparedStatement stmnt = App.databaseConnection.prepareStatement(sql);
                ResultSet results = stmnt.executeQuery();

                while (results.next()) {
                    int orderId = results.getInt("Order_id");
                    String customer = results.getString("customer");
                    String product = results.getString("product");
                    String status = results.getString("status");
                    String address = results.getString("address");
                    double total = results.getDouble("total");

                    Order_usermodel rowData = new Order_usermodel(orderId, customer, product, status, address, total);
                    orderList.add(rowData);
                }

                results.close();
                stmnt.close();
            } else {
                System.out.println("Database connection is not working in the user order table");
            }
        } catch (SQLException s) {
            System.out.println("The SQL order data was not found");
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

    @FXML
    private void handleConfirmOrder() {
        updateSelectedOrderStatus("Confirmed");
        closePopups();
    }

    @FXML
    private void handleCancelOrder() {
        updateSelectedOrderStatus("Cancelled");
        closePopups();
    }

    private void updateSelectedOrderStatus(String status) {
        if (selectedOrder == null) {
            return;
        }

        try {
            Method setter = selectedOrder.getClass().getMethod("setStatus", String.class);
            setter.invoke(selectedOrder, status);
        } catch (Exception e) {
            System.out.println("Add setStatus(String status) in Order_usermodel to update status in the table.");
        }

        ordersTable.refresh();
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
        TableSearchUtil.connectSearch(
            searchField,
            ordersTable,
            orderList,
            order -> readValue(order, "orderid"),
            order -> readValue(order, "product"),
            order -> readValue(order, "customer")
        );
    }

    private void loadActivityChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Mouse", 4));
        series.getData().add(new XYChart.Data<>("Notebook", 3));
        series.getData().add(new XYChart.Data<>("Lamp", 2));
        series.getData().add(new XYChart.Data<>("Bottle", 2));
        activityBarChart.getData().setAll(series);
    }

    private void loadRecommendations() {
        recommendationOneLabel.setText("Wireless Mouse");
        recommendationTwoLabel.setText("Notebook Pack");
        recommendationThreeLabel.setText("Desk Lamp");
        recommendationFourLabel.setText("Travel Bottle");
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
}

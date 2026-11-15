package Starter_App.Starter_Ap;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/** Supplies live user-specific values to UserDashboard.fxml. */
public class UserDashboardController {
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

    @FXML
    private void initialize() {
        // FXML fields are injected before this method runs.
        marketNames = List.of(marketProduct1Label, marketProduct2Label, marketProduct3Label,
                marketProduct4Label, marketProduct5Label);
        marketSales = List.of(marketProduct1SalesLabel, marketProduct2SalesLabel, marketProduct3SalesLabel,
                marketProduct4SalesLabel, marketProduct5SalesLabel);
        recommendationNames = List.of(recommendedProduct1Label, recommendedProduct2Label, recommendedProduct3Label,
                recommendedProduct4Label, recommendedProduct5Label);
    }

    /**
     * Call this after fetching the current user's orders/products from your database.
     * The first list is the market-wide top five; the second is personalised for the user.
     */
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

    @FXML private void refreshDashboard() { /* Re-query data here, then call setDashboardData(...). */ }
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
}

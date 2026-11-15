package Starter_App.Starter_Ap;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/** Controls the five, single-row order summary cards. */
public class OrderOverviewController {
    @FXML private Label processingCountLabel;
    @FXML private Label shippedCountLabel;
    @FXML private Label returnedCountLabel;
    @FXML private Label deliveredCountLabel;
    @FXML private Label totalOrdersCountLabel;

    /** Call after loading the current user's order counts. */
    public void setOrderCounts(int processing, int shipped, int returned, int delivered) {
        processingCountLabel.setText(String.valueOf(processing));
        shippedCountLabel.setText(String.valueOf(shipped));
        returnedCountLabel.setText(String.valueOf(returned));
        deliveredCountLabel.setText(String.valueOf(delivered));

        int totalOrders = processing + shipped + returned + delivered;
        totalOrdersCountLabel.setText(String.valueOf(totalOrders));
    }
}

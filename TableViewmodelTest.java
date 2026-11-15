package Starter_App.Starter_Ap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableViewmodelTest {

    @Test
    void constructorStoresAllFieldsCorrectly() {
        TableViewmodel row = new TableViewmodel(
                101, "Alice", "2026-07-01", false,
                "Wireless Mouse", "Processing", 24.99, "Card");

        assertEquals(101, row.getOrderId());
        assertEquals("Alice", row.getCustomer());
        assertEquals("2026-07-01", row.getDate());
        assertEquals("Wireless Mouse", row.getProduct());
        assertEquals("Processing", row.getStatus());
        assertEquals(24.99, row.getTotal(), 0.0001);
        assertFalse(row.isCheckbox());
        assertEquals("Card", row.getPayment());
    }

    @Test
    void setStatusUpdatesTheStatusProperty() {
        TableViewmodel row = new TableViewmodel(
                1, "Bob", "2026-07-02", false, "Keyboard", "Processing", 15.0, "N/A");

        row.setStatus("Cancelled");

        assertEquals("Cancelled", row.getStatus());
        assertEquals("Cancelled", row.statusProperty().get());
    }

    @Test
    void checkboxTogglesIndependentlyOfOtherFields() {
        TableViewmodel row = new TableViewmodel(
                2, "Cara", "2026-07-03", false, "Lamp", "Shipping", 9.5, "N/A");

        row.setCheckbox(true);

        assertTrue(row.isCheckbox());
        assertEquals(2, row.getOrderId()); // unaffected
    }
}

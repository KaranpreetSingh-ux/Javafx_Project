/*package Starter_App.Starter_ApUnitTesting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Starter_App.Starter_Ap.Controller.DeliveredControllor;
import Starter_App.Starter_Ap.Controller.ProcessingData;
import Starter_App.Starter_Ap.Controller.Return_Controllor;
import Starter_App.Starter_Ap.Controller.Shipping_Controllor;


//---------------------
//----Unit- Testing----
//---------------------

public class StatusFiltertesy {
	 @Test
	    void baseProcessingDataFiltersOnProcessing() {
	        ProcessingData controller = new ProcessingData();
	        assertEquals("Processing", controller.statusFilter());
	    }

	    @Test
	    void deliveredControllorFiltersOnDelivered() {
	        DeliveredControllor controller = new DeliveredControllor();
	        assertEquals("Delivered", controller.statusFilter());
	    }

	    @Test
	    void shippingControllorFiltersOnShipping() {
	        Shipping_Controllor controller = new Shipping_Controllor();
	        assertEquals("Shipping", controller.statusFilter());
	    }

	    @Test
	    void returnControllorFiltersOnReturned() {
	        Return_Controllor controller = new Return_Controllor();
	        assertEquals("Returned", controller.statusFilter());
	    }

}
*/
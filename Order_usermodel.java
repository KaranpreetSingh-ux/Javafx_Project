package Starter_App.Starter_Ap.Model;

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

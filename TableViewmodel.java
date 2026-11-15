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
} 
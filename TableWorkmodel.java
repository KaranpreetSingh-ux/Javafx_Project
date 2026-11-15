package Starter_App.Starter_Apmodel;
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

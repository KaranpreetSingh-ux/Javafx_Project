package Starter_App.Starter_Apmodel;
//This is use for changing the value
//And also for the table view
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//---------------------
//----Unit- Testing----
//---------------------


/*private int order_id;
private String customer;
private double total;  with the use of this the program dose not become properly the javafx */

public class Ordermodel {
	private final IntegerProperty order_id = new SimpleIntegerProperty();
	private final StringProperty customer = new SimpleStringProperty();
	private final StringProperty Product = new SimpleStringProperty();
	private final StringProperty status = new SimpleStringProperty();
	private final DoubleProperty total = new SimpleDoubleProperty();
	private SimpleBooleanProperty checkbox= new SimpleBooleanProperty(false);


	public Ordermodel(int order_id, String customer, String Product, String status, double total) {
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
	public boolean isCheckbox() { return checkbox.get(); }
    public void setCheckbox(boolean value) { checkbox.set(value); }
    public SimpleBooleanProperty checkboxProperty() { return checkbox; }

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

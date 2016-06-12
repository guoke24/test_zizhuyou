package guoke.bean;

import java.io.Serializable;

public class TicketBean implements Serializable{

	String name;
	double price;
	String price_mk;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getPrice_mk() {
		return price_mk;
	}
	public void setPrice_mk(String price_mk) {
		this.price_mk = price_mk;
	}
	
	
}

package guoke.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class TicketItem extends BmobObject implements Serializable{

	private String name;
	private String datail;
	private Double price;
	private String attrac_id;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDatail() {
		return datail;
	}
	public void setDatail(String detail) {
		this.datail = detail;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getAttrac_id() {
		return attrac_id;
	}
	public void setAttrac_id(String attrac_id) {
		this.attrac_id = attrac_id;
	}
	
	
}

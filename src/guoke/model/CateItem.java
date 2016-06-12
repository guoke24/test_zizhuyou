package guoke.model;

import cn.bmob.v3.BmobObject;

public class CateItem extends BmobObject{

	private String city_id;
	
	private String name;
	private String type;
	private Double price;
	private String detail;
	
	private String pricedetail;
	private String locationtext;
	
	public String getCity_id() {
		return city_id;
	}
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getPricedetail() {
		return pricedetail;
	}
	public void setPricedetail(String pricedetail) {
		this.pricedetail = pricedetail;
	}
	public String getLocationtext() {
		return locationtext;
	}
	public void setLocationtext(String locationtext) {
		this.locationtext = locationtext;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
	
}

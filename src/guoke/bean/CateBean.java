package guoke.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class CateBean extends BmobObject implements Serializable{

	private String name;
	private String city;
	private String area;
	private String address;
	private String tags;
	private String photos;
	private double avg_price;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public double getAvg_price() {
		return avg_price;
	}
	public void setAvg_price(double avg_price) {
		this.avg_price = avg_price;
	}
	
}

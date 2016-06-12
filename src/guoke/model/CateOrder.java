package guoke.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class CateOrder extends BmobObject implements Serializable{

	//编程订单号之后追加的
	private String user_id;//关联的用户
	private String user_name;
	private String user_phone;
	private Double sumFee;//总价钱
	private Integer CateNum;//预订的份数
	private Integer status;
	
	//美食表的内容
	private String name;
	private String city;
	private String area;
	private String address;
	private String tags;
	private String photos;
	private Double avg_price;
	
	
	public Double getAvg_price() {
		return avg_price;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setCateNum(Integer cateNum) {
		CateNum = cateNum;
	}
	public void setAvg_price(Double avg_price) {
		this.avg_price = avg_price;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Double getSumFee() {
		return sumFee;
	}
	public void setSumFee(Double sumFee) {
		this.sumFee = sumFee;
	}
	public int getCateNum() {
		return CateNum;
	}
	public void setCateNum(int cateNum) {
		CateNum = cateNum;
	}
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
	
	
}

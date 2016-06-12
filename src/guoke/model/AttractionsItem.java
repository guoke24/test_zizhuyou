package guoke.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class AttractionsItem extends BmobObject implements Serializable{

	String name;
	String detail; //开放时间
	Double bottomPrice; //最低价格
	String location_text; //位置
	String describe;//描述 
	Integer city_id;
	BmobFile picBmobFile;
	
	
	
	public BmobFile getPicBmobFile() {
		return picBmobFile;
	}
	public void setPicBmobFile(BmobFile picBmobFile) {
		this.picBmobFile = picBmobFile;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getLocation_text() {
		return location_text;
	}
	public void setLocation_text(String location_text) {
		this.location_text = location_text;
	}
	public Integer getCity_id() {
		return city_id;
	}
	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}
	public Double getBottomPrice() {
		return bottomPrice;
	}
	public void setBottomPrice(Double bottomPrice) {
		this.bottomPrice = bottomPrice;
	}
	
}

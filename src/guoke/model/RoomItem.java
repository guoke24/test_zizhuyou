package guoke.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/*
 * 
 */
public class RoomItem extends BmobObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1359639582951817597L;
	private Integer id;//房间id
	private String name;//房型名
	private Integer hotel_id;//关联的酒店id
	private Double price;//价格
	private String detail;//详情
	
	private String room_size;//房间大小
	private String bed_kind;//床的类型
	private String bed_size;//床的大小
	private String wifi;//wifi情况
	private String broadband;//宽带情况
	private String floor;//楼层
	private String people_number;//可住人数
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(Integer hotel_id) {
		this.hotel_id = hotel_id;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getRoom_size() {
		return room_size;
	}
	public void setRoom_size(String room_size) {
		this.room_size = room_size;
	}
	public String getBed_kind() {
		return bed_kind;
	}
	public void setBed_kind(String bed_kind) {
		this.bed_kind = bed_kind;
	}
	public String getBed_size() {
		return bed_size;
	}
	public void setBed_size(String bed_size) {
		this.bed_size = bed_size;
	}
	public String getWifi() {
		return wifi;
	}
	public void setWifi(String wifi) {
		this.wifi = wifi;
	}
	public String getBroadband() {
		return broadband;
	}
	public void setBroadband(String broadband) {
		this.broadband = broadband;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getPeople_number() {
		return people_number;
	}
	public void setPeople_number(String people_number) {
		this.people_number = people_number;
	}

	
}

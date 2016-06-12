package guoke.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

public class RoomBean implements Serializable{

	String roomName;
	String area;
	String bed;
	String price;
	String breakfast;
	
	
	
	@Override
	public String toString() {
		return "RoomBean [roomName=" + roomName + ", area=" + area + ", bed="
				+ bed + ", price=" + price + ", breakfast=" + breakfast + "]";
	}
	public String getBreakfast() {
		return breakfast;
	}
	public void setBreakfast(String breakfast) {
		this.breakfast = breakfast;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getBed() {
		return bed;
	}
	public void setBed(String bed) {
		this.bed = bed;
	}
	


	
}

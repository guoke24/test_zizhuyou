package guoke.bean;

import java.io.Serializable;

public class HotelBean implements Serializable{

	Integer id;
	String name;
	String className;
	String intro;
	String Lat;
	String Lon;
	String address;
	String largePic;
	String manyidu;
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getLat() {
		return Lat;
	}
	public void setLat(String lat) {
		Lat = lat;
	}
	public String getLon() {
		return Lon;
	}
	public void setLon(String lon) {
		Lon = lon;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLargePic() {
		return largePic;
	}
	public void setLargePic(String largePic) {
		this.largePic = largePic;
	}
	public String getManyidu() {
		return manyidu;
	}
	public void setManyidu(String manyidu) {
		this.manyidu = manyidu;
	}
	
	
	
	
}

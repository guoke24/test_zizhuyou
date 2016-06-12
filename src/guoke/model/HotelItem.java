package guoke.model;



import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class HotelItem extends BmobObject implements Serializable{

	private Integer hid;
	private String hname;
	private String hregion;
	private Double bottomprice;
	private String sitedetail;
	private Integer county_id;
	private Integer city_id;
	private BmobFile picBmobFile;
	private String pic_url;
	
	public BmobFile getPicBmobFile(){
		//picBmobFile = new BmobFile(hname+".png", null, pic_url);
		return picBmobFile;
	}
	
	
	
	public void setPicBmobFile(BmobFile picBmobFile) {
		this.picBmobFile = picBmobFile;
	}



	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public Integer getHid() {
		return hid;
	}
	public void setHid(Integer hid) {
		this.hid = hid;
	}
	public String getHname() {
		return hname;
	}
	public void setHname(String hname) {
		this.hname = hname;
	}
	public String getHregion() {
		return hregion;
	}
	public void setHregion(String hregion) {
		this.hregion = hregion;
	}
	public Double getBottomprice() {
		return bottomprice;
	}
	public void setBottomprice(Double bottomprice) {
		this.bottomprice = bottomprice;
	}
	public String getSitedetail() {
		return sitedetail;
	}
	public void setSitedetail(String sitedetail) {
		this.sitedetail = sitedetail;
	}
	public Integer getCounty_id() {
		return county_id;
	}
	public void setCounty_id(Integer county_id) {
		this.county_id = county_id;
	}
	public Integer getCity_id() {
		return city_id;
	}
	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}
	
	
	
	
}

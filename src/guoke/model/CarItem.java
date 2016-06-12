package guoke.model;

import java.io.Serializable;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class CarItem extends BmobObject implements Serializable{

	private String cname;
	private String cdetail;
	private Double cprice; //每天的价格
	private Double cpremium ;//保险费
	private Double cdriverfee = 60.0;
	
	private String cbrand;//品牌
	private Integer city_id;//所在城市id
	
	private BmobFile picBmobFile;
	
	
	
	public Double getCdriverfee() {
		return cdriverfee;
	}


	public BmobFile getPicBmobFile() {
		return picBmobFile;
	}

	public void setPicBmobFile(BmobFile picBmobFile) {
		this.picBmobFile = picBmobFile;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCdetail() {
		return cdetail;
	}

	public void setCdetail(String cdetail) {
		this.cdetail = cdetail;
	}

	public Double getCprice() {
		return cprice;
	}

	public void setCprice(Double cprice) {
		this.cprice = cprice;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public Double getCpremium() {
		return cpremium;
	}

	public void setCpremium(Double cpremium) {
		this.cpremium = cpremium;
	}

	public String getCbrand() {
		return cbrand;
	}

	public void setCbrand(String cbrand) {
		this.cbrand = cbrand;
	}
	
}

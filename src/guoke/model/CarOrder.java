package guoke.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class CarOrder extends BmobObject{
							//订单后使用自带的objectid
	private String user_id; //关联下单的用户id
	private String car_id; //关联车的id
	private String car_name;//车的名字
	private String car_detail;//车的描述
								//下单日期自带
	private BmobDate getCarDate;//取车日期
	private BmobDate returnCarDate;//还车日期
	private Integer usecardays;
	private String getcarcity;
	
	private Double dayfee;
	private Double premium;
	private Double driverfee;
	private Double fee; //总费用

	private Boolean isDriver;
	private Boolean isNote;
	private Integer status;
	
	private String carpic;
	
	
	
	public String getCarpic() {
		return carpic;
	}

	public void setCarpic(String carpic) {
		this.carpic = carpic;
	}

	public Integer getUsecardays() {
		return usecardays;
	}

	public void setUsecardays(Integer usecardays) {
		this.usecardays = usecardays;
	}

	public String getGetcarcity() {
		return getcarcity;
	}

	public void setGetcarcity(String getcarcity) {
		this.getcarcity = getcarcity;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getIsDriver() {
		return isDriver;
	}

	public void setIsDriver(Boolean isDriver) {
		this.isDriver = isDriver;
	}

	public Boolean getIsNote() {
		return isNote;
	}

	public void setIsNote(Boolean isNote) {
		this.isNote = isNote;
	}

	public Double getDayfee() {
		return dayfee;
	}

	public void setDayfee(Double dayfee) {
		this.dayfee = dayfee;
	}

	public Double getPremium() {
		return premium;
	}

	public void setPremium(Double premium) {
		this.premium = premium;
	}

	public Double getDriverfee() {
		return driverfee;
	}

	public void setDriverfee(Double driverfee) {
		this.driverfee = driverfee;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCar_id() {
		return car_id;
	}

	public void setCar_id(String car_id) {
		this.car_id = car_id;
	}

	public String getCar_name() {
		return car_name;
	}

	public void setCar_name(String car_name) {
		this.car_name = car_name;
	}

	public String getCar_detail() {
		return car_detail;
	}

	public void setCar_detail(String car_detail) {
		this.car_detail = car_detail;
	}

	public BmobDate getGetCarDate() {
		return getCarDate;
	}

	public void setGetCarDate(BmobDate getCarDate) {
		this.getCarDate = getCarDate;
	}

	public BmobDate getReturnCarDate() {
		return returnCarDate;
	}

	public void setReturnCarDate(BmobDate returnCarDate) {
		this.returnCarDate = returnCarDate;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}
	
	
	
}

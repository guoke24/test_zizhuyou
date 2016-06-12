package guoke.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class RoomOrder extends BmobObject{
	//关联的id2个
	private Integer id;		 //由于没有自增，只能用自带的objectid
	private String user_id; //关联下单的用户id
	private String username;
	private String userphone;
	
	private Integer hotel_id; //预订酒店的id
	private String hotel_name;//酒店的名字
	private String room_name;//房型的名字
	private String area;
	private String bed;
	private String price;
	private String breakfast;
	
	//入住信息2个					 //下单时间，修改时间 自带了
	private BmobDate dateCheckIn;//入住时间
	private BmobDate dateCheckOut;//离开时间
	//
	private Double fee; //费用
	//入住人的信息三个
	private String nameCheckIn; //入住人姓名，不一定是用户本人
	private String phoneCheckIn;//入住人联系信息
	private BmobDate dateArrive;//这个有待研究，需要时间选择插件
	//
	private Integer statu;//订单状态，0表示进行中，1表示完成，2表示已取消
	
	private String location;
	private String days;
	
	
	
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserphone() {
		return userphone;
	}
	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}
	public Integer getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(Integer hotel_id) {
		this.hotel_id = hotel_id;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getBreakfast() {
		return breakfast;
	}
	public void setBreakfast(String breakfast) {
		this.breakfast = breakfast;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public BmobDate getDateCheckIn() {
		//dateCheckIn.
		return dateCheckIn;
	}
	public void setDateCheckIn(BmobDate dateCheckIn) {
		this.dateCheckIn = dateCheckIn;
	}
	public BmobDate getDateCheckOut() {
		return dateCheckOut;
	}
	public void setDateCheckOut(BmobDate dateCheckOut) {
		this.dateCheckOut = dateCheckOut;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public String getNameCheckIn() {
		return nameCheckIn;
	}
	public void setNameCheckIn(String nameCheckIn) {
		this.nameCheckIn = nameCheckIn;
	}
	public String getPhoneCheckIn() {
		return phoneCheckIn;
	}
	public void setPhoneCheckIn(String phoneCheckIn) {
		this.phoneCheckIn = phoneCheckIn;
	}
	public BmobDate getDateArrive() {
		return dateArrive;
	}
	public void setDateArrive(BmobDate dateArrive) {
		this.dateArrive = dateArrive;
	}
	public String getHotel_name() {
		return hotel_name;
	}
	public void setHotel_name(String hotel_name) {
		this.hotel_name = hotel_name;
	}
	public String getRoom_name() {
		return room_name;
	}
	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}
	public Integer getStatu() {
		return statu;
	}
	public void setStatu(Integer statu) {
		this.statu = statu;
	}
	
	
	
}

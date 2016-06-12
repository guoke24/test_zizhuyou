package guoke.model;

import cn.bmob.v3.BmobObject;

public class TicketOrder extends BmobObject{

	private String user_id;
	
	private String ticket_id;
	private String ticket_name;
	private String attrac_id;
	private String attrac_name;
	private String attrac_address;
	
	private Double price;
	private Integer num;
	private String Staute;
	private Integer statues;
	private String username;
	private String userphone;
	
	private Double sumfee;
	
	
	
	public String getAttrac_address() {
		return attrac_address;
	}
	public void setAttrac_address(String attrac_address) {
		this.attrac_address = attrac_address;
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
	public Double getSumfee() {
		return sumfee;
	}
	public void setSumfee(Double sumfee) {
		this.sumfee = sumfee;
	}

	
	
	public Integer getStatues() {
		return statues;
	}
	public void setStatues(Integer statues) {
		this.statues = statues;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(String ticket_id) {
		this.ticket_id = ticket_id;
	}
	public String getTicket_name() {
		return ticket_name;
	}
	public void setTicket_name(String ticket_name) {
		this.ticket_name = ticket_name;
	}
	public String getAttrac_id() {
		return attrac_id;
	}
	public void setAttrac_id(String attrac_id) {
		this.attrac_id = attrac_id;
	}
	public String getAttrac_name() {
		return attrac_name;
	}
	public void setAttrac_name(String attrac_name) {
		this.attrac_name = attrac_name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getStaute() {
		return Staute;
	}
	public void setStaute(String staute) {
		Staute = staute;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	
	
}

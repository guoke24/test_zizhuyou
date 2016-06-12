package guoke.model;

import java.io.Serializable;
import java.util.Date;

public class TravelInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MRegion addrItem;
	private Date date;
	private int durationDays;
	
	public MRegion getAddrItem() {
		return addrItem;
	}
	public void setAddrItem(MRegion addrItem) {
		this.addrItem = addrItem;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getDurationDays() {
		return durationDays;
	}
	public void setDurationDays(int durationDays) {
		durationDays = durationDays;
	}
	
	
}

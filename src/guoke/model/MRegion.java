package guoke.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class MRegion extends BmobObject implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -8192883469051701021L;
	private Integer cid;
	private Integer rid;
	private String rname;
	private Integer style;
	private Integer pid;
	private Integer siteid;
	
	
	
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public Integer getStyle() {
		return style;
	}
	public void setStyle(Integer style) {
		this.style = style;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getSiteid() {
		return siteid;
	}
	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}
	

	

}

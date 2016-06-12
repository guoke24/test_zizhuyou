package com.example.demo_zizhuyou;

import guoke.db.DBManager;
import guoke.model.MDate;
import guoke.model.MRegion;
import guoke.model.MyUser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class BaseActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.activity_base);
        Bmob.initialize(this, "11f5d324a420448854e4e161bddd8902");

	}

	 //将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
	
	private ProgressDialog progressDialog;
	
	/**
	 * 显示进度对话框
	 */
	public void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	/**
	 * 关闭进度对话框
	 */
	public void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	/** 
	 * toast something
	 * @param toastString
	 */
	protected void toast(String toastString){
		Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获取日期的XXyueXX日的形式
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected String getMonth_Day(Date date){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(date.getMonth()+1);
		stringBuilder.append("月");
		stringBuilder.append(date.getDate());
		String md = stringBuilder.toString();
		return md;
	
	}
	
	/**
	 * 记录 登录状态
	 * @param isLogin
	 */
	public void setLoginStatu(Boolean isLogin){
		SharedPreferences.Editor editor = getSharedPreferences("loginStatu", 0).edit();
		editor.clear();
		editor.putBoolean("isLogin", isLogin);
		editor.commit();
	}
 
	/**
	 * 获取 登陆状态 
	 * @return
	 */
	public Boolean getLoginStatu(){
		SharedPreferences pref = getSharedPreferences("loginStatu",0);		
		return pref.getBoolean("isLogin", false);
	}
	
	/**
	 * 记录登陆的账户到shareprefence中
	 * @param user
	 */
	public void setUserInPreference(MyUser user){
		SharedPreferences.Editor editor = getSharedPreferences("userInfo", 0).edit();
		editor.clear();
		editor.putString("objectid", user.getObjectId());
		editor.putString("username", user.getUsername());
		editor.putString("mobilePhoneNumber", user.getMobilePhoneNumber());
		editor.commit();
	}
	
	/**
	 * 从shareprefence获取到登陆用户的信息
	 * @return
	 */
	public MyUser getUserFromPreferences(){
		MyUser myUser = new MyUser();
		SharedPreferences pref = getSharedPreferences("userInfo", 0);
		myUser.setObjectId(pref.getString("objectid", ""));
		myUser.setUsername(pref.getString("username", ""));
		myUser.setMobilePhoneNumber(pref.getString("mobilePhoneNumber", ""));
		return myUser;
	}

	public Boolean isLogin_InBase(){
		MyUser userInfo = BmobUser.getCurrentUser(this,MyUser.class);
		if(userInfo != null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 计算两个日期之间的天数，返回字符串
	 * @param beginDate
	 * @param finishDate
	 * @return
	 */
	public String getDuraionDays(MDate beginDate,MDate finishDate){
		
		
		return String.valueOf(getDuraionDaysInteger(beginDate, finishDate));
	}
	/**
	 * 计算两个日期之间的天数，返回字符串
	 * @param beginDate
	 * @param finishDate
	 * @return
	 */
	public Integer getDuraionDaysInteger(MDate beginDate,MDate finishDate){
		long beginday = beginDate.getTime()/86400000;
		//加上一天过去的毫秒数
		int h = new Date().getHours();
		long left = 86400000*h/24;
		
		long  finishday= (finishDate.getTime())/86400000;
		long durtion = finishday-beginday;
		return (int) durtion;
	}	
	
	public int getCityIdByRegion(MRegion r){
		
		{
			int cityid = 0;
			String cityname = r.getRname().replace("市", ""); 
			//get the instance of db
			initBataBase();
		 	try {
		 		//qeury the city form the db
			 	String sql = "select cityId from travelcitys where cityName like '"+cityname+"'";
		        Cursor cursor = db.rawQuery(sql,null);  
		        cursor.moveToPrevious();
		        while (cursor.moveToNext()){ 
		        	int id  = cursor.getInt(cursor.getColumnIndex("cityId"));
			        if(id>35) cityid =id;
		        	//cursor.moveToNext();
		        	//cityid = cursor.getInt(cursor.getColumnIndex("cityId"));
		        }
		        cursor.close();
			} catch (Exception e) {
				// TODO: handle exception
			}finally{			
				closeBataBase();
			}
//			if(region == null){
//				toast("选择城市或地区无效，恢复默认值");
//				region = new MRegion();
//				region.setRid(349);
//				region.setRname("桂林市");
//				region.setStyle(2);
//				region.setPid(22);
//				region.setSiteid(0);
//			}
		 	toast("选择完成，城市id:"+cityid);
			return cityid;
		}
		//		MRegion region = new MRegion();
//		region.setRid(349);
//		region.setRname("桂林市");
//		region.setStyle(2);
//		region.setPid(22);
//		region.setSiteid(0);
	//	return cityid;
		
	}
	
	private DBManager dbm;
	private SQLiteDatabase db;
	
	private void initBataBase(){
		dbm = new DBManager(BaseActivity.this);
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	}
	
	private void closeBataBase(){
		dbm.closeDatabase();
		db.close();
	}
	
	public MRegion getRegionObjectWithCursor(Cursor cursor){
		//先获取值
		Integer rid = new Integer(cursor.getInt(cursor.getColumnIndex("rid")));
		String rname = cursor.getString(cursor.getColumnIndex("rname"));
		Integer style = new Integer(cursor.getInt(cursor.getColumnIndex("style")));
		Integer pid = new Integer(cursor.getInt(cursor.getColumnIndex("pid"))); 
		//给Region对象赋值
		MRegion region = new MRegion(); 
		region.setRid(rid);
		region.setRname(rname);
		region.setStyle(style);
		region.setPid(pid);
		
		return region;
	}
	
	
}

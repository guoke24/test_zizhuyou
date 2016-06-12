package guoke.home;

import java.sql.Date;
import java.util.Calendar;

import guoke.activity.CarListActivity;
import guoke.db.DBManager;
import guoke.fragment.BaseFragment;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;

import com.example.demo_zizhuyou.AddressSelectActivity;
import com.example.demo_zizhuyou.CalendarActivity;
import com.example.demo_zizhuyou.R;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CarFragment extends BaseFragment implements OnClickListener{

	
	private Button commitButton;//提交按钮
	private TextView getCar_DateTextView;//取车日期
	private TextView returnCar_DateTextView;//还车日期
	private TextView getCar_cityTextView;//取车城市
	private TextView returnCar_cityTextView;//还车城市
	private TextView getcar_pointTextView;//取车门店
	private TextView returncar_pointTextView;//取车门店
	private TextView usecar_datsTextView;//用车天数
	/*
	 * 这四个值是要传递的，都放在一个intent就可以多次传递了
	 */
	private MRegion getCar_Region;
	private MRegion returnCar_Region;
	private MDate getCar_Date;
	private MDate returnCar_Date;
	
	/**
	 * 给一个默认城市和日期的值
	 * @param date
	 */
	private void defaultRegionAndDate(){
		MRegion region = new MRegion();
		region.setRid(349);
		region.setRname("桂林市");
		region.setStyle(2);
		region.setPid(22);
		region.setSiteid(0);
		region.setCid(102);
		setGetCar_City(region);
		setReturnCar_City(region);
		
		//今天的date
		MDate todayDate = new MDate(Calendar.getInstance().getTimeInMillis());
		setGetCar_Date(todayDate);
		//明天的date
		MDate tomorrom = new MDate(todayDate.getTime()+86400000);
		setReturnCar_Date(tomorrom);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View carLayout = inflater.inflate(R.layout.homefragment_car_layout,
				container, false);
		init(carLayout);
		setListener();
		defaultRegionAndDate();
		return carLayout;
	}
	
	private void init(View v){
		commitButton = (Button)v.findViewById(R.id.commit_button_to_carlist);
		getCar_DateTextView = (TextView)v.findViewById(R.id.carFragment_getCar_date);
		returnCar_DateTextView = (TextView) v.findViewById(R.id.carFragment_returnCar_date);
		getCar_cityTextView = (TextView)v.findViewById(R.id.carFragment_getCar_city);
		returnCar_cityTextView = (TextView) v.findViewById(R.id.carFragment_returnCar_city);
		getcar_pointTextView = (TextView) v.findViewById(R.id.carFragment_getcar_point);
		returncar_pointTextView = (TextView) v.findViewById(R.id.carFragment_returnCar_point);
		usecar_datsTextView = (TextView) v.findViewById(R.id.carFragment_usecar_day);
	}
	
	private void setListener(){
		commitButton.setOnClickListener(this);
		getCar_DateTextView.setOnClickListener(this);
		getCar_cityTextView.setOnClickListener(this);
		getcar_pointTextView.setOnClickListener(this);
		returnCar_cityTextView.setOnClickListener(this);
		returnCar_DateTextView.setOnClickListener(this);
		returncar_pointTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.commit_button_to_carlist:
			commitButton_click();
			break;
		case R.id.carFragment_getCar_date:
			getCar_Date_click();
			break;
		case R.id.carFragment_getCar_city:
			getCar_city_click();
			break;
		case R.id.carFragment_getcar_point:
			break;
		case R.id.carFragment_returnCar_city:
			returnCar_City_click();
			break;
		case R.id.carFragment_returnCar_date:
			returnCar_Date_click();
			break;
		case R.id.carFragment_returnCar_point:
			break;
		default:
			break;
		}
	}
	
	/**
	 * 点击了去选车
	 */
	private void commitButton_click(){
		if(getCar_Date!=null&&returnCar_Date!=null){
			Intent intent = new Intent();
			intent.putExtra(InfoCom.fromWhere, InfoCom.fromCarList);
			intent.putExtra(InfoCom.Extra_GetCarCity, getCar_Region);
			intent.putExtra(InfoCom.Extra_ReturnCarCity, returnCar_Region);
			intent.putExtra(InfoCom.Extra_GetCarDate, getCar_Date);
			intent.putExtra(InfoCom.Extra_ReturnCarDate, returnCar_Date);
			intent.setClass(getActivity(), CarListActivity.class);
			getActivity().startActivity(intent);
		}else{
			toast("还有日期没选择");
		}
		
	}
	
	/**
	 * 点击【取车城市】时调用
	 */
	private void getCar_city_click(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), AddressSelectActivity.class);
		getActivity().startActivityForResult(intent, InfoCom.requestGetCarCity);
	}
	
	/**
	 * 点击【取车日期】时调用
	 */
	private void getCar_Date_click(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), CalendarActivity.class);
		getActivity().startActivityForResult(intent, InfoCom.requestGetCarDate);
	}
	/**
	 * 点击了【还车城市】时调用
	 */
	private void returnCar_City_click(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), AddressSelectActivity.class);
		getActivity().startActivityForResult(intent, InfoCom.requestReturnCarCity);
	}
	private void returnCar_Date_click(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), CalendarActivity.class);
		getActivity().startActivityForResult(intent, InfoCom.requestReturnCarDate);
			
	}

	/**
	 * 设置取车城市
	 * @param getCar_Region
	 */
	public void setGetCar_City(MRegion getCar_Region) {
		this.getCar_Region = getCar_Region;
		getCar_cityTextView.setText(getCityRegion(getCar_Region).getRname());
	}



	public void setReturnCar_City(MRegion returnCar_Region) {
		this.returnCar_Region = returnCar_Region;
		returnCar_cityTextView.setText(getCityRegion(returnCar_Region).getRname());
	}


	/**
	 * 设置取车日期
	 * @param getCar_Date
	 */
	public void setGetCar_Date(MDate getCar_Date) {
		this.getCar_Date = getCar_Date;
		if(isvaliatedDuration()){
			//设置 取车日期的显示
			getCar_DateTextView.setText(getMonth_Day(getCar_Date));
		}else{
			//getCar_Date = null;
			toast("请重新选择还车日期");
			getCar_DateTextView.setText(getMonth_Day(getCar_Date));
			
			returnCar_Date = null;
			returnCar_DateTextView.setText("   ");
		}
		
	}

	public void setReturnCar_Date(MDate returnCar_Date) {
		this.returnCar_Date = returnCar_Date;
		if(isvaliatedDuration()){
			returnCar_DateTextView.setText(getMonth_Day(returnCar_Date));
		}else{
			toast("[取车日期]必须早于[还车日期]");
			returnCar_Date = null;
			returnCar_DateTextView.setText("   ");
		}
	}
	

	
//	/**
//	 * Date 转为 X月X日
//	 * @param date
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	String getMonth_Day(MDate date){
//		StringBuilder stringBuilder = new StringBuilder();
//		stringBuilder.append(date.getMonth()+1);
//		stringBuilder.append("月");
//		stringBuilder.append(date.getDate());
//		String md = stringBuilder.toString();
//		return md;
//	}	
	
	/**
	 * 判断取车，还车的日期是否合法,合法就更新用车时间
	 * @return
	 */
	Boolean isvaliatedDuration(){
//		if(getCar_Date!=null&&returnCar_Date!=null){
//			if(returnCar_Date.getTime()-getCar_Date.getTime()>10000000){
//				//一天有86400000毫秒
//				long rday = returnCar_Date.getTime();
//				long gday = getCar_Date.getTime();
//				toast("rd="+rday+",gd="+gday);
//				//偏差值在80000000毫秒左右
//				long durtion = (rday-gday+88000000)/86400000;
//				usecar_datsTextView.setText(String.valueOf(durtion));
//				
//				return true;
//			}else{
//				usecar_datsTextView.setText("   ");
//				return false;
//			}
//		}else{
//			usecar_datsTextView.setText("   ");
//			return true;
//
//		}
		if(getCar_Date==null||returnCar_Date==null){
			usecar_datsTextView.setText("");
			return true;
		}else if(valiatedDuration(getCar_Date, returnCar_Date)){
			usecar_datsTextView.setText(getDuraionDays(getCar_Date, returnCar_Date));
			return true;
		}else{
			
			usecar_datsTextView.setText("");
			return false;
		}
		
		
	}


//	private DBManager dbm;
//	private SQLiteDatabase db;
//	
//	/**
//	 * 根据选择的县区，得到所在城市
//	 * @param mr
//	 * @return
//	 */
//	MRegion getCityRegion(MRegion r){
//		if(r.getStyle()==2){
//			return r;
//		}
//		else if(r.getStyle()==3){
//			Integer pid = r.getPid();
//			MRegion region = null;
//			//get the instance of db
//			initBataBase();
//		 	try {
//		 		//qeury the city form the db
//			 	String sql = "select * from Region where rid = "+String.valueOf(pid);
//		        Cursor cursor = db.rawQuery(sql,null);  
//		        cursor.moveToPrevious();
//		        while (cursor.moveToNext()){ 
//		        	region = getRegionObjectWithCursor(cursor);
//			        cursor.moveToNext();
//		        }
//		        cursor.close();
//			} catch (Exception e) {
//				// TODO: handle exception
//			}finally{			
//				closeBataBase();
//			}
//			if(region == null){
//				toast("选择城市或地区无效，恢复默认值");
//				region = new MRegion();
//				region.setRid(349);
//				region.setRname("桂林市");
//				region.setStyle(2);
//				region.setPid(22);
//				region.setSiteid(0);
//			}
//			return region;
//		}
//		toast("选择城市无效，恢复默认值");
//		MRegion region = new MRegion();
//		region.setRid(349);
//		region.setRname("桂林市");
//		region.setStyle(2);
//		region.setPid(22);
//		region.setSiteid(0);
//		return region;
//		
//	}
//	
//	private MRegion getRegionObjectWithCursor(Cursor cursor){
//		//先获取值
//		Integer rid = new Integer(cursor.getInt(cursor.getColumnIndex("rid")));
//		String rname = cursor.getString(cursor.getColumnIndex("rname"));
//		Integer style = new Integer(cursor.getInt(cursor.getColumnIndex("style")));
//		Integer pid = new Integer(cursor.getInt(cursor.getColumnIndex("pid"))); 
//		//给Region对象赋值
//		MRegion region = new MRegion(); 
//		region.setRid(rid);
//		region.setRname(rname);
//		region.setStyle(style);
//		region.setPid(pid);
//		
//		return region;
//	}
//	
//	private void initBataBase(){
//		dbm = new DBManager(getActivity());
//	 	dbm.openDatabase();
//	 	db = dbm.getDatabase();
//	}
//	
//	private void closeBataBase(){
//		dbm.closeDatabase();
//		db.close();
//	}
	
}

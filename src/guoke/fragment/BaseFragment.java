package guoke.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import guoke.custom.listview.SingleLayoutListView;
import guoke.db.DBManager;
import guoke.model.MDate;
import guoke.model.MRegion;
import android.R.integer;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

public class BaseFragment extends Fragment{

	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

	protected SingleLayoutListView mListView;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public void onResume() {
		super.onResume();
		//applyScrollListener();
	}

	private void applyScrollListener() {
		mListView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
		outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
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
			progressDialog = new ProgressDialog(getActivity());
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
	 * 计算两个日期之间的天数，返回字符串
	 * @param beginDate
	 * @param finishDate
	 * @return
	 */
	public String getDuraionDays(MDate beginDate,MDate finishDate){
		
		
		return String.valueOf(getDuraionDaysInteger(beginDate,finishDate));
	}
	/**
	 * 计算两个日期之间的天数，返回字符串
	 * @param beginDate
	 * @param finishDate
	 * @return
	 */
	public Integer getDuraionDaysInteger(MDate beginDate,MDate finishDate){
		if(beginDate==null||finishDate==null){
			return -1;
		}
		//
		long beginday = beginDate.getTime()/86400000;
		//加上一天过去的毫秒数
		int h = new Date().getHours();
		long left = 86400000*h/24;
		long  finishday= (finishDate.getTime())/86400000;
		long durtion = finishday-beginday;
		//toast("tmp:"+beginday+",beginday:"+beginday);
		
		return (int) durtion;
	}
	
	/**
	 * 判断前后两个日期是否合法
	 * @return
	 */
	public Boolean valiatedDuration(MDate beginDate,MDate finishDate){
		Integer t = getDuraionDaysInteger(beginDate, finishDate);
		if(t>=1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Date 转为 X月X日
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getMonth_Day(MDate date){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(date.getMonth()+1);
		stringBuilder.append("月");
		stringBuilder.append(date.getDate());
		String md = stringBuilder.toString();
		return md;
	}	
	
	/**
	 * 判断取车，还车的日期是否合法,合法就更新用车时间
	 * @return
	 */
//	Boolean valiatedDuration(MDate getCar_Date,MDate returnCar_Date){
//		if(getCar_Date!=null&&returnCar_Date!=null){
//			if(getCar_Date.before(returnCar_Date)){
//				//一天有86400000毫秒
//				long rday = returnCar_Date.getTime();
//				long gday = getCar_Date.getTime();
//				//toast("rd="+rday+",gd="+gday);
//				//偏差值在80000000毫秒左右
//				long durtion = (rday-gday+80000000)/86400000;
//				usecar_datsTextView.setText(String.valueOf(durtion));
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
//	}
	/** 
	 * toast something
	 * @param toastString
	 */
	public void toast(String toastString){
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
	}

	private DBManager dbm;
	private SQLiteDatabase db;
	
	/**
	 * 根据选择的县区，得到所在城市
	 * @param mr
	 * @return
	 */
	public MRegion getCityRegion(MRegion r){
		if(r.getStyle()==2){
			return r;
		}
		else if(r.getStyle()==3){
			Integer pid = r.getPid();
			MRegion region = null;
			//get the instance of db
			initBataBase();
		 	try {
		 		//qeury the city form the db
			 	String sql = "select * from Region where rid = "+String.valueOf(pid);
		        Cursor cursor = db.rawQuery(sql,null);  
		        cursor.moveToPrevious();
		        while (cursor.moveToNext()){ 
		        	region = getRegionObjectWithCursor(cursor);
			        cursor.moveToNext();
		        }
		        cursor.close();
			} catch (Exception e) {
				// TODO: handle exception
			}finally{			
				closeBataBase();
			}
			if(region == null){
				toast("选择城市或地区无效，恢复默认值");
				region = new MRegion();
				region.setRid(349);
				region.setRname("桂林市");
				region.setStyle(2);
				region.setPid(22);
				region.setSiteid(0);
			}
			return region;
		}
		toast("选择城市无效，恢复默认值");
		MRegion region = new MRegion();
		region.setRid(349);
		region.setRname("桂林市");
		region.setStyle(2);
		region.setPid(22);
		region.setSiteid(0);
		return region;
		
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
	
	private void initBataBase(){
		dbm = new DBManager(getActivity());
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	}
	
	private void closeBataBase(){
		dbm.closeDatabase();
		db.close();
	}
	
}

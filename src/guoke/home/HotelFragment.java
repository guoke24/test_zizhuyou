package guoke.home;

import java.util.Calendar;
import java.util.Date;

import guoke.activity.HotelDetailActivity;
import guoke.activity.HotelListActivity;
import guoke.adapter.HotelListAdapter;
import guoke.fragment.BaseFragment;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;

import com.example.demo_zizhuyou.AddressSelectActivity;
import com.example.demo_zizhuyou.CalendarActivity;
import com.example.demo_zizhuyou.R;

import android.app.Fragment;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class HotelFragment extends BaseFragment implements OnClickListener{

	private TextView checkIn_CityTextView;
	private TextView checkIn_DateTextView;
	private TextView checkOut_DateTextView;
	private Button commitTo_HotelListButton;
	private MRegion curRegion = null;
	private MDate checkIn_Date;
	private MDate checkOut_Date;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View hotelLayout = inflater.inflate(R.layout.homefragment_hotel_layout,
				container, false);
		inti(hotelLayout);
		setListener();
		defaultRegionAndDate();
		return hotelLayout;
	}

	private void inti(View v){
		checkIn_CityTextView = (TextView)v.findViewById(R.id.checkIn_city_textView);
		checkIn_DateTextView = (TextView) v.findViewById(R.id.checkIn_time_textView);
		checkOut_DateTextView = (TextView)v.findViewById(R.id.checkOut_time_textView);
		commitTo_HotelListButton = (Button)v.findViewById(R.id.commit_button_to_hotellist);
		
	}
	
	private void defaultRegionAndDate(){
		MRegion region = new MRegion();
		region.setRid(349);
		region.setRname("桂林市");
		region.setStyle(2);
		region.setPid(22);
		region.setSiteid(0);
		region.setCid(102);
		setCurRegion(region);
		
		MDate todayDate = new MDate(Calendar.getInstance().getTimeInMillis());
		MDate tomorrom = new MDate(todayDate.getTime()+86400000);
		setCheckIn_Date(todayDate);
		setCheckOut_Date(tomorrom);
		
	}
	
	
	
	private void setListener(){
		checkIn_CityTextView.setOnClickListener(this);
		checkIn_DateTextView.setOnClickListener(this);
		checkOut_DateTextView.setOnClickListener(this);
		commitTo_HotelListButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.checkIn_city_textView:
			city_click();
			break;
		case R.id.checkIn_time_textView:
			checkInTime_click();
			break;
		case R.id.checkOut_time_textView:
			checkOutTime_click();
			break;
		case R.id.commit_button_to_hotellist:
			commitToHotelListButton_click();
			break;
		default:
			break;
		}
	}
	
	private void city_click(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), AddressSelectActivity.class);
		getActivity().startActivityForResult(intent, InfoCom.requestCheckInCity);
	}
	
	private void checkInTime_click(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), CalendarActivity.class);
		getActivity().startActivityForResult(intent, InfoCom.requestCheckInTime);
	}
	
	private void checkOutTime_click(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), CalendarActivity.class);
		getActivity().startActivityForResult(intent, InfoCom.requestCheckOutTime);		
	}

	private void commitToHotelListButton_click(){
		if(checkIn_Date!=null&&checkOut_Date!=null){
			Intent intent = new Intent();
			intent.putExtra(InfoCom.Extra_RegionSelect, curRegion);
			intent.putExtra(InfoCom.Extra_CheckInDate, checkIn_Date);
			intent.putExtra(InfoCom.Extra_CheckOutDate, checkOut_Date);
			intent.setClass(getActivity(), HotelListActivity.class);
			getActivity().startActivity(intent);
		}else{
			toast("还有日期没选择");
		}
			
	}
	
	
	
	public void setCurRegion(MRegion region){
		curRegion = region;
		//toast(curRegion.getRname());
		setCheckIn_CityTextView(region.getRname());
	}


	public void setCheckIn_Date(MDate checkInDate) {
		if(checkInDate==null){
			toast("入住日期未选择成功");
			return ;
		}
		this.checkIn_Date = checkInDate;
		//toast(getMonth_Day(checkInDate));
		if(isvaliatedDuration()){
			setCheckIn_DateTextView(getMonth_Day(checkInDate));
		}else{
			//toast("选择日期不合法，请重新选择");
			setCheckIn_DateTextView(getMonth_Day(checkInDate));
			toast("请重新选择离店日期");
			checkOut_Date = null;
			setCheckOut_DateTextView("");
		}
	}


	public void setCheckOut_Date(MDate checkOutDate) {
		if(checkOutDate==null){
			toast("离开日期未选择成功");
			return ;
		}
		
		this.checkOut_Date = checkOutDate;
		if(isvaliatedDuration()){
			setCheckOut_DateTextView(getMonth_Day(checkOutDate));		
		}else{
			toast("入住日期必须早于离店日期，请重新选择");
			checkOut_Date = null;
			setCheckOut_DateTextView("");
		}
	}

	/**
	 * 在HomeActivity中修改该fragment的控件值
	 * @param checkInCityTextView
	 */
	protected void setCheckIn_CityTextView(String checkInCity) {
		checkIn_CityTextView.setText(checkInCity);
	}

	protected void setCheckIn_DateTextView(String checkInTime) {
		this.checkIn_DateTextView.setText(checkInTime);
	}

	protected void setCheckOut_DateTextView(String checkOutTime) {
		this.checkOut_DateTextView.setText(checkOutTime);
	}

	/**
	 * 
	 * @return
	 */
	Boolean isvaliatedDuration(){
		if(checkIn_Date==null||checkOut_Date==null){
			return true;
		}
		return valiatedDuration(checkIn_Date, checkOut_Date);
	}
	


	@SuppressWarnings("deprecation")
	String getMonth_Day(Date date){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(date.getMonth()+1);
		stringBuilder.append("月");
		stringBuilder.append(date.getDate());
		String md = stringBuilder.toString();
		return md;
	}
	
}

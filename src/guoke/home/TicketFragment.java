package guoke.home;

import java.util.Calendar;

import guoke.activity.AttractionsListActivity;
import guoke.fragment.BaseFragment;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;

import com.example.demo_zizhuyou.AddressSelectActivity;
import com.example.demo_zizhuyou.CalendarActivity;
import com.example.demo_zizhuyou.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TicketFragment extends BaseFragment implements OnClickListener {

	private TextView destination;
	private TextView beginDate;
	private TextView finishDate;
	private EditText keyword;

	private Button dest_searchButton;
	private Button key_searchButton;
	
	private MDate bgDate;
	private MDate fnDate;
	private MRegion dsCityRegion;
	private String keyword_search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View newsLayout = inflater.inflate(R.layout.homefragment_ticket_layout,
				container, false);
		init(newsLayout);
		setListener();
		defaultRegionAndDate();
		
		return newsLayout;
	}

	void init(View v) {
		destination = (TextView) v
				.findViewById(R.id.ticketfragment_city_textView);
		beginDate = (TextView) v
				.findViewById(R.id.ticketfragment_begindate_textView);
		finishDate = (TextView) v
				.findViewById(R.id.ticketfragment_finishdate_textView);
		keyword = (EditText) v
				.findViewById(R.id.ticketfragment_keyword_textView);
		dest_searchButton = (Button) v
				.findViewById(R.id.ticketfragment_destsearch_button);
		key_searchButton = (Button) v
				.findViewById(R.id.ticketfragment_keysearch_button);
	}

	void setListener() {
		destination.setOnClickListener(this);
		beginDate.setOnClickListener(this);
		finishDate.setOnClickListener(this);
		dest_searchButton.setOnClickListener(this);
		key_searchButton.setOnClickListener(this);

	}

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
		setDsCityRegion(region);

		
		MDate todayDate = new MDate(Calendar.getInstance().getTimeInMillis());
		setBgDate(todayDate);
		MDate tomorrom = new MDate(todayDate.getTime()+86400000);
		setFnDate(tomorrom);
		
	}
	
	/**
	 * 点击了旅游目的地时候调用
	 */
	void destination_click(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), AddressSelectActivity.class);
		getActivity().startActivityForResult(intent, InfoCom.requestTravelCity);
	}
	/**
	 * 点击了【开始游玩时间】 调用
	 */
	void beginDate_click(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), CalendarActivity.class);
		getActivity().startActivityForResult(intent, InfoCom.requestBeginDate);
	}
	/**
	 * 点击了【结束游玩时间】调用
	 */
	void finishDate_click(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), CalendarActivity.class);
		getActivity().startActivityForResult(intent, InfoCom.requestFinishDate);
	}
	/**
	 * 
	 */
	void dest_searchButton_click(){
		if(beginDate!=null&finishDate!=null){
			Intent intent = new Intent();
			intent.putExtra(InfoCom.Extra_PlayDate, bgDate);
			intent.putExtra(InfoCom.Extra_FinPlayDate, fnDate);
			intent.putExtra(InfoCom.Extra_PlayCitySelect, dsCityRegion);
			intent.setClass(getActivity(), AttractionsListActivity.class);
			startActivity(intent);
		}else{
			toast("还有日期未选择");
		}
		
	}
	/**
	 * 
	 */
	void key_searchButton_click(){
		if(!keyword.getText().toString().equals("")){//不为空的时候才搜索
			
		}else{
			toast("关键字不能为空");
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ticketfragment_destsearch_button:
			dest_searchButton_click();
			break;
		case R.id.ticketfragment_keysearch_button:
			key_searchButton_click();
			break;
		case R.id.ticketfragment_begindate_textView:
			beginDate_click();
			break;
		case R.id.ticketfragment_finishdate_textView:
			finishDate_click();
			break;
		case R.id.ticketfragment_city_textView:
			destination_click();
			break;
		default:
			break;
		}
	}

	


	public void setBgDate(MDate bgDate) {
		this.bgDate = bgDate;
		if(isvaliatedDuration(bgDate, fnDate)){
			beginDate.setText(getMonth_Day(bgDate));
		}else{
			toast("请重新选择结束日期");			
			//getCar_Date = null;
			beginDate.setText(getMonth_Day(bgDate));
			
			fnDate = null;
			finishDate.setText("   ");
		}
	}



	public void setFnDate(MDate fnDate) {
		this.fnDate = fnDate;
		if(isvaliatedDuration(bgDate, fnDate)){
			finishDate.setText(getMonth_Day(fnDate));
		}else{
			toast("[开始日期]必须早于[结束日期]");			
			fnDate = null;
			finishDate.setText("   ");
		}
	}



	public void setDsCityRegion(MRegion dsCityRegion) {
		
		this.dsCityRegion = getCityRegion(dsCityRegion);
		destination.setText(this.dsCityRegion.getRname());
	}

	/**
	 * 判断开始，结束的日期是否合法
	 * @return
	 */
	public Boolean isvaliatedDuration(MDate getCar_Date,MDate returnCar_Date){
		if(getCar_Date==null||returnCar_Date==null){
			return true;
		}
		return valiatedDuration(getCar_Date, returnCar_Date);
	}
	
	
}

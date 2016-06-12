package guoke.activity;

import guoke.bean.HotelBean;
import guoke.bean.RoomBean;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MyUser;
import guoke.model.RoomOrder;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RoomOrderDetailActivity extends BaseActivity {

	//
	//private HotelItem curHotel;
	private HotelBean curHotel;
	private RoomBean curRoom;
	private MDate checkIn_Date;
	private MDate checkOut_Date;
	
	//room info
	private TextView rnameTextView;
	private TextView hnameTextView;
	private TextView rbedTextView;
	private TextView breakfastTextView;
//	private TextView rwifiTextView;
//	private TextView rbroadbandTextView;
//	private TextView rpeopleTextView;
//	private TextView rwashroomTextView;
	private TextView rsizeTextView;
	//user info
	private TextView userEditText;
	private TextView phonEditText;
	private EditText arriveEditText;
	//region and date 
	private TextView regionTextView;
	private TextView checkintimeTextView;
	private TextView checkouttimeTextView;
	//price and commit
	private TextView priceTextView;
	private Button commit_room_orderButton;
	//
	private TextView priceTextView2;
	//
	
	RoomOrder cuRoomOrder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_room_detail);
		
		getsourIntent();
		init();
		
	}

	void getsourIntent(){
		cuRoomOrder = (RoomOrder) getIntent().getSerializableExtra(InfoCom.Extra_RoomOrderSelect);
	}
	
	void init(){
		//房间名
		rnameTextView = (TextView) findViewById(R.id.room_reserve_name);
		rnameTextView.setText(cuRoomOrder.getRoom_name());
		//酒店名
		hnameTextView = (TextView )findViewById(R.id.room_reserve_hotelname);
		hnameTextView.setText(cuRoomOrder.getHotel_name());
		//房间租金
		priceTextView2 = (TextView) findViewById(R.id.room_reserve_dayprice);
		priceTextView2.setText("￥"+cuRoomOrder.getPrice()+"/天");
		//早餐
		breakfastTextView = (TextView) findViewById(R.id.room_reserve_breakfast);
		breakfastTextView.setText("早餐:"+cuRoomOrder.getBreakfast());
		//床
		rbedTextView = (TextView) findViewById(R.id.room_reserve_bed);
		rbedTextView.setText("床型:"+cuRoomOrder.getBed());
//		//wifi
//		rwifiTextView = (TextView) findViewById(R.id.room_reserve_wifi);
//		rwifiTextView.setText(curRoom.getWifi());
//		//宽带
//		rbroadbandTextView = (TextView) findViewById(R.id.room_reserve_broadband);
//		rbroadbandTextView.setText(curRoom.getName());
//		//可住人数
//		rpeopleTextView = (TextView) findViewById(R.id.room_reserve_people_number);
//		rpeopleTextView.setText(curRoom.getPeople_number());
//		//洗浴情况
//		rwashroomTextView = (TextView) findViewById(R.id.room_reserve_wash_room);
//		rwashroomTextView.setText("独立卫浴");
		//房间大小
		rsizeTextView = (TextView) findViewById(R.id.room_reserve_area);
		rsizeTextView.setText("房间大小:"+cuRoomOrder.getArea().replace("平方米", "")+"㎡");
		//用户信息
		userEditText = (TextView) findViewById(R.id.room_reserve_user_name_input);
		userEditText.setText(cuRoomOrder.getNameCheckIn());
		
		phonEditText = (TextView) findViewById(R.id.room_reserve_user_phone_input);
		phonEditText.setText(cuRoomOrder.getPhoneCheckIn());
		
//		MyUser myUser = BmobUser.getCurrentUser(this,MyUser.class);
//		if(myUser!=null) { phonEditText.setText(myUser.getMobilePhoneNumber()); }
		//arriveEditText = (EditText) findViewById(R.id.room_reserve_arrive_time_select);
		//所在位置
		regionTextView = (TextView) findViewById(R.id.room_reserve_checkIn_region_textView);
		regionTextView.setText(cuRoomOrder.getLocation());
		
		checkintimeTextView = (TextView) findViewById(R.id.room_reserve_checkIn_time_textView);
		checkintimeTextView.setText(getYMD(cuRoomOrder.getDateCheckIn()));
		
		checkouttimeTextView = (TextView) findViewById(R.id.room_reserve_checkOut_time_textView);
		checkouttimeTextView.setText(getYMD(cuRoomOrder.getDateCheckOut()));
		//总价
		priceTextView=(TextView) findViewById(R.id.room_reserve_price);
		priceTextView.setText("总价：￥"+cuRoomOrder.getFee());
		
	}
	
	/**
	 * 获得年月日
	 * @param bmd
	 * @return
	 */
	private String getYMD(BmobDate bmd){
		String tmp = bmd.getDate();
		String ymd[] = tmp.split(" ");
		String y_m_d[] = ymd[0].split("-");
		
		return y_m_d[1]+"-"+y_m_d[2];
	}
	
}

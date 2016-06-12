package guoke.activity;

import guoke.bean.HotelBean;
import guoke.bean.RoomBean;
import guoke.home.HomeActivity;
import guoke.model.HotelItem;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MyUser;
import guoke.model.RoomItem;
import guoke.model.RoomOrder;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HotelRoomReserveActivity extends BaseActivity implements OnClickListener{
	
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
	private EditText userEditText;
	private EditText phonEditText;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		setContentView(R.layout.activity_hotelroom_reserve);

		getValueFromIntent();
		initView();
	
	}
	
	/**
	 * 接收传过来的酒店，房间，入住和离开信息。
	 */
	void getValueFromIntent(){
		Intent intent = getIntent();
		curHotel = (HotelBean) intent.getSerializableExtra(InfoCom.Extra_HotelSelect);
		curRoom = (RoomBean) intent.getSerializableExtra(InfoCom.Extra_RoomSelect);
		checkIn_Date = (MDate) intent.getSerializableExtra(InfoCom.Extra_CheckInDate);
		checkOut_Date = (MDate) intent.getSerializableExtra(InfoCom.Extra_CheckOutDate);
	}
	
	/**
	 * 初始化了大量的view
	 */
	void initView(){
		//房间名
		rnameTextView = (TextView) findViewById(R.id.room_reserve_name);
		rnameTextView.setText(curRoom.getRoomName());
		//酒店名
		hnameTextView = (TextView )findViewById(R.id.room_reserve_hotelname);
		hnameTextView.setText(curHotel.getName());
		//房间租金
		priceTextView2 = (TextView) findViewById(R.id.room_reserve_dayprice);
		priceTextView2.setText("￥"+curRoom.getPrice()+"/天");
		//早餐
		breakfastTextView = (TextView) findViewById(R.id.room_reserve_breakfast);
		breakfastTextView.setText("早餐:"+curRoom.getBreakfast());
		//床
		rbedTextView = (TextView) findViewById(R.id.room_reserve_bed);
		rbedTextView.setText("床型:"+curRoom.getBed());
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
		rsizeTextView.setText("房间大小:"+curRoom.getArea().replace("平方米", "")+"㎡");
		//用户信息
		userEditText = (EditText) findViewById(R.id.room_reserve_user_name_input);
		
		
		phonEditText = (EditText) findViewById(R.id.room_reserve_user_phone_input);
		MyUser myUser = BmobUser.getCurrentUser(this,MyUser.class);
		//if(myUser!=null) { phonEditText.setText(myUser.getMobilePhoneNumber()); }
		//arriveEditText = (EditText) findViewById(R.id.room_reserve_arrive_time_select);
		//所在位置
		regionTextView = (TextView) findViewById(R.id.room_reserve_checkIn_region_textView);
		regionTextView.setText(curHotel.getAddress());
		
		checkintimeTextView = (TextView) findViewById(R.id.room_reserve_checkIn_time_textView);
		checkintimeTextView.setText(getMonth_Day(checkIn_Date));
		
		checkouttimeTextView = (TextView) findViewById(R.id.room_reserve_checkOut_time_textView);
		checkouttimeTextView.setText(getMonth_Day(checkOut_Date));
		//总价
		priceTextView=(TextView) findViewById(R.id.room_reserve_price);
		priceTextView.setText("总价：￥"+sumFeeString()+"("+getDuraionDays(checkIn_Date, checkOut_Date)+"天x"+curRoom.getPrice()+")");
		
		commit_room_orderButton = (Button)findViewById(R.id.room_reserve_commit);
		commit_room_orderButton.setOnClickListener(this);
	}
	
	/**
	 * 设置窗口大小
	 */
//	void setSize(){
//		WindowManager m = getWindowManager();    
//	    Display d = m.getDefaultDisplay();  //为获取屏幕宽、高    
//	    android.view.WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值    
//	    //p.height = (int) (d.getHeight() * 0.7);   //高度设置为屏幕的1.0   
//	    p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.8   
//	    p.alpha = 1.0f;      //设置本身透明度  
//	    p.dimAmount = 0.0f;      //设置黑暗度  
//	    getWindow().setAttributes(p); 
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.room_reserve_commit:
			commit_click();
			break;

		default:
			break;
		}

	}
	
	/**
	 * 点击了提交按钮的时候调用
	 */
	protected void commit_click(){
		
		MyUser myUser = BmobUser.getCurrentUser(this,MyUser.class);
		//先判断是否登陆
		if( myUser != null ){//如果登陆了，就获取用户，提交订单
			
			if(isInputValidate()){//输入合法，不为空
				showProgressDialog();	
				RoomOrder rd = new RoomOrder();
				rd.setUser_id(myUser.getObjectId());//关联用户的id
//				rd.setUsername(userEditText.getText().toString());
//				rd.setUserphone(phonEditText.getText().toString());
				rd.setLocation(curHotel.getAddress());
				rd.setHotel_id(curHotel.getId());//预订的房间的id
				rd.setHotel_name(curHotel.getName());//酒店名
				//rd.setDays(days);
				//记录房间的完整信息
				rd.setRoom_name(curRoom.getRoomName());//房间名
				rd.setArea(curRoom.getArea());
				rd.setBed(curRoom.getBed());
				rd.setBreakfast(curRoom.getBreakfast());
				rd.setPrice(curRoom.getPrice());
				
				
				rd.setStatu(0);//一开始被创建的订单都是进行中的状态
				
				rd.setDateCheckIn(new BmobDate(checkIn_Date));//入住日期
				rd.setDateCheckOut(new BmobDate(checkOut_Date));//离开日期
				
				rd.setFee(Double.valueOf(curRoom.getPrice()));//总费用
				
				rd.setNameCheckIn(userEditText.getText().toString());//入住人姓名
				rd.setPhoneCheckIn(phonEditText.getText().toString());//入住人联系信息
				
				rd.save(this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						//toast("提交成功");
						closeProgressDialog();
						onSuccess_reserve();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						closeProgressDialog();
						toast("提交失败");
						
					}
				});
			}else{
				closeProgressDialog();
			}
			
		}else{
			closeProgressDialog();
			toast("未登录,请先登录！");
		}

	}
	
	/**
	 * 预订成功的时候调用
	 */
	protected void onSuccess_reserve() {
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle("");
		dialog.setMessage("预订成功");
		dialog.setNegativeButton("返回主界面", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setClass(HotelRoomReserveActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
//		dialog.setPositiveButton("停留在订单页", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		});
		
		dialog.show();
	}
	
	/**
	 * 判断入住人和手机是否为空
	 * @return
	 */
	private Boolean isInputValidate(){
		//如果两个输入框都有值，就返回真 
		if(!userEditText.getText().toString().equals("")&&
		   !phonEditText.getText().toString().equals("")){
			if(phonEditText.getText().toString().length()==11){
				return true;
			}else{
				toast("手机号必须为11位！");
				return false;
			}
			
		}
		else{
			
			toast("入住人和手机都要填写！");
			return false;
		}
	}
	
	private String sumFeeString(){
		int day = getDuraionDaysInteger(checkIn_Date, checkOut_Date);
		
		int fee = (int) (day*Integer.valueOf(curRoom.getPrice()));
		
		return String.valueOf(fee);
	}
	
}

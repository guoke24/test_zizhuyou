package com.example.demo_zizhuyou;

import guoke.activity.AttracTicketReserveActivity;
import guoke.activity.CarListActivity;
import guoke.activity.CarOrderDetailActivity;
import guoke.activity.CarReserveActivity;
import guoke.activity.CateReserveActivity;
import guoke.activity.HotelDetailActivity;
import guoke.activity.HotelRoomReserveActivity;
import guoke.custom.timebutton.TimeButton;
import guoke.home.HomeActivity;
import guoke.model.HotelItem;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MyUser;
import guoke.model.RoomItem;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;


import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FastLoginActivity extends BaseActivity implements OnClickListener {

	private EditText mobilePhoneEditText;
	private EditText validateCodeEditText;
	private TimeButton getCodeButton;
	private Button commitButton;
	private Button pawLoginButton;
	//该数据只是一个中转，
	//从HotelDetailActivity来，
	//转给RoomReserveActivity。
//	private HotelItem curHotel;
//	private RoomItem curRoom;
//	private MDate checkIn_Date;
//	private MDate checkOut_Date;
	//
	Intent source_intent;
	private int fromwhere = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fast_login2);
		
		init();
		setListener();
		checkWhereCome();
	}

	void checkWhereCome(){
	
		source_intent = getIntent();
		fromwhere = source_intent.getIntExtra(InfoCom.fromWhere, 0);
		if(source_intent == null){
			source_intent = new Intent();
			source_intent.putExtra(InfoCom.fromWhere, 0);
		}		
		//toast("source_intent 为null吗"+(source_intent == null));

		
//		if(source_intent != null){
//			toast("未登录，请先登录！");
//			curHotel = (HotelItem) source_intent.getSerializableExtra(InfoCom.Extra_HotelSelect);
//			curRoom = (RoomItem) source_intent.getSerializableExtra(InfoCom.Extra_RoomSelect);
//			checkIn_Date = (MDate) source_intent.getSerializableExtra(InfoCom.Extra_CheckInDate);
//			checkOut_Date = (MDate) source_intent.getSerializableExtra(InfoCom.Extra_CheckOutDate);
//		
//		}
	}
	
	void init() {
		mobilePhoneEditText = (EditText) findViewById(R.id.fastlogin_account);
		validateCodeEditText = (EditText) findViewById(R.id.fastlogin_validateCode);
		getCodeButton = (TimeButton) findViewById(R.id.fastlogin_code_button);
		commitButton = (Button) findViewById(R.id.fastlogin_login);
		pawLoginButton = (Button) findViewById(R.id.fastlogin_paw_login);
	}

	void setListener() {
		// reget the code after 15s
		getCodeButton.setTextAfter("秒后重新获取").setTextBefore("点击获取验证码")
				.setLenght(15 * 1000);

		getCodeButton.setOnClickListener(this);
		getCodeButton.setClickable(false);
		getCodeButton.setAlpha((float)0.3);
		pawLoginButton.setOnClickListener(this);
		/*
		 * 只有等于11位才释放 【发送验证码按钮】的点击权
		 */
		mobilePhoneEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s.length()==11){
						//toast("11位了");
						getCodeButton.setClickable(true);
						getCodeButton.setAlpha((float)1);
					}else{
						getCodeButton.setClickable(true);
						getCodeButton.setAlpha((float)0.3);
					}
			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		commitButton.setOnClickListener(this);
	}

	/**
	 * 点击发送验证码，
	 */
	void getCodeButton_click() {
		
		if(mobilePhoneEditText.getText().toString().length()==11){
			//toast(mobilePhoneEditText.getText().toString());
			BmobSMS.requestSMSCode(this, mobilePhoneEditText.getText().toString(),
					"cool自助游", new RequestSMSCodeListener() {

						@Override
						public void done(Integer smsId, BmobException ex) {
							// TODO Auto-generated method stub
							if (ex == null) {// 验证码发送成功
								Log.i("smile", "短信id：" + smsId);// 用于查询本次短信发送详情
								toast("发送成功");
							}
						}
					});
		}
		else{
			toast("手机号必须为11位");
		}
		
	}

	/**
	 * 点击判断有效应，并登陆。
	 */
	void login_click() {
		//new LogInListener<MyUser>().
		// 如果手机号或验证码为空，点击无效
		if (validateCodeEditText.getText().toString().equals("")
				|| mobilePhoneEditText.getText().toString().equals("")) {
			toast("手机号或验证码不能为空！");
		} else {
			BmobUser.signOrLoginByMobilePhone(this, 
					mobilePhoneEditText.getText().toString(),
					validateCodeEditText.getText().toString(),
					new LogInListener<MyUser>() {

						@Override
						public void done(MyUser user, BmobException arg1) {
							if (user != null) {
								//toast("登陆成功！"+user.getObjectId()+","+user.getUsername()+","+user.getMobilePhoneNumber());
								toast("登录成功！");
								//onLoginSuccess(user);
								success_jump();
							}else{
								toast("登录失败，请检查验证码是否正确！");
							}
						}
						
						
					});
		}
	}

//	private void onLoginSuccess(MyUser user){
//		setLoginStatu(true);
//		setUserInPreference(user);
//	}
	
	/**
	 * 跳回到密码登录界面
	 */
	private void jumpTopawLogin(){

		source_intent.setClass(this, LoginActivity.class);
		finish();
		startActivity(source_intent);
		
		
	}
	
	/**
	 * 成功后的跳转
	 */
	private void success_jump(){
		if(fromwhere==InfoCom.fromHotelDetail){
			source_intent.setClass(this, HotelRoomReserveActivity.class);
			finish();
			startActivity(source_intent);
		}else if(fromwhere == InfoCom.fromMyselfFragment){
			//如果从myself来，把自己关了就回去了
			finish();
		}else if(fromwhere == InfoCom.fromCarList){
			source_intent.setClass(this, CarListActivity.class);
			finish();
			startActivity(source_intent);
		}else if(fromwhere == InfoCom.fromTicketList){
			source_intent.setClass(this, AttracTicketReserveActivity.class);
			finish();
			startActivity(source_intent);
		}else if(fromwhere == InfoCom.fromCateList){
			source_intent.setClass(this, CateReserveActivity.class);
			finish();
			startActivity(source_intent);
		}else{
			//如果都没有，那就常规的跳转
			source_intent.setClass(this, HomeActivity.class);
			finish();
			startActivity(source_intent);
		}
		
	}
	
//	private void checkIsLogin(){
//		if(getLoginStatu()==true){
//			MyUser myUser = getUserFromPreferences(); 
//			toast("用户"+myUser.getUsername()+",欢迎肥来");
//			jump();
//		}
//	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fastlogin_code_button:
			getCodeButton_click();
			break;
		case R.id.fastlogin_login:
			login_click();
			break;
		case R.id.fastlogin_paw_login:
			jumpTopawLogin();
			break;
		default:
			break;
		}
	}

	/**
	 * 跳回LoginActivity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        jumpTopawLogin();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}

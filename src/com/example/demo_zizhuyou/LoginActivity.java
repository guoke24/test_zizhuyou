package com.example.demo_zizhuyou;

import guoke.activity.AttracTicketReserveActivity;
import guoke.activity.CarReserveActivity;
import guoke.activity.CateReserveActivity;
import guoke.activity.HotelRoomReserveActivity;
import guoke.home.HomeActivity;
import guoke.md5util.Mdfive;
import guoke.model.HotelItem;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MyUser;
import guoke.model.RoomItem;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import com.example.demo_zizhuyou.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener{

	private EditText phoneEditText;
	private EditText passwordEditText;
	private Button fastLoginButton;
	private Button loginButton;
	private CheckBox showPasswordCheckBox;
	private Button registerButton;
//	private Button fastLookButton;
	
//	private HotelItem curHotel;
//	private RoomItem curRoom;
//	private MDate checkIn_Date;
//	private MDate checkOut_Date;	
	
	Intent source_intent;//intent的可复用性，大大简化代码
	private int fromwhere = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login2);
		
		
		init();
		setListener();
		checkWhereCome();
		check_isLogin();
	}

	/**
	 * 记下从哪里启动的，用fromwhere变量记录
	 */
	void checkWhereCome(){
		source_intent =getIntent();
		fromwhere = source_intent.getIntExtra(InfoCom.fromWhere, 0);
		if(source_intent == null){
			source_intent = new Intent();
			source_intent.putExtra(InfoCom.fromWhere, 0);
		}
		//toast("source_intent 为null吗"+(source_intent == null));
		
//		if(fromwhere == InfoCom.fromHotelDetail){
//			//如果从酒店来，就要接数据
//			source_intent = getIntent();
//			curHotel = (HotelItem) source_intent.getSerializableExtra(InfoCom.Extra_HotelSelect);
//			curRoom = (RoomItem) source_intent.getSerializableExtra(InfoCom.Extra_RoomSelect);
//			checkIn_Date = (MDate) source_intent.getSerializableExtra(InfoCom.Extra_CheckInDate);
//			checkOut_Date = (MDate) source_intent.getSerializableExtra(InfoCom.Extra_CheckOutDate);
//		}else {
//			//如果常规或myself，就没数据
//		}
	}
	
	/**
	 * 如果刚启动的时候已经登录，常规跳转
	 */
	void check_isLogin(){
		MyUser userInfo = BmobUser.getCurrentUser(this,MyUser.class);
		if(userInfo != null){
			Toast.makeText(this, "用户"+userInfo.getUsername()+",欢迎回来！", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(this, HomeActivity.class);
			startActivity(intent);
			finish();//成功登陆就销毁自己
		}
	}
	
	private void init(){
		phoneEditText=(EditText)findViewById(R.id.account);
		passwordEditText=(EditText)findViewById(R.id.password);
		loginButton=(Button)findViewById(R.id.login);
		registerButton = (Button) findViewById(R.id.login_register);
		//showPasswordCheckBox=(CheckBox)findViewById(R.id.show_password);
		fastLoginButton = (Button)findViewById(R.id.login_forget_paw);
		//fastLookButton = (Button) findViewById(R.id.login_fastlook);
		if(fromwhere != 0 ){
			//如果不是一开始就跳转来的，不显示
			//fastLookButton.setVisibility(View.GONE);
		}
	}
	
	private void setListener(){
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		//showPasswordCheckBox.setOnClickListener(this);
		fastLoginButton.setOnClickListener(this);
		//fastLookButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			login_click();
			//临时的功能
//			Intent intent = new Intent();
//			intent.setClass(this, HomeActivity.class);
//			startActivity(intent);
//			String account = accountEditText.getText().toString();
//			String password = passwordEditText.getText().toString();
//			sendHttpRequest_PostMethod(postUrl, account, password);
			break;
		case R.id.login_register:
			//跳转到【注册界面】
			Intent intent = new Intent();
			intent.setClass(this, RegisterActivity.class);
			finish();
			startActivity(intent);
			//
			break;
//		case R.id.show_password:
//			if(showPasswordCheckBox.isChecked()){
//				//显示密码
//				passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//			}else{
//				//隐藏密码
//				passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
//			}
//			break;
		case R.id.login_forget_paw:
			forgetPaw_click();
			break;
//		case R.id.login_fastlook://随便狂狂,直接跳转
//			Intent look_intent = new Intent();
//			look_intent.setClass(this, HomeActivity.class);
//			finish();
//			startActivity(look_intent);
//			
//			break;
		}
		
	}

	/**
	 * 忘记密码，就快速登录
	 */
	private void forgetPaw_click(){
		//跳转到  【快速登陆】
//		if(fromwhere == InfoCom.fromHotelDetail){
//			//如果是从酒店详情页跳过来的，还要把酒店传过来的数据传过去
//			//改一下intent的目的就传过去，看看时候可行
//			source_intent.setClass(this, FastLoginActivity.class);
//			startActivity(source_intent);
//		}else if(fromwhere == 0){
//			//如果是启动的时候来到这的，就用跳转的方式
//			//Intent fastlogin_intent = new Intent();
//			source_intent.setClass(this, FastLoginActivity.class);
//			startActivity(source_intent);
//		}else if(fromwhere == InfoCom.fromMyselfFragment){
//			
//			source_intent.setClass(this, FastLoginActivity.class);
//		}
		toast("去快速登录");
		/**
		 * 无论是从哪里来的，source_intentd都会记录有，所以直接跳转就ok
		 */
		source_intent.setClass(this, FastLoginActivity.class);
		finish();
		startActivity(source_intent);
		
	}
	
	/**
	 * 到后端查询，耗时操作！
	 */
	private void login_click(){
		String mobilePhone = phoneEditText.getText().toString();
		String paw = passwordEditText.getText().toString();
		String md5paw = new Mdfive().passto(paw);
		BmobUser.loginByAccount(this, mobilePhone, md5paw, new LogInListener<MyUser>() {
			@Override
			public void done(MyUser arg0, BmobException arg1) {
				// TODO Auto-generated method stub
				if (arg0 != null) {
					isSuccess(true);
				}
				else{
					isSuccess(false);
				}
			}
		});
		
	}
	
//	private String postUrl="http://10.0.2.2:8080/Http_Server/LoginAction2";	
//	private String postUrlformoblie="http://169.254.5.157:8080/Http_Server/LoginAction2";	
	
	/**
	 * 登录成功，从哪里来的，就跳回哪里去！！！
	 * @param success
	 */
	private void isSuccess(Boolean success){
		if(success){
			Toast.makeText(this, "登陆成功，欢迎！", Toast.LENGTH_SHORT).show();
//			Intent intent = new Intent();
//			intent.putExtra(InfoCom.Extra_HotelSelect, curHotel);
//			intent.putExtra(InfoCom.Extra_CheckInDate, checkIn_Date);
//			intent.putExtra(InfoCom.Extra_CheckOutDate, checkOut_Date);
//			intent.putExtra(InfoCom.Extra_RoomSelect, curRoom);
			
			if(fromwhere == InfoCom.fromHotelDetail){//如果是从预订页面过来
				source_intent.setClass(this, HotelRoomReserveActivity.class);
				finish();//成功登陆就销毁自己
				startActivity(source_intent);
			}else if(fromwhere == InfoCom.fromMyselfFragment){
				//从我的设置来的时候，关了自己就好
				finish();//成功登陆就销毁自己
			}else if(fromwhere == 0){
				//常规的时候，
				source_intent.setClass(this, HomeActivity.class);
				finish();//成功登陆就销毁自己
				startActivity(source_intent);
			}else if(fromwhere == InfoCom.fromCarList){
				source_intent.setClass(this, CarReserveActivity.class);
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
			}
			
			
		}else{
			Toast.makeText(this, "登陆失败，用户名或密码错误！", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * 用post方法提交账号和密�?
	 * @param url
	 * @param account
	 * @param password
	 */
//	private void sendHttpRequest_PostMethod(final String url,final String account,final String password){
//			
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try{
//					HttpClient httpClient=new DefaultHttpClient();
//					//HttpGet httpGet=new HttpGet(url);
//					HttpPost httpPost = new HttpPost(url);
//					//set the post data
//					List<NameValuePair> params = new ArrayList<NameValuePair>();
//					params.add(new BasicNameValuePair("account",account));
//					params.add(new BasicNameValuePair("password",password));
//					UrlEncodedFormEntity uefentity = new UrlEncodedFormEntity(params,"UTF-8");
//					httpPost.setEntity(uefentity);
//					//execute the commit
//					HttpResponse httpResponse = httpClient.execute(httpPost);
//					
//					//
//					if(httpResponse.getStatusLine().getStatusCode()==200){
//						String response="";		
//						HttpEntity entity = httpResponse.getEntity();
//						response = EntityUtils.toString(entity, "utf-8");
//						
//						Message msg = new Message();
//						msg.what=MSG_LOGINING;
//						//
//						msg.obj=response.toString();
//						handler.sendMessage(msg);
//					}
//										
//				}catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//				
//			}
//		}).start();
//		//return validatedResponse;
//	}
}

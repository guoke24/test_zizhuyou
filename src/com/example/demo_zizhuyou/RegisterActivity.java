package com.example.demo_zizhuyou;

import guoke.custom.timebutton.TimeButton;
import guoke.md5util.Mdfive;
import guoke.model.MyUser;


import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

import android.app.Activity;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;


public class RegisterActivity extends BaseActivity implements android.view.View.OnClickListener{

//	private EditText accountEditText;
//	private EditText emailEditText;
	private EditText phoneEditText;
//	private EditText identifyCodeEditText;
	private EditText vailatecodeEditText;
	private EditText passwordEditText;
	private EditText passwordAgainEditText;
	private Button commitButton;
	private TimeButton sendcodebButton;
	
	private Boolean isPhoneValidate = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register2);
		init();
		setlistener();
	}

	void init(){
//		accountEditText = (EditText) findViewById(R.id.register_account);
//		emailEditText = (EditText) findViewById(R.id.register_email);
//		identifyCodeEditText = (EditText) findViewById(R.id.register_identify_code);
		phoneEditText = (EditText) findViewById(R.id.register_phone);
		vailatecodeEditText = (EditText) findViewById(R.id.register_vailate_code);
		passwordEditText = (EditText) findViewById(R.id.register_password);
		passwordAgainEditText = (EditText) findViewById(R.id.register_password_again);
		
		commitButton = (Button) findViewById(R.id.register_commit);
		sendcodebButton = (TimeButton) findViewById(R.id.regiser_send_vailate_code);
		sendcodebButton.setTextAfter("秒后重新获取").setTextBefore("点击获取验证码")
		.setLenght(15 * 1000);
	}

	void setlistener(){
		commitButton.setOnClickListener(this);
		commitButton.setClickable(false);
		commitButton.setAlpha((float) 0.3);
		sendcodebButton.setOnClickListener(this);
		sendcodebButton.setClickable(false);
		sendcodebButton.setAlpha((float)0.3);
		//手机号监控验证码按钮
		phoneEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()==11){
					//toast("11位了");
					sendcodebButton.setClickable(true);
					sendcodebButton.setAlpha((float)1);
				}else{
					sendcodebButton.setClickable(false);
					sendcodebButton.setAlpha((float)0.3);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		//密码监控手机号
		passwordEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(!isVailatePhone()){
					toast("手机号必须为11位");
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		//再次密码监控全部
		passwordAgainEditText.addTextChangedListener(new TextWatcher(){

			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			public void afterTextChanged(Editable s) {
				
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(passwordEditText.getText().toString().length()<6){
					toast("密码不能少于6位！");
				}
				
				if(passwordAgainEditText.getText().toString().length()>=6){
					if(passwordEditText.getText().toString().equals(
							   passwordAgainEditText.getText().toString())){
						toast("两次密码一致！");
					}else{
						toast("密码不一致哟！");
					}
				}
				checkCanCommit();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_commit:
			commitButton_click();
			break;
		case R.id.regiser_send_vailate_code:
			BmobSMS.requestSMSCode(this, phoneEditText.getText().toString(),
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
			break;
		
		default:
			break;
		}		
	}
	
	void commitButton_click(){
		//先不验证短信了
		MyUser myUser = new MyUser();
		
		myUser.setMobilePhoneNumber(phoneEditText.getText().toString());
		myUser.setMobilePhoneNumberVerified(true);
		myUser.setUsername(phoneEditText.getText().toString());
		String pwd = new Mdfive().passto(passwordAgainEditText.getText().toString()); 
		myUser.setPassword(pwd);
		
		myUser.signUp(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("注册成功！");
				jump();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				toast("注册失败,"+arg1);
			}
		});
		
	}

	/**
	 * 不用跳转过去，结束自己就可以回去了
	 */
	void jump(){
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
	
	/**
	 * 判断是否可以提交
	 * 手机验证码不为空，
	 * 两次密码相同，
	 * 密码长度超过六位
	 * 手机号必须为11位
	 */
	Boolean checkCanCommit(){
		
		
		if(passwordEditText.getText().toString().equals(
				   passwordAgainEditText.getText().toString())&&
		   passwordEditText.getText().toString().length()>=6&&
		   passwordAgainEditText.getText().toString().length()>=6&&
		   phoneEditText.getText().toString().length()==11){
			
			commitButton.setClickable(true);
			commitButton.setAlpha((float) 1);
			
			return true;
		}else{
			commitButton.setClickable(false);
			commitButton.setAlpha((float) 0.3);
			return false;
		}
		
	}
	
	/**下面函数的主要是判断包含非法字符，目前未实现**/
	
//	Boolean isValiateAccount(){
//		if(accountEditText.getText().toString().equals("")){
//			return false;
//		}
//		
//		return true;
//	}

//	Boolean isValiateIdentifyCode(){
//		if(identifyCodeEditText.getText().toString().equals("")){
//			return false;
//		}
//		
//		return true;
//	}
	
	Boolean isVailatePassword(){
		if(passwordEditText.getText().toString().equals("")){
			return false;
		}
		
		return true;
	}
	
//	Boolean isVailateEmail(){
//		if(emailEditText.getText().toString().equals("")){
//			return false;
//		}
//		
//		return true;
//	}
	
	Boolean isVailatePhone(){
		if(phoneEditText.getText().toString().length()==11){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 跳回LoginActivity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        jump();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
}

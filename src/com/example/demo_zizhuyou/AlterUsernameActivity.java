package com.example.demo_zizhuyou;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import guoke.custom.timebutton.TimeButton;
import guoke.md5util.Mdfive;
import guoke.model.MyUser;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AlterUsernameActivity extends BaseActivity implements OnClickListener{

	private TimeButton codeButton;
	private EditText newpawinputText;
	private EditText validaCodeinputText;
	private Button commitButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alter_username);
		init();
		setListener();
	}

	void init(){

		commitButton = (Button) findViewById(R.id.fastlogin_login);
		newpawinputText =(EditText) findViewById(R.id.fastlogin_account);
	}
	
	void setListener(){
		
		commitButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fastlogin_login:
			commit_click();
			break;
		default:
			break;
		}
		
	}
	

	
	void commit_click(){
		validatecode();
	}
	/**
	 * 通过验证，保存数据
	 */
	void validatecode(){
		MyUser myUser = BmobUser.getCurrentUser(AlterUsernameActivity.this, MyUser.class);
		if(myUser != null){
			
				String userString = newpawinputText.getText().toString();
				myUser.setValue("username", userString);
				myUser.update(this,myUser.getObjectId(), new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						toast("提交成功");
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						toast("提交失败,"+arg1);
					}
				});
			
		}else{
			toast("未登录，请先登录");
		}
	}
	
	
}

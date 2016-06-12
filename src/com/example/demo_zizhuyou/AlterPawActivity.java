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

public class AlterPawActivity extends BaseActivity implements OnClickListener{

	private TimeButton codeButton;
	private EditText newpawinputText;
	private EditText validaCodeinputText;
	private Button commitButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alter_paw);
		init();
		setListener();
	}

	void init(){
		codeButton = (TimeButton) findViewById(R.id.fastlogin_code_button);
		codeButton.setTextAfter("秒后重新获取").setTextBefore("点击获取验证码")
		.setLenght(15 * 1000);
		commitButton = (Button) findViewById(R.id.fastlogin_login);
		newpawinputText =(EditText) findViewById(R.id.fastlogin_account);
		validaCodeinputText = (EditText) findViewById(R.id.fastlogin_validateCode);
	}
	
	void setListener(){
		
		codeButton.setOnClickListener(this);
		commitButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fastlogin_code_button:
			code_click();
			break;
		case R.id.fastlogin_login:
			commit_click();
			break;
		default:
			break;
		}
		
	}
	
	void code_click(){
		BmobSMS.requestSMSCode(this, BmobUser.getCurrentUser(AlterPawActivity.this, MyUser.class).getMobilePhoneNumber(),
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
	
	void commit_click(){
		BmobSMS.verifySmsCode(this,BmobUser.getCurrentUser(AlterPawActivity.this, MyUser.class).getMobilePhoneNumber(), 
				validaCodeinputText.getText().toString(), new VerifySMSCodeListener() {

		    @Override
		    public void done(BmobException ex) {
		        // TODO Auto-generated method stub
		        if(ex==null){//短信验证码已验证成功
		            //Log.i("bmob", "验证通过");
		        	validatecode();
		        }else{
		           // Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
		        	toast("验证失败");
		        }
		    }
		});
	}
	/**
	 * 通过验证，保存数据
	 */
	void validatecode(){
		MyUser myUser = BmobUser.getCurrentUser(AlterPawActivity.this, MyUser.class);
		if(myUser != null){
			if(pawValidate()){
				String pawsString = newpawinputText.getText().toString();
				String md5paw = new Mdfive().passto(pawsString);
				myUser.setValue("password", md5paw);
				myUser.update(this, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						toast("提交成功");
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						toast("提交失败");
					}
				});
			}
		}else{
			toast("未登录，请先登录");
		}
	}
	
	Boolean pawValidate(){
		if(newpawinputText.getText().toString().length()>=6){
			return true;
		}
		toast("密码至少六位数");
		return false;
	}
	
}

package com.example.demo_zizhuyou;

import guoke.home.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.id;
import com.example.demo_zizhuyou.R.layout;
import com.example.demo_zizhuyou.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddrAndDateActivity extends Activity implements OnClickListener{

	private Button commitButton;
	private EditText dstInput;
	private EditText dateInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addranddate);
		//可以获得当前的日期时间
//		Date date=new Date();
//		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		String time=df.format(date);
//		System.out.println(time);
		init();
		setListener();
		
	}

	private void init(){
		commitButton = (Button)findViewById(R.id.button_commit);
		dstInput = (EditText)findViewById(R.id.destination);
		dstInput.setClickable(true);
		dateInput = (EditText)findViewById(R.id.date);
		//dateInput.setClickable(true);
		
	}

	private void setListener(){
		commitButton.setOnClickListener(this);
		dstInput.setOnClickListener(this);
		dateInput.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_commit:
			button_commit_click();		
			break;
			
		case R.id.destination:
			dstInput_click();
			break;
		case R.id.date:
			dateInput_click();
		default:
			break;
		}
	}
	
	private void button_commit_click(){
		Intent intent = new Intent();
		intent.setClass(AddrAndDateActivity.this, HomeActivity.class);
		startActivity(intent);
	}
	
	private void dstInput_click() {
		Intent intent = new Intent();
		intent.setClass(AddrAndDateActivity.this, AddressSelectActivity.class);
		startActivityForResult(intent, 1);
	}
	
	private void dateInput_click() {
		Intent intent = new Intent();
		intent.setClass(AddrAndDateActivity.this, CalendarActivity.class);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if(resultCode == RESULT_OK){
				String returnedData = data.getStringExtra("data_return");
				System.out.println("returnedData:"+returnedData);
				dstInput.setText(returnedData);
			}
			break;
		case 2:
			if(resultCode == RESULT_OK){
				String returnedData = data.getStringExtra("data_return");
				System.out.println("returnedData:"+returnedData);
				dateInput.setText(returnedData);
			}
			break;
		default:
			break;
		}	
	}
	
}

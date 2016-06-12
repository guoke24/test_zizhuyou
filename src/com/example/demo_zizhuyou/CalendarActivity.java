package com.example.demo_zizhuyou;


import guoke.model.InfoCom;
import guoke.model.MDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.example.demo_zizhuyou.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarActivity extends BaseActivity implements OnClickListener{

	private boolean hasChange = false;
	private Button commitButton;
	private TextView selectDaTextView;
	
	CalendarView calendarView;
	private long curDate; 
	final Calendar calendar = Calendar.getInstance(); 
	
	
	DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_calendar);
		init();
		setListener();
	}

	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		curDate = calendarView.getDate();
		
	}


	void init(){
		//initial the commitButton
		commitButton = (Button)findViewById(R.id.commit_button);
		//show the current date in the begin	
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  
        
		//initial the CalendarView
	    calendarView=(CalendarView) findViewById(R.id.calendarView1);
		calendarView.setShowWeekNumber(false);//not show the week numbers		
		curDate = calendarView.getDate();
		
		selectDaTextView = (TextView) findViewById(R.id.calendar_select_date);
		selectDaTextView.setText("选中日期："+df1.format(calendarView.getDate()));
        //Toast.makeText(getApplicationContext(), "当前选中日期："+format.format(calendarView.getDate()), 0).show();

	}
	
	void setListener(){
		commitButton.setOnClickListener(this);
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {

        	/*view：CalendarView对象。
			　　year：要设置的年。
			　　month：要设置的月份，范围是0-11。
			　　dayOfMonth：要设置每月的某一天。*/
			public void onSelectedDayChange(CalendarView view,
					int year, int month, int dayOfMonth) {
				hasChange = true;
				String date = df1.format(calendarView.getDate());
				selectDaTextView.setText("选中日期："+ date);

				if(isDateValidated()){
		            //Toast.makeText(getApplicationContext(), "选中："+date, Toast.LENGTH_SHORT).show();
		            commitButton.setClickable(true);
		    		commitButton.setAlpha((float) 1);
		    		//everytime the select date was change，the sXXX variable is change
				}else{
		            Toast.makeText(getApplicationContext(), date+"已经过去！", Toast.LENGTH_SHORT).show();
		            commitButton.setClickable(false);
		    		commitButton.setAlpha((float) 0.2);
				}
				 
			}
        });
	}
	
	/**
	 * 判断选中的日期是否合法
	 * @return
	 */
	private boolean isDateValidated(){
		long today = curDate/86400000;
		//加上一天过去的毫秒数
		int h = new Date().getHours();
		long left = 86400000*h/24;
		long selectday = (calendarView.getDate()+left)/86400000;
		
		//toast(today+","+selectday);
		
				
        if(today<=selectday){
        	return true;
        }
        else{
        	return false;
        }
		

		
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.commit_button:
			finishWithRes();
			break;

		default:
			break;
		}
	}
	
	private void finishWithRes(){
		int h = new Date().getHours();
		long left = 86400000*h/24;
		long selectDate = calendarView.getDate();
		if(hasChange){
			selectDate+=left;
		}
		MDate mdate = new MDate(selectDate);
		Intent intent = new Intent();
		intent.putExtra(InfoCom.Extra_DateSelect, mdate);
		setResult(RESULT_OK,intent);
		finish();
	}
	
}

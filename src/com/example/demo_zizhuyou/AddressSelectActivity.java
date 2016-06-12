package com.example.demo_zizhuyou;

import guoke.adapter.NameSortAdapter;
import guoke.custom.SideButton;
import guoke.custom.SideButton.OnTouchSideButtonListener;
import guoke.db.DBManager;
import guoke.fragment.CitysFragment;
import guoke.fragment.ResCitysFragment;
import guoke.model.InfoCom;
import guoke.model.MRegion;

import intetface.ReBackListener;

import java.util.ArrayList;
import java.util.List;

import com.example.demo_zizhuyou.R;
import com.loc.v;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class AddressSelectActivity extends BaseActivity implements OnClickListener{
	/*
	 * 数据库的操作变量
	 */
	private DBManager dbm;
	private SQLiteDatabase db;

	/*
	 * 查找
	 */
	private Button searchButton;
	private EditText searchEditText;
	/*
	 * fragment
	 */
	FragmentManager fragmentManager;
	CitysFragment citysListFragment;
	ResCitysFragment resCitysFragment;
	
	/**
	 * 初始化
	 */
	private void init() {
		//
		fragmentManager= getFragmentManager();		
		citysListFragment = new CitysFragment();
		
		//
		searchButton = (Button)findViewById(R.id.city_search_btn);
		
		searchEditText = (EditText)findViewById(R.id.search_input);
		//设置焦点，让scrollView显示在顶部
//		searchButton.setFocusable(true);//使能控件获得焦点,键盘输入
//		searchButton.setFocusableInTouchMode(true);//使能控件获得焦点,触摸
//		searchButton.requestFocus();//立刻获得焦点
//		searchButton.clearFocus();
		view = findViewById(R.id.add_inputlayout);
		view.clearFocus();
	}
	
	/**
	 * 设置监听
	 */
	private void setListener(){
		//
		searchButton.setOnClickListener(this);
		searchButton.setClickable(false);
		searchButton.setAlpha((float) 0.3);
		//所有城市列表的子item点击时候的回调函数
		citysListFragment.setReBackListener(new ReBackListener() {			
			@Override
			public void reback(MRegion data) {
				// TODO Auto-generated method stub
				finishWithResult(data);
			}
		});
		
		//监听文本输入变化
		searchEditText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				Log.d("edittext", "beforeTextChanged");
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if(searchEditText.getText().toString().equals("")
						||searchEditText.getText().toString()==null){					
					searchButton.setClickable(false);
					searchButton.setAlpha((float) 0.3);
					transToCitysFragment();
				}else{				
					searchButton.setClickable(true);
					searchButton.setAlpha((float) 1);
					transToResCitysFragment(searchEditText.getText().toString());
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
				Log.d("edittext", "afterTextChanged");
				Log.d("edittext", searchEditText.getText().toString());
			}
			
			
		});
	}
		
	View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_address);
		
		System.out.println("here:"+Environment.getDataDirectory().getAbsolutePath());
		
		init();
		setListener();
			
		transToCitysFragment();
		
	}

	/**
	 * 点击的监听函数
	 */
	@Override
	public void onClick(View v) {
		System.out.println("yeah");
		switch (v.getId()) {
			case R.id.city_search_btn:
				//转换到查询结果
				transToResCitysFragment(searchEditText.getText().toString());
				break;		
				
			default:
				break;
		}
	}
	
	/**
	 * 转换到显示所有城市的CitysFragment
	 */
	private void transToCitysFragment(){
		citysListFragment.setReBackListener(new ReBackListener() {
			
			@Override
			public void reback(MRegion addrItem) {
				// TODO Auto-generated method stub
				int id = getCityIdByRegion(addrItem);
				addrItem.setCid(id);
				finishWithResult(addrItem);				
			}
		});
		
		// 开启一次Fragment事务  
	    FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		//beginTransaction.add(R.id.fragment_content, citysListFragment);
		beginTransaction.replace(R.id.fragment_content, citysListFragment);
		//beginTransaction.addToBackStack(null);
		beginTransaction.commit();
	}
	/**
	 * 转换到显示查询结果的ResCitysFragment
	 */
	private void transToResCitysFragment(String search_data){
		Bundle bundle = new Bundle();
		bundle.putString("search_data", search_data);
		//转变到第二个查找结果的fragment	    
	    resCitysFragment = new ResCitysFragment();
	    resCitysFragment.setArguments(bundle);
	  //查询结果的列表item点击
	  	resCitysFragment.setReBackListener(new ReBackListener() {

			@Override
			public void reback(MRegion data) {
				int id = getCityIdByRegion(data);
				data.setCid(id);
				finishWithResult(data);				
			}	  		
	  	});
		// 开启一个Fragment事务  
	    FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		//beginTransaction.hide(citysListFragment);
		//beginTransaction.add(R.id.fragment_content, resCitysFragment);
		beginTransaction.replace(R.id.fragment_content, resCitysFragment);
		//beginTransaction.addToBackStack(null);
		//beginTransaction.show(resCitysFragment);
		beginTransaction.commit();
	}

	/**
	 * 带返回结果的结束函数，封装了finish()
	 * @param return_data
	 */
	void finishWithResult(MRegion return_region){
		Intent intent = new Intent();
		intent.putExtra(InfoCom.Extra_RegionReturn, return_region);
		setResult(RESULT_OK,intent);
		finish();
	}
	
	void getCidForCity(MRegion r){
		
	}
}

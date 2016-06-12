package com.example.demo_zizhuyou;

import guoke.home.CarFragment;
import guoke.home.TicketFragment;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BeginCarActivity extends BaseActivity {

	CarFragment carFragment;
	private FragmentManager fgmManager;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_begin_car);
		
		init();
		tranToHotelFragment();
		
	}
	
	void init(){
		fgmManager = getFragmentManager();
	}

	void tranToHotelFragment(){
		FragmentTransaction bt = fgmManager.beginTransaction();
		carFragment = new CarFragment();
		bt.replace(R.id.content, carFragment);
		bt.commit();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {

		// 设置取车时间
		case InfoCom.requestGetCarDate:
			if (resultCode == RESULT_OK) {
				MDate dateSelect = (MDate) data
						.getSerializableExtra(InfoCom.Extra_DateSelect);
				carFragment.setGetCar_Date(dateSelect);
			}
			break;
		// 设置还车时间
		case InfoCom.requestReturnCarDate:
			if (resultCode == RESULT_OK) {
				MDate dateSelect = (MDate) data
						.getSerializableExtra(InfoCom.Extra_DateSelect);
				carFragment.setReturnCar_Date(dateSelect);
			}
			break;
		case InfoCom.requestGetCarCity:
			if (resultCode == RESULT_OK) {
				MRegion return_region = (MRegion) data
						.getSerializableExtra(InfoCom.Extra_RegionReturn);
				carFragment.setGetCar_City(return_region);
			}
			break;
		case InfoCom.requestReturnCarCity:
			if (resultCode == RESULT_OK) {
				MRegion return_region = (MRegion) data
						.getSerializableExtra(InfoCom.Extra_RegionReturn);
				carFragment.setReturnCar_City(return_region);
			}
			break;

		default:
			break;
		}
	}



}

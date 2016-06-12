package com.example.demo_zizhuyou;

import guoke.home.HotelFragment;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;
import guoke.orderlist.RoomOrderFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BeginHotelActivity extends BaseActivity {

	HotelFragment hotelFragment;
	private FragmentManager fgmManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotel_begin);
		
		init();
		tranToHotelFragment();
	}

	void init(){
		fgmManager = getFragmentManager();
	}

	void tranToHotelFragment(){
		FragmentTransaction bt = fgmManager.beginTransaction();
		hotelFragment = new HotelFragment();
		bt.replace(R.id.content, hotelFragment);
		bt.commit();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		// 设置入住城市
		case InfoCom.requestCheckInCity:
			if (resultCode == RESULT_OK) {
				MRegion return_region = (MRegion) data
						.getSerializableExtra(InfoCom.Extra_RegionReturn);
				hotelFragment.setCurRegion(return_region);
				// hotelFragment.setCheckInCityTextView(return_region.getRname());
			}
			break;
		// 设置入住时间
		case InfoCom.requestCheckInTime:
			if (resultCode == RESULT_OK) {
				// String return_data = data.getStringExtra("data_return");
				MDate dateSelect = (MDate) data
						.getSerializableExtra(InfoCom.Extra_DateSelect);
				hotelFragment.setCheckIn_Date(dateSelect);
				// hotelFragment.setCheckInTimeTextView(return_data);
			}
			break;
		// 设置离开时间
		case InfoCom.requestCheckOutTime:
			if (resultCode == RESULT_OK) {
				MDate dateSelect = (MDate) data
						.getSerializableExtra(InfoCom.Extra_DateSelect);
				hotelFragment.setCheckOut_Date(dateSelect);
				// hotelFragment.setCheckOutTimeTextView(return_data);
			}
			break;
		default:
			break;
		}
	}
	
	
}

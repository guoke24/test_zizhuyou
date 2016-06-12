package com.example.demo_zizhuyou;

import guoke.home.TicketFragment;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class BeginSceneryActivity extends BaseActivity {

	TicketFragment ticketFragment;
	private FragmentManager fgmManager;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_begin_scenery);
		
		init();
		tranToHotelFragment();
		
	}

	void init(){
		fgmManager = getFragmentManager();
	}

	void tranToHotelFragment(){
		FragmentTransaction bt = fgmManager.beginTransaction();
		ticketFragment = new TicketFragment();
		bt.replace(R.id.content, ticketFragment);
		bt.commit();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {

		case InfoCom.requestTravelCity:
			if (resultCode == RESULT_OK) {
				MRegion return_region = (MRegion) data
						.getSerializableExtra(InfoCom.Extra_RegionReturn);
				ticketFragment.setDsCityRegion(return_region);
			}
			break;
		case InfoCom.requestBeginDate:
			if (resultCode == RESULT_OK) {
				MDate dateSelect = (MDate) data
						.getSerializableExtra(InfoCom.Extra_DateSelect);
				ticketFragment.setBgDate(dateSelect);
			}
			break;
		case InfoCom.requestFinishDate:
			if (resultCode == RESULT_OK) {
				MDate dateSelect = (MDate) data
						.getSerializableExtra(InfoCom.Extra_DateSelect);
				ticketFragment.setFnDate(dateSelect);
			}
			break;
		default:
			break;
		}
	}
	
	
}

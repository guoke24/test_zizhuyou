package com.example.demo_zizhuyou;

import guoke.home.CateFragment;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class BeginCateActivity extends BaseActivity {

	CateFragment cateFragment;
	private FragmentManager fgmManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_begin_cate);
		
		init();
		tranToHotelFragment();
		
	}

	void init(){
		fgmManager = getFragmentManager();
	}

	void tranToHotelFragment(){
		FragmentTransaction bt = fgmManager.beginTransaction();
		cateFragment = new CateFragment();
		bt.replace(R.id.content, cateFragment);
		bt.commit();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {

		case 2333:
			if (resultCode == RESULT_OK) {
				MRegion return_region = (MRegion) data
						.getSerializableExtra(InfoCom.Extra_RegionReturn);
				cateFragment.setCurCityAndLoad(return_region);
			}
			break;

		default:
			break;
		}
	}
	
	
	
}

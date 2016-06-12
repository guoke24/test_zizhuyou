package guoke.orderlist;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.layout;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class RoomOrderListActivity extends BaseActivity {

	private FragmentManager fgmManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_roon_list);
		
		init();
		tranToAllOrderFragment();
	}

	void init(){
		fgmManager = getFragmentManager();
	}

	/**
	 * 先显示全部的订单
	 */
	void tranToAllOrderFragment(){
		FragmentTransaction bt = fgmManager.beginTransaction();
		RoomOrderFragment allOrderFragment = new RoomOrderFragment();
		bt.replace(R.id.room_orderlist_fragment, allOrderFragment);
		bt.commit();
	}
	
}

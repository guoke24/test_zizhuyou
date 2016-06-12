package guoke.home;

import java.util.Date;

import javax.security.auth.PrivateCredentialPermission;

import guoke.model.MDate;
import guoke.model.MRegion;
import guoke.model.InfoCom;
import guoke.model.TravelInfo;

import com.example.demo_zizhuyou.R;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.R.integer;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * 项目的主Activity，所有的Fragment都嵌入在这里。
 * 
 * @author guolin
 */
public class HomeActivity extends Activity implements OnClickListener {

	/**
	 * 用于展示的Fragment
	 */
	private HomeFragment homeFragment;
	private HotelFragment hotelFragment;
	private CarFragment carFragment;
	private TicketFragment ticketFragment;
	private CateFragment cateFragment;
	private MyselfFragment myselfFragment;
	private MapFunFragment mapFragment;

	private int tabSelect = 0;
	Boolean hasOnCreat = false;
	/**
	 * 界面布局
	 */
	private View hotelLayout;
	private View carLayout;
	private View ticketLayout;
	private View cateLayout;
	private View myselfLayout;

	/**
	 * 在Tab布局上显示图标的控件
	 */
	private ImageView hotelImage;
	private ImageView carImage;
	private ImageView ticketImage;
	private ImageView cateImage;
	private ImageView myselfImage;

	/**
	 * 在Tab布局上显示文字的控件
	 */
	private TextView hotelText;
	private TextView carText;
	private TextView ticketText;
	private TextView cateText;
	private TextView myselfText;

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;

	//
	// private MRegion addrItem;
	// private Date date;
	// private int durationDays;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		hasOnCreat = true;
		// 初始化布局元素
		initViews();
		fragmentManager = getFragmentManager();
		// 获取出游信息
		// getTravelInfo();
		setTabSelection(5);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 第一次启动时选中第0个tab
		int i = getTabSelect();
		//setTabSelection(5);

	}

	@Override
	protected void onStop() {
		super.onStop();
		// hotelFragment = null;
		// carFragment = null;
		// ticketFragment = null;
		// cateFragment = null;
		// myselfFragment = null;
		recordTabSelect();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		clearSelection();
	}

	/**
	 * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
	 */
	private void initViews() {
		//

		hotelLayout = findViewById(R.id.hotel_layout);
		carLayout = findViewById(R.id.car_layout);
		ticketLayout = findViewById(R.id.ticket_layout);
		cateLayout = findViewById(R.id.cate_layout);
		myselfLayout = findViewById(R.id.myself_layout);
		//
		hotelImage = (ImageView) findViewById(R.id.hotel_image);
		carImage = (ImageView) findViewById(R.id.car_image);
		ticketImage = (ImageView) findViewById(R.id.ticket_image);
		cateImage = (ImageView) findViewById(R.id.cate_image);
		myselfImage = (ImageView) findViewById(R.id.myself_image);
		//
		hotelText = (TextView) findViewById(R.id.hotel_text);
		carText = (TextView) findViewById(R.id.car_text);
		ticketText = (TextView) findViewById(R.id.ticket_text);
		cateText = (TextView) findViewById(R.id.cate_text);
		myselfText = (TextView) findViewById(R.id.myself_text);
		//
		hotelLayout.setOnClickListener(this);
		carLayout.setOnClickListener(this);
		ticketLayout.setOnClickListener(this);
		cateLayout.setOnClickListener(this);
		myselfLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hotel_layout:
			// setTabSelection(0);
			setTabSelection(5);
			break;
		case R.id.car_layout:
			setTabSelection(1);
			break;
		case R.id.ticket_layout:
			setTabSelection(2);
			break;
		case R.id.cate_layout:
			// setTabSelection(3);
			setTabSelection(6);
			break;
		case R.id.myself_layout:
			setTabSelection(4);
			break;
		default:
			break;
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。0表示酒店，1表示租车，2表示门票，3表示美食。
	 */
	private void setTabSelection(int index) {
		tabSelect = index;
		// 开启一个Fragment事务
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(beginTransaction);
		switch (index) {
		case 0:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			hotelImage.setImageResource(R.drawable.message_selected);
			hotelText.setTextColor(Color.WHITE);
			if (hotelFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				hotelFragment = new HotelFragment();
				// 传递travelInfo

				beginTransaction.add(R.id.content, hotelFragment);
				//
				getFragmentManager().beginTransaction().add(R.id.content,
						hotelFragment);

			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				beginTransaction.show(hotelFragment);
			}
			break;
		case 1:
			// 当点击了联系人tab时，改变控件的图片和文字颜色
			carImage.setImageResource(R.drawable.contacts_selected);
			carText.setTextColor(Color.WHITE);
			if (carFragment == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				carFragment = new CarFragment();
				beginTransaction.add(R.id.content, carFragment);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				beginTransaction.show(carFragment);
			}
			break;
		case 2:
			// 当点击了动态tab时，改变控件的图片和文字颜色
			ticketImage.setImageResource(R.drawable.news_selected);
			ticketText.setTextColor(Color.WHITE);
			if (ticketFragment == null) {
				// 如果NewsFragment为空，则创建一个并添加到界面上
				ticketFragment = new TicketFragment();
				beginTransaction.add(R.id.content, ticketFragment);
			} else {
				// 如果SettingFragment不为空，则直接将它显示出来
				beginTransaction.show(ticketFragment);
			}
			break;
		case 3:
			cateImage.setImageResource(R.drawable.setting_selected);
			cateText.setTextColor(Color.WHITE);
			if (cateFragment == null) {
				cateFragment = new CateFragment();
				beginTransaction.add(R.id.content, cateFragment);
			} else {
				beginTransaction.show(cateFragment);
			}
			break;
		case 4:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			myselfImage.setImageResource(R.drawable.message_selected);
			myselfText.setTextColor(Color.WHITE);
			if (myselfFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				myselfFragment = new MyselfFragment();
				beginTransaction.add(R.id.content, myselfFragment);
				//
				getFragmentManager().beginTransaction().add(R.id.content,
						myselfFragment);

			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				beginTransaction.show(myselfFragment);
			}
			break;
		case 5:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			hotelImage.setImageResource(R.drawable.contacts_selected);
			hotelText.setTextColor(Color.WHITE);
			if (homeFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				homeFragment = new HomeFragment();
				// 传递travelInfo

				beginTransaction.add(R.id.content, homeFragment);
				//
				getFragmentManager().beginTransaction().add(R.id.content,
						homeFragment);

			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				beginTransaction.show(homeFragment);
			}
			break;
		case 6:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			cateImage.setImageResource(R.drawable.setting_selected);
			cateText.setTextColor(Color.WHITE);
			if (mapFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mapFragment = new MapFunFragment();
				// 传递travelInfo

				beginTransaction.add(R.id.content, mapFragment);
				//
				getFragmentManager().beginTransaction().add(R.id.content,
						mapFragment);

			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				beginTransaction.show(mapFragment);
			}

			break;
		}
		beginTransaction.commit();
	}

	/**
	 * 清除掉所有的选中状态,
	 */
	private void clearSelection() {
		hotelImage.setImageResource(R.drawable.contacts_unselected);
		hotelText.setTextColor(Color.parseColor("#82858b"));
		carImage.setImageResource(R.drawable.contacts_unselected);
		carText.setTextColor(Color.parseColor("#82858b"));
		ticketImage.setImageResource(R.drawable.news_unselected);
		ticketText.setTextColor(Color.parseColor("#82858b"));
		cateImage.setImageResource(R.drawable.setting_unselected);
		cateText.setTextColor(Color.parseColor("#82858b"));
		myselfImage.setImageResource(R.drawable.message_unselected);
		myselfText.setTextColor(Color.parseColor("#82858b"));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param beginTransaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction beginTransaction) {
		if (hotelFragment != null) {
			beginTransaction.hide(hotelFragment);
		}
		if (carFragment != null) {
			beginTransaction.hide(carFragment);
		}
		if (ticketFragment != null) {
			beginTransaction.hide(ticketFragment);
		}
		if (cateFragment != null) {
			beginTransaction.hide(cateFragment);
		}
		if (myselfFragment != null) {
			beginTransaction.hide(myselfFragment);
		}
		if (homeFragment != null) {
			beginTransaction.hide(homeFragment);
		}
		if(mapFragment != null){
			beginTransaction.hide(mapFragment);
		}
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

	private long exitTime = 0;

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() ==
	// KeyEvent.ACTION_DOWN){
	// if((System.currentTimeMillis()-exitTime) > 2000){
	// Toast.makeText(getApplicationContext(), "再按一次退出程序",
	// Toast.LENGTH_SHORT).show();
	// exitTime = System.currentTimeMillis();
	// } else {
	// finish();
	// System.exit(0);
	// return true;
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	/**
	 * 记录 登录状态
	 * 
	 * @param isLogin
	 */
	public void recordTabSelect() {
		SharedPreferences.Editor editor = getSharedPreferences(
				"TabSelectStatu", 0).edit();
		editor.clear();
		editor.putInt("tabSelect", tabSelect);
		editor.commit();
	}

	public int getTabSelect() {
		SharedPreferences preferences = getSharedPreferences("TabSelectStatu",
				0);
		int i = preferences.getInt("tabSelect", 0);
		return i;
	}

	public void clearTabSelect() {
		SharedPreferences.Editor editor = getSharedPreferences(
				"TabSelectStatu", 0).edit();
		editor.clear();
		editor.putInt("tabSelect", 0);
		editor.commit();
	}

}

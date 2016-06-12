package guoke.activity;

import intetface.StartActivityWithObjectListener;
import guoke.bean.HotelBean;
import guoke.bean.RoomBean;
import guoke.fragment.RoomsFragment;
import guoke.home.HomeActivity;
import guoke.imageloader.BaseListActivity;
import guoke.model.HotelItem;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MyUser;
import guoke.model.MRegion;
import guoke.model.RoomItem;


import cn.bmob.v3.BmobUser;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.FastLoginActivity;
import com.example.demo_zizhuyou.LoginActivity;
import com.example.demo_zizhuyou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class HotelDetailActivity extends BaseListActivity{
	
	private FragmentManager fragmentManager;
	private ScrollView scrollView;
	private ImageView hotelImageView;
	DisplayImageOptions options;

	//
	//private HotelItem curHotel;
	private HotelBean curHotel;
	private MDate checkIn_Date;
	private MDate checkOut_Date;
	
	private TextView checkIn_CityTextView;
	private TextView checkIn_DateTextView;
	private TextView checkOut_DateTextView;
	
	private TextView hotelName;
	private TextView hotelRegion;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hotel_datail);
		fragmentManager = getFragmentManager();
		getSource_intent();
		init();
	}
	
	void getSource_intent(){
		curHotel = (HotelBean)getIntent().getSerializableExtra(InfoCom.Extra_HotelSelect);

	}
	
	private void init() {
		
		hotelImageView = (ImageView) findViewById(R.id.hotel_detail_imageview);
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
		
		
		//
		hotelName = (TextView)findViewById(R.id.hotel_detail_name);
		hotelRegion = (TextView)findViewById(R.id.hotel_detail_loaction);
		//
		//curHotel = (HotelItem)getIntent().getSerializableExtra(InfoCom.Extra_HotelSelect);
		checkIn_Date = (MDate)getIntent().getSerializableExtra(InfoCom.Extra_CheckInDate);
		checkOut_Date = (MDate)getIntent().getSerializableExtra(InfoCom.Extra_CheckOutDate);
		//
		hotelName.setText(curHotel.getName());
		hotelRegion.setText(curHotel.getAddress());
		//
		showRoomFragment(curHotel);
		
		toTop();
		
		imageLoader.displayImage(curHotel.getLargePic(), hotelImageView, options, new SimpleImageLoadingListener() {
			
		});
		
	}
	
	
	private void showRoomFragment(HotelBean ht){
		RoomsFragment roomsFragment = new RoomsFragment();
		roomsFragment.setStartWithRoomListener(new StartActivityWithObjectListener<RoomBean>() {
			
			@Override
			public void startWithObject(RoomBean t) {
				//仅测试
				//BmobUser.logOut(HotelDetailActivity.this);
				//跳转到预订页面的代码写到了这里
				Intent intent = new Intent();
				intent.putExtra(InfoCom.Extra_HotelSelect, curHotel);
				intent.putExtra(InfoCom.Extra_CheckInDate, checkIn_Date);
				intent.putExtra(InfoCom.Extra_CheckOutDate, checkOut_Date);
				intent.putExtra(InfoCom.Extra_RoomSelect, t);
				//判断有没有登录
				MyUser userInfo = BmobUser.getCurrentUser(HotelDetailActivity.this,MyUser.class);
				if(userInfo != null){
					intent.setClass(HotelDetailActivity.this, HotelRoomReserveActivity.class);
					
				}else{
					//调到登陆页面，而且带有信号
					intent.putExtra(InfoCom.fromWhere, InfoCom.fromHotelDetail);
					intent.setClass(HotelDetailActivity.this, LoginActivity.class);
				}
				
				startActivity(intent);
			}
		});
		
		//
		Bundle bundle = new Bundle();
		bundle.putSerializable(InfoCom.Extra_HotelSelect, ht);
		roomsFragment.setArguments(bundle);
		//
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.room_list_fragment,roomsFragment);
		fragmentTransaction.commit();
	}
	
	private void toTop(){
		hotelImageView = (ImageView)findViewById(R.id.hotel_detail_imageview);
		//设置焦点，让scrollView显示在顶部
		hotelImageView.setFocusable(true);//使能控件获得焦点,键盘输入
		hotelImageView.setFocusableInTouchMode(true);//使能控件获得焦点,触摸
		hotelImageView.requestFocus();//立刻获得焦点		
	}
	
	
}

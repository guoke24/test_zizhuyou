package guoke.activity;

import guoke.home.HomeActivity;
import guoke.model.CarItem;
import guoke.model.CarOrder;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;
import guoke.model.MyUser;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

public class CarReserveActivity extends BaseActivity implements OnClickListener{

	private final int driverFee = 60;
	private ScrollView scrollView ;
	//
	private ImageView carImageView;
	private TextView carNameTextView;//车名
	private TextView carDetailTextView;//车的详情
	private TextView carPriceTextView;
	private TextView getCar_DateTextView;//取车日期
	private TextView returnCar_DateTextView;//还车日期
	private TextView getCar_cityTextView;//取车城市
	private TextView returnCar_cityTextView;//还车城市
	private TextView usecar_datsTextView;//用车天数
	//
	private CheckBox isNoteBox;//是否发票
	private CheckBox isDriverBox;//是否配司机
	//
	private TextView rent_FeeTextView;//租车费
	private TextView premium_feeTextView;//保险费
	private TextView driver_feeTextView;//保险费
	private TableRow driver_feeTableRow;//司机费用的显示列
	private TextView sum_feeTextView;//总费用
	//
	private Button commit_order;//提交按钮
	//
	private MRegion getCar_Region;
	private MRegion returnCar_Region;
	private MDate getCar_Date;
	private MDate returnCar_Date;
	private CarItem carSelect;
	//
	private Intent source_intent;
	//
	
	Handler handler =new Handler(){
		
	};
	
	DisplayImageOptions options ;								// 创建配置过得DisplayImageOption对象		

	protected ImageLoader imageLoader = ImageLoader.getInstance();;
	
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	/**
	 * 图片加载第一次显示监听器
	 * @author Administrator
	 *
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_reserve);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ic_empty)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ic_error)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
		.displayer(new RoundedBitmapDisplayer(20))	// 设置成圆角图片
		.build();									// 创建配置过得DisplayImageOption对象		

		
		getIntentData();
		init();
		setListener();
		
	}

	/**
	 * 从source_intent里获取数据哦！
	 */
	void getIntentData(){
		source_intent = getIntent();
		carSelect = (CarItem) source_intent.getSerializableExtra(InfoCom.Extra_CarSelect);
		getCar_Region = (MRegion) source_intent.getSerializableExtra(InfoCom.Extra_GetCarCity);
		returnCar_Region = (MRegion) source_intent.getSerializableExtra(InfoCom.Extra_ReturnCarCity);
		getCar_Date = (MDate) source_intent.getSerializableExtra(InfoCom.Extra_GetCarDate);
		returnCar_Date = (MDate) source_intent.getSerializableExtra(InfoCom.Extra_ReturnCarDate);
	}
	
	void init(){
		scrollView = (ScrollView) findViewById(R.id.order_scrollView);
		
		carImageView = (ImageView) findViewById(R.id.order_car_item_imageView);
		imageLoader.displayImage(carSelect.getPicBmobFile().getUrl(), 
				carImageView, options, animateFirstListener);
		
		carNameTextView = (TextView) findViewById(R.id.carReserve_item_name);
		carNameTextView.setText(carSelect.getCname());
		
		carDetailTextView = (TextView) findViewById(R.id.carReserve_item_detail);
		carDetailTextView.setText(carSelect.getCdetail());
		
		carPriceTextView = (TextView)findViewById(R.id.carReserve_item_price);
		carPriceTextView.setText("￥"+carSelect.getCprice()+"/天");
		
		getCar_DateTextView = (TextView)findViewById(R.id.carReserve__get_car_date);
		getCar_DateTextView.setText(getMonth_Day(getCar_Date));
		
		returnCar_DateTextView = (TextView) findViewById(R.id.carReserve__return_car_date);
		returnCar_DateTextView.setText(getMonth_Day(returnCar_Date));
		
		getCar_cityTextView = (TextView)findViewById(R.id.carReserve_getCar_city);
		getCar_cityTextView.setText(getCar_Region.getRname());
		
		returnCar_cityTextView = (TextView) findViewById(R.id.carReserve_returnCar_city);
		returnCar_cityTextView.setText(returnCar_Region.getRname());
		
		usecar_datsTextView = (TextView) findViewById(R.id.carReserve_car_duration);
		usecar_datsTextView.setText(getDuraionDays(getCar_Date, returnCar_Date));
		
		commit_order = (Button)findViewById(R.id.carReserve_commit_order);
		//
		isNoteBox = (CheckBox) findViewById(R.id.carReserve_isnote);
		isDriverBox = (CheckBox) findViewById(R.id.carReserve_isdriver);
		//
		rent_FeeTextView = (TextView) findViewById(R.id.carReserve_car_rent_fee);
		rent_FeeTextView.setText(getRentCarFeeString());
		
		premium_feeTextView = (TextView) findViewById(R.id.carReserve__premium_fee);
		premium_feeTextView.setText("￥"+carSelect.getCpremium());
		
		driver_feeTextView = (TextView) findViewById(R.id.carReserve_driver_fee);
		driver_feeTableRow = (TableRow) findViewById(R.id.carReserve_driver_tableRow);
		
		sum_feeTextView = (TextView) findViewById(R.id.carReserve_sum_fee);
		sum_feeTextView.setText(getSumFeeString());		
	}
	void setListener(){
		isDriverBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				toast("选择了司机");
				if(isDriverBox.isChecked()){
					driver_feeTableRow.setVisibility(View.VISIBLE);
					driver_feeTextView.setText(getDriverTotelFee());
					sum_feeTextView.setText(getSumFeeString());
					new Handler().post(new Runnable() {  
					    @Override  
					    public void run() {  
					        scrollView.fullScroll(ScrollView.FOCUS_DOWN);  
					    }  
					});  
				}else{
					driver_feeTableRow.setVisibility(View.GONE);
					sum_feeTextView.setText(getSumFeeString());
				}
			}
		});
		commit_order.setOnClickListener(this);
	}

	private String getDriverTotelFee(){
		int days = Integer.valueOf(getDuraionDays(getCar_Date, returnCar_Date));
		int totalfee = days*driverFee;
		return String.valueOf("("+days+"x"+driverFee+")"+" ￥"+totalfee);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.carReserve_commit_order:
			commit_order_click();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 点击 提交时，调用
	 */
	private void commit_order_click(){
		
		MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
		if(myUser != null){
			showProgressDialog();
			//设置车辆关联信息
			CarOrder carOrder = new CarOrder();
			carOrder.setUser_id(myUser.getObjectId());
			carOrder.setCar_id(carSelect.getObjectId());
			carOrder.setCar_name(carSelect.getCname());
			carOrder.setCar_detail(carSelect.getCdetail());
			carOrder.setUsecardays(Integer.valueOf(usecar_datsTextView.getText().toString()));
			carOrder.setCarpic(carSelect.getPicBmobFile().getUrl());
			carOrder.setGetcarcity(getCar_Region.getRname());
			//设置状态
			carOrder.setStatus(0);
			//设置日期
			carOrder.setGetCarDate(new BmobDate(getCar_Date));
			carOrder.setReturnCarDate(new BmobDate(returnCar_Date));
			//开发票和配司机
			carOrder.setIsDriver(isDriverBox.isChecked());
			carOrder.setIsNote(isNoteBox.isChecked());
			//设置价格
			carOrder.setDayfee(carSelect.getCprice());
			carOrder.setPremium(carSelect.getCpremium());
			if(isDriverBox.isChecked()){
				carOrder.setDriverfee(carSelect.getCdriverfee());
			}
			carOrder.setFee(getSumFee());
			
			carOrder.save(this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					//toast("提交成功");
					closeProgressDialog();
					OnreserveSuccess();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					closeProgressDialog();
					toast("提交失败");
				}
			});
			closeProgressDialog();
		}else{
			closeProgressDialog();
			toast("未登录，请先登陆");
		}
	}

	/**
	 * 预订成功 调用
	 */
	private void OnreserveSuccess(){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle("");
		dialog.setMessage("预订成功");
		dialog.setNegativeButton("返回主界面", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setClass(CarReserveActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
//		dialog.setPositiveButton("停留在订单页", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		});
		dialog.show();
	}
	
	/**
	 * 取得总的租车费用
	 * @return
	 */
	private Double getRentCarFee(){
		int days = Integer.valueOf(getDuraionDays(getCar_Date, returnCar_Date));
		Double sumfee = days*carSelect.getCprice();
		return sumfee ;
	}
	
	/**
	 * 根据天数 取得租车的费用的字符串形式
	 * @return
	 */
	private String getRentCarFeeString(){
		int days = Integer.valueOf(getDuraionDays(getCar_Date, returnCar_Date));
		String string = String.valueOf(getRentCarFee());
		String fix= "("+days+"天x￥"+carSelect.getCprice()+") ";
		return fix+"￥"+string;
	}
	
	private Double getSumFee(){
		int days = Integer.valueOf(getDuraionDays(getCar_Date, returnCar_Date));
		double rent_car_sum = days*carSelect.getCprice();
		double sum = carSelect.getCpremium() + rent_car_sum;
		if(isDriverBox.isChecked()){
			sum += days*driverFee;
		}
		return sum;
	}
	/**
	 * 得到总费用的字符串
	 * @return
	 */
	private String getSumFeeString(){
		
		return "￥"+String.valueOf(getSumFee());
	}

}

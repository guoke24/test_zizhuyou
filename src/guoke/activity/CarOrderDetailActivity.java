package guoke.activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.drawable;
import com.example.demo_zizhuyou.R.id;
import com.example.demo_zizhuyou.R.layout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import guoke.model.CarOrder;
import guoke.model.InfoCom;
import guoke.model.TicketOrder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

public class CarOrderDetailActivity extends BaseActivity {

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
	
	
	private Intent source_Intent;
	private CarOrder carOrder;
	
	void getSourceintent(){
		source_Intent = getIntent();
		carOrder = (CarOrder) source_Intent.getSerializableExtra(InfoCom.Extra_CarOrderSelect);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_order_detail);
		getSourceintent();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ic_empty)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ic_error)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
		.displayer(new RoundedBitmapDisplayer(20))	// 设置成圆角图片
		.build();									// 创建配置过得DisplayImageOption对象		

		init();
		
	}

	void init(){
		
		scrollView = (ScrollView) findViewById(R.id.order_scrollView);
		
		carImageView = (ImageView) findViewById(R.id.order_car_item_imageView);
		imageLoader.displayImage(carOrder.getCarpic(), 
				carImageView, options, animateFirstListener);
		
		carNameTextView = (TextView) findViewById(R.id.carReserve_item_name);
		carNameTextView.setText(carOrder.getCar_name());
		
		carDetailTextView = (TextView) findViewById(R.id.carReserve_item_detail);
		carDetailTextView.setText(carOrder.getCar_detail());
		
		carPriceTextView = (TextView)findViewById(R.id.carReserve_item_price);
		carPriceTextView.setText("￥"+carOrder.getDayfee()+"/天");
		
		getCar_DateTextView = (TextView)findViewById(R.id.carReserve__get_car_date);
		getCar_DateTextView.setText(getYMD(carOrder.getGetCarDate()));
		
		returnCar_DateTextView = (TextView) findViewById(R.id.carReserve__return_car_date);
		returnCar_DateTextView.setText(getYMD(carOrder.getReturnCarDate()));
		
		getCar_cityTextView = (TextView)findViewById(R.id.carReserve_getCar_city);
		getCar_cityTextView.setText(carOrder.getGetcarcity());
		
		returnCar_cityTextView = (TextView) findViewById(R.id.carReserve_returnCar_city);
		returnCar_cityTextView.setText(carOrder.getGetcarcity());
		
		usecar_datsTextView = (TextView) findViewById(R.id.carReserve_car_duration);
		usecar_datsTextView.setText(carOrder.getUsecardays()+"");
		
		commit_order = (Button)findViewById(R.id.carReserve_commit_order);
		commit_order.setText("");
		commit_order.setVisibility(View.GONE);
		//
		isNoteBox = (CheckBox) findViewById(R.id.carReserve_isnote);
		if(carOrder.getIsNote()){
			isNoteBox.setChecked(true);
		}
		isNoteBox.setClickable(false);
		
		driver_feeTextView = (TextView) findViewById(R.id.carReserve_driver_fee);
		driver_feeTableRow = (TableRow) findViewById(R.id.carReserve_driver_tableRow);
		
		
		isDriverBox = (CheckBox) findViewById(R.id.carReserve_isdriver);
		if(carOrder.getIsDriver()){
			isDriverBox.setChecked(true);
			driver_feeTableRow.setVisibility(View.VISIBLE);
			driver_feeTextView.setText("￥"+carOrder.getDriverfee());
			
		}
		isDriverBox.setClickable(false);
		//
		rent_FeeTextView = (TextView) findViewById(R.id.carReserve_car_rent_fee);
		rent_FeeTextView.setText(getRentCarFeeString());
		
		premium_feeTextView = (TextView) findViewById(R.id.carReserve__premium_fee);
		premium_feeTextView.setText("￥"+carOrder.getPremium());
		
	
		sum_feeTextView = (TextView) findViewById(R.id.carReserve_sum_fee);
		sum_feeTextView.setText("￥"+carOrder.getFee());	
		
	}
	
	/**
	 * 获得年月日
	 * @param bmd
	 * @return
	 */
	private String getYMD(BmobDate bmd){
		String tmp = bmd.getDate();
		String ymd[] = tmp.split(" ");
		String y_m_d[] = ymd[0].split("-");
		
		return y_m_d[1]+"-"+y_m_d[2];
	}
	
	/**
	 * 根据天数 取得租车的费用的字符串形式
	 * @return
	 */
	private String getRentCarFeeString(){
		int days = Integer.valueOf(carOrder.getUsecardays());
		String string = String.valueOf(carOrder.getDayfee());
		String fix= "("+days+"天x￥"+carOrder.getDayfee()+") ";
		return fix+"￥"+string;
	}
	
	private String getStatuString(Integer i){
		if(i==0){//进行中，ing
			return "进行中";
		}else if(i==1){//已完成，complete
			return "已完成";
		}else{
			return "已取消";
		}
		
	}
	
	
}

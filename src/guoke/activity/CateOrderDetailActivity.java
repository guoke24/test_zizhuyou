package guoke.activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.id;
import com.example.demo_zizhuyou.R.layout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import guoke.model.CateOrder;
import guoke.model.InfoCom;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CateOrderDetailActivity extends BaseActivity {

	private ImageView cataviewImageView;
	
	private TextView catenameTextView;
	private TextView catedatailTextView;
	private TextView catepriceTextView;
	
	private View usercontentLayout;
	private View inputLayout;
	
	private TextView usernameEditText;
	private TextView userphoneEditText;	
	private TextView catenum;
	private Button addnumButton;
	private Button reduceButton ;
	
	private TextView loactionTextView;
	private TextView sumfeeTextView;
	private Button commitbButton;

	private TextView statusTextView;
	
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
	
	
	
	private Intent sourcr_intent;
	private CateOrder cateOrder;
	
	void getSource_intent(){
		sourcr_intent = getIntent();
		cateOrder = (CateOrder) sourcr_intent.getSerializableExtra(InfoCom.Extra_CateOrderSelect);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cate_order_detail);
		
		getSource_intent();
		init();
	}

	void init(){
		cataviewImageView = (ImageView) findViewById(R.id.cate_order_imageView);
		imageLoader.displayImage(cateOrder.getPhotos(), 
				cataviewImageView, options, animateFirstListener);

		
		catenameTextView = (TextView) findViewById(R.id.cate_order_name);
		catenameTextView.setText(cateOrder.getName());
		
		catedatailTextView = (TextView) findViewById(R.id.cate_order_detail);
		catedatailTextView.setText(cateOrder.getTags().replace("|", ","));
		
		usercontentLayout = findViewById(R.id.cate_order_usercontent_layout);
		inputLayout = findViewById(R.id.cate_order_usermessage_layout);
		
		catepriceTextView = (TextView) findViewById(R.id.cate_order_bottom_price);
		catepriceTextView.setText("￥"+String.valueOf(cateOrder.getAvg_price()));

		usernameEditText = (TextView) findViewById(R.id.cate_order_user_name_input);
		usernameEditText.setText(cateOrder.getUser_name());
		userphoneEditText = (TextView) findViewById(R.id.cate_order_user_phone_input);
		userphoneEditText.setText(cateOrder.getUser_phone());
		

		catenum =(TextView) findViewById(R.id.cate_order_cate_number);
		catenum.setText(""+cateOrder.getCateNum());
		
		loactionTextView = (TextView) findViewById(R.id.cate_order_region);
		loactionTextView.setText(cateOrder.getArea()+" "+cateOrder.getAddress());
		
		commitbButton = (Button) findViewById(R.id.cate_order_commit);
		commitbButton.setText("");
		
		sumfeeTextView = (TextView) findViewById(R.id.cate_order_price);
		sumfeeTextView.setText("￥"+String.valueOf(cateOrder.getSumFee()));
		
		statusTextView = (TextView) findViewById(R.id.cate_order_status);
		statusTextView.setText("订单状态："+getStatuString(cateOrder.getStatus()));
		
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

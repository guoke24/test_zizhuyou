package guoke.activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import guoke.bean.CateBean;
import guoke.home.HomeActivity;
import guoke.model.CateOrder;
import guoke.model.InfoCom;
import guoke.model.MyUser;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class CateReserveActivity extends BaseActivity implements OnClickListener{

	private ImageView cataviewImageView;
	
	private TextView catenameTextView;
	private TextView catedatailTextView;
	private TextView catepriceTextView;
	
	private View usercontentLayout;
	private View inputLayout;
	
	private EditText usernameEditText;
	private EditText userphoneEditText;	
	private TextView catenum;
	private Button addnumButton;
	private Button reduceButton ;
	
	private TextView loactionTextView;
	private TextView sumfeeTextView;
	private Button commitbButton;
	
	private Intent source_intent;
	private CateBean curCateBean;
	
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
	
	
	
	
	private void getSourceIntent(){
		source_intent = getIntent();
		curCateBean = (CateBean) source_intent.getSerializableExtra(InfoCom.Extra_CateSelect);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cate_reserve);
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ic_empty)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ic_error)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
		.displayer(new RoundedBitmapDisplayer(5))	// 设置成圆角图片
		.build();									// 创建配置过得DisplayImageOption对象		

		getSourceIntent();
		init();
		setListener();
	}
	
	void init(){
		cataviewImageView = (ImageView) findViewById(R.id.cate_reserve_imageView);
		imageLoader.displayImage(curCateBean.getPhotos(), 
				cataviewImageView, options, animateFirstListener);

		
		catenameTextView = (TextView) findViewById(R.id.cate_reserve_name);
		catenameTextView.setText(curCateBean.getName());
		
		catedatailTextView = (TextView) findViewById(R.id.cate_reserve_detail);
		catedatailTextView.setText(curCateBean.getTags().replace("|", ","));
		
		usercontentLayout = findViewById(R.id.cate_reserve_usercontent_layout);
		inputLayout = findViewById(R.id.cate_reserve_usermessage_layout);
		
		catepriceTextView = (TextView) findViewById(R.id.cate_reserve_bottom_price);
		setCatePrice();
		usernameEditText = (EditText) findViewById(R.id.cate_reserve_user_name_input);
		userphoneEditText = (EditText) findViewById(R.id.cate_reserve_user_phone_input);
		
		addnumButton = (Button) findViewById(R.id.cate_reserve_num_jia);
		reduceButton = (Button) findViewById(R.id.cate_reserve_num_jian);
		catenum =(TextView) findViewById(R.id.cate_reserve_cate_number);
		
		loactionTextView = (TextView) findViewById(R.id.cate_reserve_region);
		loactionTextView.setText(curCateBean.getArea()+" "+curCateBean.getAddress());
		
		commitbButton = (Button) findViewById(R.id.cate_reserve_commit);
		
		sumfeeTextView = (TextView) findViewById(R.id.cate_reserve_price);
		if(curCateBean.getAvg_price()!=0){
			sumfeeTextView.setText(getSumPriceString());
		}else{
			sumfeeTextView.setText("请到现场消费");
			usercontentLayout.setVisibility(View.GONE);
			inputLayout.setVisibility(View.GONE);
		}
		
	}

	void setListener(){
		addnumButton.setOnClickListener(this);
		reduceButton.setOnClickListener(this);

		commitbButton.setOnClickListener(this);
		//如果平均消费为0，设置button不能点击
		if(curCateBean.getAvg_price()==0){
			//commitbButton.setText("现场消费");
			commitbButton.setClickable(false);
			commitbButton.setAlpha((float)0.3);
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cate_reserve_commit:
			commit_click();
			break;
		case R.id.cate_reserve_num_jian:
			int n = Integer.valueOf(catenum.getText().toString());
			if(n>1){
				catenum.setText((--n)+"");
				sumfeeTextView.setText(getSumPriceString());
			}
			break;
		case R.id.cate_reserve_num_jia:
			int m = Integer.valueOf(catenum.getText().toString());
			if(m<5){
				catenum.setText((++m)+"");
				sumfeeTextView.setText(getSumPriceString());
			}else{
				toast("每个人最多能定5张");
			}
			break;
		default:
			break;
		}
	}
	
	void commit_click(){
		
		if(isInputValidate()){
			showProgressDialog();
			MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
			if(myUser != null){
				CateOrder ctOrder = new CateOrder();
				ctOrder.setUser_id(myUser.getObjectId());
				ctOrder.setUser_name(usernameEditText.getText().toString());
				ctOrder.setUser_phone(userphoneEditText.getText().toString());
				
				ctOrder.setCateNum(Integer.valueOf(catenum.getText().toString()));
				ctOrder.setSumFee(countSumPrice());
				ctOrder.setStatus(0);
				ctOrder.setName(curCateBean.getName());
				ctOrder.setTags(curCateBean.getTags());
				ctOrder.setAddress(curCateBean.getAddress());
				ctOrder.setArea(curCateBean.getArea());
				ctOrder.setAvg_price(curCateBean.getAvg_price());
				ctOrder.setCity(curCateBean.getCity());
				ctOrder.setPhotos(curCateBean.getPhotos());
				
				ctOrder.save(this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						//toast("提交成功");
						closeProgressDialog();
						onSuccess_reserve();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						closeProgressDialog();
						toast("提交失败，网络故障");
					}
				});
				
			}else{
				toast("未登录，请先登录");
			}
		}
		
	}
	
	/**
	 * 判断入住人和手机是否为空
	 * @return
	 */
	private Boolean isInputValidate(){
		//如果两个输入框都有值，就返回真 
		if(!usernameEditText.getText().toString().equals("")&&
		   !userphoneEditText.getText().toString().equals("")){
			if(userphoneEditText.getText().toString().length()==11){
				return true;
			}else{
				toast("手机号必须为11位！");
				return false;
			}
			
		}else{
			if(usernameEditText.getText().toString().equals("")&&
			!userphoneEditText.getText().toString().equals("")){
				toast("入住人不能为空！");
				return false;
			}else if(!usernameEditText.getText().toString().equals("")&&
					   userphoneEditText.getText().toString().equals("")){
				toast("手机不能为空！");
				return false;
			}
			toast("入住人和手机不能为空！");
			return false;
		}
	}
	
	/**
	 * 预订成功的时候调用
	 */
	protected void onSuccess_reserve() {
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle("");
		dialog.setMessage("预订成功");
		dialog.setNegativeButton("返回主界面", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setClass(CateReserveActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
//		dialog.setPositiveButton("停留在订单页", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		});
		
		dialog.show();
	}
	
	/**
	 * 获取价格的字符串
	 * @return
	 */
	private Double setCatePrice(){
		double tmpprice = curCateBean.getAvg_price();
		
			catepriceTextView.setText("￥"+String.valueOf(tmpprice)+"（代金券）");

			return (double) tmpprice;
		
		
	}
	
	/**
	 * 计算总价钱，并且返回价钱的double类型
	 * @return
	 */
	Double countSumPrice(){
		int num = Integer.valueOf(catenum.getText().toString());
		double fee = num * curCateBean.getAvg_price();
		//sumfeeTextView.setText("￥"+String.valueOf(fee));
		return fee;
	}
	
	String getSumPriceString(){
		//单价
		int num = Integer.valueOf(catenum.getText().toString());
		
		String fixString = "("+num+"x￥"+(int)curCateBean.getAvg_price()+")";
		return "￥"+String.valueOf(countSumPrice())+fixString;
	}
	
}

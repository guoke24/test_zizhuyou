package guoke.adapter;

import guoke.model.CarItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.example.demo_zizhuyou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CarListAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Context context;
	private List<CarItem> carList;
	
	
	DisplayImageOptions options;		// DisplayImageOptions是用于设置图片显示的类

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
	
	public CarListAdapter(Context context,List<CarItem> cars){
		this.context = context;
		inflater = LayoutInflater.from(context);
		carList = cars;
		if(carList == null){
			carList = new ArrayList<CarItem>();
		}

		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ic_empty)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ic_error)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
		.displayer(new RoundedBitmapDisplayer(20))	// 设置成圆角图片
		.build();									// 创建配置过得DisplayImageOption对象		

		
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return carList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return carList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CarViewHoder carHoder = null;
		if(convertView == null){
			convertView = (View)inflater.inflate(R.layout.activity_car_list_item, null);
			carHoder = new CarViewHoder();
			carHoder.carview = (ImageView) convertView.findViewById(R.id.car_item_imageView);
			carHoder.carname = (TextView) convertView.findViewById(R.id.car_item_name);
			carHoder.cardetail = (TextView) convertView.findViewById(R.id.car_item_detail);
			carHoder.carprice = (TextView) convertView.findViewById(R.id.car_item_price);
			
			convertView.setTag(carHoder);
			
		}else{
			carHoder = (CarViewHoder) convertView.getTag();
		}
		
		CarItem carItem = carList.get(position);
		carHoder.carname.setText(carItem.getCname());
		carHoder.cardetail.setText(carItem.getCdetail());
		carHoder.carprice.setText("￥"+String.valueOf(carItem.getCprice())+"/天");
		
		imageLoader.displayImage(carList.get(position).getPicBmobFile().getUrl(), 
				carHoder.carview, options, animateFirstListener);

		
		return convertView;
	}


	
}

class CarViewHoder{
	ImageView carview;
	TextView carname;
	TextView cardetail;
	TextView carprice;
}



package guoke.adapter;

import guoke.bean.HotelBean;
import guoke.model.HotelItem;
import guoke.model.MRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

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

public class HotelListAdapter extends BaseAdapter{

	
	private LayoutInflater inflater;
	private Context context;
	private List<HotelItem> hotelItems ; 
	private List<HotelBean> hotelBeans;

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
	
	
	public HotelListAdapter(Context context,List<HotelItem> hs,List<HotelBean> hb){
		this.context = context;
		inflater = LayoutInflater.from(context);
		hotelItems = hs;
		hotelBeans = hb;
		if(hotelItems == null){
			hotelItems = new ArrayList<HotelItem>();
		}
		if(hotelBeans == null){
			hotelBeans = new ArrayList<HotelBean>();
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
		return hotelBeans.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return hotelBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//1.先获得convertView
		ViewHolder vHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.activity_hotel_list_item, null);
			vHolder = new ViewHolder();
			vHolder.hotelImage = (ImageView) convertView.findViewById(R.id.hotel_item_imageView);
			vHolder.hotelNameText = (TextView) convertView.findViewById(R.id.hotel_item_name);
			vHolder.hotelRegionText = (TextView) convertView.findViewById(R.id.hotel_item_area);
			vHolder.hotelBottomPrice = (TextView) convertView.findViewById(R.id.hotel_item_price);
			
			
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}
		//2.再给控件赋值
		HotelBean hb = hotelBeans.get(position);
		vHolder.hotelNameText.setText(hb.getName());
		vHolder.hotelRegionText.setText(hb.getIntro());
		//vHolder.hotelBottomPrice.setText("￥"+hotelItems.get(position).getBottomprice());
		vHolder.hotelBottomPrice.setText(hb.getAddress());
		//
		//imageLoader.displayImage(hotelItems.get(position).getPic_url(), vHolder.hotelImage, options, animateFirstListener);
		imageLoader.displayImage(hb.getLargePic(), vHolder.hotelImage, options, animateFirstListener);
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView hotelImage ;
		TextView hotelNameText;
		TextView hotelRegionText;
		TextView hotelBottomPrice;
	}

	

	
}

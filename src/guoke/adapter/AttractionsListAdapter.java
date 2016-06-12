package guoke.adapter;

import guoke.bean.SceneryBean;
import guoke.model.AttractionsItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AttractionsListAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Context context;
	private List<AttractionsItem> aList;
	private List<SceneryBean> sList;
	
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
	
	
	public AttractionsListAdapter(Context context,List<AttractionsItem> attractions,List<SceneryBean> sl){
		this.context = context;
		inflater = LayoutInflater.from(context);
		aList = attractions;
		if(aList == null){
			aList = new ArrayList<AttractionsItem>();
		}
		sList = sl;
		if(sList == null){
			sList = new ArrayList<SceneryBean>();
		}
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ic_empty)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ic_error)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
		.displayer(new RoundedBitmapDisplayer(10))	// 设置成圆角图片
		.build();									// 创建配置过得DisplayImageOption对象		

				
		
	}
	
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AViewHoder hoder = null;
		if(convertView == null){
			convertView = (View)inflater.inflate(R.layout.activity_attraction_list_item, null);
			hoder = new AViewHoder();
			hoder.imageView = (ImageView) convertView.findViewById(R.id.attraction_item_imageView);
			hoder.nameTextView = (TextView) convertView.findViewById(R.id.attraction_item_name);
			hoder.datailTextView = (TextView) convertView.findViewById(R.id.attraction_item_detail);
			hoder.describeTextView =  (TextView)convertView.findViewById(R.id.attraction_item_describe);
			
			convertView.setTag(hoder);
		}else{
			hoder = (AViewHoder) convertView.getTag();
		}
		//AttractionsItem aItem = aList.get(position);
		SceneryBean sBean = sList.get(position);
		hoder.nameTextView.setText(sBean.getTitle());
		hoder.datailTextView.setText(sBean.getAddress());
		hoder.describeTextView.setText("最低价：￥"+sBean.getPrice_min());
		
		imageLoader.displayImage(sBean.getImgurl(), 
				hoder.imageView, options, animateFirstListener);

		return convertView;
	}

	
	
}

class AViewHoder{
	ImageView imageView;
	TextView nameTextView;
	TextView datailTextView;
	TextView describeTextView;
}


package guoke.adapter;

import guoke.bean.CateBean;
import guoke.model.CarItem;
import guoke.model.CateItem;

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

public class CateListAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private Context context;
	private List<CateBean> cateList;

	DisplayImageOptions options;		// DisplayImageOptions是用于设置图片显示的类

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
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
	
	public CateListAdapter(Context context,List<CateBean> cates){
		this.context = context;
		inflater = LayoutInflater.from(context);
		cateList = cates;
		if(cateList == null){
			cateList = new ArrayList<CateBean>();
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
		return cateList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CateViewHoder cateHoder = null;
		if(convertView == null){
			convertView = (View)inflater.inflate(R.layout.activity_cate_list_item, null);
			cateHoder = new CateViewHoder();
			cateHoder.cateview = (ImageView) convertView.findViewById(R.id.cate_item_imageView);
			cateHoder.catename = (TextView) convertView.findViewById(R.id.cate_item_name);
			cateHoder.catedetail = (TextView) convertView.findViewById(R.id.cate_item_detail);
			cateHoder.cateLoaction = (TextView) convertView.findViewById(R.id.cate_item_location);
			
			convertView.setTag(cateHoder);
			
		}else{
			cateHoder = (CateViewHoder) convertView.getTag();
		}
		
		CateBean cateItem = cateList.get(position);
		cateHoder.catename.setText(cateItem.getName());
		cateHoder.catedetail.setText(cateItem.getTags().replace('|', ','));
		cateHoder.cateLoaction.setText(cateItem.getArea()+cateItem.getAddress());
		
		imageLoader.displayImage(cateItem.getPhotos(), 
				cateHoder.cateview, options, animateFirstListener);
		
		return convertView;
	}

}

class CateViewHoder{
	ImageView cateview;
	TextView catename;
	TextView catedetail;
	TextView cateLoaction;
}

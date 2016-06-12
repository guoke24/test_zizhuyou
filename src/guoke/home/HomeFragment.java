package guoke.home;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.example.demo_zizhuyou.BeginCarActivity;
import com.example.demo_zizhuyou.BeginCateActivity;
import com.example.demo_zizhuyou.BeginHotelActivity;
import com.example.demo_zizhuyou.BeginSceneryActivity;
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.drawable;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import guoke.fragment.BaseFragment;

public class HomeFragment extends BaseFragment implements android.view.View.OnClickListener{

	private static final String LOG_TAG = "HomeFragment";
	
	private ImageHandler handler = new ImageHandler(
			new WeakReference<HomeFragment>(this));

	private ViewPager viewPager;

	private List<View> dots; // 图片标题正文的那些点
	private List<String> names;
	
	private int oldPosition = 0;
	
	TextView nameOfTextView;
	
	View hotelClickLayout;
	View carClickLayout;
	View sceneryClickLayout;
	View cateClickLayout;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View homeLayout = inflater.inflate(R.layout.homefragment_home_layout,
				container, false);
		
		start(homeLayout);
		init(homeLayout);
		
		return homeLayout;
	}

	/**
	 * 开启了轮播效果
	 * @param v
	 */
	void start(View v){
		//
		dots = new ArrayList<View>();
		dots.add(v.findViewById(R.id.v_dot0));
		dots.add(v.findViewById(R.id.v_dot1));
		dots.add(v.findViewById(R.id.v_dot2));
		//
		names = new ArrayList<String>();
		names.add("象鼻山");
		names.add("靖江王城");
		names.add("七星公园");
		
		
		// 初始化iewPager的内容
		viewPager = (ViewPager) v.findViewById(R.id.id_vp);
		// LayoutInflater inflater = LayoutInflater.from(this);
		// 初始化轮播的三个页面
		ImageView view1 = new ImageView(getActivity());
		ImageView view2 = new ImageView(getActivity());
		ImageView view3 = new ImageView(getActivity());
		// ImageView view3 = (ImageView) inflater.inflate(R.layout.item, null);
		view1.setBackgroundResource(drawable.s1);
		view1.setScaleType(ScaleType.CENTER_CROP);
		view1.setTag("num 1");
		view1.setOnClickListener(this);
		view2.setBackgroundResource(drawable.s2);
		view2.setScaleType(ScaleType.CENTER_CROP);
		view3.setBackgroundResource(drawable.s3);
		view3.setScaleType(ScaleType.CENTER_CROP);
		// 添加到views列表
		ArrayList<ImageView> views = new ArrayList<ImageView>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		// 设置适配器
		viewPager.setAdapter(new ImageAdapter(views));
		// 设置监听
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			// 配合Adapter的currentItem字段进行设置。
			/**
			 * This method will be invoked when a new page becomes selected.
			 * 当有新的页面被选中的时候调用这个函数 position: Position index of the new selected
			 * page. 参数就是当前页面的参数
			 */
			@Override
			public void onPageSelected(int position) {
				// toast(position%3+"");
				// dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
				// dots.get(position).setBackgroundResource(R.drawable.dot_focused);

				handler.sendMessage(Message.obtain(handler,
						ImageHandler.MSG_PAGE_CHANGED, position, 0));
			}

			/**
			 * 页面正在滑动时被调用
			 */
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			// 覆写该方法实现轮播效果的暂停和恢复
			@Override
			public void onPageScrollStateChanged(int arg0) {
				switch (arg0) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					handler.sendEmptyMessageDelayed(
							ImageHandler.MSG_UPDATE_IMAGE,
							ImageHandler.MSG_DELAY);
					break;
				default:
					break;
				}
			}
		});
		// 默认在中间，使用户看不到边界
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
		// 开始轮播效果
		handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE,
				ImageHandler.MSG_DELAY);
	}
	
	void init(View v){
		nameOfTextView = (TextView) v.findViewById(R.id.name_of_view);
		hotelClickLayout = v.findViewById(R.id.hotel_click);
		hotelClickLayout.setOnClickListener(this);
		sceneryClickLayout = v.findViewById(R.id.ticket_click);
		sceneryClickLayout.setOnClickListener(this);
		cateClickLayout = v.findViewById(R.id.cate_click);
		cateClickLayout.setOnClickListener(this);
		carClickLayout = v.findViewById(R.id.car_click);
		carClickLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hotel_click:
			Intent intent = new Intent();
			intent.setClass(getActivity(), BeginHotelActivity.class);
			startActivity(intent);
			break;
		case R.id.ticket_click:
			Intent intent2 = new Intent();
			intent2.setClass(getActivity(), BeginSceneryActivity.class);
			startActivity(intent2);
			break;
		case R.id.cate_click:
			Intent intent3 = new Intent();
			intent3.setClass(getActivity(), BeginCateActivity.class);
			startActivity(intent3);
			break;
		case R.id.car_click:
			Intent intent4 = new Intent();
			intent4.setClass(getActivity(), BeginCarActivity.class);
			startActivity(intent4);
			break;
		default:
			break;
		}
		
	}

	private class ImageHandler extends Handler {

		/**
		 * 请求更新显示的View。
		 */
		protected static final int MSG_UPDATE_IMAGE = 1;
		/**
		 * 请求暂停轮播。
		 */
		protected static final int MSG_KEEP_SILENT = 2;
		/**
		 * 请求恢复轮播。
		 */
		protected static final int MSG_BREAK_SILENT = 3;
		/**
		 * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
		 * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
		 * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
		 */
		protected static final int MSG_PAGE_CHANGED = 4;

		// 轮播间隔时间
		protected static final long MSG_DELAY = 3000;

		// 使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
		private WeakReference<HomeFragment> weakReference;
		private int currentItem = 0;

		protected ImageHandler(WeakReference<HomeFragment> wk) {
			weakReference = wk;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.d(LOG_TAG, "receive message " + msg.what);
			HomeFragment activity = weakReference.get();
			if (activity == null) {
				// Activity已经回收，无需再处理UI了
				return;
			}
			
			switch (msg.what) {
				case MSG_UPDATE_IMAGE:
					currentItem++;
					// 完成一次轮播
					activity.viewPager.setCurrentItem(currentItem);
					// 检查消息队列,并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
					if (activity.handler.hasMessages(MSG_UPDATE_IMAGE)) {
						activity.handler.removeMessages(MSG_UPDATE_IMAGE);
					}
					// 准备下次播放
					activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE,
							MSG_DELAY);
					break;
				case MSG_KEEP_SILENT:
					// 只要不发送消息就暂停了
					break;
				case MSG_BREAK_SILENT:
					activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE,
							MSG_DELAY);
					break;
				case MSG_PAGE_CHANGED:
					// 记录当前的页号，避免播放的时候页面显示不正确。
					currentItem = msg.arg1;
					break;
				default:
				break;
			}
		}
	}
	
	/**
	 * 
	 * @author Administrator
	 * 
	 */
	class ImageAdapter extends PagerAdapter {

		private ArrayList<ImageView> viewlist;

		public ImageAdapter(ArrayList<ImageView> viewlist) {
			this.viewlist = viewlist;
		}

		@Override
		public int getCount() {
			// 设置成最大，使用户看不到边界
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// Warning：不要在这里调用removeView
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 对ViewPager页号求模取出View列表中要显示的项
			position %= viewlist.size();
			if (position < 0) {
				position = viewlist.size() + position;
			}
			// 在这里取得要显示的view，所以也可以在这里设置要显示的点
			ImageView view = viewlist.get(position);
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
			nameOfTextView.setText(names.get(position));
			
			// 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
			ViewParent vp = view.getParent();
			if (vp != null) {
				ViewGroup parent = (ViewGroup) vp;
				parent.removeView(view);
			}
			container.addView(view);
			// add listeners here if necessary
			return view;
		}
	}

	
}

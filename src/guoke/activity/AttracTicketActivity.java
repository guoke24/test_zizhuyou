package guoke.activity;

import httpUtil.HttpUtil;
import intetface.HttpCallbackListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import json_parse.utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.LoginActivity;
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.id;
import com.example.demo_zizhuyou.R.layout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import guoke.adapter.AttracTicketAdapter;
import guoke.adapter.AttractionsListAdapter;
import guoke.adapter.RoomListAdapter;
import guoke.bean.SceneryBean;
import guoke.bean.TicketBean;
import guoke.model.AttractionsItem;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MyUser;
import guoke.model.TicketItem;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AttracTicketActivity extends BaseActivity {

	private ImageView attracImageView;
	private TextView attracNameTextView;//名字
	private TextView attracLoactionTextView;//位置
	private TextView attracDetailTextView;//开放时间
	
	private ListView eListView;
	private AttracTicketAdapter adapter;
	//private List<TicketItem> curlist = new ArrayList<TicketItem>();
	private List<TicketBean> scurList = new ArrayList<TicketBean>();
	Intent source_intent;
	//private AttractionsItem attracSelect;//选中景点
	private SceneryBean attracSelect;
	private MDate beginMDate;
	private MDate finishMDate;
	
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
	
	
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				fixListViewHeight(eListView);
				adapter.notifyDataSetChanged();
				toTop();
				break;
			case 2:
				
				loadData(1);
				break;
			}
		};
	};	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attrac_ticket);
		getSourceIntent();
		init();
		setListener();
		//loadData(1);
		findTicket();
	}
	
	void getSourceIntent(){
		source_intent = getIntent();
		attracSelect = (SceneryBean) source_intent.getSerializableExtra(InfoCom.Extra_AttracSelect);
	}
	
	void init(){
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ic_empty)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ic_error)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
		.displayer(new RoundedBitmapDisplayer(5))	// 设置成圆角图片
		.build();									// 创建配置过得DisplayImageOption对象		

		
		attracImageView = (ImageView) findViewById(R.id.attrac_ticket_imageview);
		imageLoader.displayImage(attracSelect.getImgurl(), 
				attracImageView, options, animateFirstListener);

		
		attracNameTextView = (TextView) findViewById(R.id.attrac_ticket_name);
		attracNameTextView.setText(attracSelect.getTitle());
		
		attracLoactionTextView = (TextView) findViewById(R.id.attrac_ticket_loaction);
		attracLoactionTextView.setText("位置："+attracSelect.getAddress());
		
		attracDetailTextView=(TextView)findViewById(R.id.attrac_ticket_detail);
		//attracDetailTextView.setText(attracSelect.getDetail());
		
		adapter = new AttracTicketAdapter(this, scurList);
		eListView = (ListView) findViewById(R.id.attrac_ticket_listview);
		eListView.setAdapter(adapter);
	}
	
	void setListener(){
		eListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				TicketItem ticketItem = curlist.get(position);
				TicketBean ticketBean = scurList.get(position);
				source_intent.putExtra(InfoCom.Extra_TicketSelect, ticketBean);
				MyUser myUser = BmobUser.getCurrentUser(AttracTicketActivity.this, MyUser.class);
				if(myUser != null){
					source_intent.setClass(AttracTicketActivity.this, AttracTicketReserveActivity.class);
					startActivity(source_intent);
				}else{
					toast("未登录，请先登录");
					source_intent.putExtra(InfoCom.fromWhere, InfoCom.fromTicketList);
					source_intent.setClass(AttracTicketActivity.this, LoginActivity.class);
					startActivity(source_intent);
				}
				
				
			}
		});
	}

	/**
	 * 加载数据，在发生上拉或下拉的动作后立马调用
	 */
	public void loadData(final int type) {

		new Thread() {
			@Override
			public void run() {
				//List<HotelItem> _List = null;
				switch (type) {
				case 0://下拉刷新

					break;
				case 1://上拉刷加载
					//findObj();	
					break;
				}//switch end

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				//do something here
				//休眠一秒，在更新列表
				//adapter.notifyDataSetChanged();
				mHandler.sendEmptyMessage(1);
				
			}
		}.start();
	}
	
	
	private void findTicket(){
		showProgressDialog();
		String murlString = getTicketUrl();
		HttpUtil.sendRequestWithHttpConnection(murlString, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				Log.d("json", "下载完成");
				// TODO Auto-generated method stub
				closeProgressDialog();
				// TODO Auto-generated method stub
				//mcurpage++;
				JSONObject object;
				try {
					object = new JSONObject(response);
					List<TicketBean> templist = JsonParser.parseJsonForTicketBean(object.getString("result"));

					scurList.addAll(templist);
									
					mHandler.sendEmptyMessage(2);

					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//toast("json解析失败");
					Log.d("json", "json解析失败");
				}				
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private String getTicketUrl(){
		Map params = new HashMap();//请求参数
        params.put("key",InfoCom.APPKEY);//
        params.put("sid",attracSelect.getSid());//
//        params.put("cid",cityid);//
//        params.put("page",page);//当前页数，默认1
		String url = InfoCom.ticket_url+"?"+urlencode(params);
		
		return url;
	}
	
	/**
	 * 
	 * @param where 从哪一行开始查
	 * @param how 查多少行
	 */
	private void findObj(){
		BmobQuery<TicketItem> query = new BmobQuery<TicketItem>();
		//query.addWhereEqualTo("attrac_id", attracSelect.getObjectId());
		query.findObjects(this, new FindListener<TicketItem>() {
			
			@Override
			public void onSuccess(List<TicketItem> arg0) {
				closeProgressDialog();
				toast("查询成功，查到"+arg0.size()+"个门票");
				
				//每一次查到数据，都追加到列表
				
				for(TicketItem r:arg0){
					//curlist.add(r);
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				closeProgressDialog();
				toast("查询失败："+arg1);
			}
		});

	}
	
	/**
	 * 修复listview在scrollview的高度
	 * @param listView
	 */
	 public void fixListViewHeight(ListView listView) {   
	        // 如果没有设置数据适配器，则ListView没有子项，返回。  
		 AttracTicketAdapter listAdapter = (AttracTicketAdapter) listView.getAdapter();  
	        int totalHeight = 0;   
	        if (listAdapter == null) {   
	            return;   
	        }   
	        for (int index = 0, len = listAdapter.getCount(); index < len; index++) {     
	            View listViewItem = listAdapter.getView(index , null, listView);  
	            // 计算子项View 的宽高   
	            listViewItem.measure(0, 0);    
	            // 计算所有子项的高度和
	            totalHeight += listViewItem.getMeasuredHeight();    
	        }   
	   
	        ViewGroup.LayoutParams params = listView.getLayoutParams();   
	        // listView.getDividerHeight()获取子项间分隔符的高度   
	        // params.height设置ListView完全显示需要的高度    
	        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
	        listView.setLayoutParams(params);   
	    }
	
	 private void toTop(){
			attracImageView = (ImageView) findViewById(R.id.attrac_ticket_imageview);
			//设置焦点，让scrollView显示在顶部
			attracImageView.setFocusable(true);//使能控件获得焦点,键盘输入
			attracImageView.setFocusableInTouchMode(true);//使能控件获得焦点,触摸
			attracImageView.requestFocus();//立刻获得焦点		
		}
}

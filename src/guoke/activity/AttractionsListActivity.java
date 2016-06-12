package guoke.activity;

import intetface.HttpCallbackListener;

import java.util.ArrayList;
import java.util.HashMap;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import guoke.adapter.AttractionsListAdapter;
import guoke.adapter.CarListAdapter;
import guoke.bean.HotelBean;
import guoke.bean.SceneryBean;
import guoke.custom.listview.SingleLayoutListView;
import guoke.custom.listview.SingleLayoutListView.OnLoadMoreListener;
import guoke.custom.listview.SingleLayoutListView.OnRefreshListener;
import guoke.imageloader.AbsListViewBaseActivity;
import guoke.model.AttractionsItem;
import guoke.model.CarItem;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;
import guoke.model.MyUser;
import httpUtil.HttpUtil;
import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class AttractionsListActivity extends AbsListViewBaseActivity{

	private TextView key_or_cityTextView;
	private TextView begin_daTextView;
	private TextView finish_dateTextView;
	
	//private SingleLayoutListView mListView;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	
	 //每次检索的行数
	private final int mqueryCount = 7;
	//要跳过的，等价于已经加载的行数
	private int mskipCount = 0;
	private int mcurpage = 1;
	
	private AttractionsListAdapter adapter;
	private List<AttractionsItem> queryList = new ArrayList<AttractionsItem>();
	private List<AttractionsItem> curList = new ArrayList<AttractionsItem>();
	private List<SceneryBean> scurList = new ArrayList<SceneryBean>();
	private List<SceneryBean> templist = new ArrayList<SceneryBean>();
	/*
	 * 这四个值是要传递的，都放在一个intent就可以多次传递了
	 */
	private MRegion dest_Region;
	private String key_word;
	private MDate begin_Date;
	private MDate finish_Date;
	
	private Intent source_intent;
	
	private TextView dest_cityTextView;
	
	/**
	 * 当接到 下拉刷新完成 或 下拉加载完成 的消息后，
	 * 更新adapter的数据源：curList，并通知adapter和sListView
	 */
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				if (adapter != null) {
					//mskipCount = 5;
					//下拉刷新后是把刷新加载的列表 【替换】 原来的列表
					//curList.clear();
					//curList.addAll((ArrayList<AttractionsItem>) msg.obj);
					adapter.notifyDataSetChanged();
				}
				mListView.onRefreshComplete(); // 下拉刷新完成
				break;
			case LOAD_DATA_FINISH:
				if (adapter != null) {
					//加载更多完成后事把 加载的数据【追加】到原来的列表
					//curList.addAll((ArrayList<CarItem>) msg.obj);
					if(templist.size()<10){
						mListView.setCanLoadMore(false);
					}else{
						mListView.setCanLoadMore(true);
					}
					adapter.notifyDataSetChanged();
				}
				mListView.onLoadMoreComplete(); // 加载更多完成
				break;
			}
		};
	};	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attraction_list);
		getSourceIntent();
		init();
		setListener();
		loadData(1);
	}

	void getSourceIntent(){
		source_intent = getIntent();
		dest_Region = (MRegion) source_intent.getSerializableExtra(InfoCom.Extra_PlayCitySelect);
	}
	
	void init(){
		dest_cityTextView = (TextView) findViewById(R.id.attractions_list_city);
		dest_cityTextView.setText(dest_Region.getRname());
		
		mListView = (SingleLayoutListView) findViewById(R.id.attractions_list_view);
		curList.clear();
		adapter = new AttractionsListAdapter(this,curList,scurList);
		mListView.setAdapter(adapter);
	}
	
	void setListener(){
		//下拉刷新的时候 会从回调这个函数
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadData(0);
				//toast("下拉刷新啦");
			}
		});	
		//上拉加载的时候，会从回调这个函数开始
		mListView.setOnLoadListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				// TODO 加载更多
				loadData(1);
				//toast("上拉加载啦");
			}
		});	
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				AttractionsItem aItem = curList.get(position-1);
				SceneryBean sceneryBean = scurList.get(position-1);
				source_intent.putExtra(InfoCom.Extra_AttracSelect, sceneryBean);
				source_intent.setClass(AttractionsListActivity.this, AttracTicketActivity.class);
				startActivity(source_intent);
				
				
			}
		});
		mListView.setCanLoadMore(false);
		mListView.setCanRefresh(false);
		mListView.setAutoLoadMore(false);
		mListView.setMoveToFirstItemAfterRefresh(false);
		mListView.setDoRefreshOnUIChanged(false);
	}

	/**
	 * 加载数据，在发生上拉或下拉的动作后立马调用
	 */
	public void loadData(final int type) {
		showProgressDialog();

		new Thread() {
			@Override
			public void run() {
				//List<HotelItem> _List = null;
				switch (type) {
				case 0://下拉刷新
					//下拉刷新，表示从头查起
					//mskipCount=0;
					mcurpage = 1;
					//findObj(mskipCount, mqueryCount);
					break;
				case 1://上拉刷加载
					//findObj(mskipCount, mqueryCount);	
					findScenery();
					break;
				}//switch end

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (type == 0) { // 下拉刷新
					// Collections.reverse(mList); //逆序
					Message _Msg = mHandler.obtainMessage(REFRESH_DATA_FINISH,
							queryList);
					mHandler.sendMessage(_Msg);
				} else if (type == 1) {//上拉加载
//					Message _Msg = mHandler.obtainMessage(LOAD_DATA_FINISH,
//							queryList);
//					mHandler.sendMessage(_Msg);
				}
			}
		}.start();
	}
	
	
	private void findScenery(){
		String murlString = getScenerylUrl(dest_Region.getCid(), mcurpage);
		HttpUtil.sendRequestWithHttpConnection(murlString, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				Log.d("json", "下载完成");
				// TODO Auto-generated method stub
				closeProgressDialog();
				// TODO Auto-generated method stub
				mcurpage++;
				JSONObject object;
				try {
					object = new JSONObject(response);
					templist.clear();
					templist = JsonParser.parseJsonForSceneryBean(object.getString("result"));

					scurList.addAll(templist);
					
					Message _Msg = mHandler.obtainMessage(LOAD_DATA_FINISH,queryList);
					mHandler.sendMessage(_Msg);
					
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
	
	/**
	 * 得到 url
	 * @param cityid
	 * @param page
	 * @return
	 */
	private String getScenerylUrl(Integer cityid,int page){
		Map params = new HashMap();//请求参数
        params.put("key",InfoCom.APPKEY);//
        params.put("pid","");//
        params.put("cid",cityid);//
        params.put("page",page);//当前页数，默认1
		String url = InfoCom.scenery_url+"?"+urlencode(params);
		
		return url;
	}
	
	/**
	 * 
	 * @param where 从哪一行开始查
	 * @param how 查多少行
	 */
	private void findObj(int where,int how){
		BmobQuery<AttractionsItem> query = new BmobQuery<AttractionsItem>();
		
		query.setSkip(where);
		query.setLimit(how);
		query.findObjects(this, new FindListener<AttractionsItem>() {
			
			@Override
			public void onSuccess(List<AttractionsItem> arg0) {
				closeProgressDialog();
				toast("查询成功，查到"+arg0.size()+"个景点");
				//下次再查，从哪儿查起？
				mskipCount+=arg0.size();
				//如果没查够那么多行，是不是查完了？
				if(arg0.size()<mqueryCount){
					mListView.setCanLoadMore(false);
				}else{
					mListView.setCanLoadMore(true);
				}
				//最后，把查到的装到缓存列表
				//queryList.clear();
//				for(AttractionsItem r:arg0){
//					curList.add(r);
//				}
				curList.addAll(arg0);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				closeProgressDialog();
				toast("查询失败："+arg1);
			}
		});

	}
	
}

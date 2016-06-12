package guoke.activity;

import guoke.adapter.HotelListAdapter;
import guoke.bean.HotelBean;
import guoke.bean.SceneryBean;
import guoke.custom.listview.SingleLayoutListView;
import guoke.custom.listview.SingleLayoutListView.OnLoadMoreListener;
import guoke.custom.listview.SingleLayoutListView.OnRefreshListener;
import guoke.imageloader.AbsListViewBaseActivity;
import guoke.model.CarItem;
import guoke.model.HotelItem;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;

import httpUtil.HttpUtil;
import intetface.HttpCallbackListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import json_parse.utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HotelListActivity extends AbsListViewBaseActivity {

	// private SingleLayoutListView mListView ;
	private MRegion curRegion;
	private MDate checkIn_Date;
	private MDate checkOut_Date;
	private TextView checkIn_CityTextView;
	private TextView checkIn_DateTextView;
	private TextView checkOut_DateTextView;

	private List<HotelItem> hotelList = new ArrayList<HotelItem>();
	private HotelListAdapter hotelListAdapter;

	// private String url_For_Phone =
	// "http://192.168.191.1:8080/Http_Server/LoginAction";
	// private String url_For_Monitor =
	// "http://10.0.2.2:8080/Http_Server/LoginAction";

	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	// 每次检索的行数
	private final int mqueryCount = 7;
	// 要跳过的，等价于已经加载的行数
	private int mskipCount = 0;
	private int mcurpage = 1;

	private List<HotelItem> queryList = new ArrayList<HotelItem>();
	private List<HotelItem> curList = new ArrayList<HotelItem>();
	private List<HotelBean> curbList = new ArrayList<HotelBean>();
	private List<HotelBean> templist = new ArrayList<HotelBean>();

	/**
	 * 当接到 下拉刷新完成 或 下拉加载完成 的消息后， 更新adapter的数据源：curList，并通知adapter和sListView
	 */
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				if (hotelListAdapter != null) {
					// mskipCount = 5;
					// 下拉刷新后是把刷新加载的列表 【替换】 原来的列表
					// curList.clear();
					// curList.addAll((ArrayList<CarItem>) msg.obj);
					hotelListAdapter.notifyDataSetChanged();
				}
				mListView.onRefreshComplete(); // 下拉刷新完成
				break;
			case LOAD_DATA_FINISH:
				if (hotelListAdapter != null) {
					// 加载更多完成后事把 加载的数据【追加】到原来的列表
					// curList.addAll((ArrayList<CarItem>) msg.obj);
					if (templist.size() < 20) {
						mListView.setCanLoadMore(false);
					} else {
						mListView.setCanLoadMore(true);
					}
					hotelListAdapter.notifyDataSetChanged();
				}
				mListView.onLoadMoreComplete(); // 加载更多完成
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hotel_list);

		init();
		setListener();
		loadData(1);
	}

	/**
	 * 初始化各个组�?
	 */
	private void init() {
		// get the select region and date
		curRegion = (MRegion) getIntent().getSerializableExtra(
				InfoCom.Extra_RegionSelect);
		checkIn_Date = (MDate) getIntent().getSerializableExtra(
				InfoCom.Extra_CheckInDate);
		checkOut_Date = (MDate) getIntent().getSerializableExtra(
				InfoCom.Extra_CheckOutDate);
		// getHotelList(curRegion);
		// init the textview about the city and date
		checkIn_CityTextView = (TextView) findViewById(R.id.hotel_list_city_textview);
		checkIn_CityTextView.setText(curRegion.getRname());
		checkIn_DateTextView = (TextView) findViewById(R.id.hotel_list_arrive_btn);
		checkIn_DateTextView.setText("入住" + getMonth_Day(checkIn_Date));
		checkOut_DateTextView = (TextView) findViewById(R.id.hotel_list_left_btn);
		checkOut_DateTextView.setText("离开" + getMonth_Day(checkOut_Date));
		// init the listview and adapter
		mListView = (SingleLayoutListView) findViewById(R.id.list_view);
		hotelListAdapter = new HotelListAdapter(this, curList, curbList);
		mListView.setAdapter(hotelListAdapter);

	}

	/**
	 * 设置各个事件的监听函�?
	 */
	private void setListener() {
		// 下拉刷新的时候 会从回调这个函数
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadData(0);
				// toast("下拉刷新啦");
			}
		});
		// 上拉加载的时候，会从回调这个函数开始
		mListView.setOnLoadListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				// TODO 加载更多
				loadData(1);
				// toast("上拉加载啦");
			}
		});
		// 设置item的点击事件监听函�?
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent();
				HotelBean hb = curbList.get(position - 1);

				// HotelItem ht = curList.get(position-1);
				intent.putExtra(InfoCom.Extra_HotelSelect, hb);
				intent.putExtra(InfoCom.Extra_CheckInDate, checkIn_Date);
				intent.putExtra(InfoCom.Extra_CheckOutDate, checkOut_Date);
				intent.setClass(HotelListActivity.this,
						HotelDetailActivity.class);
				startActivity(intent);
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
				// List<HotelItem> _List = null;
				switch (type) {
				case 0:// 下拉刷新
						// 下拉刷新，表示从头查起
					mskipCount = 0;
					mcurpage = 1;
					// findOrder(mskipCount, mqueryCount);
					// getHotelList(curRegion, mskipCount, mqueryCount);
					findHotelList(mcurpage);
					break;
				case 1:// 上拉刷加载
						// findOrder(mskipCount, mqueryCount);
						// getHotelList(curRegion, mskipCount, mqueryCount);
					findHotelList(mcurpage);
					break;
				}// switch end

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (type == 0) { // 下拉刷新
					// Collections.reverse(mList); //逆序
					// Message _Msg =
					// mHandler.obtainMessage(REFRESH_DATA_FINISH,
					// queryList);
					// mHandler.sendMessage(_Msg);
				} else if (type == 1) {// 上拉加载
				// Message _Msg = mHandler.obtainMessage(LOAD_DATA_FINISH,
				// queryList);
				// mHandler.sendMessage(_Msg);
				}
			}
		}.start();
	}

	/**
	 * 要把数据加载到curbList中
	 */
	private void findHotelList(int page) {
		String murlString = getHotelUrl(curRegion.getCid(), mcurpage);
		HttpUtil.sendRequestWithHttpConnection(murlString,
				new HttpCallbackListener() {

					@Override
					public void onFinish(String response) {
						closeProgressDialog();
						// TODO Auto-generated method stub
						mcurpage++;
						JSONObject object;
						try {
							object = new JSONObject(response);
							templist.clear();
							templist = JsonParser.parseJsonForHotelBean(object
									.getString("result"));

							curbList.addAll(templist);

							Message _Msg = mHandler.obtainMessage(
									LOAD_DATA_FINISH, queryList);
							mHandler.sendMessage(_Msg);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							// toast("json解析失败");
							Log.d("json", "json解析失败");
						}
					}

					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						closeProgressDialog();
						// toast("查询失败：");
						Log.d("download", "下载解析失败");
					}
				});

	}

	/**
	 * 得到 酒店列表的url
	 * 
	 * @param cityid
	 * @param page
	 * @return
	 */
	private String getHotelUrl(Integer cityid, int page) {
		Map params = new HashMap();// 请求参数
		params.put("key", InfoCom.APPKEY);//
		params.put("cityId", cityid);//
		params.put("page", page);// 当前页数，默认1
		String url = InfoCom.hotelList_url + "?" + urlencode(params);

		return url;
	}

	/**
	 * @param region
	 *            所要查询的地区
	 * @param where
	 *            从哪一行开始查
	 * @param how
	 *            查多少行
	 */
	private void findTheObject(MRegion region, int where, int how) {
		BmobQuery<HotelItem> query = new BmobQuery<HotelItem>();

		query.setSkip(where);
		query.setLimit(how);
		query.findObjects(this, new FindListener<HotelItem>() {

			@Override
			public void onSuccess(List<HotelItem> arg0) {
				closeProgressDialog();
				// toast("查询成功，查到"+arg0.size()+"个酒店");
				// 下次再查，从哪儿查起？
				mskipCount += arg0.size();
				// 如果没查够那么多行，是不是查完了？
				if (arg0.size() < mqueryCount) {
					mListView.setCanLoadMore(false);
				} else {
					mListView.setCanLoadMore(true);
				}
				// 最后，把查到的装到缓存列表
				// queryList.clear();
				for (HotelItem r : arg0) {
					curList.add(r);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				closeProgressDialog();
				toast("查询失败：" + arg1);
			}
		});

	}

	/**
	 * 根据region从网上查询出对应的酒店列表
	 * 
	 * @param region
	 */
	private void getHotelList(MRegion region, int where, int how) {
		showProgressDialog();
		if (region.getStyle() == 2) {// 按城市来查询
			// findHotelById(region);
			findTheObject(region, where, how);
		} else if (region.getStyle() == 3) {// 按地区来查询

		}
	}

	/**
	 * 根据城市id找出酒店
	 * 
	 * @param region
	 */
	// private void findHotelById(MRegion region){
	// BmobQuery<HotelItem> query = new BmobQuery<HotelItem>();
	// //
	// //query.addWhereEqualTo("city_id", region.getRid());
	// //
	// query.findObjects(this, new FindListener<HotelItem>() {
	//
	// @Override
	// public void onSuccess(List<HotelItem> arg0) {
	// toast("查询成功：共"+arg0.size()+"条数据。");
	// closeProgressDialog();
	// for(HotelItem hotel : arg0){
	// hotelList.add(hotel);
	// }
	//
	// hotelListAdapter.notifyDataSetChanged();
	// }
	//
	// @Override
	// public void onError(int arg0, String arg1) {
	// // TODO Auto-generated method stub
	// toast("查询失败："+arg1);
	// }
	// });
	// }
	//

}

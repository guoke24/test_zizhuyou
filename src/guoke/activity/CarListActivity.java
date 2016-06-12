package guoke.activity;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.LoginActivity;
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.id;
import com.example.demo_zizhuyou.R.layout;

import guoke.adapter.CarListAdapter;
import guoke.custom.listview.SingleLayoutListView;
import guoke.custom.listview.SingleLayoutListView.OnLoadMoreListener;
import guoke.custom.listview.SingleLayoutListView.OnRefreshListener;
import guoke.imageloader.AbsListViewBaseActivity;
import guoke.model.CarItem;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;
import guoke.model.MyUser;
import guoke.model.RoomOrder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CarListActivity extends AbsListViewBaseActivity {

	//private SingleLayoutListView mListView ;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;

	 //每次检索的行数
	private final int mqueryCount = 7;
	//要跳过的，等价于已经加载的行数
	private int mskipCount = 0;
	
	private CarListAdapter carListAdapter ;
	private List<CarItem> queryList = new ArrayList<CarItem>();
	private List<CarItem> curList = new ArrayList<CarItem>();
	//
	/*
	 * 这四个值是要传递的，都放在一个intent就可以多次传递了
	 */
	private MRegion getCar_Region;
	private MRegion returnCar_Region;
	private MDate getCar_Date;
	private MDate returnCar_Date;
	
	private Intent source_intent;
	
	/**
	 * 当接到 下拉刷新完成 或 下拉加载完成 的消息后，
	 * 更新adapter的数据源：curList，并通知adapter和sListView
	 */
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				if (carListAdapter != null) {
					//mskipCount = 5;
					//下拉刷新后是把刷新加载的列表 【替换】 原来的列表
					curList.clear();
					curList.addAll((ArrayList<CarItem>) msg.obj);
					carListAdapter.notifyDataSetChanged();
				}
				mListView.onRefreshComplete(); // 下拉刷新完成
				break;
			case LOAD_DATA_FINISH:
				if (carListAdapter != null) {
					//加载更多完成后事把 加载的数据【追加】到原来的列表
					//curList.addAll((ArrayList<CarItem>) msg.obj);
					carListAdapter.notifyDataSetChanged();
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
		setContentView(R.layout.activity_car_list);
		init();
		setListener();
		getSourceIntent();
		loadData(1);
	}

	void getSourceIntent(){
		source_intent = getIntent();
	}
	
	void init(){
		mListView = (SingleLayoutListView) findViewById(R.id.car_list_view);
		curList.clear();
		carListAdapter = new CarListAdapter(this,curList);
		mListView.setAdapter(carListAdapter);

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
		//点击了item时会调用该函数
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//toast("position:"+position);
				CarItem carItem = curList.get(position-1);
				source_intent.putExtra(InfoCom.Extra_CarSelect, carItem);
				MyUser myUser = BmobUser.getCurrentUser(CarListActivity.this, MyUser.class);
				if(myUser != null){
					//已经登录，就直接跳转到服务选择页面
					source_intent.setClass(CarListActivity.this, CarReserveActivity.class);
					startActivity(source_intent);
				}else{
					//未登录就先登录
					toast("未登录，请先登录");
					source_intent.putExtra(InfoCom.fromWhere, InfoCom.fromCarList);
					source_intent.setClass(CarListActivity.this, LoginActivity.class);
					startActivity(source_intent);
				}
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
					mskipCount=0;
					findOrder(mskipCount, mqueryCount);
					break;
				case 1://上拉刷加载
					findOrder(mskipCount, mqueryCount);	
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
					Message _Msg = mHandler.obtainMessage(LOAD_DATA_FINISH,
							queryList);
					mHandler.sendMessage(_Msg);
				}
			}
		}.start();
	}
	/**
	 * 
	 * @param where 从哪一行开始查
	 * @param how 查多少行
	 */
	private void findOrder(int where,int how){
		BmobQuery<CarItem> query = new BmobQuery<CarItem>();
		
		query.setSkip(where);
		query.setLimit(how);
		query.findObjects(this, new FindListener<CarItem>() {
			
			@Override
			public void onSuccess(List<CarItem> arg0) {
				closeProgressDialog();
				//toast("查询成功，查到"+arg0.size()+"个车型");
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
				for(CarItem r:arg0){
					curList.add(r);
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				closeProgressDialog();
				toast("查询失败："+arg1);
			}
		});

	}
}

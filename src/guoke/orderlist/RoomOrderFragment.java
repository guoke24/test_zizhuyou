package guoke.orderlist;

import java.util.ArrayList;
import java.util.List;

import guoke.activity.RoomOrderDetailActivity;
import guoke.custom.listview.SingleLayoutListView;
import guoke.custom.listview.SingleLayoutListView.OnLoadMoreListener;
import guoke.custom.listview.SingleLayoutListView.OnRefreshListener;
import guoke.model.InfoCom;
import guoke.model.MyUser;
import guoke.model.RoomOrder;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.example.demo_zizhuyou.R;



import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/*
 * 输入的信息：登陆的用户
 * 输出的信息：含有所有酒店订单的一个列表
 */
public class RoomOrderFragment extends Fragment{

	private SingleLayoutListView sListView;
	
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	/**
	 * 每次检索的行数
	 */
	private final int mqueryCount = 7;
	/**
	 * 要跳过的，等价于已经加载的行数
	 */
	private int mskipCount = 0;
	
	private RoomOrderListAdapter rmodAdapter;
	private List<RoomOrder> queryList = new ArrayList<RoomOrder>();
	private List<RoomOrder> curList = new ArrayList<RoomOrder>();
	
	/**
	 * 当接到 下拉刷新完成 或 下拉加载完成 的消息后，
	 * 更新adapter的数据源：curList，并通知adapter和sListView
	 */
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				if (rmodAdapter != null) {
					//mskipCount = 5;
					//下拉刷新后是把刷新加载的列表 【替换】 原来的列表
					//curList.clear();
					//curList.addAll((ArrayList<RoomOrder>) msg.obj);
					rmodAdapter.notifyDataSetChanged();
				}
				sListView.onRefreshComplete(); // 下拉刷新完成
				break;
			case LOAD_DATA_FINISH:
				if (rmodAdapter != null) {
					//加载更多完成后事把 加载的数据【追加】到原来的列表
					curList.addAll((ArrayList<RoomOrder>) msg.obj);
					rmodAdapter.notifyDataSetChanged();
				}
				sListView.onLoadMoreComplete(); // 加载更多完成
				break;
			}
		};
	};	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_allroomlist_layout, null);
		init(view);
		loadData(1);
		return view;
	}
	
	/**
	 * 初始化了组件
	 * @param view
	 */
	void init(View view){
		rmodAdapter = new RoomOrderListAdapter(getActivity(), curList);
		sListView = (SingleLayoutListView)view.findViewById(R.id.roomAllOrder_elistview);
		sListView.setAdapter(rmodAdapter);
		
		//下拉刷新的时候 会从回调这个函数
		sListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadData(0);
				//toast("下拉刷新啦");
			}
		});	
		//上拉加载的时候，会从回调这个函数开始
		sListView.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO 加载更多
				loadData(1);
				//toast("上拉加载啦");
			}
		});		
		//点击了item时会调用该函数
		sListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				RoomOrder roomOrder =  curList.get(position-1);
				 intent.putExtra(InfoCom.Extra_RoomOrderSelect, roomOrder);
				intent.setClass(getActivity(), RoomOrderDetailActivity.class);
				startActivity(intent);
				
			}
		});		
		
		sListView.setCanLoadMore(false);
		sListView.setCanRefresh(false);
		sListView.setAutoLoadMore(false);
		sListView.setMoveToFirstItemAfterRefresh(false);
		sListView.setDoRefreshOnUIChanged(false);
				
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
		BmobQuery<RoomOrder> query = new BmobQuery<RoomOrder>();
		MyUser myUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
		if(myUser!=null){
			query.addWhereEqualTo("user_id", myUser.getObjectId());
		}
		query.setSkip(where);
		query.setLimit(how);
		query.order("createdAt");
		query.findObjects(getActivity(), new FindListener<RoomOrder>() {
			
			@Override
			public void onSuccess(List<RoomOrder> arg0) {
				closeProgressDialog();
				toast("查询成功，查到"+arg0.size()+"个订单");
				//下次再查，从哪儿查起？
				mskipCount+=arg0.size();
				//如果没查够那么多行，是不是查完了？
				if(arg0.size()<mqueryCount){
					sListView.setCanLoadMore(false);
				}else{
					sListView.setCanLoadMore(true);
				}
				//最后，把查到的装到缓存列表
				queryList.clear();
				for(RoomOrder r:arg0){
					queryList.add(r);
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
	 * toast一些数据到屏幕
	 * @param s
	 */
	private void toast(String s){
		Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
	}
	
	private ProgressDialog progressDialog;
	/**
	 * 显示进度对话框
	 */
	protected void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	/**
	 * 关闭进度对话框
	 */
	protected void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
}

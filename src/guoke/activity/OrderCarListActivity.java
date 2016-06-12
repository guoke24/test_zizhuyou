package guoke.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.id;
import com.example.demo_zizhuyou.R.layout;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import guoke.custom.listview.*;
import guoke.custom.listview.SingleLayoutListView.OnLoadMoreListener;
import guoke.model.CarOrder;
import guoke.model.InfoCom;
import guoke.model.MyUser;


public class OrderCarListActivity extends BaseActivity implements OnItemClickListener{

	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;
	/**
	 * 每次检索的行数
	 */
	private final int mqueryCount = 10;
	/**
	 * 要跳过的，等价于已经加载的行数
	 */
	private int mskipCount = 0;
	
	private TextView headContentTextView;
	
	private SingleLayoutListView sListView;
	
	private Intent source_intent;
	private CateOrderListAdapter adapter;
	private List<CarOrder> carOrderList = new ArrayList<CarOrder>();
	
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
					//curList.addAll((ArrayList<RoomOrder>) msg.obj);
					adapter.notifyDataSetChanged();
				}
				sListView.onRefreshComplete(); // 下拉刷新完成
				break;
			case LOAD_DATA_FINISH:
				if (adapter != null) {
					//加载更多完成后事把 加载的数据【追加】到原来的列表
					//curList.addAll((ArrayList<RoomOrder>) msg.obj);
					adapter.notifyDataSetChanged();
				}
				sListView.onLoadMoreComplete(); // 加载更多完成
				break;
			}
		};
	};	
	
	
	void getSource_intent(){
		source_intent = getIntent();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cate_order_list);
		
		getSource_intent();
		init();
		setListener();
		loadData(1);
		
	}

	void init(){
		headContentTextView = (TextView) findViewById(R.id.order_type);
		headContentTextView.setText("租车订单");
		
		sListView = (SingleLayoutListView) findViewById(R.id.Order_cate_elistview);
		adapter = new CateOrderListAdapter(OrderCarListActivity.this,carOrderList);
		sListView.setAdapter(adapter);
		
	}
	
	void setListener(){
		//上拉加载的时候，会从回调这个函数开始
		sListView.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO 加载更多
				loadData(1);
				//toast("上拉加载啦");
			}
		});	
		sListView.setOnItemClickListener(this);
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
//					mskipCount=0;
//					findOrder(mskipCount, mqueryCount);
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
							null);
					mHandler.sendMessage(_Msg);
				} else if (type == 1) {//上拉加载
					Message _Msg = mHandler.obtainMessage(LOAD_DATA_FINISH,
							null);
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
		BmobQuery<CarOrder> query = new BmobQuery<CarOrder>();

		MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
		if(myUser!=null){
			query.addWhereEqualTo("user_id", myUser.getObjectId());
		}
		
		query.setSkip(where);
		query.setLimit(how);
		query.order("createdAt");
		query.findObjects(OrderCarListActivity.this, new FindListener<CarOrder>() {
			
			@Override
			public void onSuccess(List<CarOrder> arg0) {
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
				carOrderList.addAll(arg0);
//				for(RoomOrder r:arg0){
//					queryList.add(r);
//				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				closeProgressDialog();
				toast("查询失败："+arg1);
			}
		});

	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CarOrder carOrder = carOrderList.get(position-1);
		Intent intent = new Intent();
		intent.putExtra(InfoCom.Extra_CarOrderSelect, carOrder);
		intent.setClass(OrderCarListActivity.this, CarOrderDetailActivity.class);
		startActivity(intent);
		
	}

	
	class CateOrderListAdapter extends BaseAdapter{

		Context context;
		private List<CarOrder> ctoList;
		LayoutInflater inflater;
		
		public CateOrderListAdapter(Context context,List<CarOrder> cto){
			this.context = context;
			ctoList = cto;
			this.inflater = LayoutInflater.from(context);
			
			if(ctoList == null){
				ctoList = new ArrayList<CarOrder>();
			}
			
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ctoList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return ctoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CateViewHolder hoger = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.activity_cate_order_list_item, null);
				hoger = new CateViewHolder();
				hoger.type = (TextView) convertView.findViewById(R.id.cateorder_order_type);
				hoger.statu = (TextView) convertView.findViewById(R.id.cateorder_order_statu);
				hoger.orderName = (TextView) convertView.findViewById(R.id.cateorder_catename);
				hoger.fee = (TextView) convertView.findViewById(R.id.cateorder_fee);
				hoger.date = (TextView) convertView.findViewById(R.id.cateorder_date);
				hoger.detail = (TextView) convertView.findViewById(R.id.cateorder_detailroom);
				
				convertView.setTag(hoger);
			}else{
				hoger = (CateViewHolder) convertView.getTag();
			}
			
			CarOrder ct = ctoList.get(position);
			hoger.type.setText("租车");
			hoger.statu.setText(getStatuString(ct.getStatus()));
			hoger.orderName.setText(ct.getCar_name());
			hoger.fee.setText("￥"+ct.getFee());
			hoger.date.setText("下单时间:"+ct.getCreatedAt());
			hoger.detail.setText(ct.getCar_detail());
			return convertView;
		}
		
		
		private String getStatuString(Integer i){
			if(i==0){//进行中，ing
				return "有效";
			}else if(i==1){//已完成，complete
				return "已完成";
			}else{
				return "已取消";
			}
		}
		
	}
	
	class CateViewHolder{
		TextView type;
		TextView statu;
		TextView orderName;
		TextView fee;
		TextView date;
		TextView detail;
		
	}
	
}

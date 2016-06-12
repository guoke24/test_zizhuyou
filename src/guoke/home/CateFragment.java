package guoke.home;

import java.util.ArrayList;
import java.util.List;

import guoke.activity.AttracTicketActivity;
import guoke.activity.AttractionsListActivity;
import guoke.activity.CateReserveActivity;
import guoke.adapter.AttractionsListAdapter;
import guoke.adapter.CateListAdapter;
import guoke.bean.CateBean;
import guoke.custom.listview.SingleLayoutListView;
import guoke.custom.listview.SingleLayoutListView.OnLoadMoreListener;
import guoke.custom.listview.SingleLayoutListView.OnRefreshListener;
import guoke.fragment.BaseFragment;
import guoke.model.AttractionsItem;
import guoke.model.CateItem;
import guoke.model.InfoCom;
import guoke.model.MRegion;
import guoke.model.MyUser;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.a.a.V;
import com.example.demo_zizhuyou.AddrAndDateActivity;
import com.example.demo_zizhuyou.AddressSelectActivity;
import com.example.demo_zizhuyou.LoginActivity;
import com.example.demo_zizhuyou.R;

import android.R.integer;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CateFragment extends BaseFragment implements OnClickListener{

	private TextView city_selectTextView;
	private EditText search_inputEditText;
	private Button search_btn;
	private MRegion curCity;

	private View search_inputEditText_layout;

	//private SingleLayoutListView mListView;
	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;

	// 每次检索的行数
	private final int mqueryCount = 15;
	// 要跳过的，等价于已经加载的行数
	private int mskipCount = 0;

	private CateListAdapter adapter;
	private List<CateBean> queryList = new ArrayList<CateBean>();
	private List<CateBean> curList = new ArrayList<CateBean>();

	/**
	 * 当接到 下拉刷新完成 或 下拉加载完成 的消息后， 更新adapter的数据源：curList，并通知adapter和sListView
	 */
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				if (adapter != null) {

					adapter.notifyDataSetChanged();
				}
				//mListView.onRefreshComplete(); // 下拉刷新完成
				mListView.onLoadMoreComplete(); // 加载更多完成
				mListView.setSelection(1);
				break;
			case LOAD_DATA_FINISH:
				if (adapter != null) {					
					adapter.notifyDataSetChanged();	
				}
				mListView.onLoadMoreComplete(); // 加载更多完成
				break;
			case 3://选择新城市回来
				search_inputEditText_layout.clearFocus();
				loadData(0);
				break;
			case 5://点击了输入框的查询
				if (adapter != null) {					
					adapter.notifyDataSetChanged();	
				}
				mListView.onLoadMoreComplete(); // 加载更多完成
				mListView.setSelection(1);
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View cateLayout = inflater.inflate(R.layout.homefragment_cate_layout,
				container, false);
	
		defaultCity();
		init(cateLayout);
		setListener();
		
		showProgressDialog();
		
		loadData(0);
		
		return cateLayout;
	}
	
	
	
	void init(View v){
		city_selectTextView = (TextView) v.findViewById(R.id.cate_city);
		city_selectTextView.setText(getSimpleName(curCity));
		
		search_inputEditText_layout = v.findViewById(R.id.cate_search_input_layout);
		search_inputEditText = (EditText) v.findViewById(R.id.cate_search_input);
		//取消焦点
		search_inputEditText_layout.clearFocus();
		
		search_btn = (Button) v.findViewById(R.id.cate_search_btn);
		
		mListView = (SingleLayoutListView) v.findViewById(R.id.cate_list_view);

		adapter = new CateListAdapter(getActivity(), curList);
		mListView.setAdapter(adapter);
	}
	
	void setListener(){
		city_selectTextView.setOnClickListener(this);
		search_btn.setOnClickListener(this);
		
		//上拉加载的时候，会从回调这个函数开始
		mListView.setOnLoadListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				// TODO 加载更多
				if(search_inputEditText.getText().toString().equals("")){
					loadData(1);
				}else{
					loadData(4);
				}
			}
		});	
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				CateBean cBean = curList.get(position-1);
				intent.putExtra(InfoCom.Extra_CateSelect, cBean);

				MyUser myUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
				if(myUser != null){
					intent.setClass(getActivity(), CateReserveActivity.class);
					startActivity(intent);
				}else{
					toast("未登录，请先登录");
					intent.putExtra(InfoCom.fromWhere, InfoCom.fromCateList);
					intent.setClass(getActivity(), LoginActivity.class);
					startActivity(intent);
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
		//showProgressDialog();

		new Thread() {
			@Override
			public void run() {
				switch (type) {
				case 0:
					//表示从头查起
					mskipCount=0;
					curList.clear();
					findObj(mskipCount, mqueryCount);
					break;
				case 1://上拉刷加载
					findObj(mskipCount, mqueryCount);	
					break;
				case 3:
					mskipCount=0;
					curList.clear();
					findObjByInput(mskipCount, mqueryCount);
					break;
				case 4:
					findObjByInput(mskipCount, mqueryCount);	
					break;
				}//switch end

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (type == 0) { // 下拉刷新
					Message _Msg = mHandler.obtainMessage(REFRESH_DATA_FINISH,
							queryList);
					mHandler.sendMessage(_Msg);
				} else if (type == 1 || type ==4) {//上拉加载
					Message _Msg = mHandler.obtainMessage(LOAD_DATA_FINISH,
							queryList);
					mHandler.sendMessage(_Msg);
				}else if(type == 3){//用户点击了查找按钮，这时候多加一个操作，回到顶部
					mHandler.sendEmptyMessage(5);
				}
			}
		}.start();
	}
	
	int type = 0;
	
	/**
	 * 
	 * @param where 从哪一行开始查
	 * @param how 查多少行
	 */
	private void findObj(int where,int how){
		BmobQuery<CateBean> query = new BmobQuery<CateBean>();
		query.addWhereContains("city", getSimpleName(curCity));
		query.setSkip(where);
		query.setLimit(how);
		query.findObjects(getActivity(), new FindListener<CateBean>() {
			
			@Override
			public void onSuccess(List<CateBean> arg0) {
				closeProgressDialog();
				if(arg0.size()==0){
					toast("没有结果");
				}
				//toast("查询成功，查到"+arg0.size()+"个美食");
				//下次再查，从哪儿查起？
				mskipCount+=arg0.size();
				//如果没查够那么多行，是不是查完了？
				if(arg0.size()<mqueryCount){
					mListView.setCanLoadMore(false);
				}else{
					mListView.setCanLoadMore(true);
				}
				//最后，把查到的装到缓存列表
				curList.addAll(arg0);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				closeProgressDialog();
				toast("查询失败："+arg1);
			}
		});

	}

	private void findObjByInput(int where,int how){
		BmobQuery<CateBean> query = new BmobQuery<CateBean>();
		query.addWhereContains("city", getSimpleName(curCity));
		query.addWhereContains("name", search_inputEditText.getText().toString());
		query.setSkip(where);
		query.setLimit(how);
		query.findObjects(getActivity(), new FindListener<CateBean>() {
			
			@Override
			public void onSuccess(List<CateBean> arg0) {
				closeProgressDialog();
				if(arg0.size()==0){
					toast("没有结果");
				}
				//toast("查询成功，查到"+arg0.size()+"个美食");
				//下次再查，从哪儿查起？
				mskipCount+=arg0.size();
				//如果没查够那么多行，是不是查完了？
				if(arg0.size()<mqueryCount){
					mListView.setCanLoadMore(false);
				}else{
					mListView.setCanLoadMore(true);
				}
				//最后，把查到的装到缓存列表
				
				curList.addAll(arg0);
//				for(CateBean r:arg0){
//					curList.add(r);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cate_city:
			city_click();
			break;
		case R.id.cate_search_btn:
			search_btn_click();
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 
	 */
	void city_click(){
		search_inputEditText.setText("");
		Intent intent = new Intent();
		intent.setClass(getActivity(), AddressSelectActivity.class);
		getActivity().startActivityForResult(intent, 2333);
	}
	/**
	 * 
	 */
	void search_btn_click(){
		//取消输入框的焦点
		search_inputEditText_layout.clearFocus();
		showProgressDialog();
		loadData(3);
	}
	
	/**
	 * 
	 * @param mcity
	 */
	public void setCurCityAndLoad(MRegion mcity) {
		if(mcity!=null){
			curCity = mcity;
			city_selectTextView.setText(getSimpleName(curCity));
			
		}else{
			toast("选中城市无效，恢复默认城市");
			defaultCity();
		}
		showProgressDialog();
		mHandler.sendEmptyMessageDelayed(3, 2000);
	}
	
	String getSimpleName(MRegion city){
		
		String string = city.getRname().replace("市", "");
		return string;
	}
	
	void defaultCity(){
		MRegion region = new MRegion();
		region.setRid(349);
		region.setRname("桂林市");
		region.setStyle(2);
		region.setPid(22);
		region.setSiteid(0);
		curCity = region;
	}
	
}

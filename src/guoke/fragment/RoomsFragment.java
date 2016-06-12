package guoke.fragment;

import httpUtil.HttpUtil;
import intetface.HttpCallbackListener;
import intetface.StartActivityWithObjectListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import json_parse.utils.JsonParser;

import guoke.activity.HotelRoomReserveActivity;
import guoke.adapter.RoomListAdapter;
import guoke.bean.HotelBean;
import guoke.bean.RoomBean;
import guoke.model.HotelItem;
import guoke.model.InfoCom;
import guoke.model.MRegion;
import guoke.model.RoomItem;

import cn.bmob.v3.BmobQuery;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RoomsFragment extends BaseFragment implements OnItemClickListener {

	private ListView roomListVIew;
	private RoomListAdapter adapter;
	// private List<RoomItem> roomList = new ArrayList<RoomItem>();
	private List<RoomBean> roomBeanList = new ArrayList<RoomBean>();
	private RoomBean roomSelect;
	// private HotelItem hotelSelect;
	private HotelBean hotelSelect;

	private StartActivityWithObjectListener<RoomBean> startwithroom;

	public void setStartWithRoomListener(
			StartActivityWithObjectListener<RoomBean> s) {
		startwithroom = s;
	}

	/**
	 * 初始化
	 * 
	 * @param view
	 */
	private void init(View view) {

		Bundle bundle = getArguments();
		// hotelSelect = (HotelItem)
		// bundle.getSerializable(InfoCom.Extra_HotelSelect);
		hotelSelect = (HotelBean) bundle
				.getSerializable(InfoCom.Extra_HotelSelect);

		adapter = new RoomListAdapter(getActivity(), roomBeanList);
		roomListVIew = (ListView) view.findViewById(R.id.room_list);
		roomListVIew.setAdapter(adapter);
		showProgressDialog();
		findRoomBeanByhotel(hotelSelect);

	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				closeProgressDialog();
				fixListViewHeight(roomListVIew);
				adapter.notifyDataSetChanged();
				toast("加载成功,共有" + roomBeanList.size() + "个房型");

				break;
			case 2:
				closeProgressDialog();
				toast("加载失败");
				break;
			default:
				break;
			}

		}

	};

	/**
	 * 从好数据接口查找的房间表
	 * 
	 * @param ht
	 */
	private void findRoomBeanByhotel(HotelBean ht) {
		String roomurl = getHotelRoomUrl(hotelSelect.getId());
		HttpUtil.sendRequestWithHttpConnection(roomurl,
				new HttpCallbackListener() {

					@Override
					public void onFinish(String response) {

						// TODO Auto-generated method stub
						List<RoomBean> tmpBeans = JsonParser.parseJsonForRoomBean(response);
						roomBeanList.addAll(tmpBeans);
						mHandler.sendEmptyMessage(1);
					}

					@Override
					public void onError(Exception e) {
						mHandler.sendEmptyMessage(2);
					}
				});
	}

	/**
	 * 得到 酒店房间列表的url
	 * 
	 * @param cityid
	 * @param page
	 * @return
	 */
	private String getHotelRoomUrl(Integer hotelid) {
		Map params = new HashMap();// 请求参数
		params.put("key", InfoCom.APPKEY);//
		params.put("hid", hotelid);//
		// params.put("page", page);// 当前页数，默认1
		String url = InfoCom.roomlist_url + "?" + urlencode(params);

		return url;
	}

	/**
	 * 从bmob数据库查找的
	 * 
	 * @param ht
	 */
	private void findRoomsByHotel(HotelBean ht) {
		// RoomItem baseRoomItem = getBaseRoom(ht);
		// roomList.add(baseRoomItem);
		// showProgressDialog();
		BmobQuery<RoomItem> query = new BmobQuery<RoomItem>();
		// query.addWhereEqualTo("hotel_id", ht.getHid());
		query.findObjects(getActivity(), new FindListener<RoomItem>() {

			@Override
			public void onSuccess(List<RoomItem> arg0) {
				// closeProgressDialog();
				// roomList.clear();
				toast("加载成功,共有" + (arg0.size() + 1) + "个房型");
				// roomList.addAll(arg0);
				// for(RoomItem rt : arg0){
				// roomList.add(rt);
				// }
				fixListViewHeight(roomListVIew);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onError(int arg0, String arg1) {
				// closeProgressDialog();
				toast("加载失败");
			}
		});
	}

	private void setListener() {
		roomListVIew.setOnItemClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View roomListView = inflater.inflate(R.layout.fragment_room_list,
				container, false);
		init(roomListView);
		setListener();
		return roomListView;
	}

	/**
	 * 修复listview在scrollview的高度
	 * 
	 * @param listView
	 */
	public void fixListViewHeight(ListView listView) {
		// 如果没有设置数据适配器，则ListView没有子项，返回。
		RoomListAdapter listAdapter = (RoomListAdapter) listView.getAdapter();
		int totalHeight = 0;
		if (listAdapter == null) {
			return;
		}
		for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
			View listViewItem = listAdapter.getView(index, null, listView);
			// 计算子项View 的宽高
			listViewItem.measure(0, 0);
			// 计算所有子项的高度和
			totalHeight += listViewItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// listView.getDividerHeight()获取子项间分隔符的高度
		// params.height设置ListView完全显示需要的高度
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		startwithroom.startWithObject(roomBeanList.get(position));
	}

	@SuppressWarnings("deprecation")
	protected String getMonth_Day(Date date) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(date.getMonth() + 1);
		stringBuilder.append("月");
		stringBuilder.append(date.getDate());
		String md = stringBuilder.toString();
		return md;

	}

	// private RoomItem getBaseRoom(HotelBean ht){
	// RoomItem roomItem = new RoomItem();
	// roomItem.setName("经济单人房");
	// roomItem.setDetail("单床1.2m，有wifi");
	// //roomItem.setPrice(ht.getBottomprice());
	// //
	// roomItem.setBed_kind("单床");
	// roomItem.setBed_size("1.2m");
	// roomItem.setBroadband("免费宽带");
	// //roomItem.setHotel_id(ht.getHid());
	// roomItem.setPeople_number("1人");
	// roomItem.setRoom_size("15㎡");
	// roomItem.setWifi("有wifi");
	// roomItem.setFloor("2楼");
	// return roomItem;
	// }
	//

}

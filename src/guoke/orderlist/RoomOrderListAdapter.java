package guoke.orderlist;

import guoke.model.RoomOrder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;

import com.example.demo_zizhuyou.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RoomOrderListAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Context context;
	private List<RoomOrder> roomOrders; 
	
	public RoomOrderListAdapter(Context context,List<RoomOrder> ord){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		roomOrders = ord;
		
		if(roomOrders == null){
			roomOrders = new ArrayList<RoomOrder>();
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return roomOrders.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return roomOrders.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoger hoger = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fragment_allroomlist_item, null);
			hoger = new ViewHoger();
			
			hoger.statu = (TextView) convertView.findViewById(R.id.roomorder_order_statu);
			hoger.orderName = (TextView) convertView.findViewById(R.id.roomorder_hotelname);
			hoger.fee = (TextView) convertView.findViewById(R.id.roomorder_fee);
			hoger.date = (TextView) convertView.findViewById(R.id.roomorder_date);
			hoger.detail = (TextView) convertView.findViewById(R.id.roomorder_detailroom);
			
			convertView.setTag(hoger);
		}else{
			hoger = (ViewHoger) convertView.getTag();
		}
		
		RoomOrder rmod = roomOrders.get(position);
		hoger.orderName.setText(rmod.getHotel_name());
		hoger.fee.setText(String.valueOf(rmod.getFee()));
		hoger.date.setText(getYMD(rmod.getDateCheckIn())+" 至  "+getYMD(rmod.getDateCheckOut()));
		hoger.detail.setText(rmod.getRoom_name());
		hoger.statu.setText(getStatuString(rmod.getStatu()));
		return convertView;
	}
	
	class ViewHoger{
		TextView statu;
		TextView orderName;
		TextView fee;
		TextView date;
		TextView detail;
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
	
	/**
	 * 获得年月日
	 * @param bmd
	 * @return
	 */
	private String getYMD(BmobDate bmd){
		String tmp = bmd.getDate();
		String ymd[] = tmp.split(" ");
		return ymd[0];
	}

}

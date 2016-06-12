package guoke.adapter;

import guoke.bean.RoomBean;
import guoke.model.RoomItem;

import java.util.ArrayList;
import java.util.List;

import com.example.demo_zizhuyou.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RoomListAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Context context;
//	private List<RoomItem> roomList;
	private List<RoomBean> roomList;
	
	public RoomListAdapter(Context context,List<RoomBean> rs){
		this.context = context;
		inflater = LayoutInflater.from(context);
		roomList = rs;
		
		if(roomList == null){
			roomList = new ArrayList<RoomBean>();
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return roomList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return roomList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fragment_rooms_item, null);
			holder = new ViewHolder();
			holder.roomNameText=(TextView)convertView.findViewById(R.id.room_item_name);
			holder.roomDetailText=(TextView)convertView.findViewById(R.id.room_item_detail);
			holder.roomPriceText=(TextView)convertView.findViewById(R.id.room_item_price);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.roomNameText.setText(roomList.get(position).getRoomName());
			
		holder.roomDetailText.setText(getDetail(roomList.get(position)));
		holder.roomPriceText.setText("￥"+roomList.get(position).getPrice());
		
		return convertView;
	}
	
	class ViewHolder
	{
		TextView roomNameText;
		TextView roomDetailText;
		TextView roomPriceText;
	}

	String getDetail(RoomBean rb){
		StringBuilder sBuilder = new StringBuilder();
		String brkfst = "早餐:"+rb.getBreakfast().trim()+"  ";
		String bed = rb.getBed()+"  ";
		String areaSize = rb.getArea();
		sBuilder.append(brkfst);
		sBuilder.append(bed);
		if(!areaSize.equals("")){
			sBuilder.append(areaSize.replace("平方米", "")+"㎡");
		}
		return sBuilder.toString();
	}
	
}

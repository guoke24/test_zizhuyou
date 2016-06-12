package guoke.adapter;

import guoke.bean.TicketBean;
import guoke.model.RoomItem;
import guoke.model.TicketItem;

import java.util.List;

import com.example.demo_zizhuyou.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AttracTicketAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Context context;
	private List<TicketBean> aList;
	
	public AttracTicketAdapter(Context context,List<TicketBean> list){
		this.context = context;
		inflater = LayoutInflater.from(context);
		aList = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return aList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return aList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoder viewHoder = null;
		if(convertView == null){
			convertView = (View)inflater.inflate(R.layout.activity_attrac_ticket_item, null);
			viewHoder = new ViewHoder();
			viewHoder.nameTextView = (TextView) convertView.findViewById(R.id.ticket_item_name);
			viewHoder.detailTextView = (TextView) convertView.findViewById(R.id.ticket_item_detail);
			viewHoder.priceTextView = (TextView) convertView.findViewById(R.id.ticket_item_price);
			convertView.setTag(viewHoder);
		}else{
			viewHoder = (ViewHoder) convertView.getTag();
		}
		
		TicketBean ticketItem = aList.get(position);
		viewHoder.nameTextView.setText(ticketItem.getName());
		//viewHoder.detailTextView.setText(ticketItem.getDatail());
		viewHoder.priceTextView.setText("￥"+ticketItem.getPrice());
		
		return convertView;
	}

}

class ViewHoder{
	TextView nameTextView;
	TextView detailTextView;
	TextView priceTextView;
}

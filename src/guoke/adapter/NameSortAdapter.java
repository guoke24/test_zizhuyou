package guoke.adapter;

import com.example.demo_zizhuyou.R;

import guoke.model.MRegion;
import guoke.sort_helper.NameSortHelper;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class NameSortAdapter extends BaseExpandableListAdapter {
	

	public long getCombinedChildId(int groupId, int childId) {
		// TODO Auto-generated method stub
		MRegion mp3Info = (MRegion) getChild(groupId, childId);
		//System.out.println(mp3Info==null);
		return helper.getMp3InfosFromHashList().indexOf(mp3Info);
	}


	private LayoutInflater inflater;
	private Context context;
	// �ַ�,������
	private List<MRegion> addrInfos;
	//��������Ķ�����
	private NameSortHelper helper; 
	
	/**
	 * ���캯��,ʵ��[Mp3SortHelper],����[Mp3SortHelper]�Ĺ��캯����
	 * ��[mp3Infos]��ӵ�[hashList],ͬʱʵ�ַ��������
	 * */
	public NameSortAdapter(Context context,List<MRegion> addrs){
		this.context=context;
		this.inflater=LayoutInflater.from(context);
		this.addrInfos=addrs;
		if (addrInfos == null) {
			addrInfos = new ArrayList<MRegion>();
		}
		helper=new NameSortHelper(addrInfos);
		//sort();
	}

		
	public NameSortHelper getHelper(){
		return helper;
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return helper.getHashList().size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return helper.getHashList().getChildList(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return helper.getHashList().getKeyIndex(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return helper.getHashList().getChild(groupPosition, childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView=inflater.inflate(R.layout.addr_group_item, null);
			//����Ϊtrue,������û����Ӧ��!!!
			convertView.setClickable(true);
			
		}
		TextView textView=(TextView)convertView.findViewById(R.id.group_text);
		textView.setText(helper.getFirstChar(helper.getHashList()
				.getChild(groupPosition, 0).getRname()));
		//convertView.setVisibility(View.GONE);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;//�����󶨺Ͳ���ÿһitem�Ŀؼ�
		if(convertView==null){
			convertView=inflater.inflate(R.layout.addr_child_item, null);
			//convertView=inflater.inflate(R.layout.child_item, (ViewGroup) convertView);
			holder=new ViewHolder();
			holder.cityNameView=(TextView)convertView.findViewById(R.id.city_name);
			
			convertView.setTag(holder);
			//�˴�����Ϊtrue,item�����ܵ��
			//convertView.setClickable(true);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		MRegion addrInfo=helper.getHashList().getChild(groupPosition, childPosition);
		//��ʾ������,���������ר����
		holder.cityNameView.setText(addrInfo.getRname());
		
		
		
		return convertView;
	}

	public final class ViewHolder{
		TextView cityNameView;
		
	}

	
}

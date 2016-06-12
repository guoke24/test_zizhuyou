package guoke.adapter;

import com.jan_17_guokemusic.R;
import guoke.model.Mp3Info;
import guoke.sort_helper.Mp3SortHelper;
import guoke.utils.ImageUtil;
import guoke.utils.MediaUtil;

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

public class Mp3SortAdapter extends BaseExpandableListAdapter {
	

	public long getCombinedChildId(int groupId, int childId) {
		// TODO Auto-generated method stub
		Mp3Info mp3Info = (Mp3Info) getChild(groupId, childId);
		//System.out.println(mp3Info==null);
		return helper.getMp3InfosFromHashList().indexOf(mp3Info);
	}

	private List<String> group;
	private List<List<String>> child;
	
	private LayoutInflater inflater;
	private Context context;
	// 字符串,即人名
	private List<Mp3Info> mp3Infos;
	//处理排序的对象类
	private Mp3SortHelper helper; 
	
	//List<View> viewList= new ArrayList<View>();
	private final int EDITSTATE = 2;
	private final int GENERALSTATE = 1;
	private int stateFlag=GENERALSTATE;
	
	/**
	 * 更新编辑状态或一般状态
	 * */
	public void setEditflag(int editflag) {
		this.stateFlag = editflag;
	}


	/**
	 * 构造函数,实例化[Mp3SortHelper],并在[Mp3SortHelper]的构造函数里
	 * 将[mp3Infos]添加到[hashList],同时实现分类和排序
	 * */
	public Mp3SortAdapter(Context context,List<Mp3Info> mp3Infos){
		this.context=context;
		this.inflater=LayoutInflater.from(context);
		this.mp3Infos=mp3Infos;
		if (mp3Infos == null) {
			mp3Infos = new ArrayList<Mp3Info>();
		}
		helper=new Mp3SortHelper(mp3Infos);
		//sort();
	}

		
	public Mp3SortHelper getHelper(){
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
			convertView=inflater.inflate(R.layout.group_item, null);
			//设置为true,点击后就没有相应了!!!
			convertView.setClickable(true);
			
		}
		TextView textView=(TextView)convertView.findViewById(R.id.group_text);
		textView.setText(helper.getFirstChar(helper.getHashList()
				.getChild(groupPosition, 0).getTitle()));
		//convertView.setVisibility(View.GONE);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;//用来绑定和操作每一item的控件
		if(convertView==null){
			convertView=inflater.inflate(R.layout.child_item, null);
			holder=new ViewHolder();
			holder.titleView=(TextView)convertView.findViewById(R.id.child_titleText);
			holder.artistAndAlbumView=(TextView)convertView.findViewById(R.id.child_artisanAndAlbumText);
			holder.albumView=(ImageView)convertView.findViewById(R.id.child_albumView);
			holder.playingView=(ImageView)convertView.findViewById(R.id.playingView);
			holder.checkBox=(CheckBox)convertView.findViewById(R.id.child_checkbox);
			convertView.setTag(holder);
			//此处设置为true,item将不能点击
			//convertView.setClickable(true);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		Mp3Info tInfo=helper.getHashList().getChild(groupPosition, childPosition);
		//显示歌曲名,艺术家名和专辑名
		holder.titleView.setText(tInfo.getTitle());
		holder.artistAndAlbumView.setText(tInfo.getArtist()+"-"+tInfo.getAlbum());
		
		//显示封面专辑,滑动的时候会卡
		//showAlbum(tInfo, holder);//显示专辑封面
		if(tInfo.getIsPrePare()==true){
			holder.playingView.setVisibility(View.VISIBLE);
		}else{
			holder.playingView.setVisibility(View.GONE);
		}
		
		//System.out.println("getChildView被调用"+"playingView的可见性为:"+holder.playingView.getVisibility());
		
		//控制checkbox的显示
		if(stateFlag==GENERALSTATE){//常规状态不显示
			holder.checkBox.setVisibility(View.GONE);
		}else{//编辑状态才显示
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.checkBox.setChecked(tInfo.getIschecked());
		}
		return convertView;
	}

	public final class ViewHolder{
		TextView titleView;
		TextView artistAndAlbumView;
	    public CheckBox checkBox;
		ImageView albumView;
		ImageView playingView;
	}

	/**
	 * 被废用
	 * */
	public List<View> getViews(){
		//return viewList;
		return null;
	}
	
	/**
	 * 初始化所有mp3Info的选择状态,设置为未选择
	 * */
	 private void initmp3InfosChecked(){
		for(Mp3Info m:mp3Infos){
			m.setIschecked(false);
		}
		//addToHashList();
	}

	 /**
	  * 给[childitem]的[albumView]设置封面专辑图片
	  * */
	 private void showAlbum(Mp3Info tInfo,ViewHolder holder){
		 Bitmap bm = MediaUtil.getArtwork(context, tInfo.getMdstore_id(), tInfo.getAlbumId(), true, false);			
			//专辑封面图片为空吗?
			if(bm != null) {//专辑封面图片不为空
				holder.albumView.setImageBitmap(bm);	//显示专辑封面图片
				//musicAblumReflection.setImageBitmap(ImageUtil.createReflectionBitmapForSingle(bm));	//显示倒影
			} else {//专辑封面图片是空的
				bm = MediaUtil.getDefaultArtwork(context, true);
				holder.albumView.setImageBitmap(bm);	//显示专辑封面图片
				//musicAblumReflection.setImageBitmap(ImageUtil.createReflectionBitmapForSingle(bm));	//显示倒影
			}
	 }
	 
	/**
	 * 该函数会对adapter相关数据源的进行初始化
	 * */
	public void updataAdapter(){
		initmp3InfosChecked();
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		updataAdapter();
		super.notifyDataSetChanged();
	}

	
}

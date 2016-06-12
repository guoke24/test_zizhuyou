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
	// �ַ���,������
	private List<Mp3Info> mp3Infos;
	//��������Ķ�����
	private Mp3SortHelper helper; 
	
	//List<View> viewList= new ArrayList<View>();
	private final int EDITSTATE = 2;
	private final int GENERALSTATE = 1;
	private int stateFlag=GENERALSTATE;
	
	/**
	 * ���±༭״̬��һ��״̬
	 * */
	public void setEditflag(int editflag) {
		this.stateFlag = editflag;
	}


	/**
	 * ���캯��,ʵ����[Mp3SortHelper],����[Mp3SortHelper]�Ĺ��캯����
	 * ��[mp3Infos]��ӵ�[hashList],ͬʱʵ�ַ��������
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
			//����Ϊtrue,������û����Ӧ��!!!
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
		ViewHolder holder = null;//�����󶨺Ͳ���ÿһitem�Ŀؼ�
		if(convertView==null){
			convertView=inflater.inflate(R.layout.child_item, null);
			holder=new ViewHolder();
			holder.titleView=(TextView)convertView.findViewById(R.id.child_titleText);
			holder.artistAndAlbumView=(TextView)convertView.findViewById(R.id.child_artisanAndAlbumText);
			holder.albumView=(ImageView)convertView.findViewById(R.id.child_albumView);
			holder.playingView=(ImageView)convertView.findViewById(R.id.playingView);
			holder.checkBox=(CheckBox)convertView.findViewById(R.id.child_checkbox);
			convertView.setTag(holder);
			//�˴�����Ϊtrue,item�����ܵ��
			//convertView.setClickable(true);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		Mp3Info tInfo=helper.getHashList().getChild(groupPosition, childPosition);
		//��ʾ������,����������ר����
		holder.titleView.setText(tInfo.getTitle());
		holder.artistAndAlbumView.setText(tInfo.getArtist()+"-"+tInfo.getAlbum());
		
		//��ʾ����ר��,������ʱ��Ῠ
		//showAlbum(tInfo, holder);//��ʾר������
		if(tInfo.getIsPrePare()==true){
			holder.playingView.setVisibility(View.VISIBLE);
		}else{
			holder.playingView.setVisibility(View.GONE);
		}
		
		//System.out.println("getChildView������"+"playingView�Ŀɼ���Ϊ:"+holder.playingView.getVisibility());
		
		//����checkbox����ʾ
		if(stateFlag==GENERALSTATE){//����״̬����ʾ
			holder.checkBox.setVisibility(View.GONE);
		}else{//�༭״̬����ʾ
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
	 * ������
	 * */
	public List<View> getViews(){
		//return viewList;
		return null;
	}
	
	/**
	 * ��ʼ������mp3Info��ѡ��״̬,����Ϊδѡ��
	 * */
	 private void initmp3InfosChecked(){
		for(Mp3Info m:mp3Infos){
			m.setIschecked(false);
		}
		//addToHashList();
	}

	 /**
	  * ��[childitem]��[albumView]���÷���ר��ͼƬ
	  * */
	 private void showAlbum(Mp3Info tInfo,ViewHolder holder){
		 Bitmap bm = MediaUtil.getArtwork(context, tInfo.getMdstore_id(), tInfo.getAlbumId(), true, false);			
			//ר������ͼƬΪ����?
			if(bm != null) {//ר������ͼƬ��Ϊ��
				holder.albumView.setImageBitmap(bm);	//��ʾר������ͼƬ
				//musicAblumReflection.setImageBitmap(ImageUtil.createReflectionBitmapForSingle(bm));	//��ʾ��Ӱ
			} else {//ר������ͼƬ�ǿյ�
				bm = MediaUtil.getDefaultArtwork(context, true);
				holder.albumView.setImageBitmap(bm);	//��ʾר������ͼƬ
				//musicAblumReflection.setImageBitmap(ImageUtil.createReflectionBitmapForSingle(bm));	//��ʾ��Ӱ
			}
	 }
	 
	/**
	 * �ú������adapter�������Դ�Ľ��г�ʼ��
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

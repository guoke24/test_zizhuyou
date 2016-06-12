package guoke.fragment;

import guoke.adapter.NameSortAdapter;
import guoke.db.DBManager;
import guoke.model.MRegion;
import guoke.custom.SideButton;
import guoke.custom.SideButton.OnTouchSideButtonListener;

import intetface.ReBackListener;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.example.demo_zizhuyou.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 */
public class CitysFragment extends BaseFragment implements OnClickListener{

	private DBManager dbm;
	private SQLiteDatabase db;
	
	private ExpandableListView eListView;
	NameSortAdapter adapter;
	private List<MRegion> addrInfos;
	
	Context context;
	
	private Button city1;
	private Button city2;
	private Button city3;
	
	private SideButton sideButton;
	
	@Override
	public void onAttach(Activity activity) {
		context = activity.getApplicationContext();
		super.onAttach(activity);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View citysListView = inflater.inflate(R.layout.fragment_citys, 
				container, false);
		init(citysListView);
		setListener();
		
		return citysListView;
	}
	
	private ReBackListener reBackListener;
	
	
	
	/**
	 * @param v
	 */
	private void init(View v){
		city1 = (Button) v.findViewById(R.id.city1);
		city1.setOnClickListener(this);
		city2 = (Button) v.findViewById(R.id.city2);
		city2.setOnClickListener(this);
		city3 = (Button) v.findViewById(R.id.city3);
		city3.setOnClickListener(this);
		
		
		sideButton = (SideButton)v.findViewById(R.id.sideButton);
		addrInfos = getCityList();
		adapter=new NameSortAdapter(context, addrInfos);
		eListView = (ExpandableListView)v.findViewById(R.id.elist);
		eListView.setAdapter(adapter);
		//eListView.requestFocusFromTouch();
		for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
				eListView.expandGroup(i);
		}
	}

	
	
	/**
	 * 锟斤拷锟矫硷拷锟斤拷
	 */
	private void setListener(){
		eListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				MRegion regionItem=adapter.getHelper().getHashList().getChild(groupPosition, childPosition);
				//Toast.makeText(getActivity(), regionItem.getRname(), Toast.LENGTH_SHORT).show();
				reBackListener.reback(regionItem);
				
				return false;
			}
		});
		
		//
		sideButton.setOnTouchSideButtonListener(new OnTouchSideButtonListener(){

        	
        	View layoutView=LayoutInflater.from(getActivity())
					.inflate(R.layout.addr_alert_dialog, null);
			TextView text =(TextView) layoutView.findViewById(R.id.content);
			
			PopupWindow popupWindow ;
			@Override
			public void onTouchSideButtonDown(String selectedChar) {
				 int index=adapter.getHelper().getHashList().indexOfKey(selectedChar);
				   if(index!=-1)
				   {
						eListView.setSelectedGroup(index);;
				   }
					if(popupWindow!=null){
						text.setText(selectedChar);
					}
					else
					{   
					    popupWindow = new PopupWindow(layoutView,
								160, 160,false);
						popupWindow.showAtLocation(getActivity().getWindow().getDecorView(),
								Gravity.CENTER, 0, 0);
					}
					text.setText(selectedChar);
			}

			@Override
			public void onTouchSideButtonUP() {
				// TODO Auto-generated method stub
				if(popupWindow!=null)
					popupWindow.dismiss();
					popupWindow=null;
			}
        	
        });
	}
	
	/**
	 * 
	 * @return
	 */
	private List<MRegion> getCityList(){
		//get the instance of db
		dbm = new DBManager(context);
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	List<MRegion> list = new ArrayList<MRegion>();
	 	try {
	 		//qeury the city form the db
		 	String sql = "select * from Region where style = 2";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	//String name=cursor.getString(cursor.getColumnIndex("rname")); 
		        MRegion regionItem=getRegionObjectWithCursor(cursor);
		        //regionItem.setRname(name);
		        
		        list.add(regionItem);
		        cursor.moveToNext();
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			dbm.closeDatabase();
			db.close();
		}
	 	return list;
		
	}

	/**
	 * 锟斤拷锟矫回碉拷锟斤拷锟斤拷
	 */
	public void setReBackListener(ReBackListener r){
		reBackListener = r;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.city1:
			MRegion r1 = new MRegion();
			r1.setRid(2);
			r1.setRname("北京市");
			r1.setStyle(2);
			r1.setPid(1);
			r1.setSiteid(0);
			
			reBackListener.reback(r1);
			break;
		case R.id.city2:
			MRegion r2 = new MRegion();
			r2.setRid(312);
			r2.setRname("长沙市");
			r2.setStyle(2);
			r2.setPid(20);
			reBackListener.reback(r2);
			break;
		case R.id.city3:
			MRegion region = new MRegion();
			region.setRid(349);
			region.setRname("桂林市");
			region.setStyle(2);
			region.setPid(22);
			region.setSiteid(0);
			
			reBackListener.reback(region);
			break;
		default:
			break;
		}
		
	}
	
}



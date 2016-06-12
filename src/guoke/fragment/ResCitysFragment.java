package guoke.fragment;

import guoke.db.DBManager;
import guoke.model.MRegion;

import intetface.ReBackListener;

import java.util.ArrayList;
import java.util.List;

import com.example.demo_zizhuyou.R;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


@SuppressLint("UseValueOf")
public class ResCitysFragment extends Fragment{

	private DBManager dbm;
	private SQLiteDatabase db;
	
	ListView resListView;
	private ArrayAdapter<String> adapter;
	List<String> nameList = new ArrayList<String>();
	List<MRegion> regionList = new ArrayList<MRegion>();
	
	private ReBackListener reBackListener;

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View resCityView = inflater.inflate(R.layout.fragment_rescitys, 
							container, false);
		init(resCityView);
		setListener();
		
		return resCityView;
	}
	
	private void init(View resCityView){

		//
		Bundle bundle = getArguments();
		String search_string = bundle.getString("search_data");		
		searchAllByLikeRname(search_string);
		//violenceSearch(search_string);
		fillNameList();
		//
		resListView = (ListView)resCityView.findViewById(R.id.res_citys_list);
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameList);
		resListView.setAdapter(adapter);
	}
	
	private void setListener(){
		resListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				MRegion regionItem = regionList.get(position);
				if(regionItem.getStyle()==1){
					//就这个省份再查一遍
					searchCityByRid(regionItem);
					fillNameList();
					adapter.notifyDataSetChanged();
					
				}else if(regionItem.getStyle()==3){
					//如果点击了城市是 区县，取其城市
					
					MRegion prRegion = getParentRegion(regionItem);
					reBackListener.reback(prRegion);
				}
				else{
					reBackListener.reback(regionItem);				

				}
				//Toast.makeText(getActivity(), String.valueOf(regionItem.getRid()), Toast.LENGTH_SHORT).show();
				//String s[] = nameString.split(",");
				
			}
			
		});
	}
	
	/**
	 * 暴力搜索！全部循环
	 * @param searchString
	 */
	private void violenceSearch(String searchString){
		//
		regionList.clear();
		nameList.clear();	
		//get the instance of db
		initBataBase();
	 	try {
	 		//qeury the city form the db
		 	String sql = "select * from Region";
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToPrevious();
	        while (cursor.moveToNext()){ 
	        	String name = cursor.getString(cursor.getColumnIndex("rname"));
	        	if(name.contains(searchString)||searchString.contains(name)||name.equals(searchString)){
	        		MRegion region = getRegionObjectWithCursor(cursor);
	        		//Region parentRegion = getParentRegion(region, db);
	        		regionList.add(region);
	        		//nameList.add(region.getRname());
	        	}
		        cursor.moveToNext();
	        }
	        cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{			
			closeBataBase();
		}
	}
	
	private void fillNameList(){
		nameList.clear();
		for(MRegion r:regionList){
			String name = r.getRname();
//			initBataBase();
//			try {
//				String sql ="select * from Region where rid = "+String.valueOf(r.getPid());
//				Cursor cursor = db.rawQuery(sql, null);
//				cursor.moveToFirst();
//		        while (!cursor.isLast()){ 
//		        	name = name +","+cursor.getString(cursor.getColumnIndex("rname"));
//			        cursor.moveToNext();
//		        }
//		        cursor.close();
//		        nameList.add(name);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//			closeBataBase();
			MRegion prRegion = getParentRegion(r);
			if(prRegion!=null){
				name = name +","+prRegion.getRname();
			}
			nameList.add(name);
		}
	}
	
	private void searchCountyByRid(MRegion r){
		Integer rid = r.getRid();
	
		//get the instance of db
		initBataBase();
	 	try {
	 		//qeury the city form the db
		 	String sql = "select * from Region where pid = "+String.valueOf(rid);
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToPrevious();
	        while (cursor.moveToNext()){ 
	        	MRegion region = getRegionObjectWithCursor(cursor);
	        	regionList.add(region);	        	
		        cursor.moveToNext();
	        }
	        cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{			
			closeBataBase();
		}	
	}
	
	private void searchCityByRid(MRegion r){
		Integer rid = r.getRid();
		regionList.clear();
		nameList.clear();	
		//get the instance of db
		initBataBase();
	 	try {
	 		//qeury the city form the db
		 	String sql = "select * from Region where pid = "+String.valueOf(rid);
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToPrevious();
	        while (cursor.moveToNext()){ 
	        	MRegion region = getRegionObjectWithCursor(cursor);
	        	regionList.add(region);	        	
		        cursor.moveToNext();
	        }
	        cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{			
			closeBataBase();
		}		
		
	}
	
	/**
	 * 模糊查询，中文匹配又可以了
	 * @param searchString
	 */
	private void searchAllByLikeRname(String searchString){

		//
		regionList.clear();
		nameList.clear();	
		//get the instance of db
		initBataBase();
	 	try {
	 		//qeury the city form the db
		 	String sql = "select * from Region where rname like '";
		 	String value = searchString+"%'";  
	        Cursor cursor = db.rawQuery(sql+value,null);  
	        cursor.moveToPrevious();
	        while (cursor.moveToNext()){ 
	        	MRegion region = getRegionObjectWithCursor(cursor);
	        	regionList.add(region);	        	
		        cursor.moveToNext();
	        }
	        cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{			
			closeBataBase();
			//如果查询到城市，就添加区县
			if(regionList.size()==1&&regionList.get(0).getStyle()==2){
				searchCountyByRid(regionList.get(0));
				fillNameList();
			}
		}
		
	}
	
	private void dailTheRes(Cursor cursor){
		int style = cursor.getInt(cursor.getColumnIndex("style"));
		if(style == 2){
			dailWithCity(cursor);
		}
	}
	
	private void dailWithCity(Cursor cursor){
		//先填充城市
		MRegion cityRegion = getRegionObjectWithCursor(cursor);
		//
		regionList.add(cityRegion);
		//在填充城市名和省份名到nameList
		//Region parentRegion = getParentRegion(cityRegion);
		String name = cityRegion.getRname();
		nameList.add(name);
	}

	
	private MRegion getParentRegion(MRegion childRegion){
		//int pid = ;
		initBataBase();
		MRegion region = null;
	 	try {
	 		//qeury the city form the db
		 	String sql = "select * from Region where rid = "+String.valueOf(childRegion.getPid());  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToPrevious();
	        
	        while (cursor.moveToNext()){ 
	        	//Toast.makeText(getActivity(), cursor.getString(cursor.getColumnIndex("rname")), Toast.LENGTH_SHORT).show();
	        	region = getRegionObjectWithCursor(cursor);
	        	
		        cursor.moveToNext();
	        }
	        cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			closeBataBase();
		}
		return region;
	}

	
	private MRegion getRegionObjectWithCursor(Cursor cursor){
		//先获取值
		Integer rid = new Integer(cursor.getInt(cursor.getColumnIndex("rid")));
		String rname = cursor.getString(cursor.getColumnIndex("rname"));
		Integer style = new Integer(cursor.getInt(cursor.getColumnIndex("style")));
		Integer pid = new Integer(cursor.getInt(cursor.getColumnIndex("pid"))); 
		//给Region对象赋值
		MRegion region = new MRegion(); 
		region.setRid(rid);
		region.setRname(rname);
		region.setStyle(style);
		region.setPid(pid);
		
		return region;
	}
	
	
	private void initBataBase(){
		dbm = new DBManager(getActivity());
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	}
	
	private void closeBataBase(){
		dbm.closeDatabase();
		db.close();
	}
	
	private void search_province(String searchName,List<String> list){
		//
		dbm = new DBManager(getActivity());
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	//
	 	list.clear();//init
	 	//
	 	int pid = 0;
	 	String pname= "";
	 	try {
	 		//qeury the city form the db
	 		String sql = "select * from Province";  
	 		Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
		        int id=cursor.getInt(cursor.getColumnIndex("id")); 
		        
		        String name=cursor.getString(cursor.getColumnIndex("province_name"));
		        if(name.contains(searchName)){
		        	pid = id;
		        	pname = new String(name);
		        	list.add(name);		        	
		        	break;
		        }		        
		        cursor.moveToNext();
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			dbm.closeDatabase();
			db.close();
			if(list.size()==1){//锟介到一锟斤拷省锟捷ｏ拷锟劫诧拷锟斤拷锟斤拷碌某锟斤拷校锟酵拷锟絚ode
				search_cityByCode(pname,pid, list);
			}else{//锟介不锟斤拷省锟捷ｏ拷锟斤拷通锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
				search_cityByName(searchName, list);
			}
			
		}
	}
	
	private void search_cityByCode(String pname,int pid,List<String> list){
		dbm = new DBManager(getActivity());
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	
	 	try {
	 		//qeury the city form the db
	 		String sql = "select * from City where province_id ='"+pid+"'";  
	 		Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
		        String name=cursor.getString(cursor.getColumnIndex("city_name"));		        
		        	list.add(name+","+pname);		        			        		        
		        cursor.moveToNext();
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			dbm.closeDatabase();
			db.close();
		}
	}
	
	private void search_cityByName(String searchName,List<String> list ){
		dbm = new DBManager(getActivity());
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	//
	 	list.clear();//init
	 	//
	 	int pid = 0;
	 	String pname = "";
	 	try {
	 		//qeury the city form the db
	 		String sql = "select * from City";  
	 		Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	
	        	pid = cursor.getInt(cursor.getColumnIndex("id"));
	        	pname=cursor.getString(cursor.getColumnIndex("city_name"));
	        	if(pname.contains(searchName)){
	        		list.add(pname);
	        		break;
	        	}
	        		
	        	cursor.moveToNext();
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			dbm.closeDatabase();
			db.close();
			if(list.size()==1){//锟介到一锟斤拷锟斤拷锟叫ｏ拷锟劫诧拷锟斤拷锟斤拷碌牡锟斤拷锟酵拷锟絚ode
				search_districtByCode(pname,pid, list);
			}else{//锟介不锟斤拷锟斤拷锟叫ｏ拷锟斤拷通锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
				search_districtByName(searchName, list);
			}
		}
	 	
	}
	
	private void search_districtByCode(String pname,int pid,List<String> list){
		dbm = new DBManager(getActivity());
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();	 	
	 	try {
	 		//qeury the city form the db
	 		String sql = "select * from County where city_id ='"+pid+"'";  
	 		Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 		      
		        String name=cursor.getString(cursor.getColumnIndex("county_name"));		        
		        	list.add(name+","+pname);		        			        		        
		        cursor.moveToNext();
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			dbm.closeDatabase();
			db.close();
		}
	}
	
	private void search_districtByName(String searchName,List<String> list){
		dbm = new DBManager(getActivity());
	 	dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	//
	 	list.clear();//init

	 	try {
	 		//qeury the city form the db
	 		String sql = "select * from County";  
	 		Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
		        String name=cursor.getString(cursor.getColumnIndex("county_name")); 	       
		        if(name.contains(searchName)){
		        	list.add(name);		        	
		        	break;
		        }		        
		        cursor.moveToNext();
	        }
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			dbm.closeDatabase();
			db.close();
		}
	 	
	}
	
	/**
	 * 锟斤拷锟矫回碉拷锟斤拷锟斤拷
	 */
	public void setReBackListener(ReBackListener r){
		reBackListener = r;
	}
	
}

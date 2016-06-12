package json_parse.utils;

import guoke.bean.HotelBean;
import guoke.bean.RoomBean;
import guoke.bean.SceneryBean;
import guoke.bean.TicketBean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



import android.util.Log;

public class JsonParser {
	
	
	
	public void parseJSONWithJSONOBJECT(String jsonData){
		try {
			JSONArray jsonArray=new JSONArray(jsonData);
			for(int i = 0;i<jsonArray.length();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String name = jsonObject.getString("name");
				String version = jsonObject.getString("version");
				Log.d("json", "id: "+id);
				Log.d("json", "name: "+name);
				Log.d("json", "version: "+version);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseJSONWithGSON(String jsonData){
		Gson gson= new Gson();
//		List<App> appList=gson.fromJson(jsonData, new TypeToken<List<App>>() {}.getType());
//		for(App app : appList){
//			Log.d("gson", "id: "+app.getId());
//			Log.d("gson", "name: "+app.getName());
//			Log.d("gson", "version: "+app.getVetsion());
//			
//		}
	}
	public static List<RoomBean> parseJsonForRoomBean(String jsonData){
		List<RoomBean> RoomBeans = new ArrayList<RoomBean>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonData);
			//获取result数组，里面都所有的房型
			JSONArray resultarray = jsonObject.getJSONArray("result");
			for(int i =0;i<resultarray.length();i++){
				RoomBean rBean = new RoomBean();
				JSONObject roomJsonObject = resultarray.getJSONObject(i);
				rBean.setRoomName(roomJsonObject.getString("roomName"));
				rBean.setArea(roomJsonObject.getString("area"));
				rBean.setBed(roomJsonObject.getString("bed"));
				JSONArray policyInfos = roomJsonObject.getJSONArray("policyInfo");
				JSONObject policyInfoJsonObject = policyInfos.getJSONObject(0);
				rBean.setPrice(policyInfoJsonObject.getString("price"));
				rBean.setBreakfast(policyInfoJsonObject.getString("breakfast"));
				
				RoomBeans.add(rBean);
				System.out.println(rBean.toString());
			}
			
		return RoomBeans;

			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		
	}
	
	public static List<HotelBean> parseJsonForHotelBean(String jsonData){
		//System.out.println(jsonData);
		Gson gson= new Gson();
		List<HotelBean> hotelList=gson.fromJson(jsonData, new TypeToken<List<HotelBean>>() {}.getType());
		for(HotelBean app : hotelList){
//			Log.d("gson", "id: "+app.getId());
//			Log.d("gson", "name: "+app.getName());
//			Log.d("gson", "version: "+app.getAddress());
//			
		}
		return hotelList;
	}
	public static List<SceneryBean> parseJsonForSceneryBean(String jsonData){
//		System.out.println(jsonData);
		Gson gson= new Gson();
		List<SceneryBean> hotelList=gson.fromJson(jsonData, new TypeToken<List<SceneryBean>>() {}.getType());
		for(SceneryBean app : hotelList){
//			Log.d("gson", "id: "+app.getId());
//			Log.d("gson", "name: "+app.getName());
//			Log.d("gson", "version: "+app.getAddress());
			
		}
		return hotelList;
	}
	public static List<TicketBean> parseJsonForTicketBean(String jsonData){
		//System.out.println(jsonData);
		Gson gson= new Gson();
		List<TicketBean> hotelList=gson.fromJson(jsonData, new TypeToken<List<TicketBean>>() {}.getType());
		for(TicketBean app : hotelList){
//			Log.d("gson", "id: "+app.getId());
//			Log.d("gson", "name: "+app.getName());
			//Log.d("gson", "version: "+app.getAddress());
			
		}
		return hotelList;
	}
	
	
	public void parse(String jsonData){
		try {
			JSONArray jsonArray=new JSONArray(jsonData);
			for(int i = 0;i<jsonArray.length();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String name = jsonObject.getString("name");
				String version = jsonObject.getString("version");
				Log.d("json", "id: "+id);
				Log.d("json", "name: "+name);
				Log.d("json", "version: "+version);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}

package com.example.demo_zizhuyou;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MapFunActivity extends Activity implements OnClickListener {

	private WebView webView;
	private String provider;

	private EditText inpuText;
	private Button button;

	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			closeProgressDialog();
			webView.loadUrl("http://m.amap.com/navi/?dest="
			+ getLongitudeSix(location)
			+ ","
			+ getLatitudeSix(location)
			+ "&destName=当前位置&hideRouteIcon=1&key=e2684b2d32b921a419c1d889857744d3");

		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_fun);
		
		init();
		initLocationManager();
		setLocationListener();
		
		getLoactionWithWhile();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setLocationListener();

	}

	@Override  
	protected void onPause() {  
	    super.onPause();  
	    locationManager.removeUpdates(locationListener);  
	}  
	
	void init() {
		inpuText = (EditText) findViewById(R.id.input);
		button = (Button) findViewById(R.id.search);
		button.setOnClickListener(this);
		webView = (WebView) findViewById(R.id.web_view);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

		});
	}

	/*
	 * 定义两个对象
	 */
	private LocationManager locationManager;
	LocationListener locationListener;
	Location location;

	/**
	 * 初始化位置管理器等
	 */
	void initLocationManager() {
		// 初始化 位置管理器
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//
		// 尝试获取 位置提供器
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
			
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		} else {
			Toast.makeText(this, "无法获取定位", Toast.LENGTH_SHORT).show();

		}

	}

	void setLocationListener() {
		locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				// showLocation(location);
			}
		};

		// 设置位置监听器
		locationManager.requestLocationUpdates(provider, 5000, 1,
				locationListener);
	}

	
	void getLoactionWithWhile(){
		showProgressDialog();
		location = locationManager.getLastKnownLocation("gps");
		
		new Thread(){
			@Override
			public void run() {
				while(location  == null)  
				{  
				  locationManager.requestLocationUpdates("gps", 60000, 1, locationListener);  
				  location = locationManager.getLastKnownLocation("gps");
				} 
				mHandler.sendEmptyMessage(1);
			}
			
		}.start();
	}
	
	/**
	 * 获取位置实例
	 * 
	 * @param locationManager
	 */
	Location getLocation(LocationManager locationManager) {
		// String provider;
		if(provider!=null){
			Location location = locationManager.getLastKnownLocation(provider);
			return location;
		}else{
			// 尝试获取 位置提供器
			List<String> providerList = locationManager.getProviders(true);
			if (providerList.contains(LocationManager.GPS_PROVIDER)) {
				provider = LocationManager.GPS_PROVIDER;
			} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
				provider = LocationManager.NETWORK_PROVIDER;
			} else {
				Toast.makeText(this, "无法获取定位", Toast.LENGTH_SHORT).show();
				return null;
			}

			Location location = locationManager.getLastKnownLocation(provider);
			if (location != null) {
				return location;
			}
			Toast.makeText(this, "获取位置失败", Toast.LENGTH_SHORT).show();
			return null;
		}
		
	}

	/**
	 * 纬度
	 * 
	 * @param location
	 * @return
	 */
	String getLatitudeSix(Location location) {
		String double_str = String.format("%.6f", location.getLatitude());

		return double_str;
	}

	/**
	 * 经度
	 */
	String getLongitudeSix(Location location) {
		String double_str = String.format("%.6f", location.getLongitude());

		return double_str;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String s = inpuText.getText().toString();

		// webView.loadUrl("http://m.amap.com/navi/?dest="+getLongitudeSix(aLocation)+","+getLatitudeSix(aLocation)+"&destName=当前位置&hideRouteIcon=1&key=e2684b2d32b921a419c1d889857744d3");
		Location aLocation = getLocation(locationManager);
		webView.loadUrl("http://m.amap.com/around/?locations="
				+ getLongitudeSix(aLocation)
				+ ","
				+ getLatitudeSix(aLocation)
				+ "&keywords="
				+ s
				+ "&defaultIndex=1&defaultView=&searchRadius=20000&key=e2684b2d32b921a419c1d889857744d3");
	}

	private ProgressDialog progressDialog;
	
	/**
	 * 显示进度对话框
	 */
	public void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在定位...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	/**
	 * 关闭进度对话框
	 */
	public void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
}

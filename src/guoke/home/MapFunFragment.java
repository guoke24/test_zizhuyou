package guoke.home;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.example.demo_zizhuyou.R;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import guoke.fragment.BaseFragment;

public class MapFunFragment extends BaseFragment implements OnClickListener{

	double LongitudeSix;
	double LatitudeSix;
	double curLongitudeSix;
	double curLatitudeSix;
	boolean isload=false;
	
	View inputView;
	
	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	//声明定位回调监听器
	public AMapLocationListener mLocationListener = new AMapLocationListener(){

		public void onLocationChanged(AMapLocation amapLocation) {
		    if (amapLocation != null) {
		        if (amapLocation.getErrorCode() == 0) {
		        //定位成功回调信息，设置相关消息
		        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
		        LatitudeSix = amapLocation.getLatitude();//获取纬度
		        LongitudeSix = amapLocation.getLongitude();//获取经度
		        mHandler.sendEmptyMessage(4);
		        
		        
//		        amapLocation.getAccuracy();//获取精度信息
//		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		        Date date = new Date(amapLocation.getTime());
//		        df.format(date);//定位时间
//		        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//		        amapLocation.getCountry();//国家信息
//		        amapLocation.getProvince();//省信息
//		        amapLocation.getCity();//城市信息
//		        amapLocation.getDistrict();//城区信息
//		        amapLocation.getStreet();//街道信息
//		        amapLocation.getStreetNum();//街道门牌号信息
//		        amapLocation.getCityCode();//城市编码
//		        amapLocation.getAdCode();//地区编码
//		        amapLocation.getAOIName();//获取当前定位点的AOI信息
		    } else {
		              //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
		        Log.e("AmapError","location Error, ErrCode:"
		            + amapLocation.getErrorCode() + ", errInfo:"
		            + amapLocation.getErrorInfo());
		        }
		    }
		}
		
	};
	
	//声明mLocationOption对象
	public AMapLocationClientOption mLocationOption = null;
	
	//
	
	private WebView webView;
	private String provider;

	private EditText inpuText;
	private Button button;

	private Location location;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				toast("location为空？："+(location==null));
				closeProgressDialog();
				webView.loadUrl("http://m.amap.com/navi/?dest="
				+ getLongitudeSix(location)
				+ ","
				+ getLatitudeSix(location)
				+ "&destName=当前位置&hideRouteIcon=1&key=e2684b2d32b921a419c1d889857744d3");
				break;
			case 4:
				//toast("成功"+LatitudeSix+","+LongitudeSix);
				if(!isload){
					isload = true ;
					curLatitudeSix = LatitudeSix;
					curLongitudeSix = LongitudeSix;
					webView.loadUrl("http://m.amap.com/navi/?dest="
							+ curLongitudeSix
							+ ","
							+ curLatitudeSix
							+ "&destName=当前位置&hideRouteIcon=1&key=e2684b2d32b921a419c1d889857744d3");				
				}
				
				break;
			default:
				break;
			}

		}
		
	};
	
	void start(){
		//初始化定位
		mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
		//设置定位回调监听
		mLocationClient.setLocationListener(mLocationListener);
			
		//初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		//设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		//设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		//设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		//设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		//设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(60000);//一分钟
		
		//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		//启动定位
		mLocationClient.startLocation();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_map_fun, null);
		
		start();
		
		init(view);
		//initLocationManager();
		//setLocationListener();
		
		//getLoactionWithWhile();
		
		
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		//setLocationListener();

	}

	@Override
	public void onPause() {  
	    super.onPause();  
	    //locationManager.removeUpdates(locationListener); 
	    mLocationClient.stopLocation();//停止定位
	}  
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mLocationClient.onDestroy();//销毁定位客户端。
		super.onDestroy();
	}
	
	void init(View v) {
		inputView = v.findViewById(R.id.map_input_layout);
		
		inpuText = (EditText) v.findViewById(R.id.input);
		
		inputView.clearFocus();
		
		button = (Button) v.findViewById(R.id.search);
		button.setOnClickListener(this);
		webView = (WebView) v.findViewById(R.id.web_view);
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
	Location mlocation;

	/**
	 * 初始化位置管理器等
	 */
	void initLocationManager() {
		// 初始化 位置管理器
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		//
		// 尝试获取 位置提供器
		List<String> providerList = locationManager.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
			toast("provider："+provider);
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
			toast("provider："+provider);
		} else {
			Toast.makeText(getActivity(), "无法获取定位", Toast.LENGTH_SHORT).show();

		}

	}

	/**
	 * 实例化监听对象，并设置监听，通过locationManager来设置监听
	 */
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
				mlocation = location;
				mHandler.sendEmptyMessage(3);
			}
		};

		// 设置位置监听器
		locationManager.requestLocationUpdates(provider, 500, 1,
				locationListener);
	}

	int cout = 0;
	void getLoactionWithWhile(){
		setLocationListener();
		//showProgressDialog();
		
		location = locationManager.getLastKnownLocation(provider);
		new Thread(){
			@Override
			public void run() {
				while(location  == null)  
				{  
				  cout++;
				  locationManager.requestLocationUpdates("gps", 500, 1, locationListener);  

				  try {
					Thread.sleep(500);
						
						location = locationManager.getLastKnownLocation(provider);

					} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
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
				Toast.makeText(getActivity(), "无法获取定位", Toast.LENGTH_SHORT).show();
				return null;
			}

			Location location = locationManager.getLastKnownLocation(provider);
			if (location != null) {
				return location;
			}
			Toast.makeText(getActivity(), "获取位置失败", Toast.LENGTH_SHORT).show();
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
		inputView.clearFocus();
		// webView.loadUrl("http://m.amap.com/navi/?dest="+getLongitudeSix(aLocation)+","+getLatitudeSix(aLocation)+"&destName=当前位置&hideRouteIcon=1&key=e2684b2d32b921a419c1d889857744d3");
		//Location aLocation = getLocation(locationManager);
		if(isload){
			webView.loadUrl("http://m.amap.com/around/?locations="
					+ curLongitudeSix
					+ ","
					+ curLatitudeSix
					+ "&keywords="
					+ s
					+ "&defaultIndex=1&defaultView=&searchRadius=5000&key=e2684b2d32b921a419c1d889857744d3");		
		}else{
			
		}
		inputView.clearFocus();
}

//	private ProgressDialog progressDialog;
//	
//	/**
//	 * 显示进度对话框
//	 */
//	public void showProgressDialog() {
//		if (progressDialog == null) {
//			progressDialog = new ProgressDialog(this);
//			progressDialog.setMessage("正在定位...");
//			progressDialog.setCanceledOnTouchOutside(false);
//		}
//		progressDialog.show();
//	}
//	
//	/**
//	 * 关闭进度对话框
//	 */
//	public void closeProgressDialog() {
//		if (progressDialog != null) {
//			progressDialog.dismiss();
//		}
//	}
	
	
}

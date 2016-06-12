package httpUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import intetface.HttpCallbackListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.Message;

public class HttpUtil {
	public static void sendRequestWithHttpClient(final String url,
			final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					HttpClient httpClient=new DefaultHttpClient();
					//HttpGet httpGet=new HttpGet(url);
					//HttpResponse httpResponse = httpClient.execute(httpGet);
					HttpPost httpPost = new HttpPost(url);
					//set the post data
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("account","admin"));
					params.add(new BasicNameValuePair("password","123"));
					UrlEncodedFormEntity uefentity = new UrlEncodedFormEntity(params,"UTF-8");
					httpPost.setEntity(uefentity);
					//execute the commit
					HttpResponse httpResponse = httpClient.execute(httpPost);
					
					
					String response="";
					if(httpResponse.getStatusLine().getStatusCode()==200){
						HttpEntity entity = httpResponse.getEntity();
						response = EntityUtils.toString(entity, "utf-8");
					}
					//
					if(listener!=null){
						listener.onFinish(response );
					}
					
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					if(listener!=null){
						listener.onError(e);
					}
				}
				
			}
		}).start();
	}
	
	public static void sendRequestWithHttpConnection(final String urlStr,
			final HttpCallbackListener listener){
		//
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("run����");
				// TODO Auto-generated method stub
				HttpURLConnection connection=null;
				try{
					URL url=new URL(urlStr);
					//
					connection = (HttpURLConnection) url.openConnection();
					//
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					//
					InputStream in  = connection.getInputStream();
					//
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response =new StringBuilder();
					String line ;
					while((line=reader.readLine())!=null){
						response.append(line);
					}
					//请求结束的回调函数
					if(listener!=null){
						listener.onFinish(response.toString());
					}
				}catch(Exception e){
					e.printStackTrace();
					//发生错误的回调函数
					if(listener!=null){
						listener.onError(e);
					}
				}finally{
					if(connection!=null)
						connection.disconnect();
				}
			}
		}).start();
		
	}
	
}

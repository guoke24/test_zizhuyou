package com.example.demo_zizhuyou;




import cn.bmob.v3.Bmob;






import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LauchActivity extends Activity {

	private static final long DALAY_TIME=2000L;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lauch);
	
	
		redirectByTime();
	}
	private void redirectByTime(){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				redictToActivity(LauchActivity.this,LoginActivity.class,null);
				finish();
			}
		},DALAY_TIME);
	}
	/**
	 * Activity跳转
	 * @param context
	 * @param targetActivity
	 * @param bundle
	 */
	public void redictToActivity(Context context,Class<?> targetActivity,Bundle bundle){
		Intent intent = new Intent(context, targetActivity);
		if(null != bundle){
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
}

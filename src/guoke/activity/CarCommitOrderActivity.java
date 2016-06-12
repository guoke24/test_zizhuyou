package guoke.activity;

import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.id;
import com.example.demo_zizhuyou.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CarCommitOrderActivity extends Activity implements OnClickListener{

	private Button commitToCarOrder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_car_service_select);
		setDialogSize();
		init();
		setListener();
	}

	private void init(){
		commitToCarOrder = (Button)findViewById(R.id.commit_carOrder_in_reserve);
	}
	
	private void setListener(){
		commitToCarOrder.setOnClickListener(this);
	}
	/**
	 * 手动设置该activity所在的窗口的大小
	 */
	private void setDialogSize(){
		WindowManager m = getWindowManager();    
	    Display d = m.getDefaultDisplay();  //为获取屏幕宽、高    
	    android.view.WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值    
	    //p.height = (int) (d.getHeight() * 0.7);   //高度设置为屏幕的1.0   
	    p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.8   
	    p.alpha = 1.0f;      //设置本身透明度  
	    p.dimAmount = 0.0f;      //设置黑暗度  
	    getWindow().setAttributes(p); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit_carOrder_in_reserve:
			commitToCarOrder_click();
			break;

		default:
			break;
		}
	}

	private void commitToCarOrder_click(){
		Intent intent = new Intent();
		intent.setClass(this, CarReserveActivity.class);
		startActivity(intent);
		finish();
	}
}

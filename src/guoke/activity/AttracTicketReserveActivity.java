package guoke.activity;

import guoke.bean.SceneryBean;
import guoke.bean.TicketBean;
import guoke.home.HomeActivity;
import guoke.model.AttractionsItem;
import guoke.model.InfoCom;
import guoke.model.MDate;
import guoke.model.MRegion;
import guoke.model.MyUser;
import guoke.model.TicketItem;
import guoke.model.TicketOrder;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.a.a.there;
import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.layout;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AttracTicketReserveActivity extends BaseActivity implements OnClickListener{

	private TextView ticketNameTextView;
	private TextView attracNameTextView;
	private TextView ticketDetailTextView;
	
	private EditText username;
	private EditText userphone;
	private TextView ticketnum;
	
	private TextView attracRegionTextView;
	
	private TextView priceTextView;
	private Button commitButton;
	private Button numreduceButton;
	private Button numaddButton;
	
	private Intent source_intent;
	private MDate bgDate;
	private MDate fnDate;
	private MRegion dsCityRegion;	
	private SceneryBean attrac_select;
	private TicketBean ticket_select;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attrac_ticket_reserve);
		getSourceIntent();
		init();
		setListener();
	}

	void getSourceIntent(){
		source_intent = getIntent();
		bgDate = (MDate) source_intent.getSerializableExtra(InfoCom.Extra_PlayDate);
		fnDate = (MDate) source_intent.getSerializableExtra(InfoCom.Extra_FinPlayDate);
		dsCityRegion = (MRegion) source_intent.getSerializableExtra(InfoCom.Extra_PlayCitySelect);
		attrac_select = (SceneryBean) source_intent.getSerializableExtra(InfoCom.Extra_AttracSelect);
		ticket_select = (TicketBean) source_intent.getSerializableExtra(InfoCom.Extra_TicketSelect);
	}
	
	void init(){
		ticketNameTextView = (TextView) findViewById(R.id.ticket_reserve_name);
		ticketNameTextView.setText(ticket_select.getName());
		
		attracNameTextView = (TextView) findViewById(R.id.ticket_reserve_attracname);
		attracNameTextView.setText(attrac_select.getTitle());
		
		ticketDetailTextView = (TextView) findViewById(R.id.ticket_reserve_detail);
		//ticketDetailTextView.setText(ticket_select.getDatail());
		
		username = (EditText) findViewById(R.id.ticket_reserve_user_name_input);
		userphone = (EditText) findViewById(R.id.ticket_reserve_user_phone_input);
		ticketnum = (TextView) findViewById(R.id.ticket_reserve_ticket_number);
		ticketnum.setText("1");
		
		attracRegionTextView = (TextView) findViewById(R.id.ticket_reserve_region);
		attracRegionTextView.setText(attrac_select.getAddress());
		
		priceTextView = (TextView) findViewById(R.id.ticket_reserve_price);
		countPrice();
		
		commitButton = (Button) findViewById(R.id.ticket_reserve_commit);
		numaddButton =(Button) findViewById(R.id.ticket_num_jia);
		numreduceButton = (Button) findViewById(R.id.ticket_num_jian);
	}
	
	void setListener(){
		commitButton.setOnClickListener(this);
		numaddButton.setOnClickListener(this);
		numreduceButton.setOnClickListener(this);
		
		ticketnum.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int num =1;
//				if(s.length()!=0){
//					try {
//						num = Integer.valueOf(s.toString());
//					} catch (Exception e) {
//						
//					}
//					if(num>5){
//						toast("最多能定5张票");
//						commitButton.setClickable(false);
//						commitButton.setAlpha((float) 0.3);
//						priceTextView.setText("");
//					}else{
//						commitButton.setClickable(true);
//						commitButton.setAlpha((float) 1);
//						countPrice();
//					}
//				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
	}

	/**
	 * 计算总价钱，并且返回价钱的double类型
	 * @return
	 */
	Double countPrice(){
		int num = Integer.valueOf(ticketnum.getText().toString());
		double fee = num * ticket_select.getPrice();
		priceTextView.setText("￥"+String.valueOf(fee));
		return fee;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ticket_num_jian:
			int n = Integer.valueOf(ticketnum.getText().toString());
			if(n>1){
				ticketnum.setText((--n)+"");
				countPrice();
			}
			break;
		case R.id.ticket_num_jia:
			int m = Integer.valueOf(ticketnum.getText().toString());
			if(m<5){
				ticketnum.setText((++m)+"");
				countPrice();
			}else{
				toast("最多能定5张票");
			}
			break;
		case R.id.ticket_reserve_commit:
			commit();
			break;
		default:
			break;
		}
		
	}
	
	void commit(){
		if(isInputValidate()){
			showProgressDialog();
			MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
			if(myUser != null){
				TicketOrder ticketOrder = new TicketOrder();
				ticketOrder.setUser_id(myUser.getObjectId());
				//ticketOrder.setTicket_id(ticket_select.getObjectId());
				ticketOrder.setTicket_name(ticket_select.getName());
				ticketOrder.setAttrac_id(attrac_select.getSid());
				ticketOrder.setAttrac_name(attrac_select.getTitle());
				ticketOrder.setAttrac_address(attrac_select.getAddress());
				//记录订票人和手机
				ticketOrder.setUsername(username.getText().toString());
				ticketOrder.setUserphone(userphone.getText().toString());
				ticketOrder.setNum(Integer.valueOf(ticketnum.getText().toString()));
				//设置单价和总费用
				ticketOrder.setPrice(ticket_select.getPrice());
				ticketOrder.setSumfee(countPrice());
				//ticketOrder.setStaute("进行中");
				ticketOrder.setStatues(0);
				ticketOrder.save(this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						//toast("提交成功");
						closeProgressDialog();
						onSuccess_reserve();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						closeProgressDialog();
						toast("提交失败，网络故障");
					}
				});
				
			}else{
				closeProgressDialog();
				toast("未登录，请先登录");
			}
			closeProgressDialog();
		}else{
			closeProgressDialog();
			toast("取票人和手机号不能为空！");
		}
	}
	
	/**
	 * 判断入住人和手机是否为空
	 * @return
	 */
	private Boolean isInputValidate(){
		//如果两个输入框都有值，就返回真 
		if(!username.getText().toString().equals("")&&
		   !userphone.getText().toString().equals("")){
			if(userphone.getText().toString().length()==11){
				return true;
			}else{
				toast("手机号必须为11位！");
				return false;
			}
			
		}else{
			toast("取票人和手机号都要填写哦！");
			return false;
		}
	}
	/**
	 * 预订成功的时候调用
	 */
	protected void onSuccess_reserve() {
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle("");
		dialog.setMessage("预订成功");
		dialog.setNegativeButton("返回主界面", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setClass(AttracTicketReserveActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
//		dialog.setPositiveButton("停留在订单页", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		});
//		
		dialog.show();
	}
	
}

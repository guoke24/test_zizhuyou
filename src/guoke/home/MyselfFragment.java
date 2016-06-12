package guoke.home;

import guoke.activity.OrderCarListActivity;
import guoke.activity.OrderCateListActivity;
import guoke.activity.OrderTicketListActivity;
import guoke.model.InfoCom;
import guoke.model.MyUser;
import guoke.orderlist.RoomOrderListActivity;
import cn.bmob.v3.BmobUser;

import com.example.demo_zizhuyou.AlterPawActivity;
import com.example.demo_zizhuyou.AlterUsernameActivity;
import com.example.demo_zizhuyou.LoginActivity;
import com.example.demo_zizhuyou.MapFunActivity;
import com.example.demo_zizhuyou.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MyselfFragment extends Fragment implements OnClickListener{

	private Button signOutButton;//登出按钮
	private Button signInButton;//登陆按钮
	
	private RelativeLayout signoutLayout;//登出后显示的布局
	private TableLayout signinLayout;//登陆后显示的布局
	private TextView signin_usernameTextView;//登陆后的用户名
	private TextView signin_userphoneTextView;//登陆后的手机号
	
	private TextView hotelorderTextView;//点击了酒店订单
	private TableRow cateordeRow;//点击查看 美食订单
	private TableRow cardeRow;//点击查看 美食订单
	private TableRow ticketordeRow;//点击查看 美食订单
	
	
	private Button mapfunbButton;
	private View alterPawTextView;
	private View accountTextView;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myselfLayout = inflater.inflate(R.layout.homefragment_myself_layout,
				container, false);
		init(myselfLayout);
		setListener();
		checkIsLogin();
		return myselfLayout;
	}

	
	
	@Override
	public void onResume() {
		//再次检查是否登录
		checkIsLogin();
		super.onResume();
	}



	/**
	 * 在界面创建的时候调用,根据登陆和登出情况来更新
	 */
	Boolean checkIsLogin(){
		MyUser myUser = BmobUser.getCurrentUser(getActivity(),MyUser.class);
		if(myUser != null){
			//让【登出】布局 和【登陆】按钮消失
			signoutLayout.setVisibility(View.GONE);
			signInButton.setVisibility(View.GONE);
			//让【登陆】布局 和 【注销】按钮显示，
			signinLayout.setVisibility(View.VISIBLE);
			signOutButton.setVisibility(View.VISIBLE);
			//给 用户名 和 手机 的两个控件赋值
			signin_usernameTextView.setText(myUser.getUsername());
			signin_userphoneTextView.setText(myUser.getMobilePhoneNumber());
			
			return true;
			
		}else{
			//让登出布局 和 登陆按钮显示，
			signoutLayout.setVisibility(View.VISIBLE);
			signInButton.setVisibility(View.VISIBLE);
			//让登陆布局 和 注销按钮消失
			signinLayout.setVisibility(View.GONE);
			signOutButton.setVisibility(View.GONE);
			
			return false;
		}
	}
	
	void init(View v){
		signOutButton = (Button) v.findViewById(R.id.myself_signout_button);
		signInButton = (Button)v.findViewById(R.id.myself_signin_button);
		signinLayout = (TableLayout) v.findViewById(R.id.myself_userLogin_content);
		signoutLayout = (RelativeLayout)v.findViewById(R.id.myself_userLogout_content);
		signin_usernameTextView = (TextView) v.findViewById(R.id.myself_login_username);
		signin_userphoneTextView = (TextView) v.findViewById(R.id.myself_login_userphone);
		//点击去订单列表
		hotelorderTextView = (TextView) v.findViewById(R.id.myself_hotel_order);
		cateordeRow = (TableRow) v.findViewById(R.id.orderList_cate);
		cardeRow = (TableRow) v.findViewById(R.id.orderList_car);
		ticketordeRow = (TableRow) v.findViewById(R.id.orderList_ticket);
		
		mapfunbButton = (Button) v.findViewById(R.id.mapfun);
		alterPawTextView = (View) v.findViewById(R.id.myself_alter_paw);
		accountTextView = (View) v.findViewById(R.id.myself_account_set);
	}

	void setListener(){
		signInButton.setOnClickListener(this);
		signOutButton.setOnClickListener(this);
		
		hotelorderTextView.setOnClickListener(this);
		cateordeRow.setOnClickListener(this);
		cardeRow.setOnClickListener(this);
		ticketordeRow.setOnClickListener(this);
		
		mapfunbButton.setOnClickListener(this);
		alterPawTextView.setOnClickListener(this);
		accountTextView.setOnClickListener(this);
	}
	
	private void signOut_click(){
		BmobUser.logOut(getActivity());
		checkIsLogin();
	}
	private void signIn_click(){
		Intent intent = new Intent();
		intent.putExtra(InfoCom.fromWhere, InfoCom.fromMyselfFragment);
		intent.setClass(getActivity(), LoginActivity.class);
		getActivity().startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {

		//如果是点击了登陆或者注销按钮就不需要经过这个登陆判断
		if(v.getId()!=R.id.myself_signout_button
		 &&v.getId()!=R.id.myself_signin_button){
			if(checkIsLogin()){
				 
			 }else{
				 toast("未登录，请先登录");
				 
				 return ;
			 }
		}
		
		switch (v.getId()) {
		case R.id.myself_account_set:
			Intent intent22 = new Intent();
			intent22.setClass(getActivity(), AlterUsernameActivity.class);
			getActivity().startActivity(intent22);
			break;

		case R.id.myself_signout_button:
			signOut_click();
			break;
		case R.id.myself_signin_button:
			signIn_click();
			break;
		case R.id.myself_hotel_order:
			hotelOrder_click();
			break;
		case R.id.mapfun:
			Intent intent = new Intent();
			intent.setClass(getActivity(), MapFunActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.myself_alter_paw:
			Intent pIntent = new Intent();
			pIntent.setClass(getActivity(), AlterPawActivity.class);
			startActivity(pIntent);
			break;
		case R.id.orderList_cate:
			Intent intent2 = new Intent();
			intent2.setClass(getActivity(), OrderCateListActivity.class);
			startActivity(intent2);
			break;
		case R.id.orderList_car:
			Intent intent3 = new Intent();
			intent3.setClass(getActivity(), OrderCarListActivity.class);
			startActivity(intent3);
			break;
		case R.id.orderList_ticket:
			Intent intent4 = new Intent();
			intent4.setClass(getActivity(), OrderTicketListActivity.class);
			startActivity(intent4);
			break;
		default:
			break;
		}
	}
	
	 void hotelOrder_click(){
		 
		Intent intent = new Intent();
		intent.setClass(getActivity(), RoomOrderListActivity.class);
		startActivity(intent);
		 
	}
	
	 void toast(String s){
		 Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
	 }
}

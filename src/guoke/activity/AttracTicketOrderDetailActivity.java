package guoke.activity;

import com.example.demo_zizhuyou.BaseActivity;
import com.example.demo_zizhuyou.R;
import com.example.demo_zizhuyou.R.id;
import com.example.demo_zizhuyou.R.layout;

import guoke.model.InfoCom;
import guoke.model.TicketOrder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AttracTicketOrderDetailActivity extends BaseActivity {

	private TextView ticketNameTextView;
	private TextView attracNameTextView;
	private TextView ticketDetailTextView;
	
	private TextView username;
	private TextView userphone;
	private TextView ticketnum;
	
	private TextView attracRegionTextView;
	
	private TextView priceTextView;
	private Button commitButton;
	private Button numreduceButton;
	private Button numaddButton;
	
	private Intent source_intent;
	private TicketOrder ticketOrder;
	
	void getSource_intent(){
		source_intent = getIntent();
		ticketOrder = (TicketOrder) source_intent.getSerializableExtra(InfoCom.Extra_TicketOrderSelect);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attrac_ticket_order_detail);
		getSource_intent();
		init();
		
	}

	void init(){
		ticketNameTextView = (TextView) findViewById(R.id.ticket_reserve_name);
		ticketNameTextView.setText(ticketOrder.getTicket_name());
		
		attracNameTextView = (TextView) findViewById(R.id.ticket_reserve_attracname);
		attracNameTextView.setText(ticketOrder.getAttrac_name());
		
		ticketDetailTextView = (TextView) findViewById(R.id.ticket_reserve_detail);
		//ticketDetailTextView.setText(ticket_select.getDatail());
		
		username = (TextView) findViewById(R.id.ticket_reserve_user_name_input);
		username.setText(ticketOrder.getUsername());
		userphone = (TextView) findViewById(R.id.ticket_reserve_user_phone_input);
		userphone.setText(ticketOrder.getUserphone());
		ticketnum = (TextView) findViewById(R.id.ticket_reserve_ticket_number);
		ticketnum.setText(""+ticketOrder.getNum());
		
		attracRegionTextView = (TextView) findViewById(R.id.ticket_reserve_region);
		attracRegionTextView.setText(ticketOrder.getAttrac_address());
		
		priceTextView = (TextView) findViewById(R.id.ticket_reserve_price);
		priceTextView.setText("￥"+ticketOrder.getSumfee());
		
		commitButton = (Button) findViewById(R.id.ticket_reserve_commit);
		commitButton.setText("");
		numaddButton =(Button) findViewById(R.id.ticket_num_jia);
		numreduceButton = (Button) findViewById(R.id.ticket_num_jian);
	}
	

}

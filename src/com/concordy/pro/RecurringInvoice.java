package com.concordy.pro;

import com.concordy.pro.R;
import com.concordy.pro.bean.RecurringSetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RecurringInvoice extends Activity implements OnClickListener{
	
	private Spinner spinner;
	private static final String[] m={"Monthly","Weekly","Monthly","Annually"};
	private ArrayAdapter<String> adapter;
	private Button schedule;
	private EditText interval,startDate,endDate,endontimes;
	private RecurringSetting rs;
	private int code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recurring_invoice);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		interval = (EditText) findViewById(R.id.Interval);
		startDate = (EditText) findViewById(R.id.startDate);
		endDate = (EditText) findViewById(R.id.endDate);
		endontimes = (EditText) findViewById(R.id.endOnTimes);
		
		spinner = (Spinner) findViewById(R.id.Spinner);
		schedule = (Button) findViewById(R.id.schedulebtn);
		schedule.setOnClickListener(this);
		
		//将可选内容与ArrayAdapter连接起来
				adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m); 
				//设置下拉列表的风格
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				//将adapter 添加到spinner中
				spinner.setAdapter(adapter);
				//添加事件Spinner事件监听  
				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						code = position+1;
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						
					}
				});
				//设置默认值
				spinner.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		rsvalue();
	}

	/**
	 * 
	 */
	public void rsvalue() {
		rs = new RecurringSetting();
		String ones = interval.getText().toString();
		System.out.println("------------------"+ones);
		int one = Integer.parseInt(interval.getText().toString());
		String two = startDate.getText().toString();
		String three = endDate.getText().toString();
	    int four= Integer.parseInt(endontimes.getText().toString());
	    rs.setInterval(one);
	    rs.setStartDate(two);
	    rs.setEndDate(three);
	    rs.setEndOnTimes(four);
	    rs.setRepeatBy(code);
	    rs.setEndsBy(1);
	    rs.setRepeatOn(1);
	    Intent intent = getIntent();
	    Bundle bundle = new Bundle();
	    bundle.putSerializable("RS", rs);
	    intent.putExtras(bundle);
	    setResult(90, intent);//响应码
	    System.out.println("即将发送的数据:"+rs.toString());
	    finish();
	}
	
}
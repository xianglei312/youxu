﻿package com.concordy.pro;


/**
 * 欢迎页面
 * shenchenyu 
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.concordy.pro.R;
import com.concordy.pro.http.HttpHelper;
import com.concordy.pro.http.HttpHelper.HttpResult;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.PromptManager;
import com.concordy.pro.utils.SharedPreferencesUtils;
import com.concordy.pro.utils.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class WelcomeActivity extends Activity implements OnClickListener {
	private Context ct;
	@ViewInject(R.id.gv_home)
	private GridView mGridView;
	private WelcomeAdapter adapter;
	@ViewInject(R.id.tv_link_signin)
	private TextView mSignIn;
	@ViewInject(R.id.tv_link_signup)
	private TextView mSignUp;
	private static String[] mGvWelcome;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome_activity);
		ViewUtils.inject(this);
		init();
//		----------------leo------------------------
	}
	
	
	private void init() {
		ct = getApplicationContext();
		//4个导航内容
		mGvWelcome = new String[] {
				getResources().getString(R.string.gv_txt_1),
				getResources().getString(R.string.gv_txt_2),
				getResources().getString(R.string.gv_txt_3),
				getResources().getString(R.string.gv_txt_4), };
		adapter = new WelcomeAdapter();
		// 设置GridView适配器
		mGridView.setAdapter(adapter);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
//					跳转到invoice发票  ，通过intent
					Intent intent = new Intent(ct,InvoiceActivity.class);
					startActivity(intent);
					break;
				case 1:
					PromptManager.showToast(ct, R.string.gv_txt_2);
					break;
				case 2:
					PromptManager.showToast(ct, R.string.gv_txt_3);
					break;
				case 3:
					PromptManager.showToast(ct, R.string.gv_txt_4);
					break;
				}
			}
		});
		mSignIn.setOnClickListener(this);
		//注册
		mSignUp.setOnClickListener(this);
	}
	class WelcomeAdapter extends BaseAdapter {
		private TextView tv;
		@Override
		public int getCount() {
			return mGvWelcome.length;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(WelcomeActivity.this,
					R.layout.item_welcome, null);
			ViewUtils.inject(view);
			tv = (TextView) view.findViewById(R.id.tv_item_wel);
			tv.setText(mGvWelcome[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return mGvWelcome[position];
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.tv_link_signin://登陆
			intent = new Intent(ct, LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_link_signup://注册
			intent = new Intent(ct, RegisterActivity.class);
			startActivity(intent);
			break;	
		}	
	}
}

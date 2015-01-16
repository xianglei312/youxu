package com.concordy.pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.concordy.pro.R;
import com.concordy.pro.receiver.SleepStateService;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.SharedPreferencesUtils;
import com.concordy.pro.utils.StringUtils;
import com.lidroid.xutils.ViewUtils;

public class SplashActivity extends Activity implements OnClickListener {
	private Intent service;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		init();
	}
	private void init() {
		ViewUtils.inject(this);
		String token = SharedPreferencesUtils.getString(this, ContentValue.SPFILE_TOKEN, "");
		String pin = SharedPreferencesUtils.getString(this, ContentValue.SPFILE_PIN, "");
		boolean PINService = SharedPreferencesUtils.getBoolean(this, ContentValue.SPFILE_PIN_TOGGLE, false);
		Intent intent;
		if(PINService){
			service = new Intent(getApplicationContext(), SleepStateService.class);
			startService(service);
		}
		if(!StringUtils.isEmpty(token)){
			if(!StringUtils.isEmpty(pin))
				intent = new Intent(this, PinLoginActivity.class);
			else
				intent = new Intent(this, LoginActivity.class);
		}else{
			intent = new Intent(this,WelcomeActivity.class);
		}
		startActivity(intent);
	}
	@Override
	public void onClick(View v) {
	}
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
}

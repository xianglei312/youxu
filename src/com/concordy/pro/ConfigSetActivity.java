package com.concordy.pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.concordy.pro.receiver.SleepStateService;
import com.concordy.pro.ui.ItemClickView;
import com.concordy.pro.ui.ToggleButton;
import com.concordy.pro.ui.ToggleButton.OnToggleButtonClick;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.SharedPreferencesUtils;

public class ConfigSetActivity extends BaseActivity implements OnClickListener {
	private ItemClickView mIcvPinset;
	private ToggleButton mTogBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_set);
		initView();
		mActivities.add(this);
	};
	@Override
	protected void initView() {
		mIcvPinset = (ItemClickView) findViewById(R.id.icv_pin_set);
		mTogBtn = (ToggleButton) findViewById(R.id.tog_btn_pin_set);
		
		mIcvPinset.setOnClickListener(this);
		mTogBtn.setOnClickListener(this);
		LogUtils.d("打开界面时：curState="+SharedPreferencesUtils.getBoolean(this, ContentValue.SPFILE_PIN_TOGGLE, false));
		mTogBtn.setCurState(SharedPreferencesUtils.getBoolean(this, ContentValue.SPFILE_PIN_TOGGLE, false));
		mTogBtn.setOnToggleBtnClick(new OnToggleButtonClick() {
			private Intent service;
			@Override
			public void onChangeState() {
				LogUtils.d("设置缓存信息"+mTogBtn.isCurState());
				SharedPreferencesUtils.saveBoolean(ct, ContentValue.SPFILE_PIN_TOGGLE,mTogBtn.isCurState());
				if(mTogBtn.isCurState()){
					LogUtils.d("设置缓存信息"+true+"开启服务");
					service = new Intent(ct, SleepStateService.class);
					startService(service );
				}else{
					LogUtils.d("设置缓存信息"+false+"关闭服务");
					if(service!=null)
						stopService(service);
				}
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.d("KeyCode:"+keyCode);
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.icv_pin_set:
			intent = new Intent(ct, PinSetActivity.class);
			ct.startActivity(intent);
			break;
		case R.id.tog_btn_pin_set:
			mTogBtn.onClick(mTogBtn);
			break;
		}
	}
	/**
	 * pin码设置成功，跳转到首页
	 */
	private void savePin2Cache(){}
	/**
	 * 跳转下一步骤
	 */
	private void goNext(){}
	@Override
	protected void onPause() {
		super.onPause();
	}
}

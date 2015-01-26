package com.concordy.pro;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.concordy.pro.bean.User;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.PromptManager;
import com.concordy.pro.utils.SharedPreferencesUtils;

public class PinSetActivity extends BaseActivity implements OnClickListener {
	private EditText mEtPin;
	private Button mBtnNext;
	private TextView mTvSkip;
	private TextView mTvTitle;
	private Button mBtnLeft;
	private String mPin;
	private Intent intent;
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_pin);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, layout);
		initView();
		mActivities.add(this);
	};
	@Override
	protected void initView() {
		mEtPin = (EditText) findViewById(R.id.et_pin_guide);
		mBtnNext = (Button) findViewById(R.id.btn_next_login);
		mTvSkip = (TextView) findViewById(R.id.tv_skip_guide);
		mTvTitle = (TextView) findViewById(R.id.tv_txt_title);
		mBtnLeft = (Button) findViewById(R.id.btn_left);
		
		intent = getIntent();
		mBtnNext.setOnClickListener(this);
		mTvSkip.setOnClickListener(this);
		mBtnLeft.setVisibility(View.GONE);
		mTvTitle.setText(getResources().getString(R.string.str_pin_set));
		//隐藏显示按钮操作。
		mEtPin.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()>0)
				{
					mBtnNext.setVisibility(View.VISIBLE);
					mTvSkip.setVisibility(View.INVISIBLE);
				}else{
					mBtnNext.setVisibility(View.INVISIBLE);
					mTvSkip.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				LogUtils.d("beforeTextChanged(s:"+s+"...start:"+start+"...after"+after+"...count:"+count+")");
			}
			@Override
			public void afterTextChanged(Editable s) {
				LogUtils.d("afterTextChanged(s:"+s+"....count)");
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.d("KeyCode:"+keyCode);
		//new MyHttpAsyncTask<T>().e
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next_login:
			savePin2Cache();
			break;
		case R.id.tv_skip_guide:
			//goNext();
			PromptManager.showToast(ct, "测试阶段,强制设置PIN");
			break;
		}
	}
	/**
	 * pin码设置成功，跳转到首页
	 */
	private void savePin2Cache(){
		if(mEtPin!=null)
			mPin = mEtPin.getText().toString();
		if(mPin.length()<6){
			PromptManager.showToast(ct, getResources().getString(R.string.err_pin_empty));
			return;
		}
		mPin =CommonUtil.md5(mPin);//加密Pin码
		LogUtils.d("Pin:"+mPin);
		SharedPreferencesUtils.saveString(ct, ContentValue.SPFILE_PIN, mPin);
		goNext();
	}
	/**
	 * 跳转下一步骤
	 */
	private void goNext(){
		Bundle bundle = intent.getExtras();
		User user = (User) bundle.get("user");
		//开启意图跳转首页
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finishAll();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
}

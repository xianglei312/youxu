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

public class PinLoginActivity extends BaseActivity implements OnClickListener {
	private EditText mEtPin;
	private Button mBtnNext;
	private TextView mTvOther;
	private TextView mTvTitle;
	private Button mBtnLeft;
	private String mPin;
	private Intent intent;
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_pin);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, layout);
		initView();
		mActivities.add(this);
	};
	@Override
	protected void initView() {
		mEtPin = (EditText) findViewById(R.id.et_pin_login);
		mBtnNext = (Button) findViewById(R.id.btn_next_login);
		mTvOther = (TextView) findViewById(R.id.tv_other_login);
		mTvTitle = (TextView) findViewById(R.id.tv_txt_title);
		mBtnLeft = (Button) findViewById(R.id.btn_left);
		
		
		mBtnNext.setOnClickListener(this);
		mTvOther.setOnClickListener(this);
		mBtnLeft.setVisibility(View.GONE);
		mTvTitle.setText(getResources().getString(R.string.str_pin_verify));
		//隐藏显示按钮操作。
		mEtPin.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()>0)
				{
					mBtnNext.setVisibility(View.VISIBLE);
					mTvOther.setVisibility(View.INVISIBLE);
				}else{
					mBtnNext.setVisibility(View.INVISIBLE);
					mTvOther.setVisibility(View.VISIBLE);
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
			checkPinFromCache();
			break;
		case R.id.tv_other_login:
			Intent it = new Intent(this, LoginActivity.class);
			startActivity(it);
			break;
		}
	}
	/**
	 * 验证PIN
	 */
	private void checkPinFromCache(){
		if(mEtPin!=null)
			mPin = mEtPin.getText().toString();
		if(mPin.length()<6){
			PromptManager.showToast(ct, getResources().getString(R.string.err_pin_empty));
			return;
		}
		mPin = CommonUtil.md5(mPin);//加密Pin码
		LogUtils.d("Pin:"+mPin);
		String pin = SharedPreferencesUtils.getString(ct, ContentValue.SPFILE_PIN, "");
		int  errCount = SharedPreferencesUtils.getInteger(ct, ContentValue.SPFILE_PIN_ERRCOUNT, 0);
		if(!mPin.equals(pin)){
			errCount++;
			String err = "";
			LogUtils.d("PIN错误次数："+errCount);
			if(errCount>=5){
				//
				mTvOther.setVisibility(View.VISIBLE);
				err = getResources().getString(R.string.err_pin_lock);
				PromptManager.showToast(ct, err);
				return;
			}
			err = getResources().getString(R.string.err_pin_error);
			err = err.replaceAll("NUM", (5-errCount)+"");
			SharedPreferencesUtils.saveInteger(ct, ContentValue.SPFILE_PIN_ERRCOUNT, errCount);
			PromptManager.showToast(ct, err);
			return;
		}
		goNext();
	}
	/**
	 * 跳转下一步骤
	 */
	private void goNext(){
		//开启意图跳转首页
		intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}

package com.concordy.pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.concordy.pro.R;
import com.concordy.pro.ui.ItemClickView;
import com.concordy.pro.ui.ToggleButton;
import com.concordy.pro.ui.ToggleButton.OnToggleButtonClick;
import com.concordy.pro.ui.base.BaseTitleActivity;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PinSetItemActivity extends BaseTitleActivity implements OnClickListener {
	@ViewInject(R.id.icv_pin_set)
	private ItemClickView mIcvPinset;
	@ViewInject(R.id.tog_btn_pin_set)
	private ToggleButton mTogBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_pin);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, layout);
		initView();
	};
	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mIcvPinset.setOnClickListener(this);
		mTogBtn.setOnClickListener(this);
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
			if(mTogBtn.isCurState()){
				PromptManager.showToast(ct, "开启服务了");
			}else{
				PromptManager.showToast(ct, "关闭服务了");
			}
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

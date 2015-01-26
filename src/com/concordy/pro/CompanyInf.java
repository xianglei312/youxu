package com.concordy.pro;

import android.os.Bundle;

public class CompanyInf extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivities.add(this);
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mActivities.remove(this);
	}
}

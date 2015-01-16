package com.concordy.pro.pager;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

public abstract class BasePager {

	public Context ct;
	private View view;
	public boolean isLoadSuccess = false;
	public TextView txt_title;
	public BasePager(Context ct) {
		this.ct = ct;
		view = initView();
	}
	public void initTitleBar(View view) {
	}
	public abstract View initView();
	
	public abstract void initData();

	public View getRootView(){
		return view;
	}
}

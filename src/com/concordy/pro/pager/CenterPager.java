package com.concordy.pro.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;


public class CenterPager extends BasePager {
	
	public CenterPager(Context ct) {
		super(ct);
	}

	@Override
	public View initView() {
		TextView textView = new TextView(ct);
		textView.setTextSize(16);
		textView.setTextColor(Color.RED);
		textView.setText("Bill数据报表");
		return textView;
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}
}

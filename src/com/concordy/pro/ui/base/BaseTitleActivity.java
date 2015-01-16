package com.concordy.pro.ui.base;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.concordy.pro.R;

public abstract class BaseTitleActivity extends Activity implements OnClickListener {
	protected Context ct;
	protected int layout;
	
	private static final List<BaseTitleActivity> mActivities = new LinkedList<BaseTitleActivity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ct = getApplicationContext();
		layout = R.layout.layout_title_bar;
		//initTitleBar();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	protected abstract void initView();
	protected void initActionBar() {
	}
}

package com.concordy.pro.ui.base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity {
	private static BaseActivity mForegroundActivity = null;
	private static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
	public Context ct;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		ct = getApplicationContext();
		init();
		initView();
		initActionBar();
	}

	@Override
	protected void onResume() {
		mForegroundActivity = this;
		super.onResume();
	}

	@Override
	protected void onPause() {
		mForegroundActivity = null;
		super.onPause();
	}

	protected void init() {
	}

	protected void initView() {
	}

	protected void initActionBar() {

	}
	public static void finishAll() {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			activity.finish();
		}
	}

	public static void finishAll(BaseActivity except) {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			if (activity != except) activity.finish();
		}
	}
	public static boolean hasActivity() {
		return mActivities.size() > 0;
	}

	public static BaseActivity getForegroundActivity() {
		return mForegroundActivity;
	}

	public static BaseActivity getCurrentActivity() {
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<BaseActivity>(mActivities);
		}
		if (copy.size() > 0) {
			return copy.get(copy.size() - 1);
		}
		return null;
	}
	public void exitApp() {
		finishAll();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}

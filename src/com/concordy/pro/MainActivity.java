package com.concordy.pro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.concordy.pro.R;
import com.concordy.pro.fragment.HomeFragment;

public class MainActivity extends FragmentActivity {
	private boolean TOKEN_FLAG = false;
	private HomeFragment mHomeFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
		
	}
	private void init() {
		mHomeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.content,mHomeFragment ,"HomeFragment").commit();
		//getSupportFragmentManager().beginTransaction().replace(R.id.content,mTransFragment ,"TransFragment").commit();
	}
	/**
	 * 替换content显示内容
	 * @param fragment
	 */
	public void switchFragment(Fragment fragment){
		getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
	}
	/**
	 * 返回HomeFragment对象
	 * @return
	 */
	public HomeFragment getHomeFragment(){
		mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
		return mHomeFragment;
	}
}

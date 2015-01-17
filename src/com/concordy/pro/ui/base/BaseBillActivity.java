package com.concordy.pro.ui.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View.OnClickListener;

import com.concordy.pro.bean.Category;
import com.concordy.pro.bean.Vendor;

public abstract class BaseBillActivity extends BaseTitleActivity implements OnClickListener {
	protected Context ct;
	protected int layout;
	protected List<Vendor> vendorList;
	protected List<Category> cateList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDate();
	}
	/**
	 * 加载Vendor,Category数据
	 */
	private void initDate() {
		vendorList = new ArrayList<Vendor>();
		cateList = new ArrayList<Category>();
		
		
	}
	
}

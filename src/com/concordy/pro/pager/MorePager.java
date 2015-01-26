package com.concordy.pro.pager;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.concordy.pro.CompanyInf;
import com.concordy.pro.ConfigSetActivity;
import com.concordy.pro.R;
import com.concordy.pro.ui.ItemClickView;


public class MorePager extends BasePager implements OnClickListener{
	private ItemClickView mIcvCominfo;
	private ItemClickView mIcvMorSet;
	private Button btn;
	
	public MorePager(Context ct) {
		super(ct);
	}
	@Override
	public View initView() {
		View view = View.inflate(ct,R.layout.pager_more, null);
		mIcvCominfo = (ItemClickView) view.findViewById(R.id.icv_com_info);
		mIcvMorSet = (ItemClickView) view.findViewById(R.id.icv_more_set);
		
		btn = (Button) view.findViewById(R.id.setting);
		
		mIcvMorSet.setOnClickListener(this);
		mIcvCominfo.setOnClickListener(this);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ct,CompanyInf.class );
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ct.startActivity(intent);
			}
		});
		return view;
	}
	@Override
	public void initData() {
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.icv_com_info:
			intent = new Intent(ct, CompanyInf.class);
			ct.startActivity(intent);
			break;
		case R.id.icv_more_set:
			intent = new Intent(ct, ConfigSetActivity.class);
			ct.startActivity(intent);
			break;
		}
	}
}

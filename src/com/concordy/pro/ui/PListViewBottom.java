package com.concordy.pro.ui;

import com.concordy.pro.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
/***
 * 
 * ListView下拉头部view
 * @author scleo
 *
 */
public class PListViewBottom extends LinearLayout {

	
	private  int mState = STATE_NORMAL;//默认状态 
	/******默认显示状态************/
	public static final int STATE_NORMAL = 0;
	/******加载中状态************/
	public static final int STATE_REFRESHING = 1;
	/******准备加载状态************/
	public static final int STATE_READY = 2;
	/******没有更多显示状态************/
	public static final int STATE_NO_MORE = 3;
	private LinearLayout mLlayout;
	private RelativeLayout mRelaLayout;
	private TextView mTvHint;
	private ProgressBar mPb;
	public PListViewBottom(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PListViewBottom(Context context) {
		super(context);
		init(context);
	}

	/** 初始化 ***/
	private void init(Context context) {
		mLlayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.ui_plv_footer, null);
		addView(mLlayout);
		mLlayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		//得到子控件
		mRelaLayout = (RelativeLayout) mLlayout.findViewById(R.id.rl_plv_footer_content);
		mPb = (ProgressBar) mLlayout.findViewById(R.id.pb_plv_footer_progressbar);
		mTvHint = (TextView) mLlayout.findViewById(R.id.tv_plv_footer_hint_textview);
	}
	public void setState(int state){
		switch (state) {
		case STATE_NORMAL:
			mPb.setVisibility(View.INVISIBLE);
			mTvHint.setVisibility(View.VISIBLE);
			mTvHint.setText(R.string.ui_plv_footer_hint_normal);
			break;
		case STATE_READY:
			mTvHint.setVisibility(View.VISIBLE);
			mTvHint.setText(R.string.ui_plv_footer_hint_ready);
			mPb.setVisibility(View.INVISIBLE);
			break;
		case STATE_REFRESHING:
			mTvHint.setVisibility(View.INVISIBLE);
			mTvHint.setText(R.string.ui_plv_header_hint_loading);
			mPb.setVisibility(View.VISIBLE);
			break;
		case STATE_NO_MORE:
			mTvHint.setVisibility(View.VISIBLE);
			mTvHint.setText(R.string.ui_plv_footer_hint_no_more);
			mLlayout.setClickable(false);
			mPb.setVisibility(View.INVISIBLE);
			break;
		}
		mState = state;
	}
	public int getState(){
		return mState;
	}
	public void show(){
		LinearLayout.LayoutParams lp = (LayoutParams) mLlayout.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mRelaLayout.setLayoutParams(lp);
	}
	//设置高度
	public void setVisiableHeight(int height){
		if(height<0)
			height=0;
		LinearLayout.LayoutParams lp = (LayoutParams) mLlayout.getLayoutParams();
		lp.height = height;
		mLlayout.setLayoutParams(lp);
	}
	public int getVisiableHeight(){
		return mLlayout.getHeight();
	}

	public void hide() {
		LinearLayout.LayoutParams lp = (LayoutParams) mLlayout.getLayoutParams();
		lp.height = 0;
		mLlayout.setLayoutParams(lp);
	}
}

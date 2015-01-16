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
import android.widget.TextView;
/***
 * 
 * ListView下拉头部view
 * @author scleo
 *
 */
public class PListViewHeader extends LinearLayout {

	private LinearLayout.LayoutParams mLayParams;
	private LinearLayout mLLayout;
	private ImageView mArrowImg;
	private ProgressBar mPb;
	private TextView mTvHint;
	private RotateAnimation mRotateAnim;
	private long ROTATE_DURATION_TIME = 180;

	private  int mState = STATE_NORMAL;//默认状态 
	public static final int STATE_NORMAL = 0;
	public static final int STATE_REFRESHING = 1;
	public static final int STATE_READY = 2;
	public PListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PListViewHeader(Context context) {
		super(context);
		init(context);
	}

	/** 初始化 ***/
	private void init(Context context) {
		// 得到布局参数对象
		mLayParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		mLLayout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.ui_plv_header, null);
		addView(mLLayout,mLayParams);//重置布局大小
		setGravity(Gravity.BOTTOM);
		
		mArrowImg = (ImageView) findViewById(R.id.iv_plv_header_arrow);
		mTvHint = (TextView) findViewById(R.id.tv_plv_header_hint);
		mPb = (ProgressBar) findViewById(R.id.pb_plv_header);
		//创建图片旋转动画
		mRotateAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnim.setDuration(ROTATE_DURATION_TIME);
		mRotateAnim.setFillAfter(true);
		//mRotateAnim
	}
	/***设置当前头部下拉的状态****/
	public void setState(int state){
		if(state==mState)return;//第一次为默认状态
		
		if(state==STATE_REFRESHING){//下拉刷新
			mArrowImg.clearAnimation();//清空下拉的状态
			mArrowImg.setVisibility(View.INVISIBLE);//设置隐藏
			mPb.setVisibility(View.VISIBLE);//显示progress加载框
		}else{
			mArrowImg.setVisibility(View.VISIBLE);//显示下拉箭头
			mPb.setVisibility(View.INVISIBLE);//隐藏加载框
		}
		switch (state) {
		case STATE_NORMAL:
			//校验刷新
			if(mState == STATE_READY)//下拉的时候
				mArrowImg.startAnimation(mRotateAnim);
			else if(mState ==STATE_REFRESHING)//释放下拉
				mArrowImg.clearAnimation();
			mTvHint.setText(R.string.ui_plv_footer_hint_normal);//设置文本显示内容
			break;
		case STATE_REFRESHING:
			mTvHint.setText(R.string.ui_plv_header_hint_loading);//加载数据
			break;
		case STATE_READY:
			if(mState!=STATE_READY){
				mArrowImg.clearAnimation();
				mArrowImg.startAnimation(mRotateAnim);
				mTvHint.setText(R.string.ui_plv_footer_hint_ready);//释放下拉加载数据
			}
			break;
		}
		mState = state;
	}
	//设置高度
	public void setVisiableHeight(int height){
		if(height<0)
			height=0;
		LinearLayout.LayoutParams lp = (LayoutParams) mLLayout.getLayoutParams();
		lp.height = height;
		mLLayout.setLayoutParams(lp);
	}
	public int getVisiableHeight(){
		return mLLayout.getHeight();
	}
}

package com.concordy.pro.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.concordy.pro.R;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.LogUtils;

/**
 * @author Scleo
 */
public class PullListView extends ListView implements OnScrollListener {
	private static final int SCROLLER_DUARATION = 400;
	private static final float OFFSET_RADIO = 1.8f;
	private static final int SCROLL_BACK_HEADER= 0;
	private static final int SCROLL_BACK_BOTTOM= 1;
	private PListViewBottom mBottomView;
	private PListViewHeader mHeaderView;
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTime;
	private int mHeaderViewHeight;
	private boolean mIsBottomReady;
	private boolean mEnablePullRefresh;
	private boolean mEnablePullLoad;
	private boolean mPullRefreshing;
	private Scroller mScroller;
	private float mLastY = -1;
	private float mLastX = -1;
	private OnScrollListener mOnScrollListener;
	private int mTotalCount;
	private IPListViewListener mListViewListener;
	private boolean mPullLoading;
	private int mScrollBack;
	public PullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public PullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullListView(Context context) {
		super(context);
		init(context);
	}
	/*****初始数据****/
	@SuppressLint("NewApi")
	private void init(Context context) {
		mScroller = new Scroller(context);
		//复写滑动事件
		super.setOnScrollListener(this);
		//初始化头部对象
		mHeaderView = new PListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.rl_plv_header_content);
		mHeaderTime = (TextView) mHeaderView.findViewById(R.id.tv_plv_header_time);
		addHeaderView(mHeaderView);
		
		mBottomView = new PListViewBottom(context);
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mHeaderViewHeight = mHeaderViewContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
				//getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		});
		//初始化底部对象
	}
	@Override
	public void setAdapter(ListAdapter adapter) {
		if(mIsBottomReady==false){
			mIsBottomReady = true;
			addFooterView(mBottomView);
		}
		super.setAdapter(adapter);
	}
	/*******设置下拉刷新动作是否有效********/
	public void setPullRefreshEnable(boolean enable){
		mEnablePullRefresh = enable;
		if(mEnablePullRefresh){
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}else{
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		}
	}
	/*********设置上拉加载更多**************/
	public void setPullLoadEnable(boolean enable){
		mEnablePullLoad = enable;
		if(mEnablePullLoad){
			mBottomView.show();
			mBottomView.setState(PListViewBottom.STATE_NORMAL);
			//设置分割线
			setFooterDividersEnabled(true);
			mBottomView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadingMore();
				}
			});
		}else{
			mBottomView.hide();
			mBottomView.setOnClickListener(null);
			setFooterDividersEnabled(false);
		}
	}
	/******加载更多******/
	protected void startLoadingMore() {
		mPullLoading = true;
		mBottomView.setState(PListViewBottom.STATE_REFRESHING);
		if(mListViewListener!=null){
			mListViewListener.onLoadingMore();
		}
	}
	/******停止下拉刷新*******/
	public void stopRefresh(){
		if(mPullRefreshing){
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}
	public void setRefreshTime(String time){
		mHeaderTime.setText(time);
	}
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		
		LogUtils.d("mPullRefreshing:"+mPullRefreshing);
		
		if(height==0)return;
		if(mPullRefreshing&&height<=mHeaderViewHeight)return;
		int backY = 0;
		if(mPullRefreshing&&height>mHeaderViewHeight){//回滚
			backY = mHeaderViewHeight;
		}
		mScrollBack = SCROLL_BACK_HEADER;
		LogUtils.d("height:"+height+".....backY:"+backY);
		mScroller.startScroll(0, height, 0, backY-height, SCROLLER_DUARATION);
		invalidate();//刷新界面
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(mLastY==-1)
			mLastY = ev.getRawY();
		if(mLastX==-1)
			mLastX = ev.getRawX();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN://手指按下的时候
			mLastY = ev.getRawY();
			mLastX = ev.getRawX();
			break;
		case MotionEvent.ACTION_MOVE://移动动作
			float dy = ev.getRawY()-mLastY;
			float dx = ev.getRawX()-mLastX;
			mLastY = ev.getRawY();
			mLastX = ev.getRawX();
			if(getFirstVisiblePosition()==0&&(mHeaderView.getVisiableHeight()>0||dy>0)){//如果第一个可见的话
				updateHeaderHeight(dy/OFFSET_RADIO);
				invokeOnScrolling();
			}else if(getLastVisiblePosition()==(mTotalCount-1)&&(mBottomView.getBottom()>0||dy<0)){
				//updateBottomHeight(-dy/OFFSET_RADIO);
			}
			break;
		default:
			mLastY = -1;
			if(getFirstVisiblePosition()==0){
				if(mEnablePullRefresh&&mHeaderView.getVisiableHeight()>mHeaderViewHeight){
					mPullRefreshing = true;
					mHeaderView.setState(PListViewHeader.STATE_REFRESHING);
					if(mListViewListener!=null){
						mListViewListener.onRefresh();
					}
				}
				resetHeaderHeight();
			}else if(getLastVisiblePosition()==mTotalCount-1){//如果划拉到最后一个条目
				//预留
				//
				if(mEnablePullLoad)
					startLoadingMore();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	/*****更新底部高度*******/
	private void updateBottomHeight(float f) {
		//int height = mBottom.getb
	}
	/*******终止加载更多********/
	public void stopLoadMore(){
		if(mPullLoading){
			mPullLoading= false;
			mBottomView.setState(PListViewBottom.STATE_NORMAL);
		}
	}
	/********终止更多以后bottom状态**********/
	public void noMoreData(){
		mBottomView.setState(PListViewBottom.STATE_NO_MORE);
	}
	private void invokeOnScrolling() {
		if(mOnScrollListener instanceof ScrollListener){
			ScrollListener sl = (ScrollListener) mOnScrollListener;
			sl.onScrolling(this);
		}
	}

	/****更新头部高度*******/
	private void updateHeaderHeight(float f) {
		mHeaderView.setVisiableHeight((int) f+mHeaderView.getVisiableHeight());
		if(mEnablePullRefresh&&!mPullRefreshing){
			if(mHeaderView.getVisiableHeight()>mHeaderViewHeight){
				mHeaderView.setState(PListViewHeader.STATE_READY);
			}else{
				mHeaderView.setState(PListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0);
	}
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){
			if(mScrollBack==SCROLL_BACK_HEADER){
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			}
		}
		postInvalidate();
		invokeOnScrolling();
		super.computeScroll();
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(mOnScrollListener!=null)
			mOnScrollListener.onScrollStateChanged(view, scrollState);
	}
	/****listView上下滑动事件*****/
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mTotalCount = totalItemCount;
		if(mOnScrollListener!=null){
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}
	public void setListViewListener(IPListViewListener l) {
		mListViewListener = l;
	}
	public interface ScrollListener extends OnScrollListener{
		public void onScrolling(View view);
	}
	public interface IPListViewListener{
		public void onRefresh();
		public void onLoadingMore();
	}
}

package com.concordy.pro.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.concordy.pro.R;

/**
 * viewpager图片轮播效果
 * @author scleo
 */
public class SlideShowView extends RelativeLayout implements OnClickListener{
	// 轮播图图片数量
	private final static int IMAGE_COUNT = 5;
	// 自动轮播的时间间隔
	private final static int TIME_INTERVAL = 5;
	// 自动轮播启用开关
	private final static boolean isAutoPlay = false;
	// 自定义轮播图的资源ID
	private int[] imagesResIds;
	// 放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;
	// 放圆点的View的list
	private List<View> dotViewsList;
	//
	private ViewPager viewPager;
	// 当前轮播页
	private int currentItem = 0;
	// 定时任务
	private ScheduledExecutorService scheduledExecutorService;
	// Handler
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
		}

	};
	private LinearLayout mPtPoint;
	private TextView mDescript;

	public SlideShowView(Context context) {
		this(context, null);
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData();
		initUI(context);
		if (isAutoPlay) {
			startPlay();
		}
	}

	/**
	 * 开始轮播图切换
	 */
	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
				TimeUnit.SECONDS);
	}

	/**
	 * 停止轮播图切换
	 */
	private void stopPlay() {
		scheduledExecutorService.shutdown();
	}

	/**
	 * 初始化相关Data
	 */
	private void initData() {
		imagesResIds = new int[] { R.drawable.a4, R.drawable.a5, R.drawable.a6 };
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();
	}

	/**
	 * 初始化Views等UI
	 */
	private void initUI(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.layout_slideshow, this, true);
		mPtPoint = (LinearLayout) view.findViewById(R.id.ll_child);
		mDescript = (TextView) view.findViewById(R.id.tv_desc_slideshow);
		mDescript.setOnClickListener(this);
		for (int imageID : imagesResIds) {
			ImageView iv = new ImageView(context);
			iv.setImageResource(imageID);
			iv.setScaleType(ScaleType.FIT_XY);
			imageViewsList.add(iv);
			initPoint(context);
		}
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setFocusable(true);
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	/****** 初始化小圆点 ****/
	private void initPoint(Context ct) {
		ImageView iv = new ImageView(ct);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.leftMargin = (int) getResources().getDimension(R.dimen.img_point_margin);
		iv.setBackgroundResource(R.drawable.dot_black);
		mPtPoint.addView(iv, param);
	}

	/**
	 * 填充ViewPager的页面适配器
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(imageViewsList.get(position));
			return imageViewsList.get(position);
		}

		@Override
		public int getCount() {
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author caizhiming
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:// 手势滑动，空闲中
				isAutoPlay = false;
				break;
			case 2:// 界面切换中
				isAutoPlay = true;
				break;
			case 0:// 滑动结束，即切换完毕或者加载完毕
					// 当前为最后一张，此时从右向左滑，则切换到第一张
				/*if (viewPager.getCurrentItem() == viewPager.getAdapter()
						.getCount() - 1 && !isAutoPlay) {
					viewPager.setCurrentItem(0);
				}
				// 当前为第一张，此时从左向右滑，则切换到最后一张
				else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
					viewPager
							.setCurrentItem(viewPager.getAdapter().getCount() - 1);
				}*/
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int pos) {
			//设置小圆点状态
			currentItem = pos;
			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == currentItem) {
					(mPtPoint.getChildAt(pos))
							.setBackgroundResource(R.drawable.dot_white);
				} else {
					(mPtPoint.getChildAt(pos))
							.setBackgroundResource(R.drawable.dot_black);
				}
			}
			//设置text显示内容
			mDescript.setText("");
		}

	}
	/**
	 * 执行轮播图切换任务
	 * 
	 * @author caizhiming
	 */
	private class SlideShowTask implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViewsList.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}
	/**
	 * 销毁ImageView资源，回收内存
	 * 
	 * @author caizhiming
	 */
	private void destoryBitmaps() {

		for (int i = 0; i < IMAGE_COUNT; i++) {
			ImageView imageView = imageViewsList.get(i);
			Drawable drawable = imageView.getDrawable();
			if (drawable != null) {
				// 解除drawable对view的引用
				drawable.setCallback(null);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_desc_slideshow:
			break;
		}
	}
}
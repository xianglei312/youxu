package com.concordy.pro.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.concordy.pro.R;
import com.concordy.pro.pager.BasePager;
import com.concordy.pro.pager.CenterPager;
import com.concordy.pro.pager.HomePager;
import com.concordy.pro.pager.MorePager;
import com.concordy.pro.ui.MyViewPager;
import com.concordy.pro.ui.LazyViewPager.OnPageChangeListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HomeFragment extends BaseFragment {
	private final int TAB_LEFT = 0;
	private final int TAB_CENTER = 1;
	private final int TAB_RIGHT = 2;
	private View view;
	@ViewInject(R.id.view_pager)
	private MyViewPager view_pager;
	@ViewInject(R.id.main_radio)
	private RadioGroup mRadio;
	@ViewInject(R.id.rb_tab_left)
	private RadioButton mRbLeft;
	@ViewInject(R.id.rb_tab_center)
	private RadioButton mRbCenter;
	@ViewInject(R.id.rb_tab_right)
	private RadioButton mRbRight;
	private List<BasePager> mLists = new ArrayList<BasePager>();

	// 初始化首页的数据
	private class ViewPagerAdapter extends PagerAdapter {
		public List<BasePager> mPages;

		public ViewPagerAdapter(List<BasePager> mLists) {
			this.mPages = mLists;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((MyViewPager) container).removeView(mPages.get(position)
					.getRootView());
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((MyViewPager) container).addView(mPages.get(position)
					.getRootView(), 0);
			return mPages.get(position).getRootView();
		}
		@Override
		public int getCount() {
			return mPages.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	@Override
	public void initData() {
		mLists.clear();
		//修改成josn可随意变动
		mLists.add(new HomePager(ct));
		mLists.add(new CenterPager(ct));
		mLists.add(new MorePager(ct));
		ViewPagerAdapter vpa = new ViewPagerAdapter(mLists);
		view_pager.setAdapter(vpa);
		//设置页面改变时监听动作
		view_pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				BasePager pager = mLists.get(position);
				pager.initData();
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		mRadio.check(R.id.rb_tab_left);
		//首次进入页面 设置默认页面并初始化数据
		mLists.get(0).initData();
	}
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.frag_home, null);
		ViewUtils.inject(this, view);
		//mRadio = (RadioGroup) view.findViewById(R.id.main_radio);
		/**********切换radio时执行动作*************/
		mRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_tab_left://首页
					view_pager.setCurrentItem(TAB_LEFT,false);
					break;
				case R.id.rb_tab_center://快速添加
					view_pager.setCurrentItem(TAB_CENTER,false);
					break;
				case R.id.rb_tab_right://设置中心
					view_pager.setCurrentItem(TAB_RIGHT,false);
					break;
				}
			}
		});
		return view;
	}
}

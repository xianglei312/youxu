package com.concordy.pro.pager;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.concordy.pro.AddBillActivity;
import com.concordy.pro.BaseBillActivity;
import com.concordy.pro.InvoiceActivity;
import com.concordy.pro.R;
import com.concordy.pro.bean.Bill;
import com.concordy.pro.bean.Bills;
import com.concordy.pro.bean.HttpError;
import com.concordy.pro.http.protocol.BaseProtocol;
import com.concordy.pro.http.protocol.BillProtocol;
import com.concordy.pro.ui.PullListView;
import com.concordy.pro.ui.PullListView.IPListViewListener;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.PromptManager;
import com.concordy.pro.utils.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HomePager extends BasePager implements OnClickListener,
		OnItemClickListener, IPListViewListener, OnItemLongClickListener {
	@ViewInject(R.id.bt_add_bill)
	private Button mBtnAddbill;
	@ViewInject(R.id.btn_sort_amount)
	private Button mBtnSortAmount;
	@ViewInject(R.id.btn_sort_latest)
	private Button mBtnSortLatest;
	@ViewInject(R.id.lv_home_bill)
	private PullListView mLvBills;
	private BillAdapter adapter;
	private List<Bill> mListBills;
	private boolean CURR_SORT = false;
	private Bills mCurrBill = null;
	private PopupWindow popupWindow;

	public HomePager(Context ct) {
		super(ct);
	}

	/**
	 * 初始化页面布局
	 */
	@Override
	public View initView() {
		// ViewPager
		View view = View.inflate(ct, R.layout.pager_home, null);
		ViewUtils.inject(this, view);
		mBtnAddbill.setOnClickListener(this);
		mBtnSortLatest.setOnClickListener(this);
		mBtnSortAmount.setOnClickListener(this);
		mLvBills.setPullLoadEnable(true);
		mLvBills.setPullRefreshEnable(true);
		mLvBills.setListViewListener(this);
		mLvBills.setOnItemClickListener(this);
		mLvBills.setOnItemLongClickListener(this);
		return view;
	}
	protected void refresh() {
		mLvBills.stopRefresh();
	}
	protected void loadMore() {
		mLvBills.stopLoadMore();
	}
	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {
		/*
		 * mBillProtocol = new BillProtocol(); getLastesData();
		 */
		// mListBills = getBills();
		// 1 首先判断本地缓存里面是否有数据。
		/*mListBills = getCacheData();
		// 2 如果有数据的话，首先展示缓存里面的数据。。如果没有缓存数据。就直接展示对话框。
		// 3然后在去链接服务器。如果服务器有数据，就必须从服务器获取数据，然后替换本地的数据。
		if (mListBills != null) {
			processData(mListBills);
		}*/
		getData(BaseProtocol.GET_DATA);
	}

	/**
	 * 获取服务器请求数据
	 * @param <T>
	 */
	private <T> void getData(final int flag) {
		if (CommonUtil.isNetworkAvailable(ct) == 0) {
			PromptManager.showToast(ct,
					ct.getResources().getString(R.string.err_no_network));
			return;
		}
		new AsyncTask<String, Void, T>() {
			@Override
			protected T doInBackground(String... params) {
				BillProtocol bp = new BillProtocol();
				String url = ContentValue.SERVER_URL+"/"+ContentValue.BILL_GETALL+"?page=0";
				mCurrBill = bp.load(flag,url,ContentValue.APPLICATION_JSON);
				return parseData(bp);
			}
			@Override
			protected void onPostExecute(T t) {
				mLvBills.stopRefresh();
				String time = CommonUtil.formate2String(System
						.currentTimeMillis() + "");
				mLvBills.setRefreshTime(time);
				if (t instanceof HttpError) {
					HttpError error = (HttpError) t;
					PromptManager.showToast(ct, "出错啦："+error.getErrorMsg());
					return;
				}
				mListBills =  (List<Bill>) t;
				processData(mListBills);
			}
		}.execute();
	}
	private <T> T parseData(BillProtocol bp) {
		if(mCurrBill==null){
			HttpError httpError = bp.getHttpError();
			if(httpError!=null){
				LogUtils.d("错误码："+httpError.getErrorCode()+"错误信息："+httpError.getErrorMsg());
				return (T) httpError;
			}
			return null;
		}else{
			return (T) mCurrBill.getBills();
		}
	}
	/**
	 * 获取页面显示数据
	 * 
	 * @return
	 */
	protected void processData(List<Bill> lists) {
		if (lists != null) {
			// 设置listView适配器显示数据
			adapter = new BillAdapter(ct, lists);
			mLvBills.setAdapter(adapter);
			mLvBills.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_add_bill:
			openBill(null);
			break;
		case R.id.btn_sort_amount:// 金额排序
			CommonUtil.sortByAmount(mListBills, CURR_SORT);
			processData(mListBills);
			CURR_SORT = !CURR_SORT;
			break;
		case R.id.btn_sort_latest:// 最新数据
			CommonUtil.sortByTime(mListBills, CURR_SORT);
			processData(mListBills);
			CURR_SORT = !CURR_SORT;
			break;
		case R.id.ll_start:
			PromptManager.showToast(ct, "删除");
			break;
		case R.id.ll_uninstall:
			PromptManager.showToast(ct, "编辑");
			break;
		case R.id.ll_detail:
			PromptManager.showToast(ct, "详细");
			break;
		}
	}
	/******显示listView 子项可操作选项 ********//*
	private void showPopControl(AdapterView<?> parent, View view) {
		dissmissPopup();
		// 定义了一个数组
		int[] location = new int[2];
		// 给数组里面的元素赋值了 x y
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		View contentView = View.inflate(ct, R.layout.popup_bill, null);
		// 初始化控件
		LinearLayout ll_start = (LinearLayout) contentView
				.findViewById(R.id.ll_start);
		ll_start.setOnClickListener(this);
		LinearLayout ll_uninstall = (LinearLayout) contentView
				.findViewById(R.id.ll_uninstall);
		ll_uninstall.setOnClickListener(this);
		LinearLayout ll_detail = (LinearLayout) contentView
				.findViewById(R.id.ll_detail);
		ll_detail.setOnClickListener(this);
		popupWindow = new PopupWindow(contentView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);// 创建了一个透明颜色的背景
		popupWindow.setBackgroundDrawable(colorDrawable);
		// 步骤2 显示popup 参数1 popup挂载到哪个父窗体上 参数2 对齐方式 参数3 x轴的偏移量
		popupWindow.showAtLocation(parent,Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, y+view.getHeight());
		LogUtils.d("popupWindow-height:"+contentView.getTop()+",bottom:"+contentView.getBottom()+";y="+y);
		// 从透明到不透明
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
		alphaAnimation.setDuration(500);
		// 参数1和参数2 代表x轴变量 参数5和6 代表基于控件的哪个点缩放
		ScaleAnimation animation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
				0.5f, 0.5f);
		animation.setDuration(500);
		// 定义了动画的集合
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(animation);
		// 动画要想能够正常播放 必须保证控件有背景 popupWindow默认没有背景
		contentView.startAnimation(animationSet);// 运行了动画
	}*/
	/******显示listView 子项可操作选项 ********/
	private void showPopControl(AdapterView<?> parent, View view) {
		dissmissPopup();
		// 定义了一个数组
		int[] location = new int[2];
		// 给数组里面的元素赋值了 x y
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		View contentView = View.inflate(ct, R.layout.popup_softmanager, null);
		// 初始化控件
		LinearLayout ll_start = (LinearLayout) contentView
				.findViewById(R.id.ll_start);
		ll_start.setOnClickListener(this);
		LinearLayout ll_uninstall = (LinearLayout) contentView
				.findViewById(R.id.ll_uninstall);
		ll_uninstall.setOnClickListener(this);
		LinearLayout ll_detail = (LinearLayout) contentView
				.findViewById(R.id.ll_detail);
		ll_detail.setOnClickListener(this);
		popupWindow = new PopupWindow(contentView, -2, -2);
		popupWindow.setOutsideTouchable(true);
		ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);// 创建了一个透明颜色的背景
		popupWindow.setBackgroundDrawable(colorDrawable);
		// 步骤2 显示popup 参数1 popup挂载到哪个父窗体上 参数2 对齐方式 参数3 x轴的偏移量
		popupWindow.showAtLocation(parent, Gravity.TOP | Gravity.LEFT, x
				+ CommonUtil.dip2px(ct, 50), y);
		// 从透明到不透明
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
		alphaAnimation.setDuration(500);
		// 参数1和参数2 代表x轴变量 参数5和6 代表基于控件的哪个点缩放
		ScaleAnimation animation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
				0.5f, 0.5f);
		animation.setDuration(500);
		// 定义了动画的集合
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(animation);
		// 动画要想能够正常播放 必须保证控件有背景 popupWindow默认没有背景
		contentView.startAnimation(animationSet);// 运行了动画
	}
	
	/***
	 * ListView数据适配器
	 * 
	 * @author Scleo
	 * 
	 */
	private class BillAdapter extends BaseAdapter{
		private List<Bill> mBills;
		public BillAdapter(Context ct, List<Bill> mListBills) {
			this.mBills = mListBills;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyHolder holder;
			if (convertView != null) {
				holder = (MyHolder) convertView.getTag();
			} else {
				convertView = View.inflate(ct, R.layout.item_home_bill, null);
				holder = new MyHolder();
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.tv_item_bill_info);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_item_bill_date);
				holder.tvMoney = (TextView) convertView
						.findViewById(R.id.tv_item_bill_money);
				holder.cb = (CheckBox) convertView
						.findViewById(R.id.cb_item_bill_check);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.iv_item_bill_icon);
				holder.pay = (Button) convertView
						.findViewById(R.id.bt_item_pay);
				
				convertView.setTag(holder);
			}
			Bill bill = mBills.get(position);
			if(bill!=null){
				holder.tvContent.setText(bill.getVendor().getName() );
				holder.tvDate.setText(bill.getDueDate());
				holder.tvMoney.setText(bill.getAmount() + "");
			}
			return convertView;
		}

		@Override
		public int getCount() {
			return mBills.size();
		}

		@Override
		public Object getItem(int position) {
			return mBills.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	class MyHolder {
		public TextView tvMoney;
		public TextView tvDate;
		public TextView tvContent;
		public TextView tv;
		public Button pay;
		public CheckBox cb;
		public ImageView icon;
	}

	@Override
	public void onRefresh() {
		getData(BaseProtocol.GET_LASTESTDATA);
	}
	@Override
	public void onLoadingMore() {
		getMoreData();
	}
	/*** 加载更多数据****/
	private <T> void getMoreData() {
		if((mCurrBill!=null&&StringUtils.isEmpty(mCurrBill.getNext()))||mCurrBill==null){
			PromptManager.showToast(ct, "没有更多数据");
			mLvBills.noMoreData();
			return ;
		}
		new AsyncTask<String, Void, T>(){
			@Override
			protected T doInBackground(String... params) {
				BillProtocol bp = new BillProtocol();
				//LogUtils.d("下一页地址："+mCurrBill.getNext());
				mCurrBill = bp.load(BaseProtocol.GET_MOREDATA,mCurrBill.getNext(), ContentValue.APPLICATION_JSON);
				return parseData(bp);
			}
			@Override
			protected void onPostExecute(T t) {
				mLvBills.stopLoadMore();
				if (t instanceof HttpError) {
					HttpError error = (HttpError) t;
					PromptManager.showToast(ct, "出错啦："+error.getErrorMsg());
					return;
				}
				List<Bill> more = (List<Bill>) t;
				mListBills.addAll(more);
				processData(mListBills);
			}
		}.execute();
	}

	/*** 隐藏PopupWindow ***/
	public void dissmissPopup() {
		if (popupWindow != null) {
			popupWindow.dismiss();// 隐藏popupwindow;
			popupWindow = null;
		}
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		showPopControl(parent, view);
		return true;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LogUtils.d("第"+position+"个被点击了");
		int index = position-1;
		if(mListBills!=null){
			Bill bill = mListBills.get(index);
			LogUtils.d("bill:"+bill.toString());
			openBill(bill);
		}
		//LogUtils.d("第"+position+"个被点击了");
		
	}
	/**
	 * 跳转到
	 * @param bill
	 */
	private void openBill(Bill bill) {
		Intent intent = null ;
		if(bill!=null){
			intent = new Intent(ct, InvoiceActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("bill", bill);
			intent.putExtras(b);
		}else{
			intent = new Intent(ct, AddBillActivity.class);
		}
		ct.startActivity(intent);
	}
	
	class ItemFlingTouchListener implements OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return false;
		}
	}
}

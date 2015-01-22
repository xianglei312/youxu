package com.concordy.pro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.concordy.pro.bean.Bill.Item;
import com.concordy.pro.bean.Category;
import com.concordy.pro.bean.RecurringSetting;
import com.concordy.pro.bean.Vendor;
import com.concordy.pro.http.protocol.BaseProtocol;
import com.concordy.pro.http.protocol.CategoryProtocol;
import com.concordy.pro.http.protocol.VendorProtocol;
import com.concordy.pro.manager.ThreadManager;
import com.concordy.pro.manager.ThreadManager.ThreadPoolProxy;
import com.concordy.pro.ui.base.BaseTitleActivity;
import com.concordy.pro.ui.widget.CalendarView;
import com.concordy.pro.ui.widget.CalendarView.OnDateClickListener;
import com.concordy.pro.ui.widget.CustomerEditText;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.PromptManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public abstract  class BaseBillActivity extends BaseTitleActivity {
	protected List<Vendor> mVendorList;
	protected List<Category> mCateList;
	protected Vendor mVendor;
	protected Category mCategory;
	protected RecurringSetting rs;
	protected List<Item> items;
	protected static final int INTENT_RECURRING_FLAG = 98;
	protected static final int INTENT_TAKE_PICTURE_FLAG = 99;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		init();
	}
	private void init() {
		getBaseData();
	}
	protected View getContentView() {
		return contentView;
	}
	@Override
	protected void initView() {
	};
	/** 封装 Bill数据 */
	public abstract void fillBill();
	/** 发送数据 */
	public void sendData(){}
	/** 初始化数据  */
	public  void initData(){}
	/** 获取Category */
	private void getBaseData() {
		mThreadPool = ThreadManager.getSinglePool();
		BaseProtocol<List<Vendor>> bpVendor = new VendorProtocol();
		mVendorTask = new TaskThread<List<Vendor>>(ContentValue.SERVER_URI
				+ "/" + ContentValue.URI_VENDOR, bpVendor);
		mThreadPool.execute(mVendorTask);
		BaseProtocol<List<Category>> bpCategory = new CategoryProtocol();
		mCateTask = new TaskThread<List<Category>>(ContentValue.SERVER_URI
				+ "/" + ContentValue.URI_CATEGORY, bpCategory);
		mThreadPool.execute(mCateTask);
	}
	/** 解析数据
	 *  @param <T>
	 * @param result
	 */
	protected <T> List<T> parseData(String result, Class<T> clz) {
		LogUtils.d("result:" + result);
		Gson gson = new Gson();
		return gson.fromJson(result, new TypeToken<ArrayList<T>>() {
		}.getType());
	}

	/***
	 * 显示日历控件
	 */
	protected void showCalendor(final View parentView) {
		View view = View.inflate(this, R.layout.ui_pop_calender, null);
		// ViewUtils.inject(view);
		// 获取日历控件对象
		final CalendarView calendar = (CalendarView) view
				.findViewById(R.id.calendar_bill);
		calendar.setSelectMore(false); // 单选
		ImageButton calendarLeft = (ImageButton) view
				.findViewById(R.id.ib_left_calendar);
		final TextView calendarCenter = (TextView) view
				.findViewById(R.id.tv_date_calendar);
		ImageButton calendarRight = (ImageButton) view
				.findViewById(R.id.ib_right_calendar);
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 设置日历日期
			Date date = format.parse("2015-01-01");
			calendar.setCalendarData(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
		String[] ya = calendar.getYearAndmonth().split("-");
		calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
		calendarLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击上一月 同样返回年月
				String leftYearAndmonth = calendar.clickLeftMonth();
				String[] ya = leftYearAndmonth.split("-");
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
		});
		calendarRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击下一月
				String rightYearAndmonth = calendar.clickRightMonth();
				String[] ya = rightYearAndmonth.split("-");
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
		});
		calendar.setOnItemClickListener(new OnDateClickListener() {
			@Override
			public void OnItemClick(Date selectedStartDate,
					Date selectedEndDate, Date downDate) {
				// Log
				if (calendar.isSelectMore()) {
					Toast.makeText(
							getApplicationContext(),
							format.format(selectedStartDate) + "到"
									+ format.format(selectedEndDate),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							format.format(downDate), Toast.LENGTH_SHORT).show();
					((CustomerEditText) parentView).setText(format
							.format(downDate));
					dismissPop(mPopVendor);
				}
			}
		});
		setPopStyle(view, parentView);
	}
	/**
	 * 显示Vendor
	 */
	protected void showVendor(final View parentView) {
		View view = View.inflate(this, R.layout.ui_pop_item, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_pop_item_add);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PromptManager.showToast(BaseBillActivity.this, "添加Vendor");
				dismissPop(mPopVendor);
				return;
			}
		});
		ListView lv = (ListView) view.findViewById(R.id.lv);
		if (mVendorAdapter == null) {
			mVendorList = mVendorTask.getResult();
			mVendorAdapter = new VendorAdapter(this, mVendorList);
		}
		lv.setAdapter(mVendorAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mVendor = mVendorList.get(position);
				((CustomerEditText) parentView).setText(mVendor.getName());
				dismissPop(mPopVendor);
			}
		});
		setPopStyle(view, parentView);
	}
	/***
	 * 设置Pop样式，位置
	 * 
	 * @param parentView
	 *            挂载到哪个View上
	 * @param postionView
	 */
	private void setPopStyle(View parentView, View postionView) {
		int[] local = new int[2];
		postionView.getLocationOnScreen(local);
		mPopVendor = new PopupWindow(parentView, -2, -2);
		// lv.setItemsCanFocus(false);
		mPopVendor.setFocusable(true);
		mPopVendor.setOutsideTouchable(true);
		ColorDrawable colorDrawable = new ColorDrawable(Color.WHITE);// 创建了一个透明颜色的背景
		mPopVendor.setBackgroundDrawable(colorDrawable);
		mPopVendor.showAtLocation(parentView, Gravity.TOP | Gravity.LEFT, 0,
				local[1] + postionView.getHeight());
	}
	/***
	 * 销毁pop
	 */
	private void dismissPop(PopupWindow pop) {
		if (pop != null)
			pop.dismiss();
		pop = null;
	}
	class VendorAdapter extends BaseAdapter {
		private List<Vendor> mVendors;
		private Context ct;
		public VendorAdapter(Context ct, List<Vendor> vendors) {
			this.mVendors = vendors;
			this.ct = ct;
		}
		@Override
		public int getCount() {
			return mVendors.size();
		}
		@Override
		public Object getItem(int position) {
			return mVendors.get(position);
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv;
			if (convertView == null) {
				tv = new TextView(ct);
			} else {
				tv = (TextView) convertView;
			}
			tv.setText(mVendors.get(position).getName());
			return tv;
		}
	}

	private PopupWindow mPopVendor;
	private VendorAdapter mVendorAdapter;
	private ThreadPoolProxy mThreadPool;
	private TaskThread<List<Vendor>> mVendorTask;
	private TaskThread<List<Category>> mCateTask;
	private View contentView;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mThreadPool != null) {
			mThreadPool.shutdown();
		}
	}
	class TaskThread<Data> implements Runnable {
		private Data result;
		private BaseProtocol<Data> protocol;
		private final static int flag = BaseProtocol.GET_DATA;
		private String url;
		public TaskThread(String url, BaseProtocol<Data> protocol) {
			// result = new ArrayList<Data>
			this.protocol = protocol;
			this.url = url;
		}
		@Override
		public void run() {
			if (protocol != null) {
				result = protocol
						.load(flag, url, ContentValue.APPLICATION_JSON);
			}
		}
		protected Data getResult() {
			return result;
		}
	}
}
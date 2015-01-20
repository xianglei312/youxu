package com.concordy.pro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.concordy.pro.adapter.Itemadapter;
import com.concordy.pro.bean.Bill;
import com.concordy.pro.bean.Bill.Item;
import com.concordy.pro.bean.Category;
import com.concordy.pro.bean.RecurringSetting;
import com.concordy.pro.bean.Vendor;
import com.concordy.pro.http.HttpHelper;
import com.concordy.pro.http.HttpHelper.HttpResult;
import com.concordy.pro.http.protocol.BaseProtocol;
import com.concordy.pro.http.protocol.CategoryProtocol;
import com.concordy.pro.http.protocol.VendorProtocol;
import com.concordy.pro.manager.ThreadManager;
import com.concordy.pro.manager.ThreadManager.ThreadPoolProxy;
import com.concordy.pro.ui.widget.CalendarView;
import com.concordy.pro.ui.widget.CalendarView.OnDateClickListener;
import com.concordy.pro.ui.widget.CustomerEditText;
import com.concordy.pro.ui.widget.CustomerEditText.AutoEditTextListener;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.PromptManager;
import com.concordy.pro.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public abstract class BaseBillActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	// 注解初始化控件
	@ViewInject(R.id.iv_camera_bill)
	private ImageView ivCamera;
	@ViewInject(R.id.iv_bill)
	private ImageView ivShowBill;
	@ViewInject(R.id.btn_add_bill_item)
	private Button btnAddItem;
	@ViewInject(R.id.btn_show_more_bill)
	private Button btnShowHide;
	@ViewInject(R.id.btn_sub_bill)
	private Button btnSubBill;
	@ViewInject(R.id.linearlayout)
	private LinearLayout llNotes;
	@ViewInject(R.id.scrollview)
	private ScrollView scrollView;
	@ViewInject(R.id.checkbox)
	private CheckBox cbRecurring;
	@ViewInject(R.id.cet_bill_date)
	private CustomerEditText cetBillDate;
	@ViewInject(R.id.cet_bill_duedate)
	private CustomerEditText cetDueDate;
	@ViewInject(R.id.cet_bill_vendor)
	private CustomerEditText cetVendor;

	@ViewInject(R.id.et_total_amount_bill)
	private EditText etAmount;
	@ViewInject(R.id.lv_item_bill)
	private ListView lvBillItem;

	private Context context;
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;// 设置拍照操作的标志
	protected final static int INTENT_RECURRING_FLAG = 99;
	protected final static int INTENT_TAKE_PICTURE_FLAG = 98;
	protected String mUri;
	private List<Vendor> mVendorList;
	private List<Category> mCateList;
	private Itemadapter mItemadapter;
	private Bill mBill;
	private boolean isRecurring;
	private Vendor mVendor;
	private Category mCategory;
	private RecurringSetting rs;
	private List<Item> items;
	private String url = ContentValue.NEWSERVER_URL + "/"
			+ ContentValue.BILL_URL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_invoice);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.layout_invoice_title_bar);// 设置titleBar 布局文件
		ViewUtils.inject(this);
		init();
	}

	/***
	 * 初始化控件
	 */
	private void init() {
		getBaseData();
		initData();
		context = this;
		isRecurring = false;
		btnSubBill.setOnClickListener(this);
		cbRecurring.setOnCheckedChangeListener(this);
		btnShowHide.setOnClickListener(this);
		ivCamera.setOnClickListener(this);
		btnAddItem.setOnClickListener(this);
		// btnCalendor.setOnClickListener(this);
		cetBillDate.setOnViewClickListener(new AutoEditTextListener() {
			@Override
			public void onEditChanged() {
			}

			@Override
			public void onBtnClick() {
				showCalendor(cetBillDate);
			}
		});
		cetDueDate.setOnViewClickListener(new AutoEditTextListener() {
			@Override
			public void onEditChanged() {
			}

			@Override
			public void onBtnClick() {
				showCalendor(cetDueDate);
			}
		});
		cetVendor.setOnViewClickListener(new AutoEditTextListener() {
			@Override
			public void onEditChanged() {
			}

			@Override
			public void onBtnClick() {
				showVendor(cetVendor);
			}
		});
		mItemadapter = new Itemadapter(this, items,
				new com.concordy.pro.adapter.Itemadapter.Delete() {
					@Override
					public void delete(ArrayList<String> arr, int position) {
						// arr.remove(position);
						mItemadapter.notifyDataSetChanged();
						// setListViewHeightBasedOnChildren(lvBillItem);
					}
				});
		lvBillItem.setAdapter(mItemadapter);
		scrollView.smoothScrollTo(0, 0);
	}

	/** 初始化数据  */
	public abstract void initData();
	/** 填充账单数据  */
	public abstract void inflatBill();
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

	/**
	 * 解析数据
	 * @param <T>
	 * @param result
	 */
	protected <T> List<T> parseData(String result, Class<T> clz) {
		LogUtils.d("result:" + result);
		Gson gson = new Gson();
		return gson.fromJson(result, new TypeToken<ArrayList<T>>() {
		}.getType());
	}

	/**
	 * 
	 */
	public void bill_value(Intent intent) {
		mBill = new Bill();
		mBill.setBillDate(cetBillDate.getText());
		mBill.setAmount(Integer.parseInt(etAmount.getText().toString().trim()));
		mBill.setDueDate(cetDueDate.getText());
		mBill.setVendor(mVendor);
		if (mCateList == null) {
			mCateList = mCateTask.getResult();
			mCategory = mCateList.get(0);
		}
		mBill.setCategory(mCategory);
		mBill.setItems(items);
		if (null != intent) {
			rs = (RecurringSetting) intent.getExtras().getSerializable("RS");
			mBill.setRecurringSetting(rs);
			System.out.println("接受的RecurringSetting数据:" + rs.toString());
		} else {
			rs = null;
			mBill.setRecurringSetting(rs);
		}
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_camera_bill:
			ivShowBill.setVisibility(View.VISIBLE);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
			break;
		case R.id.btn_add_bill_item:
			addItem();
			break;
		case R.id.btn_show_more_bill:
			btnShowHide.setVisibility(View.GONE);
			llNotes.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_sub_bill:
			save_addbill();
			break;
		default:
			break;
		}
	}

	private void addItem() {
		mItemadapter.items.add(new Item());
		mItemadapter.notifyDataSetChanged();
		// setListViewHeightBasedOnChildren(lvBillItem);
	}

	/***
	 * 显示日历控件
	 */
	private void showCalendor(final View parentView) {
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
	private void showVendor(final View parentView) {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == INTENT_RECURRING_FLAG ){
			if(data==null)
				return;
			rs = (RecurringSetting) data.getSerializableExtra("");
			if(mBill==null){
				mBill = new Bill(); 
			}
			mBill.setRecurringSetting(rs);
		} else if (resultCode == Activity.RESULT_OK) {
			Bitmap bitmap = CommonUtil.getBitmap(this, "data", data);
			ivShowBill.setImageBitmap(bitmap);// 将图片显示在ImageView里
		}
	}
	private PopupWindow mPopVendor;
	private VendorAdapter mVendorAdapter;
	private ThreadPoolProxy mThreadPool;
	private TaskThread<List<Vendor>> mVendorTask;
	private TaskThread<List<Category>> mCateTask;

	private void save_addbill() {
		LogUtils.d("isRecurring:" + isRecurring);
		if (isRecurring) {
			addbill();
		} else if (!isRecurring) {
			bill_value(null);
			addbill();
		}
	}

	public void addbill() {
		String json = CommonUtil.bean2Json(mBill);
		LogUtils.d("json:" + json);
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				HttpResult result = HttpHelper.post(params[0], params[1],
						ContentValue.APPLICATION_JSON);
				String str = null;
				if (result != null) {
					LogUtils.d("服务器响应码：" + result.getCode());
					str = result.getString();
				}
				return str;
			}

			@Override
			protected void onPostExecute(String result) {
				if (StringUtils.isEmpty(result)) {
					PromptManager
							.showToast(BaseBillActivity.this, getResources()
									.getString(R.string.str_send_failure));
					return;
				}
				PromptManager.showToast(BaseBillActivity.this, getResources()
						.getString(R.string.str_send_success));
			}
		}.execute(url, json);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		System.out.println("选中状态:" + isChecked);
		if (isChecked) {
			isRecurring = true;
			Intent intent = new Intent(context, RecurringInvoice.class);
			startActivityForResult(intent, 21);// 请求码为-1
		}
	}
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
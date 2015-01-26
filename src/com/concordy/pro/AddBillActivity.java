package com.concordy.pro;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.concordy.pro.adapter.Itemadapter;
import com.concordy.pro.adapter.Itemadapter.DeleteListener;
import com.concordy.pro.bean.Bill;
import com.concordy.pro.bean.Bill.Item;
import com.concordy.pro.bean.Category;
import com.concordy.pro.bean.RecurringSetting;
import com.concordy.pro.bean.Vendor;
import com.concordy.pro.manager.ThreadManager.ThreadPoolProxy;
import com.concordy.pro.ui.widget.CustomerEditText;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.PromptManager;
import com.concordy.pro.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AddBillActivity extends BaseBillActivity implements OnClickListener,
		OnCheckedChangeListener {
	// 注解初始化控件
	private ImageView ivCamera;
	private ImageView ivShowBill;
	private Button btnAddItem;
	private Button btnShowHide;
	private Button btnSubBill;
	private LinearLayout llNotes;
	private ScrollView scrollView;
	private CheckBox cbRecurring;
	private CustomerEditText cetBillDate;
	private CustomerEditText cetDueDate;
	private CustomerEditText cetVendor;
	private EditText etAmount;
	private ListView lvBillItem;

	private Context context;
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;// 设置拍照操作的标志
	protected String mUri;
	private List<Category> mCateList;
	private Itemadapter mItemadapter;
	private Bill mBill;
	private boolean isRecurring;
	private Vendor mVendor;
	private Category mCategory;
	private RecurringSetting rs;
	private List<Item> items;
	private final String url = ContentValue.NEWSERVER_URL + "/"
			+ ContentValue.BILL_URL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoice);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.layout_invoice_title_bar);// 设置titleBar 布局文件
		context = this;
		isRecurring = false;
		initView();
	}

	/***
	 * 初始化控件
	 *//*
	
		items = new ArrayList<Item>();
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
				showCalendor(cetDueDate);
			}

			@Override
			public void onBtnClick() {
				showCalendor(cetBillDate);
			}
		});
		cetDueDate.setOnViewClickListener(new AutoEditTextListener() {
			@Override
			public void onEditChanged() {
				showCalendor(cetDueDate);
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
	}*/
	@Override
	protected void initView() {
		// 注解初始化控件
		ivCamera = (ImageView) findViewById(R.id.iv_camera_bill);
		ivShowBill = (ImageView) findViewById(R.id.iv_bill);
		btnAddItem = (Button) findViewById(R.id.btn_add_bill_item);
		btnShowHide = (Button) findViewById(R.id.btn_show_more_bill);
		btnSubBill = (Button) findViewById(R.id.btn_sub_bill);
		llNotes = (LinearLayout) findViewById(R.id.ll_detail);
		 scrollView = (ScrollView) findViewById(R.id.scrollview);
		cbRecurring = (CheckBox) findViewById(R.id.checkbox);
		cetBillDate = (CustomerEditText) findViewById(R.id.cet_bill_date);
		cetDueDate = (CustomerEditText) findViewById(R.id.cet_bill_duedate);
		cetVendor = (CustomerEditText) findViewById(R.id.cet_bill_vendor);

		etAmount = (EditText) findViewById(R.id.et_total_amount_bill);
		lvBillItem = (ListView) findViewById(R.id.lv_item_bill);
		
		
		
		initItem();
		
		btnSubBill.setOnClickListener(this);
		cbRecurring.setOnCheckedChangeListener(this);
		btnShowHide.setOnClickListener(this);
		ivCamera.setOnClickListener(this);
		btnAddItem.setOnClickListener(this);
		scrollView.smoothScrollTo(0, 0);
	}

	private void initItem() {
		items = new ArrayList<Item>();
		items.add(new Item());
		mItemadapter = new Itemadapter(this, items, new DeleteListener() {
			@Override
			public void delete(List<Item> item, int position) {
				item.remove(position);
				mItemadapter.notifyDataSetChanged();
				UIUtils.setListViewHeightBasedOnChildren(lvBillItem);
			}
		});
		lvBillItem.setAdapter(mItemadapter);
	}

	/**
	 * 解析数据
	 * @param result
	 */
	protected <T> List<T> parseData(String result, Class<T> clz) {
		LogUtils.d("result:" + result);
		Gson gson = new Gson();
		return gson.fromJson(result, new TypeToken<ArrayList<T>>() {
		}.getType());
	}
	@Override
	public void fillBill() {
		if(mBill==null)
			mBill = new Bill();
		mBill.setBillDate(cetBillDate.getText());
		mBill.setAmount(Float.parseFloat(etAmount.getText().toString().trim()));
		mBill.setDueDate(cetDueDate.getText());
		mBill.setVendor(mVendor);
		if(mCateList!=null)
			mCategory = mCateList.get(0); 
		mBill.setCategory(mCategory);
		mBill.setItems(items);
	}

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
			submit();
			break;
		default:
			break;
		}
	}

	private void addItem() {
		mItemadapter.items.add(new Item());
		mItemadapter.notifyDataSetChanged();
		UIUtils.setListViewHeightBasedOnChildren(lvBillItem);
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
	private ThreadPoolProxy mThreadPool;
	
	private void submit() {
		if(!CommonUtil.isNetWorkNormal(context)){
			PromptManager.showToast(ct, R.string.http_no_network);
			return;
		}
		fillBill();
		super.sendData(url,mBill);
	}
	/*@Override
	public <T> void sendData(Bill bill) {
		String json = CommonUtil.bean2Json(mBill);
		new AsyncTask<String, Void, T>() {
			@Override
			protected T doInBackground(String... params) {
				HttpResult result = null;
				try {
					result = HttpHelper.post(params[0], params[1],
							ContentValue.APPLICATION_JSON);
				} catch (AppException e) {
					e.printStackTrace();
					return (T) e;
				}
				String str = null;
				if (result != null) {
					LogUtils.d("服务器响应码：" + result.getCode());
					str = result.getString();
				}
				return (T) result;
			}
			@Override
			protected void onPostExecute(T result) {
				if(result instanceof AppException){
					AppException ae = (AppException) result;
					ae.errorTosat(ct);
					return;
				}
				PromptManager.showToast(AddBillActivity.this, getResources()
						.getString(R.string.str_send_success));
				if (StringUtils.isEmpty(result)) {
					PromptManager
							.showToast(AddBillActivity.this, getResources()
									.getString(R.string.str_send_failure));
					return;
				}
				PromptManager.showToast(AddBillActivity.this, getResources()
						.getString(R.string.str_send_success));
			}
		}.execute(url, json);
	}*/

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
}
package com.concordy.pro;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.concordy.pro.adapter.Itemadapter;
import com.concordy.pro.bean.Bill;
import com.concordy.pro.bean.Bill.Item;
import com.concordy.pro.bean.Category;
import com.concordy.pro.bean.RecurringSetting;
import com.concordy.pro.bean.Vendor;
import com.concordy.pro.http.HttpHelper;
import com.concordy.pro.http.HttpHelper.HttpResult;
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

public class InvoiceActivity extends BaseBillActivity implements OnClickListener,OnCheckedChangeListener{
	//注解初始化控件
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
	
	/*@ViewInject(R.id.actv_custom_auto)
	private AutoCompleteTextView actvVendor;
	@ViewInject(R.id.btn_custom_auto)
	private Button btnDrop;
	@ViewInject(R.id.et_bill_date_bill)
	private EditText etBillDate;
	@ViewInject(R.id.et_due_date_bill)
	private EditText etDueDate;
	
	@ViewInject(R.id.btn_calendor_bill)
	private Button btnCalendor;*/
	/*
	private  ImageView ivCamera;
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
	*/
	
	private Context context;
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;// 设置拍照操作的标志
	protected String mUri;
	private List<Category> mCateList;
	private Itemadapter	mItemadapter;
	private Bill mBill;
	private boolean isRecurring;
	private Vendor mVendor;
	private Category mCategory; 
	private RecurringSetting rs;
	private List<Item> items;
	private String billId;
	private String url = ContentValue.NEWSERVER_URL + "/"
			+ ContentValue.BILL_URL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.d("子Activity执行了...");
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
		initDa();
		initView();
		context = this;
		isRecurring = false;
		btnSubBill.setOnClickListener(this);
		cbRecurring.setOnCheckedChangeListener(this);
		btnShowHide.setOnClickListener(this);
		ivCamera.setOnClickListener(this);
		btnAddItem.setOnClickListener(this);
		//btnCalendor.setOnClickListener(this);
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
		//lvBillItem = (ListView) findViewById(R.id.lv_item_bill);
		mItemadapter = new Itemadapter(this,items,new com.concordy.pro.adapter.Itemadapter.Delete() {
			@Override
			public void delete(ArrayList<String> arr, int position) {
				//arr.remove(position);
				mItemadapter.notifyDataSetChanged();
			//setListViewHeightBasedOnChildren(lvBillItem);
			}
		}); 
		lvBillItem.setAdapter(mItemadapter);
		//setListViewHeightBasedOnChildren(lvBillItem);
		scrollView.smoothScrollTo(0,0);  
		//btnDrop.setOnClickListener(this);
		//if(mVendorAdapter!=null)
		//actvVendor.setEnabled(false);
	}
	/***
	 * 初始化数据
	 */
	private <T>void initDa() {
		//As
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			Bill bill = (Bill) bundle.get("bill");
			LogUtils.d("bill:"+bill.toString());
			if(bill!=null){
				processBill(bill);
			}
		}else{
			items = new ArrayList<Item>();
		}
		/*getVendors();
		getCategory();*/
	}
	/*private void getCategory() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				CategoryProtocol cp = new CategoryProtocol();
				mCateList = cp.load(BaseProtocol.GET_DATA,ContentValue.SERVER_URI+"/"+ContentValue.URI_CATEGORY, ContentValue.APPLICATION_JSON);
				//LogUtils.d("category:"+mCateList.toString());
			}
		}).start();
	}
	*//**
	 * 获取Vendors 
	 * *//*
	private void getVendors() {
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.setHeader("authorization","bearer "+ SharedPreferencesUtils.getString(this,ContentValue.SPFILE_TOKEN, ""));
		params.setHeader(ContentValue.CONTENT_TYPE, ContentValue.APPLICATION_JSON);
		params.setHeader(ContentValue.ACCEPT_TYPE,
				ContentValue.APPLICATION_JSON);
		http.send(HttpMethod.GET, ContentValue.SERVER_URI+"/vendor",params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String info) {
				LogUtils.d("failure...."+info);
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				if(info.statusCode==200){
					mVendorList= parseData(info.result,Vendor.class);
				}else{
					HttpError httpError = CommonUtil.json2Bean(info.result, HttpError.class);
					PromptManager.showToast(getApplicationContext(), httpError.getErrorMsg());
					return;
				}
			}
		});
	}*/
	/***
	 * 填充Bill 数据
	 * 
	 * */
	private void processBill(Bill bill) {
		if(bill==null)
			return;
		billId = bill.getId();
		if(bill.getVendor()!=null){
			mVendor = bill.getVendor();
			cetVendor.setText(bill.getVendor().getName());
		}
		String amount = bill.getAmount()+"";
		etAmount.setText(amount.trim());
		cetDueDate.setText(bill.getDueDate().split("T")[0]);
		cetBillDate.setText(bill.getBillDate().split("T")[0]);
		if(bill.getItems()!=null){
			items = bill.getItems();
			mItemadapter = new Itemadapter(this, items, null);
			lvBillItem.setAdapter(mItemadapter);
		}
		if(bill.getCategory()!=null){
			mCategory = bill.getCategory();
		}
	}
	/**
	 * 解析json数据
	 * @param <T>
	 * @param result
	 */
	protected <T> List<T> parseData(String result,Class<T> clz) {
		LogUtils.d("result:"+result);
		Gson gson = new Gson();
		//mVendorList = 
		//LogUtils.d("vendorlist:"+mVendorList);
		return gson.fromJson(result, new TypeToken<ArrayList<T>>(){}.getType());
	}
	/**
	 * 封装bill对象
	 */
	public void fillBill() { 
		if(mBill==null)
			mBill = new Bill();
		LogUtils.d("billid:"+billId);
		mBill.setId(billId);
		mBill.setBillDate(cetBillDate.getText());
		mBill.setAmount(Float.parseFloat(etAmount.getText().toString().trim()));
		mBill.setDueDate(cetDueDate.getText());
		mBill.setVendor(mVendor);
		if(mCateList!=null)
			mCategory = mCateList.get(0); 
		mBill.setCategory(mCategory);
		mBill.setItems(items);
		/*if(null != intent){			
			rs = (RecurringSetting) intent.getExtras().getSerializable("RS");
			mBill.setRecurringSetting(rs);
			System.out.println("接受的RecurringSetting数据:"+rs.toString());
		}else{
			rs = null;
			mBill.setRecurringSetting(rs);
		}*/
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
			mItemadapter.items.add(new Item()); 
			mItemadapter.notifyDataSetChanged(); 
			setListViewHeightBasedOnChildren(lvBillItem);
			break;
		case R.id.btn_show_more_bill:
			btnShowHide.setVisibility(View.GONE);
			llNotes.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_sub_bill:
			//
			//jump();
			send2Server();
			break;
		default:
			break;
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
		}else if (resultCode == Activity.RESULT_OK) {  
			//String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";     
			Bitmap bitmap = CommonUtil.getBitmap(context,"data", data);
			ivShowBill.setImageBitmap(bitmap);// 将图片显示在ImageView里  
		}
	}
	/**
	 * 获取图片并压缩图片
	 * @param imagePath
	 *//*
	private Bitmap getImage(String imagePath) {
		return BitmapFactory.decodeFile(imagePath);
	}*/
	private void send2Server() {
		LogUtils.d("isRecurring:"+isRecurring);
		fillBill();
		sendData();
	}
	/***
	 * 设置listView高度
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取listview的适配器
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(MeasureSpec.makeMeasureSpec(getResources()
					.getDisplayMetrics().widthPixels, MeasureSpec.EXACTLY), 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	@Override
	public void sendData() {
		String json = CommonUtil.bean2Json(mBill);
		LogUtils.d("json:"+json);
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				HttpResult result = HttpHelper.put(params[0], params[1],ContentValue.APPLICATION_JSON);
				String str = null;
				if(result!=null){
					LogUtils.d("服务器响应码："+result.getCode());
					str = result.getString();
				}
				return str;
			}
			@Override
			protected void onPostExecute(String result) {
				if(StringUtils.isEmpty(result))
				{
					PromptManager.showToast(
							InvoiceActivity.this,
							getResources().getString(
									R.string.str_send_failure));
					return;
				}
				PromptManager.showToast(
						InvoiceActivity.this,
						getResources().getString(
								R.string.str_send_success));
			}
		}.execute(url, json);
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		System.out.println("选中状态:"+isChecked);
		if(isChecked){   
			isRecurring=true;
			Intent intent = new Intent(context, RecurringInvoice.class);
			startActivityForResult(intent, INTENT_RECURRING_FLAG);//请求码为-1
		}
	}
}
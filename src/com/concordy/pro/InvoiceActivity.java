package com.concordy.pro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.concordy.pro.adapter.Itemadapter;
import com.concordy.pro.bean.Bill;
import com.concordy.pro.bean.Bill.Item;
import com.concordy.pro.bean.Category;
import com.concordy.pro.bean.HttpError;
import com.concordy.pro.bean.Itemlist;
import com.concordy.pro.bean.RecurringSetting;
import com.concordy.pro.bean.Vendor;
import com.concordy.pro.http.HttpHelper;
import com.concordy.pro.http.HttpHelper.HttpResult;
import com.concordy.pro.ui.widget.CalendarView;
import com.concordy.pro.ui.widget.CalendarView.OnDateClickListener;
import com.concordy.pro.ui.widget.CustomerEditText;
import com.concordy.pro.ui.widget.CustomerEditText.AutoEditTextListener;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.PromptManager;
import com.concordy.pro.utils.SharedPreferencesUtils;
import com.concordy.pro.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class InvoiceActivity extends Activity implements OnClickListener,OnCheckedChangeListener{
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
	
	/*@ViewInject(R.id.actv_custom_auto)
	private AutoCompleteTextView actvVendor;
	@ViewInject(R.id.btn_custom_auto)
	private Button btnDrop;
	@ViewInject(R.id.et_bill_date_bill)
	private EditText etBillDate;
	@ViewInject(R.id.et_due_date_bill)
	private EditText etDueDate;
*/	@ViewInject(R.id.et_total_amount_bill)
	private EditText etAmount;
	@ViewInject(R.id.lv_item_bill)
	private ListView lvBillItem;
	/*@ViewInject(R.id.btn_calendor_bill)
	private Button btnCalendor;*/
	
	private Context context;
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;// 设置拍照操作的标志
	protected String mUri;
	private List<Vendor> mVendorList;
	private Itemadapter	mItemadapter;
	private Bill mBill;
	private Item billItem;
	private boolean isRecurring;
	private Vendor mVendor;
	private Category mCategory; 
	private String mVenName,mVenId,name,price;
	private int number;
	private RecurringSetting rs;
	private List<Item> items = new ArrayList<Item>();
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
		initData();
		context = this;
		isRecurring = false;
		mCategory = new Category("9607ea59-ea15-45cf-b7ca-bb4a39117d64", "Cat 3", "");
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
				setListViewHeightBasedOnChildren(lvBillItem);
			}
		}); 
		lvBillItem.setAdapter(mItemadapter);
		setListViewHeightBasedOnChildren(lvBillItem);
		scrollView.smoothScrollTo(0,0);  
		//btnDrop.setOnClickListener(this);
		//if(mVendorAdapter!=null)
		//actvVendor.setEnabled(false);
	}
	/***
	 * 初始化数据
	 */
	private <T>void initData() {
		//As
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			Bill bill = (Bill) bundle.get("bill");
			LogUtils.d("bill:"+bill.toString());
			if(bill!=null){
				processBill(bill);
			}
		}
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
					parseData(info.result);
				}else{
					HttpError httpError = CommonUtil.json2Bean(info.result, HttpError.class);
					PromptManager.showToast(getApplicationContext(), httpError.getErrorMsg());
					return;
				}
			}
		});
	}
	/***
	 * 填充Bill 数据
	 * 
	 * */
	private void processBill(Bill bill) {
		if(bill.getVendor()!=null){
			cetVendor.setText(bill.getVendor().getName());
		}
		cetDueDate.setText(bill.getDueDate().split("T")[0]);
		cetBillDate.setText(bill.getBillDate().split("T")[0]);
		if(bill.getItemList()!=null){
			//mItemadapter.arr = bill.getItemList();
			lvBillItem.setAdapter(mItemadapter);
		}
	}
	/**
	 * 解析vendor数据
	 * @param result
	 */
	protected void parseData(String result) {
		LogUtils.d("result:"+result);
		Gson gson = new Gson();
		mVendorList = gson.fromJson(result, new TypeToken<ArrayList<Vendor>>(){}.getType());
		LogUtils.d("vendorlist:"+mVendorList);
	}
	/**
	 * 
	 */
	public void bill_value(Intent intent) { 
		int length = mItemadapter.items.size();//listView的条数
		for(int i = 0;i<length;i++){
			LinearLayout content = (LinearLayout) lvBillItem.getChildAt(i);
			EditText itemname = (EditText) content.findViewById(R.id.itemname);
			EditText itemnumber = (EditText) content.findViewById(R.id.et_bill_item);
			EditText itemprice = (EditText) content.findViewById(R.id.itemprice);
			name = itemname.getText().toString();
			String numberStr = itemnumber.getText().toString();
			if(!TextUtils.isEmpty(numberStr)){
				number = Integer.valueOf(numberStr);				
			}
			price = itemprice.getText().toString();
			billItem =new Item(name, number, price);
			items.add(billItem);
		}
		/*String bd = etBillDate.getText().toString();
		int at =Integer.parseInt(etAmount.getText().toString());
		String dd = etDueDate.getText().toString();*/
		//mBill = new Bill(bd,at,dd);
		mBill = new Bill();
		mBill.setBillDate(cetBillDate.getText());
		mBill.setAmount(Integer.parseInt(etAmount.getText().toString()));
		mBill.setDueDate(cetDueDate.getText());
		mBill.setVendor(mVendor);
		mBill.setCategory(mCategory);
		mBill.setItemList(items);
		if(null != intent){			
			rs = (RecurringSetting) intent.getExtras().getSerializable("RS");
			mBill.setRecurringSetting(rs);
			System.out.println("接受的RecurringSetting数据:"+rs.toString());
		}else{
			rs = null;
			mBill.setRecurringSetting(rs);
		}
	};
	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取listview的适配器
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
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
			save_addbill();
			break;
		/*case R.id.btn_custom_auto:
			//
			showVendor();
			break;
		case R.id.btn_calendor_bill:
			//
			//showCalendor();
			break;*/

		default:
			break;
		}
	}
	/***
	 * 显示日历控件
	 */
	private void showCalendor(final View parentView) {
		View view = View.inflate(this, R.layout.ui_pop_calender,null);
		//ViewUtils.inject(view);
		//获取日历控件对象
		final CalendarView calendar = (CalendarView)view.findViewById(R.id.calendar_bill);
				calendar.setSelectMore(false); //单选  
				ImageButton		calendarLeft = (ImageButton)view.findViewById(R.id.ib_left_calendar);
				final TextView	calendarCenter = (TextView)view.findViewById(R.id.tv_date_calendar);
				ImageButton	calendarRight = (ImageButton)view.findViewById(R.id.ib_right_calendar);
				final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					//设置日历日期
					Date date = format.parse("2015-01-01");
					calendar.setCalendarData(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
				String[] ya = calendar.getYearAndmonth().split("-"); 
				calendarCenter.setText(ya[0]+"年"+ya[1]+"月");
				calendarLeft.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//点击上一月 同样返回年月 
						String leftYearAndmonth = calendar.clickLeftMonth(); 
						String[] ya = leftYearAndmonth.split("-"); 
						calendarCenter.setText(ya[0]+"年"+ya[1]+"月");
					}
				});
				calendarRight.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//点击下一月
						String rightYearAndmonth = calendar.clickRightMonth();
						String[] ya = rightYearAndmonth.split("-"); 
						calendarCenter.setText(ya[0]+"年"+ya[1]+"月");
					}
				});
				calendar.setOnItemClickListener(new OnDateClickListener() {
					@Override
					public void OnItemClick(Date selectedStartDate, Date selectedEndDate,
							Date downDate) {
						//Log
						if(calendar.isSelectMore()){
							Toast.makeText(getApplicationContext(), format.format(selectedStartDate)+"到"+format.format(selectedEndDate), Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getApplicationContext(), format.format(downDate), Toast.LENGTH_SHORT).show();
							((CustomerEditText) parentView).setText( format.format(downDate));
							dismissPop(mPopVendor);
						}
					}
				});
				/*//设置控件监听，可以监听到点击的每一天（大家也可以在控件中根据需求设定）
				calendar.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void OnItemClick(Date selectedStartDate,
							Date selectedEndDate, Date downDate) {
						if(calendar.isSelectMore()){
							Toast.makeText(getApplicationContext(), format.format(selectedStartDate)+"到"+format.format(selectedEndDate), Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getApplicationContext(), format.format(downDate), Toast.LENGTH_SHORT).show();
						}
					}
				});*/
			setPopStyle(view, parentView);	
	}
	/**
	 * 显示Vendor
	 */
	private void showVendor(final View parentView) {
		//Toast.makeText(this, "右边按钮", 0).show();
		View view = View.inflate(this, R.layout.ui_pop_item, null);
		ListView lv = (ListView) view.findViewById(R.id.lv);
		if(mVendorAdapter==null)
			mVendorAdapter = new VendorAdapter(this,mVendorList);
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
		//SimpleAdapter adp = new SimpleAdapter(getApplicationContext(), data, R.layout.ui_pop_item_lv, null, null);
		setPopStyle(view,parentView);
	}
	/***
	 * 设置Pop样式，位置
	 * @param parentView 挂载到哪个View上
	 * @param postionView
	 */
	private void setPopStyle( View parentView,View postionView) {
		int[] local = new int[2];
		postionView.getLocationOnScreen(local);
		mPopVendor = new PopupWindow(parentView,-2,-2);
		// lv.setItemsCanFocus(false); 
		mPopVendor.setFocusable(true);
		mPopVendor.setOutsideTouchable(true);
		ColorDrawable colorDrawable = new ColorDrawable(Color.WHITE);// 创建了一个透明颜色的背景
		mPopVendor.setBackgroundDrawable(colorDrawable);
		mPopVendor.showAtLocation(parentView, Gravity.TOP|Gravity.LEFT, 0,local[1]+postionView.getHeight());
	}
	/***
	 * 销毁pop
	 */
	private void dismissPop(PopupWindow pop)
	{
		if(pop!=null)
			pop.dismiss();
		pop = null;
	}
	class VendorAdapter extends BaseAdapter{
		private List<Vendor> mVendors;
		private Context ct;
		public VendorAdapter(Context ct,List<Vendor> vendors){
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
			if(convertView ==null){
				tv = new TextView(ct); 
			}else{
				tv = (TextView) convertView;
			}
			tv.setText(mVendors.get(position).getName());
			return tv;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 90 ){
			bill_value(data);
		}else if (resultCode == Activity.RESULT_OK) {  
			String sdStatus = Environment.getExternalStorageState();  
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
				Log.i("TestFile",  
						"SD card is not avaiable/writeable right now.");  
				return;  
			}  
			String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";     
			Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
			Bundle bundle = data.getExtras();  
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  

			FileOutputStream b = null;  
			File file = new File("/sdcard/myImage/");  
			file.mkdirs();// 创建文件夹  
			String fileName = "/sdcard/myImage/"+name;  
			try {  
				b = new FileOutputStream(fileName);  
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
			} catch (FileNotFoundException e) {  
				e.printStackTrace();  
			} finally {  
				try {  
					b.flush();  
					b.close();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
			}  
			ivShowBill.setImageBitmap(bitmap);// 将图片显示在ImageView里  
		}
	}
	private PopupWindow mPopVendor;
	private VendorAdapter mVendorAdapter;
	/**
	 * 获取图片并压缩图片
	 * @param imagePath
	 */
	private Bitmap getImage(String imagePath) {
		return BitmapFactory.decodeFile(imagePath);
	}

	private void save_addbill() {
		LogUtils.d("isRecurring:"+isRecurring);
		if(isRecurring){
			addbill();
		}else if(!isRecurring){
			bill_value(null);
			addbill();
		}
	}



	/**
	 * 
	 */
	public void addbill() {
		String json = CommonUtil.bean2Json(mBill);
		LogUtils.d("json:"+json);
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				HttpResult result = HttpHelper.post(params[0], params[1],ContentValue.APPLICATION_JSON);
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
			startActivityForResult(intent, 21);//请求码为-1
		}

	}
}
package com.concordy.pro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.concordy.pro.bean.HttpError;
import com.concordy.pro.bean.ServiceURL;
import com.concordy.pro.bean.User;
import com.concordy.pro.http.HttpHelper;
import com.concordy.pro.http.HttpHelper.HttpResult;
import com.concordy.pro.manager.AppException;
import com.concordy.pro.manager.BaseApplication;
import com.concordy.pro.manager.ThreadManager;
import com.concordy.pro.manager.ThreadManager.ThreadPoolProxy;
import com.concordy.pro.receiver.SmsReceiver;
import com.concordy.pro.receiver.SmsReceiver.SmsResultListener;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.PromptManager;
import com.concordy.pro.utils.SharedPreferencesUtils;
import com.concordy.pro.utils.StringUtils;

public class RegisterActivity extends BaseActivity implements
		OnClickListener {
	private Button regist;
	private Button reqPhoneCode;
	private EditText etUsername;
	private EditText etVerfyCode;
	private Button mBtnBack;
	private TextView mTitle;
	private SmsReceiver mReceiver;
	private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private static final int DATA_OK = 200;
	private static final int DATA_FAILED = -0x01;
	/******** 验证码请求地址 **********/
	private String mVerifyUrl = ServiceURL.VERICAL_REQUEST;
	private IntentFilter mItFilter;
	private int mSeconds = 60;
	private boolean mTimerRunning = true;
	private ThreadPoolProxy mLongPool;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, layout);// 设置titleBar
		mLongPool = ThreadManager.getLongPool();
		mActivities.add(this);
		initView();
	}
	@Override
	protected void initView() {
		regist = (Button) this.findViewById(R.id.btn_done_regist);
		reqPhoneCode = (Button) this.findViewById(R.id.btn_request_code);
		etUsername = (EditText) this.findViewById(R.id.et_phone_number);
		etVerfyCode = (EditText) this.findViewById(R.id.et_verical_code);
		mBtnBack = (Button) this.findViewById(R.id.btn_left);
		mTitle = (TextView) this.findViewById(R.id.tv_txt_title);
		
	
		
		regist.setOnClickListener(this);
		reqPhoneCode.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		mTitle.setText(getString(R.string.tv_title_regist));
		String localPhone = getPhone();
		if (!StringUtils.isEmpty(localPhone))
			etUsername.setText(localPhone);
	}

	@Override
	protected void onResume() {
		super.onResume();
		resetView();
	}

	private void resetView() {
		reqPhoneCode.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	/**
	 * 获取本机号码
	 * 
	 * @return
	 */
	private String getPhone() {
		mTeleManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String phone = mTeleManager.getLine1Number();
		if (StringUtils.isEmpty(phone)) {
			return "";
		}
		phone = phone.replace("+86", "");
		return phone;
	}

	/*** 点击事件 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_done_regist:// 检验注册
			checkVerifyCode();
			break;
		case R.id.btn_request_code:// 获取手机验证码
			requestVercicalCode();
			// gotoNext();
			break;
		case R.id.btn_left:// 标题栏返回按钮事件
			finish();
			break;
		}
	}
	/***
	 * 校验验证码
	 * 
	 * @param <T>
	 */
	private <T> void checkVerifyCode() {
		// PromptManager.showToast(this, "被点击了");
		//BaseApplication
		if (CommonUtil.isNetworkAvailable(getApplicationContext()) == ContentValue.NO_NETWORK) {
			PromptManager.showToast(getApplicationContext(), getResources()
					.getString(R.string.err_no_network));
			return;
		}
		String verCode = etVerfyCode.getText().toString();
		String phone  = etUsername.getText().toString();
		if (StringUtils.isEmpty(verCode)) {
			PromptManager.showToast(getApplicationContext(),
					R.string.err_vercode_empty);
		} else {
			if (verCode.length() < 6) {
				PromptManager.showToast(getApplicationContext(),
						R.string.err_vercode_type_error);
				return;
			}
			// 请求服务器等待服务器返回结果
			String url = ContentValue.SERVER_URI + "/"
					+ ContentValue.VERICAL_REQUEST;
			url = url + "?" + ContentValue.VERICAL_PHONE + "="
					+ etUsername.getText().toString() + "&"
					+ ContentValue.VERICAL_REQUEST + "="
					+ etVerfyCode.getText().toString();
			com.concordy.pro.utils.LogUtils.d("访问地址：" + url);
			new AsyncTask<String, Void, T>() {
				@Override
				protected void onPreExecute() {
					showLoadDialog();
				};
				@Override
				protected T doInBackground(String... params) {
					/*HttpResult result = HttpHelper.get(params[0], params[1]);
					// LogUtils.d("result"+);
					if (result != null && result.getCode() == 200)
						return regist();*/
					return null;
				}
				@Override
				protected void onPostExecute(T t) {
					if (t == null)
						return;
					if (t instanceof User) {
						User user = (User) t;
						LogUtils.d("user:" + user.toString());
						PromptManager.showToast(ct, ct.getResources()
								.getString(R.string.str_regist_success));
						goNext(user);
					} else {
						HttpError hError = (HttpError) t;
						if (hError != null)
							PromptManager.showToast(ct, ct.getResources()
									.getString(R.string.err_server_error));
						return;
					}
				}
			}.execute(url, ContentValue.APPLICATION_JSON);
		}
		regist();
	}

	/***
	 * 注册用户
	 */
	private void regist()  {
		final Handler rh = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what ==DATA_FAILED){
					((AppException) msg.obj).errorTosat(ct);
					return;
				}else{
					String result = (String) msg.obj;
					if(msg.what==DATA_OK){
						User user = CommonUtil.json2Bean(result, User.class);
						goNext(user);
					}else{
						HttpError error = CommonUtil.json2Bean(result, HttpError.class);
						PromptManager.showToast(ct, error.getErrorMsg());
						return;
					}
				}
			}
		};
		mLongPool.execute(new Thread(){
			@Override
			public void run() {
				Message msg= Message.obtain();
				// 创建User Bean对象封装数据
				User user = new User();
				user.setPhone(etUsername.getText().toString());
				user.setPassword(etVerfyCode.getText().toString());
				user.setConfirmPassword(etVerfyCode.getText().toString());
				user.setEmail("123456@qq.com");
				// 将对象转换成json字符串提交服务器
				String json = CommonUtil.bean2Json(user);
				try {
					HttpResult result = HttpHelper.post(ServiceURL.REGIST, json,
							ContentValue.APPLICATION_JSON);
					if(result!=null){
						msg.what = result.getCode();
						msg.obj = result.getString();
					}
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = DATA_FAILED;
					msg.obj = e;
				}
				rh.sendMessage(msg);
			}
		});
	}

	/******** 解析服务器数据 ***********/
	protected <T> T parseData(HttpResult result) {
		/*
		 * LogUtils.d("Error_Code:" + result.getCode() + ",结果：" +
		 * result.getString());
		 */
		/*String json = "";
		if (result != null) {
			json = result.getString();
			com.concordy.pro.utils.LogUtils.d("regist:" + json);
			if (result.getCode() == 200)
				// HttpHelper.post(url, json, type);
				return (T) CommonUtil.json2Bean(json, User.class);
			else {
				if (StringUtils.isEmpty(json))
					return null;
				return (T) CommonUtil.json2Bean(json, HttpError.class);
			}
		}*/
		return null;
	}

	/**
	 * 跳转下一步骤
	 * 
	 * @param user
	 */
	private void goNext(User user) {
		if (user != null)
			SharedPreferencesUtils.saveString(ct, ContentValue.SPFILE_TOKEN,
					user.getAccessToken());
		Intent intent = new Intent(this, PinSetActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("user", user);
		intent.putExtras(bundle);
		// 方式1：调用webservice登录Api 登录并跳转
		// 方式2：直接跳转，省略登录步骤。
		startActivity(intent);
	}

	/**
	 * 请求验证码
	 */
	private void requestVercicalCode() {
		mTimerRunning = true;
		BaseApplication ba = (BaseApplication)getApplication();
		if (!ba.isNetworkConnected()) {
			PromptManager.showToast(ba.getBaseContext(), getResources()
					.getString(R.string.err_no_network));
			return;
		}
		if (etUsername == null|| StringUtils.isEmpty(etUsername.getText().toString())) {
			PromptManager.showToast(getApplicationContext(), getResources()
					.getString(R.string.err_vercode_empty));
			return;
		}
		requestCode();
	}

	private void requestCode() {
		mLongPool.execute(new Thread() {
			@Override
			public void run() {
				Message msg;
				HttpHelper.sendNotice();
				while (mTimerRunning) {
					 msg = Message.obtain();
					// 获取一个消息对象
					msg.arg1 = mSeconds;// 设置参数
					msg.what = 0;// 参数类型
					mSeconds--;
					try {
						// 休眠1秒
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handle.sendMessage(msg);// 发送消息
				}
			}
		});
	}

	/******** 显示dialog *********/
	protected void showLoadDialog() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("注册中,请稍候...");
		dialog.setCanceledOnTouchOutside(false);
		// dialog.set
		dialog.show();
	}

	/****** 隐藏dialog *******/
	protected void dismissDialog() {
		if (dialog != null)
			dialog.dismiss();
	}

	/**** 初始化广播接收者 ******/
	protected void initReceiver() {
		if (mReceiver == null)
			mReceiver = new SmsReceiver();
		// 设置过滤器对象
		mItFilter = new IntentFilter(SMS_ACTION);
		mItFilter.setPriority(Integer.MAX_VALUE);// 设置广播优先级
		// 设置广播回调接口
		mReceiver.setOnReceiveSmsListener(new SmsResultListener() {
			@Override
			public void onReceive(String msg) {
				if (!StringUtils.isEmpty(msg)) {
					etVerfyCode.setText(msg);
				}
			}
		});
		// 注册广播
		registerReceiver(mReceiver, mItFilter);
	}

	/*** 倒计时 **//*
	private void countDown() {
		if (mTaskThread == null)
			mTaskThread = new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg;
					while (mTimerRunning) {
						msg = Message.obtain();// 获取一个消息对象
						msg.arg1 = mSeconds;// 设置参数
						msg.what = 0;// 参数类型
						handle.sendMessage(msg);// 发送消息
						mSeconds--;
						try {
							Thread.sleep(1000);// 休眠1秒
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		mTaskThread.start();
	}*/

	private Handler handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 设置登陆按钮显示状态
			if (mTimerRunning && msg.arg1 >= 0) {
				reqPhoneCode.setClickable(false);
				reqPhoneCode
						.setBackgroundResource(R.drawable.verical_btn_unclickable);
				reqPhoneCode.setText("("
						+ msg.arg1
						+ ")"
						+ getApplicationContext().getResources().getString(
								R.string.btn_request_recode));
				regist.setVisibility(View.VISIBLE);
			} else {
				reqPhoneCode.setClickable(true);
				reqPhoneCode
						.setBackgroundResource(R.drawable.verical_btn_clickable);
				reqPhoneCode.setText(getApplicationContext().getResources()
						.getString(R.string.btn_request_code));
				// login.setVisibility(View.INVISIBLE);
				mTimerRunning = false;
				// 销毁线程
				mSeconds = 60;
				mTaskThread = null;
			}
		}
	};
	private TelephonyManager mTeleManager;
	private Thread mTaskThread;
	private ProgressDialog dialog;

	@Override
	protected void onStop() {
		super.onStop();
		dismissDialog();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播
		if (mReceiver != null)
			unregisterReceiver(mReceiver);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		dismissDialog();
	}
}

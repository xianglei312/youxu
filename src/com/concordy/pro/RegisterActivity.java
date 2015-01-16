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

import com.concordy.pro.R;
import com.concordy.pro.bean.HttpError;
import com.concordy.pro.bean.User;
import com.concordy.pro.bean.VerifyCode;
import com.concordy.pro.http.HttpHelper;
import com.concordy.pro.http.HttpHelper.HttpResult;
import com.concordy.pro.receiver.SmsReceiver;
import com.concordy.pro.receiver.SmsReceiver.SmsResultListener;
import com.concordy.pro.ui.base.BaseTitleActivity;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.PromptManager;
import com.concordy.pro.utils.SharedPreferencesUtils;
import com.concordy.pro.utils.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RegisterActivity extends BaseTitleActivity implements
		OnClickListener {
	@ViewInject(R.id.btn_done_regist)
	private Button regist;
	@ViewInject(R.id.btn_request_code)
	private Button reqPhoneCode;
	@ViewInject(R.id.et_phone_number)
	private EditText etUsername;
	@ViewInject(R.id.et_verical_code)
	private EditText etVerivalCode;
	@ViewInject(R.id.btn_left)
	private Button mBtnBack;
	@ViewInject(R.id.tv_txt_title)
	private TextView mTitle;
	private SmsReceiver mReceiver;
	private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	/******** 验证码请求地址 **********/
	private String mVerifyUrl = ContentValue.SERVER_URI + "/"
			+ ContentValue.VERICAL_REQUEST;
	private String mRegistUrl = ContentValue.SERVER_URI + "/"
			+ ContentValue.REGIST_URI;
	private IntentFilter mItFilter;
	private int mSeconds = 60;
	private boolean mTimerRunning = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, layout);// 设置titleBar
		initView();
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		String localPhone = getPhone();
		regist.setOnClickListener(this);
		reqPhoneCode.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		mTitle.setText(getString(R.string.tv_title_regist));
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
		// if(m)
		// LogUtils.d(mTeleManager.getLine1Number());
		String phone = mTeleManager.getLine1Number();
		if (StringUtils.isEmpty(phone)) {
			return "";
		}
		phone = phone.replace("+86", "");
		LogUtils.d("截取后的phone：" + phone);
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
	/***
	 * 校验验证码
	 * 
	 * @param <T>
	 */
	private <T> void checkVerifyCode() {
		// PromptManager.showToast(this, "被点击了");
		if (CommonUtil.isNetworkAvailable(getApplicationContext()) == ContentValue.NO_NETWORK) {
			PromptManager.showToast(getApplicationContext(), getResources()
					.getString(R.string.err_no_network));
			return;
		}
		String etCode = etVerivalCode.getText().toString();
		if (StringUtils.isEmpty(etCode)) {
			PromptManager.showToast(getApplicationContext(),
					R.string.err_vercode_empty);
		} else {
			if (etCode.length() < 6) {
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
					+ etVerivalCode.getText().toString();
			com.concordy.pro.utils.LogUtils.d("访问地址：" + url);
			new AsyncTask<String, Void, T>() {
				@Override
				protected void onPreExecute() {
					showLoadDialog();
				};

				@Override
				protected T doInBackground(String... params) {
					HttpResult result = HttpHelper.get(params[0], params[1]);
					// LogUtils.d("result"+);
					if (result != null && result.getCode() == 200)
						return regist();
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
	}

	/***
	 * 注册用户
	 * 
	 * @param <T>
	 */
	private <T> T regist() {
		// 创建User Bean对象封装数据
		User user = new User();
		user.setPhone(etUsername.getText().toString());
		user.setPassword(etVerivalCode.getText().toString());
		user.setConfirmPassword(etVerivalCode.getText().toString());
		user.setEmail("123456@qq.com");
		// 将对象转换成json字符串提交服务器
		String json = CommonUtil.bean2Json(user);
		com.concordy.pro.utils.LogUtils.d("regist:" + json);
		HttpResult result = HttpHelper.post(mRegistUrl, json,
				ContentValue.APPLICATION_JSON);
		if (result != null) {
			return parseData(result);
		}
		return null;
	}

	/******** 解析服务器数据 ***********/
	protected <T> T parseData(HttpResult result) {
		/*
		 * LogUtils.d("Error_Code:" + result.getCode() + ",结果：" +
		 * result.getString());
		 */
		String json = "";
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
		}
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
		if (CommonUtil.isNetworkAvailable(getApplicationContext()) == ContentValue.NO_NETWORK) {
			PromptManager.showToast(getApplicationContext(), getResources()
					.getString(R.string.err_no_network));
			return;
		}
		if (StringUtils.isEmpty(etUsername.getText().toString())
				|| etUsername == null) {
			PromptManager.showToast(getApplicationContext(), getResources()
					.getString(R.string.err_vercode_empty));
			return;
		}
		new AsyncTask<String, Void, String>() {
			@Override
			protected void onPreExecute() {
				countDown();
				// 注册广播监听短信验证码
				initReceiver();
			};

			@Override
			protected String doInBackground(String... params) {
				// 通知服务器发送短信验证码，更新button按钮动作。
				// CommonUtil.noticeSMSCode(etUsername.getText().toString())
				HttpResult result = HttpHelper.post(params[0], "=" + params[1],
						ContentValue.APPLICATION_FORM);
				if (result != null) {
					String str = result.getString();
					com.concordy.pro.utils.LogUtils.d("请求错误：" + str);
					return str;
				}
				return "";
			}

			@Override
			protected void onPostExecute(String result) {
				if (!"".equals(result)) {
					// com.leo.demo.utils.LogUtils.d(result.getString());
					PromptManager
							.showToast(RegisterActivity.this, getResources()
									.getString(R.string.str_send_success));
				} else {
					PromptManager.showToast(RegisterActivity.this,
							getResources().getString(R.string.err_send_failed));
				}
				com.concordy.pro.utils.LogUtils.d("手机验证码：" + result);
				if (!StringUtils.isEmpty(result)) {
					VerifyCode code = CommonUtil.json2Bean(result,
							VerifyCode.class);
					etVerivalCode.setText(code.getSendCode());
				}
			}
		}.execute(mVerifyUrl, etUsername.getText().toString());
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
		// TODO Auto-generated method stub
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
					etVerivalCode.setText(msg);
				}
			}
		});
		// 注册广播
		registerReceiver(mReceiver, mItFilter);
	}

	/*** 倒计时 **/
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
	}

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

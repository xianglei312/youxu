package com.concordy.pro;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.concordy.pro.bean.HttpError;
import com.concordy.pro.bean.User;
import com.concordy.pro.http.HttpHelper;
import com.concordy.pro.http.HttpHelper.HttpResult;
import com.concordy.pro.ui.base.BaseTitleActivity;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.PromptManager;
import com.concordy.pro.utils.SharedPreferencesUtils;
import com.concordy.pro.utils.StringUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoginActivity extends BaseTitleActivity implements OnClickListener {
	@ViewInject(R.id.et_pwd)
	private EditText etPwd;
	@ViewInject(R.id.et_username)
	private EditText etUsername;
	@ViewInject(R.id.tv_forget_pwd_login)
	private TextView mFindPwd;
	@ViewInject(R.id.btn_login)
	private Button login;
	@ViewInject(R.id.btn_left)
	private Button mBtnLeft;
	@ViewInject(R.id.btn_right)
	private Button mBtnRight;
	@ViewInject(R.id.tv_txt_title)
	private TextView mTvTitle;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, super.layout);// 设置titleBar
		initView();
	}

	/********* 校验输入信息 ***********/
	protected void checkEdittext() {
		if (StringUtils.isEmpty(etUsername.getText().toString())) {
			PromptManager.showToast(ct, "用户名不能为空");
			return;
		}
		if (StringUtils.isEmpty(etPwd.getText().toString())) {
			PromptManager.showToast(ct, "密码不能为空");
			return;
		}
		checkLogin();
	}

	/**
	 * 解析结果，处理登录后续动作
	 * 
	 * @param result
	 *            服务器返回结果
	 */
	protected void checkLogin() {
		if (CommonUtil.isNetworkAvailable(getApplicationContext()) == ContentValue.NO_NETWORK) {
			PromptManager.showToast(getApplicationContext(), getResources()
					.getString(R.string.err_no_network));
			return;
		}
		String url = ContentValue.SERVER_URL + "/" + ContentValue.LOGIN_URI;
		String json = CommonUtil.bean2Json(new User(etUsername.getText()
				.toString(), etPwd.getText().toString()));
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.setHeader(ContentValue.CONTENT_TYPE,
				ContentValue.APPLICATION_JSON);
		params.setHeader(ContentValue.ACCEPT_TYPE,
				ContentValue.APPLICATION_JSON);
		StringEntity entity;
		try {
			entity = new StringEntity(json);
			params.setBodyEntity(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
			}

			@Override
			public void onStart() {
				super.onStart();
				showLoadDialog();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				arg0.printStackTrace();
				PromptManager.showToast(LoginActivity.this, arg1);
				dismissDialog();
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				LogUtils.d("code:" + info.statusCode + ",result:" + info.result);
				/*PromptManager.showToast(LoginActivity.this, "code:"
						+ info.statusCode + "\n result:" + info.result);*/
				dismissDialog(); 
				if(info.statusCode==200){
					User user = CommonUtil.json2Bean(info.result, User.class);
					saveUserinfo(user);
					Intent it = new Intent(ct, MainActivity.class);
					it.putExtra("user", user);
					startActivity(it);
				}
			}
		});
		/*
		 * mLoginTask = new LoginAsyncTask(); mLoginTask.execute(url, json);
		 */
	}

	/********** 登录成功存储User信息 ***********/
	protected void saveUserinfo(User user) {
		LogUtils.d("user:" + user.toString());
		if (user != null) {
			SharedPreferencesUtils.saveString(ct, ContentValue.SPFILE_TOKEN,
					user.getAccessToken());
			SharedPreferencesUtils.saveString(ct, ContentValue.SPFILE_USERNAME,
					user.getUsername());
		}
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		// login = (Button) findViewById(R.id.btn_login);
		String username = SharedPreferencesUtils.getString(ct,
				ContentValue.SPFILE_USERNAME, "");
		if (etUsername != null && !StringUtils.isEmpty(username))
			etUsername.setText(username);
		mTvTitle.setText(getResources().getString(R.string.str_signin));
		mFindPwd.setOnClickListener(this);
		login.setOnClickListener(this);
		mBtnRight.setText(R.string.str_regist);
		mBtnRight.setOnClickListener(this);
		mBtnLeft.setOnClickListener(this);
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

	@Override
	public void onClick(View v) {
		Intent it;
		switch (v.getId()) {
		case R.id.btn_right:// 头部信息注册按钮
			it = new Intent(ct, RegisterActivity.class);
			startActivity(it);
			break;
		case R.id.btn_left:// 头部返回按键
			onDestroy();
			break;
		case R.id.btn_login:// 登录按钮
			checkEdittext();
			break;
		case R.id.tv_forget_pwd_login:// 忘记密码
			/*
			 * it= new Intent(ct, RegisterActivity.class); startActivity(it);
			 */
			PromptManager.showToast(ct, "忘记密码？");
			break;
		}
	}

	/******** 显示dialog *********/
	protected void showLoadDialog() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("登录中,请稍候...");
		dialog.setCanceledOnTouchOutside(false);
		// dialog.set
		dialog.show();
	}

	/****** 隐藏dialog *******/
	protected void dismissDialog() {
		if (dialog != null)
			dialog.dismiss();
	}

	private int httpCode;
	private LoginAsyncTask mLoginTask;

	class LoginAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			LogUtils.d("请求登录中....");
			HttpResult httpResult = HttpHelper.post(params[0], params[1],
					ContentValue.APPLICATION_JSON);
			String result = "";
			if (httpResult != null) {
				result = httpResult.getString();
				httpCode = httpResult.getCode();
			}
			return result;
		}

		@Override
		protected void onPreExecute() {
			showLoadDialog();
		}

		@Override
		protected void onPostExecute(String result) {
			// super.onPostExecute(result);
			dismissDialog();
			if (StringUtils.isEmpty(result)) {
				PromptManager.showToast(ct,
						ct.getResources().getString(R.string.err_server_error));
				return;
			}
			if (httpCode == 200) {
				User user = CommonUtil.json2Bean(result, User.class);
				saveUserinfo(user);
				Intent it = new Intent(ct, MainActivity.class);
				it.putExtra("user", user);
				startActivity(it);
			} else {
				HttpError err = CommonUtil.json2Bean(result, HttpError.class);
				PromptManager.showToast(ct, "error:" + err.getErrorMsg());
				return;
			}
		}
	}
}

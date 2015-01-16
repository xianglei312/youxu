package com.concordy.pro.receiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.concordy.pro.utils.ContentValue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	//private static final String SERVER_PHONE = "10086";
	private SmsMessage mSmsMsg;
	private String phone;
	private SmsResultListener smsListener;

	@Override
	public void onReceive(Context context, Intent intent) {
		init(context, intent);
	}
	private void init(Context ct, Intent it) {
		// 得到短信数据对象数组
		Object[] objs = (Object[]) it.getExtras().get("pdus");
		for (Object obj : objs) {// 遍历数据信息
			mSmsMsg = SmsMessage.createFromPdu((byte[]) obj);// 得到短信
			phone = mSmsMsg.getOriginatingAddress();// 得到短信中心号码
			System.out.println("phone:" + phone);
			if (ContentValue.SERVER_PHONE.equals(phone)||ContentValue.SERVER_PHONE_DX.equals(phone)||ContentValue.SERVER_PHONE_YD.equals(phone)) {
				String body = mSmsMsg.getMessageBody();// 得到短信内容
				String verifyCode = getVerifyCode(body);
				System.out.println("verifyCode:" + verifyCode);
				smsListener.onReceive(verifyCode);
			}
		}
	}
	/*****截取六位数验证码*****/
	private String getVerifyCode(String body) {
		// 6是验证码的位数一般为六位
		Pattern continuousNumberPattern = Pattern.compile("(?<![0-9])([0-9]{"
				+ 6 + "})(?![0-9])");
		Matcher m = continuousNumberPattern.matcher(body);
		String dynamicPassword = "";
		while (m.find()) {
			System.out.print(m.group());
			dynamicPassword = m.group();
		}
		return dynamicPassword;
	}
	/*****回调接口****/
	public interface SmsResultListener {
		public void onReceive(String msg);
	}
	public void setOnReceiveSmsListener(SmsResultListener listener) {
		this.smsListener = listener;
	}

}

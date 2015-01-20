package com.concordy.pro.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.concordy.pro.bean.Bill;
import com.google.gson.Gson;
/**
 * 
 * @author Scleo
 *
 */
public class CommonUtil {
	/**
	 * Dialog显示框
	 * @param context
	 * @param message
	 */
	public static void showInfoDialog(Context context, String message) {
		showInfoDialog(context, message, "提示", "确定", null);
	}
	public static void showInfoDialog(Context context, String message,
			String titleStr, String positiveStr,
			DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setTitle(titleStr);
		localBuilder.setMessage(message);
		if (onClickListener == null)
			onClickListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			};
		localBuilder.setPositiveButton(positiveStr, onClickListener);
		localBuilder.show();
	}
	/**
	 * MD5加密处理
	 * @param paramString
	 * @return
	 */
	public static String md5(String paramString) {
		String returnStr;
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			returnStr = byteToHexString(localMessageDigest.digest());
			return returnStr;
		} catch (Exception e) {
			return paramString;
		}
	}

	/**
	 * 将指定byte数组转换成16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/**
	 * 判断当前是否有可用的网络以及网络类型 
	 * 
	 * @param context
	 * @return 0：无网络 1：WIFI 2：CMWAP 3：CMNET
	 */
	public static int isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return 0;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return 1;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							String extraInfo = netWorkInfo.getExtraInfo();
							if ("cmwap".equalsIgnoreCase(extraInfo)
									|| "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
								return 2;
							}
							return 3;
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 创建图片
	 * @param d
	 * @param p
	 * @return
	 */
	private static Drawable createDrawable(Drawable d, Paint p) {
		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，
		return new BitmapDrawable(bitmap);
	}

	/** 设置Selector。 */
	public static StateListDrawable createSLD(Context context, Drawable drawable) {
		StateListDrawable bg = new StateListDrawable();
		int brightness = 50 - 127;
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness,// 改变亮度
				0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

		Drawable normal = drawable;
		Drawable pressed = createDrawable(drawable, paint);
		bg.addState(new int[] { android.R.attr.state_pressed, }, pressed);
		bg.addState(new int[] { android.R.attr.state_focused, }, pressed);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}
	/**
	 * 将文件转换成Bitmap
	 * @param ct
	 * @param fileName
	 * @return
	 */
	public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
		Bitmap image = null;
		AssetManager am = ct.getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 获取SharedPreferences文件中的token
	 * @param 
	 */
	public static boolean hasToken(Context ct) {
		String token = SharedPreferencesUtils.getString(ct, "token", "");
		if (TextUtils.isEmpty(token)) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 设置Listview中子项的高度
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	/**
	 * 通知服务器，发送短信验证码到指定号码
	 *//*
	public static String noticeSMSCode(String number) {
		String url = ContentValue.SERVER_URI+"/"+ContentValue.VERICAL_REQUEST;
		NetUtils net = new NetUtils();
		return net.doPostOfHttpClientFor(url, "="+number);
	}*/
	/**
	 * 将对象转换成json字符串
	 * @param t bean对象
	 * @return
	 */
	public static <T> String bean2Json(T t){
		Gson gs = new Gson();
		String json = gs.toJson(t);
		//LogUtils.d("转换的结果："+json);
		return json;
	}
	/**
	 * 将json映射成bean对象
	 * @param result json字符串
	 * @param clazz bean对象字节码
	 * @return
	 */
	public static <T>T json2Bean(String result,Class<T> clazz){
		if(StringUtils.isEmpty(result))
			return null;
		Gson gs = new Gson();
		T t = gs.fromJson(result, clazz);
		return t;
	}
	/**
	 * 将字符型的毫米值转换成Date
	 * @param mills
	 * @return
	 */
	public static String formate2String(String mills){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long l = Long.parseLong(mills);
		Date date;
		date = new Date(l);
		String format = sdf.format(date);
		return format;
	}
	/*****
	 * 按金额排序
	 * @param list
	 * @param isDesc
	 */
	public static void sortByAmount(List<Bill> list,final boolean isDesc){
		if(list==null)
			return;
		Collections.sort(list, new Comparator<Bill>() {
			@Override
			public int compare(Bill bf, Bill af) {
				if(bf.getAmount()==af.getAmount())
					return 0;
				if(isDesc)//如果是升序
				{
					if(bf.getAmount()>af.getAmount()){
						return 1;
					}else{
						return -1;
					}
				}else{
					if(bf.getAmount()>af.getAmount()){
						return -1;
					}else{
						return 1;
					}
				}
				//return 0;
			}
		});
	}
	/***
	 * 按客户升降序排序
	 * @param list
	 * @param isDesc
	 */
	public static void sortByVendor(List<Bill> list,final boolean isDesc){
		if(list==null)
			return;
		Collections.sort(list, new Comparator<Bill>() {
			@Override
			public int compare(Bill bf, Bill af) {
				if(bf.getVendor().getName()!=null&&af.getVendor().getName()!=null){
					if(isDesc)//如果是降序
						return af.getVendor().getName().compareTo(bf.getVendor().getName());
					return bf.getVendor().getName().compareTo(af.getVendor().getName());
				}
				return 0;
			}
		});
	}
	/***
	 * 按时间排序
	 * @param list
	 * @param isDesc
	 */
	public static void sortByTime(List<Bill> list,final boolean isDesc){
		if(list==null)
			return;
		Collections.sort(list, new Comparator<Bill>() {
			@Override
			public int compare(Bill bf, Bill af) {
				if(bf.getBillDate()!=null&&af.getBillDate()!=null){
					if(isDesc)//如果是降序
						return af.getBillDate().compareTo(bf.getBillDate());
					return bf.getBillDate().compareTo(af.getBillDate());
				}
				return 0;
			}
		});
	}
	/****
	 * 获取图片
	 * @param txt 
	 * @param key 
	 * @param data 
	 * @return 
	 */
	public static Bitmap getBitmap(Context txt,String key,Intent data) {
		if (!FileUtils.isSDCardAvailable()) { // 检测sd是否可用  
			Log.i("TestFile",  
					"SD card is not avaiable/writeable right now.");  
			PromptManager.showToast(txt, "SdCard 不可用");
			return null;  
		}  
		String fileName = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";     
		Bundle bundle = data.getExtras();  
		Bitmap bitmap = (Bitmap) bundle.get(key);// 获取相机返回的数据，并转换为Bitmap图片格式  
		FileOutputStream b = null;  
		File file = new File(FileUtils.getExternalStoragePath(),fileName);  
		file.mkdirs();// 创建文件夹  
		try {  
			b = new FileOutputStream(file);  
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
		return bitmap;
	}
}

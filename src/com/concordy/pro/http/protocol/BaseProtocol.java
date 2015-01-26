package com.concordy.pro.http.protocol;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import android.os.SystemClock;

import com.concordy.pro.bean.HttpError;
import com.concordy.pro.http.HttpHelper;
import com.concordy.pro.http.HttpHelper.HttpResult;
import com.concordy.pro.manager.AppException;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.ContentValue;
import com.concordy.pro.utils.FileUtils;
import com.concordy.pro.utils.IOUtils;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.StringUtils;
/**
 * 协议基类
 * @author Scleo
 * @param <Data>
 */
public abstract class BaseProtocol<Data> {
	public static final String cachePath = "";
	public static final int GET_LASTESTDATA = 0;
	public static final int GET_DATA = 1;
	public static final int GET_MOREDATA = 2;
	private int code;
	private String json;
	/** 加载协议 
	 * @throws AppException */
	public Data load(int flag,String url,String type) throws AppException {
		//SystemClock.sleep(1000);// 休息1秒，防止加载过快，看不到界面变化效果
		json = null;
		if(flag==GET_DATA){
			// 1.从本地缓存读取数据，查看缓存时间
			json = loadFromLocal(type);
		}
		// 2.如果是获取最新数据，从网络加载
		if (StringUtils.isEmpty(json)) {
			json = loadFromNet(url,type);
			if (json == null) {
				// 网络出错
				LogUtils.d("访问网络数据出错了....");
				return null;
			} else {
				// 3.把数据保存到本地保存到本地
				saveToLocal(json, type);
			}
		}
		return parseFromJson(flag,json);
	}

	/** 从本地加载协议 */
	protected String loadFromLocal(String type) {
		String path = FileUtils.getCacheDir();
		path = path + getKey() +".json";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();// 第一行是时间
			Long time = Long.valueOf(line);
			if (time > System.currentTimeMillis()) {//如果时间未过期
				StringBuilder sb = new StringBuilder();
				String result;
				while ((result = reader.readLine()) != null) {
					sb.append(result);
				}
				return sb.toString();
			}
		} catch (Exception e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(reader);
		}
		return null;
	}
	/** 从网络加载协议 
	 * @throws AppException */
	protected String loadFromNet(String url,String type) throws AppException {
		String result = null;
		//String url = ContentValue.SERVER_URL+"/" + getKey() + getParames();
		LogUtils.d("服务器请求地址:"+url);
		HttpResult httpResult = HttpHelper.get(url,type);
		if (httpResult != null) {
			result = httpResult.getString();
			code = httpResult.getCode();
			LogUtils.d("code:"+code);
			httpResult.close();
		}
		LogUtils.d("服务器返回的json:"+result);
		return result;
	}
	/** 保存到本地 */
	protected void saveToLocal(String str, String type) {
		String path = FileUtils.getCacheDir();
		path = path + getKey() +".json";
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path));
			long time = System.currentTimeMillis() + 1000 * 60;//先计算出过期时间，写入第一行
			writer.write(time + "\r\n");
			writer.write(str.toCharArray());
			writer.flush();
		} catch (Exception e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(writer);
		}
	}
	/** 需要增加的额外参数 */
	protected String getParames() {
		return "";
	}
	/** 该协议的访问地址 */
	protected abstract String getKey();
	/** 从json中解析正常数据 */
	protected abstract Data parseFromJson(int flag,String json);
	/** 从json中解析非正常数据 */
	public abstract HttpError getHttpError();
	/** 得到服务器返回的响应码 ***/
	public int getCode() {
		return code;
	}
	protected String getJson() {
		return json;
	}
}

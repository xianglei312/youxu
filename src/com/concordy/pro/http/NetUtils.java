package com.concordy.pro.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.concordy.pro.utils.ContentValue;
import com.lidroid.xutils.util.LogUtils;

/**
 * 
 * @author Scleo
 * 
 */
public class NetUtils {
	private static HttpGet get;
	private static HttpPost post;
	private static HttpClient client;

	public NetUtils() {
		client = new DefaultHttpClient();
	}
	/**
	 * 执行post请求,form内容格式
	 * @param url
	 * @param json
	 * @return
	 */
	public String doPostOfHttpClient(String url, String json) {
		post = new HttpPost(url);
		// 设置请求的参数
		HttpParams params = post.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 10000);
		// 设置请求头消息
		// post.setHeader(name, value)
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json");
		try {
			StringEntity entity = new StringEntity(json, ContentValue.ENCODING); // 创建一个UrlEncodedFormEntity对象
			post.setEntity(entity); // 设置请求的实体参数
			// 执行请求方法
			HttpResponse response = client.execute(post);
			// 获取服务器返回的状态码
			StatusLine statusLine = response.getStatusLine(); // 获取状态行对象,
			int statusCode = statusLine.getStatusCode(); // 从状态行中获取状态码
			HttpEntity resultServer = response.getEntity();
			// 访问成功, 获取服务器返回的数据
			String result = EntityUtils.toString(resultServer, ContentValue.ENCODING);
			if (statusCode == 200) {
				return result;
			}else{
				return ContentValue.ERROR_MSG+":"+result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConn();
		}
		return null;
	}
	/*** 执行post请求,form内容格式 **/
	public String doPostOfHttpClientFor(String url, String json) {
		post = new HttpPost(url);
		// 设置请求的参数
		HttpParams params = post.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 10000);
		// 设置请求头消息
		// post.setHeader(name, value)
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setHeader("Accept", "application/json");
		try {
			StringEntity entity = new StringEntity(json, ContentValue.ENCODING); // 创建一个UrlEncodedFormEntity对象
			post.setEntity(entity); // 设置请求的实体参数
			// 执行请求方法
			HttpResponse response = client.execute(post);
			// 获取服务器返回的状态码
			StatusLine statusLine = response.getStatusLine(); // 获取状态行对象,
			int statusCode = statusLine.getStatusCode(); // 从状态行中获取状态码
			HttpEntity resultServer = response.getEntity();
			// 访问成功, 获取服务器返回的数据
			String result = EntityUtils.toString(resultServer, ContentValue.ENCODING);
			if (statusCode == 200) {
				return result;
			}else{
				return ContentValue.ERROR_MSG+":"+result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConn();
		}
		return null;
	}

	/** * 关闭http连接** */
	private void closeConn() {
		if (client != null) {
			// 获得连接管理器, 关闭连接
			client.getConnectionManager().shutdown();
		}
	}
	/**
	 * 
	 * @param url  服务器地址
	 * @return
	 */
	public String doGetOfHttpClient(String url) {
		get = new HttpGet(url);
		// 设置一些请求参数
		HttpParams params = get.getParams();
		// 设置请求超时时间
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		// 设置读取超时时间
		HttpConnectionParams.setSoTimeout(params, 10000);
		// 有时候需要设置一些请求头消息, 可选
		get.setHeader("Content-Type", "application/json");
		get.setHeader("Accept", "application/json");
		try {
			// 开始执行get请求方法, 并且返回给我们一个HttpResponse的响应对象
			HttpResponse response = client.execute(get);
			// 获取服务器返回的状态码
			StatusLine statusLine = response.getStatusLine(); // 获取状态行对象,
																// 其中状态码就在里边
			int statusCode = statusLine.getStatusCode(); // 从状态行中获取状态码
			LogUtils.d("访问地址：" + url);
			// LogUtils.d("参数："+json.toString());
			LogUtils.d("响应码：" + statusCode);
			// 访问成功, 获取服务器返回的数据
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity,
					ContentValue.ENCODING);
			if (statusCode == 200) {
				LogUtils.d("GET_Entity:" + result);
				return result;
			}else{
				return ContentValue.ERROR_MSG+":"+result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeConn();
		}
		return null;
	}
}

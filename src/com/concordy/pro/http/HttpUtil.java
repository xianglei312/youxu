package com.concordy.pro.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.util.JsonWriter;

import com.concordy.pro.utils.ContentValue;
import com.lidroid.xutils.util.LogUtils;

public class HttpUtil {
	private HttpClient httpConn;
	
	private HttpGet get;
	private HttpPost post;
	public HttpUtil() {
		httpConn  = new DefaultHttpClient();
	}
	/**
	 * 发送post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public  String doPost(String url,String param)
	{
		post = new HttpPost(url);
		HttpParams params = post.getParams();
		JSONObject json = new JSONObject();//创建json对象封装数据
		try {
			json.put("Phone", param);
			//params.setParameter("Phone",json.toString());
			//设置与服务器连接超时时间
			HttpConnectionParams.setConnectionTimeout(params, ContentValue.SOCKET_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params,  ContentValue.SOCKET_TIMEOUT);
			post.addHeader("Content-Type","application/json");
			HttpResponse response = httpConn.execute(post);
			//创建Entity实体对象，并绑定到post请求中
			StringEntity se = new StringEntity(json.toString(),"utf-8");
			post.setEntity(se);
			//获取响应参数状态行，并获取响应码
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			LogUtils.d("访问地址："+url);
			LogUtils.d("参数："+json.toString());
			LogUtils.d("响应码："+statusCode);
			if(statusCode==200)
				LogUtils.d("VerifyResult："+EntityUtils.toString(response.getEntity()));
				return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			if(httpConn!=null)
				httpConn.getConnectionManager().shutdown();//关闭连接
		}
		return null;
	}
	public String doGet(String url,String param){
		get = new HttpGet(url+"");
		try {
			httpConn.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

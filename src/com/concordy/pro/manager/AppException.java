package com.concordy.pro.manager;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.HttpException;

import com.concordy.pro.R;
import com.concordy.pro.R.string;
import com.concordy.pro.utils.PromptManager;

import android.content.Context;

public class AppException extends Exception implements UncaughtExceptionHandler{

	/** 定义异常类型 */
	public final static byte FLAG_NETWORK 		= 0x01;
	public final static byte FLAG_SOCKET		= 0x02;
	public final static byte FLAG_HTTP_CODE		= 0x03;
	public final static byte FLAG_HTTP_ERROR	= 0x04;
	public final static byte FLAG_IO	 		= 0x06;
	public final static byte FLAG_JSON	 		= 0x07;
	public final static byte FLAG_UNKNOW_HOST	= 0x08;
	public final static byte FLAG_EXCUTE		= 0x09;
	public final static byte FLAG_NO_NETWORK	= 0x10;
	
	
	private Thread.UncaughtExceptionHandler exceptionHandler;
	private byte flag;
	private int code;
	
	protected byte getFlag() {
		return this.flag;
	}

	protected int getCode() {
		return this.code;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if(exceptionHandler!=null)
			exceptionHandler.uncaughtException(thread, ex);
	}
	
	private AppException(){
		this.exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	}
	
	public AppException(byte flag, int code, Exception e) {
		this.flag = flag;
		this.code = code;
	}
	/** http异常*****/
	public static AppException http(Exception e){
		return new AppException(FLAG_HTTP_ERROR,0,e);
	}
	/** http异常号**/
	public static AppException http(int errCode){
		return new AppException(FLAG_HTTP_CODE,0,null);
	}
	/** json解析错误**/
	public static AppException json(Exception e){
		return new AppException(FLAG_JSON,0,e);
	}
	/** 链接超时**/
	public static AppException socket(Exception e){
		return new AppException(FLAG_SOCKET,0,e);
	}
	/** 运行错误**/
	public static AppException excute(Exception e){
		return new AppException(FLAG_EXCUTE,0,e);
	}
	/** io异常**/
	public static AppException io(Exception e){
		if(e instanceof UnknownHostException){
			return new AppException(FLAG_UNKNOW_HOST,0,e);
		}else if(e instanceof ConnectException){
			return new AppException(FLAG_NETWORK, 0, e);
		}else if(e instanceof IOException){
			return new AppException(FLAG_IO, 0, e);
		}
		return excute(e);
	}
	/** 网络异常**/
	public static AppException network(Exception e){
		if(e instanceof UnknownHostException){
			return new AppException(FLAG_UNKNOW_HOST,0,e);
		}else if(e instanceof ConnectException){
			return new AppException(FLAG_NETWORK, 0, e);
		}else if(e instanceof SocketException){
			return socket(e);
		}else if(e instanceof HttpException){
			return http(e);
		}
		return new AppException(FLAG_NETWORK, 0, e);
	}
	/** 获取异常对象**/
	public AppException getAppException(){
		return new AppException();
	}
	
	
	public void errorTosat(Context ct){
		switch (this.getCode()) {
		case FLAG_HTTP_CODE:
			String errMsg = ct.getString(R.string.http_error_code, this.getCode());
			PromptManager.showToast(ct, errMsg);
			break;
		case FLAG_HTTP_ERROR:
			PromptManager.showToast(ct, R.string.http_connect_error);
			break;
		case FLAG_IO:
			break;
		case FLAG_JSON:
			PromptManager.showToast(ct, R.string.json_parser_failed);
			break;
		case FLAG_SOCKET:
			PromptManager.showToast(ct, R.string.http_socket_error);
			break;
		case FLAG_UNKNOW_HOST:
			PromptManager.showToast(ct, R.string.http_connect_error);
			break;
		case FLAG_EXCUTE:
			PromptManager.showToast(ct, R.string.app_run_code_error);
			break;
		case FLAG_NO_NETWORK:
			PromptManager.showToast(ct, R.string.http_no_network);
			break;
		}
	}
}

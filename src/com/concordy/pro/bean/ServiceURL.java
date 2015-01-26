package com.concordy.pro.bean;

public class ServiceURL {
	/** 服务器地址**/
	public static String HTTP = "http://";
	public static String HTTPS = "https://";
	public static String HOST = "www.concordyaapi.chinacloudsites.cn";
	public static String SERVER_URL = "concordyaapi.chinacloudsites.cn";
	public static String SERVER_URL_API = HTTP+SERVER_URL;
	/******API地址 **********/
	public static String BILL =SERVER_URL_API+ "/api/bill";
	public static String CATEGORY =SERVER_URL_API+  "/api/category";
	public static String VENDOR =SERVER_URL_API+  "/api/vendor";
	public static String VERICAL_REQUEST = SERVER_URL_API+ "/api/verifycode";
	public static String VERICAL_PHONE =SERVER_URL_API+  "/api/phone";
	public static String TOKEN = SERVER_URL_API+ "/api/token";
	public static String REGIST = SERVER_URL_API+ "/api/account/register";
	public static String LOGIN = SERVER_URL_API+ "/api/account/login";
}

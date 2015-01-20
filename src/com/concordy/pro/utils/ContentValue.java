package com.concordy.pro.utils;
/**
 * @author Scleo
 *
 */
public abstract class ContentValue {
	public static final int SOCKET_TIMEOUT = 10000;
	public static final int ERROR_SOCKET_TIMEOUT = 10000;
	//Sp文件中记录是否已登录
	public static final String SPFILE_TOKEN = "auth_token";
	public static final String SPFILE_PIN = "pincode";
	public static final String SPFILE_PIN_ERRCOUNT = "pinErrCount";
	public static final String SPFILE_PIN_TOGGLE = "pinToggle";
	public static final String SPFILE_USERNAME = "username";
	public static final String SPFILE_PIN_SERVICE_START = "pinServiceStart";
	/******全局UTF-8编码********/
	public static String ENCODING = "utf-8";
	/****** 服务器地址 **********/
	public static String NEWSERVER_URL = "http://concordyaapi.chinacloudsites.cn/api";
	/****** BillAPI地址 **********/
	public static String BILL_URL = "bill";
	public static String URI_CATEGORY = "category";
	public static String URI_VENDOR = "vendor";
	public static String SERVER_URL = "http://concordyaapi.chinacloudsites.cn/api";
	public static String SERVER_URI = "http://concordyaapi.chinacloudsites.cn/api";
	public static String REGIST_URI = "account/register";
	public static String VERICAL_REQUEST = "verifycode";
	public static String VERICAL_PHONE = "phone";
	public static String BILL_GETALL = "bill";
	public static String TOKEN_URI = "token";
	public static String URI_LOGIN = "account/login";
	/*****************http参数***********************/
	public static final String AUTHORIZATION = "authorization";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String ACCEPT_TYPE = "Accept";
	
	/***********http头参数的值******************/
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_FORM = "application/x-www-form-urlencoded";
	public static final String APPLICATION_XML = "application/xml";
	/***********服务器返回状态     *****************/
	public static final String ERROR_MSG = "error";
	/***************临时测试的部分***************/
	public  static String VERICAL_CODE = "55554";
	/***************网络状态***********/
	public static int NO_NETWORK = 0;
	public static int WIFI_STATE = 1;
	public static int CNWAP_STATE = 2;
	public static int CMNET_STATE = 0;
	/***************账单状态***********/
	/************测试数据**************/
	/***短信运营商号码中心***/
	public static final String SERVER_PHONE = "106550771016990848";//联通
	public static final String SERVER_PHONE_DX = "10690660990849";//电信
	public static final String SERVER_PHONE_YD = "10690262999";//移动
	
}

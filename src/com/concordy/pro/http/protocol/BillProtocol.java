package com.concordy.pro.http.protocol;

import com.concordy.pro.bean.Bills;
import com.concordy.pro.bean.HttpError;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.LogUtils;

public class BillProtocol extends BaseProtocol<Bills> {
	@Override
	protected String getKey() {
		return "bill";
	}

	@Override
	protected Bills parseFromJson(int flag,String json) {
		if (getCode() == 200||flag==BaseProtocol.GET_DATA){
			try {
				return CommonUtil.json2Bean(json, Bills.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	@Override
	protected String getParames() {
		return "?page=0";
	}

	@Override
	public HttpError getHttpError() {
		LogUtils.d("code"+getCode()+"服务器出错了："+getJson());
		try{
			return CommonUtil.json2Bean(getJson(), HttpError.class);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}

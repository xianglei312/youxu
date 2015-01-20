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
		LogUtils.d("code"+getCode()+",解析bill数据："+getJson());
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
		try{
			return CommonUtil.json2Bean(getJson(), HttpError.class);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}

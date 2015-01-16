package com.concordy.pro.http.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.concordy.pro.bean.HttpError;
import com.concordy.pro.bean.Vendor;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.LogUtils;

public class VendorProtocol extends BaseProtocol<List<Vendor>> {
	@Override
	protected String getKey() {
		return "vendor";
	}

	@Override
	protected List<Vendor> parseFromJson(int flag,String json) {
		if (getCode() == 200||flag==BaseProtocol.GET_DATA){
			List<Vendor> list = new ArrayList<Vendor>();
			JSONArray array;
			try {
				array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Vendor vendor = new Vendor();
					vendor.setId(obj.getString("id"));
					vendor.setName(obj.getString("name"));
					list.add(vendor);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		}
		return null;
	}

	@Override
	protected String getParames() {
		return "";
	}
	
	@Override
	public HttpError getHttpError() {
		LogUtils.d("code:"+getCode()+"服务器出错了："+getJson());
		try{
			return CommonUtil.json2Bean(getJson(), HttpError.class);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}

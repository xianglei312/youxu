package com.concordy.pro.http.protocol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.concordy.pro.bean.Category;
import com.concordy.pro.bean.HttpError;
import com.concordy.pro.utils.CommonUtil;
import com.concordy.pro.utils.LogUtils;

public class CategoryProtocol extends BaseProtocol<List<Category>> {
	@Override
	protected String getKey() {
		return "category";
	}
	@Override
	protected List<Category> parseFromJson(int flag,String json) {
		if (getCode() == 200||flag==BaseProtocol.GET_DATA){
			List<Category> list = new ArrayList<Category>();
			JSONArray array;
			try {
				array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					Category cate = new Category();
					cate.setId(obj.getString("id"));
					cate.setName(obj.getString("name"));
					list.add(cate);
				}
			} catch (JSONException e) {
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

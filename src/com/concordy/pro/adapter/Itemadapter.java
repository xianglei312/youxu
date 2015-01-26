package com.concordy.pro.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.concordy.pro.R;
import com.concordy.pro.bean.Bill.Item;
import com.concordy.pro.utils.LogUtils;
import com.concordy.pro.utils.StringUtils;
public class Itemadapter extends BaseAdapter {
	private final int NAME =0 ;
	private final int NUM =1 ;
	private final int PRICE =2;
	private ViewHolder holder;
	private LayoutInflater mInflater;
	public List<Item> items;
	private DeleteListener deleteListener;
	private Item item;
	
	public Itemadapter(Context context,List<Item> arr,com.concordy.pro.adapter.Itemadapter.DeleteListener delete) {  
		super(); 
		mInflater = LayoutInflater.from(context);
		this.items = arr;
		this.deleteListener = delete;
	}  
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position) ;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		item = items.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			//把vlist layout转换成View【LayoutInflater的作用】
			convertView = mInflater.inflate(R.layout.item, null);
			//通过上面layout得到的view来获取里面的具体控件
			//holder.sideslipHorScrView = (SideslipHorScrView) convertView.findViewById(R.id.sideslipHorScrView);
			holder.itemname =  (EditText) convertView.findViewById(R.id.et_name_bill_item);
			holder.itemnumber =  (EditText) convertView.findViewById(R.id.et_num_bill_item);
			holder.itemprice =  (EditText) convertView.findViewById(R.id.et_price_bill_item);
			holder.delete = (ImageView)convertView.findViewById(R.id.iv_del_bill_item);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(item!=null){
			holder.itemname.setText(item.getName());
			holder.itemnumber.setText(item.getQuantity()+"");
			holder.itemprice.setText(item.getPricePerUnit());
		}
		holder.itemname.addTextChangedListener(new MyEditViewChanged(NAME));
		holder.itemnumber.addTextChangedListener(new MyEditViewChanged(NUM));
		holder.itemprice.addTextChangedListener(new MyEditViewChanged(PRICE));
		holder.delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*items.remove(position);
				notifyDataSetChanged();*/
				//UIUtils.setListViewHeightBasedOnChildren(get);
				if(deleteListener!=null)
					deleteListener.delete(items, position);
			}
		});


		return convertView;
	}

	class ViewHolder{
		private EditText itemname,itemnumber,itemprice;
		private ImageView delete;
	}
	
	public interface DeleteListener{
		void delete(List<Item> arr,int position);
	}
	class MyEditViewChanged implements TextWatcher{
		private String result = "";
		private int flag;
		public MyEditViewChanged(int flag) {
			this.flag = flag;
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			LogUtils.d("s:"+s);
			if(s!=null&&!"".equals(s)){
				result = s.toString();
				//LogUtils.d("s:"+s);
			}
		}
		@Override
		public void afterTextChanged(Editable s) {
			LogUtils.d("item输入结束："+result);
			if(item!=null){
				switch (flag) {
					case NAME:
						item.setName(result);
						break;
					case NUM:
						if(StringUtils.isEmpty(result))
							item.setQuantity(0);
						else
							item.setQuantity(Integer.parseInt(result.trim()));
						break;
					case PRICE:
						item.setPricePerUnit(result);
						break;
				}
			}
		}
	}
}

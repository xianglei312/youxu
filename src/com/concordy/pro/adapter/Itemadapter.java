package com.concordy.pro.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.concordy.pro.R;
import com.concordy.pro.bean.Bill.Item;
import com.concordy.pro.utils.SideslipHorScrView;
public class Itemadapter extends BaseAdapter{

	private LayoutInflater mInflater;
	public List<Item> items;
	private Context mContext;
	private Delete delete;
	
	public Itemadapter(Context context,List<Item> arr,com.concordy.pro.adapter.Itemadapter.Delete delete2) {  
		super(); 
		this.delete = delete2;
		this.mContext = context;  
		mInflater = LayoutInflater.from(context);
		this.items = arr;
		//arr = new ArrayList<Item>();  
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
		// TODO Auto-generated method stub
		
		final ViewHolder holder;
		Item  item = items.get(position);
		if(convertView == null){
			holder = new ViewHolder();

			//把vlist layout转换成View【LayoutInflater的作用】
			convertView = mInflater.inflate(R.layout.item, null);
			//通过上面layout得到的view来获取里面的具体控件
			//holder.sideslipHorScrView = (SideslipHorScrView) convertView.findViewById(R.id.sideslipHorScrView);
			holder.itemname =  (EditText) convertView.findViewById(R.id.itemname);
			holder.itemnumber =  (EditText) convertView.findViewById(R.id.et_bill_item);
			
			holder.itemprice =  (EditText) convertView.findViewById(R.id.itemprice);
			//holder.delete = (Button)convertView.findViewById(R.id.deleteitem);

			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(item!=null){
			holder.itemname.setText(item.getName());
			holder.itemnumber.setText(item.getQuantity());
			holder.itemprice.setText(item.getPricePerUnit());
		}
		holder.itemname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.itemname.getText().toString().trim();
				
			}
		});
		holder.itemnumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.itemnumber.getText().toString().trim();
			}
		});
		holder.itemprice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.itemprice.getText().toString().trim();
				
			}
		});


		/*holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Log.i("xfx", "button监听" + "  arg0=" + position );

				arr.remove(position);
				notifyDataSetChanged();
				holder.sideslipHorScrView.scrollTo(0, 0);
				delete.delete(arr, position);

			}
		});*/


		return convertView;
	}

	class ViewHolder{
		private EditText itemname,itemnumber,itemprice;
		private Button delete;
		private SideslipHorScrView sideslipHorScrView;
		private LinearLayout linearlayout;

	}
	
	public interface Delete{
		void delete(ArrayList<String> arr,int position);
	}

}

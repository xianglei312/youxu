package com.concordy.pro.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
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
import com.concordy.pro.utils.SideslipHorScrView;
public class Itemadapter extends BaseAdapter{

	private LayoutInflater mInflater;
	public ArrayList<String> arr;
	private Context mContext;
	private Delete delete;
	
	public Itemadapter(Context context,ArrayList<String> arr,com.concordy.pro.adapter.Itemadapter.Delete delete2) {  
		super(); 
		this.delete = delete2;
		this.mContext = context;  
		mInflater = LayoutInflater.from(context);
		this.arr = arr;
		arr = new ArrayList<String>();  
		for(int i=0;i<arr.size();i++){    //listview初始化
			arr.add("");  
		}  
	}  


	@Override
	public int getCount() {
		return arr.size();
	}

	@Override
	public Object getItem(int position) {
		return arr.get(position) ;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		final ViewHolder holder;
	
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
		
		holder.itemname.setHint(arr.get(position));
		
		holder.itemname.setOnFocusChangeListener(new OnFocusChangeListener() {  
			@Override  
			public void onFocusChange(View v, boolean hasFocus) {  
				// TODO Auto-generated method stub  
				if(arr.size()>0){  
					arr.set(position, holder.itemname.getText().toString());  
				}  
			}  
		});  
		
		//这里testData.get(position).get("title1"))，其实就是从list集合(testData)中取出对应索引的map，然后再根据键值对取值
//		edittext.setHint("项目：");
		
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

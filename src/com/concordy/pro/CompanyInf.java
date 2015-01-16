package com.concordy.pro;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.concordy.pro.R;
import com.concordy.pro.adapter.MyAdapter;
import com.concordy.pro.bean.MyListItem;
import com.concordy.pro.db.DBManager;
import com.concordy.pro.ui.base.BaseTitleActivity;

public class CompanyInf extends BaseTitleActivity {

	private DBManager dbm;
	private SQLiteDatabase db;
	private Spinner spinner1 = null;
	private Spinner spinner2=null;
	private Spinner spinner3=null;
	private String province=null;
	private String city=null;
	private String district=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_company_inf);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				layout);// 设置titleBar 布局文件
		spinner1=(Spinner)findViewById(R.id.spinner1);
		spinner2=(Spinner)findViewById(R.id.spinner2);
		spinner3=(Spinner)findViewById(R.id.spinner3);
		spinner1.setPrompt("省");
		spinner2.setPrompt("城市");		
		spinner3.setPrompt("地区");

		initSpinner1();
	}
	public void initSpinner1(){
		dbm = new DBManager(this);
		dbm.openDatabase();
		db = dbm.getDatabase();
		List<MyListItem> list = new ArrayList<MyListItem>();

		try {    
			String sql = "select * from province";  
			Cursor cursor = db.rawQuery(sql,null);  
			cursor.moveToFirst();
			while (!cursor.isLast()){ 
				String code=cursor.getString(cursor.getColumnIndex("code")); 
				byte bytes[]=cursor.getBlob(2); 
				String name=new String(bytes,"gbk");
				MyListItem myListItem=new MyListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code=cursor.getString(cursor.getColumnIndex("code")); 
			byte bytes[]=cursor.getBlob(2); 
			String name=new String(bytes,"gbk");
			MyListItem myListItem=new MyListItem();
			myListItem.setName(name);
			myListItem.setPcode(code);
			list.add(myListItem);

		} catch (Exception e) {  
		} 
		dbm.closeDatabase();
		db.close();	

		MyAdapter myAdapter = new MyAdapter(this,list);
		spinner1.setAdapter(myAdapter);
		spinner1.setOnItemSelectedListener(new SpinnerOnSelectedListener1());
	}
	public void initSpinner2(String pcode){
		dbm = new DBManager(this);
		dbm.openDatabase();
		db = dbm.getDatabase();
		List<MyListItem> list = new ArrayList<MyListItem>();

		try {    
			String sql = "select * from city where pcode='"+pcode+"'";  
			Cursor cursor = db.rawQuery(sql,null);  
			cursor.moveToFirst();
			while (!cursor.isLast()){ 
				String code=cursor.getString(cursor.getColumnIndex("code")); 
				byte bytes[]=cursor.getBlob(2); 
				String name=new String(bytes,"gbk");
				MyListItem myListItem=new MyListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code=cursor.getString(cursor.getColumnIndex("code")); 
			byte bytes[]=cursor.getBlob(2); 
			String name=new String(bytes,"gbk");
			MyListItem myListItem=new MyListItem();
			myListItem.setName(name);
			myListItem.setPcode(code);
			list.add(myListItem);

		} catch (Exception e) {  
		} 
		dbm.closeDatabase();
		db.close();	

		MyAdapter myAdapter = new MyAdapter(this,list);
		spinner2.setAdapter(myAdapter);
		spinner2.setOnItemSelectedListener(new SpinnerOnSelectedListener2());
	}
	public void initSpinner3(String pcode){
		dbm = new DBManager(this);
		dbm.openDatabase();
		db = dbm.getDatabase();
		List<MyListItem> list = new ArrayList<MyListItem>();

		try {    
			String sql = "select * from district where pcode='"+pcode+"'";  
			Cursor cursor = db.rawQuery(sql,null);  
			cursor.moveToFirst();
			while (!cursor.isLast()){ 
				String code=cursor.getString(cursor.getColumnIndex("code")); 
				byte bytes[]=cursor.getBlob(2); 
				String name=new String(bytes,"gbk");
				MyListItem myListItem=new MyListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code=cursor.getString(cursor.getColumnIndex("code")); 
			byte bytes[]=cursor.getBlob(2); 
			String name=new String(bytes,"gbk");
			MyListItem myListItem=new MyListItem();
			myListItem.setName(name);
			myListItem.setPcode(code);
			list.add(myListItem);

		} catch (Exception e) {  
		} 
		dbm.closeDatabase();
		db.close();	

		MyAdapter myAdapter = new MyAdapter(this,list);
		spinner3.setAdapter(myAdapter);
		spinner3.setOnItemSelectedListener(new SpinnerOnSelectedListener3());
	}

	class SpinnerOnSelectedListener1 implements OnItemSelectedListener{

		public void onItemSelected(AdapterView<?> adapterView, View view, int position,
				long id) {
			province=((MyListItem) adapterView.getItemAtPosition(position)).getName();
			String pcode =((MyListItem) adapterView.getItemAtPosition(position)).getPcode();

			initSpinner2(pcode);
			initSpinner3(pcode);
		}

		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
		}		
	}
	class SpinnerOnSelectedListener2 implements OnItemSelectedListener{

		public void onItemSelected(AdapterView<?> adapterView, View view, int position,
				long id) {
			city=((MyListItem) adapterView.getItemAtPosition(position)).getName();
			String pcode =((MyListItem) adapterView.getItemAtPosition(position)).getPcode();

			initSpinner3(pcode);
		}

		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
		}		
	}

	class SpinnerOnSelectedListener3 implements OnItemSelectedListener{

		public void onItemSelected(AdapterView<?> adapterView, View view, int position,
				long id) {
			district=((MyListItem) adapterView.getItemAtPosition(position)).getName();
			Toast.makeText(CompanyInf.this, province+" "+city+" "+district, Toast.LENGTH_LONG).show();
		}

		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
		}		
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}

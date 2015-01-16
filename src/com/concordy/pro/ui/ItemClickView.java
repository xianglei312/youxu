package com.concordy.pro.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.concordy.pro.R;

public class ItemClickView extends RelativeLayout implements OnClickListener{
	private TextView tv;
	private ImageView iv;
	private String txt;
	private Drawable drawable;
	private TextView line;
	private boolean flag;
	private View view;
	public ItemClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public ItemClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemClickView);
		txt = ta.getString(R.styleable.ItemClickView_txt);
		drawable = ta.getDrawable(R.styleable.ItemClickView_leftImg);
		flag = ta.getBoolean(R.styleable.ItemClickView_bottomLine, false);
		setImage(drawable);
		setText(txt);
		setLineVisiable(flag);
	}
	public ItemClickView(Context context) {
		super(context);
		init();
	}
	private void init() {
		view = View.inflate(getContext(), R.layout.ui_stv_click, this);
		tv = (TextView) view.findViewById(R.id.tv_stv_txt);
		iv = (ImageView) view.findViewById(R.id.iv_stv_left);
		line = (TextView) view.findViewById(R.id.tv_stv_bottom_line);
		view.setOnClickListener(this);
	}
	/****是否显示底部线条****/
	public void setLineVisiable(boolean flag){
		if(flag){
			line.setVisibility(View.VISIBLE);
		}else{
			line.setVisibility(View.INVISIBLE);
		}
	}
	public void setText(String txt){
		tv.setText(txt);
	}
	public void setImage(int resId){
		iv.setImageResource(resId);
	}
	public void setImage(Drawable drawable){
		iv.setImageDrawable(drawable);
	}
	@Override
	public void onClick(View v) {
	}
}

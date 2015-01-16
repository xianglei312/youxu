package com.concordy.pro.ui.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.concordy.pro.R;

public class CustomerEditText extends RelativeLayout {
	private ImageButton btn;
	private AutoCompleteTextView actv;
	private AutoEditTextListener onViewClickListener;
	public CustomerEditText(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CustomerEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditWidget);
		Drawable drawable = typedArray.getDrawable(R.styleable.EditWidget_rightImg);
		init(context);
		setDrawable(drawable);
	}

	public CustomerEditText(Context context) {
		super(context);
		init(context);
	}
	private void init(final Context context) {
		LayoutInflater.from(context).inflate(R.layout.ui_widget_edittext, this,true);
		actv = (AutoCompleteTextView) findViewById(R.id.actv);
		btn = (ImageButton) findViewById(R.id.ibtn_right_et);
		//Log.d("SCLEO", "init(left["+btn.getLeft()+"]top["+btn.getTop()+"])");
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onViewClickListener!=null){
					onViewClickListener.onBtnClick();
				}
			}
		});
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
	public  void setOnViewClickListener(AutoEditTextListener onViewClickListener) {
		this.onViewClickListener = onViewClickListener;
	}
	public interface AutoEditTextListener{
		void onBtnClick();
		void onEditChanged();
	}
	/***
	 * �����ұ�ͼƬ�ɵ��
	 * @param drawable
	 */
	public void setDrawable(final Drawable drawable){
		if(btn!=null)
			btn.setBackgroundDrawable(drawable);
		//btn.setImageDrawable(drawable);
	}
	/***
	 * �����ı����Ƿ������
	 * @param enable
	 */
	public void setEditEnable(final boolean enable){
		if(actv!=null)
			actv.setEnabled(enable);
		if(enable){
			onViewClickListener.onEditChanged();
		}
	}
	/***
	 * 设置EditText
	 * @param text
	 */
	public void setText(String text){
		if(actv!=null)
			actv.setText(text);
	}
	/**
	 * 得到输入的文本
	 * @return
	 */
	public String getText(){
		return actv.getText().toString();
	}
}

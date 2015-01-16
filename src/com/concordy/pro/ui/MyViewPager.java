package com.concordy.pro.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends LazyViewPager {

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
    	// TODO Auto-generated method stub
    	return false;
    }
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		
		return false;
	}
	
}

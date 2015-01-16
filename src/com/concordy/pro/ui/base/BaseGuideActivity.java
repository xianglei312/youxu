package com.concordy.pro.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 模板设计
 * @author leo
 *
 */
public abstract class BaseGuideActivity extends Activity{
	protected SharedPreferences sp;
	GestureDetector detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		// 1 初始化手势识别
		detector=new GestureDetector(this, new MyGestureListener());
	}
	private class MyGestureListener extends SimpleOnGestureListener{
		// 2 处理手势事件
		// 猛动   (滑动)
		// e1  开始按下触摸事件
		// e2 结束 最后抬起的事件 
		// velocityX  x轴方向速率   
		// velocityY y轴方向速率 
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			float startX=e1.getRawX();
			float endX=e2.getRawX();
			float startY=e1.getRawY();
			float endY=e2.getRawY();
			if(Math.abs(startY-endY)>100){
				return true; // 返回false 代表事件没有处理  true 处理了事件
			}
			
			if((endX-startX)>100){
				pre_activity();
			}else if((startX-endX)>100){
				next_activity();
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}
	// activity 响应的触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//3 让手势识别器 注册到Activity触摸事件上 
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	
	/**
	 * 点击返回按钮 处理操作 
	 */
	@Override
	public void onBackPressed() {
		pre_activity();
	}
	
	public abstract void next_activity();
	public abstract void pre_activity();
	/**
	 * 下一步
	 * @param v
	 */
	public void next(View v){
		next_activity();
	}
	/**
	 * 上一步
	 * @param v
	 */
	public void pre(View v){
		pre_activity();
	}
}

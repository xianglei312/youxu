package com.concordy.pro.pager;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.concordy.pro.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CameraPager extends BasePager implements OnClickListener {
	
	private Camera camera;
	
	@ViewInject(R.id.bt_camera_take)
	private Button take_pic;
	@ViewInject(R.id.sv_camera)
	private SurfaceView sfv;

	private SurfaceHolder sfvHolder;
	public CameraPager(Context ct) {
		super(ct);
	}

	@Override
	public View initView() {
		View view = View.inflate(ct, R.layout.activity_camera, null);
		ViewUtils.inject(this, view);
		
		sfvHolder = sfv.getHolder();
		//设置回调接口
		sfvHolder.addCallback(new CameraBack());
		take_pic.setOnClickListener(this);
		return view;
	}
	class CameraBack implements Callback{
		 /**
		  * 开启摄像头并将捕捉画面与SurfaceView 绑定。
		  */
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			//开启摄像头
			camera =Camera.open();
			try {
				//设置预览画面
				camera.setPreviewDisplay(sfvHolder);
				camera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			
		}
		/**
		 * 释放Camera资源
		 */
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			camera.stopPreview();
			camera.release();
		}
	}
	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_camera_take:
			
			break;
		}
	}
}

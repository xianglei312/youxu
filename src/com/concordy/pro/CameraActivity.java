package com.concordy.pro;

import java.io.IOException;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.concordy.pro.utils.DrawableUtils;

public class CameraActivity extends BaseActivity implements OnClickListener {
	private Camera camera;
	private SurfaceView mSurfaceView;
	private Button mTakePic;
	private RelativeLayout rl_camera_bottom;
	private SurfaceHolder holder;
	private Intent intent;
	private String IMG_PATH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera);
		intent = getIntent();
		initView();

	}

	@SuppressLint("NewApi")
	@Override
	protected void initView() {
		// 设置背景图片
		GradientDrawable bg = DrawableUtils.createDrawable(Color.DKGRAY,
				Color.DKGRAY, 0);
		bg.setAlpha(155);
		rl_camera_bottom.setBackground(bg.getCurrent());

		holder = mSurfaceView.getHolder();
		mTakePic.setOnClickListener(this);
		holder.addCallback(new MyCallBack()); // 设置Surfaceview的回调接口
		mSurfaceView = (SurfaceView) findViewById(R.id.sv_camera);
		mTakePic = (Button) findViewById(R.id.bt_camera_take);
		rl_camera_bottom = (RelativeLayout) findViewById(R.id.rl_camera_bottom);
	};

	class MyCallBack implements Callback {
		/**
		 * 当SurfaceView 创建时, 回调此方法 使用摄像头开始捕获图像, 把捕获的图像设置给SurfaceView去展示
		 */
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			camera = Camera.open();

			try {
				camera.setDisplayOrientation(90);
				// 摄像头设置预览显示的位置为: SurfaceView
				camera.setPreviewDisplay(holder);
				// 开始预览
				camera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 当SurfaceView的尺寸改变时
		 */
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		/**
		 * 当SurfaceView销毁时回调此方法 停止摄像头捕获图像, 停止预览
		 */
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			camera.stopPreview();
			camera.release();
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_camera_cancel:

			break;
		case R.id.bt_camera_take:
			takePic();
			break;
		case R.id.bt_camera_save:

			break;
		}
	}

	/**
	 * 拍照
	 */
	private void takePic() {
		// 自动聚焦
		camera.autoFocus(new AutoFocusCallback() {
			/**
			 * 聚焦完成后调用此方法
			 */
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				// 拍照
				camera.takePicture(null, null, new PictureCallback() {
					/**
					 * 当jpg图片转换完成之后回调此方法
					 * 
					 * @param data
					 *            就是拍出来的图片的数据
					 * @param camera
					 */
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
					}
				});
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// camera.stopPreview();
		camera.release();
	}
}

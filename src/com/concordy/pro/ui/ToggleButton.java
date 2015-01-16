package com.concordy.pro.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.concordy.pro.R;
import com.concordy.pro.utils.LogUtils;
/****
 * 自定义开关控件
 * @author Scleo
 *
 */
public class ToggleButton extends View implements OnClickListener {
	private Bitmap backGround;
	private Bitmap slideButton;
	private OnToggleButtonClick onToggleBtnClick;
	/**
	 * 在代码中用关键字new 的时候调用
	 * @param context
	 */
	public ToggleButton(Context context) {
		super(context);
	}

	/**
	 * 从 xml布局文件中，转换的时候，由系统自动调用
	 * @param context
	 * @param attrs
	 */
	public ToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.ctx = context;
		/*TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToggleButton);
		boolean  flag = typedArray.getBoolean(R.styleable.ToggleButton_toggle, true);
		setCurState(flag);*/
		initView();
	}

	private Context ctx;
	/**
	 * 画笔
	 */
	private Paint paint;
	/**
	 * 滑动按钮的左边距
	 */
	private float slideBtnLeft;
	
	/**
	 * 判断当前开关的状态，
	 * 默认 false 为关
	 */
	private boolean curState = false;
	
	public boolean isCurState() {
		return curState;
	}
	public void setCurState(boolean curState) {
		this.curState = curState;
	}
	/**
	 * 滑动按钮左边距的最大值
	 */
	private float maxLeft;
	
	private void initView() {
		backGround = BitmapFactory.decodeResource(getResources(), R.drawable.toggle_bt_bg);
		slideButton = BitmapFactory.decodeResource(getResources(), R.drawable.toggle_bt);
		maxLeft = backGround.getWidth()-slideButton.getWidth();
		//LogUtils.d("控件初始化：curstate="+curState);
		//初始化画笔
		paint = new Paint();
		//设置为抗矩齿
		paint.setAntiAlias(true);
		//给当前view添加点击事件
		setOnClickListener(this);
	}

	/*
	 * View 从创建到 在屏幕上显示出来，中间的几个重要步骤
	 * 1、构造函数
	 * 2、测量view的大小，子view只有建议权，父view才有决定权
	 * 		相关方法 ：onMeasure(int,int);
	 * 			
	 * 3、确定的位置，子view一般不关系 这个方法 
	 * 		当父view确定位置后，会调用view的
	 * 		onLayout(boolean,int,int,int,int)
	 * 		告诉子view
	 * 4、绘制view的内容
	 * 
	 * 	onDraw(Canvas)
	 * 
	 */
	@Override
	/**
	 * 测量子view的大小
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//指定当前view的大小
//		setMeasuredDimension(100,50);
		//将当前子view设置 为和背景图大小一样
		setMeasuredDimension(backGround.getWidth(),backGround.getHeight());
		LogUtils.d("开始测量控件了：curstate="+curState);
	}
	@Override
	/**
	 * 父view确定子view的位置后，回调此方法
	 * 自定义view中此方法，意义不大，
	 * 在自定义viewGroup当中，此方法有重要作用
	 */
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
//		super.onLayout(changed, left, top, right, bottom);
		System.out.println("left::"+left+"  top:"+top+" right:"+right+" bottom:"+bottom);
	}
	/**
	 * 绘制view的内容
	 */
	@Override
	protected void onDraw(Canvas canvas) {
//		canvas.drawColor(Color.GREEN);
		//LogUtils.d("开始绘画控件了：curstate="+curState);
		if(curState)
			slideBtnLeft = maxLeft;
		canvas.drawBitmap(backGround, 0, 0, paint);
		canvas.drawBitmap(slideButton, slideBtnLeft, 0, paint);
	}
	/**
	 * 响应view的点击事件
	 */
	@Override
	public void onClick(View v) {
		LogUtils.d("被点击了");
		//如果发生托动，就返回，不再执行
		if(isDrop){
			LogUtils.d("发生拖动了");
			return ;
		}
		curState = ! curState;
		flushState();
	}
	/**
	 * 根据当前状态，确定slideBtnLeft的值
	 */
	private void flushState() {
		LogUtils.d("当前状态：curState="+curState);
		if(curState){
			slideBtnLeft = maxLeft;
		}else{
			slideBtnLeft = 0;
		}
		onToggleBtnClick.onChangeState();
		flushView();
		
	}
	/**
	 * down 事件发生时的 X坐标
	 */
	private int firstX;
	/**
	 * 上一个事件(down , move )发生时的x坐标
	 */
	private int lastX;
	
	/**
	 * 判断是否发生了托动，如果移动超过10像素，就认为发生了托动，此时，onclick就不再执行
	 */
	private boolean isDrop = false;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//点击效果，也是从onTouchEvent方法 中解析出来的，然后才会去执行，onclickListener.onclick(v);
		//如果下面这句注掉，onclick事件会失效。
		super.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			firstX = lastX = (int) event.getX();
			isDrop = false;
			break;
		case MotionEvent.ACTION_MOVE:
			//计算二个event之间发生的位移
			int distance = (int) (event.getX() - lastX);
			//让slideBtnLeft发生同样的位移
			slideBtnLeft+=distance;
			//更新lastX的值
			lastX = (int) event.getX();
			//判断是否发生托动
			if(Math.abs(event.getX()-firstX)>10){
				isDrop = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			//只有发生托动时，以下代码才执行
			if(isDrop){
				//当手指抬起时，如果slideButtonLeft > maxLeft/2  按钮应该是开的状态
				if(slideBtnLeft > maxLeft/2){
					curState = true;
				}else{//否则，就是关的状态
					curState = false;
				}
				flushState();
			}
			break;
		}
		flushView();
		return true; 
	}
	private void flushView() {
		//确保 slideButtonLeft的值，是正确的
		if(slideBtnLeft<0){
			slideBtnLeft = 0;
		}
		if(slideBtnLeft > maxLeft){
			slideBtnLeft = maxLeft;
		}
		/**
		 * 刷新view 会导致 onDraw方法的执行
		 */
		invalidate();
	}
	public interface OnToggleButtonClick{
		void onChangeState();
	}
	public void setOnToggleBtnClick(OnToggleButtonClick onToggleBtnClick) {
		this.onToggleBtnClick = onToggleBtnClick;
	}
}

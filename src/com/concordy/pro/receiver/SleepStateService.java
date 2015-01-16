package com.concordy.pro.receiver;

import java.util.List;

import com.concordy.pro.PinLoginActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
/***
 * @author Scleo
 */
public class SleepStateService extends Service {
	private ScreenOnReceiver mReceiverOn;
	private ScreenOffReceiver mReceiverOff;
	private static final int PIN_LOCK_TIME = 60;
	private static final String PACKGE_NAME = "com.leo.demo";
	private static final String TOPTASK_NAME = "com.leo.demo.PinLoginActivity";
	private boolean isChecked;
	private IntentFilter mFilter;
	private String tag = "SCLEO";
	private boolean mRecorded = false;
	protected boolean running;
	private ActivityManager mActManager;
	private long mBackgroudTime;
	private MyServiceThread mMst;
	private List<RunningTaskInfo> mTasks;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		mReceiverOn = new ScreenOnReceiver();
		mFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		registerReceiver(mReceiverOn, mFilter);
		mReceiverOff = new ScreenOffReceiver();
		mFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiverOff, mFilter);
		running = true;
		Log.d(tag, "服务启动");
		Log.d(tag, "线程的枚举值：{[New:" + Thread.State.NEW + "],[Waiting:"
				+ Thread.State.WAITING + "],[TimeWaiting:"
				+ Thread.State.TIMED_WAITING + "],[Runnable:"
				+ Thread.State.RUNNABLE + "]}");
		Log.d(tag, "线程的枚举值name：{[New:" + Thread.State.NEW.name()
				+ "],[Waiting:" + Thread.State.WAITING.name()
				+ "],[TimeWaiting:" + Thread.State.TIMED_WAITING.name()
				+ "],[Runnable:" + Thread.State.RUNNABLE.name() + "]}");
		initTask();
	}

	/******* 循环监听当前的任务栈 **********/
	private void initTask() {
		mMst = new MyServiceThread();
		// 执行run 方法
		mMst.start();
	}
	@Override
	public void onDestroy() {
		Log.d(tag, "服务销毁");
		running = false;
		if (mReceiverOn != null)
			unregisterReceiver(mReceiverOn);
		if (mReceiverOff != null)
			unregisterReceiver(mReceiverOff);
		if (mMst!=null) {
			mMst.interrupt();
			mMst=null;
		}
	}
	/*******解锁屏幕监听*************/
	class ScreenOnReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// mLock = System.currentTimeMillis();
			Log.d(tag, "程序进入后台的时间：" + mBackgroudTime);
			Log.d(tag,
					"循环线程的状态：" + mMst.getState().name() + ",线程是否可用："
							+ mMst.isAlive());
			if (getCurrentPackage().equals(PACKGE_NAME)) {
				int times = getSecond(System.currentTimeMillis()
						- mBackgroudTime);
				startOrReset(times);
			}
		}
	}
	/******锁屏监听*****************/
	class ScreenOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(tag, "广播开启了,屏幕锁住了,程序仍然在后台");
			Log.d(tag, "锁屏时间：" + getBackTime());
			Log.d(tag,
					"循环线程的状态：" + mMst.getState().name() + ",线程是否可用："
							+ mMst.isAlive());
		}
	}
	/****
	 * 将毫秒转成秒
	 * @param time
	 * @return
	 */
	public int getSecond(long time) {
		if (time != 0)
			return (int) (time / 1000);
		return 0;
	}
	/******判断是否跳转PIN验证页面*********/
	public void startOrReset(int times) {
		// 重置记录标示
		mRecorded = false;
		Log.d(tag, "程序处于后台："+times+"秒");
		// 如果程序处于后台时间过长，则跳转到PIN验证
		if (times >= PIN_LOCK_TIME) {
			Log.d(tag, "开启PIN验证界面");
			if(!getCurrentTask().equals(TOPTASK_NAME)&&!isChecked){
				jump(PinLoginActivity.class);
			}
		}
	}
	/*****跳转页面*********/
	private void jump(Class<?> cls) {
		Log.d(tag, "跳转test界面");
		Intent intent = new Intent(getApplicationContext(), cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	/****** 得到当前处于任务栈顶的程序报名 **********/
	public String getCurrentTask() {
		if(getTopTask()==null)
			return "";
		return getTopTask().getPackageName();
	}

	/****** 获取当前运行的程序包名 ***********/
	public String getCurrentPackage() {
		if(getTopTask()==null)
			return "";
		return getTopTask().getClassName();
	}
	/*****得到当前运行的栈顶Activity**********/
	private ComponentName getTopTask() {
		if(mActManager!=null)
		{
			if(mTasks==null){
				mTasks = mActManager.getRunningTasks(1);
			}
		}else{
			mActManager = (ActivityManager) getSystemService("activity");
			mTasks = mActManager.getRunningTasks(1);
		}
		return mTasks.get(0).topActivity;
	}
	/***** 记录当前程序处于后台时间 *******/
	public long getBackTime() {
		if (!mRecorded) {
			mRecorded = true;
			return mBackgroudTime = System.currentTimeMillis();
		}
		return mBackgroudTime;
	}
	/******服务核心事务*************/
	class MyServiceThread extends Thread implements Runnable {
		@Override
		public void run() {
			// super.run();
			while (running) {
				Log.d(tag, "线程：" + mMst.getState().name());
				String currPackage = getCurrentPackage();
				if (PACKGE_NAME.equals(currPackage)) {
					if (mRecorded) {
						int mBgTimes = getSecond(System.currentTimeMillis()
								- getBackTime());
						startOrReset(mBgTimes);
					}
				} else {
					long backTime = getBackTime();
					Log.d(tag, "程序处于后台的时间：" + backTime);
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/******* 校验线程是否处于Runnable状态 *************/
	public boolean isRunnableThread(Thread thread) {
		return thread.getState().name().equals(Thread.State.RUNNABLE.name());
	}

	/******* 校验线程是否处于Runnable状态 *************/
	public boolean isTimedWaitingThread(Thread thread) {
		return thread.getState().name()
				.equals(Thread.State.TIMED_WAITING.name());
	}
	/******* 校验线程是否处于waiting状态 *************/
	public boolean isWaitingThread(Thread thread) {
		return thread.getState().name().equals(Thread.State.WAITING.name());
	}
	public boolean isCheck() {
		return isChecked;
	}
	public void setCheck(boolean isCheck) {
		this.isChecked = isCheck;
	}
}

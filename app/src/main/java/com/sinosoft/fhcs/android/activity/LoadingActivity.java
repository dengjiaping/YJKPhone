package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:启动页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.AppStatusManager;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

import static com.sinosoft.fhcs.android.util.Constant.STATUS_NORMAL;

@SuppressLint("HandlerLeak")
public class LoadingActivity extends BaseActivity {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟13秒
	private static final long SPLASH_DELAY_MILLIS = 1000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GO_HOME:
					goHome();
					break;
				case GO_GUIDE:
					goGuide();
					break;
			}
			super.handleMessage(msg);
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		AppStatusManager.getInstance().setAppStatus(STATUS_NORMAL);//进入应用初始化设置成未登录状态
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_loading);
		ExitApplicaton.getInstance().addActivity(this);
		Display mDisplay = getWindowManager().getDefaultDisplay();
		int W = mDisplay.getWidth();
		int H = mDisplay.getHeight();
		Log.i("Main", "Width = " + W);
		Log.i("Main", "Height = " + H);
		// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
		JPushInterface.init(getApplicationContext());
		// 设置时间跳转
		init();
		//Toast.makeText(this, "该版本的渠道为==="+Util.getChannelName(this), 1).show();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("启动页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("启动页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void init() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirstIn) {
			// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}

	}

	private void goHome() {
		// 从首选项获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		String userId = prefs.getString("userId", "");
		Intent intent = new Intent();
		if (userId.equals("")) {
			intent.setClass(LoadingActivity.this, LoginActivity.class);
			startActivity(intent);
		} else {
			intent.setClass(LoadingActivity.this, MainActivity.class);
			startActivity(intent);
		}
		LoadingActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(LoadingActivity.this, GuideActivity.class);
		LoadingActivity.this.startActivity(intent);
		LoadingActivity.this.finish();
	}
}

package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description: 版本更新完成自动安装
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.Constant;
import com.umeng.analytics.MobclickAgent;

public class TishiActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		// setContentView(R.layout.tishi);
		if (Constant.progress != null && Constant.progress.equals("100")) {
			Intent intent = new Intent("guanbinotification");
			sendBroadcast(intent);
			if (Constant.conapk != null)
				installApk();
			clearInfo();
		}

		Constant.progress = null;
		finish();
	}

	// 清空个人信息
	private void clearInfo() {
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("userId", "");
		editor.putString("deviceNum", "");
		editor.putString("nickName", "");
		editor.putString("sex", "");
		editor.putString("area", "");
		editor.putString("phoneNumber", "");
		editor.putString("email", "");
		editor.commit();
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("安装新版本提示页"); //统计页面
		MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("安装新版本提示页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
	void installApk() {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(Constant.conapk),
				"application/vnd.android.package-archive");
		startActivity(intent);
		//
		Constant.conapk = null;

		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}

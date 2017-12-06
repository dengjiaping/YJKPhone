package com.sinosoft.fhcs.android.activity;
/**
 * 主页（侧边+底部菜单）
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.slidefragment.LeftMenuFragment;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends SlidingActivity {
	private SlidingMenu sm;
	public static MainActivity mInstance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		mInstance = this;
		ExitApplicaton.getInstance().addActivity(this);
		// set the Behind View
		setBehindContentView(R.layout.frame_menu);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		LeftMenuFragment menuFragment = new LeftMenuFragment();
		fragmentTransaction.replace(R.id.menu, menuFragment);
		fragmentTransaction.commit();
		initSlidingMenu();
		CommonUtil.initMiao(this.getApplicationContext());
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);       //统计时长
	}



	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	private void initSlidingMenu() {
		// customize the SlidingMenu
		sm = getSlidingMenu();
		sm.setShadowWidth(50);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(130);
		sm.setFadeDegree(0.35f);
		// sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				toggle();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if(sm.isMenuShowing()){
				//打开侧边栏
			}else{
				// 退出
				new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
						.setCancelable(false)
						.setTitle("温馨提示")
						.setMessage("您确定要退出吗?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int which) {
										ExitApplicaton.getInstance().exit();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int which) {

									}
								}).show();
			}


			return true;// 不知道返回true或是false有什么区别??
		}

		return super.dispatchKeyEvent(event);
	}
}

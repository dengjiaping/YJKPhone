package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:通知设定页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class NotifySettingActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private TextView tvRemind;
	private TextView tvInformation;
	private ImageView ivRemind;
	private ImageView ivInformation;
	private Button btnBack;
	private Button cbtnRemind;// 服药提醒
	private Button cbtnInformation;// 健康资讯提醒
	private int informationPush = 1;// 健康咨询推送 1-推送0-不推送
	private int medicinePush = 1; // 服药提醒推送
	private String userId;
	private ProgressDialog pDNotify;// 进度条
	private static final int OK = 1001;// 成功
	private static final int Fail = 1002;// 失败
	private static final int ChaoShi = 1003;// 超时
	private int PF = 1000;
	private boolean isRemind=true;
	private boolean isInformation=true;
	private int state=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_notifyset);
		ExitApplicaton.getInstance().addActivity(this);
		SharedPreferences prefs1 = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs1.getString("userId", "");
		init();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("通知设定页"); //统计页面
		MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("通知设定页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_notify));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		initButton();
	}

	private void initButton() {
		tvRemind = (TextView) findViewById(R.id.notifyset_tv_remind);
		ivRemind = (ImageView) findViewById(R.id.notify_icon1);
		tvInformation = (TextView) findViewById(R.id.notifyset_tv_information);
		ivInformation = (ImageView) findViewById(R.id.notify_icon2);
		cbtnRemind = (Button) findViewById(R.id.notifyset_cbtn_remind);
		cbtnInformation = (Button) findViewById(R.id.notifyset_cbtn_information);
		cbtnRemind.setOnClickListener(this);
		cbtnInformation.setOnClickListener(this);

		SharedPreferences prefs = getSharedPreferences("isNotify",
				Context.MODE_PRIVATE);
		isRemind = prefs.getBoolean("isRemind", true);
		isInformation = prefs.getBoolean("isInformation", true);
		initStateMedicine();
		initStateInformation();

	}

	// 服药提醒状态
	private void initStateMedicine() {
		if (isRemind) {
			medicinePush = 1;
			tvRemind.setTextColor(this.getResources().getColor(
					R.color.text_notify_black));
			cbtnRemind.setBackgroundResource(R.drawable.notify_btn_open);
			ivRemind.setBackgroundResource(R.drawable.notify_icon_m1);
		} else {
			medicinePush = 0;
			tvRemind.setTextColor(this.getResources().getColor(
					R.color.text_notify_gray));
			cbtnRemind.setBackgroundResource(R.drawable.notify_btn_close);
			ivRemind.setBackgroundResource(R.drawable.notify_icon_m2);
		}

	}

	// 资讯状态
	private void initStateInformation() {
		if (isInformation) {
			informationPush = 1;
			tvInformation.setTextColor(this.getResources().getColor(
					R.color.text_notify_black));
			cbtnInformation.setBackgroundResource(R.drawable.notify_btn_open);
			ivInformation.setBackgroundResource(R.drawable.notify_icon_in1);

		} else {
			informationPush = 0;
			tvInformation.setTextColor(this.getResources().getColor(
					R.color.text_notify_gray));
			cbtnInformation.setBackgroundResource(R.drawable.notify_btn_close);
			ivInformation.setBackgroundResource(R.drawable.notify_icon_in2);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				finish();
				break;
			case R.id.notifyset_cbtn_remind:
				// 服药提醒
				state=1;
				if(isRemind){
					isRemind=false;
				}else{
					isRemind=true;
				}
				initStateMedicine();
				SendRequest();
				break;
			case R.id.notifyset_cbtn_information:
				// 健康资讯
				state=2;
				if(isInformation){
					isInformation=false;
				}else{
					isInformation=true;
				}
				initStateInformation();
				SendRequest();
				break;
			default:
				break;
		}

	}

	// 发送请求
	private void SendRequest() {
		UpdateRequest re = new UpdateRequest();
		re.execute(HttpManager.urlNotify(userId, informationPush, medicinePush));

	}

	//结果
	private void initResult() {
		if (PF == ChaoShi) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
			initState();
		} else if (PF == Fail) {
			initState();
//			Constant.showDialog(this, "更改通知状态失败，请稍后重试！");
			Toast.makeText(this, "更改通知状态失败，请稍后重试!", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			state=0;
		}

	}
	private void initState() {
		// 改回原来的状态
		if(state==1){
			if (medicinePush == 1) {
				isRemind=false;
				medicinePush = 0;
				cbtnRemind.setBackgroundResource(R.drawable.notify_btn_close);
				tvRemind.setTextColor(this.getResources().getColor(
						R.color.text_notify_gray));
				ivRemind.setBackgroundResource(R.drawable.notify_icon_m2);
			} else {
				isRemind=true;
				medicinePush = 1;
				cbtnRemind.setBackgroundResource(R.drawable.notify_btn_open);
				tvRemind.setTextColor(this.getResources().getColor(
						R.color.text_notify_black));
				ivRemind.setBackgroundResource(R.drawable.notify_icon_m1);
			}
			state=0;
		}else if(state==2){
			if (informationPush == 1) {
				isInformation=false;
				informationPush = 0;
				cbtnInformation.setBackgroundResource(R.drawable.notify_btn_close);
				tvInformation.setTextColor(this.getResources().getColor(
						R.color.text_notify_gray));
				ivInformation.setBackgroundResource(R.drawable.notify_icon_in2);
			} else {
				isInformation=true;
				informationPush = 1;
				cbtnInformation.setBackgroundResource(R.drawable.notify_btn_open);
				tvInformation.setTextColor(this.getResources().getColor(
						R.color.text_notify_black));
				ivInformation.setBackgroundResource(R.drawable.notify_icon_in1);
			}
			state=0;
		}



	}
	// 更新状态
	private class UpdateRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			pDNotify = new ProgressDialog(NotifySettingActivity.this);
			Constant.showProgressDialog(pDNotify);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("notifyUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OK;
						initResult();
					} else {
						PF = Fail;
						initResult();
					}
				} catch (JSONException e) {
					PF = Fail;
					initResult();
					System.out.println("解析错误");
					e.printStackTrace();
				}
			}
			Constant.exitProgressDialog(pDNotify);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}

}

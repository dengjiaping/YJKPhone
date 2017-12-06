package com.sinosoft.fhcs.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FindPassOneActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private EditText edtPhone, edtCode;
	private TextView btnGetCode;
	private Button btnNext;
	private TimeCount time;
	/**
	 * 网络请求
	 */
	private ProgressDialog progressDialog;// 进度条
	private static final int OKGetYZM = 1001;// 验证码获取成功
	private static final int FailGetYZM = 1002;// 验证码获取失败
	private static final int OKJiaoYanYZM = 1003;// 验证码校验成功
	private static final int FailJiaoYanYZM = 1004;// 验证码校验失败
	private static final int FailServer = 1005;// 连接超时
	private int PF = 1000;
	public static  FindPassOneActivity instance;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_findpassone);
		ExitApplicaton.getInstance().addActivity(this);
		init();
		instance=this;
		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
	}

	public static FindPassOneActivity getInstance() {
		return instance;
	}
	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.findpass_tv1));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		edtPhone = (EditText) findViewById(R.id.findpassone_edt_phonenum);
		edtPhone.setHint(Constant.textHint(
				getResources().getString(R.string.findpass_tv2), 15));
		edtCode = (EditText) findViewById(R.id.findpassone_edt_code);
		edtCode.setHint(Constant.textHint(
				getResources().getString(R.string.findpass_tv3), 15));
		btnNext = (Button) findViewById(R.id.findpassone_btn_next);
		btnNext.setOnClickListener(this);
		btnGetCode = (TextView) findViewById(R.id.findpassone_btn_sentcode);
		btnGetCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				finish();
				break;
			case R.id.findpassone_btn_sentcode:
				String strPhone = edtPhone.getText().toString().trim();
				if (strPhone.equals("")) {
					Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!Constant.isPhone(strPhone)) {
					Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
					return;
				}
//			Toast.makeText(this, "获取验证码", Toast.LENGTH_SHORT).show();
				if (HttpManager.isNetworkAvailable(this)) {
					GetYZMRequest request = new GetYZMRequest();
					request.execute(HttpManager.urlYZMMember(strPhone));
				} else {
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case R.id.findpassone_btn_next:
				String strPhone2 = edtPhone.getText().toString().trim();
				String strCode = edtCode.getText().toString().trim();
				if (strPhone2.equals("")) {
					Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!Constant.isPhone(strPhone2)) {
					Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (strCode.equals("")) {
					Toast.makeText(this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (HttpManager.isNetworkAvailable(this)) {
					JiaoYanYZMRequest re = new JiaoYanYZMRequest();
					re.execute(HttpManager.urlJiaoYanYZM(strPhone2, strCode));
				} else {
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
				}
//			Toast.makeText(this, "下一步", Toast.LENGTH_SHORT).show();
//			gotoNext();
				break;
			default:
				break;
		}

	}

	// 获取验证码
	private class GetYZMRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(FindPassOneActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("GetCodeUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						PF = OKGetYZM;
						initResult();
					} else {
						PF = FailGetYZM;
						initResult();
					}
				} catch (JSONException e) {
					PF = FailGetYZM;
					initResult();
					System.out.println("解析错误");
					e.printStackTrace();
				}
			}
			Constant.exitProgressDialog(progressDialog);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}

	// 校验验证码
	private class JiaoYanYZMRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(FindPassOneActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("JiaoYanCodeUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						PF = OKJiaoYanYZM;
						initResult();
					} else {
						PF = FailJiaoYanYZM;
						initResult();
					}
				} catch (JSONException e) {
					PF = FailJiaoYanYZM;
					initResult();
					System.out.println("解析错误");
					e.printStackTrace();
				}
			}
			Constant.exitProgressDialog(progressDialog);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}

	private void initResult() {
		if (PF == FailServer) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKGetYZM) {
			time.start();//开始计时
			Toast.makeText(this, "验证码已发送，请注意查收!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailGetYZM) {
			Toast.makeText(this, "验证码发送失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKJiaoYanYZM) {
			Toast.makeText(this, "验证码校验成功!", Toast.LENGTH_SHORT).show();
			gotoNext();
		} else if (PF == FailJiaoYanYZM) {
			Toast.makeText(this, "验证码输入有误!", Toast.LENGTH_SHORT).show();
		}
	}
	private void gotoNext() {
		Intent intent = new Intent(FindPassOneActivity.this,
				FindPassTwoActivity.class);
		intent.putExtra("strPhone", edtPhone.getText().toString().trim());
		intent.putExtra("strCode", edtCode.getText().toString().trim());
		startActivity(intent);
		if(time!=null){
			time.cancel();
		}

	}
	@Override
	protected void onDestroy() {
		if(time!=null){
			time.cancel();
		}
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("找回密码页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		btnGetCode.setText(getResources().getString(R.string.findpass_tv4));
		btnGetCode.setTextColor(getResources().getColorStateList(R.drawable.text_sentcode_selector));
		btnGetCode.setClickable(true);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("找回密码页"); // 保证 onPageEnd 在onPause 之前调用,
		MobclickAgent.onPause(this);
	}
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
			btnGetCode.setText(getResources().getString(R.string.findpass_tv4));
			btnGetCode.setTextColor(getResources().getColorStateList(R.drawable.text_sentcode_selector));
			btnGetCode.setClickable(true);
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
			btnGetCode.setClickable(false);
			btnGetCode.setText("重新发送("+millisUntilFinished /1000+")");
			btnGetCode.setTextColor(Color.parseColor("#9A9691"));
		}
	}
}

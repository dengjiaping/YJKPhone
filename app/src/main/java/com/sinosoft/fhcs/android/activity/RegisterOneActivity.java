package com.sinosoft.fhcs.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterOneActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private EditText edtPhone, edtPass, edtCode;// 手机号，密码，验证码
	private Button btnNext, btnGetCode;// 下一步，获取验证码
	// 获取验证码
	private ProgressDialog progressDialog;// 进度条
	private static final int FailServer = 1001;// 连接超时
	private static final int OKGetYZM = 1002;// 验证码获取成功
	private static final int FailGetYZM = 1003;// 验证码获取失败
	private static final int OKJiaoYanYZM = 1004;// 验证码校验成功
	private static final int FailJiaoYanYZM = 1005;// 验证码校验失败
	private int PF = 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_registerone);
		ExitApplicaton.getInstance().addActivity(this);
		init();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("注册页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("注册页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void init() {
		// titlebar
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_register));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		// content
		edtPhone = (EditText) findViewById(R.id.regone_edt_phone);
		edtPass = (EditText) findViewById(R.id.regone_edt_pass);
		edtCode = (EditText) findViewById(R.id.regone_edt_code);
		btnGetCode = (Button) findViewById(R.id.regone_btn_code);
		btnGetCode.setOnClickListener(this);
		btnNext = (Button) findViewById(R.id.regone_btn_next);
		btnNext.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.regone_btn_code:
				// 获取验证码 //调接口，以短信的形式发到手机上
				if (!HttpManager.isNetworkAvailable(this)) {
					// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String strPhone = edtPhone.getText().toString().trim();
				if (strPhone.equals("")) {
					Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!Constant.isPhone(strPhone)) {
					Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
					return;
				}
				GetYZMRequest re = new GetYZMRequest();
				re.execute(HttpManager.urlYZMRegister(strPhone));
				break;
			case R.id.regone_btn_next:
				String strPhoneNum = edtPhone.getText().toString().trim();
				String strCode = edtCode.getText().toString().trim();
				String strPass = edtPass.getText().toString().trim();
				if (strPhoneNum.equals("") || strPass.equals("")
						|| strCode.equals("")) {
					Toast.makeText(this, "手机号、密码、验证码均不能为空！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (strPass.length() < 6 || strPass.length() > 15) {
					Toast.makeText(this, "密码长度6-15位！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!Constant.isPhone(strPhoneNum.toString())) {
					Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
					return;
				}
				// 校验验证码
				if (HttpManager.isNetworkAvailable(this)) {
					JiaoYanYZMRequest req = new JiaoYanYZMRequest();
					req.execute(HttpManager.urlJiaoYanYZM(strPhoneNum, strCode));
				} else {
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
				}
				break;
		}

	}

	// 验证码请求结果
	private void initResult(String message) {
		if (PF == FailServer) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKGetYZM) {
			Toast.makeText(this, "验证码已发送，请注意查收!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailGetYZM) {
			if(message.equals("")){
				Toast.makeText(this, "验证码发送失败!", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			}

		} else if (PF == OKJiaoYanYZM) {
			// Toast.makeText(this, "验证码校验成功!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, RegisterTwoActivity.class);
			intent.putExtra("strPhoneNum", edtPhone.getText().toString().trim());
			intent.putExtra("strPass", edtPass.getText().toString().trim());
			startActivity(intent);
		} else if (PF == FailJiaoYanYZM) {
			Toast.makeText(this, "验证码输入有误!", Toast.LENGTH_SHORT).show();
		}
	}

	// 获取验证码
	private class GetYZMRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(RegisterOneActivity.this);
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
				initResult("");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						PF = OKGetYZM;
						initResult("");
					} else {
						JSONObject jo2=jo.optJSONObject("error");
						String message = jo2.optString("message");
						PF = FailGetYZM;
						initResult(message);
					}
				} catch (JSONException e) {
					PF = FailGetYZM;
					initResult("");
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
			progressDialog = new ProgressDialog(RegisterOneActivity.this);
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
				initResult("");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						PF = OKJiaoYanYZM;
						initResult("");
					} else {
						PF = FailJiaoYanYZM;
						initResult("");
					}
				} catch (JSONException e) {
					PF = FailJiaoYanYZM;
					initResult("");
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
}

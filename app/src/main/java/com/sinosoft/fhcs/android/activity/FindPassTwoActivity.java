package com.sinosoft.fhcs.android.activity;

import android.app.ProgressDialog;
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

public class FindPassTwoActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private Button btnFinish;
	private TextView tvPhone;
	private String strPhone, strCode;
	private EditText edtNewPass, edtAgainPass;
	// 请求数据
	private ProgressDialog progressDialog;// 进度条
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private int PF = 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_findpasstwo);
		ExitApplicaton.getInstance().addActivity(this);
		strPhone = this.getIntent().getExtras().getString("strPhone");
		strCode = this.getIntent().getExtras().getString("strCode");
		init();
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.findpass_tv1));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		tvPhone = (TextView) findViewById(R.id.findpasstwo_tv_phone);
		tvPhone.setText(strPhone);
		edtNewPass = (EditText) findViewById(R.id.findpasstwo_edt_newpass);
		edtNewPass.setHint(Constant.textHint(
				getResources().getString(R.string.findpass_tv9), 15));
		edtAgainPass = (EditText) findViewById(R.id.findpasstwo_edt_againpass);
		edtAgainPass.setHint(Constant.textHint(
				getResources().getString(R.string.findpass_tv10), 15));
		btnFinish = (Button) findViewById(R.id.findpasstwo_btn_finish);
		btnFinish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				finish();
				break;
			case R.id.findpasstwo_btn_finish:
				String strNewPass = edtNewPass.getText().toString().trim();
				String strAgainPass = edtAgainPass.getText().toString().trim();
				if (!HttpManager.isNetworkAvailable(this)) {
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (strNewPass.equals("") || strAgainPass.equals("")) {
					Toast.makeText(this, "密码均不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if (strNewPass.length() < 6 || strAgainPass.length() < 6
						|| strNewPass.length() > 15 || strAgainPass.length() > 15) {
					Toast.makeText(this, "密码长度6-15位！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!(strNewPass.equals(strAgainPass))) {
					Toast.makeText(this, "新密码与确认新密码不一致！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				// 提交
				ModifyRequest re = new ModifyRequest();
				re.execute(HttpManager.urlFindPass(strPhone, strCode,
						strNewPass));
//			Toast.makeText(this, "找回成功！", Toast.LENGTH_SHORT).show();
//			finish();
//			FindPassOneActivity.getInstance().finish();
				break;
			default:
				break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("找回密码页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("找回密码页"); // 保证 onPageEnd 在onPause 之前调用,
		MobclickAgent.onPause(this);
	}

	// 网络请求
	private class ModifyRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(FindPassTwoActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("FindPassUrl", url + "");
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
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OK;
						String message = "";
						if (!jo.isNull("message")) {
							message = jo.optString("message");
						}
						initResult(message);
					} else {
						PF = Fail;
						String errormsg = "";
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
						initResult(errormsg);
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = Fail;
					initResult("");
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

	// 请求结果
	private void initResult(String message) {
		if (PF == FailServer) {
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			if (message.equals("")) {
				Toast.makeText(this, "找回失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			}
		} else if (PF == OK) {
			if (message.equals("")) {
				Toast.makeText(this, "找回成功！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			}
			finish();
			FindPassOneActivity.getInstance().finish();
		}
	}
}

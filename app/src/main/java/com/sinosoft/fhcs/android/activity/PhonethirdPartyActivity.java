package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:绑定手机页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class PhonethirdPartyActivity extends BaseActivity implements OnClickListener{
	private TextView tvTitle;
	private Button btnBack;
	private EditText edtPhone,edtCode;//手机号，验证码
	private Button btnCode;//获取验证码
	private Button btnCommit;//提交
	// 获取验证码
	private ProgressDialog progressDialog;// 进度条
	private static final int FailServer = 1001;// 连接超时
	private static final int OKYZM = 1002;// 验证码获取成功
	private static final int FailYZM = 1003;// 验证码获取失败
	private static final int OKCommit = 1004;// 提交成功
	private static final int FailCommit = 1005;// 提交失败
	private int PF = 1000;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_phone);
		ExitApplicaton.getInstance().addActivity(this);
		init();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("绑定手机页"); //统计页面
		MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("绑定手机页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
	private void init() {
		tvTitle=(TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_phone));
		btnBack=(Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		edtPhone=(EditText) findViewById(R.id.phone_edt_phone);
		edtCode=(EditText) findViewById(R.id.phone_edt_code);
		btnCode=(Button) findViewById(R.id.phone_btn_code);
		btnCode.setOnClickListener(this);
		btnCommit=(Button) findViewById(R.id.phone_btn_commit);
		btnCommit.setOnClickListener(this);
	}
	// 返回
	private void goback() {
		if (edtPhone.getText().toString().trim().equals("")
				&& edtCode.getText().toString().trim().equals("")) {
			finish();
		} else {
			Constant.showDialog2(this, "尚未完成绑定，是否要离开本页面？");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			goback();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				goback();
				break;
			case R.id.phone_btn_code:
				//获取验证码
				// 获取验证码 //调接口，以短信的形式发到手机上
				if (!HttpManager.isNetworkAvailable(this)) {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试!", Toast.LENGTH_SHORT).show();
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
				YZMRequest re = new YZMRequest();
				re.execute(HttpManager.urlYZM2(strPhone));
				Constant.hiddenInput(this);
				break;
			case R.id.phone_btn_commit:
				//提交
				if (HttpManager.isNetworkAvailable(this)) {
					String strPhoneNum = edtPhone.getText().toString().trim();
					String strCode = edtCode.getText().toString().trim();
					if (strPhoneNum.equals("")|| strCode.equals("")) {
						Toast.makeText(this, "手机号、验证码均不能为空！",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (!Constant.isPhone(strPhoneNum.toString())) {
						Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					// 验证码后台验证
					SharedPreferences prefs = getSharedPreferences("UserInfo",
							Context.MODE_PRIVATE);
					String userId = prefs.getString("userId", "");
					CommitRequest req = new CommitRequest();
					req.execute(HttpManager.thirdPartyBindPhone(strCode, strPhoneNum,userId));
				} else {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试!", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}

	}
	// 验证码请求结果
	private void initResultYZM(String errormsg) {
		if (PF == FailServer) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailYZM) {
			if (errormsg.equals("")) {
				Toast.makeText(this, "验证码发送失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show();
			}
		} else if (PF == OKYZM) {
			if (errormsg.equals("")) {
				Toast.makeText(this, "验证码已发送，请注意查收!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show();
			}
		}
	}
	// 提交请求结果
	private void initResultCommit(String errormsg) {
		if (PF == FailServer) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailCommit) {
			if (errormsg.equals("")) {
				Toast.makeText(this, "提交失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show();
			}
		} else if (PF == OKCommit) {
			SharedPreferences prefs = getSharedPreferences("UserInfo",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("phoneNumber", edtPhone.getText().toString().trim());
			editor.commit();
			ExitApplicaton.userInfo.setPhone(edtPhone.getText().toString().trim());
			CommonUtil.initMiao(this.getApplicationContext());//手机号提交完成后给重新请求妙健康的服务
			if (errormsg.equals("")) {
				Toast.makeText(this, "提交成功!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show();
			}
			finish();
		}

	}
	// 验证码请求
	private class YZMRequest extends AsyncTask<Object, Void, String> {
		private String urlYZM;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(PhonethirdPartyActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			urlYZM = (String) params[0];
			result = HttpManager.phoneSessionFromGet(urlYZM, "");
			Log.e("YZMUrl", urlYZM + "");
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResultYZM("");
			} else {
				try {
					String errormsg = "";
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
						Log.e("验证码", jo.optString("message"));
						PF = OKYZM;
						initResultYZM(errormsg);
					} else {
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
						PF = FailYZM;
						initResultYZM(errormsg);
					}

				} catch (JSONException e) {
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
	// 提交请求
	private class CommitRequest extends AsyncTask<Object, Void, String> {
		private String urlCommit;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(PhonethirdPartyActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			urlCommit = (String) params[0];
			Log.e("urlCommit", urlCommit + "");
			result = HttpManager.phoneSessionFromGet(urlCommit, Constant.sessionIdPhone);
			Log.e("CommitUrl", result + "");
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResultCommit("");
			} else {
				try {
					String errormsg = "";
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (!jo.isNull("errormsg")) {
						errormsg = jo.optString("errormsg");
					}
					if (resultCode.equals("1")) {
						PF = OKCommit;
						initResultCommit(errormsg);
					} else {
						PF = FailCommit;
						initResultCommit(errormsg);
					}

				} catch (JSONException e) {
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

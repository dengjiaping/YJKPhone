package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:修改密码页
 * @Author: wangshuangshuang.
 * @Create: 2015年4月9日.
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

public class ModifyPassActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private EditText edtOldPass, edtNewPass, edtAgainPass;
	private Button btnSure;
	private String userId = "";
	// 请求数据
	private ProgressDialog progressDialog;// 进度条
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private static final int FailPass = 1004;// 旧密码不正确
	private int PF = 1000;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_modify);
		ExitApplicaton.getInstance().addActivity(this);
		init();
		// 从首选项获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("修改密码页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("修改密码页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_modify));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		edtOldPass = (EditText) findViewById(R.id.modify_edt_oldpass);
		edtNewPass = (EditText) findViewById(R.id.modify_edt_newpass);
		edtAgainPass = (EditText) findViewById(R.id.modify_edt_againpass);
		btnSure = (Button) findViewById(R.id.modify_btn_sure);
		btnSure.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.modify_btn_sure:
				// 确定
				String strOldPass = edtOldPass.getText().toString().trim();
				String strNewPass = edtNewPass.getText().toString().trim();
				String strAgainPass = edtAgainPass.getText().toString().trim();
				if (!HttpManager.isNetworkAvailable(this)) {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (strOldPass.equals("") || strNewPass.equals("")
						|| strAgainPass.equals("")) {
					Toast.makeText(this, "所有密码均不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if (strOldPass.length() < 6 || strNewPass.length() < 6
						|| strAgainPass.length() < 6||strOldPass.length() > 15 || strNewPass.length()> 15
						|| strAgainPass.length() > 15) {
					Toast.makeText(this, "密码长度6-15位！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!(strNewPass.equals(strAgainPass))) {
					Toast.makeText(this, "新密码与确认新密码不一致！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				//提交
				ModifyRequest re=new ModifyRequest();
				re.execute(HttpManager.urlUpdatePassword(userId, strOldPass, strNewPass));
				break;
			default:
				break;
		}

	}
	// 网络请求
	private class ModifyRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ModifyPassActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("ModifyPassUrl", url + "");
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
						JSONObject data=jo.optJSONObject("data");
						boolean isOK=data.optBoolean("isOK");
						if(isOK){
							PF = OK;
							initResult("");
						}else{
							PF = FailPass;
							initResult("");
						}

					} else {
						String message=jo.optString("message");
						PF = Fail;
						initResult(message);
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
//				Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			if(message.equals("")){
				Toast.makeText(this, "修改失败!", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			}
		}else if(PF==FailPass){
			Toast.makeText(this, "旧密码不正确！", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}

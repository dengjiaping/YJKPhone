package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:绑定机顶盒页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
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
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class JiDingHeActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private TextView tvUserId;// 用户id
	private EditText edtCode;// 机顶盒编码
	private Button btnCommit;// 提交
	private String strUserId = "";
	private String code = "";
	private String LoginUserName = "";// 登陆账号
	private TextView tvHelp;
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
		setContentView(R.layout.activity_jidinghe);
		ExitApplicaton.getInstance().addActivity(this);
		init();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("绑定机顶盒页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("绑定机顶盒页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_jidinghe));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		// 从首选项获取信息
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		strUserId = prefs.getString("userId", "");
		code = prefs.getString("deviceNum", "");
		edtCode = (EditText) findViewById(R.id.jidinghe_edt_code);
		if (code.equals("")) {
			edtCode.setText("");
		} else {
			edtCode.setText(code);
		}
		tvUserId = (TextView) findViewById(R.id.jidinghe_tv_userid);
		SharedPreferences prefs2 = getSharedPreferences("LoginUserName",
				Context.MODE_PRIVATE);
		LoginUserName = prefs2.getString("userName", "");
		tvUserId.setText(LoginUserName);

		btnCommit = (Button) findViewById(R.id.jidinghe_btn_commit);
		btnCommit.setOnClickListener(this);

		tvHelp = (TextView) findViewById(R.id.jidinghe_tv_help);
		tvHelp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvHelp.setOnClickListener(this);
	}

	// 返回
	private void goback() {
		if (edtCode.getText().toString().trim().equals(code)) {
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
			case R.id.jidinghe_tv_help:
				// 帮助说明
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(HttpManager.urlGetJiDingHeHelp());
				intent.setData(content_url);
				startActivity(intent);
				break;
			case R.id.jidinghe_btn_commit:
				// 提交
				if (edtCode.getText().toString().trim().equals("")) {
					Toast.makeText(this, "机顶盒编码不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!HttpManager.isNetworkAvailable(this)) {
					// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				CommitRequest re = new CommitRequest();
				re.execute(HttpManager.urlJiDingHe(strUserId, edtCode.getText()
						.toString().trim()));
				break;
			default:
				break;
		}

	}

	// 网络请求
	private class CommitRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(JiDingHeActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("commitUrl", url + "");
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
						initResult("");
					} else {
						String errormsg = "";
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
						PF = Fail;
						initResult(errormsg);
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

	// 请求结果
	private void initResult(String errormsg) {
		if (PF == FailServer) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			if (errormsg.equals("")) {
				Toast.makeText(this, "提交失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show();
			}
		} else if (PF == OK) {
			Toast.makeText(this, "提交成功！", Toast.LENGTH_SHORT).show();
			saveInfo(edtCode.getText().toString().trim());
			finish();
		}
	}

	// 保存个人信息
	private void saveInfo(String deviceNum) {
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("deviceNum", deviceNum);
		editor.commit();

	}
}

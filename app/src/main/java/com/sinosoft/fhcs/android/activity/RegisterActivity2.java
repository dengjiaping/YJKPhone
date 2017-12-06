package com.sinosoft.fhcs.android.activity;

/**
 * 注册页
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.customview.RegisterMenuPop;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity2 extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private TextView tvTitle;
	private Button btnBack;
	private View titlebar;
	private CheckBox cbtnAccept;// 是否同意条款
	private TextView tvService;// 服务
	private Button btnCommit;
	private static int mCurrPos_F = 0;// 家庭成员当前
	private static List<FamilyMember> getFamilyMemberList = new ArrayList<FamilyMember>();
	private static ImageView img;
	private static TextView tvRoleName;
	private Button btnChange;//更换
	// 获取验证码
	private ProgressDialog progressDialog;// 进度条
	private static final int FailServer = 1001;// 连接超时
	private static final int OKGetYZM = 1002;// 验证码获取成功
	private static final int FailGetYZM = 1003;// 验证码获取失败
	private static final int OKRegister = 1004;// 注册成功
	private static final int FailRegister = 1005;// 注册失败
	private int PF = 1000;
	private String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_register2);
		ExitApplicaton.getInstance().addActivity(this);
		init();
		userId = getIntent().getStringExtra("userId");
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

	public static void initData(int position) {
		mCurrPos_F = position;
		img.setImageResource(Constant.ImageId(
				getFamilyMemberList.get(mCurrPos_F).getFamilyRoleName(),
				getFamilyMemberList.get(mCurrPos_F).getGender()));
		tvRoleName.setText(getFamilyMemberList.get(mCurrPos_F)
				.getFamilyRoleName());
	}

	private void init() {
		// titlebar
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_register));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		titlebar = (View) findViewById(R.id.registerone_view);
		cbtnAccept = (CheckBox) findViewById(R.id.regtwo_cbtn_accept);
		btnCommit = (Button) findViewById(R.id.regtwo_btn_finish);
		btnCommit.setOnClickListener(this);
		cbtnAccept.setOnCheckedChangeListener(this);
		cbtnAccept.setChecked(true);
		img = (ImageView) findViewById(R.id.regone_img);
		tvRoleName = (TextView) findViewById(R.id.regone_tv_rolename);
		btnChange=(Button) findViewById(R.id.regone_btn_change);
		btnChange.setOnClickListener(this);
		// 家庭成员
		getFamilyMemberList = Constant.getYzList();
		initData(0);
		// 服务
		tvService = (TextView) findViewById(R.id.regtwo_tv_service);
		tvService
				.setText(Html
						.fromHtml("<a href=\"http://www.yjkang.cn/protocol.jsp\">用户协议</a>"));
		tvService.setMovementMethod(LinkMovementMethod.getInstance());
		CharSequence text = tvService.getText();
		if (text instanceof Spannable) {

			int end = text.length();
			Spannable sp = (Spannable) tvService.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);

			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans(); // should clear old spans
			for (URLSpan url : urls) {
				URLSpan myURLSpan = new URLSpan(url.getURL());
				style.setSpan(myURLSpan, sp.getSpanStart(url),
						sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				style.setSpan(new ForegroundColorSpan(this.getResources()
								.getColor(R.color.text_reg_black)), sp
								.getSpanStart(url), sp.getSpanEnd(url),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置前景色
			}
			tvService.setText(style);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked == true) {
			btnCommit.setEnabled(true);
		} else {
			btnCommit.setEnabled(false);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				clearInfo();
				break;

			case R.id.regtwo_btn_finish:
				// 提交
				if (HttpManager.isNetworkAvailable(this)) {
					RegisterRequest req = new RegisterRequest();
					req.execute(HttpManager
							.urlRegisterNewThird(
									getFamilyMemberList.get(mCurrPos_F).getGender(),
									getFamilyMemberList.get(mCurrPos_F)
											.getBirthday(), getFamilyMemberList
											.get(mCurrPos_F).getFamilyRoleName(),
									getFamilyMemberList.get(mCurrPos_F).getWeight()
											+ "",
									getFamilyMemberList.get(mCurrPos_F).getHeight()
											+ "",
									getFamilyMemberList.get(mCurrPos_F)
											.getStepSize() + "",
									getFamilyMemberList.get(mCurrPos_F).getWaist()
											+ "",userId));
				} else {
					// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case R.id.regone_btn_change:
				RegisterMenuPop menuWindow = new RegisterMenuPop(RegisterActivity2.this, getFamilyMemberList,"2");
				menuWindow.showAsDropDown(titlebar);
				break;
		}

	}


	// 注册请求结果
	private void initResultReg(String errormsg) {
		if (PF == FailServer) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailRegister) {
			if (errormsg.equals("")) {
				Toast.makeText(this, "注册失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show();
			}
		} else if (PF == OKRegister) {
			Toast.makeText(this, "注册成功!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(RegisterActivity2.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}

	}


	// 注册请求
	private class RegisterRequest extends AsyncTask<Object, Void, String> {
		private String urlReg;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(RegisterActivity2.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			urlReg = (String) params[0];
			Log.e("urlReg", urlReg + "");
			result = HttpManager.phoneSessionFromGet(urlReg,
					Constant.sessionIdPhone);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResultReg("");
			} else {
				try {
					String errormsg = "";
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OKRegister;
						initResultReg("");
						String entity = jo.optString("data");
						JSONObject jo2 = new JSONObject(entity);
						String familyId="";
						if(!jo2.isNull("familyId")){
							familyId=jo2.optString("familyId");
						}
						SharedPreferences prefs = getSharedPreferences("UserInfo",
								Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString("familyId", familyId);
						editor.commit();
					} else {
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
						PF = FailRegister;
						initResultReg(errormsg);
					}

				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = FailRegister;
					initResultReg("");
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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		clearInfo();
	}
	public void clearInfo(){
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

}

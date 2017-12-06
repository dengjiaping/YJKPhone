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
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.customview.RegisterMenuPop;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.entity.UserInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class RegisterActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private TextView tvTitle;
	private Button btnBack;
	private View titlebar;
	private EditText edtPhone, edtPass, edtCode;// 手机号，密码，验证码
	private Button btnGetCode;// 获取验证码
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
	private String strPhoneNum;
	private String strCode;
	private String strPass;

	private String alias = "";//别名设置
	private Set<String> tagSet = new LinkedHashSet<String>();//tag设置

	private static final int OK = 1001;// 登录成功
	private static final int FailServer2 = 1002;// 连接超时
	private static final int FailLOGIN = 1003;// 登录失败

	private LoginRequest loginRequest;// 网络请求
	private String isFamilyMember;
	public String userId;
	private int PF2 = 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_register);
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
		// content
		edtPhone = (EditText) findViewById(R.id.regone_edt_phone);
		edtPass = (EditText) findViewById(R.id.regone_edt_pass);
		edtCode = (EditText) findViewById(R.id.regone_edt_code);
		btnGetCode = (Button) findViewById(R.id.regone_btn_code);
		btnGetCode.setOnClickListener(this);
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
			case R.id.regtwo_btn_finish:
				// 提交
				if (HttpManager.isNetworkAvailable(this)) {
					strPhoneNum = edtPhone.getText().toString().trim();
					strCode = edtCode.getText().toString().trim();
					strPass = edtPass.getText().toString().trim();
					if (strPhoneNum.equals("") || strPass.equals("")
							|| strCode.equals("")) {
						Toast.makeText(this, "手机号、密码、验证码均不能为空！", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					if (strPass.length() < 6 || strPass.length() > 15) {
						Toast.makeText(this, "密码长度6-15位！", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					if (!Constant.isPhone(strPhoneNum.toString())) {
						Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					RegisterRequest req = new RegisterRequest();
					req.execute(HttpManager
							.urlRegisterNew(
									strPhoneNum,
									strPass,
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
											+ "", strCode));
				} else {
					// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case R.id.regone_btn_change:
				RegisterMenuPop menuWindow = new RegisterMenuPop(RegisterActivity.this, getFamilyMemberList);
				menuWindow.showAsDropDown(titlebar);
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
			if (message.equals("")) {
				Toast.makeText(this, "验证码发送失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			}

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
			//然后登陆
			// 所有验证均成功
			if (strPhoneNum.equals("") || strPass.equals("")
					|| strCode.equals("")) {
				Toast.makeText(this, "手机号、密码、验证码均不能为空！", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (strPass.length() < 6 || strPass.length() > 15) {
				Toast.makeText(this, "密码长度6-15位！", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (!Constant.isPhone(strPhoneNum.toString())) {
				Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT)
						.show();
			}
			loginRequest = new LoginRequest();
			loginRequest
					.execute(HttpManager.urlThridLogin(strPhoneNum, strPass,"","","","",JPushInterface.getRegistrationID(this)));
			//finish();
		}

	}

	// 获取验证码
	private class GetYZMRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(RegisterActivity.this);
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
						JSONObject jo2 = jo.optJSONObject("error");
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

	// 注册请求
	private class RegisterRequest extends AsyncTask<Object, Void, String> {
		private String urlReg;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(RegisterActivity.this);
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

	// 登录请求
	private class LoginRequest extends AsyncTask<Object, Void, String> {
		private String urlLogin;


		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(RegisterActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			urlLogin = (String) params[0];
			Log.e("loginUrl", urlLogin + "");
			result = HttpManager.getStringContent(urlLogin);
			// Log.e("loginResult", result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("登录成功=="+result);
			if (result.toString().trim().equals("ERROR")) {
				PF2 = FailServer2;
				initResult2("");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						String entity = jo.optString("data");
						JSONObject jo2 = new JSONObject(entity);
						String user = jo2.optString("user");
						alias=jo2.optString("familyId");
						String familyId="";
						if(!jo2.isNull("familyId")){
							familyId=jo2.optString("familyId");
						}
						String loginType="";
						if(!jo2.isNull("loginType")){
							loginType=""+jo2.optInt("loginType");
							System.out.println("loginType===="+loginType);
							Toast.makeText(RegisterActivity.this, "loginType===="+loginType, Toast.LENGTH_SHORT).show();
						}
						if(!jo2.isNull("isFamilyMember")){
							isFamilyMember = jo2.optString("isFamilyMember");
						}
						JSONObject joUser = new JSONObject(user);
						String userId = joUser.optString("id");
						RegisterActivity.this.userId = userId;
						String deviceNum = "";
						if (!joUser.isNull("stbSerialNo")) {
							deviceNum = joUser.optString("stbSerialNo");
						}
						String nickName = "";
						if (!joUser.isNull("nickName")) {
							nickName = joUser.optString("nickName");
						}
						String phoneNumber = "";
						if (!joUser.isNull("phoneNumber")) {
							phoneNumber = joUser.optString("phoneNumber");
						}
						String email = "";
						if (!joUser.isNull("email")) {
							email = joUser.optString("email");
						}
						String area = "";
						if (!joUser.isNull("regionInfo")) {
							area = joUser.optString("regionInfo");
						}
						String sex = "男";
						// if(!joUser.isNull("sex")){
						// sex=joUser.optString("sex");
						// }
						String imgUrl = "";
						if (!joUser.isNull("avatarPath")) {
							imgUrl = joUser.optString("avatarPath");
						}
						String sign="";
						if(!joUser.isNull("sign")){
							sign=joUser.optString("sign");
						}
						PF2 = OK;
						UserInfo userInfo = new UserInfo(userId, deviceNum,
								nickName, sex, area, phoneNumber, email, imgUrl,sign,familyId,loginType);
						saveInfo(userInfo);
						initResult2("");
					} else {
						String errormsg = "";
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
						PF2 = FailLOGIN;
						initResult2(errormsg.trim());
					}

				} catch (JSONException e) {
					PF2 = FailLOGIN;
					initResult2("");
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

	// 设置别名
	private void setAlias() {
		tagSet.add("yjkang_sys");
		tagSet.add("yjkang_android_sys");
		Log.e("LoginActivity-Jpush", "alias="+alias);
		Log.e("LoginActivity-Jpush", "tagSet="+tagSet);
		mHandler.sendMessage(mHandler.obtainMessage());
	}
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			JPushInterface.setAliasAndTags(getApplicationContext(), alias, tagSet, new TagAliasCallback() {

				@Override
				public void gotResult(int code, String alias, Set<String> tags) {
					// TODO Auto-generated method stub
					switch (code) {
						case 0:
							Log.e("LoginActivity-Jpush", "设置别名成功！responseCode="+code);
							break;

						case 6002:
							Log.e("LoginActivity-Jpush", "设置别名超时！responseCode="+code);
							if (HttpManager.isConnected(getApplicationContext())) {
								mHandler.sendMessageDelayed(mHandler.obtainMessage(), 1000 * 10);
							} else {
								Log.e("LoginActivity-Jpush", "No network");
							}
							break;

						default:
							Log.e("LoginActivity-Jpush", "设置别名失败！responseCode="+code);
					}
				}
			});
		}
	};

	// 保存个人信息
	private void saveInfo(UserInfo userInfo) {
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("userId", userInfo.getUserId());
		editor.putString("deviceNum", userInfo.getDeviceNum());
		editor.putString("nickName", userInfo.getUserName());
		editor.putString("sex", userInfo.getSex());
		editor.putString("area", userInfo.getArea());
		editor.putString("phoneNumber", userInfo.getPhone());
		editor.putString("email", userInfo.getEmail());
		editor.putString("imgUrl", userInfo.getImgUrl());
		editor.putString("sign", userInfo.getSign());
		editor.putString("familyId", userInfo.getFamilyId());
		editor.putString("loginType", userInfo.getLoginType());
		editor.commit();

	}
	// 请求结果
	private void initResult2(String error) {
		if (PF2 == FailServer2) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时！", Toast.LENGTH_SHORT).show();
		} else if (PF2 == FailLOGIN) {
			if (error.equals("")) {
				Toast.makeText(this, "登录失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
			}
		} else if (PF2 == OK) {
			setAlias();// 设置别名
			saveUserName();
			if(isFamilyMember != null&&isFamilyMember.equals("1")){
				Intent intent = new Intent(RegisterActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}else if(isFamilyMember != null&&isFamilyMember.equals("0")){
				Intent intent = new Intent(RegisterActivity.this,
						RegisterActivity2.class);
				intent.putExtra("userId", userId);
				startActivity(intent);
				/*edtUsername.setText("");
				edtPassword.setText("");*/
				SharedPreferences prefs = getSharedPreferences("measureInfo",
						Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("memberId", "");
				editor.commit();
				finish();
			}

		}
	}
	// 保存用户名
	private void saveUserName() {
		SharedPreferences prefs = getSharedPreferences("LoginUserName",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("userName", edtPhone.getText().toString().trim());
		editor.commit();
	}
}

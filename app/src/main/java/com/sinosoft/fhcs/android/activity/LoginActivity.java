package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:登陆页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.sinosoft.fhcs.android.entity.UserInfo;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.sinosoft.fhcs.android.util.SPUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity implements OnClickListener {
	private Button btnLogin;// 登录
	private EditText edtUsername, edtPassword;// 用户名，密码
	private Button btnRegister;// 注册
	// 请求数据
	private ProgressDialog progressDialog;// 进度条
	private LoginRequest loginRequest;// 网络请求
	private static final int OK = 1001;// 登录成功
	private static final int FailServer = 1002;// 连接超时
	private static final int FailLOGIN = 1003;// 登录失败
	private int PF = 1000;
	private String alias = "";//别名设置
	private Set<String> tagSet = new LinkedHashSet<String>();//tag设置
	//找回密码
	private TextView tvFindPass;
	private UMShareAPI mShareAPI = null;
	private SHARE_MEDIA platform = null;
	private String isFamilyMember = null;
	private String userId = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_login);
		ExitApplicaton.getInstance().addActivity(this);
		init();
		mShareAPI = UMShareAPI.get( this );
	}

	private void init() {
		initButton();
		initEditText();

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("登陆页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("登陆页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void initEditText() {
		edtUsername = (EditText) findViewById(R.id.login_edt_email);
		edtPassword = (EditText) findViewById(R.id.login_edt_pass);

		// 从首选项获取信息
		SharedPreferences prefs = getSharedPreferences("LoginUserName",
				Context.MODE_PRIVATE);
		String userName = prefs.getString("userName", "");
		edtUsername.setText(userName);
		// edtUsername.setText("9873199@qq.com");//13811111118
		// edtPassword.setText("zxczxc");
	}

	private void initButton() {
		btnLogin = (Button) findViewById(R.id.login_btnLogin);
		btnRegister = (Button) findViewById(R.id.login_btnRegister);
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		tvFindPass=(TextView) findViewById(R.id.login_btnFindPass);
		tvFindPass.setOnClickListener(this);
		findViewById(R.id.btn_qq_login).setOnClickListener(this);
		findViewById(R.id.btn_weixin_login).setOnClickListener(this);
	}

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.login_btnLogin:
				// 登录
				if (HttpManager.isNetworkAvailable(this)) {
					String strUserName = edtUsername.getText().toString();
					String strPass = edtPassword.getText().toString();
					if (strUserName.trim().equals("") || strPass.trim().equals("")) {
						Toast.makeText(this, "用户名、密码均不能为空！", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					if (!Constant.isEmailOrPhone(strUserName)) {
						Toast.makeText(this, "格式错误，用户名只能为邮箱和手机号！",
								Toast.LENGTH_SHORT).show();
						return;
					}
					Log.e("Registration ID", "Registration ID="+JPushInterface.getRegistrationID(this));// Registration ID
					// 所有验证均成功
					loginRequest = new LoginRequest();
					loginRequest
							.execute(HttpManager.urlThridLogin(strUserName, strPass,"","","","",JPushInterface.getRegistrationID(this)));
/*				loginRequest
				.execute(HttpManager.urlLogin(strUserName, strPass,JPushInterface.getRegistrationID(this)));
*/
				} else {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.login_btnRegister:
				// 注册账号
				Intent intent2 = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent2);
				break;
			case R.id.login_btnFindPass:
				// 找回密码
				Intent intent3 = new Intent(LoginActivity.this,
						FindPassOneActivity.class);
				startActivity(intent3);
				break;
			case R.id.btn_qq_login:
				platform = SHARE_MEDIA.QQ;
				mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
				break;
			case R.id.btn_weixin_login:
				platform = SHARE_MEDIA.WEIXIN;
				mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
				break;
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mShareAPI.onActivityResult(requestCode, resultCode, data);
	}

	String uid = null;
	String access_token = null ;
	/**
	 * 第三方登录：1、进行授权；2、获取用户信息；3、调用接口请求服务器
	 */
	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> authDataResult) {
			Toast.makeText(getApplicationContext(), "认证授权成功", Toast.LENGTH_SHORT).show();
			Log.d("user info","auth info:"+authDataResult.toString());
			/*********************begin   获取认证的必要参数：uid和accessToken*************************/

			/*********************end     获取认证的必要参数：uid和accessToken*************************/
			if(platform == SHARE_MEDIA.QQ){
				uid = authDataResult.get("uid");
				access_token = authDataResult.get("access_token");
			}else if(platform == SHARE_MEDIA.WEIXIN){
				uid = authDataResult.get("unionid");
				access_token = authDataResult.get("access_token");
			}
			/*********************begin   获取用户信息*************************/
			mShareAPI.getPlatformInfo(LoginActivity.this, platform, new UMAuthListener() {
				@Override
				public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> userInfoDataResult) {
					Log.d("user info","user info:"+userInfoDataResult.toString());
					if (userInfoDataResult!=null){
						Log.d("auth callbacl","getting data");
						// Toast.makeText(getApplicationContext(),"用户信息:"+userInfoDataResult.toString(), Toast.LENGTH_SHORT).show();
						//注册极光推送，并获取注册id
						Log.e("Registration ID", "Registration ID="+JPushInterface.getRegistrationID(LoginActivity.this));// Registration ID
						loginRequest = new LoginRequest();//异步联网请求
						/*************************根据平台的不同获取参数************************************/
						Map<String, String> thirdUserInfoDataMapWraper = CommonUtil.getThirdLoginParam(platform,userInfoDataResult);
						thirdUserInfoDataMapWraper.put("uid", uid);
						thirdUserInfoDataMapWraper.put("accessToke", access_token);
						/******************将第三方登录的信息保存到sp中，在注销登录的时候删除************************/
						SPUtil.getInstance(LoginActivity.this.getApplicationContext()).putObj("thirdLogin", thirdUserInfoDataMapWraper);
						Log.d("夏远东", "微信头像=="+thirdUserInfoDataMapWraper.get("headIconUrl"));
						loginRequest.execute(HttpManager.urlThridLogin("","",thirdUserInfoDataMapWraper.get("platformName"), uid, thirdUserInfoDataMapWraper.get("nickName"), thirdUserInfoDataMapWraper.get("headIconUrl"), JPushInterface.getRegistrationID(LoginActivity.this)));
					}
				}

				@Override
				public void onError(SHARE_MEDIA platform, int action, Throwable t) {
					Toast.makeText( getApplicationContext(), "登录失败：用户信息获取失败", Toast.LENGTH_SHORT).show();

					Log.e("Registration ID", "Registration ID="+JPushInterface.getRegistrationID(LoginActivity.this));// Registration ID
					loginRequest = new LoginRequest();//异步联网请求
					/*************************根据平台的不同获取参数************************************/
					Map<String, String> thirdUserInfoDataMapWraper = CommonUtil.getThirdLoginParam(platform,new HashMap<String, String>());;
					thirdUserInfoDataMapWraper.put("uid", uid);
					thirdUserInfoDataMapWraper.put("accessToke", access_token);
					/******************将第三方登录的信息保存到sp中，在注销登录的时候删除************************/
					SPUtil.getInstance(LoginActivity.this.getApplicationContext()).putObj("thirdLogin", thirdUserInfoDataMapWraper);
					loginRequest.execute(HttpManager.urlThridLogin("","",thirdUserInfoDataMapWraper.get("platformName"), uid, "", "", JPushInterface.getRegistrationID(LoginActivity.this)));

				}

				@Override
				public void onCancel(SHARE_MEDIA platform, int action) {
					Toast.makeText( getApplicationContext(), "登录失败：用户信息取消", Toast.LENGTH_SHORT).show();
				}
			});
		}
		/*********************end   获取用户信息*************************/
		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText( getApplicationContext(), "第三方登录失败：授权失败", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText( getApplicationContext(), "第三方登录失败：授权取消", Toast.LENGTH_SHORT).show();
		}
	};
	// 请求结果
	private void initResult(String error) {
		if (PF == FailServer) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时！", Toast.LENGTH_SHORT).show();
		} else if (PF == FailLOGIN) {
			if (error.equals("")) {
				Toast.makeText(this, "登录失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
			}
		} else if (PF == OK) {
			setAlias();// 设置别名
			saveUserName();
			if(isFamilyMember != null&&isFamilyMember.equals("1")){
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
			}else if(isFamilyMember != null&&isFamilyMember.equals("0")){
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity2.class);
				intent.putExtra("userId", userId);
				startActivity(intent);
				edtUsername.setText("");
				edtPassword.setText("");
				SharedPreferences prefs = getSharedPreferences("measureInfo",
						Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("memberId", "");
				editor.commit();
				finish();
			}

		}

	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
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

			return true;// 不知道返回true或是false有什么区别??
		}
		return super.dispatchKeyEvent(event);
	}
	class MyAction implements Runnable {

		public void run() {
			setAlias();
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
	// 保存用户名
	private void saveUserName() {
		SharedPreferences prefs = getSharedPreferences("LoginUserName",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("userName", edtUsername.getText().toString().trim());
		editor.commit();
	}

	// 登录请求
	private class LoginRequest extends AsyncTask<Object, Void, String> {
		private String urlLogin;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(LoginActivity.this);
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
				PF = FailServer;
				initResult("");
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
							//Toast.makeText(LoginActivity.this, "loginType===="+loginType, Toast.LENGTH_SHORT).show();
						}
						if(!jo2.isNull("isFamilyMember")){
							isFamilyMember = jo2.optString("isFamilyMember");
						}
						JSONObject joUser = new JSONObject(user);
						String userId = joUser.optString("id");
						LoginActivity.this.userId = userId;
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
						PF = OK;
						UserInfo userInfo = new UserInfo(userId, deviceNum,
								nickName, sex, area, phoneNumber, email, imgUrl,sign,familyId,loginType);
						if(userInfo != null){
							ExitApplicaton.userInfo = userInfo;
						}
						saveInfo(userInfo);
						initResult("");
					} else {
						String errormsg = "";
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
						PF = FailLOGIN;
						initResult(errormsg.trim());
					}

				} catch (JSONException e) {
					PF = FailLOGIN;
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

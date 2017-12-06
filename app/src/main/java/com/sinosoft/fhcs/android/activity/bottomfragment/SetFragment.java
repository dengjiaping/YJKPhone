package com.sinosoft.fhcs.android.activity.bottomfragment;

/**
 * @CopyRight: SinoSoft.
 * @Description: 设置页
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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.AboutActivity;
import com.sinosoft.fhcs.android.activity.HistoryActivity;
import com.sinosoft.fhcs.android.activity.LinkedActivity;
import com.sinosoft.fhcs.android.activity.NotifySettingActivity;
import com.sinosoft.fhcs.android.activity.PersonalInfoActivity;
import com.sinosoft.fhcs.android.activity.PersonalInfothirdPartyActivity;
import com.sinosoft.fhcs.android.activity.SparePartsActivity;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.UserInfo;
import com.sinosoft.fhcs.android.service.VersionService;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class SetFragment extends Fragment implements OnClickListener {
	private View mainView;
	private TextView tvTitle;
	private RelativeLayout btnMember, btnUpdate, btnSpare, btnAbout,
			btnNotify,btnHistory;// 个人信息，更新，绑定机顶盒，关于,通知设定,健康档案
	private CircleImageView ivImg;// 头像
	private TextView tvName;// 昵称
	// 网络请求
	private static final int OK = 1001;// 成功
	private static final int Fail = 1002;// 失败
	private static final int ChaoShi = 1003;// 超时
	private static final int FailInfo = 1004;// 个人信息失败
	private static final int OKInfo = 1005;// 个人信息成功
	private static final int FailNotify = 1006;// 通知失败
	private static final int OKNotify = 1007;// 通知成功
	private int PF = 1000;
	private ProgressDialog progressDialog;// 进度条
	private ProgressDialog pDInfo;// 进度条
	private ProgressDialog pDNotify;// 进度条
	private String userId = "";
	private String familyId="";
	private DisplayImageOptions options;
	private String loginType = "";
	private RelativeLayout btnIsBind;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initOptions();
	}
	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.avatar)
				.showImageForEmptyUri(R.drawable.avatar)
				.showImageOnFail(R.drawable.avatar)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mainView = inflater
				.inflate(R.layout.fragment_setting, container, false);
		init();
		return mainView;

	}

	private void init() {
		tvTitle = (TextView) mainView.findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_set));
		ivImg = (CircleImageView) mainView.findViewById(R.id.set_icon);
		tvName = (TextView) mainView.findViewById(R.id.set_tv_name);
		btnMember = (RelativeLayout) mainView.findViewById(R.id.set_member);
		btnMember.setOnClickListener(this);
		btnUpdate = (RelativeLayout) mainView.findViewById(R.id.set_update);
		btnUpdate.setOnClickListener(this);
		btnSpare = (RelativeLayout) mainView.findViewById(R.id.set_spare);
		btnSpare.setOnClickListener(this);
		btnAbout = (RelativeLayout) mainView.findViewById(R.id.set_about);
		btnAbout.setOnClickListener(this);
		btnNotify = (RelativeLayout) mainView.findViewById(R.id.set_notify);
		btnNotify.setOnClickListener(this);
		btnIsBind = (RelativeLayout) mainView.findViewById(R.id.set_isbind_device);
		btnIsBind.setOnClickListener(this);
		btnHistory = (RelativeLayout) mainView.findViewById(R.id.set_history);
		btnHistory.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		// 从首选项获取信息
		SharedPreferences prefs = getActivity().getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		familyId=prefs.getString("familyId", "");
		loginType =prefs.getString("loginType", "");
		if (HttpManager.isNetworkAvailable(getActivity())) {
			InfoRequest re = new InfoRequest();
			re.execute(HttpManager.urlPersonalInfo(userId));
		} else {
//			Constant.showDialog(getActivity(), "您的网络没连接好，请检查后重试！");
			Toast.makeText(getActivity(), "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			String nickName = prefs.getString("nickName", "");
			if (nickName.equals("")) {
				tvName.setText("");
			} else {
				tvName.setText(nickName);
			}
		}
		super.onResume();
		MobclickAgent.onPageStart("设置页"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("设置页");
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.set_member:
				// 个人中心
				if(loginType.equals("1")){
					intent = new Intent(getActivity(), PersonalInfothirdPartyActivity.class);
				}else {
					intent = new Intent(getActivity(), PersonalInfoActivity.class);
				}
				//Intent intent1 = new Intent(getActivity(), PersonalInfoActivity.class);
				startActivity(intent);
				break;
			case R.id.set_history:
				// 健康档案
				intent = new Intent(getActivity(), HistoryActivity.class);
				startActivity(intent);
				break;
			case R.id.set_update:
				// 更新
				if (HttpManager.isNetworkAvailable(getActivity())) {
					CheckVersionRequest request = new CheckVersionRequest();
					request.execute(HttpManager.urlCheckApk("700002"));
				} else {
//				Constant.showDialog(getActivity(), "您的网络没连接好，请检查后重试！");
					Toast.makeText(getActivity(), "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.set_spare:
				// 配件管理
				intent = new Intent(getActivity(), SparePartsActivity.class);
				startActivity(intent);
				break;
			case R.id.set_about:
				// 关于
				intent = new Intent(getActivity(), AboutActivity.class);
				startActivity(intent);
				break;
			case R.id.set_notify:
				// 通知设定
				NotifyRequest re = new NotifyRequest();
				re.execute(HttpManager.urlNotifyState(userId));
				break;
			case R.id.set_isbind_device://已绑定设备管理
				// 已绑定设备管理
				intent = new Intent(getActivity(), LinkedActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}

	}

	// 通知状态结果
	private void initResultNotify() {
		if (PF == ChaoShi) {
//			Constant.showDialog(getActivity(), "服务器响应超时!");
			Toast.makeText(getActivity(), "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailNotify) {
			Toast.makeText(getActivity(), "获取通知状态失败，请稍后重试！", Toast.LENGTH_SHORT)
					.show();
		} else if (PF == OKNotify) {
			Intent intent6 = new Intent(getActivity(),
					NotifySettingActivity.class);
			startActivity(intent6);
		}

	}

	// 保存服药提醒信息
	private void saveRemind(boolean isRemind, boolean isInformation) {
		SharedPreferences prefs = getActivity().getSharedPreferences("isNotify",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("isRemind", isRemind);
		editor.putBoolean("isInformation", isInformation);
		editor.commit();

	}

	// 通知请求
	private class NotifyRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			pDNotify = new ProgressDialog(getActivity());
			Constant.showProgressDialog(pDNotify);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("NotifyUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initResultNotify();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						String data = jo.optString("data");
						JSONObject JData = new JSONObject(data);
						boolean informationPush = true;
						boolean medicinePush = true;
						if (!JData.isNull("informationPush")) {
							informationPush = JData
									.optBoolean("informationPush");
						}
						if (!JData.isNull("medicinePush")) {
							medicinePush = JData.optBoolean("medicinePush");
						}
						saveRemind(medicinePush, informationPush);
						PF = OKNotify;
						initResultNotify();
					} else {
						PF = FailNotify;
						initResultNotify();
					}
				} catch (JSONException e) {
					Log.e("SettingActivity", "解析失败！");
					PF = FailNotify;
					initResultNotify();
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

	// 版本信息请求
	private class CheckVersionRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(getActivity());
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("checkUrl", url + "");
			result = HttpManager.getStringContent(url);
			Log.e("checkResult", result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initResultVersion();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						String data = jo.optString("data");
						JSONObject JData = new JSONObject(data);
						if (!JData.isNull("version")) {
							JSONObject jo2 = new JSONObject(
									JData.optString("version"));
							String apkName = "";
							int versionCode = 0;
							String downloadURL = "";
							if (!jo2.isNull("apkName")
									&& !jo2.isNull("versionCode")
									&& !jo2.isNull("downloadURL")) {
								apkName = jo2.optString("apkName");
								versionCode = jo2.getInt("versionCode");
								downloadURL = jo2.optString("downloadURL");
								saveInfo(versionCode, HttpManager.m_imageUrl
										+ downloadURL + apkName);
								PF = OK;
								initResultVersion();
							} else {
								PF = Fail;
								initResultVersion();
							}

						} else {
							PF = Fail;
							initResultVersion();

						}
					} else {
						PF = Fail;
						initResultVersion();
					}
				} catch (JSONException e) {
					PF = Fail;
					initResultVersion();
					Log.e("MoreActivity", "解析失败！");
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

	// 版本更新
	private void showDialog() {
		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
				.setCancelable(false)
				.setIcon(R.drawable.banbenicon)
				.setTitle("版本更新")
				.setMessage("发现新版本，是否更新？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						FRToast.showToast(getActivity().getApplicationContext(), getString(R.string.loading));
						Intent intent = new Intent(getActivity(),
								VersionService.class);
						getActivity().startService(intent);
						dialog.cancel();
					}
				})
				.setNegativeButton("稍后再说",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.cancel();

							}
						}).show();

	}

	// 版本信息结果
	private void initResultVersion() {
		if (PF == ChaoShi) {
//			Constant.showDialog(getActivity(), "服务器响应超时!");
			Toast.makeText(getActivity(), "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			Toast.makeText(getActivity(), "您当前已是最新版本！", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			PackageManager packageManager = getActivity().getPackageManager();
			PackageInfo packInfo;
			try {
				packInfo = packageManager.getPackageInfo(
						getActivity().getPackageName(), 0);
				int code = packInfo.versionCode;
				// 从首选项获取角色
				SharedPreferences prefs = getActivity().getSharedPreferences(
						"NewVersion", Context.MODE_PRIVATE);
				int versionCode = prefs.getInt("versionCode", 0);
				if (versionCode > code) {
					showDialog();
				} else {
					Toast.makeText(getActivity(), "您当前已是最新版本！", Toast.LENGTH_SHORT)
							.show();
				}
			} catch (NameNotFoundException e) {
				Log.e("获取版本号", "获取版本号失败！");
				e.printStackTrace();
			}
		}
	}

	// 保存信息
	private void saveInfo(int versionCode, String url) {
		if(getActivity() !=null){
			SharedPreferences prefs = getActivity().getSharedPreferences("NewVersion",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("versionCode", versionCode);
			editor.putString("url", url);
			editor.commit();
		}
	}

	// 请求结果
	private void infoResult(String error) {
		if(getActivity() ==null){
			System.out.println("SetFragment隐藏了");
		}else{
			if (PF == ChaoShi) {
				Toast.makeText(getActivity(), "获取信息超时!", Toast.LENGTH_SHORT).show();
			} else if (PF == FailInfo) {
				if (error.equals("")) {
					Toast.makeText(getActivity(), "获取信息失败!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
				}
			} else if (PF == OKInfo) {
				// Toast.makeText(this, "获取信息成功!", Toast.LENGTH_SHORT).show();

			}
			// 从首选项获取信息
			SharedPreferences prefs = getActivity().getSharedPreferences("UserInfo",
					Context.MODE_PRIVATE);
			String nickName = prefs.getString("nickName", "");
			String imgUrl = prefs.getString("imgUrl", "");
			if (nickName.equals("")) {
				tvName.setText("");
			} else {
				tvName.setText(nickName);
			}
			if (imgUrl.equals("")) {
				ivImg.setImageResource(R.drawable.avatar);
			} else {
				// 加载图片
				ImageLoader.getInstance().displayImage(HttpManager.m_imageUrl + imgUrl, ivImg, options);
			}
		}

	}

	// 获取个人信息请求
	private class InfoRequest extends AsyncTask<Object, Void, String> {
		private String urlInfo;

		@Override
		protected void onPreExecute() {
			pDInfo = new ProgressDialog(getActivity());
			Constant.showProgressDialog(pDInfo);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			urlInfo = (String) params[0];
			Log.e("InfoUrl", urlInfo + "");
			result = HttpManager.getStringContent(urlInfo);
			// Log.e("InfoResult", result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				infoResult("");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						String entity = jo.optString("data");
						JSONObject joUser = new JSONObject(entity);
						String deviceNum = "";
						if (!joUser.isNull("stbSerialNo")) {
							deviceNum = joUser.optString("stbSerialNo");
						}
						String nikeName = "";
						if (!joUser.isNull("nikeName")) {
							nikeName = joUser.optString("nikeName");
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
						if (!joUser.isNull("sign")) {
							sign = joUser.optString("sign");
						}
						PF = OKInfo;
						UserInfo userInfo = new UserInfo(userId, deviceNum,
								nikeName, sex, area, phoneNumber, email, imgUrl,sign,familyId,loginType);
						saveInfo(userInfo);
						infoResult("");
					} else {
						String errormsg = "";
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
						PF = FailInfo;
						infoResult(errormsg.trim());
					}

				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = FailInfo;
					infoResult("");
					e.printStackTrace();
				}
			}
			Constant.exitProgressDialog(pDInfo);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}

	// 保存个人信息
	private void saveInfo(UserInfo userInfo) {
		if(getActivity()!= null){
			SharedPreferences prefs = getActivity().getSharedPreferences("UserInfo",
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
			editor.putString("loginType", userInfo.getLoginType());
			editor.commit();
		}


	}
}

package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:个人信息页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.entity.UserInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

@SuppressLint("HandlerLeak")
public class PersonalInfothirdPartyActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private Button btnSave;// 保存
	private ImageView img;// 头像
	private Button btnImg;// 更换头像
	private TextView tvUserId;// 用户邮箱
	private EditText tvName;// 用户名
	private TextView tvCode;// 机顶盒编码
	private RelativeLayout btnArea;// 地区
	private TextView tvArea;
	private RelativeLayout btnPhone;// 手机
	private ImageView ivPhone;// 箭头
	private RelativeLayout btnJiDingHe;// 机顶盒
	private TextView tvPhone;
	private RelativeLayout btnSign;// 签名
	//private RelativeLayout btnModify;// 修改密码
	private TextView tvSign;
	private Button btnExit;// 退出
	private String userId = "";
	private String sex = "";
	private String email = "";
	private String deviceNum = "";
	private String nickName = "";
	private String familyId = "";
	private String area = "";
	private String phoneNumber = "";
	private String imgUrl;
	private String sign = "";
	private String signOld = "";
	private String loginType="";

	// 相册
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private String picPath = "";
	// 请求数据
	private ProgressDialog progressDialog;// 进度条
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private int PF = 1000;
	private DisplayImageOptions options;
	private Set<String> tagSet = new LinkedHashSet<String>();// tag设置

	private final String reg = "^[\u4e00-\u9fa5\u0020-\u007E\uFE30-\uFFA0。、……“”‘’《》——￥~]*$";
	private Pattern pattern = Pattern.compile(reg);
	//输入表情前的光标位置
	private int cursorPos;
	//是否重置了EditText的内容
	private boolean resetText;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_personalinfo_third_party);
		ExitApplicaton.getInstance().addActivity(this);
		initOptions();
		Constant.province = "";
		Constant.city = "";
		Constant.district = "";
		Constant.provinceId = "";
		Constant.cityId = "";
		Constant.districtId = "";
		// 从首选项获取信息
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		email = prefs.getString("email", "");
		nickName = prefs.getString("nickName", "");
		area = prefs.getString("area", "");
		sign = prefs.getString("sign", "");
		signOld = prefs.getString("sign", "");
		sex = prefs.getString("sex", "");
		imgUrl = prefs.getString("imgUrl", "");
		init();
	}

	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.avatar)
				.showImageForEmptyUri(R.drawable.avatar)
				.showImageOnFail(R.drawable.avatar).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("个人信息页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("个人信息页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStart() {
		String strArea = Constant.province + "-" + Constant.city + "-"
				+ Constant.district;
		if (Constant.province.equals("")) {
			Constant.provinceId = "";
		}
		if (Constant.city.equals("")) {
			Constant.cityId = "";
		}
		if (Constant.district.equals("")) {
			Constant.districtId = "";
		}
		if (strArea.trim().equals("--")) {
			tvArea.setText(area);
		} else {
			tvArea.setText(strArea);
		}
		// 从首选项获取信息
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		phoneNumber = prefs.getString("phoneNumber", "");
		deviceNum = prefs.getString("deviceNum", "");
		familyId=prefs.getString("familyId", "");
		loginType=prefs.getString("loginType", "");
		if (phoneNumber.equals("")) {
			tvPhone.setText("未绑定");
			ivPhone.setVisibility(View.VISIBLE);
		} else {
			tvPhone.setText(phoneNumber);
			ivPhone.setVisibility(View.GONE);
		}
		if (deviceNum.equals("")) {
			tvCode.setText("未绑定");
		} else {
			tvCode.setText(deviceNum);
		}
		super.onStart();
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_personal));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		btnSave = (Button) findViewById(R.id.titlebar_btn_save);
		btnSave.setVisibility(View.VISIBLE);
		btnSave.setOnClickListener(this);
		// 头像
		btnImg = (Button) findViewById(R.id.personal_btn_img);
		btnImg.setOnClickListener(this);
		img = (ImageView) findViewById(R.id.personal_icon);
		if (imgUrl.equals("")) {
			img.setImageResource(R.drawable.avatar);
		} else {
			// 加载图片
			ImageLoader.getInstance().displayImage(
					HttpManager.m_imageUrl + imgUrl, img, options);
		}
		// 用户信息
		tvName = (EditText) findViewById(R.id.personal_tv_name);
		tvName.setText(nickName);
		tvUserId = (TextView) findViewById(R.id.personal_tv_userid);
		if (email.equals("")) {
			tvUserId.setText("暂无");
		} else {
			tvUserId.setText(email);
		}
		tvCode = (TextView) findViewById(R.id.personal_tv_code);
		btnArea = (RelativeLayout) findViewById(R.id.personal_btn_area);
		btnArea.setOnClickListener(this);
		tvArea = (TextView) findViewById(R.id.personal_tv_area);
		tvArea.setText(area);
		btnPhone = (RelativeLayout) findViewById(R.id.personal_btn_phone);
		btnPhone.setOnClickListener(this);
		ivPhone = (ImageView) findViewById(R.id.personal_iv_phone);
		btnJiDingHe = (RelativeLayout) findViewById(R.id.personal_btn_jidinghe);
		btnJiDingHe.setOnClickListener(this);
		tvPhone = (TextView) findViewById(R.id.personal_tv_phone);
		btnSign = (RelativeLayout) findViewById(R.id.personal_btn_sign);
		btnSign.setOnClickListener(this);
		/*btnModify = (RelativeLayout) findViewById(R.id.personal_btn_modify);
		btnModify.setOnClickListener(this);*/
		tvSign = (TextView) findViewById(R.id.personal_tv_sign);
		tvSign.setText(sign);
		btnExit = (Button) findViewById(R.id.personal_btn_exit);
		btnExit.setOnClickListener(this);
		tvName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString().contains(" ")) {
					String[] str = s.toString().split(" ");
					String str1 = "";
					for (int i = 0; i < str.length; i++) {
						str1 += str[i];
					}
					tvName.setText(str1);

					tvName.setSelection(start);

				}
				if (!resetText) {
//	                CharSequence input = s.subSequence(cursorPos, cursorPos + count);
					//正则匹配是否是表情符号
					Matcher matcher = pattern.matcher(s.toString());
					if (!matcher.matches()) {
						resetText = true;
						//是表情符号就将文本还原为输入表情符号之前的内容
						tvName.setText(sign);
						tvName.setSelection(cursorPos);
						tvName.invalidate();

					}
				} else {
					resetText = false;
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				if (!resetText) {
					cursorPos = tvName.getSelectionEnd();
					sign = s.toString();//这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
				}
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	// 返回
	private void goback() {
		if (tvName.getText().toString().trim().equals(nickName)
				&& tvArea.getText().toString().trim().equals(area)
				&& picPath.equals("")
				&& tvSign.getText().toString().trim().equals(signOld)) {
			finish();
		} else {
			Constant.showDialog2(this, "尚未完成修改，是否要离开本页面？");
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
			case R.id.titlebar_btn_save:
				// 保存
				if (!HttpManager.isNetworkAvailable(this)) {
					// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试!", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (tvArea.getText().toString().trim().equals("")) {
					Toast.makeText(this, "地区不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (tvName.getText().toString().trim().equals("")) {
					Toast.makeText(this, "昵称不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (tvSign.getText().toString().trim().equals("")) {
					Toast.makeText(this, "个性签名不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if(sign.length()>30){
					Toast.makeText(PersonalInfothirdPartyActivity.this, "个性签名不能超过30字！", Toast.LENGTH_SHORT).show();
					return;
				}
				CommitRequest re = new CommitRequest();
				re.execute(HttpManager.urlSaveInfo2);
				break;
			case R.id.personal_btn_exit:
				// 退出
				new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
						.setCancelable(false)
						.setTitle("温馨提示")
						.setMessage("您确定要退出吗?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int which) {
										clearAlias();// 清空别名
										// 关闭该Service
										/*Intent intent = new Intent();
										intent.setAction("com.sinosoft.fhcs.android.action.INFO_SERVICE");
										stopService(intent);*/
										// 退出
										clearInfo();
										/*ExitApplicaton.getInstance().exit();
										Intent intent2 = new Intent(
												PersonalInfothirdPartyActivity.this,
												LoginActivity.class);
										startActivity(intent2);*/
										//销毁所有activity（利用广播）
										Intent intent = new Intent();
										intent.setAction("exit_app");
										sendBroadcast(intent);
										if(MainActivity.mInstance != null){
											MainActivity.mInstance.finish();
										}
										startActivity(LoginActivity.class);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int which) {

									}
								}).show();

				break;
			case R.id.personal_btn_area:
				// 地区
				Intent intent = new Intent(this, ChoiceCityActivity.class);
				intent.putExtra("flag", "personal");
				startActivity(intent);
				break;
			case R.id.personal_btn_phone:
				// 手机
				Intent intent2 = null;
				if (phoneNumber.equals("")) {
					intent2 = new Intent(this, PhonethirdPartyActivity.class);
					startActivity(intent2);
				} else {
					// Toast.makeText(this, "手机号不可以重复绑定！",
					// Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.personal_btn_jidinghe:
				// 机顶盒
				Intent intent4 = new Intent(this, JiDingHeActivity.class);
				startActivity(intent4);
				break;
			case R.id.personal_btn_img:
				// 更换头像
				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				getAlbum.setType(IMAGE_TYPE);
				startActivityForResult(getAlbum, IMAGE_CODE);
				break;
			case R.id.personal_btn_sign:
				// 签名
				final EditText inputServer = new EditText(this);
				inputServer.setHint("请输入30字以内的个性签名");
				inputServer.setTextColor(getResources().getColor(
						R.color.text_set_gray));
				inputServer.setTextSize(15);
				inputServer.setBackgroundResource(R.drawable.reg_edt_bg1);
				inputServer.setPadding(10, 10, 10, 10);
				if (!sign.equals("")) {
					inputServer.setText(sign);
				}
				inputServer.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start, int before,
											  int count) {
						if (s.toString().contains(" ")) {
							String[] str = s.toString().split(" ");
							String str1 = "";
							for (int i = 0; i < str.length; i++) {
								str1 += str[i];
							}
							inputServer.setText(str1);

							inputServer.setSelection(start);

						}
					/*if(containsEmoji(s.toString())){

						Toast.makeText(PersonalInfoActivity.this, "个性签名不含有表情符号！", Toast.LENGTH_SHORT).show();
						return;
					}*/
						if (!resetText) {
//		                CharSequence input = s.subSequence(cursorPos, cursorPos + count);
							//正则匹配是否是表情符号
							Matcher matcher = pattern.matcher(s.toString());
							if (!matcher.matches()) {
								resetText = true;
								//是表情符号就将文本还原为输入表情符号之前的内容
								inputServer.setText(sign);
								inputServer.setSelection(cursorPos);
								inputServer.invalidate();

							}
						} else {
							resetText = false;
						}
					}
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
												  int after) {
						if (!resetText) {
							cursorPos = inputServer.getSelectionEnd();
							sign = s.toString();//这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
						}
					}
					@Override
					public void afterTextChanged(Editable s) {
					}
				});
				inputServer.setHighlightColor(getResources().getColor(
						R.color.text_set_gray));
				AlertDialog.Builder builder = new AlertDialog.Builder(this,
						AlertDialog.THEME_HOLO_LIGHT);
				builder.setTitle("个性签名").setView(inputServer)
						.setNegativeButton("取消", null);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								sign = inputServer.getText().toString();
								if(sign.length()>30){
									Toast.makeText(PersonalInfothirdPartyActivity.this, "个性签名不能超过30字！", Toast.LENGTH_SHORT).show();
									return;
								}
								tvSign.setText(sign);
							}
						});
				builder.show();
				break;
		/*case R.id.personal_btn_modify:
			// 修改密码
			Intent intentModify = new Intent(this, ModifyPassActivity.class);
			startActivity(intentModify);
			break;*/
			default:
				break;
		}
	}

	// 清空别名
	private void clearAlias() {
		mHandler.sendMessage(mHandler.obtainMessage());
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			JPushInterface.setAliasAndTags(getApplicationContext(), "", tagSet,
					new TagAliasCallback() {

						@Override
						public void gotResult(int code, String alias,
											  Set<String> tags) {
							// TODO Auto-generated method stub
							switch (code) {
								case 0:
									ExitRequest r = new ExitRequest();
									r.execute(HttpManager.urlExit(userId));
									Log.e("LoginActivity-Jpush",
											"设置别名成功！responseCode=" + code);
									break;

								case 6002:
									Log.e("LoginActivity-Jpush",
											"设置别名超时！responseCode=" + code);
									if (HttpManager
											.isConnected(getApplicationContext())) {
										mHandler.sendMessageDelayed(
												mHandler.obtainMessage(), 1000 * 10);
									} else {
										Log.e("LoginActivity-Jpush", "No network");
									}
									break;

								default:
									Log.e("LoginActivity-Jpush",
											"设置别名失败！responseCode=" + code);
							}
						}
					});
		}
	};

	// 清空个人信息
	private void clearInfo() {
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

	// 调用相机相册
	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case IMAGE_CODE:
					// 以下是相册
					Bitmap bm = null;
					// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
					ContentResolver resolver = getContentResolver();
					// 此处的用于判断接收的Activity是不是你想要的那个
					try {
						Uri originalUri = data.getData(); // 获得图片的uri
						Log.e("originalUri", originalUri + "");
						bm = MediaStore.Images.Media.getBitmap(resolver,
								originalUri); // 显示得到bitmap图片
						int width2 = bm.getWidth();
						int height2 = bm.getHeight();
						int newWidth2 = this.getWindowManager().getDefaultDisplay()
								.getWidth() / 2;
						int newHeight2 = this.getWindowManager()
								.getDefaultDisplay().getHeight() / 4;
						Matrix matrix2 = new Matrix();
						// 计算缩放率，新尺寸除原始尺寸
						float scaleWidth2 = ((float) newWidth2) / width2;
						float scaleHeight2 = ((float) newHeight2) / height2;
						// 缩放图片动作
						matrix2.postScale(scaleWidth2, scaleHeight2);
						Bitmap bitmap_picture = Bitmap.createBitmap(bm, 0, 0,
								width2, height2, matrix2, false);
						// 这里开始是第二部分，获取图片的路径：
						String[] proj = { MediaStore.Images.Media.DATA };
						// android多媒体数据库的封装接口
						Cursor cursor = managedQuery(originalUri, proj, null, null,
								null);
						// 获得用户选择的图片的索引值
						int column_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						// 将光标移至开头 ，防止下标越界
						cursor.moveToFirst();
						// 显示图片
						img.setImageBitmap(bitmap_picture);
						picPath = cursor.getString(column_index);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		}
	}

	// 网络请求
	private class CommitRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(PersonalInfothirdPartyActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			SharedPreferences prefs = getSharedPreferences("UserInfo",
					Context.MODE_PRIVATE);
			phoneNumber = prefs.getString("phoneNumber", "");
			String result = "";
			url = (String) params[0];
			Log.e("commitUrl", url + "");
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", userId);
			map.put("phoneNumber", phoneNumber);
			System.out.println("提交的手机号====="+phoneNumber);
			if (!Constant.provinceId.equals("")) {
				map.put("provinceId", Constant.provinceId);
			}
			if (!Constant.cityId.equals("")) {
				map.put("cityId", Constant.cityId);
			}
			if (!Constant.districtId.equals("")) {
				map.put("areaId", Constant.districtId);
			}
			map.put("nickName", tvName.getText().toString().trim());
			map.put("sign", sign);
			if (!picPath.equals("")) {
				// 更换头像
				Map<String, File> files = new HashMap<String, File>();
				File file = new File(picPath);
				files.put("avatar", file);
				result = HttpManager.postImage2(url, map, files);
			} else {
				// 不更换头像
				result = HttpManager.getStringContent(HttpManager.urlSavePersonal(
						userId,  phoneNumber,Constant.provinceId,Constant.cityId,
						Constant.districtId,
						tvName.getText().toString().trim(), sign));
			}
			// Log.e("result", result + "");
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResult("");
			} else {
				System.out.println("保存完成后的个人信息=="+result);
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						String imgUrl = "";
						if (!jo.isNull("url")) {
							imgUrl = jo.optString("url");
						}
						String entity = jo.optString("data");
						JSONObject jo2 = new JSONObject(entity);

						//Toast.makeText(PersonalInfothirdPartyActivity.this, "保存之前的userid==="+userId, Toast.LENGTH_SHORT).show();
						if (!jo2.isNull("userId")) {
							userId = jo2.optString("userId");
							//	Toast.makeText(PersonalInfothirdPartyActivity.this, "保存之后的userid==="+userId, Toast.LENGTH_SHORT).show();

						}
						//Toast.makeText(PersonalInfothirdPartyActivity.this, "保存之前的familyId==="+familyId, Toast.LENGTH_SHORT).show();
						if(!jo2.isNull("familyId")){
							familyId=jo2.optString("familyId");
							//Toast.makeText(PersonalInfothirdPartyActivity.this, "保存之后的familyId==="+familyId, Toast.LENGTH_SHORT).show();
						}
						PF = OK;
						UserInfo userInfo = new UserInfo(userId, deviceNum,
								tvName.getText().toString().trim(), sex, tvArea
								.getText().toString().trim(),
								phoneNumber, email, imgUrl, sign,familyId,loginType);
						saveInfo(userInfo);
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
					PF = Fail;
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

	// 退出
	private class ExitRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("exitUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				System.out.println("超时");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						System.out.println("退出成功");
					} else {
						System.out.println("退出失败");
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					e.printStackTrace();
				}
			}
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
				Toast.makeText(this, "保存失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show();
			}
		} else if (PF == OK) {
			Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
			finish();
		}
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

}

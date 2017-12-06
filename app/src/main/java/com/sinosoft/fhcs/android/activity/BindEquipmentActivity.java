package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:绑定设备页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月7日.
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.entity.BindEquipment;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BindEquipmentActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private Button btnValidation;// 验证
	private ViewFlipper flipperEquipment, flipperFamilymember;
	private Button btnLeftE, btnRightE;// 设备左右选择
	private Button btnLeftF, btnRightF;// 家庭成员左右选择
	private int mCurrPos_E = 0;// 设备当前
	private int mCurrPos_F = 0;// 家庭成员当前
	// 网络请求
	private ProgressDialog progressDialog;// 进度条
	private List<FamilyMember> getFamilyMemberList = new ArrayList<FamilyMember>();
	private List<BindEquipment> getEquipmentList = new ArrayList<BindEquipment>();
	private int PFFamily = 1000;
	private static final int FailServerFamily = 1001;// 连接超时
	private static final int OKFamily = 1002;// 家庭成员成功
	private static final int FailFamily = 1003;// 家庭成员失败
	private static final int NoDataFamily = 1004;// 家庭成员没有数据
	private int PFEquipment = 2000;
	private static final int FailServerEquipment = 2001;// 连接超时
	private static final int OKEquipment = 2002;// 设备成功
	private static final int FailEquipment = 2003;// 设备失败
	private static final int NoDataEquipment = 2004;// 设备没有数据
	private int PFWatch = 3000;
	private static final int FailServerWatch = 3001;// 连接超时
	private static final int OKWatch = 3002;// 绑定成功
	private static final int FailWatch = 3003;// 绑定失败
	private String userId = "";// 用户id
	private boolean flagFamily = false;
	private boolean flagEquipment = false;
	private DisplayImageOptions options;
	/**
	 * 获取token
	 */
	private static final int FailServer = 3001;// 连接超时
	private static final int OKSend = 3002;// 成功
	private static final int FailSend = 3003;// 失败
	private int PF = 3000;
	private String accessToken = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_bindequipment);
		ExitApplicaton.getInstance().addActivity(this);
		// 从首选项获取机顶盒编号
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		initOptions();
		init();
		initRequest();
	}

	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.img_ing)
				.showImageForEmptyUri(R.drawable.img_xx)
				.showImageOnFail(R.drawable.img_xx).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

	}

	// 网络请求
	private void initRequest() {
		flagFamily = false;
		flagEquipment = false;
		if (!HttpManager.isNetworkAvailable(this)) {
			// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		progressDialog = new ProgressDialog(BindEquipmentActivity.this);
		Constant.showProgressDialog(progressDialog);
		GetEquipmentListRequest re = new GetEquipmentListRequest();
		re.execute(HttpManager.urlGetBindEquipment());
		GetFamilyListRequest re2 = new GetFamilyListRequest();
		re2.execute(HttpManager.urlGetFamilyMemberNobind(userId));

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("绑定设备页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("绑定设备页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	// 初始化控件
	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_bindequipment));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		btnValidation = (Button) findViewById(R.id.binde_btn_validation);
		btnValidation.setOnClickListener(this);
		flipperEquipment = (ViewFlipper) this
				.findViewById(R.id.binde_flipper_equipment);
		flipperFamilymember = (ViewFlipper) this
				.findViewById(R.id.binde_flipper_familymember);
		btnLeftE = (Button) findViewById(R.id.binde_btn_leftE);
		btnRightE = (Button) findViewById(R.id.binde_btn_rightE);
		btnLeftF = (Button) findViewById(R.id.binde_btn_leftF);
		btnRightF = (Button) findViewById(R.id.binde_btn_rightF);
		btnLeftE.setOnClickListener(this);
		btnRightE.setOnClickListener(this);
		btnLeftF.setOnClickListener(this);
		btnRightF.setOnClickListener(this);
	}

	// 设备View
	private void setViewEquipment(int curr, int next, boolean flag) {
		View v = (View) LayoutInflater.from(this).inflate(
				R.layout.equipmentflipper_item, null);
		ImageView iv = (ImageView) v.findViewById(R.id.eflipper_img);
		TextView tv = (TextView) v.findViewById(R.id.eflipper_tv);
		// iv.setScaleType(ImageView.ScaleType.FIT_XY);
		if (flag) {
			if (curr < next && next > getEquipmentList.size() - 1)
				next = 0;
			else if (curr > next && next < 0)
				next = getEquipmentList.size() - 1;
			ImageLoader.getInstance().displayImage(
					getEquipmentList.get(next).getImgUrl(), iv, options);
			tv.setText(getEquipmentList.get(next).getDeviceName().trim());
			if (flipperEquipment.getChildCount() > 1) {
				flipperEquipment.removeViewAt(0);
			}
		} else {
			iv.setBackgroundColor(Color.BLUE);
			tv.setText("暂无");
		}
		flipperEquipment.addView(v, flipperEquipment.getChildCount());
		mCurrPos_E = next;

	}

	// 家庭成员View
	private void setViewFamily(int curr, int next, boolean flag) {
		View v = (View) LayoutInflater.from(this).inflate(
				R.layout.equipmentflipper_item, null);
		ImageView iv = (ImageView) v.findViewById(R.id.eflipper_img);
		TextView tv = (TextView) v.findViewById(R.id.eflipper_tv);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		if (flag) {
			if (curr < next && next > getFamilyMemberList.size() - 1)
				next = 0;
			else if (curr > next && next < 0)
				next = getFamilyMemberList.size() - 1;
			iv.setImageResource(Constant.ImageId(
					getFamilyMemberList.get(next)
							.getFamilyRoleName(),
					getFamilyMemberList.get(next).getGender()));
			tv.setText(getFamilyMemberList.get(next)
					.getFamilyRoleName());
			if (flipperFamilymember.getChildCount() > 1) {
				flipperFamilymember.removeViewAt(0);
			}
		} else {
			iv.setBackgroundColor(Color.BLUE);
			tv.setText("暂无");
		}
		flipperFamilymember.addView(v, flipperFamilymember.getChildCount());
		mCurrPos_F = next;

	}

	// 设备左
	private void movePreviousE() {
		setViewEquipment(mCurrPos_E, mCurrPos_E - 1, true);
		flipperEquipment.setInAnimation(this, R.anim.push_left_in);
		flipperEquipment.setOutAnimation(this, R.anim.push_left_out);
		flipperEquipment.showPrevious();
	}

	// 设备右
	private void moveNextE() {
		setViewEquipment(mCurrPos_E, mCurrPos_E + 1, true);
		flipperEquipment.setInAnimation(this, R.anim.push_right_in);
		flipperEquipment.setOutAnimation(this, R.anim.push_right_out);
		flipperEquipment.showNext();
	}

	// 家庭成员左
	private void movePreviousF() {
		setViewFamily(mCurrPos_F, mCurrPos_F - 1, true);
		flipperFamilymember.setInAnimation(this, R.anim.push_left_in);
		flipperFamilymember.setOutAnimation(this, R.anim.push_left_out);
		flipperFamilymember.showPrevious();
	}

	// 家庭成员右
	private void moveNextF() {
		setViewFamily(mCurrPos_F, mCurrPos_F + 1, true);
		flipperFamilymember.setInAnimation(this, R.anim.push_right_in);
		flipperFamilymember.setOutAnimation(this, R.anim.push_right_out);
		flipperFamilymember.showNext();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				finish();
				break;
			case R.id.binde_btn_leftE:
				// 设备左
				if (getEquipmentList.size() != 0) {
					movePreviousE();
				}
				break;
			case R.id.binde_btn_rightE:
				// 设备右
				if (getEquipmentList.size() != 0) {
					moveNextE();
				}
				break;
			case R.id.binde_btn_leftF:
				// 家庭成员左
				if (getFamilyMemberList.size() != 0) {
					movePreviousF();
				}
				break;
			case R.id.binde_btn_rightF:
				// 家庭成员右
				if (getFamilyMemberList.size() != 0) {
					moveNextF();
				}
				break;
			case R.id.binde_btn_validation:
				// 验证
				if (!HttpManager.isNetworkAvailable(this)) {
					// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (getFamilyMemberList.size() != 0
						&& getFamilyMemberList.size() != 0) {
					String name = "";
					// 01=咕咚手环 ，02=Jawbone UP24 ， 03=小米手环
					if (getEquipmentList.get(mCurrPos_E).getAppCode().equals("01")) {
						name = "咕咚手环";
					} else if (getEquipmentList.get(mCurrPos_E).getAppCode()
							.equals("02")) {
						name = "Jawbone UP24";
					} else if (getEquipmentList.get(mCurrPos_E).getAppCode()
							.equals("03")) {
						name = "小米手环";
					}else if(getEquipmentList.get(mCurrPos_E).getAppCode().equals("04")){
						name = "MOTO360智能手表";
					}
					if (getEquipmentList.get(mCurrPos_E).getAppCode().equals("01")) {
						Intent intent = new Intent(this, OauthWebViewActivity.class);
						startActivityForResult(intent,
								HttpManager.CODOON_AUTHORIZE_REQUEST_CODE);
					} else if(getEquipmentList.get(mCurrPos_E).getAppCode().equals("04")){
						//手表
						BindWatchRequest re=new BindWatchRequest();
						re.execute(HttpManager.urlBindWatch(getEquipmentList.get(mCurrPos_E).getId()+"", getFamilyMemberList.get(mCurrPos_F).getId()+""));
					}else {
						Toast.makeText(this, "暂未接入", Toast.LENGTH_SHORT).show();
					}
					// Toast.makeText(
					// this,
					// "设备="
					// + getEquipmentList.get(mCurrPos_E)
					// .getDeviceName()
					// + "\n"
					// + "家庭成员="
					// + getFamilyMemberList
					// .get(mCurrPos_F).getFamilyRoleName()+"\n手环类型="+name,
					// Toast.LENGTH_SHORT).show();

				}
				break;
			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == HttpManager.CODOON_AUTHORIZE_REQUEST_CODE
				&& resultCode == RESULT_OK) {

			String code = data.getStringExtra(HttpManager.ACCESS_CODE);
			if (code != null) {
				System.out.println("ACCESS_CODE=" + code);
				if (HttpManager.isNetworkAvailable(this)) {
					SendRequest re = new SendRequest();
					re.execute(HttpManager
							.urlGetToken(
									code,
									getFamilyMemberList
											.get(mCurrPos_F).getId() + "",
									getEquipmentList.get(mCurrPos_E).getId()
											+ ""));
				} else {
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	private void showDialog() {
		new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
				.setCancelable(false).setTitle("温馨提示").setMessage("是否开始同步数据")
				.setPositiveButton("稍后", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				})
				.setNegativeButton("开始", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent t=new Intent(BindEquipmentActivity.this,BraceletSportActivity.class);
						t.putExtra("memberId", getFamilyMemberList.get(mCurrPos_F).getId()+"");
						t.putExtra("memberName", getFamilyMemberList.get(mCurrPos_F).getFamilyRoleName()+"");
						t.putExtra("deviceName", getEquipmentList.get(mCurrPos_E).getDeviceName()+"");
						t.putExtra("accessToken", accessToken+"");
						t.putExtra("appCode", getEquipmentList.get(mCurrPos_E).getAppCode()+"");
						startActivity(t);

					}
				}).show();

	}
	private void initResultWatch() {
		if (PFWatch == FailServerWatch) {
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PFWatch == OKWatch) {
			showDialog();
		} else if (PFWatch == FailWatch) {
			Toast.makeText(this, "绑定失败!", Toast.LENGTH_SHORT).show();

		}

	}
	private void initResultToken(String msg) {
		if (PF == FailServer) {
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKSend) {
			showDialog();

		} else if (PF == FailSend) {
			if (msg.equals("")) {
				Toast.makeText(this, "获取Token失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			}

		}

	}

	// 请求结果
	private void initResult() {
		mCurrPos_F = 0;
		mCurrPos_E = 0;
		if (flagFamily && flagEquipment) {
			Constant.exitProgressDialog(progressDialog);
			if (PFFamily == FailServerFamily
					|| PFEquipment == FailServerEquipment) {
				setViewFamily(mCurrPos_F, 0, false);
				setViewEquipment(mCurrPos_E, 0, false);
				// Constant.showDialog(this, "服务器响应超时!");
				Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
			} else if (PFFamily == FailFamily && PFEquipment == FailEquipment) {
				setViewFamily(mCurrPos_F, 0, false);
				setViewEquipment(mCurrPos_E, 0, false);
				Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
			} else if (PFFamily == FailFamily && PFEquipment == NoDataEquipment) {
				setViewFamily(mCurrPos_F, 0, false);
				setViewEquipment(mCurrPos_E, 0, false);
				Toast.makeText(this, "获取家庭成员数据失败，目前还没有设备数据，请等待工作人员添加!",
						Toast.LENGTH_SHORT).show();
			} else if (PFFamily == FailFamily && PFEquipment == OKEquipment) {
				setViewFamily(mCurrPos_F, 0, false);
				setViewEquipment(mCurrPos_E, 0, true);
				Toast.makeText(this, "获取家庭成员数据失败!", Toast.LENGTH_SHORT).show();
			} else if (PFEquipment == FailEquipment && PFFamily == NoDataFamily) {
				setViewFamily(mCurrPos_F, 0, false);
				setViewEquipment(mCurrPos_E, 0, false);
				Toast.makeText(this, "获取设备数据失败,目前还没有家庭成员，请添加!",
						Toast.LENGTH_SHORT).show();
			} else if (PFEquipment == FailEquipment && PFFamily == OKFamily) {
				setViewEquipment(mCurrPos_E, 0, false);
				setViewFamily(mCurrPos_F, 0, true);
				Toast.makeText(this, "获取设备数据失败!", Toast.LENGTH_SHORT).show();
			} else if (PFFamily == NoDataFamily
					&& PFEquipment == NoDataEquipment) {
				setViewFamily(mCurrPos_F, 0, false);
				setViewEquipment(mCurrPos_E, 0, false);
				Toast.makeText(this, "目前还没有家庭成员和设备数据，请添加!", Toast.LENGTH_SHORT)
						.show();
			} else if (PFFamily == NoDataFamily && PFEquipment == OKEquipment) {
				setViewFamily(mCurrPos_F, 0, false);
				setViewEquipment(mCurrPos_E, 0, true);
				Toast.makeText(this, "目前还没有家庭成员，请添加!", Toast.LENGTH_SHORT)
						.show();
			} else if (PFEquipment == NoDataEquipment && PFFamily == OKFamily) {
				setViewEquipment(mCurrPos_E, 0, false);
				setViewFamily(mCurrPos_F, 0, true);
				Toast.makeText(this, "目前还没有设备数据，请等待工作人员添加!", Toast.LENGTH_SHORT)
						.show();
			} else if (PFFamily == OKFamily && PFEquipment == OKEquipment) {
				setViewFamily(mCurrPos_F, 0, true);
				setViewEquipment(mCurrPos_E, 0, true);
			}

		}
	}

	// 网络请求家庭成员
	private class GetFamilyListRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getFamilyListUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PFFamily = FailServerFamily;
				flagFamily = true;
				initResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						JSONArray ja = jo.optJSONArray("data");
						if (ja.length() == 0) {
							PFFamily = NoDataFamily;
							flagFamily = true;
							initResult();
						} else {
							getFamilyMemberList.clear();
							getFamilyMemberList = HttpManager.getFamilyListNoBind(ja);
							PFFamily = OKFamily;
							flagFamily = true;
							initResult();
						}
					} else {
						PFFamily = FailFamily;
						flagFamily = true;
						initResult();
					}
				} catch (JSONException e) {
					PFFamily = FailFamily;
					flagFamily = true;
					initResult();
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

	// 网络请求设备信息
	private class GetEquipmentListRequest extends
			AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getEquipmentListUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PFEquipment = FailServerEquipment;
				flagEquipment = true;
				initResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						JSONObject jo2 = jo.getJSONObject("data");
						JSONArray ja = jo2.getJSONArray("data");
						if (ja.length() == 0) {
							PFEquipment = NoDataEquipment;
							flagEquipment = true;
							initResult();
						} else {
							getEquipmentList.clear();
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo3 = ja.getJSONObject(i);
								int id = jo3.optInt("id");
								String deviceName = jo3.optString("deviceName");
								String imgUrl = jo3.optString("imgUrl");
								String appCode = jo3.optString("appCode");
								BindEquipment info = new BindEquipment(id,
										HttpManager.m_imageUrl + imgUrl,
										deviceName, appCode);
								getEquipmentList.add(info);
							}
							PFEquipment = OKEquipment;
							flagEquipment = true;
							initResult();
						}
					} else {
						PFEquipment = FailEquipment;
						flagEquipment = true;
						initResult();
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					PFEquipment = FailEquipment;
					flagEquipment = true;
					initResult();
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
	//网络请求 token
	private class SendRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(BindEquipmentActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getTokenUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResultToken("服务器返回数据错误！");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						JSONObject data = jo.optJSONObject("data");
						accessToken = data.optString("accessToken");
						PF = OKSend;
						initResultToken("");
					} else {
						String msg = jo.optString("errormsg");
						PF = FailSend;
						initResultToken(msg);
					}
				} catch (JSONException e) {
					PF = FailSend;
					initResultToken("解析失败！");
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
	//绑定手表
	private class BindWatchRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(BindEquipmentActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("bindWatchUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PFWatch = FailServerWatch;
				initResultWatch();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						PFWatch = OKWatch;
						initResultWatch();
					} else {
						PFWatch = FailWatch;
						initResultWatch();
					}
				} catch (JSONException e) {
					PFWatch = FailWatch;
					initResultWatch();
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

package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description: 好友比拼-个人资料页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月27日.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.FriendsPkDetailInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class PkPersonalActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private boolean flag;// 是否为附近，默认为否
	private CircleImageView imgAvatar;// 头像
	private ImageView imgGender;// 性别
	private DisplayImageOptions options;
	private TextView tvNickName, tvAddressPK, tvAddressFriend, tvnear,
			tvSignature, tvDistanceYes, tvDistanceBest, tvNickName2,
			tvDistanceAdd;// 昵称，地址，附近距离，签名，一天前距离，最好距离，昵称，多的距离
	private LinearLayout layoutNear, layoutFriend;
	private Button btnAdd, btnLeaveMessage, btnCompetition;// 添加好友，留言，邀请竞赛
	private String userId = "";
	private String friendsId = "";
	private String distance = "";// 一天前距离
	private double near;
	/**
	 * 网络请求
	 */
	private static final int OK = 1001;// 成功
	private static final int Fail = 1002;// 失败
	private static final int ChaoShi = 1003;// 超时
	private static final int OKAdd = 1004;// 成功
	private static final int FailAdd = 1005;// 失败
	private int PF = 1000;
	private ProgressDialog progressDialog;// 进度条
	private FriendsPkDetailInfo info = new FriendsPkDetailInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_pkpersonal);
		ExitApplicaton.getInstance().addActivity(this);
		flag = this.getIntent().getExtras().getBoolean("flag");
		friendsId = this.getIntent().getExtras().getString("id");
		distance = this.getIntent().getExtras().getString("distance");
		near = this.getIntent().getExtras().getDouble("near");
		initOptions();
		// 从首选项获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();
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
	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_pk_personal));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		layoutNear = (LinearLayout) findViewById(R.id.pkpersonal_near_layout);
		layoutFriend = (LinearLayout) findViewById(R.id.pkpersonal_friend_layout);
		imgAvatar = (CircleImageView) findViewById(R.id.pkpersonal_iv_img);
		imgGender = (ImageView) findViewById(R.id.pkpersonal_iv_gender);
		tvNickName = (TextView) findViewById(R.id.pkpersonal_tv_nickname);
		tvAddressPK = (TextView) findViewById(R.id.pkpersonal_tv_address_pk);
		tvAddressFriend = (TextView) findViewById(R.id.pkpersonal_tv_address_friend);
		tvnear = (TextView) findViewById(R.id.pkpersonal_tv_near);
		tvSignature = (TextView) findViewById(R.id.pkpersonal_tv_signature);
		tvDistanceYes = (TextView) findViewById(R.id.pkpersonal_tv_distance_yesterday);
		tvDistanceBest = (TextView) findViewById(R.id.pkpersonal_tv_distance_best);
		tvNickName2 = (TextView) findViewById(R.id.pkpersonal_tv_nickname2);
		tvDistanceAdd = (TextView) findViewById(R.id.pkpersonal_tv_distanceadd);
		btnAdd = (Button) findViewById(R.id.pkpersonal_btn_addfriend);
		btnLeaveMessage = (Button) findViewById(R.id.pkpersonal_btn_leavemessage);
		btnCompetition = (Button) findViewById(R.id.pkpersonal_btn_competition);
		btnAdd.setOnClickListener(this);
		btnLeaveMessage.setOnClickListener(this);
		btnCompetition.setOnClickListener(this);
		if (flag) {
			layoutNear.setVisibility(View.VISIBLE);
			layoutFriend.setVisibility(View.GONE);
			btnAdd.setBackgroundResource(R.drawable.btn_addfriend_selector);
		} else {
			layoutNear.setVisibility(View.GONE);
			layoutFriend.setVisibility(View.VISIBLE);
			btnAdd.setBackgroundResource(R.drawable.addfriend3);
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("好友比拼-个人资料页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		if (HttpManager.isNetworkAvailable(this)) {
			GetInfoRequest re = new GetInfoRequest();
			re.execute(HttpManager.urlGetMyFriendInfo(userId, friendsId));
		} else {
//			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试!", Toast.LENGTH_SHORT).show();
		}

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("好友比拼-个人资料页"); // 保证 onPageEnd 在onPause 之前调用
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				finish();
				break;
			case R.id.pkpersonal_btn_addfriend:
				// 加好友
				if(PF == OK){
					if (flag) {
						// 附近时添加
						if (info.getIsFriend().equals("0")
								|| info.getIsFriend().equals("")) {
							// 不是好友时添加
							if (!HttpManager.isNetworkAvailable(this)) {
//							Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
								Toast.makeText(this, "您的网络没连接好，请检查后重试!", Toast.LENGTH_SHORT).show();
								return;
							}
							AddRequest re = new AddRequest();
							re.execute(HttpManager.urlAddMyFriend(userId, friendsId));
						}

					}
				}else{
					Toast.makeText(this, "获取资料失败，不能添加!", Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.pkpersonal_btn_leavemessage:
				// 留言
				if(PF == OK){
					Intent intent=new Intent(this,LeaveMessageActivity.class);
					intent.putExtra("friendsId", friendsId);
					startActivity(intent);
				}else{
					Toast.makeText(this, "获取资料失败，不能添加!", Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.pkpersonal_btn_competition:
				// 邀请竞赛
				if(PF == OK){
					Intent intent2=new Intent(this,InvitationListActivity.class);
					intent2.putExtra("friendsId", friendsId);
					startActivity(intent2);
				}else{
					Toast.makeText(this, "获取资料失败，不能添加!", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}

	}

	// 请求结果
	private void initGetInfoResult() {
		if (PF == ChaoShi) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			Toast.makeText(this, "获取数据失败！", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			initData();
		} else if (PF == OKAdd) {
			Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
			btnAdd.setBackgroundResource(R.drawable.addfriend3);
		} else if (PF == FailAdd) {
			Toast.makeText(this, "添加失败！", Toast.LENGTH_SHORT).show();
		}

	}

	private void initData() {
		if (info.getIsFriend().equals("0")) {
			// 不是好友
			flag=true;
			btnAdd.setBackgroundResource(R.drawable.btn_addfriend_selector);
		} else {
			flag=false;
			btnAdd.setBackgroundResource(R.drawable.addfriend3);
		}
		ImageLoader.getInstance().displayImage(info.getAvatarPath(), imgAvatar, options);
		tvNickName.setText(info.getName());
		if (info.getGender().equals("1")) {
			imgGender.setBackgroundResource(R.drawable.icon_man);
		} else {
			imgGender.setBackgroundResource(R.drawable.icon_women);
		}
		if (info.getSign().equals("")) {
			tvSignature.setText("暂无个性签名");
		} else {
			tvSignature.setText("个性签名：" + info.getSign());
		}
		tvDistanceYes.setText(info.getDistance());
		tvDistanceBest.setText(info.getActivityMetersMax());
		if (Double.valueOf(info.getActivityMetersDiffer()) >= 0) {
			tvDistanceAdd.setText("多" + Math.abs(Double.valueOf(info.getActivityMetersDiffer())));
		} else {
			tvDistanceAdd.setText("少" + Math.abs(Double.valueOf(info.getActivityMetersDiffer())));
		}
		tvNickName2.setText(info.getName());
		if (info.getProvinceRegion().equals("北京")
				|| info.getProvinceRegion().equals("上海")
				|| info.getProvinceRegion().equals("天津")
				|| info.getProvinceRegion().equals("重庆")) {
			tvAddressFriend
					.setText(info.getProvinceRegion() + info.getAreaRegion());
			tvAddressPK.setText(info.getProvinceRegion() + info.getAreaRegion());
		} else {
			tvAddressFriend.setText(info.getProvinceRegion()
					+ info.getCityRegion());
			tvAddressPK
					.setText(info.getProvinceRegion() + info.getCityRegion());
		}

		tvnear.setText(Constant.NearDistance(info.getNear()));
	}

	// 获取数据
	private class GetInfoRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(PkPersonalActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("friendspkListUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initGetInfoResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						JSONObject jo1 = jo.getJSONObject("data");
						JSONObject jo2 = jo1.getJSONObject("data");
						String friendId = jo2.optString("userId");
						String name = jo2.optString("name");
						String avatarPath = jo2.optString("avatarPath");
						String gender = jo2.optString("gender");
						String provinceRegion = jo2.optString("provinceRegion");
						String cityRegion = jo2.optString("cityRegion");
						String areaRegion = jo2.optString("areaRegion");
						String sign = "";
						if (!jo2.isNull("sign")) {
							sign = jo2.optString("sign");
						}
						String activityMetersMax = jo2
								.optString("activityMetersMax");
						String activityMetersDiffer = jo2
								.optString("activityMetersDiffer");
						String isFriend = jo2.optString("isFriend");
						info = new FriendsPkDetailInfo(friendId, name,
								HttpManager.m_imageUrl + avatarPath, gender,
								provinceRegion, cityRegion, areaRegion, sign,
								activityMetersMax, activityMetersDiffer,
								isFriend, distance, near);
						PF = OK;
						initGetInfoResult();

					} else {
						PF = Fail;
						initGetInfoResult();
					}
				} catch (JSONException e) {
					Log.e("FriendsPkFragment", "解析失败！");
					PF = Fail;
					initGetInfoResult();
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

	// 添加好友
	private class AddRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(PkPersonalActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("AddFriendUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initGetInfoResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OKAdd;
						initGetInfoResult();
					} else {
						PF = FailAdd;
						initGetInfoResult();
					}
				} catch (JSONException e) {
					PF = FailAdd;
					initGetInfoResult();
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

package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:测量——睡眠页
 * @Author: wangshuangshuang.
 * @Create: 2015年4月20日.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.utils.LogUtil;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.customview.CircleProgressBar;
import com.sinosoft.fhcs.android.entity.BraceletSleepInfo;
import com.sinosoft.fhcs.android.entity.ChangeDeviceInfo;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.sinosoft.fhcs.android.util.SPUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.miao.lib.MiaoApplication;
import cn.miao.lib.model.HeartBean;
import cn.miao.lib.model.SleepBean;
import cn.miao.lib.model.SportBean;

import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_SHUIMIAN;
import static com.sinosoft.fhcs.android.util.Constant.device_result;
import static com.sinosoft.fhcs.android.util.Constant.device_shuimian;

public class BraceletSleepActivity extends BaseActivity implements OnClickListener {
	private TextView tvIncludeTitle;
	private Button btnBack;
	private CircleProgressBar circleProgressBar;// 自定义圆环进度条
	private TextView tvTitle;
	private TextView tvDeepTime, tvLightTime, tvSleepTime, tvNum,tvHeart;// 深睡 浅睡 睡眠 时长 心率
	// 超过百分比
	private TextView tvGoal;// 目标
	private TextView tvTotalTime;// 睡眠时长
	private String memberId = "";
	private String memberName = "";
	private String deviceName = "";
	private String accessToken = "";
	private String appCode = "";
	private BraceletSleepInfo sleepInfo = new BraceletSleepInfo();
	private ImageView tvSync;// 同步
	private TextView tvDate;
	private Button btnLeft, btnRight;
	private Calendar calendar;
	private String putDate = "";// 传递日期
	private String showDate = "";// 显示日期
	private String today="";//今天日期
	private int count=0;
	/**
	 * 网络请求
	 */
	private int PF = 1000;
	private static final int OK = 1001;// 成功
	private static final int Fail = 1002;// 失败
	private static final int ChaoShi = 1003;// 超时、
	private int PFSync = 2000;
	private static final int OKSync = 2001;// 同步成功
	private static final int FailSync = 2002;// 同步失败
	private static final int ChaoShiSync = 2003;// 同步超时
	private ProgressDialog progressDialog;// 进度条
	private ProgressDialog progressDialog2;// 进度条
	private Button btnChange;//选择设备
	private SPUtil spInstance;
	private int height;
	private int memberWeight;
	private int memberAge;
	private String memberGender;
	private String memberBirth;
	private TextView tvStatus;//设备连接状态
	private SharedPreferences prefs;


	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			Log.e("tag", "handleMessage: " + msg.toString());
			if(mActivity.isFinishing()){
				return;
			}
			switch (msg.what) {
				case 0:
					String log = String.valueOf(msg.obj) + "\n" + msg.toString();
					LogUtil.i("tag",log);
					tvStatus.setText("正在连接");
					tvStatus.setTextColor(getResources().getColor(R.color.measure_green));
					break;
				case 1:
					tvStatus.setText("已连接");
					tvStatus.setTextColor(getResources().getColor(R.color.measure_green));
					break;
				case 2:
					tvStatus.setText("正在测量");
					tvStatus.setTextColor(getResources().getColor(R.color.measure_green));
					break;
				case 3:
					tvStatus.setText("连接失败");
					tvStatus.setTextColor(getResources().getColor(R.color.measure_red));
					if(!BraceletSleepActivity.this.isFinishing()){
						FRToast.showToast(getApplicationContext(),"连接失败");
					}
					break;
				case 5:
					tvStatus.setText("连接已断开");
					tvStatus.setTextColor(getResources().getColor(R.color.measure_red));
					break;
				case device_result:
					tvStatus.setTextColor(getResources().getColor(R.color.measure_green));
					Object obj = msg.obj;
					if(obj instanceof SleepBean){//睡眠
						SleepBean sleepBean = (SleepBean) msg.obj;
//						FRToast.showToast(getApplicationContext(),"睡眠==" +sleepBean.toString());
						sleepInfo.setGoalTime(goalTime);
						sleepInfo.setDeepTime(sleepBean.getDeep_time());
						sleepInfo.setLightTime(sleepBean.getLight_time());
						sleepInfo.setSleepTime(sleepBean.getDuration());
						sleepInfo.setNum(10);
						/*sleepInfo = new BraceletSleepInfo(8,
								sleepBean.getDeep_time(), sleepBean.getLight_time(),
								sleepBean.getDuration(), 10,0);*/

					}else if(obj instanceof SportBean){//运动
						SportBean sportBean = (SportBean) msg.obj;
//						FRToast.showToast(getApplicationContext(),"运动==" +sportBean.toString());


					}else if(obj instanceof HeartBean){//心率
						tvStatus.setText("测量完成");
						HeartBean heartBean = (HeartBean) msg.obj;
//						FRToast.showToast(getApplicationContext(),"心率==" +heartBean.toString());
						sleepInfo.setHeartRate(heartBean.getHeart_rate());
						PF = OK;
						initGetInfoResult();
						initReguest();
					}
					break;
			}
		}
	};
	private BraceletSleepActivity mActivity;
	private int goalTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_braceletsleep);
		mActivity = this;
		spInstance = SPUtil.getInstance(this.getApplicationContext());
		ExitApplicaton.getInstance().addActivity(this);
		memberId = this.getIntent().getExtras().getString("memberId");
		memberName = this.getIntent().getExtras().getString("memberName");
		deviceName = this.getIntent().getExtras().getString("deviceName");
		accessToken = this.getIntent().getExtras().getString("accessToken");
		appCode = this.getIntent().getExtras().getString("appCode");
		memberName = this.getIntent().getExtras().getString("memberName");
		height = this.getIntent().getExtras().getInt("height");
		memberWeight = this.getIntent().getExtras().getInt("weight");
		memberAge = this.getIntent().getExtras().getInt("age");
		memberGender= this.getIntent().getExtras().getString("gender");
		memberBirth= this.getIntent().getExtras().getString("birth");
		System.out.println("memberId=" + memberId + "memberName=" + memberName
				+ "deviceName=" + deviceName + "accessToken=" + accessToken
				+ "appCode=" + appCode);
		init();
		if (HttpManager.isNetworkAvailable(this)) {
			GetInfoRequest re = new GetInfoRequest();
			re.execute(HttpManager.urlGetSleepData(memberId, putDate));
//			if (appCode != null && appCode.equals("01")) {
//				// 同步咕咚手环
//				tvSync.setVisibility(View.VISIBLE);
//				SyncRequest re = new SyncRequest();
//				re.execute(HttpManager.urlSyncAllData(memberId, accessToken));
//			} else if (appCode != null && appCode.equals("04")) {
//				// 同步手表
//				tvSync.setVisibility(View.VISIBLE);
//
//			} else {
//				// 没有设备
//				tvSync.setVisibility(View.GONE);
//				GetInfoRequest re = new GetInfoRequest();
//				re.execute(HttpManager.urlGetSleepData(memberId, putDate));
//			}
		} else {
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
		}

	}

	private void init() {
		btnChange = (Button) findViewById(R.id.titlebar_btn_change);
		btnChange.setVisibility(View.VISIBLE);
		btnChange.setOnClickListener(this);

		tvIncludeTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvIncludeTitle.setText(getResources()
				.getString(R.string.inputinfo_tv32));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.brasleep_tv_title);
		circleProgressBar = (CircleProgressBar) findViewById(R.id.brasleep_progressbar);
		tvSleepTime = (TextView) findViewById(R.id.brasleep_tv_shuimian);
		tvDeepTime = (TextView) findViewById(R.id.brasleep_tv_shenshui);
		tvLightTime = (TextView) findViewById(R.id.brasleep_tv_qianshui);
		tvTotalTime = (TextView) findViewById(R.id.brasleep_tv_total);
		tvGoal = (TextView) findViewById(R.id.brasleep_tv_goal);
		tvNum = (TextView) findViewById(R.id.brasleep_tv_value);
		tvHeart = (TextView) findViewById(R.id.brasleep_tv_xinlv);
		tvSync = (ImageView) findViewById(R.id.brasleep_tv_sync);
		tvSync.setOnClickListener(this);
		tvDate = (TextView) findViewById(R.id.brasleep_tv_date);
		btnLeft = (Button) findViewById(R.id.brasleep_btn_left);
		btnRight = (Button) findViewById(R.id.brasleep_btn_right);
		tvStatus = (TextView) findViewById(R.id.brasleep_tv_title_devce_status);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		today=simpleDateFormat.format(calendar.getTime());
		count=0;
		initputDate();
		initShowDate();
		initTitie();
		// initData();
	}
	private void initputDate() {
		putDate=Constant.getCurrentDate(today, count);
		if(putDate.equals(today)){
			btnRight.setVisibility(View.INVISIBLE);
		}else{
			btnRight.setVisibility(View.VISIBLE);
		}
	}

	private void initShowDate() {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = sim.parse(Constant.getCurrentDate(today, count));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
		showDate = simpleDateFormat.format(cal.getTime());
		if(putDate.equals(today)){
			tvDate.setText("今天");
		}else{
			tvDate.setText(showDate + "");
		}
	}

	private void initTitie() {
		ChangeDeviceInfo changeDeviceInfo = spInstance.getObj(memberId + SP_BLOOD_SELECT_SHUIMIAN, null);
		if (changeDeviceInfo != null && !changeDeviceInfo.getDeviceName().equals("")) {
			tvTitle.setText(memberName + "的可穿戴设备：" + changeDeviceInfo.getDeviceName());
		} else {
			tvTitle.setText(memberName + "暂未绑定可穿戴设备");// 奶奶的可穿戴设备：咕咚智能手环2
		}
/*
		if (deviceName != null && !deviceName.equals("")) {
			tvTitle.setText(memberName + "的可穿戴设备：" + deviceName);
		} else {
			tvTitle.setText(memberName + "暂未绑定可穿戴设备");// 奶奶的可穿戴设备：咕咚智能手环2
		}
*/
	}

	private void initData() {
		goalTime = sleepInfo.getGoalTime();
		if (sleepInfo.getGoalTime() == 0) {
			// 初始目标8小时
			sleepInfo.setGoalTime(480);
		}
		circleProgressBar.setMax(sleepInfo.getGoalTime());
		circleProgressBar.setProgress(sleepInfo.getSleepTime(), 700);
		// 展示2h23'
		tvTotalTime
				.setText(Constant.getHourFromS(sleepInfo.getSleepTime()));
		tvGoal.setText("目标"
				+ Constant.getHourFromS(sleepInfo.getGoalTime()));
		tvSleepTime
				.setText(Constant.getHourFromS(sleepInfo.getSleepTime()));
		tvDeepTime.setText(Constant.getHourFromS(sleepInfo.getDeepTime()));
		tvLightTime
				.setText(Constant.getHourFromS(sleepInfo.getLightTime()));
		tvNum.setText(sleepInfo.getNum() + "");
		tvHeart.setText(sleepInfo.getHeartRate() + "");

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("测量——睡眠页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("测量——睡眠页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		if (MiaoApplication.getMiaoHealthManager() == null) return;
		MiaoApplication.getMiaoHealthManager().disConnectAll();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.titlebar_btn_change://选择设备
				// 选择默认设备
				init();
				Intent intent4 = new Intent(BraceletSleepActivity.this,ChangeDeviceListActivity.class);
				intent4.setFlags(device_shuimian);
				intent4.putExtra("memberId",memberId);
				startActivityForResult(intent4, ChangeDeviceListActivity.request_change_device_list_activity);
				break;
			case R.id.brasleep_btn_left:
				// 向左
				if(HttpManager.isNetworkAvailable(this)){
					count--;
					initputDate();
					initShowDate();
					GetInfoRequest re = new GetInfoRequest();
					re.execute(HttpManager.urlGetSleepData(memberId, putDate));
				}else{
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.brasleep_btn_right:
				// 向右
				if(HttpManager.isNetworkAvailable(this)){
					count++;
					initputDate();
					initShowDate();
					GetInfoRequest re = new GetInfoRequest();
					re.execute(HttpManager.urlGetSleepData(memberId, putDate));
				}else{
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.brasleep_tv_sync:
				connectDevice();//连接本地设备
				// 手动同步
				if (HttpManager.isNetworkAvailable(this)) {
					if (appCode != null && appCode.equals("01")) {
						// 咕咚手环
						SyncRequest re = new SyncRequest();
						re.execute(HttpManager
								.urlSyncAllData(memberId, accessToken));
					} else if (appCode != null && appCode.equals("04")) {
						// 手表
					}

				} else {
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == ChangeDeviceListActivity.request_change_device_list_activity){//选择完设备之后开始连接设备
				ChangeDeviceInfo changeDeviceInfo = spInstance.getObj(memberId+SP_BLOOD_SELECT_SHUIMIAN, null);
				tvTitle.setText(memberName + "的可穿戴设备：" + changeDeviceInfo.getDeviceName());
				connectDevice();//连接测量
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	//妙健康的接入
	private void connectDevice() {
		if (MiaoApplication.getMiaoHealthManager() == null) return;
		MiaoApplication.getMiaoHealthManager().disConnectAll();
		ChangeDeviceInfo changeDeviceInfo = spInstance.getObj(memberId+SP_BLOOD_SELECT_SHUIMIAN,null);
		if(changeDeviceInfo != null){
			getCurrentData(changeDeviceInfo);
		}else {
			FRToast.showToast(this.getApplicationContext(),"未选择默认设备，无法进行测量。请先到【设备切换】页选择设备");
		}
	}
	public void getCurrentData( ChangeDeviceInfo changeDeviceInfo){
		//目前手环和体脂称需要弹框输入个人信息
//		List<FunctionInfoBean> fromJson = JsonUtils.fromJson(changeDeviceInfo.getFunction_info(), new TypeToken<ArrayList<FunctionInfoBean>>() {
//		}.getType());
		//初始化身高数据
		HashMap params = new HashMap();
		try {
			params.put("sex",Integer.parseInt(memberGender));
			params.put("birthday",memberBirth);
			params.put("height",height);
			params.put("weight",memberWeight);
			CommonUtil.getBleDeviceData(changeDeviceInfo,params,handler);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	// 获取数据返回结果
	private void initGetInfoResult() {
		if (PF == ChaoShi) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			Toast.makeText(this, "获取数据失败！", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			initData();
		}

	}

	//提交睡眠数据
	private void initReguest() {
		UpLoadSleepRequest upLoadSleepRequest = new UpLoadSleepRequest();
		HashMap<String, String> stringStringHashMap = new HashMap<>();
		stringStringHashMap.put("deepSleepMinutes",""+sleepInfo.getDeepTime()/60);
		stringStringHashMap.put("totalSleepMinutes",""+sleepInfo.getSleepTime()/60);
		stringStringHashMap.put("lightSleepMinutes",""+sleepInfo.getLightTime()/60);
		stringStringHashMap.put("heartRate",""+sleepInfo.getHeartRate());
		stringStringHashMap.put("familyMemeberId",memberId);
		upLoadSleepRequest.execute(HttpManager.urlShouHuanData2(Constant.putSheBei("睡眠")),stringStringHashMap);
	}

	// 同步咕咚返回结果
	private void initSyncResult(String message) {
		if (PFSync == ChaoShiSync) {
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PFSync == FailSync) {
			if (message.equals("")) {
				Toast.makeText(this, "同步数据失败！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			}

		} else if (PFSync == OKSync) {
			Toast.makeText(this, "同步数据成功！", Toast.LENGTH_SHORT).show();
		}
		// 获取数据
		GetInfoRequest re = new GetInfoRequest();
		re.execute(HttpManager.urlGetSleepData(memberId, putDate));
	}
	private class UpLoadSleepRequest extends AsyncTask<Object,Void,String>{
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(BraceletSleepActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Map<String,String> maps = (Map<String, String>) params[1];
			Log.e("getsleepUrl", url + "");
			result = HttpManager.getStringContentPost(url,maps);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Constant.exitProgressDialog(progressDialog);
			if (result.toString().trim().equals("ERROR")) {
				FRToast.showToast(getApplicationContext(),"服务器响应超时!");
				Log.e("MeasureResultActivity", "服务器响应超时!");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						tvStatus.setText("提交成功");
						FRToast.showToast(getApplicationContext(),"提交成功!");
						Log.e("MeasureResultActivity", "提交成功!");
					} else {
						try {
							JSONObject jo2 = jo.optJSONObject("error");
							String advise = jo2.optString("message");
							FRToast.showToast(getApplicationContext(),"提交失败!"+advise);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Log.e("MeasureResultActivity", "提交失败!");
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					FRToast.showToast(getApplicationContext(),"解析错误!");
					Log.e("MeasureResultActivity", "提交失败!");
					e.printStackTrace();
				}
			}
			super.onPostExecute(result);
		}
	}

	// 获取数据
	private class GetInfoRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(BraceletSleepActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();


		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getsleepUrl", url + "");
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
						int num = jo2.optInt("sleepPer");
						int totalSleepMinutes = jo2.optInt("totalSleepMinutes");
						int goalDeepMinutes = jo2.optInt("goalDeepMinutes");
						int lightSleepMinutes = jo2.optInt("lightSleepMinutes");
						int deepSleepMinutes = jo2.optInt("deepSleepMinutes");
						int heartRate = jo2.optInt("heartRate");
						sleepInfo = new BraceletSleepInfo(goalDeepMinutes,
								deepSleepMinutes, lightSleepMinutes,
								totalSleepMinutes, num,heartRate);
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

	// 同步咕咚数据
	private class SyncRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog2 = new ProgressDialog(BraceletSleepActivity.this);
			Constant.showProgressDialog(progressDialog2);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("syncpUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PFSync = ChaoShiSync;
				initSyncResult("");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PFSync = OKSync;
						initSyncResult("");

					} else {
						String message = jo.optString("message");
						PFSync = FailSync;
						initSyncResult(message);
					}
				} catch (JSONException e) {
					Log.e("FriendsPkFragment", "解析失败！");
					PFSync = FailSync;
					initSyncResult("");
				}

			}
			Constant.exitProgressDialog(progressDialog2);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}
}

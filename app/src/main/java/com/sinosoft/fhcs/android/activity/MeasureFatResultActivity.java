package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description: 测量结果页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.utils.LogUtil;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.entity.ChangeDeviceInfo;
import com.sinosoft.fhcs.android.entity.ShebeiMeaInfo;
import com.sinosoft.fhcs.android.util.BMIUtil;
import com.sinosoft.fhcs.android.util.BluetoothUtils;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.sinosoft.fhcs.android.util.SPUtil;
import com.sinosoft.fhcs.android.util.Util;
import com.sinosoft.fhcs.android.util.ZFYUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import cn.miao.lib.MiaoApplication;
import cn.miao.lib.model.SlimmingBean;

import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_TIZHI;
import static com.sinosoft.fhcs.android.util.Constant.device_result;
import static com.sinosoft.fhcs.android.util.Constant.device_tizhi;

public class MeasureFatResultActivity extends BaseActivity implements OnClickListener {

	public static int REQUEST_ENABLE_BT=2000;
	public static int INPUT_CODE=1000;
	/**
	 * 标题栏
	 */
	private Button btnBack;// 返回
	private TextView tvTitle;// 标题
	/**
	 * 通用
	 */
	private TextView tvDevice;// 设备名称
	private EditText tvAdvice;// 健康建议
	private TextView tvState;// 连接状态
	private TextView tvStateMsg;//错误信息
	private Button btnHelpNoDevice;// 帮助
	private ImageView ivState;// bg
	private RelativeLayout layoutAdvice;
	private RelativeLayout layoutReady;
	private Button btnAddReady;
	private LinearLayout layoutNodevice;
	private Button btnHelpIsDevice;// 帮助
	private Button btnStartMeasure;// 开始测量
	private Button btnRestartMeasure;//重新测量
	private Button btnInputMenu;//没有设备时点击
	/**
	 * 体重、血糖、体温
	 */
	private LinearLayout layoutWeight;
	private ImageView imgWeight;// src
	private TextView tvNameWeight;// 体重、血糖、体温
	private TextView tvValueWeight;// 值
	private TextView tvUnitWeight;// 单位
	private TextView tvSignWeight;// 正常范围
	/**
	 * 脂肪仪
	 */
	private LinearLayout layoutTiZhi;
	private ImageView imgTizhi;// src
	private TextView tvValueZFLTiZhi;// 脂肪率值
	private TextView tvValueNZDJTiZhi;// 内脏等级值
	private TextView tvValueSFLTiZhi;// 水分率值
	private TextView tvValueJRLTiZhi;// 肌肉量值
	private TextView tvSighTizhi;// 正常范围

	// 通用
	private String memberName = "";
	private String userId = "";
	private String strTitle = "体脂";
	private int height = 0;
	private int memberWeight = 0;
	private double BMI = 0;
	private int memberAge=0;
	private String memberGender="0";
	private String memberBirth="";
	private String memberId="";

	BroadcastReceiver bluetoothState;


	//private WearableSDKApi sdkApi;
	private boolean isShowingMeasureResult = false;
	private MeasureFatResultActivity instance;
	private SPUtil spInstance;
	private Button btnChange;

	/**
	 * 提交数据
	 */

	private Handler handler = new Handler(Looper.getMainLooper()) {
		@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			Log.e("tag", "handleMessage: " + msg.toString());
			switch (msg.what) {
				case 0:
					String log = String.valueOf(msg.obj) + "\n" + msg.toString();
					LogUtil.i("tag",log);
					tvState.setText("正在连接设备，请稍等...");
					tvState.setTextColor(getResources().getColor(R.color.measure_green));
//					ivState.setBackground(null);
					tvStateMsg.setText("");
					btnStartMeasure.setEnabled(true);
					btnInputMenu.setEnabled(false);
					btnStartMeasure.setBackgroundResource(R.drawable.ready_measure3);
					ivState.setBackground(null);
					break;
				case 1:
					btnAddReady.setEnabled(false);
					tvState.setText("已连接");
					tvStateMsg.setText("");
					tvState.setTextColor(getResources().getColor(R.color.measure_green));
					ivState.setBackgroundResource(R.drawable.vv);
					btnStartMeasure.setEnabled(false);
					btnStartMeasure.setBackgroundResource(R.drawable.ready_measure3);
					break;
				case 2:
					btnAddReady.setEnabled(false);
					tvState.setText("正在测量");
					tvState.setTextColor(getResources().getColor(R.color.measure_green));
					ivState.setBackgroundResource(R.drawable.vv);
					tvStateMsg.setText("");
					btnStartMeasure.setEnabled(false);
					btnInputMenu.setEnabled(false);
					btnStartMeasure.setBackgroundResource(R.drawable.ready_measure3);
					break;
				case 3:
					/*if(mActivity != null){
						FRToast.showToast(MeasureBloodResultActivity.this.getApplicationContext(),"连接失败");
					}*/
					tvState.setTextColor(getResources().getColor(R.color.measure_red));
					tvStateMsg.setText("");
					btnStartMeasure.setBackgroundResource(R.drawable.btn_mrdevice_selector);
					tvState.setText("连接失败，请重试");
					ivState.setBackgroundResource(R.drawable.xx);
//					initStartData();
					break;
				case 5:
					tvState.setTextColor(getResources().getColor(R.color.measure_red));
					ivState.setBackgroundResource(R.drawable.xx);
					tvState.setText("连接已断开");
					tvStateMsg.setText("请重新点击设备测量，并站上云康宝体重秤");
					btnStartMeasure.setEnabled(true);
					btnStartMeasure.setBackgroundResource(R.drawable.btn_mrdevice_selector);
					break;
				case device_result:
					SlimmingBean slimmingBean = (SlimmingBean) msg.obj;
//					BloodPressureBean bloodPressureBean = JsonUtils.fromJson(msg.obj.toString(), BloodPressureBean.class);
					if(slimmingBean != null) {
						isShowingMeasureResult = true;
						tvState.setText("测量完成");
						ivState.setBackgroundResource(R.drawable.vv);
						Calendar calendar = Calendar.getInstance();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String currentTime=simpleDateFormat.format(calendar.getTime());
						String measureTime = currentTime.replaceAll(" ", "%20");
						DecimalFormat decimalFormat = new DecimalFormat("0.0");
						//String num = decimalFormat.format(weight);
						double fatPercentage;
						double visceralFatRating;
						double moistureRate;
						double protenin;
						double bmi;
						try {
							fatPercentage = Double.parseDouble(String.valueOf(slimmingBean.getFat_ratio()));
							visceralFatRating = Double.parseDouble("0");//妙健康的sdk不支持内脏脂肪等级的获取
							moistureRate = Double.parseDouble(String.valueOf(slimmingBean.getMoisture()));
							protenin = Double.parseDouble(String.valueOf(slimmingBean.getMuscle()));
							bmi = Double.parseDouble(String.valueOf(slimmingBean.getBmi()));
							ShebeiMeaInfo shebei=new ShebeiMeaInfo(measureTime, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
							shebei.setFatPercentage(fatPercentage);
							shebei.setVisceralFatRating(visceralFatRating);
							shebei.setMoistureRate(moistureRate);
							shebei.setMuscleVolume(protenin);
							shebei.setNBMI(bmi);
							ShebeiMeaInfo shebeiMeaInfo = calculateBodyFatValue(shebei, memberBirth);
							shebeiResult(shebeiMeaInfo);
							initReguest(shebeiMeaInfo);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}
					break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_measureresult);
		spInstance = SPUtil.getInstance(this.getApplicationContext());
		ExitApplicaton.getInstance().addActivity(this);
		this.instance = this;
		strTitle = this.getIntent().getExtras().getString("strTitle");
		memberName = this.getIntent().getExtras().getString("memberName");
		height = this.getIntent().getExtras().getInt("height");
		memberWeight = this.getIntent().getExtras().getInt("weight");
		memberAge = this.getIntent().getExtras().getInt("age");
		memberGender= this.getIntent().getExtras().getString("gender");
		memberBirth= this.getIntent().getExtras().getString("birth");
		memberId= this.getIntent().getExtras().getString("familyMemberId");
		// 正常的BMI 18.5-23.9 体质指数（BMI）=体重（kg）÷身高^2（m）
		BMI = Constant.divideF(
				Constant.divideF(memberWeight, Constant.divideF(height, 100)
						* Constant.divideF(height, 100)), 1);
		// 从首选项获取机顶盒编号
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();
		//connectAndMeasure();
		initStartData();
		showFisrtRun();
	}

	protected void showFisrtRun() {
		// TODO Auto-generated method stub
		boolean appIsFisrstRun = Util.appIsFisrstRun(getApplicationContext(),Constant.APP_IS_FIRSR_RUN_TIZHONG);
		if(appIsFisrstRun){
			Intent intent = new Intent(this,MaskingActivity.class);
			intent.putExtra("strTitle", strTitle);
			startActivity(intent);
		}
	}


	private Button btnXueTangMenu;
	private ProgressDialog progressDialog;
	private LinearLayout llMearsultStart;
	//初始化显示
	private void initStartData() {
		layoutAdvice.setVisibility(View.GONE);
		tvAdvice.setText("暂无健康建议");
		tvState.setVisibility(View.GONE);
		tvStateMsg.setVisibility(View.GONE);
		ivState.setVisibility(View.GONE);
		//		btnHelpNoDevice.setVisibility(View.VISIBLE);
		btnHelpNoDevice.setVisibility(View.GONE);
		if (strTitle.equals("体重")) {
			tvState.setVisibility(View.VISIBLE);
			tvStateMsg.setVisibility(View.VISIBLE);
			ivState.setVisibility(View.VISIBLE);
			btnHelpNoDevice.setVisibility(View.GONE);
			tvState.setText("未连接");
			tvStateMsg.setText("请站上云康宝体重秤");
			tvState.setTextColor(getResources().getColor(R.color.measure_red));
			ivState.setBackgroundResource(R.drawable.xx);

			layoutNodevice.setVisibility(View.GONE);
			layoutReady.setVisibility(View.VISIBLE);
			btnStartMeasure.setEnabled(true);
			btnStartMeasure.setBackgroundResource(R.drawable.btn_mrdevice_selector);
			tvTitle.setText("体重测量");
			layoutWeight.setVisibility(View.VISIBLE);
			layoutTiZhi.setVisibility(View.GONE);
			tvDevice.setText("体重仪");
			tvNameWeight.setText("体重");
			tvValueWeight.setText("0");
			tvUnitWeight.setText("公斤");
			imgWeight.setImageResource(R.drawable.weight_ready);
			System.err.println("height=" + height);
			// 正常的BMI 18.5-23.9 体质指数（BMI）=体重（kg）÷身高^2（m）
			String[] temp = null;
			int age=memberAge;
			if (age <= 7) {
				age = 7;
			}
			if (age >= 18) {
				age = 18;
			}
			if(memberGender.equals("0")){
				temp=BMIUtil.FEMALE_NORMAL_STANDARD.get(age).split("#");
			}else{
				temp=BMIUtil.MALE_NORMAL_STANDARD.get(age).split("#");
			}
			double minBmi=Double.parseDouble(temp[0]);
			double maxBmi=Double.parseDouble(temp[1]);
			int weightMin = (int) (minBmi * Constant.divideF((float) height, 100) * Constant
					.divideF((float) height, 100));
			System.err.println("weightMin=" + weightMin);
			int weightMax = (int) (maxBmi * Constant.divideF((float) height, 100) * Constant
					.divideF((float) height, 100));
			System.err.println("weightMax=" + weightMax);
			tvSignWeight.setText("标准体重：\n" + weightMin + "~" + weightMax);
			//initBluetooth();//设备测量初始化
		} else if (strTitle.equals("体脂")) {
			layoutNodevice.setVisibility(View.VISIBLE);
			layoutReady.setVisibility(View.GONE);
			tvTitle.setText("体脂测量");
			layoutTiZhi.setVisibility(View.VISIBLE);
			layoutWeight.setVisibility(View.GONE);
			tvDevice.setText("脂肪仪");
			tvValueZFLTiZhi.setText("0");
			tvValueNZDJTiZhi.setText("0");
			tvValueSFLTiZhi.setText("0");
			tvValueJRLTiZhi.setText("0");
			imgTizhi.setImageResource(R.drawable.tizhi_ready);
			tvState.setVisibility(View.VISIBLE);
			tvStateMsg.setVisibility(View.VISIBLE);
			tvState.setTextColor(getResources().getColor(R.color.measure_red));
			ivState.setBackgroundResource(R.drawable.xx);
			tvState.setText("未连接");
			tvStateMsg.setText("请站上云康宝体重秤");
			btnXueTangMenu.setEnabled(true);
			btnXueTangMenu.setBackgroundResource(R.drawable.btn_mrdevice_selector);
			String[] temp = null;
			int age=memberAge;
			if (age <= 5) {
				age = 5;
			}
			if(18<=age&&age<=39){
				age=18;
			}
			if(40<=age&&age<=59){
				age=40;
			}
			if (age >= 60) {
				age = 60;
			}
			if(memberGender.equals("0")){
				temp=ZFYUtil.FEMALE_NORMAL_STANDARD.get(age).split("#");
			}else{
				temp=ZFYUtil.MALE_NORMAL_STANDARD.get(age).split("#");
			}
			double minTZ=Double.parseDouble(temp[0]);
			double maxTZ=Double.parseDouble(temp[1]);
			tvSighTizhi.setText("标准脂肪率 ："+minTZ+"%~"+maxTZ+"%");
		} else if (strTitle.equals("体温")) {
			layoutNodevice.setVisibility(View.VISIBLE);
			layoutReady.setVisibility(View.GONE);
			tvTitle.setText("体温测量");
			layoutWeight.setVisibility(View.VISIBLE);
			layoutTiZhi.setVisibility(View.GONE);
			tvDevice.setText("体温计");
			tvNameWeight.setText("体温");
			tvValueWeight.setText("0");
			tvUnitWeight.setText("℃");
			imgWeight.setImageResource(R.drawable.temperature_ready);
			tvSignWeight.setText("正常体温：\n36~37℃");
		} else if (strTitle.equals("血糖")) {
			tvState.setVisibility(View.VISIBLE);
			tvStateMsg.setVisibility(View.VISIBLE);
			ivState.setVisibility(View.VISIBLE);
			tvState.setText("未连接");
			tvStateMsg.setText("请打开血糖仪");
			tvState.setTextColor(getResources().getColor(R.color.measure_red));
			ivState.setBackgroundResource(R.drawable.xx);
			layoutReady.setVisibility(View.VISIBLE);
			tvTitle.setText("血糖测量");
			layoutWeight.setVisibility(View.VISIBLE);
			layoutTiZhi.setVisibility(View.GONE);
			layoutNodevice.setVisibility(View.VISIBLE);
			tvDevice.setText("血糖仪");
			tvNameWeight.setText("血糖");
			tvValueWeight.setText("0");
			tvUnitWeight.setText("mmol/L");
			imgWeight.setImageResource(R.drawable.xuetang_ready);
			tvSignWeight
					.setText("标准空腹血糖：\n4.4~7.0mmol/L\n餐后血糖：\n4.4~10.0mmol/L");
		}
	}


	// 初始化控件
	private void init() {
		// 标题栏
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		btnChange = (Button) findViewById(R.id.titlebar_btn_change);
		btnChange.setVisibility(View.VISIBLE);
		btnChange.setOnClickListener(this);
		// 通用
		layoutAdvice = (RelativeLayout) findViewById(R.id.mearesult_advice_layout);
		tvDevice = (TextView) findViewById(R.id.mearesult_tv_title);
		tvAdvice = (EditText) findViewById(R.id.mearesult_advice);
		tvState = (TextView) findViewById(R.id.mearesult_tv_state);
		tvStateMsg=(TextView) findViewById(R.id.mearesult_tv_state_msg);
		ivState = (ImageView) findViewById(R.id.mearesult_iv_state);
		layoutReady = (RelativeLayout) findViewById(R.id.mearesult_ready_layout);
		btnAddReady = (Button) findViewById(R.id.mearesult_ready_btn);
		btnAddReady.setOnClickListener(this);
		btnStartMeasure = (Button) findViewById(R.id.mearesult_ready_btnstart);
		btnStartMeasure.setOnClickListener(this);
		btnRestartMeasure=(Button) findViewById(R.id.mearesult_ready_btnRestart);
		btnRestartMeasure.setOnClickListener(this);
		btnHelpIsDevice = (Button) findViewById(R.id.mearesult_ready_btnhelp);
		btnHelpIsDevice.setOnClickListener(this);
		btnHelpNoDevice= (Button) findViewById(R.id.mearesult_btn_help);
		btnHelpNoDevice.setOnClickListener(this);
		layoutNodevice=(LinearLayout) findViewById(R.id.mearesult_nodevice_layout);
		btnInputMenu=(Button) findViewById(R.id.mearesult_nodevice_btn);
		btnXueTangMenu=(Button) findViewById(R.id.mearesult_device_btn);
		btnInputMenu.setOnClickListener(this);
		btnXueTangMenu.setOnClickListener(this);
		// 体重、血糖、体温
		layoutWeight = (LinearLayout) findViewById(R.id.mearesult_layout_weight);
		imgWeight = (ImageView) findViewById(R.id.mearesult_img_weight);
		tvNameWeight = (TextView) findViewById(R.id.mearesult_name_weight);
		tvValueWeight = (TextView) findViewById(R.id.mearesult_value_weight);
		tvUnitWeight = (TextView) findViewById(R.id.mearesult_unit_weight);
		tvSignWeight = (TextView) findViewById(R.id.mearesult_sign_weight);
		// 脂肪仪
		layoutTiZhi = (LinearLayout) findViewById(R.id.mearesult_layout_tizhi);
		imgTizhi = (ImageView) findViewById(R.id.mearesult_img_tizhi);
		tvValueZFLTiZhi = (TextView) findViewById(R.id.mearesult_value_zfl_tizhi);
		tvValueNZDJTiZhi = (TextView) findViewById(R.id.mearesult_value_nzdj_tizhi);
		tvValueSFLTiZhi = (TextView) findViewById(R.id.mearesult_value_sfl_tizhi);
		tvValueJRLTiZhi = (TextView) findViewById(R.id.mearesult_value_jrl_tizhi);
		tvSighTizhi = (TextView) findViewById(R.id.mearesult_sign_tizhi);

		llMearsultStart = (LinearLayout) findViewById(R.id.ll_mearesult_ready_btnstart);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("体脂测量结果页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		/*if(strTitle.equals("体重")){
			// doConnect();

		}*/
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("体脂测量结果页"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);
		/*connectAndMeasure();
		boolean screen = Util.getScreen(this);*/
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (MiaoApplication.getMiaoHealthManager() == null) return;
		MiaoApplication.getMiaoHealthManager().disConnectAll();

	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		if(bluetoothState != null){
			unRegisterBluetoothChangeForOnce();
		}
	}
	public void registerBlutetoothChangeForOnce(){
		unRegisterBluetoothChangeForOnce();
		bluetoothState = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String stateExtra = BluetoothAdapter.EXTRA_STATE;
				int state = intent.getIntExtra(stateExtra, -1);
				switch(state) {
					case BluetoothAdapter.STATE_TURNING_ON:
						//Toast.makeText(getApplicationContext(), "STATE_TURNING_ON", 1).show();
						break;
					case BluetoothAdapter.STATE_ON:
						unregisterReceiver(bluetoothState);
						connectDevice();
						// Toast.makeText(getApplicationContext(), "STATE_ON", 1).show();
						break;
					case BluetoothAdapter.STATE_TURNING_OFF:
						// Toast.makeText(getApplicationContext(), "STATE_TURNING_OFF", 1).show();
						break;
					case BluetoothAdapter.STATE_OFF:
						// Toast.makeText(getApplicationContext(), "STATE_OFF", 1).show();
						//  unregisterReceiver(bluetoothState);
						break;
				}
			}
		};
		registerReceiver(bluetoothState,new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
	}
	private void unRegisterBluetoothChangeForOnce() {
		try {
			if(bluetoothState  != null){
				unregisterReceiver(bluetoothState);
				bluetoothState = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.mearesult_ready_btnstart:
				// 马上测量

				//设备测量
				if(CommonUtil.isEnabledNetWork(getApplicationContext())){
					if(!CommonUtil.isEnableBluetooth()){
						registerBlutetoothChangeForOnce();
						CommonUtil.openBluetooth();
					}else{
						if(BluetoothUtils.isBluetoothDeviceOpen()){
							connectDevice();
							return;
						}else{
							openBT();
						}
					}
				}else{
					Toast.makeText(this, "请先检查网络，稍后再试！", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.titlebar_btn_change:
				//设备切换
				initStartData();
				Intent intent4 = new Intent(MeasureFatResultActivity.this,ChangeDeviceListActivity.class);
				intent4.putExtra("changeDevice", "blood");
				intent4.setFlags(device_tizhi);
				startActivityForResult(intent4, ChangeDeviceListActivity.request_change_device_list_activity);
				break;
			case R.id.mearesult_ready_btnRestart:
				//重新测量
				isShowingMeasureResult = false;
				initStartData();
				break;
			case R.id.mearesult_ready_btnhelp:
				// 设备帮助
				if(strTitle.equals("体重")){
					Intent intent=new Intent(MeasureFatResultActivity.this,SheBeiHelpActivity.class);
					intent.putExtra("strTitle", strTitle);
					startActivity(intent);
				}else{
					Toast.makeText(this, "暂无设备帮助！", Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.mearesult_btn_help:
				// 帮助
				break;
			case R.id.mearesult_device_btn:
				//体脂测量
			/*progressDialog = new ProgressDialog(this);
			Constant.showProgressDialog(progressDialog);*/

				if(CommonUtil.isEnabledNetWork(getApplicationContext())){
					if(!CommonUtil.isEnableBluetooth()){
						registerBlutetoothChangeForOnce();
						CommonUtil.openBluetooth();
					}else{
						if(BluetoothUtils.isBluetoothDeviceOpen()){
//							connectAndMeasure();
							connectDevice();
							return;
						}else{
							openBT();
						}
					}
				}else{
					Toast.makeText(this, "请先检查网络，稍后再试！", Toast.LENGTH_SHORT).show();
				}
			/*QNUser user = buildUser();
			if (user == null) {
				return;
			}
			connect(user);*/
				break;
			case R.id.mearesult_nodevice_btn:
				//没有设备时点击手动录入
				Intent intent2 = new Intent(MeasureFatResultActivity.this,
						InputInfoActivity.class);
				intent2.putExtra("strTitle", strTitle);
				intent2.putExtra("memberName", memberName);
				startActivityForResult(intent2, INPUT_CODE);
				break;
			case R.id.mearesult_ready_btn:
				//有设备时点击手动录入
				Intent intent3 = new Intent(MeasureFatResultActivity.this,
						InputInfoActivity.class);
				intent3.putExtra("strTitle", strTitle);
				intent3.putExtra("memberName", memberName);
				startActivityForResult(intent3, INPUT_CODE);
				break;
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			default:
				break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == INPUT_CODE) {
			if (data != null) {
				String jsonStr = data.getStringExtra("Json");
				int meal = data.getExtras().getInt("xuetang");
				fresh(jsonStr, meal);

			}
		}else if (requestCode == REQUEST_ENABLE_BT
				&& resultCode == Activity.RESULT_CANCELED) {
			return;
		}
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == ChangeDeviceListActivity.request_change_device_list_activity){//选择完设备之后开始连接设备
//				connectDevice();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	//妙健康的接入
	private void connectDevice() {
		ChangeDeviceInfo changeDeviceInfo = spInstance.getObj(SP_BLOOD_SELECT_TIZHI,null);
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
//		CommonUtil.getBleDeviceData(changeDeviceInfo,null,handler);

	}
	private void fresh(String jsonStr, int meal) {
		layoutAdvice.setVisibility(View.VISIBLE);
		layoutReady.setVisibility(View.GONE);
		layoutNodevice.setVisibility(View.GONE);
		try {
			JSONObject jo = new JSONObject(jsonStr);
			int deviceType = jo.optInt("deviceType");
			String advise = jo.optString("advise");
			if(advise!=null&&!advise.equals("")){
				tvAdvice.setText(advise);
			}else{
				tvAdvice.setText("暂无健康建议");
			}

			JSONObject model = jo.optJSONObject("model");
			switch (deviceType) {
				// 1--脂肪仪 2--健康秤 3--血压计4--血糖仪 5--耳温枪
				case 1:
					// 体脂
					double moistureRate = model.optDouble("moistureRate");
					double fatPercentage = model.optDouble("fatPercentage");
					double muscleVolume = model.optDouble("muscleVolume");
					double visceralFatRating = model.optDouble("visceralFatRating");
					tvValueZFLTiZhi.setText(fatPercentage + "");
					tvValueNZDJTiZhi.setText(visceralFatRating + "");
					tvValueSFLTiZhi.setText(moistureRate + "");
					tvValueJRLTiZhi.setText(muscleVolume + "");
					initShowTiZhi(fatPercentage);
					break;
				case 2:
					// 体重
					double weight = model.optDouble("weight");
					tvValueWeight.setText(weight + "");
					initShowWeight(weight);
					tvState.setText("测量完成");
					tvStateMsg.setVisibility(View.GONE);
					tvState.setTextColor(getResources().getColor(R.color.measure_green));
					ivState.setVisibility(View.VISIBLE);
					ivState.setBackgroundResource(R.drawable.vv);
					break;
				case 4:
					// 血糖
					double bloodGlucose = model.optDouble("bloodGlucose");
					tvValueWeight.setText(bloodGlucose + "");
					initShowXueTang(bloodGlucose, meal);
					break;
				case 5:
					// 体温
					double temperature = model.optDouble("temperature");
					tvValueWeight.setText(temperature + "");
					initShowTemperature(temperature);
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initShowTiZhi(double fatPercentage) {
		String temp=ZFYUtil.fetchBMIState(memberAge, Integer.valueOf(memberGender),fatPercentage);
		if(temp.equals("消瘦")){
			// 过低
			imgTizhi.setImageResource(R.drawable.tizhi_thin);
		}else if(temp.equals("肥胖")){
			// 肥胖
			imgTizhi.setImageResource(R.drawable.tizhi_fat);
		}else if(temp.equals("正常")){
			// 正常
			imgTizhi.setImageResource(R.drawable.tizhi_normal);
		}

	}

	private void initShowWeight(double weight) {
		double BMI2 = Constant.divideF(
				Constant.divideF((float) weight, Constant.divideF(height, 100)
						* Constant.divideF(height, 100)), 1);
		String temp=BMIUtil.fetchBMIState(memberAge, BMI2, Integer.valueOf(memberGender));
		if(temp.equals("消瘦")){
			// 过低
			imgWeight.setImageResource(R.drawable.weight_thin);
		}else if(temp.equals("肥胖")){
			// 肥胖
			imgWeight.setImageResource(R.drawable.weight_fat);
		}else if(temp.equals("超重")){
			// 超重
			imgWeight.setImageResource(R.drawable.weight_chaofat);
		}else if(temp.equals("正常")){
			// 正常
			imgWeight.setImageResource(R.drawable.weight_normal);
		}

	}


	private void initShowXueTang(double xuetang, int meal) {
		// 餐前：4.4-7.0 餐后：4.4-10.0
		if (meal == 1||meal==3||meal==5||meal==7) {
			// 餐前
			if (xuetang < 4.4) {
				// 低血糖
				imgWeight.setImageResource(R.drawable.xuetang_low);
			} else if (xuetang >= 4.4 && xuetang <= 7.0) {
				// 标准
				imgWeight.setImageResource(R.drawable.xuetang_normal);
			} else if (xuetang > 7.0) {
				// 高血糖
				imgWeight.setImageResource(R.drawable.xuetang_hign);
			}
		} else {
			// 餐后
			if (xuetang < 4.4) {
				// 低血糖
				imgWeight.setImageResource(R.drawable.xuetang_low);
			} else if (xuetang >= 4.4 && xuetang <= 10.0) {
				// 标准
				imgWeight.setImageResource(R.drawable.xuetang_normal);
			} else if (xuetang > 10.0) {
				// 高血糖
				imgWeight.setImageResource(R.drawable.xuetang_hign);
			}
		}

	}

	private void initShowTemperature(double temperature) {
		if (temperature < 36) {
			// 低温
			imgWeight.setImageResource(R.drawable.temperature_diwen);
		} else if (temperature >= 36 && temperature <= 37) {
			// 正常
			imgWeight.setImageResource(R.drawable.temperature_normal);
		} else if (temperature > 37 && temperature < 38) {
			// 低热
			imgWeight.setImageResource(R.drawable.temperature_dire);
		} else if (temperature >= 38 && temperature < 39) {
			// 中热
			imgWeight.setImageResource(R.drawable.temperature_zdfr);
		} else {
			// 高烧
			imgWeight.setImageResource(R.drawable.temperature_gaore);
		}

	}


	/**
	 * 设备测量
	 */
	//结果
	private void shebeiResult(ShebeiMeaInfo info) {
		layoutAdvice.setVisibility(View.VISIBLE);
		layoutNodevice.setVisibility(View.GONE);
		tvAdvice.setText("正在获取健康建议，请稍后...");
		if(strTitle.equals("体重")){
			tvValueWeight.setText(info.getWeight()+"");
			initShowWeight(info.getWeight());
		}else if(strTitle.equals("血糖")){
			tvValueWeight.setText(info.getBloodGlucose()+"");
			initShowXueTang(info.getBloodGlucose(),10);
		}else if(strTitle.equals("体脂")){
			layoutReady.setVisibility(View.GONE);
			layoutNodevice.setVisibility(View.GONE);
			/**
			 * 脂肪仪
			 */
			/*private LinearLayout layoutTiZhi;
			private ImageView imgTizhi;// src
			private TextView tvValueZFLTiZhi;// 脂肪率值
			private TextView tvValueNZDJTiZhi;// 内脏等级值
			private TextView tvValueSFLTiZhi;// 水分率值
			private TextView tvValueJRLTiZhi;// 肌肉量值
			private TextView tvSighTizhi;// 正常范围
*/

			tvValueZFLTiZhi.setText("" +info.getFatPercentage());
			tvValueNZDJTiZhi.setText("" +info.getVisceralFatRating());
			tvValueSFLTiZhi.setText("" +info.getMoistureRate());
			tvValueJRLTiZhi.setText("" +info.getMuscleVolume());
			initShowTiZhi(info.getFatPercentage());
		}
	}

	//网络请求
	private void initReguest(ShebeiMeaInfo shebei) {
		AddRequest re = new AddRequest();
		re.execute(HttpManager.urlInputInfo(Constant.putSheBei(strTitle),
				userId, memberName, shebei.getMeasureTime(), shebei.getWeight()+"",
				shebei.getBloodGlucose()+"", "", "", "", shebei.getTemperature()+"", shebei.getDiastolicPressure()+"",
				shebei.getSystolicPressure()+"",
				shebei.getPulse()+"", shebei.getMoistureRate()+"", shebei.getMoistureRate()+"", shebei.getVisceralFatRating()+"",
				shebei.getMuscleVolume()+"", 0,
				"", ""));

	}
	private class AddRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("ShebeiInputInfoUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				Log.e("MeasureResultActivity", "服务器响应超时!");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						try {
							JSONObject jo2 = jo.optJSONObject("data");
							String advise = jo2.optString("advise");
							tvAdvice.setText(advise);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Log.e("MeasureResultActivity", "提交成功!");
					} else {
						Log.e("MeasureResultActivity", "提交失败!");
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					Log.e("MeasureResultActivity", "提交失败!");
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


	//体重仪
//	QNUser buildUser() {
//		Date birthday = null;
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
//		try {
//			birthday = dateFormat.parse(memberBirth);
//		} catch (Exception e) {
//			System.out.println("请按yyyy-M-d 的格式输入生日");
//
//		}
//		return new QNUser(memberId, height, Integer.parseInt(memberGender), birthday);
//	}

	//打开蓝牙
	public void openBT(){
		// 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
		// 那么将会收到RESULT_OK的结果，
		// 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
		Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(mIntent,Constant.OPEN_CODE);
		// 用enable()方法来开启，无需询问用户(实惠无声息的开启蓝牙设备),这时就需要用到android.permission.BLUETOOTH_ADMIN权限。
		// mBluetoothAdapter.enable();
		// mBluetoothAdapter.disable();//关闭蓝牙
	}

//	public static void calculateBodyFatValue(Map<String, String> data, QNUser user) {
//		//1.2×BMI+0.23×年龄-5.4-10.8×性别（男为1，女为0）
//		try {
//			double bmi = Double.parseDouble(data.get("bmi"));
//			int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt((""+user.getBirthday()).substring(0, 4));
//			double bodyFat = ((1.2 * bmi) + (0.23 * age) - (5.4) - (10.8 * user.getGender()));
//			if (bodyFat < 0) {
//				bodyFat = 0;
//			}
//			DecimalFormat decimalFormat = new DecimalFormat("##.#");
//			data.put("bodyFat", decimalFormat.format(bodyFat));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public  ShebeiMeaInfo calculateBodyFatValue(ShebeiMeaInfo data, String birthday) {
		//1.2×BMI+0.23×年龄-5.4-10.8×性别（男为1，女为0）
		double bmi = data.getNBMI();
		int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(birthday.substring(0, 4));
		double bodyFat = ((1.2 * bmi) + (0.23 * age) - (5.4) - (10.8 * Integer.parseInt(memberGender)));
		if (bodyFat < 0) {
			bodyFat = 0;
		}
		DecimalFormat decimalFormat = new DecimalFormat("##.#");
		data.setFatPercentage(Double.parseDouble(decimalFormat.format(bodyFat)));
		return data;
	}

}

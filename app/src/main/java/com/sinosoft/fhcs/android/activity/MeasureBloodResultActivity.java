package com.sinosoft.fhcs.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.utils.LogUtil;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.entity.ChangeDeviceInfo;
import com.sinosoft.fhcs.android.entity.ShebeiMeaInfo;
import com.sinosoft.fhcs.android.util.BluetoothUtils;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.sinosoft.fhcs.android.util.SPUtil;
import com.sinosoft.fhcs.android.util.Util;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import cn.miao.lib.MiaoApplication;
import cn.miao.lib.model.BloodPressureBean;

import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_XUEYA;
import static com.sinosoft.fhcs.android.util.Constant.device_result;
import static com.sinosoft.fhcs.android.util.Constant.device_xueya;

public class MeasureBloodResultActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener{
	public static int INPUT_CODE=1000;
	private boolean isConnented;//正在测量中，无法执行操作

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
	private Button btnHelpIsDevice;// 帮助
	private Button btnStartMeasure;// 开始测量
	private Button btnRestartMeasure;//重新测量
	/**
	 * 血压
	 */
	private LinearLayout layoutXueYa;
	private ImageView imgXueYa;// src
	private TextView tvValueMaiBoXueYa;// 脉搏值
	private TextView tvValueGaoYaXueYa;// 高压值
	private TextView tvValueDiYaXueya;// 低压值
	private TextView tvSignXueYa;// 正常范围
	//public  WearableSDKApi sdkApi;
	private Button btnChange;//切换设备

	// 通用
	private String memberName = "";
	private String userId = "";
	private String strTitle = "";

	//记住选择的血压计
	String bloodName = null;
	private SPUtil spInstance;
	private ChangeDeviceInfo dInfo;

	private LinearLayout linear;

	ProgressDialog progressDialog = null;
	private String cId;
	private MeasureBloodResultActivity mActivity;


	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			Log.e("tag", "handleMessage: " + msg.toString());
			switch (msg.what) {
				case 0:
					String log = String.valueOf(msg.obj) + "\n" + msg.toString();
					LogUtil.i("tag",log);
					tvState.setText("正在连接设备，请稍等...");
					tvStateMsg.setText("");
					tvState.setTextColor(getResources().getColor(R.color.measure_green));
					ivState.setVisibility(View.GONE);
					btnStartMeasure.setEnabled(false);
					break;
				case 1:
					isConnented = true;
					btnAddReady.setEnabled(false);
					tvState.setText("已连接");
					tvStateMsg.setText("");
					tvState.setTextColor(getResources().getColor(R.color.measure_green));
					ivState.setBackgroundResource(R.drawable.vv);
					btnStartMeasure.setEnabled(false);
					btnStartMeasure.setBackgroundResource(R.drawable.ready_measure3);
					break;
				case 2:
					isConnented = true;
					btnAddReady.setEnabled(false);
					tvState.setText("正在测量");
					tvStateMsg.setText("");
					tvState.setTextColor(getResources().getColor(R.color.measure_green));
					ivState.setBackgroundResource(R.drawable.vv);
					btnStartMeasure.setEnabled(false);
					btnStartMeasure.setBackgroundResource(R.drawable.ready_measure3);
					break;
				case 3:
					/*if(mActivity != null){
						FRToast.showToast(MeasureBloodResultActivity.this.getApplicationContext(),"连接失败");
					}*/
					btnAddReady.setEnabled(true);
					btnStartMeasure.setEnabled(true);
					tvStateMsg.setText("");
					tvState.setTextColor(getResources().getColor(R.color.measure_red));
					btnStartMeasure.setBackgroundResource(R.drawable.btn_mrdevice_selector);
					tvState.setText("连接失败，请重试");
					ivState.setBackgroundResource(R.drawable.xx);
//					initStartData();
					break;
				case 5:
					tvState.setText("设备已断开连接");
					tvState.setTextColor(getResources().getColor(R.color.measure_red));
					ivState.setBackgroundResource(R.drawable.xx);
					btnStartMeasure.setEnabled(true);
					btnStartMeasure.setBackgroundResource(R.drawable.btn_mrdevice_selector);
					btnAddReady.setEnabled(true);
					isConnented = false;
					break;
				case device_result:
					BloodPressureBean bloodPressureBean = (BloodPressureBean) msg.obj;
//					BloodPressureBean bloodPressureBean = JsonUtils.fromJson(msg.obj.toString(), BloodPressureBean.class);
					if(bloodPressureBean != null) {
						tvState.setText("测量完成");
						btnAddReady.setEnabled(true);
						btnStartMeasure.setEnabled(true);
						tvState.setTextColor(getResources().getColor(R.color.measure_green));
						Calendar calendar = Calendar.getInstance();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String currentTime = simpleDateFormat.format(calendar.getTime());
						String measureTime = currentTime.replaceAll(" ", "%20");

//						Map<String, String> dataMap = reciveData.getData();
						double heartRate = Double.parseDouble(String.valueOf(bloodPressureBean.getHeart_rate()));
						double diastole = Double.parseDouble(String.valueOf(bloodPressureBean.getLow_press()));
						double shrink = Double.parseDouble(String.valueOf(bloodPressureBean.getHigh_press()));
						ShebeiMeaInfo shebei = new ShebeiMeaInfo(measureTime, 0, heartRate, diastole, shrink, 0, 0, 0, 0, 0, 0);
						//ShebeiMeaInfo shebei=new ShebeiMeaInfo(measureTime, 0, info.getPulseValue(), info.getDbpValue(), info.getSbpValue(), 0, 0, 0, 0, 0, 0);
						System.out.println("测量结果" + shrink + "," + diastole + "," + heartRate);
						setDateToSelectDevice();
						shebeiResult(shebei);
						initReguest(shebei);
						btnAddReady.setEnabled(true);
						isConnented = false;
					}
					break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_measure_blood_result);
		mActivity = this;
		ExitApplicaton.getInstance().addActivity(this);
		strTitle = this.getIntent().getExtras().getString("strTitle");
		memberName = this.getIntent().getExtras().getString("memberName");
		// 从首选项获取机顶盒编号
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		spInstance = SPUtil.getInstance(this);
		init();
		if(strTitle.equals("血压")){
			initStartData();
			showFisrtRun();
		}
	}

	BroadcastReceiver bluetoothState;
	@Override
	public void finish() {
		super.finish();
		if(bluetoothState != null){
			unRegisterBluetoothChangeForOnce();
		}
	};
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
	protected void showFisrtRun() {
		// TODO Auto-generated method stub
		boolean appIsFisrstRun = Util.appIsFisrstRun(getApplicationContext(),Constant.APP_IS_FIRSR_RUN_XUEYA);
		if(appIsFisrstRun){
			Intent intent = new Intent(this,MaskingActivity.class);
			intent.putExtra("strTitle", strTitle);
			startActivity(intent);
		}
	}

	private void initStartData() {
		// TODO Auto-generated method stub
		layoutAdvice.setVisibility(View.GONE);
		tvAdvice.setText("暂无健康建议");
		tvState.setVisibility(View.GONE);
		tvStateMsg.setVisibility(View.GONE);
		ivState.setVisibility(View.GONE);
		//		btnHelpNoDevice.setVisibility(View.VISIBLE);
		btnHelpNoDevice.setVisibility(View.GONE);
		if (strTitle.equals("血压")) {
			tvState.setVisibility(View.VISIBLE);
			tvStateMsg.setVisibility(View.VISIBLE);
			ivState.setVisibility(View.VISIBLE);
			btnHelpNoDevice.setVisibility(View.GONE);
			tvState.setText("未连接");
			setBloodNameUiTip();
			//tvStateMsg.setText("正在扫描设备，请稍等");
			tvState.setTextColor(getResources().getColor(R.color.measure_red));
			ivState.setBackgroundResource(R.drawable.xx);
			layoutReady.setVisibility(View.VISIBLE);
			btnStartMeasure.setEnabled(true);
			btnStartMeasure.setBackgroundResource(R.drawable.btn_mrdevice_selector);
			tvTitle.setText("血压测量");
			layoutXueYa.setVisibility(View.VISIBLE);
			tvDevice.setText("血压计");
			tvValueMaiBoXueYa.setText("0");
			tvValueGaoYaXueYa.setText("0");
			tvValueDiYaXueya.setText("0");
			imgXueYa.setImageResource(R.drawable.xueya_ready);
			tvSignXueYa.setText("正常高压（收缩压）：90-130mmHg\n正常低压（舒张压）：60-85mmHg");
			//initBluetooth();//设备测量初始化
		}

	}
	private void setBloodNameUiTip() {
		ChangeDeviceInfo cdInfo = spInstance.getObj(SP_BLOOD_SELECT_XUEYA, null);
		if(cdInfo != null){
			bloodName = cdInfo.getDeviceName();
			cId = cdInfo.getConnectId();
			if(bloodName == null || bloodName.length() == 0){
				//tvStateMsg.setText("请选择默认测量设备");
			}else{
				tvStateMsg.setText("请打开"+bloodName);
			}
		}

		if(TextUtils.isEmpty(bloodName)){
			tvStateMsg.setVisibility(View.VISIBLE);
			tvStateMsg.setTextColor(getResources().getColor(R.color.measure_red));
			tvStateMsg.setText("请选择默认测量设备");
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
		linear = (LinearLayout) findViewById(R.id.blood_title_name);
		layoutAdvice = (RelativeLayout) findViewById(R.id.mearesult_advice_layout_blood);
		tvDevice = (TextView) findViewById(R.id.mearesult_tv_title_blood);
		tvAdvice = (EditText) findViewById(R.id.mearesult_advice_blood);
		tvState = (TextView) findViewById(R.id.mearesult_tv_state_blood);
		tvStateMsg=(TextView) findViewById(R.id.mearesult_tv_state_msg_blood);
		ivState = (ImageView) findViewById(R.id.mearesult_iv_state_blood);
		layoutReady = (RelativeLayout) findViewById(R.id.mearesult_ready_layout_blood);
		btnAddReady = (Button) findViewById(R.id.mearesult_ready_btn_blood);
		btnAddReady.setOnClickListener(this);
		btnStartMeasure = (Button) findViewById(R.id.mearesult_ready_btnstart_blood);
		btnStartMeasure.setOnClickListener(this);
		btnRestartMeasure=(Button) findViewById(R.id.mearesult_ready_btnRestart_blood);
		btnRestartMeasure.setOnClickListener(this);
		btnHelpIsDevice = (Button) findViewById(R.id.mearesult_ready_btnhelp_blood);
		btnHelpIsDevice.setOnClickListener(this);
		btnHelpNoDevice= (Button) findViewById(R.id.mearesult_btn_help_blood);
		btnHelpNoDevice.setOnClickListener(this);
		// 血压
		layoutXueYa = (LinearLayout) findViewById(R.id.mearesult_layout_xueya_blood);
		imgXueYa = (ImageView) findViewById(R.id.mearesult_img_xueya_blood);
		tvValueMaiBoXueYa = (TextView) findViewById(R.id.mearesult_value_maibo_xueya_blood);
		tvValueGaoYaXueYa = (TextView) findViewById(R.id.mearesult_value_gaoya_xueya_blood);
		tvValueDiYaXueya = (TextView) findViewById(R.id.mearesult_value_diya_xueya_blood);
		tvSignXueYa = (TextView) findViewById(R.id.mearesult_sign_xueya_blood);
	}
	public boolean openBluetooth() {
		boolean isBlueToothEnable =  BluetoothUtils.isBluetoothDeviceOpen();
		if(!isBlueToothEnable){
			//Toast.makeText(this, "设备测量需要使用蓝牙，正在为您打开蓝牙", Toast.LENGTH_SHORT).show();
			return BluetoothUtils.enableBluetoothDevice();
		}else{
			return true;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("血压测量结果页"); // 统计页面
		MobclickAgent.onResume(this);
	}
	public void changeDeviceAction(){
		ChangeDeviceInfo cdInfo = spInstance.getObj(SP_BLOOD_SELECT_XUEYA, null);
		setBloodNameUiTip();
		List<ChangeDeviceInfo> cdInfos = spInstance.getObj(Constant.SP_BLOOD_ALL, null);
		if( !strTitle.equals("体重") && !strTitle.equals("血糖")){
			tvStateMsg.setText("请打开"+bloodName);
			tvState.setTextColor(getResources().getColor(R.color.measure_red));
			ivState.setBackgroundResource(R.drawable.xx);
			btnStartMeasure.setEnabled(true);
			btnStartMeasure.setBackgroundResource(R.drawable.btn_mrdevice_selector);
		}

		if(cdInfos != null && cdInfos.size()!= 0 && cdInfo != null){
			for(int i = 0 ; i< cdInfos.size(); i++){
				ChangeDeviceInfo blood = cdInfos.get(i);
				if(cdInfo.getDeviceName().equals(blood.getDeviceName())){
					if(blood.isFisrt()){
						//Toast.makeText(this, "已选择的"+bloodName+"是第一次使用111", Toast.LENGTH_SHORT).show();
						blood.setFisrt(false);
						spInstance.putObj(Constant.SP_BLOOD_ALL,cdInfos);
						Intent intent = new Intent(this,MaskingDeviceActivity.class);
						intent.putExtra("bloodName", bloodName);
						startActivity(intent);
					}else{
						//Toast.makeText(this, "已选择的"+bloodName+"不是第一次使用222", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (MiaoApplication.getMiaoHealthManager() == null) return;
		MiaoApplication.getMiaoHealthManager().disConnectAll();
		bloodName = null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.mearesult_ready_btnstart_blood:
				//设备测量
				if(!CommonUtil.isEnableBluetooth()){
					registerBlutetoothChangeForOnce();
					CommonUtil.openBluetooth();
				}else{
					if(BluetoothUtils.isBluetoothDeviceOpen()){
						connectDevice();
//						connectAndMeasure();
						return;
					}else{
						openBT();
					}
				}
			/*if(bloodName != null && bloodName.length() != 0){
				if(!CommonUtil.isEnableBluetooth()){
					registerBlutetoothChangeForOnce();
					CommonUtil.openBluetooth();
				}else{
					if(BluetoothUtils.isBluetoothDeviceOpen()){
						ChangeDeviceInfo changeDeviceInfo = spInstance.getObj(SP_BLOOD_SELECT,null);
						if(changeDeviceInfo != null) getCurrentData(changeDeviceInfo);
//						connectAndMeasure();
						return;
					}else{
						openBT();
					}
				}
			}else{
				FRToast.showToast(MeasureBloodResultActivity.this.getApplicationContext(),"未选择默认设备，无法进行测量。请先到【设备切换】页选择设备");
			}*/

				break;
			case R.id.titlebar_btn_change:
				//设备切换
				new AsyncTask<Void, Void, Void>(){
					protected void onPreExecute() {
						progressDialog = new ProgressDialog(MeasureBloodResultActivity.this);
						Constant.showProgressDialog(progressDialog);
					}
					@Override
					protected Void doInBackground(Void... arg0) {
//					SystemClock.sleep(2000);
						return null;
					}
					protected void onPostExecute(Void result) {
						Constant.exitProgressDialog(progressDialog);
						initStartData();
						Intent intent4 = new Intent(MeasureBloodResultActivity.this,ChangeDeviceListActivity.class);
						intent4.putExtra("changeDevice", "blood");
						intent4.setFlags(device_xueya);
						startActivityForResult(intent4, ChangeDeviceListActivity.request_change_device_list_activity);
					};
				}.executeOnExecutor(Executors.newCachedThreadPool());
				break;
			case R.id.mearesult_ready_btnRestart_blood:
				//重新测量
				//bManager.disConnect(cId);
				linear.setVisibility(View.VISIBLE);
				initStartData();
				//btnStartMeasure.callOnClick();
				break;
			case R.id.mearesult_ready_btnhelp_blood:
				// 设备帮助
				if(!isConnented){
					if(strTitle.equals("血压")){
						Intent intent=new Intent(MeasureBloodResultActivity.this,SheBeiHelpActivity.class);
						intent.putExtra("deviceName", bloodName);
						intent.putExtra("strTitle", strTitle);
						startActivity(intent);
					}else{
						Toast.makeText(this, "暂无设备帮助！", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(this, "请等待测量完成", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.mearesult_ready_btn_blood:
				//没有设备时点击手动录入
				Intent intent2 = new Intent(MeasureBloodResultActivity.this,
						InputInfoActivity.class);
				intent2.putExtra("strTitle", strTitle);
				intent2.putExtra("memberName", memberName);
				startActivityForResult(intent2, INPUT_CODE);
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
			}else{
				System.out.println("未返回测量血压的数据");
			}
		}else if(requestCode == Constant.OPEN_CODE){
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "蓝牙已经开启", Toast.LENGTH_SHORT).show();
				connectDevice();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "不允许蓝牙开启,无法进行测量", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == ChangeDeviceListActivity.request_change_device_list_activity){//选择完设备之后开始连接设备
				connectDevice();
//				changeDeviceAction();
				//btnStartMeasure.callOnClick();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	//妙健康的接入
	private void connectDevice() {
		ChangeDeviceInfo changeDeviceInfo = spInstance.getObj(SP_BLOOD_SELECT_XUEYA,null);
		if(changeDeviceInfo != null){
			getCurrentData(changeDeviceInfo);
		}else {
			FRToast.showToast(this.getApplicationContext(),"未选择默认设备，无法进行测量。请先到【设备切换】页选择设备");
		}
	}

	public void getCurrentData( ChangeDeviceInfo changeDeviceInfo){
		boolean needDialog = false;
		int functional_id = 0;
		//目前手环和体脂称需要弹框输入个人信息
		/*for (int i = 0; i < changeDeviceInfo.getFunction_info().size(); i++) {
			functional_id = changeDeviceInfo.getFunction_info().get(i).getFunctional_id();
			if ( functional_id == 7 || functional_id == 1 || functional_id == 9) {
				needDialog = true;
			}

		}
		if (needDialog) {
			//初始化身高数据
			if (StringUtil.isEmpty(userBirthday)) {
				userBirthday = "1995-01-01";
			}
			showUserInfoDialog(functional_id);
		} else {
			getBleDeviceData(deviceId, device_sn, device_no,null);
		}*/
		CommonUtil.getBleDeviceData(changeDeviceInfo,null,handler);

	}



	private void fresh(String jsonStr, int meal) {
		layoutAdvice.setVisibility(View.VISIBLE);
		layoutReady.setVisibility(View.GONE);
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
				case 3:
					// 血压
					double diastolicPressure = model.optDouble("diastolicPressure");
					double systolicPressure = model.optDouble("systolicPressure");
					double pulse = model.optDouble("pulse");
					tvValueMaiBoXueYa.setText(pulse + "");
					tvValueGaoYaXueYa.setText(systolicPressure + "");
					tvValueDiYaXueya.setText(diastolicPressure + "");
					initShowXueYa(diastolicPressure, systolicPressure);
					linear.setVisibility(View.INVISIBLE);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设备测量
	 */
	//结果
	private void shebeiResult(ShebeiMeaInfo info) {
		layoutAdvice.setVisibility(View.VISIBLE);
		layoutReady.setVisibility(View.GONE);
		tvAdvice.setText("正在获取健康建议，请稍后...");
		if (strTitle.equals("血压")) {
			tvValueMaiBoXueYa.setText(info.getPulse() + "");
			tvValueGaoYaXueYa.setText(info.getSystolicPressure() + "");
			tvValueDiYaXueya.setText(info.getDiastolicPressure() + "");
			initShowXueYa(info.getDiastolicPressure(), info.getSystolicPressure());
		}
	}
	private void initShowXueYa(double diya, double gaoya) {
		//修改图标丢失
		if (gaoya > 90 && gaoya < 120 && diya > 60 && diya < 80) {
			// 理想
			imgXueYa.setImageResource(R.drawable.xueya_lxxy);
		} else if (gaoya >= 90 && gaoya <= 130 && diya >= 60 && diya <=85) {
			// 正常
			imgXueYa.setImageResource(R.drawable.xueya_normal);
		} else if (gaoya < 90 || diya < 60) {
			// 血压低
			imgXueYa.setImageResource(R.drawable.xueya_low);
		} else if ((gaoya > 130 && gaoya <= 139) || (diya > 85 && diya <=89))  {
			// 血压偏高
			imgXueYa.setImageResource(R.drawable.xueya_pianhigh);
		} else if (gaoya >139 || diya >89) {
			// 血压高
			imgXueYa.setImageResource(R.drawable.xueya_hign);
		} else{
			System.out.println("error");
		}
	}
	private void setDateToSelectDevice() {
		// TODO Auto-generated method stub
		List<ChangeDeviceInfo> infos = spInstance.getObj(Constant.SP_BLOOD_ALL, null);
		if(strTitle.equals("血压") && infos != null){
			for(ChangeDeviceInfo in : infos){
				if(in.getIsSelect() == 1){
					in.setLastTime(new Date());
				}
			}
		}
		spInstance.putObj(Constant.SP_BLOOD_ALL, infos);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

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

	//打开蓝牙
	public void openBT(){
		// 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
		// 那么将会收到RESULT_OK的结果，
		// 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
		Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(mIntent, Constant.OPEN_CODE);
		// 用enable()方法来开启，无需询问用户(实惠无声息的开启蓝牙设备),这时就需要用到android.permission.BLUETOOTH_ADMIN权限。
		// mBluetoothAdapter.enable();
		// mBluetoothAdapter.disable();//关闭蓝牙
	}



	/*public void selectDeviceDialog(){
		try {
			dInfo = spInstance.getObj(Constant.SP_BLOOD_SELECT, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			dInfo = null;
			e.printStackTrace();
		}

		if(dInfo != null){
			String deviceName = dInfo.getDeviceName();
			System.out.println("已有的默认设备==="+deviceName);
			//TODO
			bloodName = deviceName;

		}else {
			System.out.println("没有默认测量设备");
			YesOrNoDialog.Builder builder = new YesOrNoDialog.Builder(this);
			builder.setTitle("请先选择测量设备");
			builder.setMessage("是否跳转到支持设备页面？");
			builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					//showFisrtRun();
				}
			});
			builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(MeasureBloodResultActivity.this,ChangeDeviceListActivity.class);
					startActivity(intent);
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	}*/
}

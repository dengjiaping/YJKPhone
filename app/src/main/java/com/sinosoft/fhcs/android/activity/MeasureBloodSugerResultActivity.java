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
import java.util.concurrent.Executors;

import cn.miao.lib.MiaoApplication;
import cn.miao.lib.model.BloodGlucoseBean;

import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_XUETANG;
import static com.sinosoft.fhcs.android.util.Constant.device_result;
import static com.sinosoft.fhcs.android.util.Constant.device_xuetang;

public class MeasureBloodSugerResultActivity extends BaseActivity implements OnClickListener{

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
	private LinearLayout layoutNodevice;
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

	// 通用
	private String memberName = "";
	private String userId = "";
	private String strTitle = "体脂";

	private Button btnXueTangMenu;
	private ProgressDialog progressDialog;
	private LinearLayout linear;


	BroadcastReceiver bluetoothState;
	private SPUtil spInstance;
	private Button btnChange;


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
					btnXueTangMenu.setEnabled(false);
					btnInputMenu.setEnabled(false);
					break;
				case 1:
					btnInputMenu.setEnabled(false);
					tvState.setText("已连接");
					tvStateMsg.setText("");
					tvState.setTextColor(getResources().getColor(R.color.measure_green));
					ivState.setBackgroundResource(R.drawable.vv);
					btnXueTangMenu.setEnabled(false);
					btnXueTangMenu.setBackgroundResource(R.drawable.ready_measure3);
					break;
				case 2:
					btnInputMenu.setEnabled(false);
					tvState.setText("正在测量");
					tvStateMsg.setText("");
					tvState.setTextColor(getResources().getColor(R.color.measure_green));
					ivState.setBackgroundResource(R.drawable.vv);
					btnXueTangMenu.setEnabled(false);
					btnXueTangMenu.setBackgroundResource(R.drawable.ready_measure3);
					break;
				case 3:
					/*if(mActivity != null){
						FRToast.showToast(MeasureBloodResultActivity.this.getApplicationContext(),"连接失败");
					}*/
					btnInputMenu.setEnabled(true);
					btnXueTangMenu.setEnabled(true);
					tvStateMsg.setText("");
					tvState.setTextColor(getResources().getColor(R.color.measure_red));
					btnXueTangMenu.setBackgroundResource(R.drawable.btn_mrdevice_selector);
					tvState.setText("连接失败，请重试");
					ivState.setBackgroundResource(R.drawable.xx);
//					initStartData();
					break;
				case 5:
					tvState.setText("设备已断开连接");
					tvState.setTextColor(getResources().getColor(R.color.measure_red));
					ivState.setBackgroundResource(R.drawable.xx);
					btnXueTangMenu.setEnabled(true);
					btnXueTangMenu.setBackgroundResource(R.drawable.btn_mrdevice_selector);
					btnInputMenu.setEnabled(true);
					break;
				case device_result:
					BloodGlucoseBean bloodGlucoseBean = (BloodGlucoseBean) msg.obj;
//					BloodPressureBean bloodPressureBean = JsonUtils.fromJson(msg.obj.toString(), BloodPressureBean.class);
					if(bloodGlucoseBean != null) {
						tvState.setText("测量完成");
						layoutNodevice.setVisibility(View.GONE);
						btnInputMenu.setEnabled(true);
						btnXueTangMenu.setEnabled(true);
						tvState.setTextColor(getResources().getColor(R.color.measure_green));
						Calendar calendar = Calendar.getInstance();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String currentTime = simpleDateFormat.format(calendar.getTime());
						String measureTime = currentTime.replaceAll(" ", "%20");
						float glucose_value = bloodGlucoseBean.getGlucose_value();
						ShebeiMeaInfo shebei=new ShebeiMeaInfo(measureTime, 0,  0,0, 0 ,glucose_value, 0, 0, 0, 0, 0);
						System.out.println("血糖测量结果"+Util.FloatTodouble(glucose_value));
						tvState.setTextColor(getResources().getColor(R.color.measure_green));
						tvState.setText("测量完成");
						shebeiResult(shebei);
						initReguest(shebei);
					}else{
						Toast.makeText(MeasureBloodSugerResultActivity.this, "测量数据为空", 0).show();
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
		setContentView(R.layout.activity_measure_blood_suger_result);
		spInstance = SPUtil.getInstance(this.getApplicationContext());
		ExitApplicaton.getInstance().addActivity(this);
		strTitle = this.getIntent().getExtras().getString("strTitle");
		memberName = this.getIntent().getExtras().getString("memberName");
		// 从首选项获取机顶盒编号
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();
		initStartData();
		showFisrtRun();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("血糖测量界面"); // 统计页面
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("血糖测量结果页"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);
	}

	protected void showFisrtRun() {
		// TODO Auto-generated method stub
		boolean appIsFisrstRun = Util.appIsFisrstRun(getApplicationContext(),Constant.APP_IS_FIRSR_RUN_XUETANG);
		if(appIsFisrstRun){
			//Toast.makeText(this,"血糖页面   是  第一次运行", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this,MaskingActivity.class);
			intent.putExtra("strTitle", strTitle);
			startActivity(intent);
		}else{
			//Toast.makeText(this,"血糖页面   不是  第一次运行", Toast.LENGTH_SHORT).show();
		}
	}

	private void initStartData() {
		// TODO Auto-generated method stub
		layoutAdvice.setVisibility(View.GONE);
		tvAdvice.setText("暂无健康建议");
		tvState.setVisibility(View.GONE);
		tvStateMsg.setVisibility(View.GONE);
		ivState.setVisibility(View.GONE);
		if (strTitle.equals("血糖")) {
			tvState.setVisibility(View.VISIBLE);
			tvStateMsg.setVisibility(View.VISIBLE);
			ivState.setVisibility(View.VISIBLE);
			tvState.setText("未连接");
			tvStateMsg.setText("请打开血糖仪");
			tvState.setTextColor(getResources().getColor(R.color.measure_red));
			ivState.setBackgroundResource(R.drawable.xx);
			tvTitle.setText("血糖测量");
			btnXueTangMenu.setEnabled(true);
			btnXueTangMenu.setBackgroundResource(R.drawable.btn_mrdevice_selector);
			layoutWeight.setVisibility(View.VISIBLE);
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




	private void init() {
		// TODO Auto-generated method stub
		// 标题栏
		btnChange = (Button) findViewById(R.id.titlebar_btn_change);
		btnChange.setVisibility(View.VISIBLE);
		btnChange.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		// 通用
		linear = (LinearLayout) findViewById(R.id.blood_suger_title_name);
		layoutAdvice = (RelativeLayout) findViewById(R.id.mearesult_advice_layout_suger);
		tvDevice = (TextView) findViewById(R.id.mearesult_tv_title_suger);
		tvAdvice = (EditText) findViewById(R.id.mearesult_advice_suger);
		tvState = (TextView) findViewById(R.id.mearesult_tv_state_suger);
		tvStateMsg=(TextView) findViewById(R.id.mearesult_tv_state_msg_suger);
		ivState = (ImageView) findViewById(R.id.mearesult_iv_state_suger);
		btnRestartMeasure=(Button) findViewById(R.id.mearesult_ready_btnRestart_suger);
		btnRestartMeasure.setOnClickListener(this);
		btnHelpNoDevice= (Button) findViewById(R.id.mearesult_btn_help_suger);
		btnHelpNoDevice.setOnClickListener(this);
		layoutNodevice=(LinearLayout) findViewById(R.id.mearesult_nodevice_layout_suger);
		btnInputMenu=(Button) findViewById(R.id.mearesult_nodevice_btn_suger);
		btnInputMenu.setOnClickListener(this);

		//血糖
		btnXueTangMenu=(Button) findViewById(R.id.mearesult_device_btn_suger);
		btnXueTangMenu.setOnClickListener(this);

		// 体重、血糖、体温
		layoutWeight = (LinearLayout) findViewById(R.id.mearesult_layout_weight_suger);
		imgWeight = (ImageView) findViewById(R.id.mearesult_img_weight_suger);
		tvNameWeight = (TextView) findViewById(R.id.mearesult_name_weight_suger);
		tvValueWeight = (TextView) findViewById(R.id.mearesult_value_weight_suger);
		tvUnitWeight = (TextView) findViewById(R.id.mearesult_unit_weight_suger);
		tvSignWeight = (TextView) findViewById(R.id.mearesult_sign_weight_suger);

	}





	public void openBluetooth() {
		boolean isBlueToothEnable =  BluetoothUtils.isBluetoothDeviceOpen();
		if(!isBlueToothEnable){
			Toast.makeText(this, "正在打开蓝牙设备", Toast.LENGTH_SHORT).show();
			BluetoothUtils.enableBluetoothDevice();
		}
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
						connectAndMeasure();
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.mearesult_device_btn_suger:
				//血糖仪设备测量
				//设备测量
				if(!CommonUtil.isEnableBluetooth()){
					registerBlutetoothChangeForOnce();
					CommonUtil.openBluetooth();
				}else{
					if(BluetoothUtils.isBluetoothDeviceOpen()){
//					connectAndMeasure();
						connectDevice();
						return;
					}else{
						CommonUtil.openBluetooth();
					}
				}
				break;
			case R.id.titlebar_btn_change:
				//设备切换
				new AsyncTask<Void, Void, Void>(){
					protected void onPreExecute() {
						progressDialog = new ProgressDialog(MeasureBloodSugerResultActivity.this);
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
						Intent intent4 = new Intent(MeasureBloodSugerResultActivity.this,ChangeDeviceListActivity.class);
						intent4.putExtra("changeDevice", "blood");
						intent4.setFlags(device_xuetang);
						startActivityForResult(intent4, ChangeDeviceListActivity.request_change_device_list_activity);
					};
				}.executeOnExecutor(Executors.newCachedThreadPool());
				break;
			case R.id.mearesult_nodevice_btn_suger:
				//没有设备时点击手动录入
				Intent intent2 = new Intent(MeasureBloodSugerResultActivity.this,
						InputInfoActivity.class);
				intent2.putExtra("strTitle", strTitle);
				intent2.putExtra("memberName", memberName);
				startActivityForResult(intent2, INPUT_CODE);
				break;
			case R.id.mearesult_ready_btnRestart_suger:
				//重新测量
				linear.setVisibility(View.VISIBLE);
				initStartData();
				break;
			case R.id.mearesult_btn_help_suger:
				//帮助
				Intent intent=new Intent(MeasureBloodSugerResultActivity.this,SheBeiHelpActivity.class);
				intent.putExtra("strTitle", strTitle);
				startActivity(intent);
				break;

		}
	}


	public void connectAndMeasure() {
		tvState.setText("正在连接设备，请稍等...");
		tvStateMsg.setText("");
		tvState.setTextColor(getResources().getColor(R.color.measure_green));
		ivState.setBackground(null);
		progressDialog = new ProgressDialog(this);
		Constant.showProgressDialog(progressDialog);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == INPUT_CODE) {
			if (data != null) {
				String jsonStr = data.getStringExtra("Json");
				int meal = data.getExtras().getInt("xuetang");
				fresh(jsonStr, meal);
			}
		}else if(requestCode == Constant.OPEN_CODE){
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "蓝牙已经开启", Toast.LENGTH_SHORT).show();
//				connectAndMeasure();
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
		ChangeDeviceInfo changeDeviceInfo = spInstance.getObj(SP_BLOOD_SELECT_XUETANG,null);
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
		CommonUtil.getBleDeviceData(changeDeviceInfo,null,handler);

	}

	private void fresh(String jsonStr, int meal) {
		layoutAdvice.setVisibility(View.VISIBLE);
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
				case 4:
					// 血糖
					double bloodGlucose = model.optDouble("bloodGlucose");
					tvValueWeight.setText(bloodGlucose + "");
					initShowXueTang(bloodGlucose, meal);
					linear.setVisibility(View.INVISIBLE);
					break;
				default:
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
		tvAdvice.setText("正在获取健康建议，请稍后...");
		if(strTitle.equals("血糖")){
			tvValueWeight.setText(info.getBloodGlucose()+"");
			initShowXueTang(info.getBloodGlucose(),10);
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





	/*//打开蓝牙
	public void openBT(){
		// 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
		// 那么将会收到RESULT_OK的结果，
		// 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
		Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(mIntent,Constant.OPEN_CODE);
		// 用enable()方法来开启，无需询问用户(实惠无声息的开启蓝牙设备),这时就需要用到android.permission.BLUETOOTH_ADMIN权限。
		// mBluetoothAdapter.enable();
		// mBluetoothAdapter.disable();//关闭蓝牙
	}*/


}

package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:健康档案曲线图页
 * @Author: wangshuangshuang.
 * @Create: 2015年3月18日.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.entity.BalanceDevice;
import com.sinosoft.fhcs.android.entity.BloodGlucoseDevice;
import com.sinosoft.fhcs.android.entity.BloodPressureDevice;
import com.sinosoft.fhcs.android.entity.FatMonitorDevice;
import com.sinosoft.fhcs.android.entity.PedometerDevice;
import com.sinosoft.fhcs.android.entity.TemperatureDevice;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.sinosoft.fhcs.android.util.Util;
import com.umeng.analytics.MobclickAgent;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HealthRecordActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private Button btnMenuRight;// 右菜单
	// 标题
	private ImageView titleImg;
	private TextView titleTvBig;
	private LinearLayout titleLayout;
	public static TextView titleTvLittle;
	// 时间
	private RadioGroup rgTime;
	private static RadioButton rbtn_time_one;// 最近六次，最近一周，最近一个月，全部
	private static RadioButton rbtn_time_two;
	private static RadioButton rbtn_time_three;
	private static RadioButton rbtn_time_all;
	// 结果
	private TextView tvDate, tvCount;// 时间，结果

	private int deviceId = 0;
	public int typeZFY = 1;// 1-4脂肪率，水分率，肌肉量，内脏脂肪等级
	public int typeXYJ = 1;// 1-2低压高压，脉搏
	public int typeJBQ = 1;// 1-3步数，距离，卡路里

	// 折线图
	private LinearLayout layout;
	private GraphicalView mChartView;
	private XYMultipleSeriesRenderer mRenderer;
	// 请求数据
	private ProgressDialog progressDialog;// 进度条
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private static final int FailNoData = 1004;// 没有数据
	private int PF = 1000;
	private List<BalanceDevice> list1 = new ArrayList<BalanceDevice>();
	private List<FatMonitorDevice> list2 = new ArrayList<FatMonitorDevice>();
	private List<BloodPressureDevice> list3 = new ArrayList<BloodPressureDevice>();
	private List<BloodGlucoseDevice> list4 = new ArrayList<BloodGlucoseDevice>();
	private List<TemperatureDevice> list5 = new ArrayList<TemperatureDevice>();
	private List<PedometerDevice> list6 = new ArrayList<PedometerDevice>();
	private String strRoleName = "";// 角色名
	private String userId = "";// 用户id
	private int count = 6;// count = 7 最近一周；count = 1 最近一个月； count = 6
	// 最近6次；count = 2; 全部

	private TextView hrecord_tv_date2;
	private LinearLayout hrecord_layout_value1, hrecord_layout_value2;
	private TextView hrecord_tv_countgaoya, hrecord_tv_countdiya;
	private Button btn_screenshot;//截屏发送
	private File file;
	private int flags = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_healthrecord);
		ExitApplicaton.getInstance().addActivity(this);
		strRoleName = this.getIntent().getExtras().getString("roleName");
		deviceId = this.getIntent().getExtras().getInt("deviceId");
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();
		initTitle(deviceId);
		initClick();
		// initRequest();
	}

	private void initClick() {
		rgTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (group.getCheckedRadioButtonId()) {
					case R.id.hrecord_time_one:
						// 最近6次
						count = 6;
						initRequest();
						break;
					case R.id.hrecord_time_two:
						// 最近一周
						count = 7;
						initRequest();
						break;
					case R.id.hrecord_time_three:
						// 最近一个月
						count = 1;
						initRequest();
						break;
					case R.id.hrecord_time_all:
						// 全部
						count = 2;
						initRequest();
						break;
				}

			}
		});

	}

	private void init() {
		try {
			flags = getIntent().getFlags();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flags = -1;
		}
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		btnMenuRight = (Button) findViewById(R.id.titlebar_btn_xiala);
		btnMenuRight.setVisibility(View.VISIBLE);
		btnMenuRight.setOnClickListener(this);
		titleImg = (ImageView) findViewById(R.id.hr_iv_icon);
		titleTvBig = (TextView) findViewById(R.id.hr_tv_titlebig);
		titleLayout = (LinearLayout) findViewById(R.id.hr_layout_title);
		titleTvLittle = (TextView) findViewById(R.id.hr_tv_titlelittle);
		titleLayout.setOnClickListener(this);
		// 图表
		layout = (LinearLayout) findViewById(R.id.chart);
		// 时间
		rgTime = (RadioGroup) findViewById(R.id.hrecord_time_rg);
		rbtn_time_one = (RadioButton) findViewById(R.id.hrecord_time_one);
		rbtn_time_two = (RadioButton) findViewById(R.id.hrecord_time_two);
		rbtn_time_three = (RadioButton) findViewById(R.id.hrecord_time_three);
		rbtn_time_all = (RadioButton) findViewById(R.id.hrecord_time_all);
		// 结果
		tvDate = (TextView) findViewById(R.id.hrecord_tv_date);
		tvCount = (TextView) findViewById(R.id.hrecord_tv_count);
		// 血压
		hrecord_tv_date2 = (TextView) findViewById(R.id.hrecord_tv_date2);
		hrecord_tv_countgaoya = (TextView) findViewById(R.id.hrecord_tv_countgaoya);
		hrecord_tv_countdiya = (TextView) findViewById(R.id.hrecord_tv_countdiya);
		hrecord_layout_value1 = (LinearLayout) findViewById(R.id.hrecord_layout_value1);
		hrecord_layout_value2 = (LinearLayout) findViewById(R.id.hrecord_layout_value2);

		btn_screenshot = (Button) findViewById(R.id.btn_screenshot);
		btn_screenshot.setOnClickListener(this);
		if(flags == Constant.department_erke){
			btn_screenshot.setVisibility(View.VISIBLE);
		}else{
			btn_screenshot.setVisibility(View.GONE);
		}

	}

	private void initTitle(int typeId) {
		deviceId = typeId;
		if (deviceId == 4000103 && typeXYJ == 1) {
			hrecord_layout_value1.setVisibility(View.VISIBLE);
			hrecord_layout_value2.setVisibility(View.VISIBLE);
			hrecord_tv_date2.setVisibility(View.VISIBLE);
			tvDate.setVisibility(View.GONE);
			tvCount.setVisibility(View.GONE);
			hrecord_tv_date2.setText("");
			hrecord_tv_countgaoya.setText("");
			hrecord_tv_countdiya.setText("");
		} else {
			hrecord_layout_value1.setVisibility(View.GONE);
			hrecord_layout_value2.setVisibility(View.GONE);
			hrecord_tv_date2.setVisibility(View.GONE);
			tvDate.setVisibility(View.VISIBLE);
			tvCount.setVisibility(View.VISIBLE);
			tvDate.setText("");
			tvCount.setText("");
		}
		if (!rbtn_time_one.isChecked()) {
			rbtn_time_one.setChecked(true);
			rbtn_time_two.setChecked(false);
			rbtn_time_three.setChecked(false);
			rbtn_time_all.setChecked(false);
		} else {
			count = 6;
			initRequest();
		}
		switch (typeId) {
			// 健康称 4000101脂肪仪 4000102 血压计 4000103 血糖仪
			// 4000104 耳温枪 4000105 计步器 4000106
			case 4000101:
				tvTitle.setText("体重历史记录");
				titleImg.setBackgroundResource(R.drawable.hr_icon_jkc);
				titleLayout.setVisibility(View.GONE);
				titleTvBig.setText("健康秤");
				break;
			case 4000102:
				tvTitle.setText("体脂历史记录");
				titleImg.setBackgroundResource(R.drawable.hr_icon_zfy);
				titleLayout.setVisibility(View.VISIBLE);
				titleTvBig.setText("脂肪仪");
				titleTvLittle.setText("脂肪率");
				typeZFY = 1;
				break;
			case 4000104:
				tvTitle.setText("血糖历史记录");
				titleImg.setBackgroundResource(R.drawable.hr_icon_xty);
				titleLayout.setVisibility(View.GONE);
				titleTvBig.setText("血糖仪");
				break;
			case 4000103:
				tvTitle.setText("血压历史记录");
				titleImg.setBackgroundResource(R.drawable.hr_icon_xyj);
				titleLayout.setVisibility(View.VISIBLE);
				titleTvBig.setText("血压计");
				titleTvLittle.setText("低压高压");
				typeXYJ = 1;
				break;
			case 4000105:
				tvTitle.setText("体温历史记录");
				titleImg.setBackgroundResource(R.drawable.hr_icon_ewq);
				titleLayout.setVisibility(View.GONE);
				titleTvBig.setText("体温计");
				break;
			case 4000106:
				tvTitle.setText("运动历史记录");
				titleImg.setBackgroundResource(R.drawable.hr_icon_jbq);
				titleLayout.setVisibility(View.VISIBLE);
				titleTvBig.setText("手环");
				titleTvLittle.setText("步数");
				typeJBQ = 1;
				break;
			default:
				break;
		}

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("健康档案页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("健康档案页"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				finish();
				break;
			case R.id.titlebar_btn_xiala:
				// 设备菜单
				HrTypeMenuPop menuWindow = new HrTypeMenuPop(this);
				menuWindow.showAtLocation(btnMenuRight,
						Gravity.TOP | Gravity.RIGHT, 0, 0);
				break;
			case R.id.hr_layout_title:
				// 类型菜单
				HrTypeLittleMenuPop menuWindow2 = new HrTypeLittleMenuPop(this,
						deviceId);
				menuWindow2.showAsDropDown(titleTvLittle);
				break;
			case R.id.btn_screenshot:
				// 截屏发送
				btn_screenshot.setVisibility(View.GONE);
				File getandSaveCurrentImage = GetandSaveCurrentImage();
				Log.i("截屏图片路径==",getandSaveCurrentImage.getPath());
				Intent intent = new Intent();
				intent.putExtra("getandSaveCurrentImage", getandSaveCurrentImage);
				setResult(Constant.Json_Request_Onetask, intent);
				finish();
				break;
			default:
				break;
		}

	}

	private void initStartChartView(int goal, int type) {
		int[] colors;
		if (goal == 2 && type == 1) {
			colors = new int[] { getResources().getColor(R.color.view_k_line4),
					getResources().getColor(R.color.view_k_line3),
					getResources().getColor(R.color.view_k_line2) };
		} else {
			colors = new int[] { getResources().getColor(R.color.view_k_line),
					getResources().getColor(R.color.view_k_line2),
					getResources().getColor(R.color.view_k_line2) };
		}

		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,
				PointStyle.CIRCLE, PointStyle.CIRCLE };

		mRenderer = Util.buildRenderer(colors, styles, goal);
		int length = mRenderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) mRenderer.getSeriesRendererAt(i))
					.setFillPoints(true);
		}
	}

	private void initView(int type) {
		if (deviceId == 4000101 || deviceId == 4000104 || deviceId == 4000102) {
			initStartChartView(0, type);
		} else if (deviceId == 4000103) {
			initStartChartView(2, type);
		} else {
			initStartChartView(1, type);
		}
		if (deviceId == 4000103 && typeXYJ == 1) {
			hrecord_layout_value1.setVisibility(View.VISIBLE);
			hrecord_layout_value2.setVisibility(View.VISIBLE);
			hrecord_tv_date2.setVisibility(View.VISIBLE);
			tvDate.setVisibility(View.GONE);
			tvCount.setVisibility(View.GONE);
			hrecord_tv_date2.setText("");
			hrecord_tv_countgaoya.setText("");
			hrecord_tv_countdiya.setText("");
		} else {
			hrecord_layout_value1.setVisibility(View.GONE);
			hrecord_layout_value2.setVisibility(View.GONE);
			hrecord_tv_date2.setVisibility(View.GONE);
			tvDate.setVisibility(View.VISIBLE);
			tvCount.setVisibility(View.VISIBLE);
			tvDate.setText("");
			tvCount.setText("");
		}
		switch (deviceId) {
			case 4000101:
				// 健康称 4000101
				Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 10, 100,
						getResources().getColor(R.color.view_grid_line),
						getResources().getColor(R.color.view_xy_text));
				break;
			case 4000102:
				// 脂肪仪 4000102
				// 1-4脂肪率，水分率，肌肉量，内脏脂肪等级
				switch (typeZFY) {
					case 1:
						Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 0, 80,
								getResources().getColor(R.color.view_grid_line),
								getResources().getColor(R.color.view_xy_text));
						break;
					case 2:
						Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 0, 100,
								getResources().getColor(R.color.view_grid_line),
								getResources().getColor(R.color.view_xy_text));
						break;
					case 3:
						Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 10, 100,
								getResources().getColor(R.color.view_grid_line),
								getResources().getColor(R.color.view_xy_text));
						break;
					case 4:
						Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 0, 10,
								getResources().getColor(R.color.view_grid_line),
								getResources().getColor(R.color.view_xy_text));
						break;
				}
				break;
			case 4000103:
				// 血压计 4000103
				// 1-2低压高压，脉搏
				switch (typeXYJ) {
					case 1:
						Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 10, 220,
								getResources().getColor(R.color.view_grid_line),
								getResources().getColor(R.color.view_xy_text));
						break;
					case 2:
						Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 10, 110,
								getResources().getColor(R.color.view_grid_line),
								getResources().getColor(R.color.view_xy_text));
						break;
				}
				break;
			case 4000104:
				// 血糖仪4000104
				Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 0, 10,
						getResources().getColor(R.color.view_grid_line),
						getResources().getColor(R.color.view_xy_text));
				break;
			case 4000105:
				// 耳温枪 4000105
				Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 10, 60,
						getResources().getColor(R.color.view_grid_line),
						getResources().getColor(R.color.view_xy_text));
				break;
			case 4000106:
				// 计步器 4000106
				// 1-3步数，距离，卡路里
				switch (typeJBQ) {
					case 1:
						Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 1000,
								60000, getResources().getColor(R.color.view_grid_line),
								getResources().getColor(R.color.view_xy_text));
						break;
					case 2:
						Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 100,
								20000, getResources().getColor(R.color.view_grid_line),
								getResources().getColor(R.color.view_xy_text));
						break;
					case 3:
						Util.setChartSettings(mRenderer, "", "", "", -0.4, 5, 100,
								50000, getResources().getColor(R.color.view_grid_line),
								getResources().getColor(R.color.view_xy_text));
						break;
				}
				break;
		}
		mRenderer = Util.setRenderer(mRenderer, 10, 0);
	}

	// 设置数据
	private XYMultipleSeriesDataset buildDataset(String[] titles,
												 List<List<String>> xValues, List<List<Double>> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		addXYSeries(dataset, titles, xValues, yValues, 0);
		return dataset;
	}

	private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
							 List<List<String>> xValues, List<List<Double>> yValues, int scale) {
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i], scale);
			List<String> xV = xValues.get(i);
			List<Double> yV = yValues.get(i);
			int seriesLength = yV.size();
			for (int k = 0; k < seriesLength; k++) {
				series.add(k, yV.get(k));
				mRenderer.addTextLabel(k, xV.get(k));
			}
			dataset.addSeries(series);
		}
	}

	private void initChartView() {
		if (deviceId == 4000103 && typeXYJ == 1) {
			initView(1);
		} else {
			initView(0);
		}

		List<String> dates0 = new ArrayList<String>();
		List<String> dates1 = new ArrayList<String>();
		List<List<String>> dateAll = new ArrayList<List<String>>();
		final List<String> dates2 = new ArrayList<String>();
		final List<String> dates3 = new ArrayList<String>();
		final List<Double> values1 = new ArrayList<Double>();
		final List<Double> values2 = new ArrayList<Double>();
		final List<Double> values3 = new ArrayList<Double>();
		List<List<Double>> valueAll = new ArrayList<List<Double>>();
		String[] titles = new String[] { "高压", "低压", "目标" };
		// 健康称 4000101脂肪仪 4000102 血压计 4000103 血糖仪 4000104 耳温枪 4000105 计步器
		// 4000106
		switch (deviceId) {
			case 4000101:
				for (int i = 0; i < list1.size(); i++) {
					BalanceDevice device1 = list1.get(i);
					dates0.add(Constant.getViewDate(device1.getMeasureTime()));
					dates2.add(Constant.getDateFormat4(device1.getMeasureTime()));
					values1.add(device1.getWeight());
					if(device1.getGoalWeight()!=0){
						dates1.add(Constant.getViewDate(device1.getMeasureTime()));
						values2.add(device1.getGoalWeight());
					}

				}
				break;
			case 4000102:
				// 1-4脂肪率，水分率，肌肉量，内脏脂肪等级
				for (int i = 0; i < list2.size(); i++) {
					FatMonitorDevice device2 = list2.get(i);
					dates0.add(Constant.getViewDate(device2.getMeasureTime()));
					dates2.add(Constant.getDateFormat4(device2.getMeasureTime()));
					switch (typeZFY) {
						case 1:
							values1.add(device2.getFatPercentage());
							if(device2.getGoalFatPercentage()!=0){
								dates1.add(Constant.getViewDate(device2.getMeasureTime()));
								values2.add(device2.getGoalFatPercentage());
							}
							break;
						case 2:
							values1.add(device2.getMoistureRate());
							break;
						case 3:
							values1.add(device2.getMuscleVolume());
							break;
						case 4:
							values1.add(device2.getVisceralFatRating());
							break;
					}
				}
				break;
			case 4000103:
				// 1-3低压，高压，脉搏
				for (int i = 0; i < list3.size(); i++) {
					BloodPressureDevice device3 = list3.get(i);
					dates0.add(Constant.getViewDate(device3.getMeasureTime()));
					dates1.add(Constant.getViewDate(device3.getMeasureTime()));
					dates2.add(Constant.getDateFormat4(device3.getMeasureTime()));
					switch (typeXYJ) {
						case 1:
							values1.add(device3.getDiastolicPressure());
							values2.add(device3.getSystolicPressure());
							if(device3.getGoalSystolicPressure()!=0){
								dates3.add(Constant.getViewDate(device3.getMeasureTime()));
								values3.add(device3.getGoalSystolicPressure());
							}
							break;
						case 2:
							values1.add(device3.getPulse());
							break;
					}
				}
				break;
			case 4000104:
				for (int i = 0; i < list4.size(); i++) {
					BloodGlucoseDevice device4 = list4.get(i);
					dates0.add(Constant.getViewDate(device4.getMeasureTime()));
					dates2.add(Constant.getDateFormat4(device4.getMeasureTime()));
					values1.add(device4.getBloodGlucose());
					if(device4.getGoalBloodGlucose()!=0){
						dates1.add(Constant.getViewDate(device4.getMeasureTime()));
						values2.add(device4.getGoalBloodGlucose());
					}
				}
				break;
			case 4000105:
				for (int i = 0; i < list5.size(); i++) {
					TemperatureDevice device5 = list5.get(i);
					dates0.add(Constant.getViewDate(device5.getMeasureTime()));
					dates2.add(Constant.getDateFormat4(device5.getMeasureTime()));
					values1.add(device5.getTemperature());
				}
				break;
			case 4000106:
				// 1-3步数，距离，卡路里
				for (int i = 0; i < list6.size(); i++) {
					PedometerDevice device6 = (PedometerDevice) list6.get(i);
					dates0.add(Constant.getViewDate(device6.getMeasureTime()));
					dates2.add(Constant.getDateFormat4(device6.getMeasureTime()));
					switch (typeJBQ) {
						case 1:
							values1.add(Double.valueOf(device6.getStepNum()));
							break;
						case 2:
							values1.add(device6.getDistance());
							break;
						case 3:
							values1.add(device6.getCalorie());
							break;
					}
				}
				break;
		}
		dateAll.add(dates0);
		dateAll.add(dates1);
		dateAll.add(dates3);
		valueAll.add(values1);
		valueAll.add(values2);
		valueAll.add(values3);
//		 整数显示：内脏脂肪等级，高压，低压，脉搏
		if (deviceId == 4000103 && typeXYJ == 1) {
			if (dates2.size() != 0) {
				hrecord_tv_date2.setText(dates2.get(dates2.size() - 1) + "");
			} else {
				hrecord_tv_date2.setText("");
			}
			if (values1.size() != 0) {
				double temp=values1.get(values1.size() - 1);
				hrecord_tv_countdiya.setText((int)temp+"");
			} else {
				hrecord_tv_countdiya.setText("");
			}
			if (values2.size() != 0) {
				double temp=values2.get(values2.size() - 1);
				hrecord_tv_countgaoya.setText((int)temp
						+ "");
			} else {
				hrecord_tv_countgaoya.setText("");
			}
		} else {
			if (dates2.size() != 0) {
				tvDate.setText(dates2.get(dates2.size() - 1) + "");
			} else {
				tvDate.setText("");
			}
			if (values1.size() != 0) {
				if ((deviceId == 4000103 && typeXYJ == 2)||(deviceId==4000102&&typeZFY==4)){
					double temp=values1.get(values1.size() - 1);
					tvCount.setText((int)temp + "");
				}else{
					tvCount.setText(values1.get(values1.size() - 1) + "");
				}

			} else {
				tvCount.setText("");
			}
		}

		layout.removeAllViews();
		mChartView = ChartFactory.getLineChartView(this,
				buildDataset(titles, dateAll, valueAll), mRenderer);
		mChartView.setBackgroundColor(Color.TRANSPARENT);
		mRenderer.setClickEnabled(true);// 设置图表是否允许点击
		// 设置点的缓冲半径值(在某点附件点击时,多大范围内都算点击这个点)
		mRenderer.setSelectableBuffer(10);
		mChartView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 这段代码处理点击一个点后,获得所点击的点在哪个序列中以及点的坐标.
				// -->start

				SeriesSelection seriesSelection = mChartView
						.getCurrentSeriesAndPoint();
				// double[] xy = mChartView.toRealPoint(0);
				if (seriesSelection == null) {
					// Toast.makeText(HealthRecordActivity.this, "没有图表元素被点击",
					// Toast.LENGTH_SHORT).show();
				} else {
					// Toast.makeText(
					// HealthRecordActivity.this,
					// "图表元素系列指数 " + seriesSelection.getSeriesIndex()
					// + " 数据点指数 "
					// + seriesSelection.getPointIndex() + " 被点击了"
					// + " 最近点值X=" + seriesSelection.getXValue()
					// + ", 最近点值Y=" + seriesSelection.getValue()
					// + " 点击点值X=" + (float) xy[0] + ", 点击点值Y="
					// + (float) xy[1], Toast.LENGTH_SHORT).show();
					int x = (int) seriesSelection.getXValue();
					double y = seriesSelection.getValue();
//					 整数显示：内脏脂肪等级，高压，低压，脉搏
					if (deviceId == 4000103 && typeXYJ == 1) {
						hrecord_tv_date2.setText(dates2.get(x) + "");
						double temp1=values2.get(x);
						double temp2=values1.get(x);
						hrecord_tv_countgaoya.setText((int)temp1+ "");
						hrecord_tv_countdiya.setText((int)temp2+ "");
					} else {
						if ((deviceId == 4000103 && typeXYJ == 2)||(deviceId==4000102&&typeZFY==4)){
							tvDate.setText(dates2.get(x) + "");
							tvCount.setText((int)y + "");
						}else{
							tvDate.setText(dates2.get(x) + "");
							tvCount.setText(y + "");
						}

					}

				}
			}
			// -->end

		});

		layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

	}

	// 请求数据
	private void initRequest() {
		if (!HttpManager.isNetworkAvailable(this)) {
			// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (count == 7) {
			GetRecordRequest re = new GetRecordRequest();
			re.execute(HttpManager.urlHealthRecord(userId, strRoleName,
					Constant.getDay(count), deviceId));
		} else if (count == 6) {
			GetRecordRequest re = new GetRecordRequest();
			re.execute(HttpManager.urlHealthRecord2(userId, strRoleName,
					deviceId));
		} else {
			GetRecordRequest re = new GetRecordRequest();
			re.execute(HttpManager.urlHealthRecord(userId, strRoleName,
					Constant.getDate(count), deviceId));
		}

	}

	// 请求结果
	private void initResult() {
		if (PF == FailServer) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			Toast.makeText(this, "获取失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			// Toast.makeText(this, "获取成功！", Toast.LENGTH_SHORT).show();
			initChartView();
		} else if (PF == FailNoData) {
			layout.removeAllViews();
			tvDate.setText("");
			tvCount.setText("");
			Toast.makeText(this, "暂无测量数据!", Toast.LENGTH_SHORT).show();
		}
	}

	// 请求
	private class GetRecordRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(HealthRecordActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getRecordUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResult();
			} else {
				try {
					JSONObject jo1 = new JSONObject(result);
					String resultCode = jo1.optString("resultCode");
					if (resultCode.equals("1")) {
						JSONObject jo2 = jo1.getJSONObject("data");
						Double goalWeight = jo2.optDouble("goalWeight");
						Double goalBloodGlucose = jo2
								.optDouble("goalBloodGlucose");
						Double goalSystolicPressure = jo2
								.optDouble("goalSystolicPressure");
						Double goalFatPercentage = jo2
								.optDouble("goalFatPercentage");
						JSONArray ja = jo2.getJSONArray("Data");
						if (ja.length() == 0) {
							PF = FailNoData;
							initResult();
						} else {
							list1.clear();
							list2.clear();
							list3.clear();
							list4.clear();
							list5.clear();
							list6.clear();
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo = ja.getJSONObject(i);
								switch (deviceId) {
									case 4000101:
										String measureTime = jo
												.optString("measureTime");
										Double weight = jo.optDouble("weight");
										BalanceDevice device = new BalanceDevice(
												weight, goalWeight, measureTime);
										list1.add(device);
										break;
									case 4000102:
										String measureTime2 = jo
												.optString("measureTime");
										Double fatPercentage = jo
												.optDouble("fatPercentage");
										Double moistureRate = jo
												.optDouble("moistureRate");
										Double muscleVolume = jo
												.optDouble("muscleVolume");
										Double visceralFatRating = jo
												.optDouble("visceralFatRating");
										FatMonitorDevice device2 = new FatMonitorDevice(
												measureTime2, fatPercentage,
												visceralFatRating, moistureRate,
												muscleVolume, goalFatPercentage);
										list2.add(device2);
										break;
									case 4000103:
										String measureTime3 = jo
												.optString("measureTime");
										Double diastolicPressure = jo
												.optDouble("diastolicPressure");
										Double systolicPressure = jo
												.optDouble("systolicPressure");
										Double pulse = jo.optDouble("pulse");
										BloodPressureDevice device3 = new BloodPressureDevice(
												measureTime3, pulse,
												diastolicPressure,
												systolicPressure,
												goalSystolicPressure);
										list3.add(device3);
										break;
									case 4000104:
										String measureTime4 = jo
												.optString("measureTime");
										Double bloodGlucose = jo
												.optDouble("bloodGlucose");
										BloodGlucoseDevice device4 = new BloodGlucoseDevice(
												measureTime4, bloodGlucose,
												goalBloodGlucose);
										list4.add(device4);
										break;
									case 4000105:
										String measureTime5 = jo
												.optString("measureTime");
										Double temperature = jo
												.optDouble("temperature");
										TemperatureDevice device5 = new TemperatureDevice(
												measureTime5, temperature);
										list5.add(device5);
										break;
									case 4000106:
										String measureTime6 = jo
												.optString("measureTime");
										Double distance = jo.optDouble("distance");
										int stepNum = jo.optInt("stepNum");
										Double calorie = jo.optDouble("calorie");
										PedometerDevice device6 = new PedometerDevice(
												stepNum, distance, calorie,
												measureTime6);
										list6.add(device6);
										break;

								}

							}

							PF = OK;
							initResult();
						}
					} else {
						PF = Fail;
						initResult();
					}
				} catch (JSONException e) {
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

	public class HrTypeMenuPop extends PopupWindow implements OnClickListener {

		private View mMenuView;
		private Activity mContext;
		private TextView rbtnJkc, rbtnZfy, rbtnEwq, rbtnXyj, rbtnXty, rbtnJbq;// 健康秤，脂肪仪，耳温枪，血压计，血糖仪，计步器；

		public HrTypeMenuPop(Activity context) {
			super(context);
			this.mContext = context;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.pop_hr, null);
			findId();
			this.setContentView(mMenuView);
			this.setWidth(LayoutParams.WRAP_CONTENT);
			this.setHeight(LayoutParams.WRAP_CONTENT);
			this.setFocusable(true);
			ColorDrawable dw = new ColorDrawable(0x00000000);
			this.setBackgroundDrawable(dw);
			mMenuView.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					dismiss();
					return true;
				}
			});

		}

		private void findId() {
			rbtnJkc = (TextView) mMenuView.findViewById(R.id.hrtype_jkc);
			rbtnZfy = (TextView) mMenuView.findViewById(R.id.hrtype_zfy);
			rbtnXty = (TextView) mMenuView.findViewById(R.id.hrtype_xty);
			rbtnXyj = (TextView) mMenuView.findViewById(R.id.hrtype_xyj);
			rbtnEwq = (TextView) mMenuView.findViewById(R.id.hrtype_ewq);
			rbtnJbq = (TextView) mMenuView.findViewById(R.id.hrtype_jbq);
//			rbtnJbq.setVisibility(View.GONE);
			rbtnJkc.setOnClickListener(this);
			rbtnZfy.setOnClickListener(this);
			rbtnXty.setOnClickListener(this);
			rbtnXyj.setOnClickListener(this);
			rbtnEwq.setOnClickListener(this);
			rbtnJbq.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			// 健康称 4000101脂肪仪 4000102 血压计 4000103 血糖仪
			// 4000104 耳温枪 4000105 计步器 4000106
			switch (v.getId()) {
				case R.id.hrtype_jkc:
					initTitle(4000101);
					dismiss();
					break;
				case R.id.hrtype_zfy:
					initTitle(4000102);
					dismiss();
					break;
				case R.id.hrtype_xty:
					initTitle(4000104);
					dismiss();
					break;
				case R.id.hrtype_xyj:
					initTitle(4000103);
					dismiss();
					break;
				case R.id.hrtype_ewq:
					initTitle(4000105);
					dismiss();
					break;
				case R.id.hrtype_jbq:
					initTitle(4000106);
					dismiss();
					break;
				default:
					dismiss();
					break;
			}

		}
	}

	public class HrTypeLittleMenuPop extends PopupWindow implements
			OnClickListener {

		private View mMenuView;
		private Activity mContext;
		private TextView tv1, tv2, tv3, tv4;
		private int deviceId = 4000101;// 健康称 4000101脂肪仪 4000102 血压计 4000103 血糖仪
		// 4000104 耳温枪 4000105 计步器4000106

		public HrTypeLittleMenuPop(Activity context, int deviceId) {
			super(context);
			this.mContext = context;
			this.deviceId = deviceId;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.pop_hr_little, null);
			findId();
			this.setContentView(mMenuView);
			this.setWidth(LayoutParams.WRAP_CONTENT);
			this.setHeight(LayoutParams.WRAP_CONTENT);
			this.setFocusable(true);
			ColorDrawable dw = new ColorDrawable(0x00000000);
			this.setBackgroundDrawable(dw);
			mMenuView.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					dismiss();
					return true;
				}
			});

		}

		private void findId() {
			tv1 = (TextView) mMenuView.findViewById(R.id.hrtype_little1);
			tv2 = (TextView) mMenuView.findViewById(R.id.hrtype_little2);
			tv3 = (TextView) mMenuView.findViewById(R.id.hrtype_little3);
			tv4 = (TextView) mMenuView.findViewById(R.id.hrtype_little4);
			switch (deviceId) {
				case 4000102:
					// 脂肪仪
					tv1.setText(Constant.type1[0]);
					tv2.setText(Constant.type1[1]);
					tv3.setText(Constant.type1[2]);
					tv4.setText(Constant.type1[3]);
					tv4.setVisibility(View.VISIBLE);
					break;
				case 4000103:
					// 血压计
					tv1.setText(Constant.type2[0]);
					tv2.setText(Constant.type2[1]);
					tv3.setVisibility(View.GONE);
					tv4.setVisibility(View.GONE);
					break;
				case 4000106:
					// 计步器
					tv1.setText(Constant.type3[0]);
					tv2.setText(Constant.type3[1]);
					tv3.setText(Constant.type3[2]);
					tv4.setVisibility(View.GONE);
					break;

				default:
					break;
			}
			tv1.setOnClickListener(this);
			tv2.setOnClickListener(this);
			tv3.setOnClickListener(this);
			tv4.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.hrtype_little1:
					HealthRecordActivity.titleTvLittle.setText(tv1.getText()
							.toString());
					if (deviceId == 4000102) {
						// 1-4脂肪率，水分率，肌肉量，内脏脂肪等级
						typeZFY = 1;
					} else if (deviceId == 4000103) {
						// 1-2低压，高压，脉搏
						typeXYJ = 1;
					} else {
						// 1-3步数，距离，卡路里
						typeJBQ = 1;
					}
					initLittleType();
					dismiss();
					break;
				case R.id.hrtype_little2:
					HealthRecordActivity.titleTvLittle.setText(tv2.getText()
							.toString());
					if (deviceId == 4000102) {
						// 1-4脂肪率，水分率，肌肉量，内脏脂肪等级
						typeZFY = 2;
					} else if (deviceId == 4000103) {
						// 1-2低压，高压，脉搏
						typeXYJ = 2;
					} else {
						// 1-3步数，距离，卡路里
						typeJBQ = 2;
					}
					initLittleType();
					dismiss();
					break;
				case R.id.hrtype_little3:
					HealthRecordActivity.titleTvLittle.setText(tv3.getText()
							.toString());
					if (deviceId == 4000102) {
						// 1-4脂肪率，水分率，肌肉量，内脏脂肪等级
						typeZFY = 3;
					} else {
						// 1-3步数，距离，卡路里
						typeJBQ = 3;
					}
					initLittleType();
					dismiss();
					break;
				case R.id.hrtype_little4:
					HealthRecordActivity.titleTvLittle.setText(tv4.getText()
							.toString());
					if (deviceId == 4000102) {
						// 1-4脂肪率，水分率，肌肉量，内脏脂肪等级
						typeZFY = 4;
					}
					initLittleType();
					dismiss();
					break;
				default:
					dismiss();
					break;
			}

		}

	}

	private void initLittleType() {
		if (PF == OK) {
			initChartView();
		} else {
			Toast.makeText(HealthRecordActivity.this, "该设备没有测量数据!",
					Toast.LENGTH_SHORT).show();
		}

	}


	/**
	 * 获取和保存当前屏幕的截图
	 */
	private File GetandSaveCurrentImage()
	{
		//1.构建Bitmap
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();

		Bitmap Bmp = Bitmap.createBitmap( w, h, Config.ARGB_8888 );

		//2.获取屏幕
		View decorview = this.getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		Bmp = decorview.getDrawingCache();

		String SavePath = getSDCardPath()+"/AndyDemo/ScreenImage";

		//3.保存Bitmap
		try {
			File path = new File(SavePath);
			//文件
			String filepath = SavePath + System.currentTimeMillis() + ".png";
			file = new File(filepath);
			if(!path.exists()){
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
				//Toast.makeText(this, "截屏文件已保存至SDCard/AndyDemo/ScreenImage/下", Toast.LENGTH_LONG).show();
			}
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 获取SDCard的目录路径功能
	 * @return
	 */
	private String getSDCardPath(){
		File sdcardDir = null;
		//判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(sdcardExist){
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}
}

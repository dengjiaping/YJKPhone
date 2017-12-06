package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:手动录入页
 * @Author: wangshuangshuang.
 * @Create: 2015年2月25日.
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InputInfoActivity extends BaseActivity implements OnClickListener {
	public static int INPUT_CODE=1000;
	private TextView tvTitle;
	private Button btnBack;// 返回
	public static Button btnDate;// 日期
	public static Button btnSleepTime;// 入睡时间
	public static Button btnWakeTime;// 醒来时间
	private Calendar calendar;
	private Button btnMeal;// 餐前餐后
	private LinearLayout layoutWeight, layoutTiZhi, layoutTemperature,
			layoutSleep, layoutXueTang, layoutXueYa, layoutTime;// 体重，体脂，体温，睡眠，血糖，血压,测量时间
	private EditText edtWeight, edtXueTang, edtTemperature, edtSFL, edtZFL,
			edtNZZF, edtJRl, edtDiYa, edtGaoYa, edtMaiLv;// 体重，血糖，体温,水分率，脂肪率，内脏脂肪等级，肌肉量，低压，高压，脉率
	private Button btnSure;// 确定
	private String userId = "";
	private String memberName = "";
	private String strTitle = "体脂";
	// 网络请求
	private ProgressDialog progressDialog;// 进度条
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private int PF = 1000;
	private String jsonStr = "";
	private int beforOrAfter = 1;// 1 空腹血糖, 2 早餐后2小时血糖, 3 午餐前血糖, 4 午餐后2小时血糖, 5
	// 晚餐前血糖, 6 晚餐后2小时血糖, 7 睡前血糖
	private String currentTime;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_inputinfo);
		ExitApplicaton.getInstance().addActivity(this);
		strTitle = this.getIntent().getExtras().getString("strTitle");
		memberName = this.getIntent().getExtras().getString("memberName");
		// 从首选项获取机顶盒编号
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();
		initEditText();
		showView(strTitle);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("健康录入页"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("健康录入页");
	}

	private void init() {
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		if (strTitle.equals("体重")) {
			tvTitle.setText(getResources().getString(R.string.title_input1));
		} else if (strTitle.equals("体脂")) {
			tvTitle.setText(getResources().getString(R.string.title_input2));
		} else if (strTitle.equals("体温")) {
			tvTitle.setText(getResources().getString(R.string.title_input3));
		} else if (strTitle.equals("睡眠")) {
			tvTitle.setText(getResources().getString(R.string.title_input4));
		} else if (strTitle.equals("血糖")) {
			tvTitle.setText(getResources().getString(R.string.title_input5));
		} else if (strTitle.equals("血压")) {
			tvTitle.setText(getResources().getString(R.string.title_input6));
		}
		// 日期
		btnDate = (Button) findViewById(R.id.input_btn_date);
		btnDate.setOnClickListener(this);
		// 确定
		btnSure = (Button) findViewById(R.id.input_btn_sure);
		btnSure.setOnClickListener(this);
		// edittext
		edtWeight = (EditText) findViewById(R.id.input_edt_weight);
		edtXueTang = (EditText) findViewById(R.id.input_edt_xuetang);
		edtTemperature = (EditText) findViewById(R.id.input_edt_temperature);
		edtSFL = (EditText) findViewById(R.id.input_edt_shuifenlv);
		edtZFL = (EditText) findViewById(R.id.input_edt_zhifanglv);
		edtNZZF = (EditText) findViewById(R.id.input_edt_neizang);
		edtJRl = (EditText) findViewById(R.id.input_edt_jirouliang);
		edtDiYa = (EditText) findViewById(R.id.input_edt_diya);
		edtGaoYa = (EditText) findViewById(R.id.input_edt_gaoya);
		edtMaiLv = (EditText) findViewById(R.id.input_edt_mailv);
		// layout
		layoutWeight = (LinearLayout) findViewById(R.id.input_layout_weight);
		layoutTiZhi = (LinearLayout) findViewById(R.id.input_layout_tizhi);
		layoutTemperature = (LinearLayout) findViewById(R.id.input_layout_temperature);
		layoutSleep = (LinearLayout) findViewById(R.id.input_layout_sleep);
		layoutXueTang = (LinearLayout) findViewById(R.id.input_layout_xuetang);
		layoutXueYa = (LinearLayout) findViewById(R.id.input_layout_xueya);
		layoutTime = (LinearLayout) findViewById(R.id.input_layout_time);
		// 入睡时间
		btnSleepTime = (Button) findViewById(R.id.input_btn_sleeptime);
		btnSleepTime.setOnClickListener(this);
		// 醒来时间
		btnWakeTime = (Button) findViewById(R.id.input_btn_waketime);
		btnWakeTime.setOnClickListener(this);
		// 餐前餐后
		btnMeal = (Button) findViewById(R.id.input_btn_meal);
		btnMeal.setOnClickListener(this);
		btnMeal.setText("请选择");
	}

	@SuppressLint("SimpleDateFormat")
	private void updateDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		currentTime=simpleDateFormat.format(calendar.getTime());
		btnDate.setText(simpleDateFormat.format(calendar.getTime()));
		btnSleepTime.setText(simpleDateFormat.format(calendar.getTime()));
		btnWakeTime.setText(simpleDateFormat.format(calendar.getTime()));
	}

	private void initEditText() {
		edtWeight.setText("");
		edtXueTang.setText("");
		edtTemperature.setText("");
		edtSFL.setText("");
		edtZFL.setText("");
		edtNZZF.setText("");
		edtJRl.setText("");
		edtDiYa.setText("");
		edtGaoYa.setText("");
		edtMaiLv.setText("");
		btnMeal.setText("请选择");
		calendar = Calendar.getInstance();
		updateDate();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.input_btn_date:
				// 日期
				Constant.showTime(this, btnDate, btnDate.getText().toString(),
						"测量时间");
				break;
			case R.id.input_btn_sleeptime:
				// 入睡时间
				Constant.showTime(this, btnSleepTime, btnSleepTime.getText()
						.toString(), "入睡时间");
				break;
			case R.id.input_btn_waketime:
				// 醒来时间
				Constant.showTime(this, btnWakeTime, btnWakeTime.getText()
						.toString(), "醒来时间");
				break;
			case R.id.input_btn_sure:
				// 确定
				if (!HttpManager.isNetworkAvailable(this)) {
					// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				String strSleep = btnSleepTime.getText().toString().trim();
				String strWake = btnWakeTime.getText().toString().trim();
				String strTest = btnDate.getText().toString().trim();
				String testTime = strTest.replaceAll(" ", "%20");
				String sleepTime = strSleep.replaceAll(" ", "%20");
				String wakeTime = strWake.replaceAll(" ", "%20");
				// if (btnMeal.getText().toString().trim().equals("餐后")) {
				// beforOrAfter = 1;
				// } else {
				// beforOrAfter = 0;
				// }
				beforOrAfter = Constant.putXueTangMeal(btnMeal.getText().toString()
						.trim());
				int result1 = currentTime.compareTo(btnDate.getText()
						.toString().trim());
				if (result1 < 0) {
					Toast.makeText(this, "测量时间不能大于当前时间！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				switch (Constant.putSheBei(strTitle.trim())) {
					// 4000101体重 4000102体脂 4000103血压 4000104血糖 4000105体温 4000107 睡眠
					case 4000101:
						// 体重
						if (edtWeight.getText().toString().trim().equals("")) {
							Toast.makeText(this, "体重不能为空！", Toast.LENGTH_SHORT).show();
							return;
						}
						if (edtWeight.getText().toString().trim().equals(".")) {
							Toast.makeText(this, "填写内容有误！", Toast.LENGTH_SHORT).show();
							return;
						}
						if (edtWeight.getText().toString().trim().contains(".")) {
							if (edtWeight.getText().toString().trim().split("\\.")[1]
									.length() > 1) {
								Toast.makeText(this, "体重值请保留一位小数！", Toast.LENGTH_SHORT)
										.show();
								return;
							}
						}
						if (Double.valueOf(edtWeight.getText().toString().trim()) > 150
								|| Double
								.valueOf(edtWeight.getText().toString().trim()) < 5) {
							Toast.makeText(this, "称重范围：5KG-150KG！", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						break;
					case 4000102:
						// 体脂
						if (edtSFL.getText().toString().trim().equals("")
								|| edtZFL.getText().toString().trim().equals("")
								|| edtNZZF.getText().toString().trim().equals("")
								|| edtJRl.getText().toString().trim().equals("")) {
							Toast.makeText(this, "设备参数不能为空！", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						if (edtSFL.getText().toString().trim().equals(".")
								|| edtZFL.getText().toString().trim().equals(".")
								|| edtNZZF.getText().toString().trim().equals(".")
								|| edtJRl.getText().toString().trim().equals(".")) {
							Toast.makeText(this, "填写内容有误！", Toast.LENGTH_SHORT).show();
							return;
						}
						if (edtZFL.getText().toString().trim().contains(".")) {
							if (edtZFL.getText().toString().trim().split("\\.")[1]
									.length() > 1) {
								Toast.makeText(this, "脂肪率值请保留一位小数！", Toast.LENGTH_SHORT)
										.show();
								return;
							}
						}
						if (edtSFL.getText().toString().trim().contains(".")) {
							if (edtSFL.getText().toString().trim().split("\\.")[1]
									.length() > 1) {
								Toast.makeText(this, "水分率值请保留一位小数！", Toast.LENGTH_SHORT)
										.show();
								return;
							}
						}
						if (edtJRl.getText().toString().trim().contains(".")) {
							if (edtJRl.getText().toString().trim().split("\\.")[1]
									.length() > 1) {
								Toast.makeText(this, "肌肉量值请保留一位小数！", Toast.LENGTH_SHORT)
										.show();
								return;
							}
						}
						if (Double.valueOf(edtZFL.getText().toString().trim()) > 100
								|| Double.valueOf(edtZFL.getText().toString().trim()) < 1) {
							Toast.makeText(this, "脂肪率范围：1%-100%！",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (Double.valueOf(edtNZZF.getText().toString().trim()) > 10
								|| Double.valueOf(edtNZZF.getText().toString().trim()) < 1) {
							Toast.makeText(this, "内脏脂肪等级范围：1-10级！", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						if (Double.valueOf(edtSFL.getText().toString().trim()) > 100
								|| Double.valueOf(edtSFL.getText().toString().trim()) <1) {
							Toast.makeText(this, "水分率范围：1%-100%！",
									Toast.LENGTH_SHORT).show();
							return;
						}

						if (Double.valueOf(edtJRl.getText().toString().trim()) > 200
								|| Double.valueOf(edtJRl.getText().toString().trim()) < 1) {
							Toast.makeText(this, "肌肉量范围：1-200公斤！",
									Toast.LENGTH_SHORT).show();
							return;
						}

						break;

					case 4000103:
						// 血压
						if (edtDiYa.getText().toString().trim().equals("")
								|| edtGaoYa.getText().toString().trim().equals("")
								|| edtMaiLv.getText().toString().trim().equals("")) {
							Toast.makeText(this, "设备参数不能为空！", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						if (edtDiYa.getText().toString().trim().equals(".")
								|| edtGaoYa.getText().toString().trim().equals(".")
								|| edtMaiLv.getText().toString().trim().equals(".")) {
							Toast.makeText(this, "填写内容有误！", Toast.LENGTH_SHORT).show();
							return;
						}
						if (Double.valueOf(edtDiYa.getText().toString().trim()) > 280
								|| Double.valueOf(edtDiYa.getText().toString().trim()) < 30
								|| Double.valueOf(edtGaoYa.getText().toString().trim()) > 280
								|| Double.valueOf(edtGaoYa.getText().toString().trim()) < 30) {
							Toast.makeText(this, "血压范围：30-280mmHg！", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						if (Double.valueOf(edtMaiLv.getText().toString().trim()) > 199
								|| Double.valueOf(edtMaiLv.getText().toString().trim()) < 40) {
							Toast.makeText(this, "脉搏范围：40-199pulse/min！",
									Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case 4000104:
						// 血糖
						if (edtXueTang.getText().toString().trim().equals("")) {
							Toast.makeText(this, "血糖值不能为空！", Toast.LENGTH_SHORT).show();
							return;
						}
						if (btnMeal.getText().toString().trim().equals("请选择")) {
							Toast.makeText(this, "请选择时段！", Toast.LENGTH_SHORT).show();
							return;
						}
						if (edtXueTang.getText().toString().trim().equals(".")) {
							Toast.makeText(this, "填写内容有误！", Toast.LENGTH_SHORT).show();
							return;
						}
						if (edtXueTang.getText().toString().trim().contains(".")) {
							if (edtXueTang.getText().toString().trim().split("\\.")[1]
									.length() > 1) {
								Toast.makeText(this, "血糖值请保留一位小数！", Toast.LENGTH_SHORT)
										.show();
								return;
							}
						}
						if (Double.valueOf(edtXueTang.getText().toString().trim()) > 30
								|| Double.valueOf(edtXueTang.getText().toString()
								.trim()) < 1) {
							Toast.makeText(this, "血糖范围：1-30mmol/L！",
									Toast.LENGTH_SHORT).show();
							return;
						}
						break;

					case 4000105:
						// 体温
						if (edtTemperature.getText().toString().trim().equals("")) {
							Toast.makeText(this, "体温不能为空！", Toast.LENGTH_SHORT).show();
							return;
						}
						if (edtTemperature.getText().toString().trim().equals(".")) {
							Toast.makeText(this, "填写内容有误！", Toast.LENGTH_SHORT).show();
							return;
						}
						if (edtTemperature.getText().toString().trim().contains(".")) {
							if (edtTemperature.getText().toString().trim().split("\\.")[1]
									.length() > 1) {
								Toast.makeText(this, "体温值请保留一位小数！", Toast.LENGTH_SHORT)
										.show();
								return;
							}
						}
						if (Double.valueOf(edtTemperature.getText().toString().trim()) > 49.9
								|| Double.valueOf(edtTemperature.getText().toString()
								.trim()) < 30) {
							Toast.makeText(this, "体温范围：30度-49.9度！", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						break;

					case 4000107:
						// 睡眠
						int result2 = wakeTime.compareTo(sleepTime);
						if (result2 <= 0) {
							Toast.makeText(this, "醒来时间必须大于入睡时间！", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						break;

					default:
						break;
				}
				AddRequest re = new AddRequest();
				re.execute(HttpManager.urlInputInfo(Constant.putSheBei(strTitle),
						userId, memberName, testTime, edtWeight.getText()
								.toString().trim(), edtXueTang.getText().toString()
								.trim(), "", "", "", edtTemperature.getText()
								.toString().trim(), edtDiYa.getText().toString()
								.trim(), edtGaoYa.getText().toString().trim(),
						edtMaiLv.getText().toString().trim(), edtSFL.getText()
								.toString().trim(), edtZFL.getText().toString()
								.trim(), edtNZZF.getText().toString().trim(),
						edtJRl.getText().toString().trim(), beforOrAfter,
						sleepTime, wakeTime));

				break;
			case R.id.input_btn_meal:
				// 餐前餐后
				createMealDialog();
				break;
			default:
				break;
		}

	}

	// 设置餐前餐后
	private void createMealDialog() {
		new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
				.setTitle("请选择")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setItems(Constant.InputByMeal,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								btnMeal.setText(Constant.InputByMeal[which]);
								dialog.dismiss();
							}
						}).create().show();
	}

	// 显示layout
	private void showView(String str) {
		if (str.equals("体重")) {
			layoutWeight.setVisibility(View.VISIBLE);
			layoutTiZhi.setVisibility(View.GONE);
			layoutTemperature.setVisibility(View.GONE);
			layoutSleep.setVisibility(View.GONE);
			layoutXueTang.setVisibility(View.GONE);
			layoutXueYa.setVisibility(View.GONE);
			layoutTime.setVisibility(View.VISIBLE);
		} else if (str.equals("体脂")) {
			layoutTiZhi.setVisibility(View.VISIBLE);
			layoutWeight.setVisibility(View.GONE);
			layoutTemperature.setVisibility(View.GONE);
			layoutSleep.setVisibility(View.GONE);
			layoutXueTang.setVisibility(View.GONE);
			layoutXueYa.setVisibility(View.GONE);
			layoutTime.setVisibility(View.VISIBLE);
		} else if (str.equals("体温")) {
			layoutTemperature.setVisibility(View.VISIBLE);
			layoutTiZhi.setVisibility(View.GONE);
			layoutWeight.setVisibility(View.GONE);
			layoutSleep.setVisibility(View.GONE);
			layoutXueTang.setVisibility(View.GONE);
			layoutXueYa.setVisibility(View.GONE);
			layoutTime.setVisibility(View.VISIBLE);
		} else if (str.equals("睡眠")) {
			layoutSleep.setVisibility(View.VISIBLE);
			layoutTiZhi.setVisibility(View.GONE);
			layoutTemperature.setVisibility(View.GONE);
			layoutWeight.setVisibility(View.GONE);
			layoutXueTang.setVisibility(View.GONE);
			layoutXueYa.setVisibility(View.GONE);
			layoutTime.setVisibility(View.GONE);
		} else if (str.equals("血糖")) {
			layoutXueTang.setVisibility(View.VISIBLE);
			layoutTiZhi.setVisibility(View.GONE);
			layoutTemperature.setVisibility(View.GONE);
			layoutSleep.setVisibility(View.GONE);
			layoutWeight.setVisibility(View.GONE);
			layoutXueYa.setVisibility(View.GONE);
			layoutTime.setVisibility(View.VISIBLE);
		} else if (str.equals("血压")) {
			layoutXueYa.setVisibility(View.VISIBLE);
			layoutTiZhi.setVisibility(View.GONE);
			layoutTemperature.setVisibility(View.GONE);
			layoutSleep.setVisibility(View.GONE);
			layoutWeight.setVisibility(View.GONE);
			layoutXueTang.setVisibility(View.GONE);
			layoutTime.setVisibility(View.VISIBLE);
		}

	}

	private class AddRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(InputInfoActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("AddInputInfoUrl", url + "");
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
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						jsonStr = jo.optString("data");
						PF = OK;
						initResult();
					} else {
						PF = Fail;
						initResult();
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = Fail;
					initResult();
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

	// 请求结果
	private void initResult() {
		if (PF == FailServer) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			Toast.makeText(this, "提交失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			Toast.makeText(this, "提交成功！", Toast.LENGTH_SHORT).show();
			Intent in = getIntent();
			in.putExtra("Json", jsonStr);
			in.putExtra("xuetang", beforOrAfter);
			setResult(INPUT_CODE, in);
			finish();
		}

	}
}

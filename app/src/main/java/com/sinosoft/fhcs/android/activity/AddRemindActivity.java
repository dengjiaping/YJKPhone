package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:添加服药提醒页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.SpinnerRoleNameAdapter;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class AddRemindActivity extends BaseActivity implements OnClickListener,OnItemSelectedListener {
	private TextView tvTitle;
	private Button btnBack;
	private RelativeLayout btnStartDate;
	public static TextView tvStartDate;
	private EditText edtCount;// 服用周期
	@SuppressWarnings("unused")
	private RadioGroup rGroup;
	private RadioButton rbtnDay, rbtnWeek;// 天，周
	@SuppressWarnings("unused")
	private RadioGroup rGroupMeal;
	private RadioButton rbtnCanQian, rbtnCanHou;// 餐前餐后
	private TextView tvTime;// 提醒时间
	private RelativeLayout btnTime;
	private String strTime = "";
	private String[] strTimes = new String[24];
	private boolean[] arraySelected = new boolean[24];
	private EditText edtMedicineName;// 药品名称
	private EditText edtDosage;// 剂量
	private CheckBox cbtnPhone, cbtnSms;// 手机客户端，短信
	private Button btnAdd;// 创建
	private Calendar calendar;
	private String userId = "";
	private String fmId = "";// 成员id
	private String currentTime = "";
	private Spinner spinRoleName;
	private List<Object>list=new ArrayList<Object>();
	// 请求数据
	private ProgressDialog progressDialog;// 进度条
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private int PF = 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_addremind);
		ExitApplicaton.getInstance().addActivity(this);
		list = (List<Object>) this.getIntent().getExtras().get("list");
		init();
		// 从首选项获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("添加提醒页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("添加提醒页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_addRemind));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		// 添加
		edtCount = (EditText) findViewById(R.id.addremind_count);
		rGroup = (RadioGroup) findViewById(R.id.addremind_radiogroup);
		rbtnDay = (RadioButton) findViewById(R.id.addremind_rbtn_day);
		rbtnWeek = (RadioButton) findViewById(R.id.addremind_rbtn_week);
		// 餐前餐后
		rGroupMeal = (RadioGroup) findViewById(R.id.addremind_rg_meal);
		rbtnCanQian = (RadioButton) findViewById(R.id.addremind_rbtn_meal1);
		rbtnCanHou = (RadioButton) findViewById(R.id.addremind_rbtn_meal2);
		edtMedicineName = (EditText) findViewById(R.id.addremind_medicineName);
		edtDosage = (EditText) findViewById(R.id.addremind_dosage);
		spinRoleName=(Spinner) findViewById(R.id.addremind_member);
		spinRoleName.setPrompt("请选择");
		SpinnerRoleNameAdapter adapter = new SpinnerRoleNameAdapter(
				AddRemindActivity.this, list);
		spinRoleName.setAdapter(adapter);
		spinRoleName.setOnItemSelectedListener(this);
		fmId="";
		// 开始日期
		btnStartDate = (RelativeLayout) findViewById(R.id.addremind_datestart);
		tvStartDate = (TextView) findViewById(R.id.addremind_tv_datestart);
		btnStartDate.setOnClickListener(this);
		calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		tvStartDate.setText(simpleDateFormat.format(calendar.getTime()));
		currentTime = simpleDateFormat.format(calendar.getTime());
		// 提醒时间
		btnTime = (RelativeLayout) findViewById(R.id.addremind_time);
		tvTime = (TextView) findViewById(R.id.addremind_tv_time);
		tvTime.setText("请选择");
		initTimes();
		btnTime.setOnClickListener(this);
		// 提醒方式
		cbtnPhone = (CheckBox) findViewById(R.id.addremind_cbtn_phone);
		cbtnSms = (CheckBox) findViewById(R.id.addremind_cbtn_sms);
		// 创建
		btnAdd = (Button) findViewById(R.id.addremind_btn_add);
		btnAdd.setOnClickListener(this);
	}

	// 返回
	private void goback() {
		if (edtCount.getText().toString().trim().equals("")
				&& (strTime.equals("请选择") || strTime.equals(""))
				&& edtMedicineName.getText().toString().trim().equals("")
				&& edtDosage.getText().toString().trim().equals("")
				&& !cbtnPhone.isChecked() && !cbtnSms.isChecked()) {
			finish();
		} else {
			Constant.showDialog2(this, "尚未完成添加，是否要离开本页面？");
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
	public void onItemSelected(AdapterView<?> parent, View view, int position,
							   long id) {
		fmId = ((FamilyMember) list.get(position)).getId() + "";
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				goback();
				break;
			case R.id.addremind_datestart:
				// 开始日期
				// new DatePickerDialog(AddRemindActivity.this, listener,
				// calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				// calendar.get(Calendar.DAY_OF_MONTH)).show();
				Constant.showDate(this, tvStartDate, tvStartDate.getText()
						.toString().trim(), "addRemind");
				break;
			case R.id.addremind_time:
				// 提醒时间
				createTimeDialog();
				break;
			case R.id.addremind_btn_add:
				// 创建
				if (!HttpManager.isNetworkAvailable(this)) {
					// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (tvStartDate.getText().toString().trim().equals("")
						|| edtCount.getText().toString().trim().equals("")
						|| strTime.equals("请选择") || strTime.equals("")
						|| edtMedicineName.getText().toString().trim().equals("")
						|| edtDosage.getText().toString().trim().equals("")
						|| fmId.equals("")) {
					Toast.makeText(AddRemindActivity.this, "所有的内容都是必填项，请填写！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				int result1 = currentTime.compareTo(tvStartDate.getText()
						.toString().trim());
				if (result1 > 0) {
					Toast.makeText(this, "服药时间不能小于当前时间！", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (!cbtnPhone.isChecked() && !cbtnSms.isChecked()) {
					Toast.makeText(AddRemindActivity.this, "请选择提醒方式！",
							Toast.LENGTH_SHORT).show();
					return;
				}

				String reminderWay = "";
				if (cbtnPhone.isChecked() & !cbtnSms.isChecked()) {
					reminderWay = "60001101";
				}
				if (cbtnSms.isChecked() && !cbtnPhone.isChecked()) {
					reminderWay = "60001102";
				}
				if (cbtnSms.isChecked() && cbtnPhone.isChecked()) {
					reminderWay = "60001101" + "," + "60001102";
				}
				int count = 0;
				if (rbtnDay.isChecked()) {
					count = Integer.parseInt(edtCount.getText().toString().trim());
				}
				if (rbtnWeek.isChecked()) {
					count = Integer.parseInt(edtCount.getText().toString().trim()) * 7;
				}
				// 餐前 60002101 //餐中 60002102 //餐后 60002103
				String meal = "60002101";
				if (rbtnCanQian.isChecked()) {
					meal = "60002101";
				}
				if (rbtnCanHou.isChecked()) {
					meal = "60002103";
				}
				String strEndDate = Constant.getCurrentDate(tvStartDate.getText()
						.toString().trim(), count);
				AddRequest re = new AddRequest();
				re.execute(HttpManager.urlAddRemind(userId, fmId, tvStartDate
								.getText().toString().trim(), strEndDate, strTime,
						edtMedicineName.getText().toString().trim(), edtDosage
								.getText().toString().trim(), reminderWay, meal));
				break;
			default:
				break;
		}

	}

	private void initTimes() {
		String temp = "";
		for (int i = 1; i < 25; i++) {
			if (i < 10) {
				temp = "0" + i;
			} else {
				temp = i + "";
			}
			strTimes[i - 1] = temp + ":00";
		}
		for (int i = 0; i < strTimes.length; i++) {
			arraySelected[i] = false;
		}
	}

	private void initTime2() {
		if (tvTime.getText().toString().trim().equals("")) {
			for (int i = 0; i < strTimes.length; i++) {
				arraySelected[i] = false;
			}
		} else {
			for (int i = 0; i < strTimes.length; i++) {
				arraySelected[i] = false;
			}
			String[] ary = tvTime.getText().toString().trim().split(",");
			for (int i = 0; i < ary.length; i++) {
				for (int j = 0; j < strTimes.length; j++) {
					if (ary[i].equals(strTimes[j])) {
						arraySelected[j] = true;
					}
				}
			}
		}

	}

	// 设置提醒时间
	private void createTimeDialog() {
		new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
				.setTitle("请选择")
				.setMultiChoiceItems(strTimes, arraySelected,
						new DialogInterface.OnMultiChoiceClickListener() {
							//
							@Override
							public void onClick(DialogInterface dialog,
												int which, boolean isChecked) {
								arraySelected[which] = isChecked;
							}
						})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						strTime = "";
						for (int i = 0; i < arraySelected.length; i++) {
							if (arraySelected[i] == true) {
								strTime = strTime + strTimes[i] + ",";
							}
						}
						if (strTime.equals("")) {
							tvTime.setText("请选择");
						} else {
							strTime = strTime.substring(0, strTime.length() - 1);
							tvTime.setText(strTime);
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						initTime2();
						dialog.dismiss();
					}
				}).create().show();

	}

	// 网络请求
	private class AddRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(AddRemindActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("AddRemindUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResult("");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OK;
						initResult("");
					} else {
						String message = jo.optString("errormsg");
						PF = Fail;
						initResult(message);
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = Fail;
					initResult("");
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
	private void initResult(String message) {
		if (PF == FailServer) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			if (message.equals("")) {
				Toast.makeText(this, "添加失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			}
		} else if (PF == OK) {
			Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

}

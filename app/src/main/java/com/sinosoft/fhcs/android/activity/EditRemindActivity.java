package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:修改服药提醒页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.entity.RemindListInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

@SuppressLint("SimpleDateFormat")
public class EditRemindActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	public static TextView tvStartDate, tvEndDate;
	private RelativeLayout btnStartDate, btnEndDate;
	private TextView tvTime;// 提醒时间
	private RelativeLayout btnTime;
	private EditText edtMedicineName;// 药品名称
	private EditText edtDosage;// 剂量
	private CheckBox cbtnPhone, cbtnSms;// 手机客户端，短信
	private Button btnSure;// 确定
	private RemindListInfo info = new RemindListInfo();
	private String strStartDate = "";
	private String strEndDate = "";
	private String strTime = "";
	private String[] strTimes = new String[24];
	private boolean[] arraySelected = new boolean[24];
	private String strMedicineName = "";
	private String strDosage = "";
	private String strMeal = "";
	private String strMethods = "";
	private Calendar calendarStart, calendarEnd;
	@SuppressWarnings("unused")
	private RadioGroup rGroupMeal;
	private RadioButton rbtnCanQian, rbtnCanHou;// 餐前餐后
	private String currentTime = "";

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
		setContentView(R.layout.activity_editremind);
		info = (RemindListInfo) this.getIntent().getExtras().get("info");
		ExitApplicaton.getInstance().addActivity(this);
		// 得到提醒信息
		strStartDate = info.getStartTime();
		strEndDate = info.getEndTime();
		strTime = info.getReminderTime();
		strMedicineName = info.getMedicineName();
		strDosage = info.getDosage();
		strMeal = info.getReminderByMeal();
		strMethods = info.getReminderWay();
		init();
		initTimes();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("修改提醒页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("修改提醒页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
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
		String[] ary = strTime.split(",");
		for (int i = 0; i < ary.length; i++) {
			for (int j = 0; j < strTimes.length; j++) {
				if (ary[i].equals(strTimes[j])) {
					arraySelected[j] = true;
				}
			}
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
	// 初始化控件
	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_addRemind));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		// 开始日期
		btnStartDate = (RelativeLayout) findViewById(R.id.editremind_datestart);
		btnStartDate.setOnClickListener(this);
		tvStartDate = (TextView) findViewById(R.id.editremind_tv_datestart);
		calendarStart = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		currentTime=simpleDateFormat.format(calendarStart.getTime());
		String start[] = Constant.getSplitDate(strStartDate);
		calendarStart.set(Calendar.YEAR, Integer.valueOf(start[0]));
		calendarStart.set(Calendar.MONTH, Integer.valueOf(start[1]) - 1);
		calendarStart.set(Calendar.DAY_OF_MONTH, Integer.valueOf(start[2]));
		updateStartDate();
		// 结束日期
		btnEndDate = (RelativeLayout) findViewById(R.id.editremind_dateEnd);
		btnEndDate.setOnClickListener(this);
		tvEndDate = (TextView) findViewById(R.id.editremind_tv_dateEnd);
		calendarEnd = Calendar.getInstance();
		String end[] = Constant.getSplitDate(strEndDate);
		calendarEnd.set(Calendar.YEAR, Integer.valueOf(end[0]));
		calendarEnd.set(Calendar.MONTH, Integer.valueOf(end[1]) - 1);
		calendarEnd.set(Calendar.DAY_OF_MONTH, Integer.valueOf(end[2]));
		updateEndDate();

		// 提醒时间
		btnTime = (RelativeLayout) findViewById(R.id.editremind_time);
		tvTime = (TextView) findViewById(R.id.editremind_tv_time);
		tvTime.setText(strTime);
		btnTime.setOnClickListener(this);
		// 药品名称
		edtMedicineName = (EditText) findViewById(R.id.editremind_medicineName);
		edtMedicineName.setText(strMedicineName);
		// 剂量
		edtDosage = (EditText) findViewById(R.id.editremind_dosage);
		edtDosage.setText(strDosage);
		// 餐前餐后
		rGroupMeal = (RadioGroup) findViewById(R.id.editremind_rg_meal);
		rbtnCanQian = (RadioButton) findViewById(R.id.editremind_rbtn_meal1);
		rbtnCanHou = (RadioButton) findViewById(R.id.editremind_rbtn_meal2);
		// 提醒方式
		cbtnPhone = (CheckBox) findViewById(R.id.editremind_cbtn_phone);
		cbtnSms = (CheckBox) findViewById(R.id.editremind_cbtn_sms);
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(strMethods, ",");
		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
		if (list.size() != 0) {
			if (list.size() == 1) {
				if (list.get(0).toString().trim().equals("60001101")) {
					cbtnPhone.setChecked(true);
					cbtnSms.setChecked(false);
				} else {
					cbtnPhone.setChecked(false);
					cbtnSms.setChecked(true);
				}
			} else if (list.size() == 2) {
				cbtnPhone.setChecked(true);
				cbtnSms.setChecked(true);
			}
		}
		if(strMeal.equals("60002101")){
			rbtnCanQian.setChecked(true);
			rbtnCanHou.setChecked(false);
		}else{
			rbtnCanQian.setChecked(false);
			rbtnCanHou.setChecked(true);
		}
		// 确定修改
		btnSure = (Button) findViewById(R.id.editremind_btn_add);
		btnSure.setOnClickListener(this);
	}


	private void updateStartDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		tvStartDate.setText(simpleDateFormat.format(calendarStart.getTime()));
	}


	private void updateEndDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		tvEndDate.setText(simpleDateFormat.format(calendarEnd.getTime()));
	}

	// 返回
	private void goback() {
		String reminderWay = "";// 用于处理返回按钮时使用
		if (cbtnPhone.isChecked() & !cbtnSms.isChecked()) {
			reminderWay = "60001101";
		}
		if (cbtnSms.isChecked() && !cbtnPhone.isChecked()) {
			reminderWay = "60001102";
		}
		if (cbtnSms.isChecked() && cbtnPhone.isChecked()) {
			reminderWay = "60001101" + "," + "60001102";
		}
		String meal = "";
		if (rbtnCanQian.isChecked()) {
			meal = "60002101";
		}
		if (rbtnCanHou.isChecked()) {
			meal = "60002103";
		}
		if (tvStartDate
				.getText()
				.toString()
				.equals(Constant.getSplitDate(info.getStartTime())[0] + "-"
						+ Constant.getSplitDate(info.getStartTime())[1] + "-"
						+ Constant.getSplitDate(info.getStartTime())[2])
				&& tvEndDate
				.getText()
				.toString()
				.equals(Constant.getSplitDate(info.getEndTime())[0]
						+ "-"
						+ Constant.getSplitDate(info.getEndTime())[1]
						+ "-"
						+ Constant.getSplitDate(info.getEndTime())[2])
				&& edtMedicineName.getText().toString().trim()
				.equals(info.getMedicineName())
				&& tvTime.getText().toString().trim()
				.equals(info.getReminderTime())
				&& edtDosage.getText().toString().trim()
				.equals(info.getDosage())
				&& (meal.equals("") || meal.equals(strMeal))

				&& (reminderWay.equals("") || reminderWay.equals(info
				.getReminderWay()))) {
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
			case R.id.editremind_datestart:
				// 开始日期
				Constant.showDate(this, tvStartDate, tvStartDate.getText()
						.toString().trim(), "EditRemindStart");
				break;
			case R.id.editremind_dateEnd:
				// 结束日期
				Constant.showDate(this, tvEndDate, tvEndDate.getText().toString()
						.trim(), "EditRemindEnd");
				break;
			case R.id.editremind_time:
				// 提醒时间
				createTimeDialog();
				break;
			case R.id.editremind_btn_add:
				// 确定
				// 创建
				if (tvStartDate.getText().toString().trim().equals("")
						|| tvEndDate.getText().toString().trim().equals("")
						|| strTime.equals("") || strTime.equals("请选择")
						|| edtMedicineName.getText().toString().trim().equals("")
						|| edtDosage.getText().toString().trim().equals("")) {
					Toast.makeText(EditRemindActivity.this, "所有的内容都是必填项，请填写！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				int result1 = currentTime.compareTo(tvStartDate.getText().toString().trim());
				if (result1 > 0) {
					Toast.makeText(this, "服药时间不能小于当前时间！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				int result2 = (tvStartDate.getText().toString().trim()).compareTo(tvEndDate.getText().toString().trim());
				if (result2 >= 0) {
					Toast.makeText(this, "结束时间必须大于开始时间！", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (!cbtnPhone.isChecked() && !cbtnSms.isChecked()) {
					Toast.makeText(EditRemindActivity.this, "请选择提醒方式！",
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
				// 餐前 60002101 //餐中 60002102 //餐后 60002103
				String meal = "60002101";
				if (rbtnCanQian.isChecked()) {
					meal = "60002101";
				}
				if (rbtnCanHou.isChecked()) {
					meal = "60002103";
				}
				if (!HttpManager.isNetworkAvailable(this)) {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
					return;
				}
				EditRequest re = new EditRequest();
				re.execute(HttpManager.urlEditRemind(info.getId(), tvStartDate
								.getText().toString().trim(), tvEndDate.getText()
								.toString().trim(), strTime, edtMedicineName.getText()
								.toString().trim(), edtDosage.getText().toString().trim(),
						reminderWay, meal));
				break;
			default:
				break;
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
						// System.out.println(strTime);
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
	private class EditRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(EditRemindActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("editRemindUrl", url + "");
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
						String message=jo.optString("errormsg");
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
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			if(message.equals("")){
				Toast.makeText(this, "修改失败!", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			}

		} else if (PF == OK) {
			Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
			finish();
		}

	}
}

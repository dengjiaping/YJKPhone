package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:创建竞赛页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月16日.
 */
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class AddCompetitionActivity extends BaseActivity implements OnClickListener {
	public static Button btnPersonNum;// 人数
	public static Button btnStartDate, btnStartTime, btnEndDate, btnEndTime;// 开始结束日期时间
	public static Button btnModelTwo;// 两人模式，多人模式
	public static Button btnModelMore;
	private Button btnTypeRun, btnTypeBike;// 跑步，骑行
	private EditText edtContent;// 宣言
	private Button btnCreate;// 创建
	private TextView tvTitle;
	private Button btnBack;// 返回
	private Calendar calendarStart, calendarEnd;
	private SimpleDateFormat simpleDateFormatDate, simpleDateFormatTime;
	public static String strModel = "";
	private String strType = "";// 多人 01 两人 02 跑步 01 骑行 02
	// 请求数据
	private ProgressDialog progressDialog;// 进度条
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private int PF = 1000;
	private String userId = "";
	private String friendId="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initData();// 初始值
	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_addcompetition);
		ExitApplicaton.getInstance().addActivity(this);
		friendId=this.getIntent().getExtras().getString("friendId");
		// 从首选项获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();// 初始化控件
	}

	@SuppressLint("SimpleDateFormat")
	private void initData() {
		// 设置初始值
		btnPersonNum.setText("2");
		btnModelTwo.setBackgroundResource(R.drawable.twopeople2);
		btnModelMore.setBackgroundResource(R.drawable.morepeople1);
		strModel = "02";
		btnTypeRun.setBackgroundResource(R.drawable.run2);
		btnTypeBike.setBackgroundResource(R.drawable.bike1);
		strType = "01";

		calendarStart = Calendar.getInstance();
		calendarEnd = Calendar.getInstance();
		simpleDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
		simpleDateFormatTime = new SimpleDateFormat("HH:mm");
		btnStartDate.setText(simpleDateFormatDate.format(calendarStart
				.getTime()));
		btnStartTime.setText(simpleDateFormatTime.format(calendarStart
				.getTime()));
		btnEndDate.setText(simpleDateFormatDate.format(calendarEnd.getTime()));
		btnEndTime.setText(simpleDateFormatTime.format(calendarEnd.getTime()));
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_addcompetition));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		btnPersonNum = (Button) findViewById(R.id.addcompt_btn_personnum);
		btnStartDate = (Button) findViewById(R.id.addcompt_btn_dateStart);
		btnStartTime = (Button) findViewById(R.id.addcompt_btn_timeStart);
		btnEndDate = (Button) findViewById(R.id.addcompt_btn_dateEnd);
		btnEndTime = (Button) findViewById(R.id.addcompt_btn_timeEnd);
		btnModelTwo = (Button) findViewById(R.id.addcompt_btn_twoperson);
		btnModelMore = (Button) findViewById(R.id.addcompt_btn_moreperson);
		btnTypeRun = (Button) findViewById(R.id.addcompt_btn_run);
		btnTypeBike = (Button) findViewById(R.id.addcompt_btn_bike);
		edtContent = (EditText) findViewById(R.id.addcompt_edt_content);
		edtContent.setText("快来和我们一起PK吧!");
		btnPersonNum.setOnClickListener(this);
		btnStartDate.setOnClickListener(this);
		btnStartTime.setOnClickListener(this);
		btnEndDate.setOnClickListener(this);
		btnEndTime.setOnClickListener(this);
		btnModelTwo.setOnClickListener(this);
		btnModelMore.setOnClickListener(this);
		btnTypeRun.setOnClickListener(this);
		btnTypeBike.setOnClickListener(this);
		btnCreate = (Button) findViewById(R.id.addcompt_btn_add);
		btnCreate.setOnClickListener(this);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("创建竞赛页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("创建竞赛页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.addcompt_btn_personnum:
				// 人数
				Constant.showNum(this, btnPersonNum, btnPersonNum.getText()
						.toString().trim(), "personNum", "addcompetition", 2, 50);
				break;
			case R.id.addcompt_btn_dateStart:
				// 开始日期
				Constant.showDate(this, btnStartDate, btnStartDate.getText()
						.toString().trim(), "addCompetition_btnStartDate");
				break;
			case R.id.addcompt_btn_timeStart:
				// 开始时间
				Constant.showOnlyTime(this, btnStartTime, btnStartTime.getText()
						.toString().trim(), "addCompetition_btnStartTime");
				break;
			case R.id.addcompt_btn_dateEnd:
				// 结束日期
				Constant.showDate(this, btnEndDate, btnEndDate.getText().toString()
						.trim(), "addCompetition_btnEndDate");
				break;
			case R.id.addcompt_btn_timeEnd:
				// 结束时间
				Constant.showOnlyTime(this, btnEndTime, btnEndTime.getText()
						.toString().trim(), "addCompetition_btnEndTime");
				break;
			case R.id.addcompt_btn_twoperson:
				// 两人模式
				btnPersonNum.setText("2");
				btnModelTwo.setBackgroundResource(R.drawable.twopeople2);
				btnModelMore.setBackgroundResource(R.drawable.morepeople1);
				strModel = "02";
				break;
			case R.id.addcompt_btn_moreperson:
				// 多人模式
				btnModelTwo.setBackgroundResource(R.drawable.twopeople1);
				btnModelMore.setBackgroundResource(R.drawable.morepeople2);
				strModel = "01";
				break;
			case R.id.addcompt_btn_run:
				// 跑步
				btnTypeRun.setBackgroundResource(R.drawable.run2);
				btnTypeBike.setBackgroundResource(R.drawable.bike1);
				strType = "01";
				break;
			case R.id.addcompt_btn_bike:
				// 骑行
				btnTypeRun.setBackgroundResource(R.drawable.run1);
				btnTypeBike.setBackgroundResource(R.drawable.bike2);
				strType = "02";
				break;
			case R.id.addcompt_btn_add:
				// 创建
				if (!HttpManager.isNetworkAvailable(this)) {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (edtContent.getText().toString().trim().equals("")) {
					Toast.makeText(this, "宣言不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				Calendar calendarCurrent = Calendar.getInstance();
				String currentTime = simpleDateFormatDate.format(calendarCurrent
						.getTime())
						+ "%20"
						+ simpleDateFormatTime.format(calendarCurrent.getTime());
				String startTime = btnStartDate.getText().toString().trim() + "%20"
						+ btnStartTime.getText().toString().trim();
				String endTime = btnEndDate.getText().toString().trim() + "%20"
						+ btnEndTime.getText().toString().trim();

				int day = calendarCurrent.get(Calendar.DAY_OF_MONTH) + 7;
				calendarCurrent.set(Calendar.DAY_OF_MONTH, day);
				String SevenDay = simpleDateFormatDate.format(calendarCurrent
						.getTime())
						+ "%20"
						+ simpleDateFormatTime.format(calendarCurrent.getTime());

				int result1 = startTime.compareTo(currentTime);
				int result2 = endTime.compareTo(currentTime);
				int result3 = endTime.compareTo(startTime);
				int result4 = startTime.compareTo(SevenDay);
				int result5 = endTime.compareTo(SevenDay);
				if (result1 <= 0) {
					Toast.makeText(this, "开始时间必须大于当前时间！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (result2 <= 0) {
					Toast.makeText(this, "结束时间必须大于当前时间！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (result3 <= 0) {
					Toast.makeText(this, "结束时间必须大于开始时间！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (result4 > 0) {
					Toast.makeText(this, "开始时间必须在7天之内！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (result5 > 0) {
					Toast.makeText(this, "结束时间必须在7天之内！", Toast.LENGTH_SHORT).show();
					return;
				}
				if(friendId.equals("add")){
					//创建竞赛
					AddRequest re = new AddRequest();
					re.execute(HttpManager.urlAddCompetition(userId, btnPersonNum
									.getText().toString().trim(), startTime, endTime, strModel,
							strType, edtContent.getText().toString().trim()));
				}else{
					//邀请竞赛
					AddRequest re = new AddRequest();
					re.execute(HttpManager.urlAddRaceForFriend(userId,friendId, btnPersonNum
									.getText().toString().trim(), startTime, endTime, strModel,
							strType, edtContent.getText().toString().trim()));
				}

				break;
			default:
				break;
		}
	}

	// 网络请求
	private class AddRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(AddCompetitionActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("AddCompetitionUrl", url + "");
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
					// System.err.println(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OK;
						initResult();
					} else {
						PF = Fail;
						initResult();
					}
				} catch (JSONException e) {
					PF = Fail;
					initResult();
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

	// 请求结果
	private void initResult() {
		if (PF == FailServer) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			if(friendId.equals("add")){
				Toast.makeText(this, "创建失败!", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "邀请失败!", Toast.LENGTH_SHORT).show();
			}

		} else if (PF == OK) {
			if(friendId.equals("add")){
				Toast.makeText(this, "创建成功！", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "邀请成功！", Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	}
}

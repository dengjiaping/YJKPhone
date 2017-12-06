package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:竞赛详情页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月20日.
 */
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.CompetitionDetailGridviewAdapter;
import com.sinosoft.fhcs.android.entity.CompetitionDetail;
import com.sinosoft.fhcs.android.entity.CompetitionPeople;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CompetitionDetailActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private TextView tvTitle;
	private Button btnBack;// 返回
	private GridView gridview;
	private List<CompetitionPeople> fmList = new ArrayList<CompetitionPeople>();// 包括添加按钮的数据
	private List<CompetitionPeople> getList = new ArrayList<CompetitionPeople>();// 不包括添加按钮的数据
	private CompetitionDetailGridviewAdapter adapter;
	private Button btnComment;// 评论
	private ImageView ivType;
	private TextView tvTime;
	private LinearLayout layoutTime;
	private TextView tvh1, tvh2, tvh3, tvm1, tvm2, tvs1, tvs2;
	private TextView tvDate;
	private TextView tvPeople;
	private TextView tvModel;
	private TextView tvContent;
	// 家庭列表网络请求
	private ProgressDialog progressDialog;// 进度条
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private static final int FailNoData = 1004;// 没有数据
	private static final int OKJoin = 1005;// 成功
	private static final int FailServerJoin = 1006;// 连接超时
	private static final int FailJoin = 1007;// 失败

	private int PF = 1000;
	private String raceId = "";
	private String userId = "";
	private String name = "";
	private String strImgUrl = "";
	private String changCi = "";
	private CompetitionDetail detailInfo;
	private boolean isFull = false;
	//倒计时
	private static int hour = -1;
	private static int minute = -1;
	private static int second = -1;
	private final static String tag = "CompetitionDetailActivity";
	private Timer timer;
	private TimerTask timerTask;
	private String countDown = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_competitiondetail);
		ExitApplicaton.getInstance().addActivity(this);
		raceId = this.getIntent().getExtras().getString("raceId");
		changCi = this.getIntent().getExtras().getString("session");
		// 从首选项获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		name = prefs.getString("nickName", "");
		strImgUrl = prefs.getString("imgUrl", "");
		init();// 初始化控件
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText("第" + changCi + "场");
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		// 内容
		layoutTime = (LinearLayout) findViewById(R.id.cptdetail_layout_time);
		tvTime = (TextView) findViewById(R.id.cptdetail_tv_time);
		ivType = (ImageView) findViewById(R.id.cptdetail_img_type);
		tvh1 = (TextView) findViewById(R.id.cptdetail_tv_time_h1);
		tvh2 = (TextView) findViewById(R.id.cptdetail_tv_time_h2);
		tvh3 = (TextView) findViewById(R.id.cptdetail_tv_time_h3);
		tvm1 = (TextView) findViewById(R.id.cptdetail_tv_time_m1);
		tvm2 = (TextView) findViewById(R.id.cptdetail_tv_time_m2);
		tvs1 = (TextView) findViewById(R.id.cptdetail_tv_time_s1);
		tvs2 = (TextView) findViewById(R.id.cptdetail_tv_time_s2);
		tvDate = (TextView) findViewById(R.id.cptdetail_tv_date);
		tvPeople = (TextView) findViewById(R.id.cptdetail_tv_people);
		tvModel = (TextView) findViewById(R.id.cptdetail_tv_model);
		tvContent = (TextView) findViewById(R.id.cptdetail_tv_content);
		btnComment = (Button) findViewById(R.id.cptdetail_btn_comment);
		btnComment.setOnClickListener(this);
		gridview = (GridView) findViewById(R.id.cptdetail_gridview);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		if (detailInfo.getIsJoin().toString().trim().equals("1")
				|| isFull == true) {
			// if(isFull==true){
			// Toast.makeText(CompetitionDetailActivity.this, "人数已经满了，不能再加入了！",
			// Toast.LENGTH_SHORT).show();
			// }else{
			// Toast.makeText(CompetitionDetailActivity.this, "您已经加入了！",
			// Toast.LENGTH_SHORT).show();
			// }
		} else {
			if (HttpManager.isNetworkAvailable(this)) {
				JoinInRequest request = new JoinInRequest();
				request.execute(HttpManager.urlJoinCompetition(userId, raceId));
			} else {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
				Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			}

		}
	}

	@Override
	protected void onStart() {
		fmList.clear();
		getList.clear();
		if (HttpManager.isNetworkAvailable(this)) {
			GetListRequest re = new GetListRequest();
			re.execute(HttpManager.urlGetCompetitionDetail(userId, raceId));
		} else {
//			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
		}
		super.onStart();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("竞赛详情页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("竞赛详情页"); // 保证 onPageEnd 在onPause 之前调用
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.cptdetail_btn_comment:
				// 评论
				Intent intent = new Intent(this, CommentListActivity.class);
				intent.putExtra("raceId", raceId);
				startActivity(intent);
				break;
			default:
				break;
		}

	}

	// 请求结果
	private void initResult() {
		if (PF == FailServer) {
			initStartData();
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			initStartData();
			Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			initDetailData();
			initEndData();
		} else if (PF == FailNoData) {
			initDetailData();
			initStartData();
		} else if (PF == FailServerJoin) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailJoin) {
			Toast.makeText(this, "加入失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKJoin) {
			initJoinData();
			Toast.makeText(this, "加入成功!", Toast.LENGTH_SHORT).show();
		}

	}

	// 人员数据
	private void initEndData() {
		fmList.addAll(getList);
		CompetitionPeople member;
		if (detailInfo.getIsJoin().toString().trim().equals("1")
				|| isFull == true) {
			// 已经加入
			member = new CompetitionPeople("", "加入他们", "joiniconOk", "");
		} else {
			member = new CompetitionPeople("", "加入他们", "joiniconFalse", "");

		}
		fmList.add(member);
		adapter = new CompetitionDetailGridviewAdapter(this, fmList);
		gridview.setAdapter(adapter);
	}

	// 加入处理
	private void initJoinData() {
		fmList.clear();
		fmList.addAll(getList);
		CompetitionPeople member = new CompetitionPeople("", name,
				HttpManager.m_imageUrl + strImgUrl, "0");
		fmList.add(member);
		CompetitionPeople member2 = new CompetitionPeople("", "加入他们",
				"joiniconOk", "");
		fmList.add(member2);
		detailInfo.setIsJoin("1");
		tvPeople.setText(Integer.valueOf(detailInfo.getNowNumber().toString()
				.trim())
				+ 1 + "/" + detailInfo.getNumber());
		adapter.notifyDataSetChanged();

	}

	// 详情数据
	private void initDetailData() {
		if (detailInfo.getCountdown().equals("已开始")) {
			tvTime.setText("已开始");
			layoutTime.setVisibility(View.INVISIBLE);
		} else if (detailInfo.getCountdown().equals("已结束")) {
			tvTime.setText("已结束");
			layoutTime.setVisibility(View.INVISIBLE);
		} else {
			tvTime.setText("距开始");
			layoutTime.setVisibility(View.VISIBLE);
			if (detailInfo.getCountdown().equals("")
					|| detailInfo.getCountdown().equals("--")) {
				countDown = "00-00-00";
			} else {
				countDown = detailInfo.getCountdown();
			}
			String[] temp = Constant.getWheelDate(countDown);
			hour= Integer.valueOf(temp[0]);
			minute= Integer.valueOf(temp[1]);
			second= Integer.valueOf(temp[2]);
			initShow();
			if(PF==OK){
				timerTask = new TimerTask() {

					@Override
					public void run() {
						Message msg = new Message();
						msg.what = 0;
						handler.sendMessage(msg);
					}
				};

				timer = new Timer();
				timer.schedule(timerTask, 0, 1000);//1秒
			}

		}
		if (detailInfo.getType().toString().trim().equals("01")) {
			ivType.setBackgroundResource(R.drawable.iconrun_big);
		} else {
			ivType.setBackgroundResource(R.drawable.iconbike_big);
		}
		if (detailInfo.getModel().toString().trim().equals("01")) {
			tvModel.setText("多人竞赛");
		} else {
			tvModel.setText("两人PK");
		}
		tvDate.setText(detailInfo.getStartTime() + " 至 "
				+ detailInfo.getEndTime());
		tvPeople.setText(detailInfo.getNowNumber() + "/"
				+ detailInfo.getNumber());
		if (detailInfo.getNowNumber().toString().trim()
				.equals(detailInfo.getNumber().toString().trim())) {
			// 满人
			isFull = true;
		} else {
			isFull = false;
		}
		tvContent.setText(detailInfo.getContent());
		btnComment.setText("评论(" + detailInfo.getCommentNumber() + ")");

	}
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			System.out.println("handle!");
			if(hour==0){
				if (minute == 0) {
					if (second == 0) {
//						Toast.makeText(CompetitionDetailActivity.this, "Time out !", Toast.LENGTH_SHORT).show();
						countDown = "00-00-00";
						tvTime.setText("已开始");
						initShow();
						if (timer != null) {
							timer.cancel();
							timer = null;
						}
						if (timerTask != null) {
							timerTask = null;
						}
					} else {
						second--;
						initShow();
					}
				} else {
					if (second == 0) {
						second = 59;
						minute--;
						initShow();
					} else {
						second--;
						initShow();
					}
				}
			}else{
				//有小时
				if(minute==0){
					if(second==0){
						minute =59;
						second=59;
						hour--;
						initShow();
					}else{
						second--;
						initShow();
					}
				}else{
					if(second==0){
						second=59;
						minute--;
						second--;
						initShow();
					}else{
						second--;
						initShow();
					}
				}
			}
		};
	};
	private void initShow() {
		String strH =hour+"";
		String strM = minute+"";
		String strS = second+"";
		if (strH.length() != 0 && strH.length() < 2) {
			strH = "0" + strH;
		}
		if (strM.length() != 0 && strM.length() < 2) {
			strM = "0" + strM;
		}
		if (strS.length() != 0 && strS.length() < 2) {
			strS = "0" + strS;
		}
		if (strH.length() > 2) {
			tvh1.setVisibility(View.VISIBLE);
			tvh1.setText(strH.charAt(0) + "");
			tvh2.setText(strH.charAt(1) + "");
			tvh3.setText(strH.charAt(2) + "");
		} else {
			tvh1.setVisibility(View.GONE);
			tvh2.setText(strH.charAt(0) + "");
			tvh3.setText(strH.charAt(1) + "");
		}
		tvm1.setText(strM.charAt(0) + "");
		tvm2.setText(strM.charAt(1) + "");
		tvs1.setText(strS.charAt(0) + "");
		tvs2.setText(strS.charAt(1) + "");

	}
	// 默认数据
	private void initStartData() {
		isFull = false;
		CompetitionPeople member = new CompetitionPeople("", "加入他们",
				"joiniconFalse", "");
		fmList.add(member);
		adapter = new CompetitionDetailGridviewAdapter(this, fmList);
		gridview.setAdapter(adapter);
	}

	// 网络请求
	private class GetListRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(CompetitionDetailActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getcptDetailUrl", url + "");
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
						JSONObject jo1 = jo.getJSONObject("data");
						JSONObject jo2 = jo1.getJSONObject("data");
						String id = jo2.optString("id");
						String session = jo2.optString("uid");
						String model = jo2.optString("model");
						String type = jo2.optString("type");
						String content = jo2.optString("manifesto");
						String countdown = jo2.optString("countdown");
						String startTime = jo2.optString("startTime");
						String endTime = jo2.optString("endTime");
						String nowNumber = jo2.optString("nowNumber");
						String number = jo2.optString("number");
						String commentNum = jo2.optString("commentNum");
						String isJoin = jo2.optString("isJoin");
						detailInfo = new CompetitionDetail(id, session, model,
								type, content, countdown, startTime, endTime,
								nowNumber, number, commentNum, isJoin);
						JSONArray ja = jo2.getJSONArray("userList");
						if (ja.length() == 0) {
							PF = FailNoData;
							initResult();
						} else {
							getList.clear();
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo3 = ja.getJSONObject(i);
								String userName = jo3.optString("userName");
								String avatarPath = jo3.optString("avatarPath");
								String km = "0";
								if (!jo3.isNull("km")) {
									km = jo3.optString("km");
								}
								CompetitionPeople info = new CompetitionPeople(
										"", userName, HttpManager.m_imageUrl
										+ avatarPath, km);
								getList.add(info);
							}
							PF = OK;
							initResult();
						}
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

	// 提交目标
	private class JoinInRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(CompetitionDetailActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("joincptDetailUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServerJoin;
				initResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					System.err.println(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OKJoin;
						initResult();
					} else {
						PF = FailJoin;
						initResult();
					}
				} catch (JSONException e) {
					PF = FailJoin;
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

	@Override
	protected void onDestroy() {
		Log.v(tag, "log---------->onDestroy!");
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask = null;
		}
		minute = -1;
		second = -1;
		super.onDestroy();
	}
}

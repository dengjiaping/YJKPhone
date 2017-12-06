package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:邀请竞赛列表页
 * @Author: wangshuangshuang.
 * @Create: 2015年3月16日.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.CompetitionListAdapter;
import com.sinosoft.fhcs.android.customview.XListView;
import com.sinosoft.fhcs.android.customview.XListView.IXListViewListener;
import com.sinosoft.fhcs.android.entity.CompetitionListInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InvitationListActivity extends BaseActivity implements
		OnClickListener, IXListViewListener, OnItemClickListener {
	private final String TAG = "CompetitionListActivity";
	private Button btnBack;// 返回
	private String userId = "";
	private TextView tvTitle;
	private Button btnCreate;// 创建
	private XListView listView;
	private CompetitionListAdapter adapter;
	private List<CompetitionListInfo> getList = new ArrayList<CompetitionListInfo>();
	private Handler mHandler;
	private static final int OKList = 1001;// 成功
	private static final int FailList = 1002;// 失败
	private static final int ChaoShi = 1003;// 超时
	private int PF = 1000;
	private ProgressDialog progressDialog;// 进度条
	private int page = 1;// 页数
	private boolean FlagIsMore = false;
	private boolean isFirst = false;
	private String friendId = "";
	private static final int OKJoin = 1005;// 成功
	private static final int FailServerJoin = 1006;// 连接超时
	private static final int FailJoin = 1007;// 失败
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_invitationlist);
		ExitApplicaton.getInstance().addActivity(this);
		friendId = this.getIntent().getExtras().getString("friendsId");
		// 从首选项获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();// 初始化控件
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_invitationlist));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		btnCreate = (Button) findViewById(R.id.invitation_btn_add);
		btnCreate.setOnClickListener(this);
		listView = (XListView) findViewById(R.id.invitationlist_listview);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
		adapter = new CompetitionListAdapter(this, getList);
		listView.setAdapter(adapter);
		mHandler = new Handler();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		if (getList.size() != 0) {
			position -= listView.getHeaderViewsCount();
			// 邀请
			if (HttpManager.isNetworkAvailable(this)) {
				JoinInRequest request = new JoinInRequest();
				request.execute(HttpManager.urlJoinRaceCompetition(userId,friendId, getList.get(position).getId()));
			} else {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
				Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			}

		}

	}

	@Override
	protected void onStart() {
		page = 1;
		FlagIsMore = false;
		isFirst = false;
		if (!HttpManager.isNetworkAvailable(this)) {
//			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		getList.clear();
		ListRequest request = new ListRequest();
		request.execute(HttpManager.urlGetMyRaceForFriend(userId,friendId, 1));
		super.onStart();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("邀请竞赛列表页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("邀请竞赛列表页"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.invitation_btn_add:
				// 创建
				Intent intent = new Intent(this, AddCompetitionActivity.class);
				intent.putExtra("friendId", friendId);
				startActivity(intent);
				break;
			default:
				break;
		}

	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime(Constant
				.getNowDateStrByFormate("yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public void onRefresh() {
		Log.i(TAG, "刷新最新");
		page = 1;
		FlagIsMore = false;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getList.clear();
				ListRequest request = new ListRequest();
				request.execute(HttpManager.urlGetMyRaceForFriend(userId,friendId, 1));
				onLoad();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		FlagIsMore = true;
		page++;
		Log.i(TAG, "加载更多");
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				ListRequest request = new ListRequest();
				request.execute(HttpManager.urlGetMyRaceForFriend(userId,friendId, page));
				onLoad();
			}
		}, 2000);

	}

	// 获取结果
	private void initGetInfoResult() {
		if (PF == ChaoShi) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailList) {
			if (FlagIsMore == false) {
				Toast.makeText(this, "没有竞赛数据！", Toast.LENGTH_SHORT).show();
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
			}

		} else if (PF == OKList) {
			if (FlagIsMore == false) {
				adapter = new CompetitionListAdapter(this, getList);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		}else if (PF == FailServerJoin) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailJoin) {
			Toast.makeText(this, "邀请失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKJoin) {
			Toast.makeText(this, "邀请成功!", Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	// 解析数据
	private List<CompetitionListInfo> jiexi(String result) {
		try {
			JSONObject jo = new JSONObject(result);
			String resultCode = jo.optString("resultCode");
			if (resultCode.equals("1")) {
				String entity = jo.optString("data");
				JSONObject jo2 = new JSONObject(entity);
				if (!jo2.isNull("data")) {
					JSONArray ja = jo2.getJSONArray("data");
					if (ja.length() != 0) {
						for (int i = 0; i < ja.length(); i++) {
							JSONObject jo3 = ja.getJSONObject(i);
							String id = jo3.optString("raceId");
							String uid = jo3.optString("uid");
							String model = jo3.optString("model");
							String type = jo3.optString("type");
							String number = jo3.optString("number");
							String nowNumber = jo3.optString("nowNumber");
							String countdown = jo3.optString("countdown");
							CompetitionListInfo info = new CompetitionListInfo(
									id, uid, countdown, number, nowNumber,
									model, type);
							getList.add(info);
						}
						PF = OKList;
						initGetInfoResult();
					} else {
						PF = FailList;
						initGetInfoResult();
					}

				} else {
					PF = FailList;
					initGetInfoResult();
				}

			} else {
				PF = FailList;
				initGetInfoResult();
			}
		} catch (JSONException e) {
			Log.e(TAG, "解析失败！");
			PF = FailList;
			initGetInfoResult();
		}
		return getList;

	}

	// 网络请求
	private class ListRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			if (isFirst == false) {
				progressDialog = new ProgressDialog(InvitationListActivity.this);
				Constant.showProgressDialog(progressDialog);
			}
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("InvitationListUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initGetInfoResult();
			} else {
				getList = jiexi(result);
			}
			if (isFirst == false) {
				Constant.exitProgressDialog(progressDialog);
			}
			isFirst = true;
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}
	//邀请竞赛
	private class JoinInRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(InvitationListActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("InvitationJoinUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServerJoin;
				initGetInfoResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					System.err.println(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OKJoin;
						initGetInfoResult();
					} else {
						PF = FailJoin;
						initGetInfoResult();
					}
				} catch (JSONException e) {
					PF = FailJoin;
					initGetInfoResult();
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
}

package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:圈子消息页
 * @Author: wangshuangshuang.
 * @Create: 2015年4月3日.
 */
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.QuanZiListAdapter;
import com.sinosoft.fhcs.android.customview.XListView;
import com.sinosoft.fhcs.android.customview.XListView.IXListViewListener;
import com.sinosoft.fhcs.android.entity.QuanZiInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuanZiActivity extends BaseActivity implements OnItemClickListener,
		OnClickListener, OnItemLongClickListener, IXListViewListener {
	private final String TAG = "QuanZiActivity";
	private TextView tvTitle;
	private Button btnBack;
	private XListView listView;
	private QuanZiListAdapter adapter;
	private List<QuanZiInfo> getList = new ArrayList<QuanZiInfo>();
	private Handler mHandler;
	private static final int OKList = 1001;// 成功
	private static final int FailList = 1002;// 失败
	private static final int OKDelete = 1004;// 删除成功
	private static final int FailDelete = 1005;// 删除失败
	private static final int OKUpdate = 1006;// 更新成功
	private static final int FailUpdate = 1007;// 更新失败
	private static final int ChaoShi = 1003;// 超时
	private int PF = 1000;
	private ProgressDialog progressDialog;// 进度条
	private String userId = "";
	private int page = 1;// 页数
	private boolean FlagIsMore = false;
	private boolean isFirst = false;
	private int index = 1;// 删除id

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_quanzi);
		ExitApplicaton.getInstance().addActivity(this);
		// 从首选项获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();// 初始化控件
	}

	@Override
	protected void onStart() {
		page = 1;
		FlagIsMore = false;
		isFirst = false;
		if (!HttpManager.isNetworkAvailable(this)) {
			// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试!", Toast.LENGTH_SHORT).show();
			return;
		}
		getList.clear();
		ListRequest request = new ListRequest();
		request.execute(HttpManager.urlGetCircleMessage(userId, 1));
		super.onStart();
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_quanzilist));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		listView = (XListView) findViewById(R.id.quanzi_listview);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		adapter = new QuanZiListAdapter(this, getList);
		listView.setAdapter(adapter);
		mHandler = new Handler();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// 消息类型:留言消息-01；加好友消息-02；邀请竞赛消息-03
		if (getList.size() != 0) {
			position -= listView.getHeaderViewsCount();
			if (HttpManager.isNetworkAvailable(QuanZiActivity.this)) {
				//更新
				getList.get(position).setRead(true);
				adapter.notifyDataSetChanged();
				UpdateRequest re = new UpdateRequest();
				re.execute(HttpManager.urlUpdateCircleMessageIsRead(userId,
						getList.get(position).getId()));
				//跳转
				if (getList.get(position).getMessageType().equals("01")) {
					Intent intent = new Intent(QuanZiActivity.this,
							LeaveMessageActivity.class);
					intent.putExtra("friendsId", getList.get(position)
							.getFriendUserId());
					startActivity(intent);
				} else if (getList.get(position).getMessageType().equals("02")) {
					Intent intent = new Intent(QuanZiActivity.this,
							PkPersonalActivity.class);
					intent.putExtra("flag", false);
					intent.putExtra("id", getList.get(position)
							.getFriendUserId());
					intent.putExtra("distance", getList.get(position)
							.getDistance());
					intent.putExtra("near", getList.get(position).getNear());
					startActivity(intent);
				} else {
					Intent intent = new Intent(QuanZiActivity.this,
							CompetitionDetailActivity.class);
					intent.putExtra("raceId", getList.get(position).getRaceId());
					intent.putExtra("session", getList.get(position)
							.getChangCi());
					startActivity(intent);
				}
			}
		} else {
			Toast.makeText(QuanZiActivity.this, "请检查您的网络连接", Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
								   int position, long id) {
		if (getList.size() != 0) {
			position -= listView.getHeaderViewsCount();
			index = position;
			DeleteDialog();
		}
		return false;
	}

	// 删除
	private void DeleteDialog() {
		AlertDialog.Builder builder = new Builder(QuanZiActivity.this,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage("您确定删除这条消息吗？");
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				DeleteRequest re = new DeleteRequest();
				re.execute(HttpManager.urlDelCircleMessage(userId,
						getList.get(index).getId()));
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("圈子消息页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("圈子消息页"); // 保证 onPageEnd 在onPause 之前调用
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
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
				request.execute(HttpManager.urlGetCircleMessage(userId, 1));
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
				request.execute(HttpManager.urlGetCircleMessage(userId, page));
				onLoad();
			}
		}, 2000);

	}

	// 获取结果
	private void initGetInfoResult() {
		if (PF == ChaoShi) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailList) {
			if (FlagIsMore == false) {
				// Constant.showDialog(this, "没有获取到数据!");
				Toast.makeText(this, "暂无圈子消息!", Toast.LENGTH_SHORT).show();
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(this, "没有更多的消息了！", Toast.LENGTH_SHORT).show();
			}

		} else if (PF == OKList) {
			if (FlagIsMore == false) {
				adapter = new QuanZiListAdapter(this, getList);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		} else if (PF == OKDelete) {
			// 通知adapter 更新
			getList.remove(index);
			adapter.notifyDataSetChanged();
			Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();

		} else if (PF == FailDelete) {
			Toast.makeText(this, "删除失败！", Toast.LENGTH_SHORT).show();
		} else if (PF == OKUpdate) {
			System.out.println("更新成功");
		} else if (PF == FailUpdate) {
			System.out.println("更新失败");
		}

	}

	// 解析数据
	private List<QuanZiInfo> jiexi(String result) {
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
							String id = jo3.optString("id");
							String messageType = jo3.optString("messageType");
							String content = "";
							// 消息类型:留言消息-01；加好友消息-02；邀请竞赛消息-03
							if (messageType.equals("01")) {
								content = "给你留言了";
							} else if (messageType.equals("02")) {
								content = "加你为好友";
							} else {
								content = "邀请你加入竞赛";
							}
							String friendUserId = jo3.optString("friendUserId");
							String messageTime = jo3.optString("messageTime");
							String avatarPath = jo3.optString("avatarPath");
							String friendName = jo3.optString("friendName");
							boolean isRead = jo3.optBoolean("isRead");
							String raceId = jo3.optString("raceId");
							String distance = jo3.optString("activityMeters");
							double near = jo3.optDouble("distance");// 附近
							String sessionId = jo3.optString("sessionId");
							QuanZiInfo info = new QuanZiInfo(id, content,
									friendUserId, friendName,
									HttpManager.m_imageUrl + avatarPath,
									messageType, messageTime, isRead, near,
									distance, raceId, sessionId);
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
				progressDialog = new ProgressDialog(QuanZiActivity.this);
				Constant.showProgressDialog(progressDialog);
			}
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("MyCompetitionListUrl", url + "");
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

	// 网络请求
	private class DeleteRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(QuanZiActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("deleteQuanZiUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initGetInfoResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OKDelete;
						initGetInfoResult();
					} else {
						String message = "";
						if (!jo.isNull("message")) {
							message = jo.optString("message");
						}
						Log.e("message", "message" + message);
						PF = FailDelete;
						initGetInfoResult();
					}
				} catch (JSONException e) {
					PF = FailDelete;
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

	// 更新已读未读
	private class UpdateRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			// progressDialog = new ProgressDialog(QuanZiActivity.this);
			// Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("UpdateQuanZiUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initGetInfoResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OKUpdate;
						initGetInfoResult();
					} else {
						PF = FailUpdate;
						initGetInfoResult();
					}
				} catch (JSONException e) {
					PF = FailUpdate;
					initGetInfoResult();
					System.out.println("解析错误");
					e.printStackTrace();
				}
			}
			// Constant.exitProgressDialog(progressDialog);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}
}

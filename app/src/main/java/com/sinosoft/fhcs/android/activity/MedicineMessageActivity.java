package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description: 服药提醒消息页
 * @Author: wangshuangshuang.
 * @Create: 2015年2月12日.
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.MedicineMessageAdapter;
import com.sinosoft.fhcs.android.entity.MedicineMessageInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MedicineMessageActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, OnItemLongClickListener {
	private TextView tvTitle;
	private Button btnBack;// 返回
	private ListView listView;
	private MedicineMessageAdapter adapter;
	private List<MedicineMessageInfo> list = new ArrayList<MedicineMessageInfo>();
	// 请求数据
	private ProgressDialog progressDialog;// 进度条
	private static final int OKList = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int FailList = 1003;// 失败
	private static final int OKDelete = 1004;// 删除成功
	private static final int FailDelete = 1005;// 删除失败
	private static final int OKReadOne = 1006;// 单条已读成功
	private static final int FailReadOne = 1007;// 单条已读失败
	private static final int NoData = 1008;// 没有数据
	private int PF = 1000;
	private String userId = "";
	private int index = 1;// 删除id

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_sysmessage);
		ExitApplicaton.getInstance().addActivity(this);
		init();// 初始化控件
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources()
				.getString(R.string.title_medicinemessage));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.sysmessage_listview);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);

	}

	@Override
	protected void onStart() {
		list.clear();
		// 从首选项获取机顶盒编号
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		if (HttpManager.isNetworkAvailable(this)) {
			ListRequest re = new ListRequest();
			re.execute(HttpManager.urlSysMsg(userId, 1));
		} else {
//			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
		}
		super.onStart();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
								   int position, long id) {
		// 长按删除
		index = position;
		DeleteDialog();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		if (HttpManager.isNetworkAvailable(MedicineMessageActivity.this)) {
			list.get(position).setRead(true);
			adapter.notifyDataSetChanged();
			ReadOneRequest re = new ReadOneRequest();
			re.execute(HttpManager.urlMsgReadOne(userId, list.get(position)
					.getId()));
		} else {
			Toast.makeText(MedicineMessageActivity.this, "请检查您的网络连接",
					Toast.LENGTH_SHORT).show();
		}

	}

	// 删除
	private void DeleteDialog() {
		AlertDialog.Builder builder = new Builder(MedicineMessageActivity.this,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage("您确定删除这条消息吗？");
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (HttpManager
						.isNetworkAvailable(MedicineMessageActivity.this)) {
					DeleteRequest re = new DeleteRequest();
					re.execute(HttpManager.urlMsgDel(userId, list.get(index)
							.getId()));
				} else {
					Toast.makeText(MedicineMessageActivity.this, "请检查您的网络连接",
							Toast.LENGTH_SHORT).show();
				}
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
		MobclickAgent.onPageStart("服药提醒消息页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("服药提醒消息页");
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

	// 请求结果
	private void initResult() {
		if (PF == FailServer) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailList) {
			Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKList) {
			adapter = new MedicineMessageAdapter(this, list);
			listView.setAdapter(adapter);
		} else if (PF == NoData) {
			Toast.makeText(this, "目前没有数据!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKDelete) {
			// 通知adapter 更新
			list.remove(index);
			adapter.notifyDataSetChanged();
			Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
		} else if (PF == FailDelete) {
			Toast.makeText(this, "删除失败！", Toast.LENGTH_SHORT).show();
		} else if (PF == OKReadOne) {
			System.out.println("更新单条已读成功");
		} else if (PF == FailReadOne) {
			System.out.println("更新单条已读状态失败");
			// Toast.makeText(this, "更新单条已读状态失败！", Toast.LENGTH_SHORT).show();
		}

	}

	// 列表请求
	private class ListRequest extends AsyncTask<Object, Void, String> {
		private String urlList;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MedicineMessageActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			urlList = (String) params[0];
			Log.e("medicineMsgUrl", urlList + "");
			result = HttpManager.getStringContent(urlList);
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
						String entity = jo.optString("data");
						JSONObject jo2 = new JSONObject(entity);
						JSONArray ja = jo2.getJSONArray("historyList");
						if (ja.length() == 0) {
							PF = NoData;
							initResult();
						} else {
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo3 = ja.getJSONObject(i);
								String id = jo3.optString("id");
								String pushDate = jo3.optString("pushDate");
								boolean readState = jo3.optBoolean("readState");
								String reminderContent = jo3
										.optString("reminderContent");
								MedicineMessageInfo info = new MedicineMessageInfo(
										readState, id, pushDate,
										reminderContent);
								list.add(info);
							}
							PF = OKList;
							initResult();
						}

					} else {
						PF = FailList;
						initResult();
					}

				} catch (JSONException e) {
					PF = FailList;
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

	// 删除请求
	private class DeleteRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MedicineMessageActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("deleteMedicineMsgUrl", url + "");
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
						PF = OKDelete;
						initResult();
					} else {
						PF = FailDelete;
						initResult();
					}
				} catch (JSONException e) {
					PF = FailDelete;
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

	// 单条已读请求
	private class ReadOneRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MedicineMessageActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("readOneMedicineMsgUrl", url + "");
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
						PF = OKReadOne;
						initResult();
					} else {
						PF = FailReadOne;
						initResult();
					}
				} catch (JSONException e) {
					PF = FailReadOne;
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
}

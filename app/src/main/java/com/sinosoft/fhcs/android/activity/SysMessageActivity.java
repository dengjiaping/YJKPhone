package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description: 系统消息页
 * @Author: wangshuangshuang.
 * @Create: 2015年2月12日.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.MessageAdapter;
import com.sinosoft.fhcs.android.entity.MessageInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SysMessageActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;// 返回
	private ListView listView;
	private MessageAdapter adapter;
	private List<MessageInfo> list = new ArrayList<MessageInfo>();
	// 请求数据
	private ProgressDialog progressDialog;// 进度条
	private static final int OKList = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int FailList = 1003;// 失败
	private static final int NoData = 1004;// 没有数据
	private int PF = 1000;
	private String userId = "";

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
		tvTitle.setText(getResources().getString(R.string.title_sysmessage));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.sysmessage_listview);
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
			re.execute(HttpManager.urlSysMsg(userId, 3));
		} else {
//			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
		}
		super.onStart();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("系统消息页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("系统消息页");
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
			Toast.makeText(this, "服务器响应超时！", Toast.LENGTH_SHORT).show();
		} else if (PF == FailList) {
			Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == NoData) {
			Toast.makeText(this, "目前没有数据!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKList) {
			adapter = new MessageAdapter(this, list);
			listView.setAdapter(adapter);
		}

	}

	// 列表请求
	private class ListRequest extends AsyncTask<Object, Void, String> {
		private String urlList;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SysMessageActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			urlList = (String) params[0];
			Log.e("SysMsgUrl", urlList + "");
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
								MessageInfo info = new MessageInfo(readState,
										id, pushDate, reminderContent);
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
}

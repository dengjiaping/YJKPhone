package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:服务订购列表页
 * @Author: wangshuangshuang.
 * @Create: 2015年10月28日.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.OrderServiceListAdapter;
import com.sinosoft.fhcs.android.entity.OrderServiceInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceListActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private TextView tvTitle;
	private Button btnBack;// 返回
	private ImageView imageView;
	private ListView listView;
	private OrderServiceListAdapter adapter;
	private List<OrderServiceInfo> getList = new ArrayList<OrderServiceInfo>();
	private ProgressDialog progressDialog;// 进度条
	private static final int OKList = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int FailList = 1003;// 失败
	private static final int FailNoData = 1004;// 没有数据
	private int PF = 1000;
	private String cooperateIdentify = "xk";
	private String familyId = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_orderservicelist);
		ExitApplicaton.getInstance().addActivity(this);
		// 从首选项获取用户id
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		familyId = prefs.getString("familyId", "");
		cooperateIdentify=this.getIntent().getExtras().getString("cooperateIdentify");
		init();// 初始化控件
	}

	@Override
	protected void onResume() {
		super.onResume();
		initRequest();
		MobclickAgent.onPageStart("服务订购列表页"); // 统计页面
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("服务订购列表页");
	}

	// 网络请求
	private void initRequest() {
		if (!HttpManager.isNetworkAvailable(this)) {
			// Constant.showDialog(mActivity, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		getList.clear();
		ListRequest re = new ListRequest();
		re.execute(HttpManager.urlGetOrderServiceList(cooperateIdentify,
				familyId));

	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.orderservicelist_img);
		listView = (ListView) findViewById(R.id.orderservicelist_listview);
		listView.setOnItemClickListener(this);
		adapter = new OrderServiceListAdapter(this, familyId,getList);
		listView.setAdapter(adapter);
		if(cooperateIdentify.equals("tgj")){
			imageView.setBackgroundResource(R.drawable.icon_order_tgj);
			tvTitle.setText("糖管家");
		}else{
			imageView.setBackgroundResource(R.drawable.icon_order_xk);
			tvTitle.setText("东软熙康");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		Intent intent=new Intent(OrderServiceListActivity.this,WebViewActivity.class);
		intent.putExtra("name", getList.get(position).getTitle());
		intent.putExtra("url", getList.get(position).getUrl());
		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				finish();
				break;

			default:
				break;
		}

	}

	// 获取结果
	private void initGetInfoResult() {
		if (PF == FailServer) {
			// Constant.showDialog(mActivity, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailList) {
			Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailNoData) {
			Toast.makeText(this, "暂无数据!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKList) {
			// adapter = new OrderServiceListAdapter(this, getList);
			// listView.setAdapter(adapter);
			// adapter.notifyDataSetChanged();
		} else {
			// adapter.notifyDataSetChanged();
			Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
		}
		adapter.notifyDataSetChanged();
	}

	// 网络请求
	private class ListRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(OrderServiceListActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("OrderServiceListUrl", url + "");
			result = HttpManager.getStringContent(url);
			System.out.println("返回的订购信息====="+result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initGetInfoResult();
			} else {
				getList = jiexi(result);
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

	// 解析数据
	private List<OrderServiceInfo> jiexi(String result) {
		try {
			JSONObject jo = new JSONObject(result);
			String resultCode = jo.optString("resultCode");
			if (resultCode.equals("1")) {
				String entity = jo.optString("data");
				JSONObject jo2 = new JSONObject(entity);
				JSONArray ja = jo2.getJSONArray("data");
				if (ja.length() != 0) {
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo3 = ja.getJSONObject(i);
						String id = jo3.optString("id");
						String price = "";
						if (!jo3.isNull("price")) {
							price = jo3.optString("price");
						}
						boolean isbuy = false;
						if (!jo3.isNull("isbuy")) {
							isbuy = jo3.optBoolean("isbuy");
						}
						String description = "";
						if (!jo3.isNull("description")) {
							description = jo3.optString("description");
						}
						String title = "";
						if (!jo3.isNull("title")) {
							title = jo3.optString("title");
						}
						String orderBeginDate = "";
						if (!jo3.isNull("orderBeginDate")) {
							orderBeginDate = jo3.optString("orderBeginDate");
						}
						String orderEndDate = "";
						if (!jo3.isNull("orderEndDate")) {
							orderEndDate = jo3.optString("orderEndDate");
						}
						boolean isTuiGuang = true;
//						if (price.equals("") || price.equals("0")) {
//							isTuiGuang = true;
//						} else {
//							isTuiGuang = false;
//						}
						String date="";
						if(!orderBeginDate.equals("")&&!orderEndDate.equals("")){
							date=orderBeginDate+"-"+orderEndDate;
						}
						String url="http://www.baidu.com";
						if(!jo3.isNull("serviceDetailUrl")){
							url=jo3.optString("serviceDetailUrl");
						}
						OrderServiceInfo info = new OrderServiceInfo(id,
								cooperateIdentify, title, description, price,
								isbuy, isTuiGuang, date,url);
						getList.add(info);
					}
					PF = OKList;
					initGetInfoResult();
				} else {
					PF = FailNoData;
					initGetInfoResult();
				}
			} else {
				PF = FailList;
				initGetInfoResult();
			}
		} catch (JSONException e) {
			PF = FailList;
			initGetInfoResult();
		}
		return getList;

	}
}

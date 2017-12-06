package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:设备管理页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月8日.
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
import com.sinosoft.fhcs.android.adapter.EquipmentlistAdapter;
import com.sinosoft.fhcs.android.entity.EquipmentListInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class SparePartsActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {
	private TextView tvTitle;
	private Button btnBack;// 返回
	private Button btnAdd;
	private Button btnHelp;// 帮助
	private ListView listview;
	private EquipmentlistAdapter adapter;
	private String userId = "";// 用户id
	private List<EquipmentListInfo> getList = new ArrayList<EquipmentListInfo>();
	/**
	 * 网络请求
	 */
	private ProgressDialog progressDialog;// 进度条
	private static final int OKGet = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int FailGet = 1003;// 失败
	private static final int FailNoData = 1004;// 没有数据
	private int PF = 1000;

	private static final int OKUnBind = 2001;// 成功
	private static final int FailServerUnBind = 2002;// 连接超时
	private static final int FailUnBind = 2003;// 失败
	private int PFUnBind = 2000;
	private int position = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_equipmentlist);
		ExitApplicaton.getInstance().addActivity(this);
		// 从首选项获取用户id
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();
	}

	@Override
	public void onResume() {
		super.onResume();
		initRequest();
		MobclickAgent.onPageStart("配件管理页"); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("配件管理页");
	}

	// 网络请求
	private void initRequest() {
		position=0;
		if (!HttpManager.isNetworkAvailable(this)) {
			// Constant.showDialog(mActivity, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		GetEquipmentListRequest re = new GetEquipmentListRequest();
		re.execute(HttpManager.urlGetEquipmentList(userId));

	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_equipmentlist));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		btnAdd = (Button) findViewById(R.id.titlebar_btn_add);
		btnAdd.setVisibility(View.VISIBLE);
		btnAdd.setOnClickListener(this);
		btnHelp = (Button) findViewById(R.id.equiplist_btn_help);
		btnHelp.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.equiplist_listview);
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		Intent intent6 = new Intent(this, BraceletSportActivity.class);
		intent6.putExtra("memberId", getList.get(position).getFamilyMember_id());
		intent6.putExtra("memberName", getList.get(position)
				.getFamilyMemberRoleName());
		intent6.putExtra("deviceName", getList.get(position).getDeviceName());
		intent6.putExtra("accessToken", getList.get(position).getAccessToken());
		intent6.putExtra("appCode", getList.get(position).getAppCode() + "");
		startActivity(intent6);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
								   int arg2, long id) {
		position = arg2;
		if (HttpManager.isNetworkAvailable(this)) {
			showDialog();
		}else{
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	private void showDialog() {
		new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
				.setCancelable(false).setTitle("温馨提示")
				.setMessage("您确定要解绑该设备吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						UnBindRequest re=new UnBindRequest();
						re.execute(HttpManager.urlUnBind(getList.get(position).getFamilyMember_id()+""));
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.titlebar_btn_add:
				// 绑定设备
				Intent intent = new Intent(this, BindEquipmentActivity.class);
				startActivity(intent);
				break;
			case R.id.equiplist_btn_help:
				// 帮助
//			Toast.makeText(this, "帮助", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
		}

	}

	// 请求结果
	private void initResult() {
		if (PF == FailServer) {
			// Constant.showDialog(mActivity, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailGet) {
			Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailNoData) {
			Toast.makeText(this, "目前还没有设备数据，请添加!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKGet) {
			adapter = new EquipmentlistAdapter(this, getList);
			listview.setAdapter(adapter);
			Constant.setListViewHeight(listview);// 解决ScrollView和ListView在嵌套使用时冲突的问题
			// ,在setAdapter后调用。
		}

	}
	private void initResultUnBind() {
		if(PFUnBind==FailServerUnBind){
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		}else if(PFUnBind==FailUnBind){
			Toast.makeText(this, "解绑失败!", Toast.LENGTH_SHORT).show();
		}else if(PFUnBind==OKUnBind){
			getList.remove(position);
			adapter = new EquipmentlistAdapter(this, getList);
			listview.setAdapter(adapter);
			Constant.setListViewHeight(listview);
			Toast.makeText(this, "解绑成功!", Toast.LENGTH_SHORT).show();
		}

	}
	// 网络请求
	private class GetEquipmentListRequest extends
			AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SparePartsActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getEquipmentListUrl", url + "");
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
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						JSONArray ja = jo.optJSONArray("data");
						if (ja.length() == 0) {
							PF = FailNoData;
							initResult();
						} else {
							getList.clear();
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo4 = ja.getJSONObject(i);
								String deviceId = jo4.optString("deviceId");
								String deviceName = jo4.optString("deviceName");
								String familyMember_id = jo4
										.optString("familyMember_id");
								String familyMemberRoleName = jo4
										.optString("familyMemberRoleName");
								String appCode = jo4.optString("appCode");
								String imgUrl = jo4.optString("imgUrl");
								String syncDataTime = "";
								if (!jo4.isNull("syncDataTime")) {
									syncDataTime = jo4
											.optString("syncDataTime");
								}
								String accessToken = jo4
										.optString("accessToken");

								EquipmentListInfo info = new EquipmentListInfo(
										deviceId, deviceName, familyMember_id,
										familyMemberRoleName, appCode,
										HttpManager.m_imageUrl + imgUrl,
										syncDataTime, accessToken);
								getList.add(info);
							}
							PF = OKGet;
							initResult();
						}
					} else {
						PF = FailNoData;
						initResult();
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = FailGet;
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

	// 解绑手环
	private class UnBindRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SparePartsActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("unBindUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PFUnBind = FailServerUnBind;
				initResultUnBind();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						PFUnBind = OKUnBind;
						initResultUnBind();
					} else {
						PFUnBind = FailUnBind;
						initResultUnBind();
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					PFUnBind = FailUnBind;
					initResultUnBind();
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

package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description: 所有成员的服药提醒列表页
 * @Author: wangshuangshuang.
 * @Create: 2015年6月3日.
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
import com.sinosoft.fhcs.android.adapter.RemindHomeAdapter;
import com.sinosoft.fhcs.android.customview.XListView;
import com.sinosoft.fhcs.android.customview.XListView.IXListViewListener;
import com.sinosoft.fhcs.android.entity.RemindListInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RemindHomeActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, OnItemLongClickListener,
		IXListViewListener {
	private final String TAG = "RemindListActivity";
	private TextView tvTitle;
	private Button btnBack;
	private Button btnMessage;
	private Button btnAdd;// 添加提醒
	private XListView listView;
	private RemindHomeAdapter adapter;
	private List<RemindListInfo> list = new ArrayList<RemindListInfo>();
	private RemindListInfo info = new RemindListInfo();;
	private Handler mHandler;
	private static final int OKList = 1001;// 成功
	private static final int FailList = 1002;// 失败
	private static final int OKDelete = 1004;// 删除成功
	private static final int FailDelete = 1005;// 删除失败
	private static final int ChaoShi = 1003;// 超时
	private int PF = 1000;
	private ProgressDialog progressDialog;// 进度条
	private int pageNum = 1;// 页数
	private int pageSize = 10;// 条数
	private int pageTotal = 1;// 总数
	private int pageCount = 1;// 总条数
	private boolean isFirst = false;
	private boolean FlagIsMore = false;
	private int index = 1;// 删除id
	private String userId = "";
	// 家庭列表网络请求
	private static final int OK = 2001;// 成功
	private static final int FailServer = 2002;// 连接超时
	private static final int Fail = 2003;// 失败
	private static final int FailNoData = 2004;// 没有数据
	private List<Object> getList = new ArrayList<Object>();// 不包括添加按钮的数据
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_remindhome);
		ExitApplicaton.getInstance().addActivity(this);
		// 从首选项获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("服药提醒列表页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("服药提醒列表页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStart() {
		pageNum = 1;
		FlagIsMore = false;
		isFirst = false;
		if (!HttpManager.isNetworkAvailable(this)) {
			// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		list.clear();
		ListRequest request = new ListRequest();
		request.execute(HttpManager.urlRemindHomeNew(userId, 1, pageSize));
		super.onStart();
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_remindhome));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		btnMessage = (Button) findViewById(R.id.titlebar_btn_message);
		btnMessage.setVisibility(View.VISIBLE);
		btnMessage.setOnClickListener(this);
		btnAdd = (Button) findViewById(R.id.remindhome_btn);
		btnAdd.setOnClickListener(this);
		listView = (XListView) findViewById(R.id.remindhome_listview);
		listView.setPullLoadEnable(true);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		listView.setXListViewListener(this);
		adapter = new RemindHomeAdapter(this, list);
		listView.setAdapter(adapter);
		mHandler = new Handler();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// 跳转到提醒详情页
		arg2 -= listView.getHeaderViewsCount();
		if (list.get(arg2).isExpired()) {
			// 过期
			// Constant.showDialog(RemindListActivity.this, "这条提醒已经过期了，不能修改哦！");
			Toast.makeText(this, "这条提醒已经过期了，不能修改哦！", Toast.LENGTH_SHORT).show();
		} else {
			EditDialog(arg2);
		}
	}

	// 修改
	private void EditDialog(final int position) {
		AlertDialog.Builder builder = new Builder(RemindHomeActivity.this,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage("您确定要修改这条提醒吗？");
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(RemindHomeActivity.this,
						EditRemindActivity.class);
				intent.putExtra("info", list.get(position));
				startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.remindhome_btn:
				// 添加
				if (HttpManager.isNetworkAvailable(this)) {
					GetFamilyListRequest re = new GetFamilyListRequest();
					re.execute(HttpManager.urlFamilyList(userId));
				} else {
					// Constant.showDialog(getActivity(), "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！",
							Toast.LENGTH_SHORT).show();
				}
				// Intent intent = new Intent(this, AddRemindActivity.class);
				// intent.putExtra("fmId", member.getId() + "");
				// startActivity(intent);
				break;
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.titlebar_btn_message:
				//消息
				Intent intent2 = new Intent(this,
						MedicineMessageActivity.class);
				startActivity(intent2);
				break;
			default:
				break;
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
								   long arg3) {
		// 长按删除
		arg2 -= listView.getHeaderViewsCount();
		index = arg2;
		DeleteDialog();
		return true;
	}

	// 删除
	private void DeleteDialog() {
		AlertDialog.Builder builder = new Builder(RemindHomeActivity.this,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage("您确定删除这条提醒吗？");
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				DeleteRequest re = new DeleteRequest();
				re.execute(HttpManager.urlDeleteRemind(list.get(index).getId()));
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
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
		pageNum = 1;
		FlagIsMore = false;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				list.clear();
				ListRequest request = new ListRequest();
				request.execute(HttpManager.urlRemindHomeNew(userId, 1,
						pageSize));
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		pageNum++;
		FlagIsMore = true;
		Log.i(TAG, "加载更多");
		if (pageNum > pageTotal) {
			pageNum = pageTotal;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(RemindHomeActivity.this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
					onLoad();
				}
			}, 2000);

		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					ListRequest request = new ListRequest();
					request.execute(HttpManager.urlRemindHomeNew(userId,
							pageNum, pageSize));
					onLoad();
				}
			}, 2000);
		}
	}

	// 获取结果
	private void initGetInfoResult() {
		if (PF == ChaoShi) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailList) {
			if (FlagIsMore == false) {
				Toast.makeText(this, "没有数据,请添加！", Toast.LENGTH_SHORT).show();
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
			}

		} else if (PF == OKList) {
			if (FlagIsMore == false) {
				adapter = new RemindHomeAdapter(this, list);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		} else if (PF == OKDelete) {
			// 通知adapter 更新
			list.remove(index);
			adapter.notifyDataSetChanged();
			// adapter = new RemindHomeAdapter(RemindListActivity.this, list,
			// false);
			// listView.setAdapter(adapter);
			Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();

		} else if (PF == FailDelete) {
			Toast.makeText(this, "删除失败！", Toast.LENGTH_SHORT).show();
		}

	}

	// 解析数据
	private List<RemindListInfo> jiexi(String result) {
		try {
			JSONObject jo = new JSONObject(result);
			String resultCode = jo.optString("status");
			if (resultCode.equals("0000")) {
				String entity = jo.optString("data");
				JSONObject jo2 = new JSONObject(entity);
				pageCount = jo2.optInt("totalCount");
				if ((pageCount % pageSize) == 0) {
					pageTotal = pageCount / pageSize;
				} else {
					pageTotal = pageCount / pageSize + 1;
				}
				if (!jo2.isNull("medicineReminders")) {
					JSONArray ja = jo2.getJSONArray("medicineReminders");
					if (ja.length() != 0) {
						for (int i = 0; i < ja.length(); i++) {
							JSONObject jo3 = ja.getJSONObject(i);
							String id = jo3.optString("id");
							String dosage = "";
							if (!jo3.isNull("dosage")) {
								dosage = jo3.optString("dosage");
							}
							String startTime = "";
							if (!jo3.isNull("startTime")) {
								startTime = jo3.optString("startTime");
							}
							String endTime = "";
							if (!jo3.isNull("endTime")) {
								endTime = jo3.optString("endTime");
							}
							String reminderTime = "";
							if (!jo3.isNull("reminderTime")) {
								reminderTime = jo3.optString("reminderTime");
							}
							String medicineName = "";
							if (!jo3.isNull("medicineName")) {
								medicineName = jo3.optString("medicineName");
							}
							String reminderByMeal = "";
							if (!jo3.isNull("reminderByMeal")) {
								reminderByMeal = jo3
										.optString("reminderByMeal");
							}
							String reminderWay = "";
							if (!jo3.isNull("reminderWay")) {
								reminderWay = jo3.optString("reminderWay");
							}
							String phoneNumber = "";
							if (!jo3.isNull("phoneNumber")) {
								phoneNumber = jo3.optString("phoneNumber");
							}
							boolean expired = jo3.optBoolean("expires");

							String familyMember_id=jo3.optString("familyMember_id");
							String familyMemberRoleName=jo3.optString("familyMemberRoleName");
							String gender=jo3.optString("gender");
							info = new RemindListInfo(id, startTime, endTime,
									reminderTime, familyMember_id,
									familyMemberRoleName,
									gender, medicineName, dosage,
									reminderWay, phoneNumber, reminderByMeal,
									pageTotal+"", expired);
							list.add(info);
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
			// TODO Auto-generated catch block
			Log.e(TAG, "解析失败！");
		}
		return list;

	}

	// 网络请求
	private class ListRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			if (isFirst == false) {
				progressDialog = new ProgressDialog(RemindHomeActivity.this);
				Constant.showProgressDialog(progressDialog);
			}
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("MemberRemindListUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initGetInfoResult();
			} else {
				list = jiexi(result);
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
			progressDialog = new ProgressDialog(RemindHomeActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("deleteRemindUrl", url + "");
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
						String errormsg = "";
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
						String message = "";
						if (!jo.isNull("message")) {
							message = jo.optString("message");
						}
						Log.e("errormsg", "errormsg" + errormsg);
						Log.e("message", "message" + message);
						PF = FailDelete;
						initGetInfoResult();
					}
				} catch (JSONException e) {
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
	private void initResult() {
		if (PF == FailServer) {
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT)
					.show();
		} else if (PF == Fail) {
			Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			Intent intent = new Intent(this, AddRemindActivity.class);
			intent.putExtra("list", (Serializable)getList);
			startActivity(intent);
			// Toast.makeText(this, "获取成功！", Toast.LENGTH_SHORT).show();
		} else if (PF == FailNoData) {
			Toast.makeText(this, "目前还没有家庭成员，请添加!", Toast.LENGTH_SHORT)
					.show();
		}

	}
	// 网络请求
	private class GetFamilyListRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(RemindHomeActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getListUrl", url + "");
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
						JSONObject jo2 = jo.getJSONObject("data");
						JSONArray ja = jo2.getJSONArray("FamilyMemberList");
						if (ja.length() == 0) {
							PF = FailNoData;
							initResult();
						} else {
							getList.clear();
							getList = HttpManager.getFamilyList(ja);
							PF = OK;
							initResult();
						}
					} else {
						PF = Fail;
						initResult();
					}
				} catch (JSONException e) {
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

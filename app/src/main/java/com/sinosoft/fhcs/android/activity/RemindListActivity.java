//package com.sinosoft.fhcs.android.activity;
//
///**
// * @CopyRight: SinoSoft.
// * @Description: 单个成员的服药提醒列表页  废弃页面
// * @Author: wangshuangshuang.
// * @Create: 2014年8月15日.
// */
//import java.util.ArrayList;
//import java.util.List;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.sinosoft.fhcs.android.ExitApplicaton;
//import com.sinosoft.fhcs.android.adapter.RemindListAdapter;
//import com.sinosoft.fhcs.android.customview.MyMenuPopupWindow;
//import com.sinosoft.fhcs.android.customview.XListView;
//import com.sinosoft.fhcs.android.customview.XListView.IXListViewListener;
//import com.sinosoft.fhcs.android.entity.FamilyMember;
//import com.sinosoft.fhcs.android.entity.RemindListInfo;
//import com.sinosoft.fhcs.android.R;
//import com.sinosoft.fhcs.android.util.Constant;
//import com.sinosoft.fhcs.android.util.HttpManager;
//import com.umeng.analytics.MobclickAgent;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.app.AlertDialog.Builder;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class RemindListActivity extends Activity implements
//		OnItemClickListener, OnClickListener, OnItemLongClickListener,
//		IXListViewListener {
//	private final String TAG = "RemindListActivity";
//	private TextView tvTitle;
//	private LinearLayout titleLayout;
//	private Button btnBack;
//	private Button btnMenu;
//	private Button btnAdd;// 添加提醒
//	private XListView listView;
//	private TextView tvNickName;
//	private ImageView img;
//	private RemindListAdapter adapter;
//	private List<RemindListInfo> list = new ArrayList<RemindListInfo>();
//	private RemindListInfo info = new RemindListInfo();;
//	private FamilyMember member = new FamilyMember();
//	private Handler mHandler;
//	private static final int OKList = 1001;// 成功
//	private static final int FailList = 1002;// 失败
//	private static final int OKDelete = 1004;// 删除成功
//	private static final int FailDelete = 1005;// 删除失败
//	private static final int ChaoShi = 1003;// 超时
//	private int PF = 1000;
//	private ProgressDialog progressDialog;// 进度条
//	private int page = 1;// 页数
//	private boolean FlagIsMore = false;
//	private boolean isFirst=false;
//	private int index = 1;// 删除id
//	// 自定义的弹出框类
//	private MyMenuPopupWindow menuWindow;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_remindlist);
//		ExitApplicaton.getInstance().addActivity(this);
//		member = (FamilyMember) this.getIntent().getExtras().get("member");
//		init();
//	}
//	public void onResume() {
//	    super.onResume();
//	    MobclickAgent.onPageStart("服药提醒列表页"); //统计页面
//	    MobclickAgent.onResume(this);          //统计时长
//	}
//	public void onPause() {
//	    super.onPause();
//	    MobclickAgent.onPageEnd("服药提醒列表页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
//	    MobclickAgent.onPause(this);
//	}
//	@Override
//	protected void onStart() {
//		page = 1;
//		FlagIsMore = false;
//		isFirst=false;
//		if (!HttpManager.isNetworkAvailable(this)) {
////			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
//			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		list.clear();
//		ListRequest request = new ListRequest();
//		request.execute(HttpManager.urlRemindList(member.getId() + "", 1));
//		super.onStart();
//	}
//
//	private void init() {
//		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
//		tvTitle.setText(getResources().getString(R.string.title_remind));
//		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
//		btnBack.setVisibility(View.VISIBLE);
//		btnBack.setOnClickListener(this);
//		titleLayout=(LinearLayout) findViewById(R.id.titlebar_layout_remind);
//		titleLayout.setVisibility(View.VISIBLE);
//		btnMenu = (Button) findViewById(R.id.titlebar_btn_remind_menu);
//		btnMenu.setOnClickListener(this);
//		btnAdd = (Button) findViewById(R.id.titlebar_btn_remind_add);
//		btnAdd.setOnClickListener(this);
//		tvNickName=(TextView) findViewById(R.id.remindlist_tv_nickname);
//		img=(ImageView) findViewById(R.id.remindlist_img);
//		tvNickName.setText(member.getFamilyRoleName());
//		img.setImageResource(Constant.ImageIdbg(member.getFamilyRoleName(), member.getGender()));
//		listView = (XListView) findViewById(R.id.remindlist_listview);
//		listView.setPullLoadEnable(true);
//		listView.setOnItemClickListener(this);
//		listView.setOnItemLongClickListener(this);
//		listView.setXListViewListener(this);
//		adapter = new RemindListAdapter(this, list);
//		listView.setAdapter(adapter);
//		mHandler = new Handler();
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		// 跳转到提醒详情页
//		arg2 -= listView.getHeaderViewsCount();
//		if (list.get(arg2).isExpired()) {
//			// 过期
////			Constant.showDialog(RemindListActivity.this, "这条提醒已经过期了，不能修改哦！");
//			Toast.makeText(this, "这条提醒已经过期了，不能修改哦！", Toast.LENGTH_SHORT).show();
//		} else {
//			EditDialog(arg2);
//		}
//	}
//
//	// 修改
//	private void EditDialog(final int position) {
//			AlertDialog.Builder builder = new Builder(RemindListActivity.this,
//					AlertDialog.THEME_HOLO_LIGHT);
//			builder.setMessage("您确定要修改这条提醒吗？");
//			builder.setTitle("温馨提示");
//			builder.setPositiveButton("确定",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							Intent intent = new Intent(RemindListActivity.this,
//									EditRemindActivity.class);
//							intent.putExtra("info", list.get(position));
//							startActivity(intent);
//						}
//					});
//			builder.setNegativeButton("取消",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//
//						}
//					});
//			builder.create().show();
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.titlebar_btn_remind_add:
//			// 添加
//			Intent intent = new Intent(this, AddRemindActivity.class);
//			intent.putExtra("fmId", member.getId() + "");
//			startActivity(intent);
//			break;
//		case R.id.titlebar_btn_back:
//			// 返回
//			finish();
//			break;
//		case R.id.titlebar_btn_remind_menu:
//			// 菜单
//			menuWindow = new MyMenuPopupWindow(this, member);
//			menuWindow.showAtLocation(btnMenu, Gravity.TOP | Gravity.RIGHT, 0,
//					0);
//			break;
//		default:
//			break;
//		}
//
//	}
//
//	@Override
//	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
//			long arg3) {
//		// 长按删除
//		arg2 -= listView.getHeaderViewsCount();
//		index = arg2;
//		DeleteDialog();
//		return true;
//	}
//
//	// 删除
//	private void DeleteDialog() {
//			AlertDialog.Builder builder = new Builder(RemindListActivity.this,
//					AlertDialog.THEME_HOLO_LIGHT);
//			builder.setMessage("您确定删除这条提醒吗？");
//			builder.setTitle("温馨提示");
//			builder.setPositiveButton("确定",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							DeleteRequest re = new DeleteRequest();
//							re.execute(HttpManager.urlDeleteRemind(list.get(
//									index).getId()));
//						}
//					});
//			builder.setNegativeButton("取消",
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//
//						}
//					});
//			builder.create().show();
//	}
//
//	private void onLoad() {
//		listView.stopRefresh();
//		listView.stopLoadMore();
//		listView.setRefreshTime(Constant
//				.getNowDateStrByFormate("yyyy-MM-dd HH:mm:ss"));
//	}
//
//	@Override
//	public void onRefresh() {
//		Log.i(TAG, "刷新最新");
//		page = 1;
//		FlagIsMore = false;
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				list.clear();
//				ListRequest request = new ListRequest();
//				request.execute(HttpManager.urlRemindList(member.getId() + "",
//						1));
//				onLoad();
//			}
//		}, 2000);
//	}
//
//	@Override
//	public void onLoadMore() {
//		FlagIsMore = true;
//		page++;
//		Log.i(TAG, "加载更多");
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				ListRequest request = new ListRequest();
//				request.execute(HttpManager.urlRemindList(member.getId() + "",
//						page));
//				onLoad();
//			}
//		}, 2000);
//
//	}
//
//	// 获取结果
//	private void initGetInfoResult() {
//		if (PF == ChaoShi) {
////			Constant.showDialog(this, "服务器响应超时!");
//			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
//		} else if (PF == FailList) {
//			if (FlagIsMore == false) {
//				Toast.makeText(this, "没有数据,请添加！", Toast.LENGTH_SHORT).show();
//				adapter.notifyDataSetChanged();
//			} else {
//				Toast.makeText(this, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
//			}
//
//		} else if (PF == OKList) {
//			if (FlagIsMore == false) {
//				adapter = new RemindListAdapter(this, list);
//				listView.setAdapter(adapter);
//			} else {
//				adapter.notifyDataSetChanged();
//			}
//		} else if (PF == OKDelete) {
//			// 通知adapter 更新
//			list.remove(index);
//			adapter.notifyDataSetChanged();
//			// adapter = new RemindHomeAdapter(RemindListActivity.this, list,
//			// false);
//			// listView.setAdapter(adapter);
//			Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
//
//		} else if (PF == FailDelete) {
//			Toast.makeText(this, "删除失败！", Toast.LENGTH_SHORT).show();
//		}
//
//	}
//
//	// 解析数据
//	private List<RemindListInfo> jiexi(String result) {
//		try {
//			JSONObject jo = new JSONObject(result);
//			String resultCode = jo.optString("resultCode");
//			if (resultCode.equals("1")) {
//				String entity = jo.optString("data");
//				JSONObject jo2 = new JSONObject(entity);
//				String pageCount = jo2.optString("pageCount");
//				if (!jo2.isNull("data")) {
//					JSONArray ja = jo2.getJSONArray("data");
//					if (ja.length() != 0) {
//						for (int i = 0; i < ja.length(); i++) {
//							JSONObject jo3 = ja.getJSONObject(i);
//							String id = jo3.optString("id");
//							String dosage = "";
//							if (!jo3.isNull("dosage")) {
//								dosage = jo3.optString("dosage");
//							}
//							String startTime = "";
//							if (!jo3.isNull("startTime")) {
//								startTime = jo3.optString("startTime");
//							}
//							String endTime = "";
//							if (!jo3.isNull("endTime")) {
//								endTime = jo3.optString("endTime");
//							}
//							String reminderTime = "";
//							if (!jo3.isNull("reminderTime")) {
//								reminderTime = jo3.optString("reminderTime");
//							}
//							String medicineName = "";
//							if (!jo3.isNull("medicineName")) {
//								medicineName = jo3.optString("medicineName");
//							}
//							String reminderByMeal = "";
//							if (!jo3.isNull("reminderByMeal")) {
//								reminderByMeal = jo3
//										.optString("reminderByMeal");
//							}
//							String reminderWay = "";
//							if (!jo3.isNull("reminderWay")) {
//								reminderWay = jo3.optString("reminderWay");
//							}
//							String phoneNumber = "";
//							if (!jo3.isNull("phoneNumber")) {
//								phoneNumber = jo3.optString("phoneNumber");
//							}
//							boolean expired = jo3.getBoolean("expired");
//							info = new RemindListInfo(id, startTime, endTime,
//									reminderTime, member.getId() + "",
//									member.getFamilyRoleName(),
//									member.getGender(), medicineName, dosage,
//									reminderWay, phoneNumber, reminderByMeal,
//									pageCount, expired);
//							list.add(info);
//						}
//						PF = OKList;
//						initGetInfoResult();
//					} else {
//						PF = FailList;
//						initGetInfoResult();
//					}
//
//				} else {
//					PF = FailList;
//					initGetInfoResult();
//				}
//
//			} else {
//				PF = FailList;
//				initGetInfoResult();
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			Log.e(TAG, "解析失败！");
//		}
//		return list;
//
//	}
//
//	// 网络请求
//	private class ListRequest extends AsyncTask<Object, Void, String> {
//		private String url;
//
//		@Override
//		protected void onPreExecute() {
//			if(isFirst==false){
//				progressDialog = new ProgressDialog(RemindListActivity.this);
//				Constant.showProgressDialog(progressDialog);
//			}
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(Object... params) {
//			String result = "";
//			url = (String) params[0];
//			Log.e("MemberRemindListUrl", url + "");
//			result = HttpManager.getStringContent(url);
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (result.toString().trim().equals("ERROR")) {
//				PF = ChaoShi;
//				initGetInfoResult();
//			} else {
//				list = jiexi(result);
//			}
//			if(isFirst==false){
//				Constant.exitProgressDialog(progressDialog);
//			}
//			isFirst=true;
//			super.onPostExecute(result);
//		}
//
//		@Override
//		protected void onCancelled() {
//			// TODO Auto-generated method stub
//			super.onCancelled();
//		}
//	}
//
//	// 网络请求
//	private class DeleteRequest extends AsyncTask<Object, Void, String> {
//		private String url;
//
//		@Override
//		protected void onPreExecute() {
//			progressDialog = new ProgressDialog(RemindListActivity.this);
//			Constant.showProgressDialog(progressDialog);
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(Object... params) {
//			String result = "";
//			url = (String) params[0];
//			Log.e("deleteRemindUrl", url + "");
//			result = HttpManager.getStringContent(url);
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (result.toString().trim().equals("ERROR")) {
//				PF = ChaoShi;
//				initGetInfoResult();
//			} else {
//				try {
//					JSONObject jo = new JSONObject(result);
//					String resultCode = jo.optString("resultCode");
//					if (resultCode.equals("1")) {
//						PF = OKDelete;
//						initGetInfoResult();
//					} else {
//						String errormsg = "";
//						if (!jo.isNull("errormsg")) {
//							errormsg = jo.optString("errormsg");
//						}
//						String message = "";
//						if (!jo.isNull("message")) {
//							message = jo.optString("message");
//						}
//						Log.e("errormsg", "errormsg" + errormsg);
//						Log.e("message", "message" + message);
//						PF = FailDelete;
//						initGetInfoResult();
//					}
//				} catch (JSONException e) {
//					System.out.println("解析错误");
//					e.printStackTrace();
//				}
//			}
//			Constant.exitProgressDialog(progressDialog);
//			super.onPostExecute(result);
//		}
//
//		@Override
//		protected void onCancelled() {
//			// TODO Auto-generated method stub
//			super.onCancelled();
//		}
//	}
//}

//package com.sinosoft.fhcs.android.activity;
//
///**
// * @CopyRight: SinoSoft.
// * @Description: 资讯厂商页 废弃页面
// * @Author: wangshuangshuang.
// * @Create: 2015年2月13日.
// */
//import java.util.ArrayList;
//import java.util.List;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import com.sinosoft.fhcs.android.ExitApplicaton;
//import com.sinosoft.fhcs.android.R;
//import com.sinosoft.fhcs.android.adapter.InformationThirdAdapter;
//import com.sinosoft.fhcs.android.customview.MyMenuPopupWindow;
//import com.sinosoft.fhcs.android.entity.FamilyMember;
//import com.sinosoft.fhcs.android.entity.InformationThird;
//import com.sinosoft.fhcs.android.util.Constant;
//import com.sinosoft.fhcs.android.util.HttpManager;
//import com.umeng.analytics.MobclickAgent;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//
//public class InformationThirdActivity extends Activity implements
//		OnClickListener, OnItemClickListener {
//	private TextView tvTitle;
//	private Button btnBack;// 返回
//	private Button btnMenu;// 菜单
//	private ListView listView;
//	private InformationThirdAdapter adapter;
//	private List<InformationThird> list = new ArrayList<InformationThird>();
//	// 请求数据
//	private ProgressDialog progressDialog;// 进度条
//	private static final int OKList = 1001;// 成功
//	private static final int FailServer = 1002;// 连接超时
//	private static final int FailList = 1003;// 失败
//	private static final int NoData = 1004;// 没有数据
//	private int PF = 1000;
//	private String userId = "";
//	private String memberId = "memberId";
//	// 自定义的弹出框类
//	private MyMenuPopupWindow menuWindow;
//	private FamilyMember member = new FamilyMember();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_sysmessage);
//		ExitApplicaton.getInstance().addActivity(this);
//		memberId = getIntent().getExtras().getString("memberId");
//		if (!memberId.equals("memberId")) {
//			member = (FamilyMember) this.getIntent().getExtras().get("member");
//		}
//		init();// 初始化控件
//	}
//
//	private void init() {
//		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
//		tvTitle.setText(getResources().getString(R.string.title_information));
//		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
//		btnBack.setVisibility(View.VISIBLE);
//		btnBack.setOnClickListener(this);
//		btnMenu = (Button) findViewById(R.id.titlebar_btn_memu);
//		btnMenu.setOnClickListener(this);
//		if (memberId.equals("memberId")) {
//			btnMenu.setVisibility(View.GONE);
//		} else {
//			btnMenu.setVisibility(View.VISIBLE);
//		}
//		listView = (ListView) findViewById(R.id.sysmessage_listview);
//		listView.setOnItemClickListener(this);
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		if(list.get(position).isBuy()){
//			Intent intent = new Intent(this, InformationActivity.class);
//			intent.putExtra("memberId", memberId);
//			intent.putExtra("cooperateIdentify", list.get(position)
//					.getCooperateIdentify());
//			startActivity(intent);
//		}else{
//			//没有订购服务
//			Toast.makeText(this, "您还没有订购过“"+list.get(position).getFacilitatorName()+"”的服务，如需订购，请使用翼家康机顶盒客户端。", Toast.LENGTH_LONG).show();
//		}
//
//	}
//
//	@Override
//	protected void onStart() {
//		list.clear();
//		// 从首选项获取机顶盒编号
//		SharedPreferences prefs = getSharedPreferences("UserInfo",
//				Context.MODE_PRIVATE);
//		userId = prefs.getString("userId", "");
//		if (HttpManager.isNetworkAvailable(this)) {
//			ListRequest re = new ListRequest();
//			if (!memberId.equals("memberId")) {
//				re.execute(HttpManager.urlGetInformationThirdByMember(userId, memberId));
//			}else{
//				re.execute(HttpManager.urlGetInformationThird(userId));
//			}
//		} else {
////			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
//			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
//		}
//		super.onStart();
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		MobclickAgent.onPageStart("资讯厂商页"); // 统计页面
//		MobclickAgent.onResume(this); // 统计时长
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		MobclickAgent.onPageEnd("资讯厂商页");
//		MobclickAgent.onPause(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.titlebar_btn_memu:
//			// 菜单
//			menuWindow = new MyMenuPopupWindow(this, member);
//			menuWindow.showAtLocation(btnMenu, Gravity.TOP | Gravity.RIGHT, 0,
//					0);
//			break;
//		case R.id.titlebar_btn_back:
//			// 返回
//			finish();
//			break;
//		default:
//			break;
//		}
//	}
//
//	// 请求结果
//	private void initResult() {
//		if (PF == FailServer) {
////			Constant.showDialog(this, "服务器响应超时!");
//			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
//		} else if (PF == FailList) {
//			Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
//		} else if (PF == OKList) {
//			adapter = new InformationThirdAdapter(this, list);
//			listView.setAdapter(adapter);
//		} else if (PF == NoData) {
//			Toast.makeText(this, "目前没有数据!", Toast.LENGTH_SHORT).show();
//		}
//
//	}
//
//	// 列表请求
//	private class ListRequest extends AsyncTask<Object, Void, String> {
//		private String urlList;
//
//		@Override
//		protected void onPreExecute() {
//			progressDialog = new ProgressDialog(InformationThirdActivity.this);
//			Constant.showProgressDialog(progressDialog);
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(Object... params) {
//			String result = "";
//			urlList = (String) params[0];
//			Log.e("medicineMsgUrl", urlList + "");
//			result = HttpManager.getStringContent(urlList);
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (result.toString().trim().equals("ERROR")) {
//				PF = FailServer;
//				initResult();
//			} else {
//				try {
//					JSONObject jo = new JSONObject(result);
//					String resultCode = jo.optString("resultCode");
//					if (resultCode.equals("1")) {
//						String entity = jo.optString("data");
//						JSONObject jo2 = new JSONObject(entity);
//						JSONArray ja = jo2.getJSONArray("providers");
//						if (ja.length() == 0) {
//							PF = NoData;
//							initResult();
//						} else {
//							for (int i = 0; i < ja.length(); i++) {
//								JSONObject jo3 = ja.getJSONObject(i);
//								String id = jo3.optString("id");
//								int notReadCount = jo3.optInt("notReadCount");
//								String minIcon = jo3.optString("minIcon");
//								String facilitatorName = jo3
//										.optString("facilitatorName");
//								String cooperateIdentify = jo3
//										.optString("cooperateIdentify");
//								boolean isBuy=jo3.optBoolean("isBuy");
//								InformationThird info = new InformationThird(
//										id, notReadCount,
//										HttpManager.m_imageUrl + minIcon,
//										facilitatorName, cooperateIdentify,isBuy);
//								list.add(info);
//							}
//							PF = OKList;
//							initResult();
//						}
//
//					} else {
//						PF = FailList;
//						initResult();
//					}
//
//				} catch (JSONException e) {
//					PF = FailList;
//					initResult();
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

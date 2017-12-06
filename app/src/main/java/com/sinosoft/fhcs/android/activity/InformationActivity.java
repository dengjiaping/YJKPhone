package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:健康资讯页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.InformationAdapter;
import com.sinosoft.fhcs.android.entity.InformationGroup;
import com.sinosoft.fhcs.android.service.InformationService;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("HandlerLeak")
public class InformationActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private ExpandableListView listView;
	public static List<InformationGroup> list_Ground = new ArrayList<InformationGroup>();
	public static Handler handler = null;
	private InformationAdapter adapter;
	private ProgressDialog progressDialog;
	private String userId;
	private static final int OKDelete = 1004;// 删除成功
	private static final int FailDelete = 1005;// 删除失败
	private static final int ChaoShi = 1003;// 超时
	private int PF = 1000;
	private int groupPosition;
	private int childPosition;
	private String memberId = "memberId";
	private String cooperateIdentify="";
	private TextView  tvLookUpService;
	private TextView  tvLookUpTip1,tvLookUpTip2;
	private LinearLayout lookupLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_information);
		ExitApplicaton.getInstance().addActivity(this);
		memberId = getIntent().getExtras().getString("memberId");
		cooperateIdentify=getIntent().getExtras().getString("cooperateIdentify");
		// 从首选项获取角色
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();
	}

	@Override
	protected void onStart() {
		getData();//请求数据
		super.onStart();
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("健康资讯页"); //统计页面
		MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("健康资讯页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_information));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		tvLookUpService=(TextView) findViewById(R.id.information_btn_lookup_service);
		tvLookUpService.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tvLookUpService.setOnClickListener(this);
		lookupLayout=(LinearLayout) findViewById(R.id.information_layout_lookup);
		tvLookUpTip1=(TextView) findViewById(R.id.information_tv_lookup_tip1);
		tvLookUpTip2=(TextView) findViewById(R.id.information_tv_lookup_tip2);
		tvLookUpTip1.setText("您还没有订购健康服务,请点此  “");
		tvLookUpTip2.setText("”");
		if(cooperateIdentify.equals("lk")){
			lookupLayout.setVisibility(View.GONE);
		}else{
			lookupLayout.setVisibility(View.VISIBLE);
		}
		listView = (ExpandableListView) findViewById(R.id.information_listview);
		listView.setGroupIndicator(null);// 将控件默认的左边箭头去掉，
//		getData();//请求数据
		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
										int groupPosition, int childPosition, long id) {
				list_Ground.get(groupPosition).getList_child()
						.get(childPosition).setRead(true);
				adapter.notifyDataSetChanged();
				Intent intent = new Intent(InformationActivity.this,
						InformationDetailActivity.class);
				intent.putExtra("entity", list_Ground.get(groupPosition)
						.getList_child().get(childPosition));
				startActivity(intent);
				return false;
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent,
										   View childView, int flatPos, long id) {
				if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
					long packedPos = ((ExpandableListView) parent)
							.getExpandableListPosition(flatPos);
					groupPosition = ExpandableListView
							.getPackedPositionGroup(packedPos);
					childPosition = ExpandableListView
							.getPackedPositionChild(packedPos);
					if (HttpManager
							.isNetworkAvailable(InformationActivity.this)) {
						DeleteDialog();
					} else {
						Toast.makeText(InformationActivity.this, "检查网络是否连接",
								Toast.LENGTH_SHORT).show();
					}
					return true;
				}

				return false;
			}

		});
	}

	// 删除
	private void DeleteDialog() {
		AlertDialog.Builder builder = new Builder(InformationActivity.this,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage("您确定删除这条资讯吗？");
		builder.setTitle("温馨提示");
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DeleteRequest re = new DeleteRequest();
						re.execute(HttpManager.urlInfoDelete(userId,
								list_Ground.get(groupPosition)
										.getList_child().get(childPosition)
										.getId()));
					}
				});
		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.create().show();
	}

	private void getData() {
		list_Ground.clear();
		if (HttpManager.isNetworkAvailable(InformationActivity.this)) {
			// 启动该Service
			Intent intent = new Intent(this, InformationService.class);
//			intent.setAction("com.sinosoft.fhcs.android.action.INFO_SERVICE");
			if (!memberId.equals("memberId")) {
				intent.putExtra("taskUrl", HttpManager.urlInfoTypeByMemberId(userId, cooperateIdentify,memberId));
			}else{
				intent.putExtra("taskUrl", HttpManager.urlInfoType(userId, cooperateIdentify));
			}

			intent.putExtra("request", Constant.Json_Request_Alltask);
			intent.putExtra("userId", userId);
			intent.putExtra("memberId", memberId);
			intent.putExtra("cooperateIdentify", cooperateIdentify);
			startService(intent);
			// ////////////////
			progressDialog = new ProgressDialog(InformationActivity.this);
			Constant.showProgressDialog(progressDialog);
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					switch (msg.what) {
						case Constant.Json_Return_Success:
							Constant.exitProgressDialog(progressDialog);
							if(list_Ground.get(0).isThirdPartyOrder()){
								if(list_Ground == null || list_Ground.size() == 0){
									return;
								}
								tvLookUpTip1.setText("请点此  “");
							}else{
								tvLookUpTip1.setText("您还没有订购健康服务,请点此  “");
							}
							adapter = new InformationAdapter(
									InformationActivity.this, list_Ground);
							listView.setAdapter(adapter);
							break;
						case Constant.Json_Return_Fail:
							Constant.exitProgressDialog(progressDialog);
							Toast.makeText(InformationActivity.this, "服务器没有响应！",
									Toast.LENGTH_LONG).show();
							tvLookUpTip1.setText("您还没有订购健康服务,请点此  “");
							break;
					}
				}
			};
		} else {
			Toast.makeText(InformationActivity.this, "检查网络是否连接",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.information_btn_lookup_service:
				//订购服务列表
				Intent intent=new Intent(InformationActivity.this,OrderServiceListActivity.class);
				intent.putExtra("cooperateIdentify", cooperateIdentify);
				startActivity(intent);
				break;
			default:
				break;
		}

	}
	// 获取结果
	private void initGetInfoResult() {
		if (PF == ChaoShi) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKDelete) {
			// 通知adapter 更新
			list_Ground.get(groupPosition).getList_child()
					.remove(childPosition);
			adapter.notifyDataSetChanged();
			Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();

		} else if (PF == FailDelete) {
			Toast.makeText(this, "删除失败！", Toast.LENGTH_SHORT).show();
		}

	}

	// 网络请求
	private class DeleteRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(InformationActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("deleteInfoUrl", url + "");
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

}

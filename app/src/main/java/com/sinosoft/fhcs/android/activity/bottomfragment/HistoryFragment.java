package com.sinosoft.fhcs.android.activity.bottomfragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.AddFamilyMemberActivity;
import com.sinosoft.fhcs.android.activity.EditFamilyMemberActivity;
import com.sinosoft.fhcs.android.activity.HealthRecordActivity;
import com.sinosoft.fhcs.android.adapter.FamilylistGridviewAdapter;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class HistoryFragment extends Fragment implements OnItemClickListener,
		OnItemLongClickListener {
	private TextView tvTitle;
	private GridView gridview;
	private List<Object> fmList = new ArrayList<Object>();// 包括添加按钮的数据
	private List<Object> getList = new ArrayList<Object>();// 不包括添加按钮的数据
	private FamilylistGridviewAdapter adapter;
	private boolean flag = false;// 是否处于删除状态
	private int position = 0;
	// 家庭列表网络请求
	private ProgressDialog progressDialog;// 进度条
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private static final int FailNoData = 1004;// 没有数据
	private static final int OKDelete = 1005;// 删除成功
	private static final int FailDelete = 1006;// 删除失败
	private int PF = 1000;
	private String userId = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_family, container, false);
		tvTitle = (TextView) view.findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(
				R.string.title_healthfamilylist));
		gridview = (GridView) view.findViewById(R.id.familylist_gridview);
		gridview.setOnItemClickListener(this);
		gridview.setOnItemLongClickListener(this);
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				flag = false;
				adapter = new FamilylistGridviewAdapter(getActivity(), fmList,
						flag);
				gridview.setAdapter(adapter);
				return false;
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		fmList.clear();
		getList.clear();
		position = 0;
		flag = false;
		FamilyMember member = new FamilyMember(7, "添加", 0, "添加", 0, 0, 0, "",
				0, R.drawable.add, "", false,4000101);
		fmList.add(member);
		adapter = new FamilylistGridviewAdapter(getActivity(), fmList, flag);
		gridview.setAdapter(adapter);

		// 从首选项获取userId
		SharedPreferences prefs = getActivity().getSharedPreferences(
				"UserInfo", Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		if (HttpManager.isNetworkAvailable(getActivity())) {
			GetFamilyListRequest re = new GetFamilyListRequest();
			re.execute(HttpManager.urlFamilyList(userId));
		} else {
			// Constant.showDialog(getActivity(), "您的网络没连接好，请检查后重试！");
			Toast.makeText(getActivity(), "您的网络没连接好，请检查后重试！",
					Toast.LENGTH_SHORT).show();
		}
		super.onResume();
		MobclickAgent.onPageStart("家庭成员健康档案页"); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("家庭成员健康档案页");
	}

	// 点击
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		position = arg2;
		if (!(((FamilyMember) fmList.get(arg2)).getFamilyRoleName()
				.equals("添加"))) {
			if (flag) {
				// 删除
				if (((FamilyMember) fmList.get(arg2)).isMasterFamilyMember()) {
					Toast.makeText(getActivity(), "不能删除与登陆账号绑定的家庭成员！", Toast.LENGTH_SHORT).show();
				} else {
					new AlertDialog.Builder(getActivity(),
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle("温馨提示")
							.setMessage("您确定要删除该角色吗？")
							.setPositiveButton("删除",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											long fmId = ((FamilyMember) fmList
													.get(position)).getId();
											if (HttpManager
													.isNetworkAvailable(getActivity())) {
												DeleteMemberRequest re = new DeleteMemberRequest();
												re.execute(HttpManager
														.urlDeleteFamilyMember(
																userId, fmId));
											} else {
												Toast.makeText(getActivity(),
														"您的网络没连接好，请检查后重试！",
														Toast.LENGTH_SHORT)
														.show();
											}
											dialog.dismiss();
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();

				}
			} else {
				// 选项卡
//				Intent intent = new Intent(getActivity(),
//						RecentlyRecordActivity.class);
//				intent.putExtra("member", (FamilyMember) getList.get(position));
//				startActivity(intent);

				Intent intent = new Intent(getActivity(),
						HealthRecordActivity.class);
				intent.putExtra("roleName", ((FamilyMember) getList.get(position)).getFamilyRoleName().trim());
				intent.putExtra("deviceId", ((FamilyMember) getList.get(position)).getDevice_name());
				startActivity(intent);
			}
		} else {
			if (flag) {
				flag = false;
				adapter = new FamilylistGridviewAdapter(getActivity(), fmList,
						flag);
				gridview.setAdapter(adapter);
			} else {
				// 添加
				Intent intent = new Intent(getActivity(),
						AddFamilyMemberActivity.class);
				intent.putExtra("list", (Serializable) getList);
				startActivity(intent);
			}
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
								   long arg3) {
		if (!(((FamilyMember) fmList.get(arg2)).getFamilyRoleName()
				.equals("添加"))) {
//			flag = true;
//			adapter = new FamilylistGridviewAdapter(getActivity(), fmList, flag);
//			gridview.setAdapter(adapter);
			Intent ii=new Intent(getActivity(),EditFamilyMemberActivity.class);
			ii.putExtra("member", (FamilyMember)fmList.get(arg2));
			startActivity(ii);
		}

		return true;
	}

	// 网络请求
	private class GetFamilyListRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(getActivity());
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
				initResult("");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						JSONObject jo2 = jo.getJSONObject("data");
						JSONArray ja = jo2.getJSONArray("FamilyMemberList");
						if (ja.length() == 0) {
							PF = FailNoData;
							initResult("");
						} else {
							getList.clear();
							getList = HttpManager.getFamilyList(ja);
							PF = OK;
							initResult("");
						}
					} else {
						PF = Fail;
						initResult("");
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

	// 删除
	private class DeleteMemberRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(getActivity());
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("DeleteMemberUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResult("");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					String message = jo.optString("message");
					if (resultCode.equals("1")) {
						PF = OKDelete;
						initResult("");
					} else {
						PF = FailDelete;
						initResult(message);
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = FailDelete;
					initResult("");
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

	// 请求结果
	private void initResult(String message) {
		if(getActivity() ==null){
			System.out.println("HistoryFragment隐藏了");
		}else{
			if (PF == FailServer) {
				// Constant.showDialog(getActivity(), "服务器响应超时!");
				Toast.makeText(getActivity(), "服务器响应超时!", Toast.LENGTH_SHORT)
						.show();
			} else if (PF == Fail) {
				Toast.makeText(getActivity(), "获取数据失败!", Toast.LENGTH_SHORT).show();
			} else if (PF == OK) {
				// Toast.makeText(this, "获取成功！", Toast.LENGTH_SHORT).show();
				initData();
			} else if (PF == FailNoData) {
				Toast.makeText(getActivity(), "目前还没有家庭成员，请添加!", Toast.LENGTH_SHORT)
						.show();
			} else if (PF == OKDelete) {
				Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
				fmList.remove(position);
				adapter = new FamilylistGridviewAdapter(getActivity(), fmList, flag);
				gridview.setAdapter(adapter);
			} else if (PF == FailDelete) {
				if (message.equals("")) {
					Toast.makeText(getActivity(), "删除失败！", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
							.show();
				}

			}
		}


	}

	private void initData() {
		fmList.clear();
		FamilyMember member = new FamilyMember(7, "添加", 0, "添加", 0, 0, 0, "",
				0, R.drawable.add, "", false,4000101);
		fmList.addAll(getList);
		fmList.add(member);
		adapter = new FamilylistGridviewAdapter(getActivity(), fmList, flag);
		gridview.setAdapter(adapter);
	}

}

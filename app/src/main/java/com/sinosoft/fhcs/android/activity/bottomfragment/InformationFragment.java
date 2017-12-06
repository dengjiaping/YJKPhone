package com.sinosoft.fhcs.android.activity.bottomfragment;

/**
 * @CopyRight: SinoSoft.
 * @Description: 服务中心页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.InformationActivity;
import com.sinosoft.fhcs.android.activity.RegistrationAndPharmacyActivity;
import com.sinosoft.fhcs.android.activity.RemindHomeActivity;
import com.sinosoft.fhcs.android.adapter.InformationThirdAdapter;
import com.sinosoft.fhcs.android.entity.InformationThird;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class InformationFragment extends Fragment implements
		OnItemClickListener {
	private View mainView;
	private TextView tvTitle;
	private ListView listView;
	private InformationThirdAdapter adapter;
	private List<InformationThird> list = new ArrayList<InformationThird>();
	// 请求数据
	private ProgressDialog progressDialog;// 进度条
	private static final int OKList = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int FailList = 1003;// 失败
	private static final int NoData = 1004;// 没有数据
	private int PF = 1000;
	private String userId = "";
	private String memberId = "memberId";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.activity_sysmessage, container,
				false);
		init();
		return mainView;
	}

	private void init() {
		tvTitle = (TextView) mainView.findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_message2));
		listView = (ListView) mainView.findViewById(R.id.sysmessage_listview);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onResume() {
		list.clear();
		// 从首选项获取机顶盒编号
		SharedPreferences prefs = getActivity().getSharedPreferences(
				"UserInfo", Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		if (HttpManager.isNetworkAvailable(getActivity())) {
			ListRequest re = new ListRequest();
			re.execute(HttpManager.urlGetInformationThirdAndMedicineNotice(userId));
		} else {
			// Constant.showDialog(getActivity(), "您的网络没连接好，请检查后重试！");
			Toast.makeText(getActivity(), "您的网络没连接好，请检查后重试！",
					Toast.LENGTH_SHORT).show();
		}
		super.onResume();
		MobclickAgent.onPageStart("服务中心页"); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("服务中心页");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		Intent intent = null;
		if (position == 6) {
			// 服药提醒消息
			intent = new Intent(getActivity(),
					RemindHomeActivity.class);
			startActivity(intent);
		}else if(position == 0){//预约挂号
			intent = new Intent(getActivity(),
					RegistrationAndPharmacyActivity.class);
			intent.setFlags(Constant.information_registration);
			intent.putExtra("titleName",list.get(position).getFacilitatorName());
			startActivity(intent);

		}else if(position == 1){//用药评估
			intent = new Intent(getActivity(),
					RegistrationAndPharmacyActivity.class);
			intent.setFlags(Constant.information_pharmacy);
			intent.putExtra("titleName",list.get(position).getFacilitatorName());
			startActivity(intent);
		}else if(position == 2){//疾病评估
			intent = new Intent(getActivity(),
					RegistrationAndPharmacyActivity.class);
			intent.setFlags(Constant.information_illness);
			intent.putExtra("titleName",list.get(position).getFacilitatorName());
			startActivity(intent);
		}else{
//			if (!list.get(position).isBuy()&&!(list.get(position).getCooperateIdentify().equals("lk"))) {
//				// 没有订购服务
//				Toast.makeText(
//						getActivity(),
//						"您还没有订购过“" + list.get(position).getFacilitatorName()
//								+ "”的服务，如需更多资讯内容，请使用翼家康机顶盒客户端订购该服务。", Toast.LENGTH_LONG)
//						.show();
//			}
			intent = new Intent(getActivity(),
					InformationActivity.class);
			intent.putExtra("memberId", memberId);
			intent.putExtra("cooperateIdentify", list.get(position)
					.getCooperateIdentify());
			startActivity(intent);
		}

	}
	// 请求结果
	private void initResult() {
		if(getActivity() ==null){
			System.out.println("InformationFragment隐藏了");
		}else{
			if (PF == FailServer) {
//					Constant.showDialog(this, "服务器响应超时!");
				Toast.makeText(getActivity(), "服务器响应超时!", Toast.LENGTH_SHORT).show();
			} else if (PF == FailList) {
				Toast.makeText(getActivity(), "获取数据失败!", Toast.LENGTH_SHORT).show();
			} else if (PF == OKList) {
				adapter = new InformationThirdAdapter(getActivity(), list);
				listView.setAdapter(adapter);
			} else if (PF == NoData) {
				Toast.makeText(getActivity(), "目前没有数据!", Toast.LENGTH_SHORT).show();
			}
		}


	}
	// 列表请求
	private class ListRequest extends AsyncTask<Object, Void, String> {
		private String urlList;

		@Override
		protected void onPreExecute() {
			try {
				Constant.exitProgressDialog(progressDialog);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			progressDialog = new ProgressDialog(getActivity());
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
					list.clear();
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						String entity = jo.optString("data");
						JSONObject jo2 = new JSONObject(entity);
						JSONArray ja = jo2.getJSONArray("providers");
						if (ja.length() == 0) {
							PF = NoData;
							initResult();
						} else {
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo3 = ja.getJSONObject(i);
								String id = jo3.optString("id");
								int notReadCount = jo3.optInt("notReadCount");
								String minIcon = jo3.optString("minIcon");
								String facilitatorName = jo3
										.optString("facilitatorName");
								String cooperateIdentify = jo3
										.optString("cooperateIdentify");
								boolean isBuy=jo3.optBoolean("isBuy");
								InformationThird info = new InformationThird(
										id, notReadCount,
										HttpManager.m_imageUrl + minIcon,
										facilitatorName, cooperateIdentify,isBuy);
								list.add(info);
							}
						}
						int takeMedicineCount=jo2.optInt("takeMedicineCount");
						InformationThird info = new InformationThird(
								"", takeMedicineCount,
								"",
								"服药提醒", "",true);
						InformationThird info2 = new InformationThird(
								"", 0,
								"",
								"预约挂号", "",true);
						InformationThird info3 = new InformationThird(
								"", 0,
								"",
								"用药评估", "",true);
						InformationThird info4 = new InformationThird(
								"", 0,
								"",
								"疾病评估", "",true);
						list.add(info);
						list.add(0,info2);
						list.add(1,info3);
						list.add(2,info4);
						PF = OKList;
						initResult();
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

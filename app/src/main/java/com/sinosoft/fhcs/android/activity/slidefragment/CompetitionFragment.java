package com.sinosoft.fhcs.android.activity.slidefragment;
/**
 * @CopyRight: SinoSoft.
 * @Description:竞赛列表页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月20日.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.AddCompetitionActivity;
import com.sinosoft.fhcs.android.activity.CompetitionDetailActivity;
import com.sinosoft.fhcs.android.activity.MainActivity;
import com.sinosoft.fhcs.android.activity.MyCompetitionListActivity;
import com.sinosoft.fhcs.android.adapter.CompetitionListAdapter;
import com.sinosoft.fhcs.android.customview.XListView;
import com.sinosoft.fhcs.android.customview.XListView.IXListViewListener;
import com.sinosoft.fhcs.android.entity.CompetitionListInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class CompetitionFragment extends Fragment implements OnClickListener,IXListViewListener,OnItemClickListener{
	private final String TAG = "CompetitionListActivity";
	private Activity mActivity;
	private View mainView;
	private String userId="";
	private TextView tvTitle;
	private Button btnLeftMebu;// 左菜单
	private Button btnMyCompetition;// 我的竞赛
	private Button btnCreate;//创建
	private Button btnRules;//规则
	private XListView listView;
	private CompetitionListAdapter adapter;
	private List<CompetitionListInfo>getList=new ArrayList<CompetitionListInfo>();
	private Handler mHandler;
	private static final int OKList = 1001;// 成功
	private static final int FailList = 1002;// 失败
	private static final int ChaoShi = 1003;// 超时
	private int PF = 1000;
	private ProgressDialog progressDialog;// 进度条
	private int page = 1;// 页数
	private boolean FlagIsMore = false;
	private boolean isFirst=false;
	public CompetitionFragment(MainActivity mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//inflater the layout
		mainView = inflater.inflate(R.layout.fragment_competitionlist, null);
		// 从首选项获取userId
		SharedPreferences prefs = mActivity.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();// 初始化控件
		return mainView;
	}
	private void init() {
		tvTitle = (TextView) mainView.findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_competitionlist));
		btnLeftMebu = (Button) mainView.findViewById(R.id.titlebar_btn_memuleft);
		btnLeftMebu.setVisibility(View.VISIBLE);
		btnLeftMebu.setOnClickListener(this);
		btnMyCompetition =(Button) mainView.findViewById(R.id.titlebar_btn_myCompetition);
		btnMyCompetition.setVisibility(View.VISIBLE);
		btnMyCompetition.setOnClickListener(this);
		btnCreate=(Button) mainView.findViewById(R.id.comptlist_btn_add);
		btnCreate.setOnClickListener(this);
		btnRules=(Button) mainView.findViewById(R.id.comptlist_btn_rules);
		btnRules.setOnClickListener(this);
		listView=(XListView) mainView.findViewById(R.id.comptlist_listview);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
		adapter = new CompetitionListAdapter(mActivity, getList);
		listView.setAdapter(adapter);
		mHandler = new Handler();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		if(getList.size()!=0){
			position -= listView.getHeaderViewsCount();
			Intent intent=new Intent(mActivity,CompetitionDetailActivity.class);
			intent.putExtra("raceId", getList.get(position).getId());
			intent.putExtra("session", getList.get(position).getSession());
			startActivity(intent);
		}

	}
	@Override
	public void onStart() {
		super.onStart();
		page = 1;
		FlagIsMore = false;
		isFirst=false;
		if (!HttpManager.isNetworkAvailable(mActivity)) {
//			Constant.showDialog(mActivity, "您的网络没连接好，请检查后重试！");
			Toast.makeText(mActivity, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		getList.clear();
		ListRequest request = new ListRequest();
		request.execute(HttpManager.urlGetCompetitionList(userId, 1));
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("竞赛列表页"); //统计页面
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("竞赛列表页");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_memuleft:
				// 左菜单
//			MainActivity.sm.showMenu();
				SlidingMenu sm =((SlidingActivity) mActivity).getSlidingMenu();
				sm.showMenu();
				break;
			case R.id.titlebar_btn_myCompetition:
				//我的竞赛
				Intent myIntent=new Intent(mActivity,MyCompetitionListActivity.class);
				startActivity(myIntent);
				break;
			case R.id.comptlist_btn_add:
				//创建
				Intent intent=new Intent(mActivity,AddCompetitionActivity.class);
				intent.putExtra("friendId", "add");
				startActivity(intent);
				break;
			case R.id.comptlist_btn_rules:
				//规则
//			String url="http://wap.baidu.com";
//			Uri u = Uri.parse(url);
//			Intent it = new Intent(Intent.ACTION_VIEW, u);
//			mActivity.startActivity(it);
				break;
			default:
				break;
		}

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
		page = 1;
		FlagIsMore = false;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getList.clear();
				ListRequest request = new ListRequest();
				request.execute(HttpManager.urlGetCompetitionList(userId,
						1));
				onLoad();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		FlagIsMore = true;
		page++;
		Log.i(TAG, "加载更多");
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				ListRequest request = new ListRequest();
				request.execute(HttpManager.urlGetCompetitionList(userId,
						page));
				onLoad();
			}
		}, 2000);


	}
	// 获取结果
	private void initGetInfoResult() {
		if (PF == ChaoShi) {
//				Constant.showDialog(mActivity, "服务器响应超时!");
			Toast.makeText(mActivity, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailList) {
			if (FlagIsMore == false) {
				Toast.makeText(mActivity, "暂无竞赛数据！", Toast.LENGTH_SHORT).show();
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(mActivity, "没有更多的数据了！", Toast.LENGTH_SHORT).show();
			}

		} else if (PF == OKList) {
			if (FlagIsMore == false) {
				adapter = new CompetitionListAdapter(mActivity, getList);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		}

	}

	// 解析数据
	private List<CompetitionListInfo> jiexi(String result) {
		try {
			JSONObject jo = new JSONObject(result);
			String resultCode = jo.optString("resultCode");
			if (resultCode.equals("1")) {
				String entity = jo.optString("data");
				JSONObject jo2 = new JSONObject(entity);
				if (!jo2.isNull("raceList")) {
					JSONArray ja = jo2.getJSONArray("raceList");
					if (ja.length() != 0) {
						for (int i = 0; i < ja.length(); i++) {
							JSONObject jo3 = ja.getJSONObject(i);
							String id = jo3.optString("id");
							String uid = jo3.optString("uid");
							String model = jo3.optString("model");
							String type = jo3.optString("type");
							String number = jo3.optString("number");
							String nowNumber=jo3.optString("nowNumber");
							String countdown = jo3.optString("countdown");
							CompetitionListInfo info=new CompetitionListInfo(id, uid, countdown, number, nowNumber, model, type);
							getList.add(info);
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
			Log.e(TAG, "解析失败！");
			PF = FailList;
			initGetInfoResult();
		}
		return getList;

	}

	// 网络请求
	private class ListRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			if(isFirst==false){
				progressDialog = new ProgressDialog(mActivity);
				Constant.showProgressDialog(progressDialog);
			}
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("CompetitionListUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initGetInfoResult();
			} else {
				getList = jiexi(result);
			}
			if(isFirst==false){
				Constant.exitProgressDialog(progressDialog);
			}
			isFirst=true;
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}


}

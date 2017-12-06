package com.sinosoft.fhcs.android.activity.slidefragment;

/**
 * @CopyRight: SinoSoft.
 * @Description: 好友比拼页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月27日.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.MainActivity;
import com.sinosoft.fhcs.android.activity.PkPersonalActivity;
import com.sinosoft.fhcs.android.adapter.FriendsPKAdapter;
import com.sinosoft.fhcs.android.entity.FriendsPKInfo;
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
public class FriendsPkFragment extends Fragment implements OnClickListener,
		OnItemClickListener {
	private View mainView;
	private Activity mActivity;
	private TextView tvTitle;
	private Button btnMenuLeft;// 左菜单
	private Button btnFriends, btnNear;// 好友，附近的人
	private ListView listView;
	private FriendsPKAdapter adapter;
	private List<FriendsPKInfo> getList = new ArrayList<FriendsPKInfo>();
	private boolean flag = false;// 是否为附近，默认为否
	/**
	 * 网络请求
	 */
	private static final int OK = 1001;// 成功
	private static final int Fail = 1002;// 失败
	private static final int FailNoData = 1003;// 没有数据
	private static final int ChaoShi = 1004;// 超时
	private int PF = 1000;
	private ProgressDialog progressDialog;// 进度条
	private double lat = 40.0001;// 纬度
	private double lon = 116.0001;// 经度
	private String userId = "";
	/**
	 * 定位
	 */
	private BDLocationListener myListener = new MyLocationListener();
	private LocationClient mLocationClient;

	public FriendsPkFragment(MainActivity mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mLocationClient = new LocationClient(mActivity); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// inflater the layout
		mainView = inflater.inflate(R.layout.fragment_friendspk, null);
		// 从首选项获取userId
		SharedPreferences prefs = mActivity.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();
		return mainView;
	}

	private void init() {
		tvTitle = (TextView) mainView.findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_friendspk));
		btnMenuLeft = (Button) mainView
				.findViewById(R.id.titlebar_btn_memuleft);
		btnMenuLeft.setVisibility(View.VISIBLE);
		btnMenuLeft.setOnClickListener(this);
		btnFriends = (Button) mainView.findViewById(R.id.friendspk_btn_friend);
		btnFriends.setOnClickListener(this);
		btnNear = (Button) mainView.findViewById(R.id.friendspk_btn_near);
		btnNear.setOnClickListener(this);
		listView = (ListView) mainView.findViewById(R.id.friendspk_listview);
		listView.setOnItemClickListener(this);
		adapter = new FriendsPKAdapter(mActivity, getList, flag);
		listView.setAdapter(adapter);
		initRequest();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("好友比拼页"); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("好友比拼页");
	}

	// 请求
	private void initRequest() {
		if (!HttpManager.isNetworkAvailable(mActivity)) {
//			Constant.showDialog(mActivity, "您的网络没连接好，请检查后重试！");
			Toast.makeText(mActivity, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (flag) {
			// 附近
			GetListRequest request = new GetListRequest();
			request.execute(HttpManager.urlGetNearlist(userId, lat, lon));
		} else {
			// 好友
			GetListRequest request = new GetListRequest();
			request.execute(HttpManager.urlGetFriendsList(userId));
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		if(getList.size()!=0){
			Intent intent = new Intent(mActivity, PkPersonalActivity.class);
			intent.putExtra("flag", flag);
			intent.putExtra("id", getList.get(position).getId());
			intent.putExtra("distance", getList.get(position).getDistance());
			intent.putExtra("near", getList.get(position).getNear());
			startActivity(intent);
		}
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
			case R.id.friendspk_btn_friend:
				// 好友
				flag = false;
				getList.clear();
				adapter.notifyDataSetChanged();
				if(mLocationClient.isStarted()){
					mLocationClient.stop();
				}
				btnFriends.setBackgroundResource(R.drawable.pk_friends2);
				btnNear.setBackgroundResource(R.drawable.pk_near1);
				initRequest();
				break;
			case R.id.friendspk_btn_near:
				// 附近
				flag = true;
				getList.clear();
				adapter.notifyDataSetChanged();
				btnFriends.setBackgroundResource(R.drawable.pk_friends1);
				btnNear.setBackgroundResource(R.drawable.pk_near2);
				adapter.notifyDataSetChanged();
				Toast.makeText(mActivity, "正在定位...", Toast.LENGTH_SHORT).show();
				initGps();//定位
				break;
			default:
				break;
		}

	}
	//定位
	private void initGps() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02s
		option.setScanSpan(7 * 1000);// 设置发起定位请求的间隔时间为7000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}
	//定位
	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			lat=location.getLatitude();
			lon=location.getLongitude();
			System.out.println(lat+"\n"+lon);
			initRequest();//请求
			mLocationClient.stop();
		}
	}

	// 获取结果
	private void initGetInfoResult() {
		if (PF == ChaoShi) {
//			Constant.showDialog(mActivity, "服务器响应超时!");
			Toast.makeText(mActivity, "服务器响应超时!", Toast.LENGTH_SHORT).show();
			adapter.notifyDataSetChanged();
		} else if (PF == FailNoData) {
			if (flag) {
				Toast.makeText(mActivity, "您的附近没有玩家哦！", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(mActivity, "您还没有好友快去添加好友吧！", Toast.LENGTH_SHORT)
						.show();
			}
			adapter.notifyDataSetChanged();
		} else if (PF == Fail) {
			Toast.makeText(mActivity, "获取数据失败！", Toast.LENGTH_SHORT).show();
			adapter.notifyDataSetChanged();
		} else if (PF == OK) {
			adapter = new FriendsPKAdapter(mActivity, getList, flag);
			listView.setAdapter(adapter);
		}

	}

	// 获取数据
	private class GetListRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(mActivity);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("friendspkListUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = ChaoShi;
				initGetInfoResult();
			} else {
				getList.clear();
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						String entity = jo.optString("data");
						JSONObject jo2 = new JSONObject(entity);
						if (!jo2.isNull("data")) {
							JSONArray ja = jo2.getJSONArray("data");
							if (ja.length() != 0) {
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jo3 = ja.getJSONObject(i);
									String userId = jo3.optString("userId");
									double near = 0.0;
									if (flag) {
										near = jo3.optDouble("distance");// 附近
									} else {
										near = 0.0;// 好友
									}
									String name = jo3.optString("name");
									String distance = jo3
											.optString("activityMeters");
									String gender = jo3.optString("gender");
									String avatarPath = jo3
											.optString("avatarPath");
									FriendsPKInfo info = new FriendsPKInfo(
											userId, HttpManager.m_imageUrl
											+ avatarPath, name,
											distance, near, gender);
									getList.add(info);
								}
								PF = OK;
								initGetInfoResult();
							} else {
								PF = FailNoData;
								initGetInfoResult();
							}
						} else {
							PF = FailNoData;
							initGetInfoResult();
						}
					} else {
						PF = Fail;
						initGetInfoResult();
					}
				} catch (JSONException e) {
					Log.e("FriendsPkFragment", "解析失败！");
					PF = Fail;
					initGetInfoResult();
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

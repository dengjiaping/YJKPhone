package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:我的配件——运动页
 * @Author: pikai.
 * @Create: 2015年2月12日.
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.customview.MyMenuPopupWindowTwo;
import com.sinosoft.fhcs.android.customview.RoundProgressBar;
import com.sinosoft.fhcs.android.entity.FamilyMemberGoalInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

public class FamilyMemberGoalActivity extends BaseActivity {
	private ViewPager myViewPager;// 滑动
	List<View> views;// viewpager 配置的view集合
	LayoutInflater inflater;
	MyPagerAdapter pagerAdatper;// viewpager 适配器
	Button button;
	TextView tab_sports, tab_sleep, tab_one, tab_two;// 标题栏随滑动变化的tab
	RoundProgressBar roundprogressBar;// 自定义圆环进度条
	private ProgressDialog progressDialog;// 加载框
	TextView tv_sleep_totle_time, tv_sleep_deep_time, tv_sleep_light_time,
			tv_activity_Calories, tv_activity_Steps, tv_activity_Min,
			tv_sleep_time_long;
	LinearLayout bottom_one, bottom_two;// 底部布局 ，切换效果采用隐藏实现
	ImageView left_window, right_window;// 左右菜单栏
	MyMenuPopupWindowTwo menuWindow; // 弹出框
	ListView devices_listview; // 设备列表 水平显示listview
	FamilyMemberGoalInfo familyMemberBean, familyMemberBean2;// 家庭成员目标实体类
	ArrayList<FamilyMemberGoalInfo> lists;
	private DisplayImageOptions options;
	String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_familymember);
		ExitApplicaton.getInstance().addActivity(this);
		// 从首选项获取信息
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		initView();// 初始化控件
		initRequest();// 网络请求
		initData();// 初始化数据
		initPageView();
		setOnClickListener();// 初始化viewpager
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("我的配件——运动页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("我的配件——运动页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		left_window.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menuWindow = new MyMenuPopupWindowTwo(
						FamilyMemberGoalActivity.this);
				menuWindow.showAsDropDown(left_window, 0, 20);
			}
		});
	}

	private void initPageView() {
		// TODO Auto-generated method stub
		pagerAdatper = new MyPagerAdapter(views);
		myViewPager.setAdapter(pagerAdatper);
		myViewPager.setCurrentItem(0);
		myViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				switch (arg0) {
					case 0:
						/**
						 *
						 * 改变标题栏 tab 的字体颜色
						 */
						tab_one.setVisibility(View.VISIBLE);
						tab_sports.setTextColor(Color.parseColor("#ffffff"));
						tab_two.setVisibility(View.INVISIBLE);
						tab_sleep.setTextColor(Color.parseColor("#CCCCCC"));

						// /////////////////////////////////////////
						// 底部隐藏
						bottom_one.setVisibility(View.VISIBLE);
						bottom_two.setVisibility(View.GONE);
						break;

					case 1:
						tab_one.setVisibility(View.INVISIBLE);
						tab_sports.setTextColor(Color.parseColor("#cccccc"));
						tab_two.setVisibility(View.VISIBLE);
						tab_sleep.setTextColor(Color.parseColor("#ffffff"));

						bottom_one.setVisibility(View.GONE);
						bottom_two.setVisibility(View.VISIBLE);
						break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		initOptions();
		lists = new ArrayList<FamilyMemberGoalInfo>();
		inflater = getLayoutInflater().from(this);
		views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.view_sports, null));
		views.add(inflater.inflate(R.layout.view_sleep, null));

	}

	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.avatar)
				.showImageForEmptyUri(R.drawable.avatar)
				.showImageOnFail(R.drawable.avatar).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

	}

	private void initRequest() {
		if (!HttpManager.isNetworkAvailable(this)) {
//			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		FamilyMemberGoalRequest re = new FamilyMemberGoalRequest();
		re.execute(HttpManager.m_serverAddress
				+ "rest/app/getFamilyMemberHandRingInfo?userId=" + userId
				+ "&serialNo=");
	}

	private void initView() {
		// TODO Auto-generated method stub
		myViewPager = (ViewPager) findViewById(R.id.myviewpager);
		tab_one = (TextView) findViewById(R.id.tab_one);
		tab_two = (TextView) findViewById(R.id.tab_two);
		tab_sports = (TextView) findViewById(R.id.tab_sports);
		tab_sleep = (TextView) findViewById(R.id.tab_sleep);
		tv_sleep_totle_time = (TextView) findViewById(R.id.textView4);
		tv_sleep_deep_time = (TextView) findViewById(R.id.textView5);
		tv_sleep_light_time = (TextView) findViewById(R.id.textView6);
		tv_activity_Calories = (TextView) findViewById(R.id.textView3);
		tv_activity_Min = (TextView) findViewById(R.id.textView1);
		tv_activity_Steps = (TextView) findViewById(R.id.textView2);
		bottom_one = (LinearLayout) findViewById(R.id.bottom_one);
		bottom_two = (LinearLayout) findViewById(R.id.bottom_two);
		tv_sleep_time_long = (TextView) findViewById(R.id.tv_sleep_time);
		left_window = (ImageView) findViewById(R.id.left_window);
		devices_listview = (ListView) findViewById(R.id.devices_listview);
	}

	class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			if (arg1 == 0) {
				((ViewPager) arg0).addView(mListViews.get(arg1), 0);
				roundprogressBar = (RoundProgressBar) arg0
						.findViewById(R.id.progressbar1);
				roundprogressBar.setProgress(90, "我们", "我们的目标");
			} else if (arg1 == 1) {
				((ViewPager) arg0).addView(mListViews.get(arg1), 0);
				roundprogressBar = (RoundProgressBar) arg0
						.findViewById(R.id.progressbar1);
				roundprogressBar.setProgress(60, "tamen", "他们的目标");
			}

			return mListViews.get(arg1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));

		}
	}

	// 网络请求
	private class FamilyMemberGoalRequest extends
			AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(FamilyMemberGoalActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getRecentlyUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i("tag", result);
			if (result.toString().trim().equals("ERROR")) {

			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						JSONObject jo2 = jo.getJSONObject("data");
						JSONArray jo3 = jo2.getJSONArray("data");
						JSONObject jo4 = jo3.getJSONObject(0);
						familyMemberBean = new FamilyMemberGoalInfo();
						familyMemberBean.setTotalSleepMinutes(jo4
								.getString("totalSleepMinutes"));
						familyMemberBean.setLightSleepMinutes(jo4
								.getString("lightSleepMinutes"));
						familyMemberBean.setDeepSleepMinutes(jo4
								.getString("deepSleepMinutes"));
						familyMemberBean.setActivityCalories(jo4
								.getString("activityCalories"));
						familyMemberBean.setActivityMinutes(jo4
								.getString("activityMinutes"));
						familyMemberBean.setActivitySteps(jo4
								.getString("activitySteps"));
						familyMemberBean.setGoalDeepMinutes(jo4
								.getString("goalDeepMinutes"));
						familyMemberBean.setGoalSteps(jo4
								.getString("goalSteps"));
						// 把取得的数据放入下面的textView 中
						putData();
					} else if (resultCode.equals("0")) {
						Toast.makeText(FamilyMemberGoalActivity.this,
								"抱歉，没有数据", 3000).show();
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

	// 网络请求
	private class FamilyMemberDeviceRequest extends
			AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(FamilyMemberGoalActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getRecentlyUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i("tag", result);
			if (result.toString().trim().equals("ERROR")) {

			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						JSONObject jo2 = jo.getJSONObject("data");
						JSONArray jo3 = jo2.getJSONArray("data");
						JSONObject jo4 = jo3.getJSONObject(0);
						familyMemberBean.setTotalSleepMinutes(jo4
								.getString("totalSleepMinutes"));
						familyMemberBean.setLightSleepMinutes(jo4
								.getString("lightSleepMinutes"));
						familyMemberBean.setDeepSleepMinutes(jo4
								.getString("deepSleepMinutes"));
						familyMemberBean.setActivityCalories(jo4
								.getString("activityCalories"));
						familyMemberBean.setActivityMinutes(jo4
								.getString("activityMinutes"));
						familyMemberBean.setActivitySteps(jo4
								.getString("activitySteps"));
						familyMemberBean.setGoalDeepMinutes(jo4
								.getString("goalDeepMinutes"));
						familyMemberBean.setGoalSteps(jo4
								.getString("goalSteps"));
						// 把取得的数据放入下面的textView 中
						putData();
					} else if (resultCode.equals("0")) {
						Toast.makeText(FamilyMemberGoalActivity.this,
								"抱歉，没有数据", 3000).show();
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

	public void putData() {
		// TODO Auto-generated method stub
		tv_sleep_deep_time.setText(familyMemberBean.getDeepSleepMinutes());
		tv_sleep_light_time.setText(familyMemberBean.getLightSleepMinutes());
		tv_sleep_totle_time.setText(familyMemberBean.getTotalSleepMinutes());
		tv_activity_Calories.setText(familyMemberBean.getActivityCalories());
		tv_activity_Steps.setText(familyMemberBean.getActivitySteps());
		tv_activity_Min.setText(familyMemberBean.getActivityMinutes());
		tv_sleep_time_long.setText(Html.fromHtml("您的睡眠时长已击败全国"
				+ "<font color=\"#595959\"><big>50%</big></font>" + "的用户"));
	}

	class DeviceListsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return lists.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (arg1 == null) {
				arg1 = inflater.inflate(R.layout.device_lists_layout, null);
				holder = new ViewHolder();
				holder.tv1 = (TextView) arg1.findViewById(R.id.device_list_tv1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.device_list_tv2);
				holder.imageview = (ImageView) arg1
						.findViewById(R.id.device_list_img);
				arg1.setTag(holder);

			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			familyMemberBean2 = lists.get(arg0);
			holder.tv1.setText(familyMemberBean2.getRoleName() + "的");
			holder.tv2.setText(familyMemberBean2.getDeviceName());
			ImageLoader.getInstance().displayImage(
					familyMemberBean2.getImgUrl(), holder.imageview, options);
			return arg1;
		}

	}

	class ViewHolder {
		TextView tv1, tv2;
		ImageView imageview;
	}
}

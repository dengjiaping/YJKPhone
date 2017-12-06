package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:运动历史页
 * @Author:pikai.
 * @Create: 2015年2月12日.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.entity.TrackBean;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pikai
 *
 * 运动轨迹历史页面
 *
 */
public class TrackHistoryActivity extends BaseActivity {
	ListView trackList;
	List<Map<String, Object>> points;
	List<TrackBean> lists;//轨迹实体
	LayoutInflater inflater;
	private ProgressDialog progressDialog;// 进度条
	TrackHistoryRequest request;//请求
	ImageView back;
	String lastMonth = "";
	String userId;

	SparseBooleanArray map = new SparseBooleanArray();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_trackhistory);
		ExitApplicaton.getInstance().addActivity(this);
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		initView();
		initData();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TrackHistoryActivity.this.finish();
			}
		});
		trackList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				int trailid = Integer.parseInt(lists.get(position)
						.getMotionTrailId());
				Intent intent = new Intent(TrackHistoryActivity.this,
						RunningTrackActivity.class);
				intent.putExtra("trailid", trailid);
				startActivity(intent);
			}
		});
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("运动历史页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("运动历史页"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		lists = new ArrayList<TrackBean>();
		request = new TrackHistoryRequest();
		request.execute(HttpManager.m_serverAddress
				+ "rest/app/getMotionTrailHistory?userId=" + userId);

	}

	private void initView() {
		// TODO Auto-generated method stub
		inflater = LayoutInflater.from(TrackHistoryActivity.this);
		trackList = (ListView) findViewById(R.id.tracklist);
		back = (ImageView) findViewById(R.id.trackhistory_back);
	}

	class TrackListsAdapter extends BaseAdapter {
		DecimalFormat df = new DecimalFormat("###,##0.00");
		DecimalFormat df2 = new DecimalFormat("###,##0");
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
			TrackBean bean;
			if (arg1 == null) {
				arg1 = inflater.inflate(R.layout.activity_tracklist_item, null);
				holder = new ViewHolder();
				holder.tv1 = (TextView) arg1.findViewById(R.id.track_time_one);
				holder.tv2 = (TextView) arg1.findViewById(R.id.track_time_two);
				holder.imageview = (ImageView) arg1
						.findViewById(R.id.track_type);
				holder.tv3 = (TextView) arg1
						.findViewById(R.id.track_time_three);
				holder.tv4 = (TextView) arg1.findViewById(R.id.track_cl);
				holder.tv5 = (TextView) arg1.findViewById(R.id.track_km);
				holder.tv7 = (TextView) arg1
						.findViewById(R.id.track_time_color);
				arg1.setTag(holder);

			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			bean = lists.get(arg0);
			if (bean.getMotionType().equals("1")) {
				holder.imageview.setImageResource(R.drawable.track_run);
				holder.tv7.setBackgroundColor(Color.parseColor("#54cb00"));
			} else if (bean.getMotionType().equals("2")) {
				holder.imageview.setImageResource(R.drawable.track_walk);
				holder.tv7.setBackgroundColor(Color.parseColor("#00aeef"));
			} else if (bean.getMotionType().equals("3")) {
				holder.imageview.setImageResource(R.drawable.track_ride);
				holder.tv7.setBackgroundColor(Color.parseColor("#faa701"));
			}
			if(map.get(arg0)){
				holder.tv1.setVisibility(View.VISIBLE);
			}else{
				holder.tv1.setVisibility(View.INVISIBLE);
			}
//			if (subString(2, bean.getStart_time()).equals(lastMonth)) {
//				holder.tv1.setVisibility(View.INVISIBLE);
//			}
			if(arg0 - 1>=0) {
				if (bean.getMotionType().equals(lists.get(arg0 - 1).getMotionType())) {
					holder.imageview.setVisibility(View.GONE);
				} else {
					holder.imageview.setVisibility(View.VISIBLE);
				}
			}else if(arg0 == 0){
				holder.imageview.setVisibility(View.VISIBLE);
			}
			String  s = subString(2, bean.getStart_time()).substring(0,4);
			String  ss = subString(2, bean.getStart_time()).substring(4);
			holder.tv1.setText(s+"\n"+ss);
			holder.tv2.setText(subString(1, bean.getStart_time()) + "时");
			holder.tv3.setText(df.format(Double.parseDouble(bean
					.getMotionMinutes())) + "'");
			if(bean.getBurnCalories()!=null&&!(bean.getBurnCalories().equals(""))){
				holder.tv4.setText(df2.format(Double.parseDouble(bean
						.getBurnCalories()))+"");
			}else{
				holder.tv4.setText("");
			}


			holder.tv5.setText(bean.getKm());
			lastMonth = subString(2, bean.getStart_time());
			return arg1;
		}

	}

	class ViewHolder {
		TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
		ImageView imageview;
	}

	// 请求数据
	private class TrackHistoryRequest extends AsyncTask<Object, Void, String> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(TrackHistoryActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = HttpManager.getStringContent((String) params[0]);
			// Log.e("loginResult", result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {

			} else {
				try {
					JSONObject jo = new JSONObject(result);
					Log.i("ifijajfasdkljfl", result);
					String resultCode = jo.optString("resultCode");
					TrackBean bean;
					if (resultCode.equals("1")) {
						JSONObject jo2 = jo.getJSONObject("data");
						JSONArray jo3 = jo2.getJSONArray("data");
						for (int i = 0; i < jo3.length(); i++) {
							bean = new TrackBean();
							JSONObject jo4 = jo3.getJSONObject(i);
							bean.setStart_time(jo4.get("startTime") + "");
							bean.setEnd_time(jo4.get("endTime") + "");
							bean.setMotionTrailId(jo4.get("motionTrailId") + "");
							bean.setBurnCalories(jo4.get("burnCalories") + "");
							bean.setMotionMinutes(jo4.get("motionMinutes") + "");
							bean.setMotionType(jo4.get("motionType") + "");
							bean.setKm(jo4.get("km") + "");
							lists.add(bean);

							if (subString(2, bean.getStart_time()).equals(lastMonth)) {
								map.put(i,false);
							}else{
								map.put(i,true);
							}
							lastMonth = subString(2, bean.getStart_time());
						}
						trackList.setAdapter(new TrackListsAdapter());

					} else {
						String errormsg = "";
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
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

	public String subString(int type, String str) {
		String str2 = "";
		if (type == 1) {
			str2 = str.substring(5, 13);
		} else if (type == 2) {
			String[] ss = str.split("-");
			String sss = ss[1].substring(0, 2);
			str2 = str.substring(0, 4) + sss + "月";
		}
		return str2;

	}
}

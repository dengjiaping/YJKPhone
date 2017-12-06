package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:运动轨迹页
 * @Author:pikai.
 * @Create: 2015年2月12日.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.entity.TrackBean;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.sinosoft.fhcs.android.util.UmengShareService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RunningTrackActivity extends BaseActivity {
	public LocationClient mLocationClient = null;
	MapView mMapView;
	LatLng startPoint, endPoint, midPoint;
	OverlayOptions option_sta, option2, option_end;
	BitmapDescriptor bitmap_sta, bitmap_end;
	MapStatusUpdate statusUpdate;
	BaiduMap mBaiduMap;
	Intent intent, intent2;
	ImageView back;
	int trailid;
	private ProgressDialog progressDialog;// 进度条
	RunningTrackRequest request;
	TextView cl, time, km;
	ArrayList<LatLng> points;
	DecimalFormat df = new DecimalFormat("###,##0.00");
	DecimalFormat df2 = new DecimalFormat("###,##0");
	String userId;

	private Button btnShare;// 分享

	public static Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_runningtrack);
		activity = this;
		intent2 = getIntent();
		trailid = intent2.getIntExtra("trailid", 0);
		ExitApplicaton.getInstance().addActivity(this);
		// Log.i("yes", RunningRightNowActivity.points.size()+"");
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		points = new ArrayList<LatLng>();
		cl = (TextView) findViewById(R.id.runningtrack_cl);
		time = (TextView) findViewById(R.id.runningtrack_time);
		km = (TextView) findViewById(R.id.runningtrack_km);
		back = (ImageView) findViewById(R.id.back1);
		btnShare = (Button) findViewById(R.id.btn_runningtrack_share);
		btnShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				quickShare();
			}
		});
		// initBaiduMap();
		request = new RunningTrackRequest();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RunningTrackActivity.this.finish();
			}
		});
		request.execute(HttpManager.m_serverAddress
				+ "rest/app/getMotionTrail?userId=" + userId
				+ "&motionTrailId=" + trailid);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("运动轨迹页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("运动轨迹页"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);
	}

	private void initBaiduMap() {
		// 构建Marker图标
		bitmap_sta = BitmapDescriptorFactory
				.fromResource(R.drawable.start_point);
		bitmap_end = BitmapDescriptorFactory.fromResource(R.drawable.end_point);

		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		// LocationClientOption option = new LocationClientOption();
		// option.setOpenGps(true);
		// option.setAddrType("all");// 返回的定位结果包含地址信息
		// option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02s
		// option.setScanSpan(10000);// 设置发起定位请求的间隔时间为5000ms
		// option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		// option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		// mLocationClient.setLocOption(option);
		// mLocationClient.start();
		// if (mLocationClient != null && mLocationClient.isStarted())
		// mLocationClient.requestLocation();
		// else {
		// Log.d("LocSDK3", "locClient is null or not started");
		// }
		if (points.size() > 1) {
			startPoint = points.get(0);
			endPoint = points.get(points.size() - 1);
			if (points.size() % 2 == 0) {
				midPoint = points.get(points.size() / 2 - 1);
			} else {
				midPoint = points.get((points.size() + 1) / 2);
			}
			statusUpdate = MapStatusUpdateFactory.newLatLngZoom(midPoint, 16);
			option2 = new PolylineOptions().points(points)
					.color(Color.parseColor("#00aeef")).width(8);
			option_sta = new MarkerOptions().position(startPoint).icon(
					bitmap_sta);
			option_end = new MarkerOptions().position(endPoint)
					.icon(bitmap_end);
			mBaiduMap.addOverlay(option2);
			mBaiduMap.addOverlay(option_sta);
			mBaiduMap.addOverlay(option_end);
			mBaiduMap.animateMapStatus(statusUpdate, 100);
		} else {
			Toast.makeText(RunningTrackActivity.this, "您的运动轨迹太短", Toast.LENGTH_LONG).show();
		}
	}

	// 登录请求
	private class RunningTrackRequest extends AsyncTask<Object, Void, String> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(RunningTrackActivity.this);
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
					Log.i("tttttttt", result);
					String resultCode = jo.optString("resultCode");
					TrackBean bean;
					if (resultCode.equals("1")) {
						JSONObject jo2 = jo.getJSONObject("data");
						JSONObject jo3 = jo2.getJSONObject("data");
						if(jo3.getString("burnCalories")!=null&&!(jo3
								.getString("burnCalories").equals(""))){
							cl.setText(df2.format(Double.parseDouble(jo3
									.getString("burnCalories"))) + "Kcal");
						}else{
							cl.setText("0Kcal");
						}

						time.setText(df.format(Double.parseDouble(jo3
								.getString("motionMinutes"))) + "'");
						km.setText(df.format(Double.parseDouble(jo3
								.getString("km"))) + "km");
						JSONArray jo4 = jo3.getJSONArray("points");
						for (int i = 0; i < jo4.length(); i++) {
							JSONObject jo5 = (JSONObject) jo4.get(i);
							LatLng latlng = new LatLng(Double.parseDouble(jo5
									.getString("latitude")),
									Double.parseDouble(jo5
											.getString("longitude")));
							points.add(latlng);
						}
						// trackList.setAdapter(new TrackListsAdapter());
						initBaiduMap();
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
//	/**
//	 * @功能描述 : 快速分享接口（呼出编辑页）
//	 */
//	private void quickShare() {
//		UmengShareService.share(this,
//				"我正在使用中国电信翼家康健康云服务测量身体状况，足不出户就能呵护身体健康！网址：" + Constant.HOST,
//				new UMImage(this, shotImage()));
//	}
//	/**
//	 * 截屏方法
//	 *
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	private Bitmap shotImage() {
//		View view = getWindow().getDecorView();
//		Rect rect = new Rect();
//		view.getWindowVisibleDisplayFrame(rect);
//		int statusBarHeight = rect.top;
//		Display display = this.getWindowManager().getDefaultDisplay();
//		view.layout(0, 0, display.getWidth(), display.getHeight());
//		view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
//		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
//				statusBarHeight, view.getWidth(), view.getHeight()
//						- statusBarHeight);
//		return bmp;
//	}
	/**
	 * @功能描述 : 快速分享接口（呼出编辑页）
	 */
	private void quickShare() {
		shotImage();
	}
	/**
	 * 截屏方法
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private void shotImage() {
		mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
			@Override
			public void onSnapshotReady(Bitmap bitmap) {
				try {
					View view = getWindow().getDecorView();
					Rect rect = new Rect();
					view.getWindowVisibleDisplayFrame(rect);
					int statusBarHeight = rect.top;
					Display display = RunningTrackActivity.this.getWindowManager().getDefaultDisplay();
					view.layout(0, 0, display.getWidth(), display.getHeight());
					view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
					Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
							statusBarHeight, view.getWidth(), view.getHeight()
									- statusBarHeight);
					int top = mMapView.getTop();
					Canvas canvas = new Canvas(bmp);
					canvas.drawBitmap(bitmap, 0, top, null);

					UmengShareService.share(RunningTrackActivity.this,
							"我正在使用中国电信翼家康健康云服务测量身体状况，足不出户就能呵护身体健康！网址：" ,Constant.HOST,
							new UMImage(RunningTrackActivity.this, bmp),umShareListener);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(RunningTrackActivity.this, "暂时无法分享！", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
	private static UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Log.d("plat","platform"+platform);
			if(platform.name().equals("WEIXIN_FAVORITE")){
				Toast.makeText(activity,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(activity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(activity,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
			if(t!=null){
				Log.d("throw","throw:"+t.getMessage());
			}
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(activity,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	};
}

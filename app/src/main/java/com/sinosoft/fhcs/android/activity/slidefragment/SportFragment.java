package com.sinosoft.fhcs.android.activity.slidefragment;

/**
 * @CopyRight: SinoSoft.
 * @Description:立刻运动主页
 * @Author: pikai.
 * @Create: 2015年2月12日.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.MainActivity;
import com.sinosoft.fhcs.android.activity.RunningRightNowActivity;
import com.sinosoft.fhcs.android.activity.TrackHistoryActivity;
import com.sinosoft.fhcs.android.entity.Weather;
import com.sinosoft.fhcs.android.entity.WeatherInfo;
import com.sinosoft.fhcs.android.util.Configration;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.sinosoft.fhcs.android.util.LogUtils;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@SuppressLint("ValidFragment")
public class SportFragment extends Fragment {
	private ImageButton start_button;// 开始跑步按钮
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	MapView mMapView;
	LatLng point;
	OverlayOptions option;
	BitmapDescriptor bitmap;
	BaiduMap mBaiduMap;
	Intent intent;
	boolean isFirst = true;
	ImageView leftMenu;
	LinearLayout run, walk, ride;
	TextView line_one, line_two, line_three, trackhistory;
	Activity mActivity;
	public static int motionType = 1;// 运动模式记录

	/**
	 * 获取天气
	 */
	private String strCity = "";
	private WeatherInfo weatherInfo = new WeatherInfo();
	private static final int OK = 1001;// 成功
	private static final int Fail = 1002;// 失败
	private static final int ChaoShi = 1003;// 超时
	private int PF = 1000;
	private ProgressDialog progressDialog;// 进度条

	private ImageView imgWeather;
	private TextView tvWeather, tvTemp, tvWD, tvWS;// 例如 晴 21 北风 3-4级

	private boolean isGPS;


	public SportFragment(){}

	public SportFragment(MainActivity mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// inflater the layout

		View view = inflater.inflate(R.layout.fragment_startrunning, null);
		initView(view);
		setOnClickListener();
		initBaiduMap();
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		start_button = (ImageButton) view
				.findViewById(R.id.startrunning_startbutton);
		leftMenu = (ImageView) view.findViewById(R.id.startrunning_left_memu);
		run = (LinearLayout) view.findViewById(R.id.startrunning_run);
		walk = (LinearLayout) view.findViewById(R.id.startrunning_walk);
		ride = (LinearLayout) view.findViewById(R.id.startrunning_ride);
		line_one = (TextView) view
				.findViewById(R.id.startrunning_bottomline_one);
		line_two = (TextView) view
				.findViewById(R.id.startrunning_bottomline_two);
		line_three = (TextView) view
				.findViewById(R.id.startrunning_bottomline_three);
		trackhistory = (TextView) view.findViewById(R.id.trackhistory_activity);
		imgWeather = (ImageView) view
				.findViewById(R.id.startrunning_weather_img);
		tvWeather = (TextView) view
				.findViewById(R.id.startrunning_weather_type);
		tvTemp = (TextView) view
				.findViewById(R.id.startrunning_weather_temperature);
		tvWD = (TextView) view.findViewById(R.id.startrunning_weather_WD);
		tvWS = (TextView) view.findViewById(R.id.startrunning_weather_WS);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		start_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isOpen(getActivity())){
					if(isGPS){//信号好
						intent = new Intent(getActivity(),
								RunningRightNowActivity.class);
						intent.putExtra("entity", weatherInfo);
						startActivity(intent);
					}else{
						createDialog(SportFragment.this.getActivity(),"","翼家康使用GPS记录户外运动轨迹，请打开GPS服务，并到户外开阔场地运动。","","");
					}
				}else{
					Toast.makeText(getActivity(),"请先打开GPS",Toast.LENGTH_LONG).show();
					open();
				}
			}
		});

		leftMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// MainActivity.sm.showMenu();
				SlidingMenu sm = ((SlidingActivity) mActivity).getSlidingMenu();
				sm.showMenu();
			}
		});

		run.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setBottomLineVisible();
				line_one.setVisibility(View.VISIBLE);
				motionType = 1;
			}
		});
		walk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setBottomLineVisible();
				line_two.setVisibility(View.VISIBLE);
				motionType = 2;
			}
		});
		ride.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setBottomLineVisible();
				line_three.setVisibility(View.VISIBLE);
				motionType = 3;
			}
		});
		trackhistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						TrackHistoryActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initBaiduMap() {
		// 构建Marker图标
		bitmap = BitmapDescriptorFactory.fromResource(R.drawable.gpspoint);
		mBaiduMap = mMapView.getMap();
		mLocationClient = new LocationClient(getActivity()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02s
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else {
			Log.d("LocSDK3", "locClient is null or not started");
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mLocationClient.stop();
		mMapView.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		MobclickAgent.onPageStart("立刻运动主页"); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		MobclickAgent.onPageEnd("立刻运动主页");
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}

			// 定义Maker坐标点
			point = new LatLng(location.getLatitude(), location.getLongitude());

			// 构建MarkerOption，用于在地图上添加Marker
			option = new MarkerOptions().position(point).icon(bitmap);
			MapStatusUpdate statusUpdate = MapStatusUpdateFactory
					.newLatLngZoom(point, 18);
			if (isFirst) {
				// 在地图上添加Marker，并显示
				mBaiduMap.addOverlay(option);
				mBaiduMap.animateMapStatus(statusUpdate, 100);
				isFirst = false;
				strCity = location.getCity();
				if (strCity != null) {
					if (!strCity.equals("") && strCity.length() != 0) {
						// 如果有市 就去掉 市
						if (strCity.substring(strCity.length() - 1).equals("市")) {
							strCity = strCity
									.substring(0, strCity.length() - 1);
						}
						System.out.println(strCity);// 输出d
						// 获取天气
						if (!HttpManager.isNetworkAvailable(mActivity)) {
							Toast.makeText(mActivity, "您的网络没连接好，无法获取天气信息！",
									Toast.LENGTH_SHORT).show();
							return;
						}
						GetWeatherRequest request = new GetWeatherRequest();
						request.execute(HttpManager.urlGetWeather(strCity));
						/*GetWeatherRequest2 request = new GetWeatherRequest2();
						request.execute(HttpManager.urlGetWeather2(strCity,"DJOYnieT8234jlsK","0"));*/
					}

				}
			}
			if (location.getRadius() < 10.0 && location.getRadius() > 0.0) {
				isGPS = true;
			} else if (location.getRadius() > 10.0
					&& location.getRadius() < 40.0) {
				isGPS = true;
			} else {
				isGPS = false;
			}
			// Log.i("tag", sb.toString());
		}

	}

	public void setBottomLineVisible() {
		line_one.setVisibility(View.GONE);
		line_two.setVisibility(View.GONE);
		line_three.setVisibility(View.GONE);
	};

	private void initGetInfoResult() {
		if (PF == ChaoShi) {
			Toast.makeText(mActivity, "您的网络连接超时，无法获取天气信息！", Toast.LENGTH_SHORT)
					.show();
		} else if (PF == OK) {
			// System.err.println(weatherInfo);
			tvWeather.setText(weatherInfo.getWeather());
			tvTemp.setText(weatherInfo.getTemp() + "°C");
			tvWD.setText(weatherInfo.getWD());
			String strWS = weatherInfo.getWS();
			if (!strWS.equals("")) {
				if (strWS.contains("(")) {
					int n_pos = strWS.indexOf("(");
					strWS = strWS.substring(0, n_pos);
				}
			}
			tvWS.setText(strWS);
			imgWeather.setImageResource(Constant.ImageWeather(weatherInfo
					.getWeather() + ""));
		} else if (PF == Fail) {
			Toast.makeText(mActivity, "获取天气信息失败！", Toast.LENGTH_SHORT).show();
		}
	}

	// 获取数据
	private class GetWeatherRequest extends AsyncTask<Object, Void, String> {
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
			Log.e("getWeatherUrl", url + "");
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
					String errNum = jo.optString("errNum");
					String errMsg = jo.optString("errMsg");
					System.err.println(errMsg);
					if (errNum.equals("0")) {
						JSONObject entity = jo.optJSONObject("retData");
						String city = entity.optString("city");
						String pinyin = entity.optString("pinyin");
						String citycode = entity.optString("citycode");
						String date = entity.optString("date");
						String time = entity.optString("time");
						String postCode = entity.optString("postCode");
						String longitude = entity.optString("longitude");
						String latitude = entity.optString("latitude");
						String altitude = entity.optString("altitude");
						String weather = entity.optString("weather");
						String temp = entity.optString("temp");
						String l_tmp = entity.optString("l_tmp");
						String h_tmp = entity.optString("h_tmp");
						String WD = entity.optString("WD");
						String WS = entity.optString("WS");
						String sunrise = entity.optString("sunrise");
						String sunset = entity.optString("sunset");
						weatherInfo = new WeatherInfo(city, pinyin, citycode,
								date, time, postCode, longitude, latitude,
								altitude, weather, temp, l_tmp, h_tmp, WD, WS,
								sunrise, sunset);
						PF = OK;
						initGetInfoResult();
					} else {
						PF = Fail;
						initGetInfoResult();
					}
				} catch (JSONException e) {
					Log.e("getWeather", "解析失败！");
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
	// 获取天气数据
	private class GetWeatherRequest2 extends AsyncTask<Object, Void, String> {
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
			Log.e("getWeatherUrl", url + "");
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
					InputStream in_withcode   =   new ByteArrayInputStream(result.getBytes("UTF-8"));
					Weather w=new Configration().readInfo(in_withcode);
					if(w!=null){
						/*weather_tx = w.getStatus1();
						celsius_tx = w.getTemperature2()+"~"+w.getTemperature1()+"℃";*/
						FRToast.showToast(getActivity().getApplicationContext(),"天气数据为===" + w.toString());
						LogUtils.i("天气==" + w.toString());
					}


					/*JSONObject jo = new JSONObject(result);
					String errNum = jo.optString("errNum");
					String errMsg = jo.optString("errMsg");
					System.err.println(errMsg);
					if (errNum.equals("0")) {
						JSONObject entity = jo.optJSONObject("retData");
						String city = entity.optString("city");
						String pinyin = entity.optString("pinyin");
						String citycode = entity.optString("citycode");
						String date = entity.optString("date");
						String time = entity.optString("time");
						String postCode = entity.optString("postCode");
						String longitude = entity.optString("longitude");
						String latitude = entity.optString("latitude");
						String altitude = entity.optString("altitude");
						String weather = entity.optString("weather");
						String temp = entity.optString("temp");
						String l_tmp = entity.optString("l_tmp");
						String h_tmp = entity.optString("h_tmp");
						String WD = entity.optString("WD");
						String WS = entity.optString("WS");
						String sunrise = entity.optString("sunrise");
						String sunset = entity.optString("sunset");
						weatherInfo = new WeatherInfo(city, pinyin, citycode,
								date, time, postCode, longitude, latitude,
								altitude, weather, temp, l_tmp, h_tmp, WD, WS,
								sunrise, sunset);
						PF = OK;
						initGetInfoResult();
					} else {
						PF = Fail;
						initGetInfoResult();
					}*/
				} catch (Exception e) {
					Log.e("getWeather", "解析失败！");
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
	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启
	 * @return true 表示开启
	 * */
	public static final boolean isOpen(final Context context){
		LocationManager locationManager =
				((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	public void open(){
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			getActivity().startActivity(intent);
		}catch(ActivityNotFoundException ex){
			intent.setAction(Settings.ACTION_SETTINGS);
			try {
				getActivity().startActivity(intent);
			}catch(Exception e){

			}
		}
	}
	public void createDialog(Context context,String title,String message,String ok,String cancle) {
		final AlertDialog d = new AlertDialog.Builder(context).create();
		d.show();
		d.getWindow().setContentView(R.layout.mydialog);
		TextView tvMessage = (TextView) d.getWindow().findViewById(R.id.tv_mydialog_message);
		TextView tvCancle = (TextView) d.getWindow().findViewById(R.id.tv_mydialog_cancle);
		TextView tvOk = (TextView) d.getWindow().findViewById(R.id.tv_mydialog_ok);
		tvMessage.setText(message);
		tvCancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d.dismiss();
			}
		});
		tvOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getActivity(),
						RunningRightNowActivity.class);
				intent.putExtra("entity", weatherInfo);
				startActivity(intent);
				d.dismiss();
			}
		});
	}
}

package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:正在运动页
 * @Author:pikai.
 * @Create: 2015年2月12日.
 */
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.activity.slidefragment.SportFragment;
import com.sinosoft.fhcs.android.entity.WeatherInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class RunningRightNowActivity extends BaseActivity {
	SeekBar seekBar;// 滑动解锁
	int currentProgress;// 滑动解锁 progress
	boolean isFirst = true, isFirstEnter = true;// 是不是第一次滑动
	LinearLayout button_menu;// 暂停button布局
	TextView countDown;// 倒计时牌
	ScaleAnimation scaleAnimation;// 倒计时动画
	Handler handler, timeHandler;// 倒计时，计时 handler
	RelativeLayout shadow_layout;// 倒计时阴影布局
	Button running_continue, stop_running;// 继续按钮
	ImageView back;
	public LocationClient mLocationClient = null;//地图client
	public BDLocationListener myListener = new MyLocationListener();//定位监听器
	MapView mMapView;// 初始化地图
	LatLng point, newPoint;//定位两个点，用来记录路线轨迹
	OverlayOptions option1, option2;
	BitmapDescriptor bitmap;
	MapStatusUpdate statusUpdate;
	BaiduMap mBaiduMap;
	public static ArrayList<LatLng> points;// 百度地图绘制 路线 需要传入 点的集合
	Double totalDistance = 0.0;// 总距离 公里
	Double calorie = 0.0;//卡路里
	TextView timeCount;// 计时器
	Thread timethread;// 计时 线程
	int secordCount;// 总时间 秒
	boolean threadSwitch = true;// 控制暂停开关
	boolean closeThread = true;// 控制销毁线程开关
	Double aSpeed = 1.6;// 平均速度
	Double mSpeed = 0.0;//瞬间速度
	TextView tv_speed, tv_speed2, tv_distance, tv_calorie;
	ImageView gps_signal;
	private ProgressDialog progressDialog;// 进度条
	Long start_time = 0L, end_time = 0L, current_time = 0L;
	List<Map<String, Object>> list;
	Map<String, Object> map, maps;
	LinearLayout bottom_layout;
	TextView bottom_layout_save, bottom_layout_delete, bottom_layout_cancel;
	Map<String, String> paramss;
	String userId;


	// 天气
	private ImageView imgWeather;
	private TextView tvWeather, tvTemp, tvWD, tvWS;// 例如 晴 21 北风 3-4级
	private WeatherInfo weatherInfo = new WeatherInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		SDKInitializer.initialize(getApplicationContext());//初始化
		setContentView(R.layout.activity_runningrightnow);
		ExitApplicaton.getInstance().addActivity(this);
		// 从首选项获取信息
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		weatherInfo = (WeatherInfo) this.getIntent().getExtras().get("entity");
		initView();
		initData();
		initBaiduMap();
		setOnClickListener();
		initTimeThread();
	}


	/**
	 * 初始化计时器，运动时间计时器
	 */
	private void initTimeThread() {
		// TODO Auto-generated method stub
		timethread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//closeThread 控制线程的开关
				while (closeThread) {
					if (threadSwitch) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						secordCount += 1;
						timeHandler.sendEmptyMessage(secordCount);

					} else {

					}
				}
			}
		});

	}

	/**
	 * @param num
	 * @return
	 *
	 * 把 秒表  转换成    时分秒  格式
	 */
	protected String translateClock(int num) {
		// TODO Auto-generated method stub
		String a, b, c;
		int secord = num % 60;
		int min = (num % 3600) / 60;
		int hour = num / 3600;
		if (secord < 10) {
			a = "0" + secord;
		} else {
			a = secord + "";
		}
		if (min < 10) {
			b = "0" + min;
		} else {
			b = min + "";
		}
		if (hour < 10) {
			c = "0" + hour;
		} else {
			c = hour + "";
		}
		return c + ":" + b + ":" + a;
	}

	private void initData() {
		points = new ArrayList<LatLng>();
		list = new ArrayList<Map<String, Object>>();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0) {
					shadow_layout.setVisibility(View.GONE);
					seekBar.setVisibility(View.VISIBLE);
					timethread.start();
				} else {
					countDown.setText(msg.what + "");
					countDown.startAnimation(scaleAnimation);
				}
			}

		};
		timeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				String clock = translateClock(msg.what);
				timeCount.setText(clock);
			}
		};
		// TODO Auto-generated method stub
		scaleAnimation = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(900);
		countDown.startAnimation(scaleAnimation);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (int i = 2; i >= 0; i--) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendEmptyMessage(i);
				}
			}
		}).start();

	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		//继续运动按钮
		running_continue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isFirst = true;
				seekBar.setProgress(10);
				button_menu.setVisibility(View.GONE);
				seekBar.setVisibility(View.VISIBLE);
				threadSwitch = true;
			}
		});
		//向右滑动停止运动
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				//用 进度控制活动力度
				if (currentProgress > 10 && currentProgress < 80) {
					seekBar.setProgress(10);
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
										  boolean fromUser) {
				// TODO Auto-generated method stub

				if (progress < 10) {
					seekBar.setProgress(10);
				} else if (progress > 70) {
					//当滑动超过70 progress 视作成功
					if (isFirst) {
						seekBar.setVisibility(View.GONE);
						button_menu.setVisibility(View.VISIBLE);
						threadSwitch = false;
						isFirst = false;
						end_time = System.currentTimeMillis();

					}
				}
				currentProgress = progress;

			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		//停止运动按钮
		stop_running.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button_menu.setVisibility(View.GONE);
				bottom_layout.setVisibility(View.VISIBLE);

			}
		});
		//取消按钮
		bottom_layout_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button_menu.setVisibility(View.VISIBLE);
				bottom_layout.setVisibility(View.GONE);
			}
		});
		//删除按钮
		bottom_layout_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RunningRightNowActivity.this.finish();
			}
		});
		//保存按钮
		bottom_layout_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//如果采集到两个点，就保存，否则 不计入
				if (points.size() > 1) {
					uploadTrack();
					closeThread = false;
				} else {
					bottom_layout.setVisibility(View.GONE);
					AlertDialog.Builder builder = new Builder(
							RunningRightNowActivity.this, AlertDialog.THEME_HOLO_LIGHT);
					builder.setMessage("您的运动轨迹太短!!!");

					builder.setPositiveButton("继续运动",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									dialog.dismiss();
									button_menu.setVisibility(View.VISIBLE);
								}
							});
					builder.setNegativeButton("退出",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									// TODO Auto-generated method fstub
									dialog.dismiss();
									RunningRightNowActivity.this.finish();
									closeThread = false;
								}

							});
					builder.create().show();
				}
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		seekBar = (SeekBar) findViewById(R.id.myseekbar);
		button_menu = (LinearLayout) findViewById(R.id.button_menu);
		countDown = (TextView) findViewById(R.id.countdown);
		shadow_layout = (RelativeLayout) findViewById(R.id.shadow_layout);
		running_continue = (Button) findViewById(R.id.running_continue);
		back = (ImageView) findViewById(R.id.startrunning_left_memu);
		timeCount = (TextView) findViewById(R.id.timecount);
		stop_running = (Button) findViewById(R.id.stop_running);
		tv_speed = (TextView) findViewById(R.id.tv_speed);
		tv_speed2 = (TextView) findViewById(R.id.tv_speed2);
		tv_calorie = (TextView) findViewById(R.id.tv_calorie);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		gps_signal = (ImageView) findViewById(R.id.gps_signal);
		bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
		bottom_layout_save = (TextView) findViewById(R.id.bottom_layout_save);
		bottom_layout_cancel = (TextView) findViewById(R.id.bottom_layout_cancel);
		bottom_layout_delete = (TextView) findViewById(R.id.bottom_layout_delete);
		// 天气
		imgWeather = (ImageView) findViewById(R.id.runningrightnow_weather_img);
		tvWeather = (TextView) findViewById(R.id.runningrightnow_weather_type);
		tvTemp = (TextView) findViewById(R.id.runningrightnow_weather_temperature);
		tvWD = (TextView) findViewById(R.id.runningrightnow_weather_WD);
		tvWS = (TextView) findViewById(R.id.runningrightnow_weather_WS);
		if(weatherInfo.getWeather()==null||weatherInfo.getWeather().equals("")){
			tvWeather.setText("");
		}else{
			tvWeather.setText(weatherInfo.getWeather()+"");
			imgWeather.setImageResource(Constant.ImageWeather(weatherInfo.getWeather()+""));
		}
		if(weatherInfo.getTemp()==null||weatherInfo.getTemp().equals("")){
			tvTemp.setText("");
		}else{
			tvTemp.setText(weatherInfo.getTemp() + "°C");
		}
		if(weatherInfo.getWD()==null||weatherInfo.getWD().equals("")){
			tvWD.setText("");
		}else{
			tvWD.setText(weatherInfo.getWD()+"");
		}
		String strWS = weatherInfo.getWS();
		if(strWS!=null&&!strWS.equals("")){
			if (strWS.contains("(")) {
				int n_pos = strWS.indexOf("(");
				strWS = strWS.substring(0, n_pos);
			}
			tvWS.setText(strWS+"");
		}else{
			tvWS.setText("");
		}
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location == null)
				return;
			Log.i("province", location.getProvince() + location.getCity()
					+ location.getDirection());
			Log.i("province",
					location.getLatitude() + "," + location.getLongitude());
			// if (location == null)
			// return;
			// StringBuffer sb = new StringBuffer(256);
			// sb.append("time : ");
			// sb.append(location.getTime());
			// sb.append("\nerror code : ");
			// sb.append(location.getLocType());
			// sb.append("\nlatitude : ");
			// sb.append(location.getLatitude());
			// sb.append("\nlontitude : ");
			// sb.append(location.getLongitude());
			// sb.append("\nradius : ");
			// sb.append(location.getRadius());
			// if (location.getLocType() == BDLocation.TypeGpsLocation) {
			// sb.append("\nspeed : ");
			// sb.append(location.getSpeed());
			// sb.append("\nsatellite : ");
			// sb.append(location.getSatelliteNumber());
			// } else if (location.getLocType() ==
			// BDLocation.TypeNetWorkLocation) {
			// sb.append("\naddr : ");
			// sb.append(location.getAddrStr());
			// }
			// // 定义Maker坐标点
			// newPoint = new LatLng(location.getLatitude(),
			// location.getLongitude());
			// if (point != null && DistanceUtil.getDistance(newPoint,
			// point) >
			// 10) {
			// points.add(newPoint);
			// draw_points.add(newPoint);
			// }
			// // 构建MarkerOption，用于在地图上添加Marker
			// // option1 = new
			// MarkerOptions().position(newPoint).icon(bitmap);
			// statusUpdate = MapStatusUpdateFactory
			// .newLatLngZoom(newPoint, 18);
			// if (points.size() > 1) {
			// option2 = new PolylineOptions().points(points);
			// mBaiduMap.addOverlay(option2);
			// totalDistance = totalDistance
			// + DistanceUtil.getDistance(newPoint, point);
			// //
			// ============================================================
			// points.remove(0);
			// speed = totalDistance / secordCount;
			// setRunnningData(speed);
			// }
			//
			// point = newPoint;
			// mBaiduMap.animateMapStatus(statusUpdate, 100);
			// option2 = null;


			//用threadSwitch来判断是否正在计时，也就是是否正在运动
			if (threadSwitch) {
				// 通过定位精确数值，粗略判断 GPS 信号
//				Toast.makeText(RunningRightNowActivity.this,location.getRadius()+"",Toast.LENGTH_LONG).show();
//				Log.d("TAG", location.getRadius() + "getRadius");
				if (location.getRadius() < 10.0 && location.getRadius() > 0.0) {
					gps_signal.setImageResource(R.drawable.gps_high);
				} else if (location.getRadius() > 10.0
						&& location.getRadius() < 40.0) {
					gps_signal.setImageResource(R.drawable.gps_mid);
				} else {
					gps_signal.setImageResource(R.drawable.gps_low);
				}
				//得到每次定位的点
				newPoint = new LatLng(location.getLatitude(),
						location.getLongitude());
				//在地图上画点
				statusUpdate = MapStatusUpdateFactory.newLatLngZoom(newPoint,
						18);
				//判断是否是第一次进入,也意味只有一个点
				if (isFirstEnter) {
					option1 = new MarkerOptions().position(newPoint).icon(
							bitmap);
					isFirstEnter = false;
					point = newPoint;
					mBaiduMap.addOverlay(option1);
					mBaiduMap.animateMapStatus(statusUpdate, 100);
					start_time = System.currentTimeMillis();
					map = new HashMap<String, Object>();
					map.put("timestamp", System.currentTimeMillis());
					map.put("latitude", newPoint.latitude);
					map.put("longitude", newPoint.longitude);
					maps = new HashMap<String, Object>();
					maps.put("timestamp", System.currentTimeMillis());
					maps.put("latitude", newPoint.latitude);
					maps.put("longitude", newPoint.longitude);
					list.add(map);
					list.add(maps);
				} else {
					//不是第一次进入， 取两点的距离，如果太短，不计入，可能没运动;如果太长,也不计入，防止有定位乱跳的情况
					if (DistanceUtil.getDistance(newPoint, point) < 80 &&
							DistanceUtil.getDistance(newPoint, point) > 5) {
						//把定位点加入集合
						points.add(newPoint);
						map = new HashMap<String, Object>();
						map.put("timestamp", System.currentTimeMillis());
						map.put("latitude", newPoint.latitude);
						map.put("longitude", newPoint.longitude);
						list.add(map);
						if (option2 != null) {
							mBaiduMap.clear();
							// 把之前画的点都清空
							option1 = new MarkerOptions().position(newPoint).icon(
									bitmap);
							mBaiduMap.addOverlay(option1);
						}
						//当点的集合超过两个，都重新在地图上绘制点，绘制最新的路线
						if (points.size() > 1) {
							option2 = new PolylineOptions().points(points)
									.color(Color.parseColor("#00aeef"));
							mBaiduMap.addOverlay(option2);
							// mBaiduMap.addOverlay(option1);


							//计算的瞬间速度
							mSpeed = ((double)(DistanceUtil.getDistance(newPoint, point)-2) / (double)1000)/( (double)5/(double)3600 ) ;
							totalDistance = (double)totalDistance
									+ (double)(DistanceUtil
									.getDistance(newPoint, point) - 2)
									/ (double)1000;
						}
						mBaiduMap.animateMapStatus(statusUpdate, 100);
						if (totalDistance < 0.0) {
							totalDistance = 0.0;
						}
						// 注意 类型的转换， 全部强转成 double
						double hourCount = (double) secordCount / (double)3600.00;
						aSpeed = totalDistance / hourCount;
//						calorie = (totalDistance/(double)100)*(double)6.4;
						calorie = totalDistance*64;
						//把数据都set 到 页面上
						setRunnningData(mSpeed);
						//最后把  最新的点赋值给之前的店，用于计算每次定位的距离
						point = newPoint;

					}
				}

				// Log.i("tag", sb.toString());
			}
		}

	}

	private void initBaiduMap() {
		// 构建Marker图标
		bitmap = BitmapDescriptorFactory.fromResource(R.drawable.start_point);

		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02s
		option.setScanSpan(5 * 1000);// 设置发起定位请求的间隔时间为5000ms
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

	public void setRunnningData(double d) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("###,##0.00");
		System.out.println(df.format(d));
		tv_speed.setText(df.format(d) + "");
		tv_speed2.setText(df.format(1 / (d)) + "");
		tv_distance.setText(df.format(totalDistance) + "");
		tv_calorie.setText(df.format(calorie)+"");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
		mMapView.onDestroy();
		closeThread = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		MobclickAgent.onPageStart("正在运动页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
		MobclickAgent.onPageEnd("正在运动页"); // 保证 onPageEnd 在onPause 之前调用
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}


	//上传轨迹的数据处理
	public void uploadTrack() {
		Date start_date = new Date(start_time);
		Date end_date = new Date(end_time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("###,##0.00");
		String hourCount = df.format((double) secordCount / 60.00);
		Log.i("sjdfj", (double) secordCount / 60.00 + "");
		paramss = new HashMap<String, String>();
		paramss.put("userId", userId);
		paramss.put("points", transformPointsToString(list));
		paramss.put("motionType", SportFragment.motionType+"");
		paramss.put("startTime", formatter.format(start_date));
		paramss.put("endTime", formatter.format(end_date));
		paramss.put("speed", df.format(aSpeed));
		paramss.put("ltp", df.format(1 / aSpeed));
		paramss.put("motionMinutes", hourCount);
		paramss.put("burnCalories", df.format(calorie));
		paramss.put("km", df.format(totalDistance));

		// ====================================================================

		UploadTrackRequest request = new UploadTrackRequest();
		request.execute();

	}

	// 上传轨迹请求
	private class UploadTrackRequest extends AsyncTask<Object, Void, String> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(RunningRightNowActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = HttpManager.getStringContentPost2(
					HttpManager.m_serverAddress + "rest/app/addMotionTrail",
					paramss);
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
					if (resultCode.equals("1")) {
						Toast.makeText(RunningRightNowActivity.this, "保存成功",
								Toast.LENGTH_SHORT).show();
					} else if (resultCode.equals("0")) {
						Toast.makeText(RunningRightNowActivity.this, "保存失败",
								Toast.LENGTH_SHORT).show();
					}
					RunningRightNowActivity.this.finish();

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

	/**
	 * @param list
	 * @return
	 * 定位点集合转换成String
	 */
	public String transformPointsToString(List<Map<String, Object>> list) {
		Gson gson = new Gson();
		gson.toJson(list);
		return gson.toJson(list);

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
			重写 back 事件
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Toast.makeText(RunningRightNowActivity.this, "请先结束本次运动", Toast.LENGTH_LONG)
					.show();
		}
		return true;
	}
}

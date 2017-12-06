//package com.sinosoft.fhcs.android.activity;
//
///**
// * @CopyRight: SinoSoft.
// * @Description: 健康档案（最近一次的测量结果）页 废弃页面
// * @Author: wangshuangshuang.
// * @Create: 2015年3月17日.
// */
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import com.sinosoft.fhcs.android.ExitApplicaton;
//import com.sinosoft.fhcs.android.customview.CircleImageView;
//import com.sinosoft.fhcs.android.customview.MyMenuPopupWindow;
//import com.sinosoft.fhcs.android.entity.FamilyMember;
//import com.sinosoft.fhcs.android.entity.MeasureMainInfo;
//import com.sinosoft.fhcs.android.R;
//import com.sinosoft.fhcs.android.util.Constant;
//import com.sinosoft.fhcs.android.util.HttpManager;
//import com.sinosoft.fhcs.android.util.UmengShareService;
//import com.umeng.analytics.MobclickAgent;
//import com.umeng.socialize.media.UMImage;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Rect;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class RecentlyRecordActivity extends Activity implements OnClickListener {
//	private TextView tvTitle;
//	private Button btnBack;
//	private Button btnMenu;
//	private FamilyMember member = new FamilyMember();
//	private Button btnShare;// 分享
//	private CircleImageView img;
//	private TextView tvNickName;
//	private TextView tvWeight, tvTemperature, tvSleep, tvXueYa, tvXueTang,
//			tvSport, tvTizhi;
//	private LinearLayout layoutWeight, layoutTemperature, layoutSleep,
//			layoutXueYa, layoutXueTang, layoutSport, layoutTizhi;
//	private List<MeasureMainInfo> getList = new ArrayList<MeasureMainInfo>();
//	// 自定义的弹出框类
//	 private MyMenuPopupWindow menuWindow;
//	/**
//	 * 网络请求
//	 */
//	private ProgressDialog progressDialog;// 进度条
//	private static final int OKGet = 1001;// 成功
//	private static final int FailServer = 1002;// 连接超时
//	private static final int FailGet = 1003;// 失败
//	private static final int FailNoData = 1004;// 没有数据
//	private int PF = 1000;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_recentlyrecord);
//		ExitApplicaton.getInstance().addActivity(this);
//		member = (FamilyMember) this.getIntent().getExtras().get("member");
//		init();
//	}
//
//	public void onResume() {
//		if (!HttpManager.isNetworkAvailable(this)) {
////			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
//			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		GetListRequest re = new GetListRequest();
//		re.execute(HttpManager.urlGetLastHealthByMember(member.getId()+""));
//		super.onResume();
//		MobclickAgent.onPageStart("最近测量页"); // 统计页面
//		MobclickAgent.onResume(this); // 统计时长
//	}
//
//	public void onPause() {
//		super.onPause();
//		MobclickAgent.onPageEnd("最近测量页"); // 保证 onPageEnd 在onPause 之前调用,因为
//											// onPause 中会保存信息
//		MobclickAgent.onPause(this);
//	}
//
//	private void init() {
//		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
//		tvTitle.setText(getResources().getString(R.string.title_recentlyrecord));
//		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
//		btnBack.setVisibility(View.VISIBLE);
//		btnBack.setOnClickListener(this);
//		btnMenu = (Button) findViewById(R.id.titlebar_btn_memu);
//		btnMenu.setVisibility(View.VISIBLE);
//		btnMenu.setOnClickListener(this);
//		btnShare = (Button) findViewById(R.id.recently_btn_share);
//		btnShare.setOnClickListener(this);
//		tvNickName = (TextView) findViewById(R.id.recently_measure_tv_nickname);
//		img = (CircleImageView) findViewById(R.id.recently_measure_img);
//		tvWeight = (TextView) findViewById(R.id.recently_measure_tv_weight);
//		tvTemperature = (TextView) findViewById(R.id.recently_measure_tv_temperature);
//		tvSleep = (TextView) findViewById(R.id.recently_measure_tv_sleep);
//		tvXueYa = (TextView) findViewById(R.id.recently_measure_tv_pressure);
//
//		tvXueTang = (TextView) findViewById(R.id.recently_measure_tv_glucose);
//		tvSport = (TextView) findViewById(R.id.recently_measure_tv_stepnum);
//		tvTizhi= (TextView) findViewById(R.id.recently_measure_tv_tizhi);
//		layoutWeight = (LinearLayout) findViewById(R.id.recently_measure_layout_weight);
//		layoutWeight.setOnClickListener(this);
//		layoutTemperature = (LinearLayout) findViewById(R.id.recently_measure_layout_temperature);
//		layoutTemperature.setOnClickListener(this);
//		layoutSleep = (LinearLayout) findViewById(R.id.recently_measure_layout_sleep);
//		layoutSleep.setOnClickListener(this);
//		layoutXueYa = (LinearLayout) findViewById(R.id.recently_measure_layout_pressure);
//		layoutXueYa.setOnClickListener(this);
//		layoutXueTang = (LinearLayout) findViewById(R.id.recently_measure_layout_glucose);
//		layoutXueTang.setOnClickListener(this);
//		layoutSport = (LinearLayout)findViewById(R.id.recently_measure_layout_stepnum);
//		layoutSport.setOnClickListener(this);
//		layoutTizhi= (LinearLayout)findViewById(R.id.recently_measure_layout_tizhi);
//		layoutTizhi.setOnClickListener(this);
//	}
//	private void initResult() {
//		if (PF == FailServer) {
////			Constant.showDialog(this, "服务器响应超时!");
//			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
//		} else if (PF == FailGet) {
//			Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
//		} else if (PF == FailNoData) {
//			Toast.makeText(this, "目前还没有数据!", Toast.LENGTH_SHORT)
//					.show();
//		} else if (PF == OKGet) {
//				initData(getList.get(0));
//
//		}
//	}
//
//	private  void initData(MeasureMainInfo measureInfo) {
//		tvNickName.setText(measureInfo.getNickName());
//		img.setImageResource(Constant.ImageIdbg(measureInfo.getNickName(),
//				measureInfo.getGender()));
//		tvWeight.setText(measureInfo.getWeight());
//		tvTemperature.setText(measureInfo.getTemperature());
//		DecimalFormat df = new DecimalFormat("#.0");
//		double hour = Double.valueOf(measureInfo.getSleep()) / 60;
//		if (measureInfo.getSleep().equals("0")
//				|| (df.format(hour) + "").equals(".0")) {
//			tvSleep.setText("0");
//		} else {
//			tvSleep.setText(df.format(hour) + "");
//		}
//		tvXueYa.setText((int) (measureInfo.getDiya()) + "/"
//				+ (int) (measureInfo.getGaoya()));
//		tvXueTang.setText(measureInfo.getBloodGlucose() + "");
//		tvSport.setText(measureInfo.getStepNum());
//		tvTizhi.setText(measureInfo.getTizhi());
//	}
//	@Override
//	public void onClick(View v) {
//		// 健康称 4000101脂肪仪 4000102 血压计 4000103 血糖仪
//		// 4000104 耳温枪 4000105 计步器 4000106
//		switch (v.getId()) {
//		case R.id.recently_measure_layout_weight:
//			// 体重
//			Intent intent1 = new Intent(this, HealthRecordActivity.class);
//			intent1.putExtra("deviceId", 4000101);
//			intent1.putExtra("roleName", member.getFamilyRoleName());
//			startActivity(intent1);
//			break;
//		case R.id.recently_measure_layout_temperature:
//			// 体温
//			Intent intent2 = new Intent(this, HealthRecordActivity.class);
//			intent2.putExtra("deviceId", 4000105);
//			intent2.putExtra("roleName", member.getFamilyRoleName());
//			startActivity(intent2);
//			break;
//		case R.id.recently_measure_layout_sleep:
//			// 睡眠
////			Intent intent3 = new Intent(this, HealthRecordActivity.class);
////			intent3.putExtra("deviceId", 4000101);
////			intent3.putExtra("roleName", member.getFamilyRoleName());
////			startActivity(intent3);
//			break;
//		case R.id.recently_measure_layout_pressure:
//			// 血压
//			Intent intent4 = new Intent(this, HealthRecordActivity.class);
//			intent4.putExtra("deviceId", 4000103);
//			intent4.putExtra("roleName", member.getFamilyRoleName());
//			startActivity(intent4);
//			break;
//		case R.id.recently_measure_layout_glucose:
//			// 血糖
//			Intent intent5 = new Intent(this, HealthRecordActivity.class);
//			intent5.putExtra("deviceId", 4000104);
//			intent5.putExtra("roleName", member.getFamilyRoleName());
//			startActivity(intent5);
//			break;
//		case R.id.recently_measure_layout_stepnum:
//			// 运动
//			Intent intent6 = new Intent(this, HealthRecordActivity.class);
//			intent6.putExtra("deviceId", 4000106);
//			intent6.putExtra("roleName", member.getFamilyRoleName());
//			startActivity(intent6);
//			break;
//		case R.id.recently_measure_layout_tizhi:
//			// 体脂
//			Intent intent7 = new Intent(this, HealthRecordActivity.class);
//			intent7.putExtra("deviceId", 4000102);
//			intent7.putExtra("roleName", member.getFamilyRoleName());
//			startActivity(intent7);
//			break;
//		case R.id.titlebar_btn_back:
//			finish();
//			break;
//		case R.id.titlebar_btn_memu:
//			menuWindow = new MyMenuPopupWindow(this, member);
//			menuWindow.showAtLocation(btnMenu, Gravity.TOP | Gravity.RIGHT, 0,
//					0);
//			break;
//		case R.id.recently_btn_share:
//			// 分享
//			if (PF != OKGet) {
//				Toast.makeText(this, "该设备没有测量数据,还不能进行分享哦!", Toast.LENGTH_SHORT)
//						.show();
//			} else {
//				quickShare();
//			}
//			break;
//		default:
//			break;
//		}
//	}
//
//	/**
//	 * @功能描述 : 快速分享接口（呼出编辑页）
//	 */
//	private void quickShare() {
//		UmengShareService.share(this,
//				"我正在使用中国电信翼家康健康云服务测量身体状况，足不出户就能呵护身体健康！网址：" + Constant.HOST,
//				new UMImage(this, shotImage()));
//	}
//
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
//	// 网络请求
//		private class GetListRequest extends AsyncTask<Object, Void, String> {
//			private String url;
//
//			@Override
//			protected void onPreExecute() {
//				progressDialog = new ProgressDialog(RecentlyRecordActivity.this);
//				Constant.showProgressDialog(progressDialog);
//				super.onPreExecute();
//			}
//
//			@Override
//			protected String doInBackground(Object... params) {
//				String result = "";
//				url = (String) params[0];
//				Log.e("getLastListUrl", url + "");
//				result = HttpManager.getStringContent(url);
//				return result;
//			}
//
//			@Override
//			protected void onPostExecute(String result) {
//				if (result.toString().trim().equals("ERROR")) {
//					PF = FailServer;
//					initResult();
//				} else {
//					try {
//						JSONObject jo = new JSONObject(result);
//						String resultCode = jo.optString("resultCode");
//						if (resultCode.equals("1")) {
//							JSONObject jo2 = jo.getJSONObject("data");
//							JSONArray ja = jo2.getJSONArray("data");
//							if (ja.length() == 0) {
//								PF = FailNoData;
//								initResult();
//							} else {
//								getList.clear();
//								for (int i = 0; i < ja.length(); i++) {
//									JSONObject jo4 = ja.getJSONObject(i);
//									String totalSleepMinutes = jo4
//											.optString("totalSleepMinutes");
//									double bloodGlucose = jo4
//											.optDouble("bloodGlucose");
//									String activitySteps = jo4
//											.optString("activitySteps");
//									String weight = jo4.optString("weight");
//									String gender = jo4.optString("gender");
//									double systolicPressure = jo4
//											.optDouble("systolicPressure");
//									String roleName = jo4.optString("roleName");
//									String familyMemberId = jo4
//											.optString("familyMemberId");
//									double diastolicPressure = jo4
//											.optDouble("diastolicPressure");
//									String temperature = jo4
//											.optString("temperature");
//									String fatPercentage=jo4.optString("fatPercentage");
//									double height=jo4.optDouble("height");
//									double memberWeight=jo4.optDouble("memberWeight");
//									String accessToken="";
//									if(!jo4.isNull("accessToken")){
//										accessToken=jo4.optString("accessToken");
//									}
//									String appCode="";
//									if(!jo4.isNull("appCode")){
//										appCode=jo4.optString("appCode");
//									}
//									String deviceName="";
//									if(!jo4.isNull("deviceName")){
//										deviceName=jo4.optString("deviceName");
//									}
//									int age=0;
//									if(!jo4.isNull("age")){
//										age=jo4.optInt("age");
//									}
//									MeasureMainInfo info = new MeasureMainInfo(
//											familyMemberId, gender, roleName,
//											weight, totalSleepMinutes,
//											activitySteps, temperature,
//											diastolicPressure, systolicPressure,
//											bloodGlucose,fatPercentage,height,memberWeight,deviceName,accessToken,appCode,age);
//									getList.add(info);
//								}
//								PF = OKGet;
//								initResult();
//							}
//						} else {
//							PF = FailNoData;
//							initResult();
//						}
//					} catch (JSONException e) {
//						System.out.println("解析错误");
//						PF = FailGet;
//						initResult();
//						e.printStackTrace();
//					}
//				}
//				Constant.exitProgressDialog(progressDialog);
//				super.onPostExecute(result);
//			}
//
//			@Override
//			protected void onCancelled() {
//				// TODO Auto-generated method stub
//				super.onCancelled();
//			}
//		}
//}

package com.sinosoft.fhcs.android.activity.bottomfragment;

/**
 * @CopyRight: SinoSoft.
 * @Description:测量主页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月20日.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.BraceletSleepActivity;
import com.sinosoft.fhcs.android.activity.BraceletSportActivity;
import com.sinosoft.fhcs.android.activity.DepartmentListActivity;
import com.sinosoft.fhcs.android.activity.MainActivity;
import com.sinosoft.fhcs.android.activity.MeasureBloodResultActivity;
import com.sinosoft.fhcs.android.activity.MeasureBloodSugerResultActivity;
import com.sinosoft.fhcs.android.activity.MeasureFatResultActivity;
import com.sinosoft.fhcs.android.activity.MeasureResultActivity;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.customview.MeasureMenuPop;
import com.sinosoft.fhcs.android.entity.MeasureMainInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.sinosoft.fhcs.android.util.UmengShareService;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static com.sinosoft.fhcs.android.ExitApplicaton.getList;

@SuppressLint("ValidFragment")
public class MeasurementFragment extends Fragment implements OnClickListener {
	private View titlebar;
	private Activity mActivity;
	private static Activity mActivity2;
	private TextView tvTitle;
	private Button btnMenuLeft;// 左菜单
	private Button btnMenuRight;// 右菜单
	private static CircleImageView img;
	private static TextView tvNickName;
	private static TextView tvWeight, tvTemperature, tvSleep, tvXueYa,
			tvXueTang, tvSport,tvTizhi;
	private LinearLayout layoutWeight, layoutTemperature, layoutSleep,
			layoutXueYa, layoutXueTang, layoutSport,layoutTizhi,layoutWenzhen;
	private static String memberName = "";
	private static String memberId = "";
	private static int memberHeight =0;
	private static int memberWeight=0;
	private static String accessToken="";
	private static String deviceName="";
	private static String appCode="";
	private static int memberAge=0;
	private static String memberGender="";
	private static String memberBirth="";
	/**
	 * 网络请求
	 */
	private ProgressDialog progressDialog;// 进度条
	private static final int OKGet = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int FailGet = 1003;// 失败
	private static final int FailNoData = 1004;// 没有数据
	private int PF = 1000;
	private String userId = "";
	/**
	 * 分享
	 */
	private Button btnShare;// 分享
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public MeasurementFragment() {
		// TODO Auto-generated constructor stub
	}
	public MeasurementFragment(MainActivity mActivity) {

		this.mActivity = mActivity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_measure, container,
				false);
		mActivity2= getActivity();
		// 从首选项获取userId
		SharedPreferences prefs = getActivity().getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		titlebar = (View) view.findViewById(R.id.frag_measure_view);
		tvTitle = (TextView) view.findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_left_main));
		btnMenuLeft = (Button) view.findViewById(R.id.titlebar_btn_memuleft);
		btnMenuLeft.setVisibility(View.VISIBLE);
		btnMenuLeft.setOnClickListener(this);
		btnMenuRight = (Button) view.findViewById(R.id.titlebar_btn_xiala_measure);
		btnMenuRight.setVisibility(View.VISIBLE);
		btnMenuRight.setOnClickListener(this);
		tvNickName = (TextView) view
				.findViewById(R.id.frag_measure_tv_nickname);
		img = (CircleImageView) view.findViewById(R.id.frag_measure_img);
		tvWeight = (TextView) view.findViewById(R.id.frag_measure_tv_weight);
		tvTemperature = (TextView) view
				.findViewById(R.id.frag_measure_tv_temperature);
		tvSleep = (TextView) view.findViewById(R.id.frag_measure_tv_sleep);
		tvXueYa = (TextView) view.findViewById(R.id.frag_measure_tv_pressure);

		tvXueTang = (TextView) view.findViewById(R.id.frag_measure_tv_glucose);
		tvSport = (TextView) view.findViewById(R.id.frag_measure_tv_stepnum);
		tvTizhi= (TextView) view.findViewById(R.id.frag_measure_tv_tizhi);
		layoutWeight = (LinearLayout) view
				.findViewById(R.id.frag_measure_layout_weight);
		layoutWeight.setOnClickListener(this);
		layoutTemperature = (LinearLayout) view
				.findViewById(R.id.frag_measure_layout_temperature);
		layoutTemperature.setOnClickListener(this);
		layoutSleep = (LinearLayout) view
				.findViewById(R.id.frag_measure_layout_sleep);
		layoutSleep.setOnClickListener(this);
		layoutXueYa = (LinearLayout) view
				.findViewById(R.id.frag_measure_layout_pressure);
		layoutXueYa.setOnClickListener(this);
		layoutXueTang = (LinearLayout) view
				.findViewById(R.id.frag_measure_layout_glucose);
		layoutXueTang.setOnClickListener(this);
		layoutSport = (LinearLayout) view
				.findViewById(R.id.frag_measure_layout_stepnum);
		layoutSport.setOnClickListener(this);
		layoutTizhi= (LinearLayout) view
				.findViewById(R.id.frag_measure_layout_tizhi);
		layoutTizhi.setOnClickListener(this);
		layoutWenzhen= (LinearLayout) view
				.findViewById(R.id.frag_measure_layout_wenzhen);
		layoutWenzhen.setOnClickListener(this);
		btnShare = (Button) view.findViewById(R.id.frag_measure_btn_share);
		btnShare.setOnClickListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("测量主页"); // 统计页面
		if (!HttpManager.isNetworkAvailable(getActivity())) {
//			Constant.showDialog(getActivity(), "您的网络没连接好，请检查后重试！");
			Toast.makeText(getActivity(), "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		GetListRequest re = new GetListRequest();
		re.execute(HttpManager.urlGetLastHealth(userId));
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("测量主页");
	}

	private void initResult() {
		if(getActivity() == null){

		}else{
			try {
				if (PF == FailServer) {
//				Constant.showDialog(getActivity(), "服务器响应超时!");
					Toast.makeText(getActivity(), "服务器响应超时!", Toast.LENGTH_SHORT).show();
				} else if (PF == FailGet) {
					Toast.makeText(getActivity(), "获取数据失败!", Toast.LENGTH_SHORT).show();
				} else if (PF == FailNoData) {
					Toast.makeText(getActivity(), "目前还没有数据，请添加!", Toast.LENGTH_SHORT)
							.show();
				} else if (PF == OKGet) {
					// 判断。。。。。
					SharedPreferences prefs = getActivity().getSharedPreferences(
							"measureInfo", Context.MODE_PRIVATE);
					String memberId = prefs.getString("memberId", "");
					if (memberId.equals("")) {
						initData(getList.get(0));
					} else {
						int position = 0;
						for (int i = 0; i < getList.size(); i++) {
							if (memberId.equals(getList.get(i).getMemberId())) {
								position = i;
							}
						}
						initData(getList.get(position));
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

	public static void initData(MeasureMainInfo measureInfo) {
		// 用于把当前的ID上传个服务端
		System.out.println("家庭成员ID=" + measureInfo.getMemberId() + "");
		SharedPreferences prefs = mActivity2.getSharedPreferences(
				"measureInfo", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("memberId", measureInfo.getMemberId());
		editor.commit();
		tvNickName.setText(measureInfo.getNickName());
		img.setImageResource(Constant.ImageIdbg(measureInfo.getNickName(),
				measureInfo.getGender()));
		tvWeight.setText(measureInfo.getWeight());
		tvTemperature.setText(measureInfo.getTemperature());
		DecimalFormat df = new DecimalFormat("#.0");
		double hour = Double.valueOf(measureInfo.getSleep()) / 60;
		if (measureInfo.getSleep().equals("0")
				|| (df.format(hour) + "").equals(".0")) {
			tvSleep.setText("0");
		} else {
			tvSleep.setText(df.format(hour) + "");
		}
		tvXueYa.setText((int) (measureInfo.getDiya()) + "/"
				+ (int) (measureInfo.getGaoya()));
		tvXueTang.setText(measureInfo.getBloodGlucose() + "");
		tvSport.setText(measureInfo.getStepNum());
		tvTizhi.setText(measureInfo.getTizhi());
		memberName = measureInfo.getNickName();
		memberId=measureInfo.getMemberId();
		memberHeight=(int)(measureInfo.getHeight());
		memberWeight=(int)(measureInfo.getMemberWeight());
		deviceName=measureInfo.getDeviceName();
		accessToken=measureInfo.getAccessToken();
		appCode=measureInfo.getAppCode();
		memberAge=measureInfo.getAge();
		memberGender=measureInfo.getGender();
		memberBirth=measureInfo.getBirth();
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
			case R.id.titlebar_btn_xiala_measure:
				// 右菜单
				MeasureMenuPop menuWindow = new MeasureMenuPop(getActivity(), getList);
				menuWindow.showAsDropDown(titlebar);
				break;
			case R.id.frag_measure_layout_weight:
				// 体重
				Intent intent1 = new Intent(getActivity(), MeasureResultActivity.class);
				intent1.putExtra("strTitle", "体重");
				intent1.putExtra("familyMemberId", memberId);
				intent1.putExtra("birth", memberBirth);
				intent1.putExtra("memberName", memberName);
				intent1.putExtra("height", memberHeight);
				intent1.putExtra("weight", memberWeight);
				intent1.putExtra("age", memberAge);
				intent1.putExtra("gender", memberGender);
				startActivity(intent1);
				break;
			case R.id.frag_measure_layout_temperature:
				// 体温
				Intent intent2 = new Intent(getActivity(), MeasureResultActivity.class);
				intent2.putExtra("strTitle", "体温");
				intent2.putExtra("familyMemberId", memberId);
				intent2.putExtra("birth", memberBirth);
				intent2.putExtra("memberName", memberName);
				intent2.putExtra("height", memberHeight);
				intent2.putExtra("weight", memberWeight);
				intent2.putExtra("age", memberAge);
				intent2.putExtra("gender", memberGender);
				startActivity(intent2);
				break;
			case R.id.frag_measure_layout_sleep:
				// 睡眠
				Intent intent3 = new Intent(getActivity(), BraceletSleepActivity.class);
				intent3.putExtra("memberId", memberId);
				intent3.putExtra("memberName", memberName);
				intent3.putExtra("deviceName", deviceName);
				intent3.putExtra("accessToken", accessToken);
				intent3.putExtra("appCode", appCode);
				intent3.putExtra("birth", memberBirth);
				intent3.putExtra("memberName", memberName);
				intent3.putExtra("height", memberHeight);
				intent3.putExtra("weight", memberWeight);
				intent3.putExtra("age", memberAge);
				intent3.putExtra("gender", memberGender);
				startActivity(intent3);
				break;
			case R.id.frag_measure_layout_pressure:
				// 血压
				//Intent intent4 = new Intent(getActivity(), MeasureResultActivity.class);
				Intent intent4 = new Intent(getActivity(), MeasureBloodResultActivity.class);
				intent4.putExtra("strTitle", "血压");
				intent4.putExtra("familyMemberId", memberId);
				intent4.putExtra("birth", memberBirth);
				intent4.putExtra("memberName", memberName);
				intent4.putExtra("height", memberHeight);
				intent4.putExtra("weight", memberWeight);
				intent4.putExtra("age", memberAge);
				intent4.putExtra("gender", memberGender);
				startActivity(intent4);
				break;
			case R.id.frag_measure_layout_glucose:
				// 血糖
				//	Intent intent5 = new Intent(getActivity(), MeasureResultActivity.class);
				Intent intent5 = new Intent(getActivity(), MeasureBloodSugerResultActivity.class);
				intent5.putExtra("strTitle", "血糖");
				intent5.putExtra("familyMemberId", memberId);
				intent5.putExtra("birth", memberBirth);
				intent5.putExtra("memberName", memberName);
				intent5.putExtra("height", memberHeight);
				intent5.putExtra("weight", memberWeight);
				intent5.putExtra("age", memberAge);
				intent5.putExtra("gender", memberGender);
				startActivity(intent5);
				break;
			case R.id.frag_measure_layout_stepnum:
				// 运动
				Intent intent6 = new Intent(getActivity(), BraceletSportActivity.class);
				intent6.putExtra("memberId", memberId);
				intent6.putExtra("memberName", memberName);
				intent6.putExtra("deviceName", deviceName);
				intent6.putExtra("accessToken", accessToken);
				intent6.putExtra("appCode", appCode);
				intent6.putExtra("birth", memberBirth);
				intent6.putExtra("memberName", memberName);
				intent6.putExtra("height", memberHeight);
				intent6.putExtra("weight", memberWeight);
				intent6.putExtra("age", memberAge);
				intent6.putExtra("gender", memberGender);
				startActivity(intent6);
				break;
			case R.id.frag_measure_layout_tizhi:
				// 体脂
				Intent intent7 = new Intent(getActivity(), MeasureFatResultActivity.class);
				intent7.putExtra("strTitle", "体脂");
				intent7.putExtra("familyMemberId", memberId);
				intent7.putExtra("birth", memberBirth);
				intent7.putExtra("memberName", memberName);
				intent7.putExtra("height", memberHeight);
				intent7.putExtra("weight", memberWeight);
				intent7.putExtra("age", memberAge);
				intent7.putExtra("gender", memberGender);
				startActivity(intent7);
				break;
			case R.id.frag_measure_layout_wenzhen://问诊
				Intent intent8 = new Intent(getActivity(), DepartmentListActivity.class);
				intent8.putExtra("strTitle", "问诊");
				intent8.putExtra("familyMemberId", memberId);
				intent8.putExtra("birth", memberBirth);
				intent8.putExtra("memberName", memberName);
				intent8.putExtra("height", memberHeight);
				intent8.putExtra("weight", memberWeight);
				intent8.putExtra("age", memberAge);
				intent8.putExtra("gender", memberGender);
				startActivity(intent8);
				break;
			case R.id.frag_measure_btn_share:
				// 分享
				if (PF != OKGet) {
					Toast.makeText(getActivity(), "该成员没有测量数据,还不能进行分享哦!", Toast.LENGTH_SHORT)
							.show();
				} else {
					try {
						quickShare();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(getActivity(), "暂时无法分享！", Toast.LENGTH_SHORT).show();
					}

				}
				break;
			default:
				break;
		}

	}
	/**
	 * @功能描述 : 快速分享接口（呼出编辑页）
	 */
	private void quickShare() {
		//Toast.makeText(mActivity2, "开始分享",Toast.LENGTH_SHORT).show();
		UmengShareService.share(getActivity(),
				"我正在使用中国电信翼家康健康云服务测量身体状况，足不出户就能呵护身体健康！网址：" ,Constant.HOST,
				new UMImage(getActivity(), shotImage()),umShareListener);
	}
	private static UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Log.d("plat","platform"+platform);
			if(platform.name().equals("WEIXIN_FAVORITE")){
				Toast.makeText(mActivity2,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(mActivity2, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(mActivity2,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
			if(t!=null){
				Log.d("throw","throw:"+t.getMessage());
			}
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(mActivity2,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** attention to this below ,must add this**/
		UMShareAPI.get(mActivity2).onActivityResult(requestCode, resultCode, data);
		Log.d("result","onActivityResult");
	}

	/**
	 * 截屏方法
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Bitmap shotImage() {
		View view = getActivity().getWindow().getDecorView();
		Rect rect = new Rect();
		view.getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top;
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		view.layout(0, 0, display.getWidth(), display.getHeight());
		view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
		Bitmap bmp = null;
		try {
			bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
					statusBarHeight, view.getWidth(), view.getHeight()
							- statusBarHeight);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
					statusBarHeight, view.getWidth(), view.getHeight()
							- statusBarHeight);
		}

		return bmp;
	}
	// 网络请求
	private class GetListRequest extends AsyncTask<Object, Void, String> {
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
			Log.e("getLastListUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						JSONObject jo2 = jo.getJSONObject("data");
						JSONArray ja = jo2.getJSONArray("data");
						if (ja.length() == 0) {
							PF = FailNoData;
							initResult();
						} else {
							getList.clear();
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo4 = ja.getJSONObject(i);
								String totalSleepMinutes = jo4
										.optString("totalSleepMinutes");
								double bloodGlucose = jo4
										.optDouble("bloodGlucose");
								String activitySteps = jo4
										.optString("activitySteps");
								String weight = jo4.optString("weight");
								String gender = jo4.optString("gender");
								double systolicPressure = jo4
										.optDouble("systolicPressure");
								String roleName = jo4.optString("roleName");
								String familyMemberId = jo4
										.optString("familyMemberId");
								double diastolicPressure = jo4
										.optDouble("diastolicPressure");
								String temperature = jo4
										.optString("temperature");
								String fatPercentage=jo4.optString("fatPercentage");
								double height = jo4.optDouble("height");
								double memberWeight=jo4.optDouble("memberWeight");
								String accessToken="";
								if(!jo4.isNull("accessToken")){
									accessToken=jo4.optString("accessToken");
								}
								String deviceName="";
								if(!jo4.isNull("deviceName")){
									deviceName=jo4.optString("deviceName");
								}
								String appCode="";
								if(!jo4.isNull("appCode")){
									appCode=jo4.optString("appCode");
								}
								int age=0;
								if(!jo4.isNull("age")){
									age=jo4.optInt("age");
								}
								String birth="";
								if(!jo4.isNull("birthday")){
									birth=jo4.optString("birthday");
								}
								MeasureMainInfo info = new MeasureMainInfo(
										familyMemberId, gender, roleName,
										weight, totalSleepMinutes,
										activitySteps, temperature,
										diastolicPressure, systolicPressure,
										bloodGlucose,fatPercentage,height,memberWeight,deviceName,accessToken,appCode,age,birth);
								getList.add(info);
							}
							PF = OKGet;
							initResult();
						}
					} else {
						PF = FailNoData;
						initResult();
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = FailGet;
					initResult();
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

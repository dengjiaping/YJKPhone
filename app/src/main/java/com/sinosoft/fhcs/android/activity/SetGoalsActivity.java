//package com.sinosoft.fhcs.android.activity;
//
///**
// * @CopyRight: SinoSoft.
// * @Description:设定目标页   废弃页面
// * @Author: wangshuangshuang.
// * @Create: 2014年12月24日.
// */
//import java.text.DecimalFormat;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.sinosoft.fhcs.android.ExitApplicaton;
//import com.sinosoft.fhcs.android.R;
//import com.sinosoft.fhcs.android.customview.MyMenuPopupWindow;
//import com.sinosoft.fhcs.android.customview.VerticalLineView;
//import com.sinosoft.fhcs.android.entity.FamilyMember;
//import com.sinosoft.fhcs.android.util.Constant;
//import com.sinosoft.fhcs.android.util.HttpManager;
//import com.umeng.analytics.MobclickAgent;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.Html;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class SetGoalsActivity extends Activity implements OnClickListener,
//		SeekBar.OnSeekBarChangeListener {
//	private TextView tvTitle;
//	private Button btnBack;
//	private Button btnMenu;
//	private Button btnSure;// 确定
//	private FamilyMember member = new FamilyMember();
//	// Content
//	private TextView tvBMI;// 显示正常体重范围
//	private SeekBar barSleep, barSport, barWeight,barGaoYa,barXueTang,barZfl;// 睡眠，运动，体重，高压，血糖，脂肪率
//	private TextView tvSleepGoalH, tvSleepGoalM, tvSportGoal, tvWeightGoal,tvGaoYaGoal,tvXueTangGoal,tvZflGoal;// 目标睡眠小时，睡眠分钟，运动，体重,高压，血糖，脂肪率
//	private VerticalLineView lineViewSleep, lineViewSport, lineViewWeight,lineViewGaoYa,lineViewXueTang,lineViewZfl;
//	private int currentSleep = 480, currentSport = 10000, currentWeight = 0,currentGaoYa=125,currentXueTang=50,currentZfl=20;// 当前睡眠时间（分钟），当前运动步数，当前体重
//	private int maxSleep = 600, maxSport = 20000, maxWeight = 100,maxGaoYa=160,maxXueTang=130,maxZfl=40;// 最大体重  血糖显示除10
//	private int goalSleep = 480, goalSport = 10000, goalWeight = 50,goalGaoYa=125,goalXueTang=50,goalZfl=20;// 目标值
//	private String goalSleepCommit = "480", goalSportCommit = "10000",
//			goalWeightCommit = "50",goalGaoYaCommit="125",goalXueTangCommit="50",goalZflCommit="20";// 提交的目标值
//	private int minSleep = 300, minSport = 2000, minWeight = 0,minGaoYa=80,minXueTang=30,minZfl=10;// 最小分钟数，最小步数,最小体重
//	private double height = 160;
//	private int valueSport, valueSleep;// 用于判断向左滑还是向右滑
//	private float thumbWidth = 40;
//	private float seedBarlineWidth_T = 0, seedBarlineWidth_S = 0,
//			seedBarlineWidth_W = 0,seedBarlineWidth_G=0,seedBarlineWidth_X=0,seedBarlineWidth_Z=0;
//	private float drawLineHeight = 20;
//	private float seekBarMarginTop = 10;
//	private float seekBarY_T = 0, seekBarY_S = 0, seekBarY_W = 0,seekBarY_G = 0,seekBarY_X = 0,seekBarY_Z = 0;
//	// 需要绘制的坐标
//	private float currentLineX_T = 0, currentLineX_S = 0, currentLineX_W = 0, currentLineX_G = 0, currentLineX_X = 0, currentLineX_Z = 0;
//	private float currentLineStartY_T = 0, currentLineStartY_S = 0,
//			currentLineStartY_W = 0,currentLineStartY_G = 0,currentLineStartY_X = 0,currentLineStartY_Z = 0;
//	private float currentLineEndY_T = 0, currentLineEndY_S = 0,
//			currentLineEndY_W = 0,currentLineEndY_G = 0,currentLineEndY_X = 0,currentLineEndY_Z = 0;
//	private float currentTextStartX_T, currentTextStartX_S,
//			currentTextStartX_W,currentTextStartX_G,currentTextStartX_X,currentTextStartX_Z;
//	private float currentTextStartY_T, currentTextStartY_S,
//			currentTextStartY_W,currentTextStartY_G,currentTextStartY_X,currentTextStartY_Z;
//	// 自定义view 宽度
//	private int viewWidth_T, viewWidth_S, viewWidth_W,viewWidth_G,viewWidth_X,viewWidth_Z;
//	// 自定义view 宽度和progress余差一般
//	private float halfWidth_T, halfWidth_S, halfWidth_W,halfWidth_G,halfWidth_X,halfWidth_Z;
//	private int textSize = 14;
//	// 自定义的弹出框类
//	private MyMenuPopupWindow menuWindow;
//	/**
//	 * 网络请求
//	 */
//	private ProgressDialog progressDialog;// 进度条
//	private static final int OKGet = 1001;// 成功
//	private static final int FailServer = 1002;// 连接超时
//	private static final int FailGet = 1003;// 失败
//	private static final int FailNoData = 1004;// 没有数据
//	private static final int OKCommit = 1005;// 成功
//	private static final int FailCommit = 1006;// 失败
//	private int PF = 1000;
//
//	DecimalFormat format = new DecimalFormat(("#####0.0"));
////	format.format(Double.valueOf(xueTangShow)/10) + ""
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_setgoals);
//		ExitApplicaton.getInstance().addActivity(this);
//		member = (FamilyMember) this.getIntent().getExtras().get("member");
//		height = member.getHeight();
//		// 正常的BMI 18.5-23.9 体质指数（BMI）=体重（kg）÷身高^2（m）
//		goalWeight = (int) (22 * Constant.divideF((float) height, 100) * Constant
//				.divideF((float) height, 100));
//		currentWeight=goalWeight;
//		init();// 初始化控件
//		initData();// 初始化数据
//		initRequest();
//	}
//
//	// 请求数据
//	private void initRequest() {
//		if (!HttpManager.isNetworkAvailable(this)) {
////			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
//			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		GetGoalRequest re = new GetGoalRequest();
//		re.execute(HttpManager.urlGetGoalInfo(member.getId() + ""));
//
//	}
//
//	public void onResume() {
//		super.onResume();
//		MobclickAgent.onPageStart("设定目标页"); // 统计页面
//		MobclickAgent.onResume(this); // 统计时长
//	}
//
//	public void onPause() {
//		super.onPause();
//		MobclickAgent.onPageEnd("设定目标页"); // 保证 onPageEnd 在onPause 之前调用,因为
//											// onPause 中会保存信息
//		MobclickAgent.onPause(this);
//	}
//
//	private void init() {
//		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
//		tvTitle.setText(getResources().getString(R.string.title_setgoals));
//		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
//		btnBack.setVisibility(View.VISIBLE);
//		btnBack.setOnClickListener(this);
//		btnMenu = (Button) findViewById(R.id.titlebar_btn_memu);
//		btnMenu.setVisibility(View.VISIBLE);
//		btnMenu.setOnClickListener(this);
//
//		barSleep = (SeekBar) findViewById(R.id.setgoal_seekbar_sleep);
//		barSport = (SeekBar) findViewById(R.id.setgoal_seekbar_sport);
//		barWeight = (SeekBar) findViewById(R.id.setgoal_seekbar_weight);
//		barGaoYa = (SeekBar) findViewById(R.id.setgoal_seekbar_gaoya);
//		barXueTang = (SeekBar) findViewById(R.id.setgoal_seekbar_xuetang);
//		barZfl = (SeekBar) findViewById(R.id.setgoal_seekbar_zfl);
//
//		tvWeightGoal = (TextView) findViewById(R.id.setgoals_tv_weight);
//		tvSportGoal = (TextView) findViewById(R.id.setgoals_tv_sport);
//		tvSleepGoalH = (TextView) findViewById(R.id.setgoals_tv_Hour);
//		tvSleepGoalM = (TextView) findViewById(R.id.setgoals_tv_Minute);
//		tvGaoYaGoal = (TextView) findViewById(R.id.setgoals_tv_gaoya);
//		tvXueTangGoal = (TextView) findViewById(R.id.setgoals_tv_xuetang);
//		tvZflGoal = (TextView) findViewById(R.id.setgoals_tv_zfl);
//
//		lineViewSleep = (VerticalLineView) findViewById(R.id.setgoal_seekbar_lineview_sleep);
//		lineViewSport = (VerticalLineView) findViewById(R.id.setgoal_seekbar_lineview_sport);
//		lineViewWeight = (VerticalLineView) findViewById(R.id.setgoal_seekbar_lineview_weight);
//		lineViewGaoYa = (VerticalLineView) findViewById(R.id.setgoal_seekbar_lineview_gaoya);
//		lineViewXueTang = (VerticalLineView) findViewById(R.id.setgoal_seekbar_lineview_xuetang);
//		lineViewZfl = (VerticalLineView) findViewById(R.id.setgoal_seekbar_lineview_zfl);
//
//		tvBMI = (TextView) findViewById(R.id.setgoal_seekbar_bmi);
//		btnSure = (Button) findViewById(R.id.setgoal_seekbar_btn_sure);
//		btnSure.setOnClickListener(this);
//	}
//
//	private void initData() {
//		// 正常的BMI 18.5-23.9 体质指数（BMI）=体重（kg）÷身高^2（m）
//		int weightMin = (int) (18.5 * Constant.divideF((float) height, 100) * Constant
//				.divideF((float) height, 100));
//		int weightMax = (int) (23.9 * Constant.divideF((float) height, 100) * Constant
//				.divideF((float) height, 100));
//		tvBMI.setText(Html
//				.fromHtml("根据世界卫生组织的BMI公式，您的体重应该介于<font color=\"#faa701\"><big>"
//						+ weightMin
//						+ "</big></font>公斤到<font color=\"#faa701\"><big>"
//						+ weightMax + "</big></font>公斤之间。"));
//		// 设置向左滑右滑
//		valueSport = getSportProgress(goalSport + minSport);
//		valueSleep = getSleepProgress(goalSleep + minSleep);
//
//		// 设置最大值
//		barSleep.setMax(maxSleep - minSleep);
//		barSport.setMax(maxSport - minSport);
//		barWeight.setMax(maxWeight - minWeight);
//		barGaoYa.setMax(maxGaoYa-minGaoYa);
//		barXueTang.setMax(maxXueTang-minXueTang);
//		barZfl.setMax(maxZfl-minZfl);
//		// 设置目标值
//		barSleep.setProgress(goalSleep - minSleep);
//		barSport.setProgress(goalSport - minSport);
//		barWeight.setProgress(goalWeight - minWeight);
//		barGaoYa.setProgress(goalGaoYa - minGaoYa);
//		barXueTang.setProgress(goalXueTang - minXueTang);
//		barZfl.setProgress(goalZfl - minZfl);
//		// 设置目标显示
//		tvGaoYaGoal.setText(barGaoYa.getProgress() + minGaoYa + "");
//		tvXueTangGoal.setText(format.format(Double.valueOf(barXueTang.getProgress() + minXueTang)/10) + "");
//		tvZflGoal.setText(barZfl.getProgress() + minZfl + "");
//		tvSportGoal.setText(barSport.getProgress() + minSport + "");
//		tvWeightGoal.setText(barWeight.getProgress() + minWeight + "");
//		int temp = barSleep.getProgress() + minSleep;
//		int H = temp / 60;
//		temp = temp % 60;
//		int M = temp;
//		tvSleepGoalH.setText(H + "");
//		tvSleepGoalM.setText(M + "");
//		// 画当前值
//		lineViewSleep.post(new Runnable() {
//
//			@Override
//			public void run() {
//				// 自定义view宽度要大于progress长度，显示全文字
//				lineViewSleep.invalidate();
//				viewWidth_T = lineViewSleep.getWidth();
//			}
//		});
//		lineViewSport.post(new Runnable() {
//
//			@Override
//			public void run() {
//				lineViewSport.invalidate();
//				viewWidth_S = lineViewSport.getWidth();
//			}
//		});
//		lineViewWeight.post(new Runnable() {
//
//			@Override
//			public void run() {
//				lineViewWeight.invalidate();
//				viewWidth_W = lineViewWeight.getWidth();
//			}
//		});
//		lineViewGaoYa.post(new Runnable() {
//
//			@Override
//			public void run() {
//				lineViewGaoYa.invalidate();
//				viewWidth_G = lineViewGaoYa.getWidth();
//			}
//		});
//		lineViewXueTang.post(new Runnable() {
//
//			@Override
//			public void run() {
//				lineViewXueTang.invalidate();
//				viewWidth_X = lineViewXueTang.getWidth();
//			}
//		});
//		lineViewZfl.post(new Runnable() {
//
//			@Override
//			public void run() {
//				lineViewZfl.invalidate();
//				viewWidth_Z = lineViewZfl.getWidth();
//			}
//		});
//		barSleep.post(new Runnable() {
//
//			@Override
//			public void run() {
//				// 获取view大小需要放到此回调接口，接口在view绘制完成执行
//				seedBarlineWidth_T = barSleep.getWidth() - thumbWidth;// 计算出seekbar的实际长条长度
//				seekBarY_T = barSleep.getY();// 计算出长条纵坐标
//				// 减去progress余差
//				halfWidth_T = Constant.divideF(
//						viewWidth_T - barSleep.getWidth(), 2);
//				currentLineSleepPosition(currentSleep - minSleep, maxSleep
//						- minSleep, seedBarlineWidth_T);
//
//				int temp = currentSleep;
//				int H = temp / 60;
//				temp = temp % 60;
//				int M = temp;
//				String temp2 = "";
//				if (M == 0) {
//					temp2 = H + "小时";
//				} else {
//					temp2 = H + "小时" + M + "分";
//				}
//
//				lineViewSleep.setLineAndPoint(
//						getResources().getColor(R.color.text_line_black),
//						textSize, currentLineX_T + halfWidth_T,
//						currentLineStartY_T, currentLineX_T + halfWidth_T,
//						currentLineEndY_T, currentTextStartX_T + halfWidth_T,
//						currentTextStartY_T, temp2);
//			}
//		});
//		// 运动
//		barSport.post(new Runnable() {
//
//			@Override
//			public void run() {
//				seedBarlineWidth_S = barSport.getWidth() - thumbWidth;
//				seekBarY_S = barSport.getY();
//				halfWidth_S = Constant.divideF(
//						viewWidth_S - barSport.getWidth(), 2);
//				currentLineSportPosition(currentSport - minSport, maxSport
//						- minSport, seedBarlineWidth_S);
//				lineViewSport.setLineAndPoint(
//						getResources().getColor(R.color.text_line_black),
//						textSize, currentLineX_S + halfWidth_S,
//						currentLineStartY_S, currentLineX_S + halfWidth_S,
//						currentLineEndY_S, currentTextStartX_S + halfWidth_S,
//						currentTextStartY_S, (int) currentSport + "步");
//			}
//		});
//		// 体重
//		barWeight.post(new Runnable() {
//
//			@Override
//			public void run() {
//				seedBarlineWidth_W = barWeight.getWidth() - thumbWidth;
//				seekBarY_W = barWeight.getY();
//				halfWidth_W = Constant.divideF(
//						viewWidth_W - barWeight.getWidth(), 2);
//				currentLineWeightPosition(currentWeight - minWeight, maxWeight
//						- minWeight, seedBarlineWidth_W);
//				lineViewWeight.setLineAndPoint(
//						getResources().getColor(R.color.text_line_black),
//						textSize, currentLineX_W + halfWidth_W,
//						currentLineStartY_W, currentLineX_W + halfWidth_W,
//						currentLineEndY_W, currentTextStartX_W + halfWidth_W,
//						currentTextStartY_W, (int) currentWeight + "公斤");
//			}
//		});
//		//高压
//		barGaoYa.post(new Runnable() {
//
//			@Override
//			public void run() {
//				seedBarlineWidth_G = barGaoYa.getWidth() - thumbWidth;
//				seekBarY_G = barGaoYa.getY();
//				halfWidth_G = Constant.divideF(
//						viewWidth_G - barGaoYa.getWidth(), 2);
//				currentLineGaoYaPosition(currentGaoYa - minGaoYa, maxGaoYa
//						- minGaoYa, seedBarlineWidth_G);
//				lineViewGaoYa.setLineAndPoint(
//						getResources().getColor(R.color.text_line_black),
//						textSize, currentLineX_G + halfWidth_G,
//						currentLineStartY_G, currentLineX_G + halfWidth_G,
//						currentLineEndY_G, currentTextStartX_G + halfWidth_G,
//						currentTextStartY_G, (int) currentGaoYa + "mmHg");
//			}
//		});
//		//血糖
//		barXueTang.post(new Runnable() {
//
//			@Override
//			public void run() {
//				seedBarlineWidth_X = barXueTang.getWidth() - thumbWidth;
//				seekBarY_X = barXueTang.getY();
//				halfWidth_X = Constant.divideF(
//						viewWidth_X - barXueTang.getWidth(), 2);
//				currentLineXueTangPosition(currentXueTang - minXueTang, maxXueTang
//						- minXueTang, seedBarlineWidth_X);
//				lineViewXueTang.setLineAndPoint(
//						getResources().getColor(R.color.text_line_black),
//						textSize, currentLineX_X + halfWidth_X,
//						currentLineStartY_X, currentLineX_X + halfWidth_X,
//						currentLineEndY_X, currentTextStartX_X + halfWidth_X,
//						currentTextStartY_X, format.format(Double.valueOf(currentXueTang)/10) + "mmol/L");
//			}
//		});
//		//脂肪率
//		barZfl.post(new Runnable() {
//
//			@Override
//			public void run() {
//				seedBarlineWidth_Z = barZfl.getWidth() - thumbWidth;
//				seekBarY_Z = barZfl.getY();
//				halfWidth_Z = Constant.divideF(
//						viewWidth_Z - barZfl.getWidth(), 2);
//				currentLineZflPosition(currentZfl - minZfl, maxZfl
//						- minZfl, seedBarlineWidth_Z);
//				lineViewZfl.setLineAndPoint(
//						getResources().getColor(R.color.text_line_black),
//						textSize, currentLineX_Z + halfWidth_Z,
//						currentLineStartY_Z, currentLineX_Z + halfWidth_Z,
//						currentLineEndY_Z, currentTextStartX_Z + halfWidth_Z,
//						currentTextStartY_Z, (int) currentZfl + "%");
//			}
//		});
//	}
//
//	// current坐标睡眠
//	public void currentLineSleepPosition(float currentValue,
//			float seekBarlineWidthMax, float seekbarWidth) { // 当前值和最大值比例系数
//		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
//		// -2 px 误差
//		currentLineX_T = currentValue * biliX
//				+ (Constant.divideF(thumbWidth, 2) - 0);
//		currentLineStartY_T = seekBarY_T - seekBarMarginTop - drawLineHeight;
//		currentLineEndY_T = seekBarY_T - seekBarMarginTop;
//		// 获取文字传入点坐标
//		currentTextStartX_T = currentLineX_T - 20;
//		currentTextStartY_T = currentLineStartY_T - 10;
//	}
//
//	// 运动
//	public void currentLineSportPosition(float currentValue,
//			float seekBarlineWidthMax, float seekbarWidth) {
//		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
//		currentLineX_S = currentValue * biliX
//				+ (Constant.divideF(thumbWidth, 2) - 2);
//		currentLineStartY_S = seekBarY_S - seekBarMarginTop - drawLineHeight;
//		currentLineEndY_S = seekBarY_S - seekBarMarginTop;
//		currentTextStartX_S = currentLineX_S - 20;
//		currentTextStartY_S = currentLineStartY_S - 10;
//	}
//
//	// 体重
//	public void currentLineWeightPosition(float currentValue,
//			float seekBarlineWidthMax, float seekbarWidth) {
//		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
//		currentLineX_W = currentValue * biliX
//				+ (Constant.divideF(thumbWidth, 2) - 2);
//		currentLineStartY_W = seekBarY_W - seekBarMarginTop - drawLineHeight;
//		currentLineEndY_W = seekBarY_W - seekBarMarginTop;
//		currentTextStartX_W = currentLineX_W - 20;
//		currentTextStartY_W = currentLineStartY_W - 10;
//	}
//	//高压
//	public void currentLineGaoYaPosition(float currentValue,
//			float seekBarlineWidthMax, float seekbarWidth) {
//		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
//		currentLineX_G = currentValue * biliX
//				+ (Constant.divideF(thumbWidth, 2) - 2);
//		currentLineStartY_G = seekBarY_G - seekBarMarginTop - drawLineHeight;
//		currentLineEndY_G = seekBarY_G - seekBarMarginTop;
//		currentTextStartX_G = currentLineX_G - 20;
//		currentTextStartY_G = currentLineStartY_G - 10;
//	}
//	//血糖
//	public void currentLineXueTangPosition(float currentValue,
//			float seekBarlineWidthMax, float seekbarWidth) {
//		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
//		currentLineX_X = currentValue * biliX
//				+ (Constant.divideF(thumbWidth, 2) - 2);
//		currentLineStartY_X = seekBarY_X - seekBarMarginTop - drawLineHeight;
//		currentLineEndY_X = seekBarY_X - seekBarMarginTop;
//		currentTextStartX_X = currentLineX_X - 20;
//		currentTextStartY_X = currentLineStartY_X - 10;
//	}
//	//脂肪率
//	public void currentLineZflPosition(float currentValue,
//			float seekBarlineWidthMax, float seekbarWidth) {
//		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
//		currentLineX_Z = currentValue * biliX
//				+ (Constant.divideF(thumbWidth, 2) - 2);
//		currentLineStartY_Z = seekBarY_Z - seekBarMarginTop - drawLineHeight;
//		currentLineEndY_Z = seekBarY_Z - seekBarMarginTop;
//		currentTextStartX_Z = currentLineX_Z - 10;
//		currentTextStartY_Z = currentLineStartY_Z - 10;
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.titlebar_btn_back:
//			finish();
//			break;
//		case R.id.titlebar_btn_memu:
//			// 菜单
//			menuWindow = new MyMenuPopupWindow(this, member);
//			menuWindow.showAtLocation(btnMenu, Gravity.TOP | Gravity.RIGHT, 0,
//					0);
//			break;
//		case R.id.setgoal_seekbar_btn_sure:
//			// 确定
//			int tempH = 0;
//			int tempM = 0;
//			if (!tvSleepGoalH.getText().toString().trim().equals("")) {
//				tempH = Integer.valueOf(tvSleepGoalH.getText().toString()
//						.trim());
//			}
//			if (!tvSleepGoalM.getText().toString().trim().equals("")) {
//				tempM = Integer.valueOf(tvSleepGoalM.getText().toString()
//						.trim());
//			}
//			goalSleepCommit = (tempH * 60 + tempM) + "";
//			goalSportCommit = tvSportGoal.getText().toString().trim();
//			goalWeightCommit = tvWeightGoal.getText().toString();
//			goalGaoYaCommit=tvGaoYaGoal.getText().toString();
//			goalXueTangCommit=tvXueTangGoal.getText().toString();
//			goalZflCommit=tvZflGoal.getText().toString();
//			System.err.println("goalSleepCommit=" + goalSleepCommit);
//			System.err.println("goalSportCommit=" + goalSportCommit);
//			System.err.println("goalWeightCommit=" + goalWeightCommit);
//			System.err.println("goalGaoYaCommit=" + goalGaoYaCommit);
//			System.err.println("goalXueTangCommit=" + goalXueTangCommit);
//			System.err.println("goalZflCommit=" + goalZflCommit);
//			if (!HttpManager.isNetworkAvailable(this)) {
////				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
//				Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			CommitGoalRequest re = new CommitGoalRequest();
//			re.execute(HttpManager.urlCommitGoalInfo());
//			break;
//		default:
//			break;
//		}
//
//	}
//
//	// 获取睡眠刻度值（30分钟一个单位，最小值为5个小时==300分钟==10个单位）
//	private int getSleepProgress(int progress) {
//		int p = progress / 30;
//		return p;
//	}
//
//	// 获取运动刻度值（500步一个单位，最小值为2000步==4个单位）
//	private int getSportProgress(int progress) {
//		int p = progress / 500;
//		return p;
//	}
//
//	// 设置显示运动数据
//	private int showSportData(int progress) {
//		int p = progress * 500;
//		return p;
//	}
//
//	// 设置显示睡眠数据
//	private int showSleepData(int progress) {
//		int p = progress * 30;
//		return p;
//	}
//
//	@Override
//	public void onProgressChanged(SeekBar seekBar, int progress,
//			boolean fromUser) {
//		switch (seekBar.getId()) {
//		case R.id.setgoal_seekbar_sleep:
//			int tempSleep = getSleepProgress(seekBar.getProgress() + minSleep);
//			// System.err.println("tempSleep="+tempSleep);
//			// System.err.println("valueSleep="+valueSleep);
//			if (valueSleep < tempSleep) {
//				valueSleep++;
//			} else if (valueSleep > tempSleep) {
//				valueSleep--;
//			}
//			// Log.e("valueSleep", valueSleep + "");
//			// Log.e("showSleepData(valueSleep)", showSleepData(valueSleep) +
//			// "");
//			barSleep.setProgress(showSleepData(valueSleep) - minSleep);
//			int temp2 = showSleepData(valueSleep);
//			if (temp2 >= maxSleep) {
//				temp2 = maxSleep;
//			} else if (temp2 <= minSleep) {
//				temp2 = minSleep;
//			}
//			int temp = temp2;
//			// int temp = seekBar.getProgress() + minSleep;
//			int H = temp / 60;
//			temp = temp % 60;
//			int M = temp;
//			tvSleepGoalH.setText(H + "");
//			tvSleepGoalM.setText(M + "");
//			break;
//		case R.id.setgoal_seekbar_sport:
//			int tempSport = getSportProgress(seekBar.getProgress() + minSport);
//			// System.err.println("tempSport="+tempSport);
//			// System.err.println("valueSport="+valueSport);
//			if (valueSport < tempSport) {
//
//				valueSport++;
//			} else if (valueSport > tempSport) {
//				valueSport--;
//			}
//			// Log.e("valueSport", valueSport + "");
//			// Log.e("showSportData(valueSport)", showSportData(valueSport) +
//			// "");
//			barSport.setProgress(showSportData(valueSport) - minSport);
//			int temp3 = showSportData(valueSport);
//			if (temp3 >= maxSport) {
//				temp3 = maxSport;
//			} else if (temp3 <= minSport) {
//				temp3 = minSport;
//			}
//			tvSportGoal.setText(temp3 + "");
//			// tvSportGoal.setText(seekBar.getProgress() + minSport + "");
//			break;
//		case R.id.setgoal_seekbar_weight:
//			tvWeightGoal.setText(seekBar.getProgress() + minWeight + "");
//			break;
//		case R.id.setgoal_seekbar_gaoya:
//			tvGaoYaGoal.setText(seekBar.getProgress() + minGaoYa + "");
//			break;
//		case R.id.setgoal_seekbar_xuetang:
//			tvXueTangGoal.setText(format.format(Double.valueOf(seekBar.getProgress() + minXueTang)/10) + "");
//			break;
//		case R.id.setgoal_seekbar_zfl:
//			tvZflGoal.setText(seekBar.getProgress() + minZfl + "");
//			break;
//		default:
//			break;
//		}
//	}
//
//	@Override
//	public void onStartTrackingTouch(SeekBar seekBar) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onStopTrackingTouch(SeekBar seekBar) {
//		// TODO Auto-generated method stub
//
//	}
//
//	// 请求结果
//	private void initResult() {
//		if (PF == FailServer) {
////			Constant.showDialog(this, "服务器响应超时!");
//			Toast.makeText(this, "服务器响应超时！", Toast.LENGTH_SHORT).show();
//			// 添加监听
//			barSleep.setOnSeekBarChangeListener(this);
//			barSport.setOnSeekBarChangeListener(this);
//			barWeight.setOnSeekBarChangeListener(this);
//			barGaoYa.setOnSeekBarChangeListener(this);
//			barXueTang.setOnSeekBarChangeListener(this);
//			barZfl.setOnSeekBarChangeListener(this);
//		} else if (PF == FailGet) {
//			Toast.makeText(this, "获取目标失败!", Toast.LENGTH_SHORT).show();
//			// 添加监听
//			barSleep.setOnSeekBarChangeListener(this);
//			barSport.setOnSeekBarChangeListener(this);
//			barWeight.setOnSeekBarChangeListener(this);
//			barGaoYa.setOnSeekBarChangeListener(this);
//			barXueTang.setOnSeekBarChangeListener(this);
//			barZfl.setOnSeekBarChangeListener(this);
//		} else if (PF == OKGet) {
//			initData();
//			// 添加监听
//			barSleep.setOnSeekBarChangeListener(this);
//			barSport.setOnSeekBarChangeListener(this);
//			barWeight.setOnSeekBarChangeListener(this);
//			barGaoYa.setOnSeekBarChangeListener(this);
//			barXueTang.setOnSeekBarChangeListener(this);
//			barZfl.setOnSeekBarChangeListener(this);
//		} else if (PF == FailNoData) {
//			Toast.makeText(this, "该成员没有目标数据!", Toast.LENGTH_SHORT).show();
//			// 添加监听
//			barSleep.setOnSeekBarChangeListener(this);
//			barSport.setOnSeekBarChangeListener(this);
//			barWeight.setOnSeekBarChangeListener(this);
//			barGaoYa.setOnSeekBarChangeListener(this);
//			barXueTang.setOnSeekBarChangeListener(this);
//			barZfl.setOnSeekBarChangeListener(this);
//		} else if (PF == OKCommit) {
//			Toast.makeText(this, "提交成功!", Toast.LENGTH_SHORT).show();
//		} else if (PF == FailCommit) {
//			Toast.makeText(this, "提交失败!", Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	// 网络请求获取目标
//	private class GetGoalRequest extends AsyncTask<Object, Void, String> {
//		private String url;
//
//		@Override
//		protected void onPreExecute() {
//			progressDialog = new ProgressDialog(SetGoalsActivity.this);
//			Constant.showProgressDialog(progressDialog);
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(Object... params) {
//			String result = "";
//			url = (String) params[0];
//			Log.e("getGoalUrl", url + "");
//			result = HttpManager.getStringContent(url);
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (result.toString().trim().equals("ERROR")) {
//				PF = FailServer;
//				initResult();
//			} else {
//				try {
//					JSONObject jo = new JSONObject(result);
//					String resultCode = jo.optString("resultCode");
//					if (resultCode.equals("1")) {
//						JSONObject jo2 = jo.getJSONObject("data");
//						JSONObject jo3 = jo2.getJSONObject("data");
//						if (!jo3.isNull("height")) {
//							height = jo3.getDouble("height");
//						}
//						if (!jo3.isNull("avgSteps")) {
//							currentSport = jo3.getInt("avgSteps");
//						}
//						if (!jo3.isNull("avgSleepMinutes")) {
//							currentSleep = jo3.getInt("avgSleepMinutes");
//						}
//						if (!jo3.isNull("newWeight")) {
//							currentWeight = jo3.getInt("newWeight");
//						}
//						if (!jo3.isNull("newSystolicPressure")) {
//							currentGaoYa = jo3.getInt("newSystolicPressure");
//						}
//						if (!jo3.isNull("newBloodGlucose")) {
//							currentXueTang = (int)(jo3.getDouble("newBloodGlucose")*10);
//						}
//						if (!jo3.isNull("newFatPercentage")) {
//							currentZfl = jo3.getInt("newFatPercentage");
//						}
//						if (!jo3.isNull("goalDeepMinutes")) {
//							goalSleep = jo3.getInt("goalDeepMinutes");
//						}
//						if (!jo3.isNull("goalWeight")) {
//							goalWeight = jo3.getInt("goalWeight");
//						}
//						if (!jo3.isNull("goalSteps")) {
//							goalSport = jo3.getInt("goalSteps");
//						}
//						if (!jo3.isNull("goalSystolicPressure")) {
//							goalGaoYa = jo3.getInt("goalSystolicPressure");
//						}
//						if (!jo3.isNull("goalbloodGlucose")) {
//							goalXueTang =(int) (jo3.getDouble("goalbloodGlucose")*10);
//						}
//						if (!jo3.isNull("goalFatPercentage")) {
//							goalZfl = jo3.getInt("goalFatPercentage");
//						}
//						PF = OKGet;
//						initResult();
//					} else {
//						PF = FailNoData;
//						initResult();
//					}
//				} catch (JSONException e) {
//					PF = FailGet;
//					initResult();
//					System.out.println("解析错误");
//					e.printStackTrace();
//				}
//			}
//			Constant.exitProgressDialog(progressDialog);
//			super.onPostExecute(result);
//		}
//
//		@Override
//		protected void onCancelled() {
//			// TODO Auto-generated method stub
//			super.onCancelled();
//		}
//	}
//
//	// 提交目标
//	private class CommitGoalRequest extends AsyncTask<Object, Void, String> {
//		private String url;
//
//		@Override
//		protected void onPreExecute() {
//			progressDialog = new ProgressDialog(SetGoalsActivity.this);
//			Constant.showProgressDialog(progressDialog);
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(Object... params) {
//			String result = "";
//			url = (String) params[0];
//			Log.e("commitGoalUrl", url + "");
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("familyMemberId", member.getId() + "");
//			map.put("goalDeepMinutes", goalSleepCommit);
//			map.put("goalSteps", goalSportCommit);
//			map.put("goalWeight", goalWeightCommit);
//			map.put("goalFatPercentage", goalZflCommit+ "");
//			map.put("goalSystolicPressure", goalGaoYaCommit + "");
//			map.put("goalBloodGlucose", goalXueTangCommit + "");
//			result = HttpManager.getStringContentPost(url, map);
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (result.toString().trim().equals("ERROR")) {
//				PF = FailServer;
//				initResult();
//			} else {
//				try {
//					JSONObject jo = new JSONObject(result);
//					System.err.println(result);
//					String resultCode = jo.optString("resultCode");
//					if (resultCode.equals("1")) {
//						PF = OKCommit;
//						initResult();
//					} else {
//						PF = FailCommit;
//						initResult();
//					}
//				} catch (JSONException e) {
//					PF = FailCommit;
//					initResult();
//					System.out.println("解析错误");
//					e.printStackTrace();
//				}
//			}
//			Constant.exitProgressDialog(progressDialog);
//			super.onPostExecute(result);
//		}
//
//		@Override
//		protected void onCancelled() {
//			// TODO Auto-generated method stub
//			super.onCancelled();
//		}
//	}
//}

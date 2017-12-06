package com.sinosoft.fhcs.android.activity.slidefragment;

/**
 * @CopyRight: SinoSoft.
 * @Description:设定目标页
 * @Author: wangshuangshuang.
 * @Create: 2015年6月1日.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.MainActivity;
import com.sinosoft.fhcs.android.customview.VerticalLineView;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.BMIUtil;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetGoalFragment extends Fragment implements OnClickListener,
		SeekBar.OnSeekBarChangeListener {
	private Button btnLeftMebu;// 左菜单
	private Activity mActivity;
	private View mainView;
	private TextView tvTitle;
	private Button btnSure;// 确定
	private FamilyMember member = new FamilyMember();
	// Content
	private TextView tvBMI;// 显示正常体重范围
	private SeekBar barSleep, barSport, barWeight, barGaoYa, barXueTang,
			barZfl;// 睡眠，运动，体重，高压，血糖，脂肪率
	private TextView tvSleepGoalH, tvSleepGoalM, tvSportGoal, tvWeightGoal,
			tvGaoYaGoal, tvXueTangGoal, tvZflGoal;// 目标睡眠小时，睡眠分钟，运动，体重,高压，血糖，脂肪率
	private VerticalLineView lineViewSleep, lineViewSport, lineViewWeight,
			lineViewGaoYa, lineViewXueTang, lineViewZfl;
	private int currentSleep = 480, currentSport = 10000,
			currentGaoYa = 125, currentXueTang = 50;// 当前睡眠时间（分钟），当前运动步数，当前体重
	private double currentZfl = 20;
	private double currentWeight = 0;
	private int maxSleep = 600, maxSport = 20000, maxWeight = 100,
			maxGaoYa = 160, maxXueTang = 130, maxZfl = 40;// 最大体重 血糖显示除10
	private int goalSleep = 480, goalSport = 10000, goalWeight = 50,
			goalGaoYa = 125, goalXueTang = 50, goalZfl = 20;// 目标值
	private String goalSleepCommit = "480", goalSportCommit = "10000",
			goalWeightCommit = "50", goalGaoYaCommit = "125",
			goalXueTangCommit = "50", goalZflCommit = "20";// 提交的目标值
	private int minSleep = 300, minSport = 2000, minWeight = 0, minGaoYa = 80,
			minXueTang = 30, minZfl = 10;// 最小分钟数，最小步数,最小体重
	private double height = 160;
	private int valueSport, valueSleep;// 用于判断向左滑还是向右滑
	private float thumbWidth = 40;
	private float seedBarlineWidth_T = 0, seedBarlineWidth_S = 0,
			seedBarlineWidth_W = 0, seedBarlineWidth_G = 0,
			seedBarlineWidth_X = 0, seedBarlineWidth_Z = 0;
	private float drawLineHeight = 20;
	private float seekBarMarginTop = 10;
	private float seekBarY_T = 0, seekBarY_S = 0, seekBarY_W = 0,
			seekBarY_G = 0, seekBarY_X = 0, seekBarY_Z = 0;
	// 需要绘制的坐标
	private float currentLineX_T = 0, currentLineX_S = 0, currentLineX_W = 0,
			currentLineX_G = 0, currentLineX_X = 0, currentLineX_Z = 0;
	private float currentLineStartY_T = 0, currentLineStartY_S = 0,
			currentLineStartY_W = 0, currentLineStartY_G = 0,
			currentLineStartY_X = 0, currentLineStartY_Z = 0;
	private float currentLineEndY_T = 0, currentLineEndY_S = 0,
			currentLineEndY_W = 0, currentLineEndY_G = 0,
			currentLineEndY_X = 0, currentLineEndY_Z = 0;
	private float currentTextStartX_T, currentTextStartX_S,
			currentTextStartX_W, currentTextStartX_G, currentTextStartX_X,
			currentTextStartX_Z;
	private float currentTextStartY_T, currentTextStartY_S,
			currentTextStartY_W, currentTextStartY_G, currentTextStartY_X,
			currentTextStartY_Z;
	// 自定义view 宽度
	private int viewWidth_T, viewWidth_S, viewWidth_W, viewWidth_G,
			viewWidth_X, viewWidth_Z;
	// 自定义view 宽度和progress余差一般
	private float halfWidth_T, halfWidth_S, halfWidth_W, halfWidth_G,
			halfWidth_X, halfWidth_Z;
	private int textSize = 20;
	/**
	 * 家庭成员
	 */
	private List<Object> getFamilyMemberList = new ArrayList<Object>();
	private Button btnLeftF, btnRightF;// 家庭成员左右选择
	private ViewFlipper flipperFamilymember;
	private int mCurrPos_F = 0;// 家庭成员当前
	/**
	 * 网络请求
	 */
	private String userId = "";// 用户id
	private ProgressDialog progressDialog;// 进度条
	private static final int OKGet = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int FailGet = 1003;// 失败
	private static final int FailNoData = 1004;// 没有数据
	private static final int OKCommit = 1005;// 成功
	private static final int FailCommit = 1006;// 失败
	private int PF = 1000;
	private ProgressDialog progressDialogF;// 进度条
	private int PFFamily = 2000;
	private static final int FailServerFamily = 2001;// 连接超时
	private static final int OKFamily = 2002;// 家庭成员成功
	private static final int FailFamily = 2003;// 家庭成员失败
	private static final int NoDataFamily = 2004;// 家庭成员没有数据
	DecimalFormat format = new DecimalFormat(("#####0.0"));

	private int flagSleep=1,flagSport=1,flagWeight = 1,flagGaoYa=1,flagXueTang=1,flagZfl=1;
	// format.format(Double.valueOf(xueTangShow)/10) + ""
	public SetGoalFragment(MainActivity mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// inflater the layout
		mainView = inflater.inflate(R.layout.activity_setgoals, null);
		// 从首选项获取机顶盒编号
		SharedPreferences prefs = getActivity().getSharedPreferences(
				"UserInfo", Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();// 初始化控件
		initData();// 初始化数据
		initRequest();
		return mainView;
	}

	private void initStartData() {
		currentSleep = 480;
		currentSport = 10000;
		currentGaoYa = 125;
		currentXueTang = 50;
		currentZfl = 20;// 当前睡眠时间（分钟），当前运动步数，当前体重
		goalSleep = 480;
		goalSport = 10000;
		goalGaoYa = 125;
		goalXueTang = 50;
		goalZfl = 20;// 目标值
		valueSport = 0;
		valueSleep = 0;// 用于判断向左滑还是向右滑
	}

	// 请求数据
	private void initRequest() {
		if (!HttpManager.isNetworkAvailable(mActivity)) {
			// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(mActivity, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		GetFamilyListRequest re2 = new GetFamilyListRequest();
		re2.execute(HttpManager.urlFamilyList(userId));

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("设定目标页"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("设定目标页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
	}

	private void init() {
		tvTitle = (TextView) mainView.findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_setgoals));
		btnLeftMebu = (Button) mainView
				.findViewById(R.id.titlebar_btn_memuleft);
		btnLeftMebu.setVisibility(View.VISIBLE);
		btnLeftMebu.setOnClickListener(this);

		barSleep = (SeekBar) mainView.findViewById(R.id.setgoal_seekbar_sleep);
		barSport = (SeekBar) mainView.findViewById(R.id.setgoal_seekbar_sport);
		barWeight = (SeekBar) mainView
				.findViewById(R.id.setgoal_seekbar_weight);
		barGaoYa = (SeekBar) mainView.findViewById(R.id.setgoal_seekbar_gaoya);
		barXueTang = (SeekBar) mainView
				.findViewById(R.id.setgoal_seekbar_xuetang);
		barZfl = (SeekBar) mainView.findViewById(R.id.setgoal_seekbar_zfl);

		tvWeightGoal = (TextView) mainView
				.findViewById(R.id.setgoals_tv_weight);
		tvSportGoal = (TextView) mainView.findViewById(R.id.setgoals_tv_sport);
		tvSleepGoalH = (TextView) mainView.findViewById(R.id.setgoals_tv_Hour);
		tvSleepGoalM = (TextView) mainView
				.findViewById(R.id.setgoals_tv_Minute);
		tvGaoYaGoal = (TextView) mainView.findViewById(R.id.setgoals_tv_gaoya);
		tvXueTangGoal = (TextView) mainView
				.findViewById(R.id.setgoals_tv_xuetang);
		tvZflGoal = (TextView) mainView.findViewById(R.id.setgoals_tv_zfl);

		lineViewSleep = (VerticalLineView) mainView
				.findViewById(R.id.setgoal_seekbar_lineview_sleep);
		lineViewSport = (VerticalLineView) mainView
				.findViewById(R.id.setgoal_seekbar_lineview_sport);
		lineViewWeight = (VerticalLineView) mainView
				.findViewById(R.id.setgoal_seekbar_lineview_weight);
		lineViewGaoYa = (VerticalLineView) mainView
				.findViewById(R.id.setgoal_seekbar_lineview_gaoya);
		lineViewXueTang = (VerticalLineView) mainView
				.findViewById(R.id.setgoal_seekbar_lineview_xuetang);
		lineViewZfl = (VerticalLineView) mainView
				.findViewById(R.id.setgoal_seekbar_lineview_zfl);

		tvBMI = (TextView) mainView.findViewById(R.id.setgoal_seekbar_bmi);
		btnSure = (Button) mainView.findViewById(R.id.setgoal_seekbar_btn_sure);
		btnSure.setOnClickListener(this);

		flipperFamilymember = (ViewFlipper) mainView
				.findViewById(R.id.setgoals_flipper_familymember);
		btnLeftF = (Button) mainView.findViewById(R.id.setgoals_btn_leftF);
		btnRightF = (Button) mainView.findViewById(R.id.setgoals_btn_rightF);
		btnLeftF.setOnClickListener(this);
		btnRightF.setOnClickListener(this);
	}

	// 家庭成员左
	private void movePreviousF() {
		setViewFamily(mCurrPos_F, mCurrPos_F - 1, true);
		flipperFamilymember.setInAnimation(mActivity, R.anim.push_left_in);
		flipperFamilymember.setOutAnimation(mActivity, R.anim.push_left_out);
		flipperFamilymember.showPrevious();
	}

	// 家庭成员右
	private void moveNextF() {
		setViewFamily(mCurrPos_F, mCurrPos_F + 1, true);
		flipperFamilymember.setInAnimation(mActivity, R.anim.push_right_in);
		flipperFamilymember.setOutAnimation(mActivity, R.anim.push_right_out);
		flipperFamilymember.showNext();
	}

	// 家庭成员View
	private void setViewFamily(int curr, int next, boolean flag) {
		View v = (View) LayoutInflater.from(mActivity).inflate(
				R.layout.equipmentflipper_item, null);
		ImageView iv = (ImageView) v.findViewById(R.id.eflipper_img);
		TextView tv = (TextView) v.findViewById(R.id.eflipper_tv);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		if (flag) {
			if (curr < next && next > getFamilyMemberList.size() - 1)
				next = 0;
			else if (curr > next && next < 0)
				next = getFamilyMemberList.size() - 1;
			iv.setImageResource(Constant.ImageId(
					((FamilyMember) getFamilyMemberList.get(next))
							.getFamilyRoleName(),
					((FamilyMember) getFamilyMemberList.get(next)).getGender()));
			tv.setText(((FamilyMember) getFamilyMemberList.get(next))
					.getFamilyRoleName());
			if (flipperFamilymember.getChildCount() > 1) {
				flipperFamilymember.removeViewAt(0);
			}
			member = (FamilyMember) getFamilyMemberList.get(next);
			height = member.getHeight();
			// 正常的BMI 18.5-23.9 体质指数（BMI）=体重（kg）÷身高^2（m）
			String[] temp = null;
			int age=member.getAge();
			if (age <= 7) {
				age = 7;
			}
			if (age >= 18) {
				age = 18;
			}
			if(member.getGender().equals("0")){
				temp=BMIUtil.FEMALE_NORMAL_STANDARD.get(age).split("#");
			}else{
				temp=BMIUtil.MALE_NORMAL_STANDARD.get(age).split("#");
			}
			double minBmi=Double.parseDouble(temp[0]);
			double maxBmi=Double.parseDouble(temp[1]);
			double biaozhun=minBmi+Constant.divideF((float)(maxBmi-minBmi),2);
			int weightMin = (int) (minBmi * Constant.divideF((float) height, 100) * Constant
					.divideF((float) height, 100));
			int weightMax = (int) (maxBmi * Constant.divideF((float) height, 100) * Constant
					.divideF((float) height, 100));
			tvBMI.setText(Html
					.fromHtml("根据世界卫生组织的BMI公式，您的体重应该介于<font color=\"#faa701\"><big>"
							+ weightMin
							+ "</big></font>公斤到<font color=\"#faa701\"><big>"
							+ weightMax + "</big></font>公斤之间。"));
			goalWeight = (int) (biaozhun * Constant.divideF((float) height, 100) * Constant
					.divideF((float) height, 100));
			currentWeight = goalWeight;
			if (PFFamily == OKFamily) {
				initStartData();
				GetGoalRequest re = new GetGoalRequest();
				re.execute(HttpManager.urlGetGoalInfo(member.getId() + ""));
			}
		} else {
			iv.setBackgroundColor(Color.BLUE);
			tv.setText("暂无");
		}
		flipperFamilymember.addView(v, flipperFamilymember.getChildCount());
		mCurrPos_F = next;

	}
	private void initData() {
		// 设置向左滑右滑
		valueSport = getSportProgress(goalSport + minSport);
		valueSleep = getSleepProgress(goalSleep + minSleep);

		// 设置最大值
		barSleep.setMax(maxSleep - minSleep);
		barSport.setMax(maxSport - minSport);
		barWeight.setMax(maxWeight - minWeight);
		barGaoYa.setMax(maxGaoYa - minGaoYa);
		barXueTang.setMax(maxXueTang - minXueTang);
		barZfl.setMax(maxZfl - minZfl);
		// 设置目标值
		barWeight.setProgress(goalWeight - minWeight);
		barGaoYa.setProgress(goalGaoYa - minGaoYa);
		barXueTang.setProgress(goalXueTang - minXueTang);
		barZfl.setProgress(goalZfl - minZfl);

		barSleep.setProgress(goalSleep - minSleep);
		barSport.setProgress(goalSport - minSport);

		// 设置目标显示
		tvSportGoal.setText(barSport.getProgress() + minSport + "");
		int temp = barSleep.getProgress() + minSleep;
		int H = temp / 60;
		temp = temp % 60;
		int M = temp;
		tvSleepGoalH.setText(H + "");
		tvSleepGoalM.setText(M + "");

		tvGaoYaGoal.setText(barGaoYa.getProgress() + minGaoYa + "");
		tvXueTangGoal.setText(format.format(Double.valueOf(barXueTang
				.getProgress() + minXueTang) / 10)
				+ "");
		tvZflGoal.setText(barZfl.getProgress() + minZfl + "");
		tvWeightGoal.setText(barWeight.getProgress() + minWeight + "");
		// 画当前值
		lineViewSleep.post(new Runnable() {

			@Override
			public void run() {
				// 自定义view宽度要大于progress长度，显示全文字
				lineViewSleep.invalidate();
				viewWidth_T = lineViewSleep.getWidth();
			}
		});
		lineViewSport.post(new Runnable() {

			@Override
			public void run() {
				lineViewSport.invalidate();
				viewWidth_S = lineViewSport.getWidth();
			}
		});
		lineViewWeight.post(new Runnable() {

			@Override
			public void run() {
				lineViewWeight.invalidate();
				viewWidth_W = lineViewWeight.getWidth();
			}
		});
		lineViewGaoYa.post(new Runnable() {

			@Override
			public void run() {
				lineViewGaoYa.invalidate();
				viewWidth_G = lineViewGaoYa.getWidth();
			}
		});
		lineViewXueTang.post(new Runnable() {

			@Override
			public void run() {
				lineViewXueTang.invalidate();
				viewWidth_X = lineViewXueTang.getWidth();
			}
		});
		lineViewZfl.post(new Runnable() {

			@Override
			public void run() {
				lineViewZfl.invalidate();
				viewWidth_Z = lineViewZfl.getWidth();
			}
		});
		barSleep.post(new Runnable() {

			@Override
			public void run() {
				// 获取view大小需要放到此回调接口，接口在view绘制完成执行
				seedBarlineWidth_T = barSleep.getWidth() - thumbWidth;// 计算出seekbar的实际长条长度
				seekBarY_T = barSleep.getY();// 计算出长条纵坐标
				// 减去progress余差
				halfWidth_T = Constant.divideF(
						viewWidth_T - barSleep.getWidth(), 2);
				int flag=0;
				if(currentSleep==minSleep){
					flag=0;
				}else{
					flag=1;
				}
				currentLineSleepPosition(currentSleep - minSleep, maxSleep
						- minSleep, seedBarlineWidth_T,flag);
				String ff="";
				if(flagSleep==0){
					ff="推荐";
				}else{
					ff="平均";
				}
				int temp = currentSleep;
				int H = temp / 60;
				temp = temp % 60;
				int M = temp;
				String temp2 = "";
				if (M == 0) {
					temp2 = H + "小时";
				} else {
					temp2 = H + "小时" + M + "分";
				}
				lineViewSleep.setLineAndPoint(
						getResources().getColor(R.color.text_line_black),
						textSize, currentLineX_T + halfWidth_T,
						currentLineStartY_T, currentLineX_T + halfWidth_T,
						currentLineEndY_T, currentTextStartX_T + halfWidth_T,
						currentTextStartY_T, ff+temp2);
			}
		});
		// 运动
		barSport.post(new Runnable() {

			@Override
			public void run() {
				seedBarlineWidth_S = barSport.getWidth() - thumbWidth;
				seekBarY_S = barSport.getY();
				halfWidth_S = Constant.divideF(
						viewWidth_S - barSport.getWidth(), 2);
				int flag=0;
				if(currentSport==minSport){
					flag=0;
				}else{
					flag=1;
				}
				currentLineSportPosition(currentSport - minSport, maxSport
						- minSport, seedBarlineWidth_S,flag);
				String ff="";
				if(flagSport==0){
					ff="推荐";
				}else{
					ff="平均";
				}
				lineViewSport.setLineAndPoint(
						getResources().getColor(R.color.text_line_black),
						textSize, currentLineX_S + halfWidth_S,
						currentLineStartY_S, currentLineX_S + halfWidth_S,
						currentLineEndY_S, currentTextStartX_S + halfWidth_S,
						currentTextStartY_S, ff+(int) currentSport + "");
			}
		});
		// 体重
		barWeight.post(new Runnable() {

			@Override
			public void run() {
				seedBarlineWidth_W = barWeight.getWidth() - thumbWidth;
				seekBarY_W = barWeight.getY();
				halfWidth_W = Constant.divideF(
						viewWidth_W - barWeight.getWidth(), 2);
				currentLineWeightPosition((float)currentWeight - minWeight, maxWeight
						- minWeight, seedBarlineWidth_W);
				String ff="";
				if(flagWeight==0){
					ff="推荐";
				}else{
					ff="最近";
				}
				lineViewWeight.setLineAndPoint(
						getResources().getColor(R.color.text_line_black),
						textSize, currentLineX_W + halfWidth_W,
						currentLineStartY_W, currentLineX_W + halfWidth_W,
						currentLineEndY_W, currentTextStartX_W + halfWidth_W,
						currentTextStartY_W, ff+ currentWeight + "");
			}
		});
		// 高压
		barGaoYa.post(new Runnable() {

			@Override
			public void run() {
				seedBarlineWidth_G = barGaoYa.getWidth() - thumbWidth;
				seekBarY_G = barGaoYa.getY();
				halfWidth_G = Constant.divideF(
						viewWidth_G - barGaoYa.getWidth(), 2);
				currentLineGaoYaPosition(currentGaoYa - minGaoYa, maxGaoYa
						- minGaoYa, seedBarlineWidth_G);
				String ff="";
				if(flagGaoYa==0){
					ff="推荐";
				}else{
					ff="最近";
				}
				lineViewGaoYa.setLineAndPoint(
						getResources().getColor(R.color.text_line_black),
						textSize, currentLineX_G + halfWidth_G,
						currentLineStartY_G, currentLineX_G + halfWidth_G,
						currentLineEndY_G, currentTextStartX_G + halfWidth_G,
						currentTextStartY_G, ff+(int) currentGaoYa + "");
			}
		});
		// 血糖
		barXueTang.post(new Runnable() {

			@Override
			public void run() {
				seedBarlineWidth_X = barXueTang.getWidth() - thumbWidth;
				seekBarY_X = barXueTang.getY();
				halfWidth_X = Constant.divideF(
						viewWidth_X - barXueTang.getWidth(), 2);
				currentLineXueTangPosition(currentXueTang - minXueTang,
						maxXueTang - minXueTang, seedBarlineWidth_X);
				String ff="";
				if(flagXueTang==0){
					ff="推荐";
				}else{
					ff="最近";
				}
				lineViewXueTang.setLineAndPoint(
						getResources().getColor(R.color.text_line_black),
						textSize, currentLineX_X + halfWidth_X,
						currentLineStartY_X, currentLineX_X + halfWidth_X,
						currentLineEndY_X, currentTextStartX_X + halfWidth_X,
						currentTextStartY_X,
						ff+format.format(Double.valueOf(currentXueTang) / 10)
								+ "");
			}
		});
		// 脂肪率
		barZfl.post(new Runnable() {

			@Override
			public void run() {
				seedBarlineWidth_Z = barZfl.getWidth() - thumbWidth;
				seekBarY_Z = barZfl.getY();
				halfWidth_Z = Constant.divideF(viewWidth_Z - barZfl.getWidth(),
						2);
				currentLineZflPosition((float)currentZfl - minZfl, maxZfl - minZfl,
						seedBarlineWidth_Z);
				String ff="";
				if(flagZfl==0){
					ff="推荐";
				}else{
					ff="最近";
				}
				lineViewZfl.setLineAndPoint(
						getResources().getColor(R.color.text_line_black),
						textSize, currentLineX_Z + halfWidth_Z,
						currentLineStartY_Z, currentLineX_Z + halfWidth_Z,
						currentLineEndY_Z, currentTextStartX_Z + halfWidth_Z,
						currentTextStartY_Z, ff+ currentZfl + "");
			}
		});
	}

	// current坐标睡眠
	public void currentLineSleepPosition(float currentValue,
										 float seekBarlineWidthMax, float seekbarWidth,int flag) { // 当前值和最大值比例系数
		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
		// -2 px 误差
		currentLineX_T = currentValue * biliX
				+ (Constant.divideF(thumbWidth, 2) - 0);
		currentLineStartY_T = seekBarY_T - seekBarMarginTop - drawLineHeight;
		currentLineEndY_T = seekBarY_T - seekBarMarginTop;
		// 获取文字传入点坐标
		if(flag==0){
			currentTextStartX_T = currentLineX_T - 40;
		}else{
			currentTextStartX_T = currentLineX_T - 55;
		}
		currentTextStartY_T = currentLineStartY_T - 10;
	}

	// 运动
	public void currentLineSportPosition(float currentValue,
										 float seekBarlineWidthMax, float seekbarWidth,int flag) {
		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
		currentLineX_S = currentValue * biliX
				+ (Constant.divideF(thumbWidth, 2) - 2);
		currentLineStartY_S = seekBarY_S - seekBarMarginTop - drawLineHeight;
		currentLineEndY_S = seekBarY_S - seekBarMarginTop;
		if(flag==0){
			currentTextStartX_S = currentLineX_S - 40;
		}else{
			currentTextStartX_S = currentLineX_S - 50;
		}
		currentTextStartY_S = currentLineStartY_S - 10;
	}

	// 体重
	public void currentLineWeightPosition(float currentValue,
										  float seekBarlineWidthMax, float seekbarWidth) {
		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
		currentLineX_W = currentValue * biliX
				+ (Constant.divideF(thumbWidth, 2) - 2);
		currentLineStartY_W = seekBarY_W - seekBarMarginTop - drawLineHeight;
		currentLineEndY_W = seekBarY_W - seekBarMarginTop;
		currentTextStartX_W = currentLineX_W - 30;
		currentTextStartY_W = currentLineStartY_W - 10;
	}

	// 高压
	public void currentLineGaoYaPosition(float currentValue,
										 float seekBarlineWidthMax, float seekbarWidth) {
		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
		currentLineX_G = currentValue * biliX
				+ (Constant.divideF(thumbWidth, 2) - 2);
		currentLineStartY_G = seekBarY_G - seekBarMarginTop - drawLineHeight;
		currentLineEndY_G = seekBarY_G - seekBarMarginTop;
		currentTextStartX_G = currentLineX_G - 30;
		currentTextStartY_G = currentLineStartY_G - 10;
	}

	// 血糖
	public void currentLineXueTangPosition(float currentValue,
										   float seekBarlineWidthMax, float seekbarWidth) {
		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
		currentLineX_X = currentValue * biliX
				+ (Constant.divideF(thumbWidth, 2) - 2);
		currentLineStartY_X = seekBarY_X - seekBarMarginTop - drawLineHeight;
		currentLineEndY_X = seekBarY_X - seekBarMarginTop;
		currentTextStartX_X = currentLineX_X - 35;
		currentTextStartY_X = currentLineStartY_X - 10;
	}

	// 脂肪率
	public void currentLineZflPosition(float currentValue,
									   float seekBarlineWidthMax, float seekbarWidth) {
		float biliX = Constant.divideF(seekbarWidth, seekBarlineWidthMax);
		currentLineX_Z = currentValue * biliX
				+ (Constant.divideF(thumbWidth, 2) - 2);
		currentLineStartY_Z = seekBarY_Z - seekBarMarginTop - drawLineHeight;
		currentLineEndY_Z = seekBarY_Z - seekBarMarginTop;
		currentTextStartX_Z = currentLineX_Z - 30;
		currentTextStartY_Z = currentLineStartY_Z - 10;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.setgoals_btn_leftF:
				// 家庭成员左
				if (getFamilyMemberList.size() != 0) {
					movePreviousF();
				}
				break;
			case R.id.setgoals_btn_rightF:
				// 家庭成员右
				if (getFamilyMemberList.size() != 0) {
					moveNextF();
				}
				break;
			case R.id.titlebar_btn_memuleft:
				// 菜单
				SlidingMenu sm = ((SlidingActivity) mActivity).getSlidingMenu();
				sm.showMenu();
				break;
			case R.id.setgoal_seekbar_btn_sure:
				// 确定
				int tempH = 0;
				int tempM = 0;
				if (!tvSleepGoalH.getText().toString().trim().equals("")) {
					tempH = Integer.valueOf(tvSleepGoalH.getText().toString()
							.trim());
				}
				if (!tvSleepGoalM.getText().toString().trim().equals("")) {
					tempM = Integer.valueOf(tvSleepGoalM.getText().toString()
							.trim());
				}
				goalSleepCommit = (tempH * 60 + tempM) + "";
				goalSportCommit = tvSportGoal.getText().toString().trim();
				goalWeightCommit = tvWeightGoal.getText().toString();
				goalGaoYaCommit = tvGaoYaGoal.getText().toString();
				goalXueTangCommit = tvXueTangGoal.getText().toString();
				goalZflCommit = tvZflGoal.getText().toString();
				System.err.println("goalSleepCommit=" + goalSleepCommit);
				System.err.println("goalSportCommit=" + goalSportCommit);
				System.err.println("goalWeightCommit=" + goalWeightCommit);
				System.err.println("goalGaoYaCommit=" + goalGaoYaCommit);
				System.err.println("goalXueTangCommit=" + goalXueTangCommit);
				System.err.println("goalZflCommit=" + goalZflCommit);
				if (!HttpManager.isNetworkAvailable(mActivity)) {
					// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(mActivity, "您的网络没连接好，请检查后重试！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				CommitGoalRequest re = new CommitGoalRequest();
				re.execute(HttpManager.urlCommitGoalInfo());
				break;
			default:
				break;
		}

	}

	// 获取睡眠刻度值（30分钟一个单位，最小值为5个小时==300分钟==10个单位）
	private int getSleepProgress(int progress) {
		int p = progress / 30;
		return p;
	}

	// 获取运动刻度值（500步一个单位，最小值为2000步==4个单位）
	private int getSportProgress(int progress) {
		int p = progress / 500;
		return p;
	}

	// 设置显示运动数据
	private int showSportData(int progress) {
		int p = progress * 500;
		return p;
	}

	// 设置显示睡眠数据
	private int showSleepData(int progress) {
		int p = progress * 30;
		return p;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
								  boolean fromUser) {
		switch (seekBar.getId()) {
			case R.id.setgoal_seekbar_sleep:
				if (fromUser) {
					int tempSleep = getSleepProgress(seekBar.getProgress()
							+ minSleep);
					// System.err.println("tempSleep="+tempSleep);
					// System.err.println("valueSleep="+valueSleep);
					if (valueSleep < tempSleep) {
						valueSleep++;
					} else if (valueSleep > tempSleep) {
						valueSleep--;
					}
					// Log.e("valueSleep", valueSleep + "");
					// Log.e("showSleepData(valueSleep)", showSleepData(valueSleep)
					// +
					// "");
					barSleep.setProgress(showSleepData(valueSleep) - minSleep);
					int temp2 = showSleepData(valueSleep);
					if (temp2 >= maxSleep) {
						temp2 = maxSleep;
					} else if (temp2 <= minSleep) {
						temp2 = minSleep;
					}
					int temp = temp2;
					// int temp = seekBar.getProgress() + minSleep;
					int H = temp / 60;
					temp = temp % 60;
					int M = temp;
					tvSleepGoalH.setText(H + "");
					tvSleepGoalM.setText(M + "");
				}

				break;
			case R.id.setgoal_seekbar_sport:
				if (fromUser) {
					int tempSport = getSportProgress(seekBar.getProgress()
							+ minSport);
					// System.err.println("tempSport="+tempSport);
					// System.err.println("valueSport="+valueSport);
					if (valueSport < tempSport) {

						valueSport++;
					} else if (valueSport > tempSport) {
						valueSport--;
					}
					// Log.e("valueSport", valueSport + "");
					// Log.e("showSportData(valueSport)", showSportData(valueSport)
					// +
					// "");
					barSport.setProgress(showSportData(valueSport) - minSport);
					int temp3 = showSportData(valueSport);
					if (temp3 >= maxSport) {
						temp3 = maxSport;
					} else if (temp3 <= minSport) {
						temp3 = minSport;
					}
					tvSportGoal.setText(temp3 + "");
					// tvSportGoal.setText(seekBar.getProgress() + minSport + "");
				}
				break;
			case R.id.setgoal_seekbar_weight:
				if (fromUser) {
					tvWeightGoal.setText(seekBar.getProgress() + minWeight + "");
				}
				break;
			case R.id.setgoal_seekbar_gaoya:
				if (fromUser) {
					tvGaoYaGoal.setText(seekBar.getProgress() + minGaoYa + "");
				}
				break;
			case R.id.setgoal_seekbar_xuetang:
				if (fromUser) {
					tvXueTangGoal.setText(format.format(Double.valueOf(seekBar
							.getProgress() + minXueTang) / 10)
							+ "");
				}
				break;
			case R.id.setgoal_seekbar_zfl:
				if (fromUser) {
					tvZflGoal.setText(seekBar.getProgress() + minZfl + "");
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	private void initResult2() {
		mCurrPos_F = 0;
		if (PFFamily == FailServerFamily) {
			setViewFamily(mCurrPos_F, 0, false);
			Toast.makeText(mActivity, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PFFamily == FailFamily) {
			setViewFamily(mCurrPos_F, 0, false);
			Toast.makeText(mActivity, "获取家庭成员数据失败!", Toast.LENGTH_SHORT).show();
		} else if (PFFamily == NoDataFamily) {
			setViewFamily(mCurrPos_F, 0, false);
			Toast.makeText(mActivity, "目前还没有家庭成员，请添加!", Toast.LENGTH_SHORT)
					.show();
		} else if (PFFamily == OKFamily) {
			setViewFamily(mCurrPos_F, 0, true);
		}

	}

	// 请求结果
	private void initResult() {
		if (PF == FailServer) {
			flagSleep=0;flagSport=0;flagWeight = 0;flagGaoYa=0;flagXueTang=0;flagZfl=0;
			initData();
			// Constant.showDialog(this, "服务器响应超时!");
			// 添加监听
			barSleep.setOnSeekBarChangeListener(this);
			barSport.setOnSeekBarChangeListener(this);
			barWeight.setOnSeekBarChangeListener(this);
			barGaoYa.setOnSeekBarChangeListener(this);
			barXueTang.setOnSeekBarChangeListener(this);
			barZfl.setOnSeekBarChangeListener(this);
			Toast.makeText(mActivity, "服务器响应超时！", Toast.LENGTH_SHORT).show();
		} else if (PF == FailGet) {
			flagSleep=0;flagSport=0;flagWeight = 0;flagGaoYa=0;flagXueTang=0;flagZfl=0;
			initData();
			// 添加监听
			barSleep.setOnSeekBarChangeListener(this);
			barSport.setOnSeekBarChangeListener(this);
			barWeight.setOnSeekBarChangeListener(this);
			barGaoYa.setOnSeekBarChangeListener(this);
			barXueTang.setOnSeekBarChangeListener(this);
			barZfl.setOnSeekBarChangeListener(this);
			Toast.makeText(mActivity, "获取目标失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKGet) {
			initData();
			// 添加监听
			barSleep.setOnSeekBarChangeListener(this);
			barSport.setOnSeekBarChangeListener(this);
			barWeight.setOnSeekBarChangeListener(this);
			barGaoYa.setOnSeekBarChangeListener(this);
			barXueTang.setOnSeekBarChangeListener(this);
			barZfl.setOnSeekBarChangeListener(this);
		} else if (PF == FailNoData) {
			initData();
			// 添加监听
			barSleep.setOnSeekBarChangeListener(this);
			barSport.setOnSeekBarChangeListener(this);
			barWeight.setOnSeekBarChangeListener(this);
			barGaoYa.setOnSeekBarChangeListener(this);
			barXueTang.setOnSeekBarChangeListener(this);
			barZfl.setOnSeekBarChangeListener(this);
//			Toast.makeText(mActivity, "该成员没有目标数据!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKCommit) {
			Toast.makeText(mActivity, "提交成功!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailCommit) {
			Toast.makeText(mActivity, "提交失败!", Toast.LENGTH_SHORT).show();
		}
	}

	// 网络请求获取目标
	private class GetGoalRequest extends AsyncTask<Object, Void, String> {
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
			Log.e("getGoalUrl", url + "");
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
						JSONObject jo3 = jo2.getJSONObject("data");
						if (!jo3.isNull("height")) {
							height = jo3.getDouble("height");
						}
						if (!jo3.isNull("avgSteps")) {
							currentSport = jo3.getInt("avgSteps");
							if(currentSport==0){
								flagSport=0;
							}else{
								flagSport=1;
							}
						}else{
							flagSport=0;
						}
						if (!jo3.isNull("avgSleepMinutes")) {
							currentSleep = jo3.getInt("avgSleepMinutes");
							if(currentSleep==0){
								flagSleep=0;
							}else{
								flagSleep=1;
							}
						}else{
							flagSleep=0;
						}
						if (!jo3.isNull("newWeight")) {
							currentWeight = jo3.getDouble("newWeight");
							if(currentWeight==0){
								flagWeight=0;
							}else{
								flagWeight=1;
							}
						}else{
							flagWeight=0;
						}
						if (!jo3.isNull("newSystolicPressure")) {
							currentGaoYa = jo3.getInt("newSystolicPressure");
							if(currentGaoYa==0){
								flagGaoYa=0;
							}else{
								flagGaoYa=1;
							}
						}else{
							flagGaoYa=0;
						}
						if (!jo3.isNull("newBloodGlucose")) {
							currentXueTang = (int) (jo3
									.getDouble("newBloodGlucose") * 10);
							if(currentXueTang==0){
								flagXueTang=0;
							}else{
								flagXueTang=1;
							}
						}else{
							flagXueTang=0;
						}
						if (!jo3.isNull("newFatPercentage")) {
							currentZfl = jo3.getDouble("newFatPercentage");
							if(currentZfl==0){
								flagZfl=0;
							}else{
								flagZfl=1;
							}
						}else{
							flagZfl=0;
						}
						if (!jo3.isNull("goalDeepMinutes")&&jo3.getInt("goalDeepMinutes")!=0) {
							goalSleep = jo3.getInt("goalDeepMinutes");
						}
						if (!jo3.isNull("goalWeight")&&jo3.getInt("goalWeight")!=0) {
							goalWeight = jo3.getInt("goalWeight");
						}
						if (!jo3.isNull("goalSteps")&&jo3.getInt("goalSteps")!=0) {
							goalSport = jo3.getInt("goalSteps");
						}
						if (!jo3.isNull("goalSystolicPressure")&&jo3.getInt("goalSystolicPressure")!=0) {
							goalGaoYa = jo3.getInt("goalSystolicPressure");
						}
						if (!jo3.isNull("goalbloodGlucose")&&(int) (jo3
								.getDouble("goalbloodGlucose") * 10)!=0) {
							goalXueTang = (int) (jo3
									.getDouble("goalbloodGlucose") * 10);
						}
						if (!jo3.isNull("goalFatPercentage")&&jo3.getInt("goalFatPercentage")!=0) {
							goalZfl = jo3.getInt("goalFatPercentage");
						}
						PF = OKGet;
						initResult();
					} else {
						JSONObject jo2 = jo.optJSONObject("data");
						JSONObject jo3 = jo2.optJSONObject("data");
						if (!jo3.isNull("height")) {
							height = jo3.getDouble("height");
						}
						if (!jo3.isNull("avgSteps")) {
							currentSport = jo3.getInt("avgSteps");
							if(currentSport==0){
								flagSport=0;
							}else{
								flagSport=1;
							}
						}else{
							flagSport=0;
						}
						if (!jo3.isNull("avgSleepMinutes")) {
							currentSleep = jo3.getInt("avgSleepMinutes");
							if(currentSleep==0){
								flagSleep=0;
							}else{
								flagSleep=1;
							}
						}else{
							flagSleep=0;
						}
						if (!jo3.isNull("newWeight")) {
							currentWeight = jo3.getDouble("newWeight");
							if(currentWeight==0){
								flagWeight=0;
							}else{
								flagWeight=1;
							}
						}else{
							flagWeight=0;
						}
						if (!jo3.isNull("newSystolicPressure")) {
							currentGaoYa = jo3.getInt("newSystolicPressure");
							if(currentGaoYa==0){
								flagGaoYa=0;
							}else{
								flagGaoYa=1;
							}
						}else{
							flagGaoYa=0;
						}
						if (!jo3.isNull("newBloodGlucose")) {
							currentXueTang = (int) (jo3
									.getDouble("newBloodGlucose") * 10);
							if(currentXueTang==0){
								flagXueTang=0;
							}else{
								flagXueTang=1;
							}
						}else{
							flagXueTang=0;
						}
						if (!jo3.isNull("newFatPercentage")) {
							currentZfl = jo3.getDouble("newFatPercentage");
							if(currentZfl==0){
								flagZfl=0;
							}else{
								flagZfl=1;
							}
						}else{
							flagZfl=0;
						}
						PF = FailNoData;
						initResult();
					}
				} catch (JSONException e) {
					PF = FailGet;
					initResult();
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

	// 提交目标
	private class CommitGoalRequest extends AsyncTask<Object, Void, String> {
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
			Log.e("commitGoalUrl", url + "");
			Map<String, String> map = new HashMap<String, String>();
			map.put("familyMemberId", member.getId() + "");
			map.put("goalDeepMinutes", goalSleepCommit);
			map.put("goalSteps", goalSportCommit);
			map.put("goalWeight", goalWeightCommit);
			map.put("goalFatPercentage", goalZflCommit + "");
			map.put("goalSystolicPressure", goalGaoYaCommit + "");
			map.put("goalBloodGlucose", goalXueTangCommit + "");
			result = HttpManager.getStringContentPost(url, map);
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
					System.err.println(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OKCommit;
						initResult();
					} else {
						PF = FailCommit;
						initResult();
					}
				} catch (JSONException e) {
					PF = FailCommit;
					initResult();
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

	// 网络请求家庭成员
	private class GetFamilyListRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialogF = new ProgressDialog(mActivity);
			Constant.showProgressDialog(progressDialogF);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getFamilyListUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PFFamily = FailServerFamily;
				initResult2();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						JSONObject jo2 = jo.getJSONObject("data");
						JSONArray ja = jo2.getJSONArray("FamilyMemberList");
						if (ja.length() == 0) {
							PFFamily = NoDataFamily;
							initResult2();
						} else {
							getFamilyMemberList.clear();
							getFamilyMemberList = HttpManager.getFamilyList(ja);
							PFFamily = OKFamily;
							initResult2();
						}
					} else {
						PFFamily = FailFamily;
						initResult2();
					}
				} catch (JSONException e) {
					PFFamily = FailFamily;
					initResult2();
					System.out.println("解析错误");
					e.printStackTrace();
				}
			}
			Constant.exitProgressDialog(progressDialogF);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}
}

package com.sinosoft.fhcs.android.customview;
/**
 * @CopyRight: SinoSoft.
 * @Description:自定义日期控件弹窗
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.AddCompetitionActivity;
import com.sinosoft.fhcs.android.activity.AddFamilyMemberActivity;
import com.sinosoft.fhcs.android.activity.AddRemindActivity;
import com.sinosoft.fhcs.android.activity.EditFamilyMemberActivity;
import com.sinosoft.fhcs.android.activity.EditRemindActivity;
import com.sinosoft.fhcs.android.wheel.NumericWheelAdapter;
import com.sinosoft.fhcs.android.wheel.OnWheelChangedListener;
import com.sinosoft.fhcs.android.wheel.WheelView;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@SuppressLint("ViewConstructor")
public class DateWheelPop extends PopupWindow implements
		android.view.View.OnClickListener {
	private static int START_YEAR = 1900, END_YEAR = 3100;
	private Activity mContext;
	private TextView tvTitle;// 个人信息
	private Button btnSure;
	private Button btnCancle;
	private View mMenuView;
	private WheelView wv_year, wv_month, wv_day;
	private int curYear = 0;
	private int curMonth = 0;
	private int curDay = 0;
	private String flag;// 修改，添加

	@SuppressWarnings("deprecation")
	public DateWheelPop(Activity context, int curYear, int curMonth, int curDay,
						String flag) {
		super(context);
		this.mContext = context;
		this.curYear = curYear;
		this.curMonth = curMonth - 1;
		this.curDay = curDay;
		this.flag = flag;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.wheel_date, null);
		tvTitle = (TextView) mMenuView.findViewById(R.id.wheel_date_tv_title);
		btnSure = (Button) mMenuView.findViewById(R.id.wheel_date_btn_sure);
		btnCancle = (Button) mMenuView.findViewById(R.id.wheel_date_btn_cancle);
		btnSure.setOnClickListener(this);
		btnCancle.setOnClickListener(this);
		initData();
		initWheel();
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.popuStyle);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x33333333);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				dismiss();
				return true;
			}
		});
	}

	private void initData() {
		if (flag.equals("addMember")) {
			tvTitle.setText(mContext.getString(R.string.add_tv_birth3));
		} else if (flag.equals("editMember")) {
			tvTitle.setText(mContext.getString(R.string.add_tv_birth2));
		} else if(flag.equals("addRemind")){
			tvTitle.setText(mContext.getString(R.string.remind_detail_tv_time));
		}else if(flag.equals("EditRemindStart")||flag.equals("addCompetition_btnStartDate")){
			tvTitle.setText("开始日期");
		}else if(flag.equals("EditRemindEnd")||flag.equals("addCompetition_btnEndDate")){
			tvTitle.setText("结束日期");
		}else{
			tvTitle.setText("请选择");
		}

	}

	private void initWheel() {
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		// 年
		wv_year = (WheelView) mMenuView.findViewById(R.id.wheel_date_view_year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(curYear - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) mMenuView
				.findViewById(R.id.wheel_date_view_month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(curMonth);

		// 日
		wv_day = (WheelView) mMenuView.findViewById(R.id.wheel_date_view_day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(curMonth + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(curMonth + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((curYear % 4 == 0 && curYear % 100 != 0) || curYear % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setLabel("日");
		wv_day.setCurrentItem(curDay - 1);

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};

		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小
		int textSize = 0;
		Display mDisplay = mContext.getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int W = mDisplay.getWidth();
		if(W>=1080){
			textSize = 60;
		}else if(W<1080&&W>=720){
			textSize = 40;
		}else if(W<=320){
			textSize = 20;
		}else{
			textSize = 30;
		}

		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

	}

	private void updateStatus() {
		String parten = "00";
		DecimalFormat decimal = new DecimalFormat(parten);
		// 设置日期的显示
		if (flag.equals("addMember")) {
			AddFamilyMemberActivity.tvBirth
					.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
							+ decimal.format((wv_month.getCurrentItem() + 1))
							+ "-"
							+ decimal.format((wv_day.getCurrentItem() + 1)));
		} else if (flag.equals("editMember")) {
			EditFamilyMemberActivity.tvBirth
					.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
							+ decimal.format((wv_month.getCurrentItem() + 1))
							+ "-"
							+ decimal.format((wv_day.getCurrentItem() + 1)));
		}else if(flag.equals("addRemind")){
			AddRemindActivity.tvStartDate
					.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
							+ decimal.format((wv_month.getCurrentItem() + 1))
							+ "-"
							+ decimal.format((wv_day.getCurrentItem() + 1)));
		}else if(flag.equals("EditRemindStart")){
			EditRemindActivity.tvStartDate
					.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
							+ decimal.format((wv_month.getCurrentItem() + 1))
							+ "-"
							+ decimal.format((wv_day.getCurrentItem() + 1)));
		}else if(flag.equals("EditRemindEnd")){
			EditRemindActivity.tvEndDate
					.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
							+ decimal.format((wv_month.getCurrentItem() + 1))
							+ "-"
							+ decimal.format((wv_day.getCurrentItem() + 1)));
		}else if(flag.equals("addCompetition_btnStartDate")){
			AddCompetitionActivity.btnStartDate.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
					+ decimal.format((wv_month.getCurrentItem() + 1))
					+ "-"
					+ decimal.format((wv_day.getCurrentItem() + 1)));
		}else if(flag.equals("addCompetition_btnEndDate")){
			AddCompetitionActivity.btnEndDate.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
					+ decimal.format((wv_month.getCurrentItem() + 1))
					+ "-"
					+ decimal.format((wv_day.getCurrentItem() + 1)));
		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.wheel_date_btn_sure:
				updateStatus();
				DateWheelPop.this.dismiss();
				break;
			case R.id.wheel_date_btn_cancle:
				DateWheelPop.this.dismiss();
				break;
		}

	}
}

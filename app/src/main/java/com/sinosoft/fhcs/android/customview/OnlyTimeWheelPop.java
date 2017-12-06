package com.sinosoft.fhcs.android.customview;
/**
 * @CopyRight: SinoSoft.
 * @Description:时间控件弹窗
 * @Author: wangshuangshuang.
 * @Create: 2015年1月19日.
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
import com.sinosoft.fhcs.android.wheel.NumericWheelAdapter;
import com.sinosoft.fhcs.android.wheel.WheelView;

import java.text.DecimalFormat;

@SuppressLint("ViewConstructor")
public class OnlyTimeWheelPop extends PopupWindow implements
		android.view.View.OnClickListener {
	private Activity mContext;
	private TextView tvTitle;// 个人信息
	private Button btnSure;
	private Button btnCancle;
	private View mMenuView;
	private WheelView wv_hours,wv_mins;
	private int curHour=0;
	private int curMinute=0;
	private String flag="";
	@SuppressWarnings("deprecation")
	public OnlyTimeWheelPop(Activity context, int curHour,
							int curMinute,String flag) {
		super(context);
		this.flag=flag;
		this.mContext = context;
		this.curHour = curHour;
		this.curMinute = curMinute;

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.wheel_onlytime, null);
		tvTitle = (TextView) mMenuView.findViewById(R.id.wheel_onlytime_tv_title);
		btnSure = (Button) mMenuView.findViewById(R.id.wheel_onlytime_btn_sure);
		btnCancle = (Button) mMenuView.findViewById(R.id.wheel_onlytime_btn_cancle);
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
		if(flag.equals("addCompetition_btnStartTime")){
			tvTitle.setText("开始时间");
		}else if(flag.equals("addCompetition_btnEndTime")){
			tvTitle.setText("结束时间");
		}else{
			tvTitle.setText("请选择");
		}


	}
	private void initWheel() {
		// 时
		wv_hours = (WheelView) mMenuView.findViewById(R.id.wheel_onlytime_view_hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCyclic(true);
		wv_hours.setCurrentItem(curHour);

		// 分
		wv_mins = (WheelView) mMenuView.findViewById(R.id.wheel_onlytime_view_minute);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wv_mins.setCyclic(true);
		wv_mins.setCurrentItem(curMinute);
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
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;

	}
	private void updateStatus() {
		String parten = "00";
		DecimalFormat decimal = new DecimalFormat(parten);
		// 设置日期的显示
		if(flag.equals("addCompetition_btnStartTime")){
			AddCompetitionActivity.btnStartTime.setText(decimal.format(wv_hours.getCurrentItem()) + ":"
					+ decimal.format(wv_mins.getCurrentItem()));
		}else if(flag.equals("addCompetition_btnEndTime")){
			AddCompetitionActivity.btnEndTime.setText(decimal.format(wv_hours.getCurrentItem()) + ":"
					+ decimal.format(wv_mins.getCurrentItem()));
		}
	}
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.wheel_onlytime_btn_sure:
				updateStatus();
				OnlyTimeWheelPop.this.dismiss();
				break;
			case R.id.wheel_onlytime_btn_cancle:
				OnlyTimeWheelPop.this.dismiss();
				break;
		}

	}
}

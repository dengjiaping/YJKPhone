package com.sinosoft.fhcs.android.customview;

/**
 * @CopyRight: SinoSoft.
 * @Description:自定义数字控件弹窗
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
import com.sinosoft.fhcs.android.activity.EditFamilyMemberActivity;
import com.sinosoft.fhcs.android.wheel.NumericWheelAdapter;
import com.sinosoft.fhcs.android.wheel.WheelView;

@SuppressLint("ViewConstructor")
public class NumberWheelPop extends PopupWindow implements
		android.view.View.OnClickListener {
	private Activity context;
	private TextView tvTitle;// 个人信息
	private Button btnSure;
	private Button btnCancle;
	private WheelView wheel;
	private int min;
	private int max;
	private int currentItem;
	private View mMenuView;
	private String type;// 身高，体重，腰围，步长
	private String flag;// 修改，添加

	@SuppressWarnings("deprecation")
	public NumberWheelPop(Activity context, String type, String flag, int min,
						  int max, int currentItem) {
		super(context);
		this.context = context;
		this.type = type;
		this.flag = flag;
		this.min = min;
		this.max = max;
		this.currentItem = currentItem;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.wheel_number, null);
		tvTitle = (TextView) mMenuView.findViewById(R.id.wheel_number_tv_title);
		btnSure = (Button) mMenuView.findViewById(R.id.wheel_number_btn_sure);
		btnCancle = (Button) mMenuView
				.findViewById(R.id.wheel_number_btn_cancle);
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
		if (flag.equals("addMember") || flag.equals("editMember")) {
			if (type.equals("height")) {
				tvTitle.setText(context.getString(R.string.add_tv_height));
			} else if (type.equals("weight")) {
				tvTitle.setText(context.getString(R.string.add_tv_weight));
			} else if (type.equals("stepSize")) {
				tvTitle.setText(context.getString(R.string.add_tv_stepSize));
			} else if (type.equals("waist")) {
				tvTitle.setText(context.getString(R.string.add_tv_waist));
			}
		} else if(flag.equals("addcompetition")){
			if (type.equals("personNum")) {
				tvTitle.setText(context.getString(R.string.addcompetition_tv1));
			}
		}else {
			tvTitle.setText("请选择");
		}
	}

	private void initWheel() {
		wheel = (WheelView) mMenuView.findViewById(R.id.wheel_number_view);
		wheel.setAdapter(new NumericWheelAdapter(min, max));
		wheel.setCurrentItem(currentItem - min);
		// wheel.setVisibleItems(3); // Number of items
		wheel.setCyclic(true);

		// 根据屏幕密度来指定选择器字体的大小
		int textSize = 0;
		Display mDisplay = context.getWindowManager().getDefaultDisplay();
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

		wheel.TEXT_SIZE = textSize;
	}

	private void updateStatus() {
		if (flag.equals("addMember")) {
			if (type.equals("height")) {
				AddFamilyMemberActivity.btnHeight.setText(getAllCode());
			} else if (type.equals("weight")) {
				AddFamilyMemberActivity.btnWeight.setText(getAllCode());
			} else if (type.equals("stepSize")) {
				AddFamilyMemberActivity.btnStepSize.setText(getAllCode());
			} else if (type.equals("waist")) {
				AddFamilyMemberActivity.btnWaist.setText(getAllCode());
			}
		} else if (flag.equals("editMember")) {
			if (type.equals("height")) {
				EditFamilyMemberActivity.btnHeight.setText(getAllCode());
			} else if (type.equals("weight")) {
				EditFamilyMemberActivity.btnWeight.setText(getAllCode());
			} else if (type.equals("stepSize")) {
				EditFamilyMemberActivity.btnStepSize.setText(getAllCode());
			} else if (type.equals("waist")) {
				EditFamilyMemberActivity.btnWaist.setText(getAllCode());
			}
		} else if(flag.equals("addcompetition")){
			if (type.equals("personNum")) {
				AddCompetitionActivity.btnPersonNum.setText(getAllCode());
				if(Integer.valueOf(AddCompetitionActivity.btnPersonNum.getText().toString().trim())!=2){
					AddCompetitionActivity.btnModelTwo.setBackgroundResource(R.drawable.twopeople1);
					AddCompetitionActivity.btnModelMore.setBackgroundResource(R.drawable.morepeople2);
					AddCompetitionActivity.strModel="01";
				}else{
					AddCompetitionActivity.btnModelTwo.setBackgroundResource(R.drawable.twopeople2);
					AddCompetitionActivity.btnModelMore.setBackgroundResource(R.drawable.morepeople1);
					AddCompetitionActivity.strModel="02";
				}
			}
		}

	}

	private String getAllCode() {
		StringBuilder sb = new StringBuilder();
		return sb.append(wheel.getCurrentItem() + min).toString();
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.wheel_number_btn_sure:
				updateStatus();
				NumberWheelPop.this.dismiss();
				break;
			case R.id.wheel_number_btn_cancle:
				NumberWheelPop.this.dismiss();
				break;
		}

	}
}

package com.sinosoft.fhcs.android.customview;

/**
 * @CopyRight: SinoSoft.
 * @Description:我的配件——运动页
 * @Author: pikai.
 * @Create: 2015年2月12日.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.EditFamilyMemberActivity;
import com.sinosoft.fhcs.android.activity.InformationActivity;

@SuppressLint("ViewConstructor")
public class MyMenuPopupWindowTwo extends PopupWindow implements OnClickListener {

	private View mMenuView;
	private Activity mContext;
	private LinearLayout menu_layout_one, menu_layout_two,
			menu_layout_three, menu_layout_four;

	public MyMenuPopupWindowTwo(Activity context) {
		super(context);
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.menupop_two, null);
		findId();
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.popuStyle);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
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

	private void findId() {
		menu_layout_one= (LinearLayout) mMenuView
				.findViewById(R.id.menu_layout_one);
		menu_layout_two= (LinearLayout) mMenuView
				.findViewById(R.id.menu_layout_two);
		menu_layout_three= (LinearLayout) mMenuView
				.findViewById(R.id.menu_layout_three);
		menu_layout_four= (LinearLayout) mMenuView
				.findViewById(R.id.menu_layout_four);
		menu_layout_one.setOnClickListener(this);
		menu_layout_two.setOnClickListener(this);
		menu_layout_three.setOnClickListener(this);
		menu_layout_four.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.menu_layout_one:
//			Intent intent1=new Intent(mContext,RecentlyRecordActivity.class);
//			mContext.startActivity(intent1);
				mContext.finish();
				dismiss();
				break;
			case R.id.menu_layout_two:
				Intent intent2=new Intent(mContext,InformationActivity.class);
				mContext.startActivity(intent2);
				mContext.finish();
				dismiss();
				break;
			case R.id.menu_layout_three:
//			Intent intent3=new Intent(mContext,RemindListActivity.class);
//			mContext.startActivity(intent3);
				mContext.finish();
				dismiss();
				break;
			case R.id.menu_layout_four:
				Intent intent4=new Intent(mContext,EditFamilyMemberActivity.class);
				mContext.startActivity(intent4);
				mContext.finish();
				dismiss();
				break;
			default:
				dismiss();
				break;
		}

	}
}
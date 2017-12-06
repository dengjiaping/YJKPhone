//package com.sinosoft.fhcs.android.customview;
//
///**
// * @CopyRight: SinoSoft.
// * @Description:家庭管理右上角菜单弹窗    废弃页面
// * @Author: wangshuangshuang.
// * @Create: 2014年8月15日.
// */
//import com.sinosoft.fhcs.android.R;
//import com.sinosoft.fhcs.android.activity.EditFamilyMemberActivity;
//import com.sinosoft.fhcs.android.activity.InformationThirdActivity;
//import com.sinosoft.fhcs.android.activity.RecentlyRecordActivity;
//import com.sinosoft.fhcs.android.activity.RemindListActivity;
//import com.sinosoft.fhcs.android.activity.SetGoalsActivity;
//import com.sinosoft.fhcs.android.entity.FamilyMember;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//
//@SuppressLint("ViewConstructor")
//public class MyMenuPopupWindow extends PopupWindow implements OnClickListener {
//
//	private View mMenuView;
//	private Activity mContext;
//	private RelativeLayout menu_layout_record, menu_layout_information,
//			menu_layout_medicine, menu_layout_info,menu_layout_goal;
//	private FamilyMember member=new FamilyMember();
//	public MyMenuPopupWindow(Activity context, FamilyMember member) {
//		super(context);
//		this.mContext = context;
//		this.member=member;
//		LayoutInflater inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		mMenuView = inflater.inflate(R.layout.menupop, null);
//		findId();
//		// 设置SelectPicPopupWindow的View
//		this.setContentView(mMenuView);
//		// 设置SelectPicPopupWindow弹出窗体的宽
//		this.setWidth(LayoutParams.WRAP_CONTENT);
//		// 设置SelectPicPopupWindow弹出窗体的高
//		this.setHeight(LayoutParams.WRAP_CONTENT);
//		// 设置SelectPicPopupWindow弹出窗体可点击
//		this.setFocusable(true);
//		// 设置SelectPicPopupWindow弹出窗体动画效果
//		// this.setAnimationStyle(R.style.popuStyle);
//		// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0x00000000);
//		// 设置SelectPicPopupWindow弹出窗体的背景
//		this.setBackgroundDrawable(dw);
//		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//		mMenuView.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View v, MotionEvent event) {
//
//				dismiss();
//				return true;
//			}
//		});
//
//	}
//
//	private void findId() {
//		menu_layout_record = (RelativeLayout) mMenuView
//				.findViewById(R.id.menu_layout_record);
//		menu_layout_information = (RelativeLayout) mMenuView
//				.findViewById(R.id.menu_layout_information);
//		menu_layout_medicine = (RelativeLayout) mMenuView
//				.findViewById(R.id.menu_layout_medicine);
//		menu_layout_info = (RelativeLayout) mMenuView
//				.findViewById(R.id.menu_layout_info);
//		menu_layout_goal=(RelativeLayout) mMenuView
//				.findViewById(R.id.menu_layout_goal);
//		menu_layout_record.setOnClickListener(this);
//		menu_layout_information.setOnClickListener(this);
//		menu_layout_medicine.setOnClickListener(this);
//		menu_layout_info.setOnClickListener(this);
//		menu_layout_goal.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.menu_layout_record:
//			Intent intent1=new Intent(mContext,RecentlyRecordActivity.class);
//			intent1.putExtra("member", member);
//			mContext.startActivity(intent1);
//			mContext.finish();
//			dismiss();
//			break;
//		case R.id.menu_layout_information:
//			Intent intent2=new Intent(mContext,InformationThirdActivity.class);
//			intent2.putExtra("memberId", member.getId()+"");
//			intent2.putExtra("member", member);
//			mContext.startActivity(intent2);
//			mContext.finish();
//			dismiss();
//			break;
//		case R.id.menu_layout_medicine:
//			Intent intent3=new Intent(mContext,RemindListActivity.class);
//			intent3.putExtra("member", member);
//			mContext.startActivity(intent3);
//			mContext.finish();
//			dismiss();
//			break;
//		case R.id.menu_layout_info:
//			Intent intent4=new Intent(mContext,EditFamilyMemberActivity.class);
//			intent4.putExtra("member", member);
//			mContext.startActivity(intent4);
//			mContext.finish();
//			dismiss();
//			break;
//		case R.id.menu_layout_goal:
//			Intent intent5=new Intent(mContext,SetGoalsActivity.class);
//			intent5.putExtra("member", member);
//			mContext.startActivity(intent5);
//			mContext.finish();
//			dismiss();
//			break;
//		default:
//			dismiss();
//			break;
//		}
//
//	}
//}
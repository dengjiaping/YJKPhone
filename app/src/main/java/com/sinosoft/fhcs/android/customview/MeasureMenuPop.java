package com.sinosoft.fhcs.android.customview;

/**
 * @CopyRight: SinoSoft.
 * @Description:测量右上角菜单弹窗
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.bottomfragment.MeasurementFragment;
import com.sinosoft.fhcs.android.adapter.MeasureGridviewAdapter;
import com.sinosoft.fhcs.android.entity.MeasureMainInfo;

import java.util.ArrayList;
import java.util.List;

public class MeasureMenuPop extends PopupWindow {

	private View mMenuView;
	private Activity mContext;
	private GridView gridView;
	private MeasureGridviewAdapter adapter;
	private List<MeasureMainInfo> list = new ArrayList<MeasureMainInfo>();

	public MeasureMenuPop(Activity context, List<MeasureMainInfo> list) {
		super(context);
		this.mContext = context;
		this.list = list;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_measure, null);
		findId();
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
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
		gridView = (GridView) mMenuView.findViewById(R.id.pop_measure_gridview);
		adapter = new MeasureGridviewAdapter(mContext, list);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				MeasurementFragment.initData(list.get(position));
				dismiss();

			}
		});
	}

}
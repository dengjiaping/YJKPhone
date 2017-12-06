package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description:服药提醒列表适配器
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.entity.RemindListInfo;
import com.sinosoft.fhcs.android.util.Constant;

import java.util.List;

public class RemindListAdapter extends BaseAdapter {

	private Context mContext;
	private List<RemindListInfo> myList;
	private LayoutInflater mInflater;
	public RemindListAdapter(Context context, List<RemindListInfo> list) {
		this.mContext = context;
		this.myList = list;
		mInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return myList.size();
	}

	public Object getItem(int position) {
		return myList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.remindlist_listview_item, null);
		}
		TextView tvMedicineName=(TextView) convertView.findViewById(R.id.rl_item_medicinename);
		tvMedicineName.setText(myList.get(position).getMedicineName());
		TextView tvMeal=(TextView) convertView.findViewById(R.id.rl_item_meal);
		tvMeal.setText(Constant.getMeal(myList.get(position).getReminderByMeal()));
		TextView tvCount=(TextView) convertView.findViewById(R.id.rl_item_medicinecount);
		tvCount.setText(myList.get(position).getDosage());
		TextView tvDate=(TextView) convertView.findViewById(R.id.rl_item_date);
		String startTime=myList.get(position).getStartTime();
		String endTime=myList.get(position).getEndTime();
		tvDate.setText(Constant.getDateFormat(startTime)+"~"+Constant.getDateFormat(endTime));
		TextView tvTime=(TextView) convertView.findViewById(R.id.rl_item_time);
		tvTime.setText(myList.get(position).getReminderTime());
		ImageView imgMEAL=(ImageView) convertView.findViewById(R.id.rl_item_meal_img);
		ImageView imgJL=(ImageView) convertView.findViewById(R.id.rl_item_medicinecount_img);
		if(myList.get(position).isExpired()){
			//过期
			convertView.setBackgroundResource(R.drawable.bg_sys_isread_selector);
			imgJL.setBackgroundResource(R.drawable.remind_icon_jl2);
			if(Constant.getMeal(myList.get(position).getReminderByMeal()).equals("餐前")){
				imgMEAL.setBackgroundResource(R.drawable.remind_icon_cq2);
			}else{
				imgMEAL.setBackgroundResource(R.drawable.remind_icon_ch2);
			}
		}else{
			//没过期
			convertView.setBackgroundResource(R.drawable.bg_sys_noread_selector);
			imgJL.setBackgroundResource(R.drawable.remind_icon_jl1);
			if(Constant.getMeal(myList.get(position).getReminderByMeal()).equals("餐前")){
				imgMEAL.setBackgroundResource(R.drawable.remind_icon_cq1);
			}else{
				imgMEAL.setBackgroundResource(R.drawable.remind_icon_ch1);
			}
		}
		return convertView;
	}
}













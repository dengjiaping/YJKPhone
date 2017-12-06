package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 竞赛列表适配器
 * @Author: wangshuangshuang.
 * @Create: 2015年1月19日.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.entity.CompetitionListInfo;

import java.util.ArrayList;
import java.util.List;

public class CompetitionListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<CompetitionListInfo>myList=new ArrayList<CompetitionListInfo>();
	public CompetitionListAdapter(Context context,List<CompetitionListInfo>list) {
		this.mContext = context;
		this.myList=list;
		mInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		if(myList.size()==0){
			return 0;
		}else{
			return myList.size();
		}
	}

	public Object getItem(int position) {
		return myList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.competitionlist_listview_item,
					null);
		}
		ImageView ivModel = (ImageView) convertView
				.findViewById(R.id.cmptlist_listviewitem_model);
		ImageView ivType = (ImageView) convertView
				.findViewById(R.id.cmptlist_listviewitem_type);
		TextView tvSession = (TextView) convertView
				.findViewById(R.id.cmptlist_listviewitem_session);
		TextView tvPeopleNum = (TextView) convertView
				.findViewById(R.id.cmptlist_listviewitem_peoplenum);
		TextView tvCountDown = (TextView) convertView
				.findViewById(R.id.cmptlist_listviewitem_countdown);
		View line=convertView.findViewById(R.id.cmptlist_listviewitem_view);
		if(position==myList.size()-1){
			line.setVisibility(View.GONE);
		}else{
			line.setVisibility(View.VISIBLE);
		}


		//显示
		if(myList.get(position).getModel().toString().trim().equals("01")){
			//多人 01  两人 02
			ivModel.setImageResource(R.drawable.icon_morepeople);
		}else{
			ivModel.setImageResource(R.drawable.icon_twopeople);
		}
		if(myList.get(position).getType().toString().trim().equals("01")){
			//跑步  01   骑行  02
			ivType.setImageResource(R.drawable.icon_run);
		}else{
			ivType.setImageResource(R.drawable.icon_bike);
		}
		tvSession.setText(myList.get(position).getSession().toString().trim());
		tvPeopleNum.setText(myList.get(position).getPeopleNowNum().toString().trim());
		tvCountDown.setText(myList.get(position).getCountdown().toString().trim());
		return convertView;
	}
}

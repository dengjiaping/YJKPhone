package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 预置家庭成员列表适配器
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class MemberGridViewAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private List<FamilyMember>list=new ArrayList<FamilyMember>();
	public MemberGridViewAdapter(Context context,List<FamilyMember>list) {
		super();
		this.mContext = context;
		this.list=list;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if(list.size()==0){
			return 0;
		}else{
			return list.size();
		}

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.dialog_membergridview_item, null);
		CircleImageView iconBg=(CircleImageView) convertView.findViewById(R.id.memberdialog_gridview_icon);
		iconBg.setImageResource(Constant.ImageIdbg(list.get(position).getFamilyRoleName(),list.get(position).getGender()));
		TextView tvName=(TextView) convertView.findViewById(R.id.memberdialog_gridview_name);
		tvName.setText(list.get(position).getFamilyRoleName());
		return convertView;
	}

}

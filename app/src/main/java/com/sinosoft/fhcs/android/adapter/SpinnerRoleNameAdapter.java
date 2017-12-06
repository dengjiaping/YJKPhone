package com.sinosoft.fhcs.android.adapter;
/**
 * 添加服药提醒  服药人 Adapter
 * @author wangshuangshuang
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.entity.FamilyMember;

import java.util.ArrayList;
import java.util.List;

public class SpinnerRoleNameAdapter extends BaseAdapter{
	private Context mContext;
	private List<Object> getList = new ArrayList<Object>();
	private LayoutInflater mInflater;

	public SpinnerRoleNameAdapter(Context context, List<Object> getList) {
		super();
		this.mContext = context;
		this.getList = getList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if(getList.size()==0){
			return 0;
		}else{
			return getList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return getList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.spinner_meal_adapter, null);
		}
		TextView tv=(TextView) convertView.findViewById(R.id.spinmeal_text);
		tv.setText(((FamilyMember)getList.get(position)).getFamilyRoleName());
		return convertView;
	}

}

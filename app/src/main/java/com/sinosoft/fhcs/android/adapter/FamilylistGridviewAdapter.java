package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 家庭成员列表适配器
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
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class FamilylistGridviewAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Object>list=new ArrayList<Object>();
	private boolean flag=false;
	public FamilylistGridviewAdapter(Context context,List<Object>list,boolean flag) {
		super();
		this.mContext = context;
		this.list=list;
		this.flag=flag;
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
		convertView = mInflater.inflate(R.layout.familylist_gridview_item, null);
		CircleImageView iconBg=(CircleImageView) convertView.findViewById(R.id.familylist_gridview_icon);
		iconBg.setImageResource(Constant.ImageId(((FamilyMember)list.get(position)).getFamilyRoleName(),((FamilyMember)list.get(position)).getGender()));
		TextView tvName= (TextView) convertView.findViewById(R.id.familylist_gridview_name);
		tvName.setText(((FamilyMember)list.get(position)).getFamilyRoleName());
		ImageView ivDelete=(ImageView) convertView.findViewById(R.id.familylist_gridview_delete);
		ImageView ivMain=(ImageView) convertView.findViewById(R.id.familylist_gridview_zhu);
		if(((FamilyMember)list.get(position)).getFamilyRoleName().equals("添加")){
			tvName.setTextColor(mContext.getResources().getColor(
					R.color.history_blue_text));
		}else{
			tvName.setTextColor(mContext.getResources().getColor(
					R.color.history_black_text));
		}
		if(((FamilyMember)list.get(position)).isMasterFamilyMember()){
			//主家庭成员
			ivMain.setVisibility(View.VISIBLE);
		}else{
			ivMain.setVisibility(View.GONE);
			if(flag){
				if(((FamilyMember)list.get(position)).getFamilyRoleName().equals("添加")){
					ivDelete.setVisibility(View.GONE);
				}else{
					ivDelete.setVisibility(View.VISIBLE);
				}

			}else{

			}
		}

		return convertView;
	}

}

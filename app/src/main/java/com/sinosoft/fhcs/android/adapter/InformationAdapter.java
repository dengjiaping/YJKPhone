package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 健康资讯列表适配器
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.entity.InformationChild;
import com.sinosoft.fhcs.android.entity.InformationGroup;
import com.sinosoft.fhcs.android.util.Constant;

import java.util.List;

public class InformationAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private LayoutInflater mInflater = null;
	private List<InformationGroup> list_ground;
	private List<InformationChild> list_child;

	public InformationAdapter(Context context,
							  List<InformationGroup> list_ground,
							  List<InformationChild> list_child) {
		super();
		this.mContext = context;
		this.list_ground = list_ground;
		this.list_child = list_child;
		mInflater = LayoutInflater.from(mContext);
	}

	public InformationAdapter(Context context,
							  List<InformationGroup> list_ground) {
		super();
		this.mContext = context;
		this.list_ground = list_ground;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		list_child = list_ground.get(arg0).getList_child();
		return list_child.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {

		return arg1;
	}

	@Override
	public int getChildrenCount(int arg0) {
		list_child = list_ground.get(arg0).getList_child();
		return list_child.size();
	}

	@Override
	public Object getGroup(int arg0) {
		return list_ground.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return list_ground.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.information_listview_item_tag, null);
		}
		GroupViewHolder holder = new GroupViewHolder();
		holder.mGroupName = (TextView) convertView
				.findViewById(R.id.information_tv_tag);
		holder.mGroupName.setText(list_ground.get(groupPosition).getS_title());
		holder.mGroupImg = (ImageView) convertView
				.findViewById(R.id.information_iv_tag);
		holder.mGroupNum = (TextView) convertView
				.findViewById(R.id.information_tv_num);

		int noRead = list_ground.get(groupPosition).getUnreadCount();
		if (noRead == 0) {
			holder.mGroupNum.setVisibility(View.GONE);
		} else {
			holder.mGroupNum.setVisibility(View.VISIBLE);
		}
		if (noRead > 9) {
			holder.mGroupNum.setText("9+");
			holder.mGroupNum.setBackgroundResource(R.drawable.flag2);
		} else {
			holder.mGroupNum.setText(noRead + "");
			holder.mGroupNum.setBackgroundResource(R.drawable.flag1);
		}
		if (isExpanded) {
			holder.mGroupImg.setBackgroundResource(R.drawable.narrow_select);
			holder.mGroupName.setTextColor(mContext.getResources().getColor(
					R.color.info_tagselector_text));
			convertView.setBackgroundResource(R.color.info_tagselector_bg);
		} else {
			holder.mGroupImg.setBackgroundResource(R.drawable.narrow);
			holder.mGroupName.setTextColor(mContext.getResources().getColor(
					R.color.info_tagnormal_text));
			convertView.setBackgroundResource(R.color.info_tagnormal_bg);
		}
		return convertView;
	}

	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.information_listview_item,
					null);
		}
		final ChildViewHolder holder = new ChildViewHolder();
		holder.tvChildName = (TextView) convertView
				.findViewById(R.id.information_tv_content);
		holder.tvDate = (TextView) convertView
				.findViewById(R.id.information_tv_date);
		holder.img = (ImageView) convertView.findViewById(R.id.information_img);
		holder.img2 = (ImageView) convertView
				.findViewById(R.id.information_img2);
		list_child = list_ground.get(groupPosition).getList_child();
		if (list_child.get(childPosition).isRead()) {
			convertView
					.setBackgroundResource(R.drawable.bg_sys_isread_selector);
			holder.tvChildName.setTextColor(mContext.getResources().getColor(
					R.color.sys_isread_text));
			holder.tvDate.setTextColor(mContext.getResources().getColor(
					R.color.sys_isread_text));
		} else {
			convertView
					.setBackgroundResource(R.drawable.bg_info_noread_selector);
			holder.tvChildName.setTextColor(mContext.getResources().getColor(
					R.color.sys_noread_text_black));
			holder.tvDate.setTextColor(mContext.getResources().getColor(
					R.color.sys_noread_text_gray));
		}
		String type = list_child.get(childPosition).getInformationType()
				.toString().trim();
		if (type.equals("3000101") || type.equals("3000102")) {
			// 即时简讯||关爱报告
			holder.img.setVisibility(View.VISIBLE);
			holder.img2.setVisibility(View.GONE);
			holder.img.setImageResource(Constant.ImageId(
					list_child.get(childPosition).getFamilyMemberRoleName(),
					list_child.get(childPosition).getGender()));
		} else if (type.equals("3000103")) {
			// 知识图文
			holder.img.setVisibility(View.GONE);
			holder.img2.setVisibility(View.VISIBLE);
			if (list_child.get(childPosition).isRead()) {
				// 已读
				holder.img2.setImageResource(R.drawable.img_isread);
			} else {
				// 未读
				holder.img2.setImageResource(R.drawable.img_noread);
			}

		} else if (type.equals("3000104")) {
			// 健康讲座
			holder.img.setVisibility(View.GONE);
			holder.img2.setVisibility(View.VISIBLE);
			if (list_child.get(childPosition).isRead()) {
				// 已读
				holder.img2.setImageResource(R.drawable.video_isread);
			} else {
				// 未读
				holder.img2.setImageResource(R.drawable.video_noread);
			}
		}

		String strName = list_child.get(childPosition).getTitle();
		String strDate = Constant.getDateFormat2(list_child.get(childPosition)
				.getPubdate());
		holder.tvChildName.setText(strName);
		holder.tvDate.setText(strDate);
		return convertView;
	}

	private class GroupViewHolder {
		private TextView mGroupName;
		private ImageView mGroupImg;
		private TextView mGroupNum;
	}

	private class ChildViewHolder {
		private TextView tvChildName;
		private TextView tvDate;
		private ImageView img;
		private ImageView img2;
	}

}

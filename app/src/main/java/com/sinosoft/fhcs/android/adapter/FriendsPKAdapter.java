package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 好友比拼适配器
 * @Author: wangshuangshuang.
 * @Create: 2015年1月27日.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.FriendsPKInfo;
import com.sinosoft.fhcs.android.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class FriendsPKAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<FriendsPKInfo> myList = new ArrayList<FriendsPKInfo>();
	private DisplayImageOptions options;
	private boolean flag = false;// 是否为附近，默认为否

	public FriendsPKAdapter(Context context, List<FriendsPKInfo> list,
							boolean flag) {
		this.mContext = context;
		this.myList = list;
		this.flag = flag;
		mInflater = LayoutInflater.from(mContext);
		initOptions();
	}
	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.avatar)
				.showImageForEmptyUri(R.drawable.avatar)
				.showImageOnFail(R.drawable.avatar)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

	}
	public int getCount() {
		if (myList.size() == 0) {
			return 0;
		} else {
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
			convertView = mInflater.inflate(R.layout.friendspk_listview_item,
					null);
		}
		CircleImageView img = (CircleImageView) convertView
				.findViewById(R.id.friendspk_listviewitem_iv_img);
		TextView tvName = (TextView) convertView
				.findViewById(R.id.friendspk_listviewitem_tv_name);
		TextView tvDistance = (TextView) convertView
				.findViewById(R.id.friendspk_listviewitem_tv_distance);
		TextView tvNear = (TextView) convertView
				.findViewById(R.id.friendspk_listviewitem_tv_near);
		ImageView ivGender = (ImageView) convertView
				.findViewById(R.id.friendspk_listviewitem_iv_gender);
		View line = convertView
				.findViewById(R.id.friendspk_listviewitem_view_line);
		if (position == 0) {
			line.setVisibility(View.GONE);
		} else {
			line.setVisibility(View.VISIBLE);
		}
		if (flag) {
			tvNear.setVisibility(View.VISIBLE);
		} else {
			tvNear.setVisibility(View.GONE);
		}
		if (myList.get(position).getGender().toString().trim().equals("1")) {
			// 男
			ivGender.setBackgroundResource(R.drawable.icon_man);
		} else {
			ivGender.setBackgroundResource(R.drawable.icon_women);
		}
		tvName.setText(myList.get(position).getNickName().toString().trim());
		tvDistance
				.setText(myList.get(position).getDistance().toString().trim());
		tvNear.setText(Constant.NearDistance(myList.get(position).getNear()));
		ImageLoader.getInstance().displayImage(myList.get(position).getImgUrl(), img, options);
		return convertView;
	}
}

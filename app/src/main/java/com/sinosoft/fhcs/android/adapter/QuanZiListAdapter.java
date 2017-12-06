package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 圈子列表适配器
 * @Author: wangshuangshuang.
 * @Create: 2015年4月3日.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.QuanZiInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuanZiListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<QuanZiInfo> myList = new ArrayList<QuanZiInfo>();
	private DisplayImageOptions options;
	public QuanZiListAdapter(Context context, List<QuanZiInfo> list) {
		this.mContext = context;
		this.myList = list;
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

	@SuppressLint({ "SimpleDateFormat", "InflateParams" })
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.quanzi_listviewitem, null);
		}
		CircleImageView icon = (CircleImageView) convertView
				.findViewById(R.id.quanzi_listviewitem_iv_img);
		TextView tvContent = (TextView) convertView
				.findViewById(R.id.quanzi_listviewitem_tv_content);
		TextView tvNickName = (TextView) convertView
				.findViewById(R.id.quanzi_listviewitem_tv_name);
		TextView tvDate = (TextView) convertView
				.findViewById(R.id.quanzi_listviewitem_tv_date);
		if (myList.get(position).isRead()) {
			tvContent.setTextColor(mContext.getResources().getColor(R.color.sys_isread_text));
			tvNickName.setTextColor(mContext.getResources().getColor(R.color.sys_isread_text));
			tvDate.setTextColor(mContext.getResources().getColor(R.color.sys_isread_text));
			convertView.setBackgroundResource(R.drawable.bg_sys_isread_selector);

		} else {
			tvContent.setTextColor(mContext.getResources().getColor(R.color.sys_noread_text_gray2));
			tvNickName.setTextColor(mContext.getResources().getColor(R.color.sys_noread_text_black));
			tvDate.setTextColor(mContext.getResources().getColor(R.color.sys_noread_text_gray2));
			convertView.setBackgroundResource(R.drawable.bg_sys_noread_selector);
		}
		tvContent.setText(myList.get(position).getContent().toString().trim());
		tvNickName.setText(myList.get(position).getFriendName().toString()
				.trim());
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sf2=new SimpleDateFormat("MM-dd HH:mm");
		try {
			Date date=sf.parse(myList.get(position).getMessageTime().toString());
			tvDate.setText(sf2.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ImageLoader.getInstance().displayImage(myList.get(position).getAvatarPath(), icon, options);
		return convertView;
	}
}

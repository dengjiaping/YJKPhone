package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 设备管理列表适配器
 * @Author: wangshuangshuang.
 * @Create: 2015年1月8日.
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
import com.sinosoft.fhcs.android.entity.EquipmentListInfo;

import java.util.ArrayList;
import java.util.List;

public class EquipmentlistAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private DisplayImageOptions options;
	private List<EquipmentListInfo>myList=new ArrayList<EquipmentListInfo>();
	public EquipmentlistAdapter(Context context,List<EquipmentListInfo>list) {
		this.mContext = context;
		this.myList=list;
		mInflater = LayoutInflater.from(mContext);
		initOptions();
	}
	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.img_ing)
				.showImageForEmptyUri(R.drawable.img_xx)
				.showImageOnFail(R.drawable.img_xx)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

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
			convertView = mInflater.inflate(R.layout.equipmentlist_listview_item,
					null);
		}
		ImageView iv = (ImageView) convertView
				.findViewById(R.id.equiplistview_item_img);
		TextView tvName = (TextView) convertView
				.findViewById(R.id.equiplistview_item_tv_name);
		TextView tvDate = (TextView) convertView
				.findViewById(R.id.equiplistview_item_tv_time);
		View line=convertView.findViewById(R.id.equiplistview_item_view);
		if(position==myList.size()-1){
			line.setVisibility(View.GONE);
		}else{
			line.setVisibility(View.VISIBLE);
		}
		//显示
		ImageLoader.getInstance().displayImage(myList.get(position).getImgUrl(), iv, options);
		tvName.setText(myList.get(position).getFamilyMemberRoleName().trim()+"的"+myList.get(position).getDeviceName().trim());
		if(myList.get(position).getSyncDataTime()!=null&&!myList.get(position).getSyncDataTime().equals("")){
			tvDate.setText("最近数据同步时间："+myList.get(position).getSyncDataTime());
		}else{
			tvDate.setText("暂未同步数据");
		}
		return convertView;
	}
}

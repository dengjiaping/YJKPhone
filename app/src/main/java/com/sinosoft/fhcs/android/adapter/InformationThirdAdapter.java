package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 资讯厂商列表适配器
 * @Author: wangshuangshuang.
 * @Create: 2015年2月13日.
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
import com.sinosoft.fhcs.android.entity.InformationThird;

import java.util.List;

public class InformationThirdAdapter extends BaseAdapter {

	private Context mContext;
	private List<InformationThird> myList;
	private LayoutInflater mInflater;
	private DisplayImageOptions options;
	public InformationThirdAdapter(Context context, List<InformationThird> list) {
		this.mContext = context;
		this.myList = list;
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
			convertView = mInflater.inflate(R.layout.informationthird_listitem,
					null);
		}
		ImageView img = (ImageView) convertView
				.findViewById(R.id.inthird_item_img);
		TextView tvName = (TextView) convertView
				.findViewById(R.id.inthird_item_name);
		TextView tvName2 = (TextView) convertView
				.findViewById(R.id.inthird_item_name2);
		TextView tvNum = (TextView) convertView
				.findViewById(R.id.inthird_item_num);

		tvName.setText(myList.get(position).getFacilitatorName());
		tvName2.setText(myList.get(position).getFacilitatorName());
		int noRead=myList.get(position).getNotReadCount();
		if (noRead == 0) {
			tvNum.setVisibility(View.GONE);
			tvName.setVisibility(View.GONE);
			tvName2.setVisibility(View.VISIBLE);
		} else {
			tvNum.setVisibility(View.VISIBLE);
			tvName.setVisibility(View.VISIBLE);
			tvName2.setVisibility(View.GONE);
		}
		if (noRead > 9) {
			tvNum.setText("9+");
			tvNum.setBackgroundResource(R.drawable.flag2);
		} else {
			tvNum.setText(noRead + "");
			tvNum.setBackgroundResource(R.drawable.flag1);
		}
		if(position==6){
			img.setImageResource(R.drawable.icon_info_medicine);
		}else if(position==0){
			img.setImageResource(R.drawable.icon_info_medicine_registration);

		}else if(position==1){
			img.setImageResource(R.drawable.icon_info_medicine_pharmacy);

		}else if(position==2){
			img.setImageResource(R.drawable.icon_info_medicine_disease);
		}else {
			ImageLoader.getInstance().displayImage(myList.get(position).getMinIcon(), img, options);
		}
		return convertView;
	}
}

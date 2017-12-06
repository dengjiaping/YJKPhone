package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 家庭成员列表适配器
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.CompetitionPeople;

import java.util.ArrayList;
import java.util.List;

public class CompetitionDetailGridviewAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<CompetitionPeople> list = new ArrayList<CompetitionPeople>();
	private DisplayImageOptions options;

	public CompetitionDetailGridviewAdapter(Context context,
											List<CompetitionPeople> list) {
		super();
		this.mContext = context;
		this.list = list;
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
	@Override
	public int getCount() {
		if (list.size() == 0) {
			return 0;
		} else {
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
		convertView = mInflater.inflate(R.layout.cptdetail_gridview_item, null);
		// 正常数据
		CircleImageView iconBg = (CircleImageView) convertView
				.findViewById(R.id.cptdetail_item_ivNormal);
		TextView tvNumber = (TextView) convertView
				.findViewById(R.id.cptdetail_item_tvNormal_number);
		TextView tvName = (TextView) convertView
				.findViewById(R.id.cptdetail_item_tvNormal_name);
		TextView tvDistance = (TextView) convertView
				.findViewById(R.id.cptdetail_item_tvNormal_distance);
		LinearLayout l1 = (LinearLayout) convertView
				.findViewById(R.id.cptdetail_item_layoutNormal);
		// 加入他们
		LinearLayout l2 = (LinearLayout) convertView
				.findViewById(R.id.cptdetail_item_layoutJoin);
		ImageView imgJoin = (ImageView) convertView
				.findViewById(R.id.cptdetail_item_ivJoin);
		TextView tvJoin = (TextView) convertView
				.findViewById(R.id.cptdetail_item_tvJoin);

		if (list.get(position).getName().toString().trim().equals("加入他们")
				&& list.get(position).getUrl().toString().trim()
				.equals("joiniconOk")) {
			//已经加入
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.VISIBLE);
			imgJoin.setBackgroundResource(R.drawable.joinicon3);
			tvJoin.setTextColor(mContext.getResources().getColor(R.color.text_NOjoincompetition));
		} else if (list.get(position).getName().toString().trim()
				.equals("加入他们")
				&& list.get(position).getUrl().toString().trim()
				.equals("joiniconFalse")) {
			l1.setVisibility(View.GONE);
			l2.setVisibility(View.VISIBLE);
			imgJoin.setBackgroundResource(R.drawable.btn_cptdetailjoin_selector);
			tvJoin.setTextColor(mContext.getResources().getColor(R.color.text_joincompetition));
		} else {
			l1.setVisibility(View.VISIBLE);
			l2.setVisibility(View.GONE);
			String temp = position + 1 + "";
//			if (temp.length() == 1) {
//				temp = "0" + temp;
//			}
			temp = "No." + temp;
			tvNumber.setText("(" + (temp) + ")");
			tvName.setText(list.get(position).getName());
			tvDistance.setText(list.get(position).getDistance());
			ImageLoader.getInstance().displayImage(list.get(position).getUrl(), iconBg, options);
		}

		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		if ((list.size() - 1) == position) {
			return true;
		} else {
			return false;
		}

	}

}

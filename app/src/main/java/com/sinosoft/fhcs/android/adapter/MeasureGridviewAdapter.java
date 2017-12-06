package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 测量家庭成员列表适配器
 * @Author: wangshuangshuang.
 * @Create: 2015年2月2日.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.MeasureMainInfo;
import com.sinosoft.fhcs.android.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class MeasureGridviewAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private List<MeasureMainInfo>list=new ArrayList<MeasureMainInfo>();
	public MeasureGridviewAdapter(Context context,List<MeasureMainInfo>list) {
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
		convertView = mInflater.inflate(R.layout.pop_measure_gridview_item, null);
		CircleImageView iconBg=(CircleImageView) convertView.findViewById(R.id.pop_measure_gridviewitem_img);
		iconBg.setImageResource(Constant.ImageIdbg(list.get(position).getNickName(),list.get(position).getGender()));
		TextView tvName=(TextView) convertView.findViewById(R.id.pop_measure_gridviewitem_name);
		tvName.setText(list.get(position).getNickName());
		return convertView;
	}

}

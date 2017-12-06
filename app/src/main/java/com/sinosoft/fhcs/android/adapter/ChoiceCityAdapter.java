package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description: 城市选择适配器
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
import com.sinosoft.fhcs.android.entity.CityListItem;

import java.util.List;

public class ChoiceCityAdapter extends BaseAdapter {

	private Context mContext;
	private List<CityListItem> myList;
	private LayoutInflater mInflater;
	public ChoiceCityAdapter(Context context, List<CityListItem> myList) {
		this.mContext = context;
		this.myList = myList;
		mInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return myList.size();
	}

	public Object getItem(int position) {
		return myList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.choice_gridview_item, null);
		TextView tvTitle=(TextView) convertView.findViewById(R.id.choice_gridview_tv);
		tvTitle.setText(myList.get(position).getName());
		return convertView;
	}

//	class MyAdapterView extends LinearLayout {
//		public static final String LOG_TAG = "MyAdapterView";
//
//		public MyAdapterView(Context context, MyListItem myListItem) {
//			super(context);
//			this.setOrientation(HORIZONTAL);
//
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			TextView name = new TextView(context);
//			name.setText(myListItem.getName());
//			name.setTextColor(Color.BLACK);
//			name.setGravity(Gravity.CENTER);
//			params.gravity=Gravity.CENTER;
//			addView(name, params);
//
//			LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
//					200, LayoutParams.WRAP_CONTENT);
//			params2.setMargins(1, 1, 1, 1);
//
//			TextView pcode = new TextView(context);
//			pcode.setText(myListItem.getPcode());
//			name.setTextColor(Color.BLACK);
//			name.setPadding(10, 5, 10, 5);
//			addView(pcode, params2);
//			pcode.setVisibility(GONE);
//
//		}
//
//	}

}
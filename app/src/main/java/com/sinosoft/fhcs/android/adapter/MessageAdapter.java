package com.sinosoft.fhcs.android.adapter;

/**
 * @CopyRight: SinoSoft.
 * @Description:系统消息列表适配器
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
import com.sinosoft.fhcs.android.entity.MessageInfo;
import com.sinosoft.fhcs.android.util.Constant;

import java.util.List;
public class MessageAdapter extends BaseAdapter {

	private Context mContext;
	private List<MessageInfo> myList;
	private LayoutInflater mInflater;
	public MessageAdapter(Context context,List<MessageInfo> list) {
		this.mContext = context;
		this.myList = list;
		mInflater = LayoutInflater.from(mContext);
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
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.message_listview_item, null);
		}
		ImageView iv=(ImageView) convertView.findViewById(R.id.message_item_iv);
		TextView tvContent=(TextView) convertView.findViewById(R.id.message_item_tv_content);
		TextView tvDate=(TextView) convertView.findViewById(R.id.message_item_tv_date);
//		if(myList.get(position).isRead()){
//			iv.setBackgroundResource(R.drawable.sys_isread);
//			tvContent.setTextColor(mContext.getResources().getColor(R.color.sys_isread_text));
//			tvDate.setTextColor(mContext.getResources().getColor(R.color.sys_isread_text));
//		}else{
//			iv.setBackgroundResource(R.drawable.sys_noread);
//			tvContent.setTextColor(mContext.getResources().getColor(R.color.sys_noread_text_black));
//			tvDate.setTextColor(mContext.getResources().getColor(R.color.sys_noread_text_gray));
//		}
		tvContent.setText(myList.get(position).getContent());
		tvDate.setText(Constant.getDateFormat2(myList.get(position).getDate()));
		return convertView;
	}


	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}













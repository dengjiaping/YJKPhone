package com.sinosoft.fhcs.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.entity.ChangeDeviceInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChangeDeviceListAdapter extends BaseAdapter {


	private Context c;
	private LayoutInflater inflater;
	List<ChangeDeviceInfo> infos = new ArrayList<ChangeDeviceInfo>();

	public ChangeDeviceListAdapter(Context c,List<ChangeDeviceInfo> devices) {
		// TODO Auto-generated constructor stub
		this.c = c;
		inflater = LayoutInflater.from(c);
		setDevice(devices);

	}
	public void setDevices(List<ChangeDeviceInfo> d){
		setDevice(d);
		notifyDataSetChanged();
	}

	private void setDevice(List<ChangeDeviceInfo> devices) {
		// TODO Auto-generated method stub
		if(devices != null && devices.size() != 0){
			infos.clear();
			infos.addAll(devices);
		}else{
			System.out.println("设备列表为空");
		}

	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public ChangeDeviceInfo getItem(int arg0) {
		return infos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(v == null){
			v = inflater.inflate(R.layout.change_device_listview_item, null);
			holder = new ViewHolder(v);
			v.setTag(holder);
		}else {
			holder = (ViewHolder) v.getTag();
		}
		ChangeDeviceInfo changeDeviceInfo = infos.get(arg0);
		holder.setBuild(changeDeviceInfo);

		return v;
	}

	class ViewHolder{
		ImageView iv;
		TextView dName;
		TextView dTime;
		public ViewHolder(View v) {
			// TODO Auto-generated constructor stub
			iv = (ImageView) v.findViewById(R.id.equiplistview_item_img_change);
			dName = (TextView) v.findViewById(R.id.equiplistview_item_tv_name_change);
			dTime = (TextView) v.findViewById(R.id.equiplistview_item_tv_time_change);
		}
		public void setBuild(ChangeDeviceInfo changeDeviceInfo) {
			// TODO Auto-generated method stub
			String deviceName = changeDeviceInfo.getDeviceName();
			if(deviceName.equals("康康血压计")){
				ImageLoader.getInstance().displayImage("drawable://"+R.drawable.kangkang, iv);
			}else if(deviceName.equals("天天血压计")){
				ImageLoader.getInstance().displayImage("drawable://"+R.drawable.tiantian, iv);
			}else if(deviceName.equals("鱼跃血压计")){
				ImageLoader.getInstance().displayImage("drawable://"+R.drawable.yuwell, iv);
			}
			if(changeDeviceInfo.getIsSelect() == 1){
				dName.setText(""+deviceName+"(当前默认设备)");
			}else{
				dName.setText(""+deviceName);
			}
			Date date = changeDeviceInfo.getLastTime();
			if(date != null ){
				dTime.setText(""+dateToString(date));
			}else {
				dTime.setText("暂未使用过该设备");

			}

		}
	}

	private String dateToString(Date d){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(d);
	}

}

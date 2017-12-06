package com.sinosoft.fhcs.android.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.entity.ChangeDeviceInfo;

import java.util.ArrayList;
import java.util.List;

import cn.funtalk.miao.lib.fresco.MSmartCustomView;
import cn.miao.lib.model.BindDeviceBean;

import static com.sinosoft.fhcs.android.util.Constant.device_shoubiao;
import static com.sinosoft.fhcs.android.util.Constant.device_shouhuan;
import static com.sinosoft.fhcs.android.util.Constant.device_shuimian;
import static com.sinosoft.fhcs.android.util.Constant.device_tiwen;
import static com.sinosoft.fhcs.android.util.Constant.device_tizhi;
import static com.sinosoft.fhcs.android.util.Constant.device_tizhicheng;
import static com.sinosoft.fhcs.android.util.Constant.device_tizhong;
import static com.sinosoft.fhcs.android.util.Constant.device_xuetang;
import static com.sinosoft.fhcs.android.util.Constant.device_xuetangyi;
import static com.sinosoft.fhcs.android.util.Constant.device_xueya;
import static com.sinosoft.fhcs.android.util.Constant.device_xueyaji;
import static com.sinosoft.fhcs.android.util.Constant.device_yundong;

public class ChangeDeviceAdapter extends CommonAdapter<BindDeviceBean> {


	private int flages;
	private ChangeDeviceInfo currentDevice;

	public ChangeDeviceAdapter(Context context, List<BindDeviceBean> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		if(mDatas == null){
			return;
		}
	}

	@Override
	public void convert(ViewHolder holder, BindDeviceBean item, int postion) {
		setData2view(holder, item);
	}

	private void setData2view(ViewHolder holder, BindDeviceBean item) {
		String uri = item.getLogo();
		if(!TextUtils.isEmpty(uri)){
			MSmartCustomView view = holder.getView(R.id.image);
			view.setImageURI(uri);
		}
		holder.setText(R.id.tv_item_name,item.getDevice_name());
		int link_type = item.getLink_type();
		if(link_type == 1){
			holder.setText(R.id.tv_item_type,"蓝牙设备");
		}else if(link_type == 2){
			holder.setText(R.id.tv_item_type,"API设备");
		}else if(link_type == 3){
			holder.setText(R.id.tv_item_type,"二维码设备");
		}
		if(currentDevice != null && currentDevice.getDeviceNo().equals(item.getDevice_no())){
			holder.setViewVisible(R.id.unbind,true);
			holder.setText(R.id.unbind,"当前正在使用的设备");
		}else{
			holder.setViewVisible(R.id.unbind,false);
		}
	}

	public void setBindDeviceBeans(ArrayList<BindDeviceBean> bindDeviceBeans) {
		if(bindDeviceBeans == null){
			return;
		}
		List<BindDeviceBean> deviceBeanList = new ArrayList<>();
		for (BindDeviceBean bindDeviceBean : bindDeviceBeans) {
			switch (flages) {
				case  device_tizhong :
					if(bindDeviceBean.getType_id() == device_tizhicheng){
						deviceBeanList.add(bindDeviceBean);
					}
					break;
				case device_tiwen :
					if(bindDeviceBean.getType_id() == device_tiwen){
						deviceBeanList.add(bindDeviceBean);
					}
					break;
				case device_shuimian :
					if(bindDeviceBean.getType_id() == device_shouhuan){
						deviceBeanList.add(bindDeviceBean);
					}
					break;
				case device_xueya :
					if(bindDeviceBean.getType_id() == device_xueyaji)
						deviceBeanList.add(bindDeviceBean);

					break;
				case device_xuetang :
					if(bindDeviceBean.getType_id() == device_xuetangyi){
						deviceBeanList.add(bindDeviceBean);

					}
					break;
				case device_yundong:
					if(bindDeviceBean.getType_id() == device_shoubiao || bindDeviceBean.getType_id() == device_shouhuan){
						deviceBeanList.add(bindDeviceBean);

					}
					break;
				case device_tizhi :
					if(bindDeviceBean.getType_id() == device_tizhicheng){
						deviceBeanList.add(bindDeviceBean);
					}
					break;
			}
		}
		mDatas.clear();
		this.mDatas.addAll(deviceBeanList);
		this.notifyDataSetChanged();
	}

	public void setFlages(int flages) {
		this.flages = flages;
	}

	//设置当前的默认设备
	public void setcurrentDevice(ChangeDeviceInfo currentDevice) {
		this.currentDevice = currentDevice;
	}
}

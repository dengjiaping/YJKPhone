package com.sinosoft.fhcs.android.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.sinosoft.fhcs.android.R;

import java.util.ArrayList;
import java.util.List;

import cn.funtalk.miao.lib.fresco.MSmartCustomView;
import cn.miao.lib.model.DeviceBean;

/**
 * 作者：shuiq_000 on 2017/9/24 13:06
 * 邮箱：2028318192@qq.com
 */

public class DeviceListAdapter extends  CommonAdapter<DeviceBean> {

    public DeviceListAdapter(Context context, List<DeviceBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        if(mDatas == null){
            return;
        }
    }

    @Override
    public void convert(ViewHolder holder, DeviceBean item, int postion) {
        String uri = item.getLogo();
        if(!TextUtils.isEmpty(uri)){
            MSmartCustomView view = holder.getView(R.id.image);
            view.setImageURI(uri);
        }
        holder.setText(R.id.tv_item_name,item.getDevice_name());
        holder.setText(R.id.tv_item_desc,item.getDevice_des());
        int link_type = item.getLink_type();
        if(link_type == 1){
            holder.setText(R.id.tv_item_type,item.getDevice_des()+"\n已有" + item.getBindnum()+"绑定");
        }else if(link_type == 2){
            holder.setText(R.id.tv_item_type,"API设备"+item.getDevice_des()+"\n已有" + item.getBindnum()+"绑定");
        }else if(link_type == 3){
            holder.setText(R.id.tv_item_type,"二维码设备"+item.getDevice_des()+"\n已有" + item.getBindnum()+"绑定");
        }
    }

    public void setList(ArrayList<DeviceBean> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }
}

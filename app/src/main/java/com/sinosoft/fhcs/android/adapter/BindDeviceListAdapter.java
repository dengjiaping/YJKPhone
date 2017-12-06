package com.sinosoft.fhcs.android.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.entity.ChangeDeviceInfo;
import com.sinosoft.fhcs.android.entity.MeasureMainInfo;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import cn.funtalk.miao.lib.fresco.MSmartCustomView;
import cn.miao.lib.MiaoApplication;
import cn.miao.lib.listeners.MiaoUnBindListener;
import cn.miao.lib.model.BindDeviceBean;

import static com.sinosoft.fhcs.android.ExitApplicaton.getList;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_SHUIMIAN;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_TIWEN;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_TIZHI;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_TIZHONG;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_XUETANG;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_XUEYA;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_YUNDONG;
import static com.sinosoft.fhcs.android.util.Constant.device_shoubiao;
import static com.sinosoft.fhcs.android.util.Constant.device_shouhuan;
import static com.sinosoft.fhcs.android.util.Constant.device_tiwen;
import static com.sinosoft.fhcs.android.util.Constant.device_tizhicheng;
import static com.sinosoft.fhcs.android.util.Constant.device_xuetangyi;
import static com.sinosoft.fhcs.android.util.Constant.device_xueyaji;

/**
 * 作者：shuiq_000 on 2017/9/25 16:24
 * 邮箱：2028318192@qq.com
 */

public abstract class BindDeviceListAdapter extends CommonAdapter<BindDeviceBean> {

    private static List<BindDeviceBean> mDatas;
    private final Handler handler;
    private ProgressDialog progressDialog;

    public BindDeviceListAdapter(Context context, List<BindDeviceBean> mDatas, int itemLayoutId, Handler handler) {
        super(context, mDatas, itemLayoutId);
        this.mContext = context;
        this.handler = handler;
        if(mDatas == null){
            return;
        }
        this.mDatas = mDatas;
    }

    @Override
    public void convert(final ViewHolder holder, final BindDeviceBean item, int postion) {
        String uri = item.getLogo();
        if(!TextUtils.isEmpty(uri)){
            MSmartCustomView view = holder.getView(R.id.image);
            view.setImageURI(uri);
        }
        holder.setText(R.id.tv_item_name,item.getDevice_name());
        holder.setText(R.id.tv_item_desc,item.getDevice_no());
        holder.setViewVisible(R.id.unbind,false);
        int link_type = item.getLink_type();
        if(link_type == 1){
            holder.setText(R.id.tv_item_type,"蓝牙设备\n" + item.getFunction_info().get(0).getFunctional_name());
        }else if(link_type == 2){
            holder.setText(R.id.tv_item_type,"API设备\n" + item.getFunction_info().get(0).getFunctional_name());
        }else if(link_type == 3){
            holder.setText(R.id.tv_item_type,"二维码设备\n" + item.getFunction_info().get(0).getFunctional_name());
        }
        holder.setOnClickListener(R.id.btn_unbind, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeMenuLayout view = holder.getView(R.id.swipeMenuLayout);
//                unBindDevice(item.getDevice_sn(),item.getDevice_no(),view);
                unBindDevice(item,view);
            }
        });


    }



    public abstract void refresh(SwipeMenuLayout view);

    public static void setBindDeviceBeans(ArrayList<BindDeviceBean> mList) {
        mDatas = mList;
    }
    private void unBindDevice(final BindDeviceBean item, final SwipeMenuLayout view) {
        if(getList == null){
            FRToast.showToast(mContext.getApplicationContext(),"家庭列表为空");
            return;
        }
        if (!TextUtils.isEmpty(item.getDevice_no())) {
            progressDialog = new ProgressDialog(mContext);
            Constant.showProgressDialog(progressDialog);
            MiaoApplication.getMiaoHealthManager().unbindDevice(item.getDevice_sn(), item.getDevice_no(), new MiaoUnBindListener() {
                @Override
                public void onUnBindResponse(int unbindStatus) {
                    Constant.exitProgressDialog(progressDialog);
                    if (unbindStatus == 1) {
                        if(mContext != null){
                            refresh(view);
                            clearSPDevice(item);//删除sp里边保存的数据
                            CommonUtil.sendMessage(handler,2,"设备解除绑定成功");
//                            FRToast.showToast(mContext,"设备解除绑定成功");
                        }
                    } else{
                        if(mContext != null){
                            CommonUtil.sendMessage(handler,0,"设备解除绑定失败");
//                            FRToast.showToast(mContext,"设备解除绑定失败");
                        }
                    }
                }
                @Override
                public void onError(int code, String msg) {
                    Constant.exitProgressDialog(progressDialog);
                    if(mContext != null){
                        CommonUtil.sendMessage(handler,0,"设备解除绑定失败"+ msg);
//                        FRToast.showToast(mContext,"解除绑定失败" + msg);
                    }
                }
            });
        } else {
            if(mContext != null){
                CommonUtil.sendMessage(handler,0,"message");
//                FRToast.showToast(mContext,"设备未绑定");
            }
        }
    }

    private void clearSPDevice(BindDeviceBean bindDeviceBean) {
        SPUtil spUtil =SPUtil.getInstance(mContext);
        ChangeDeviceInfo changeDeviceInfo = null;
        List<String> memberIdList = new ArrayList<>();
        for (MeasureMainInfo measureMainInfo : getList) {
            memberIdList.add(measureMainInfo.getMemberId());
        }
        switch (bindDeviceBean.getType_id()) {
            case  device_tizhicheng :
                changeDeviceInfo  = spUtil.getObj(SP_BLOOD_SELECT_TIZHONG, null);
                if(changeDeviceInfo != null &&
                        changeDeviceInfo.getDeviceNo().equals(bindDeviceBean.getDevice_no()) &&
                        changeDeviceInfo.getDeviceSn().equals(bindDeviceBean.getDevice_sn())){//将符合条件的数据从sp中删除
                    spUtil.remove(SP_BLOOD_SELECT_TIZHONG);
                }else{
                    changeDeviceInfo  = spUtil.getObj(SP_BLOOD_SELECT_TIZHI, null);
                    if(changeDeviceInfo != null &&
                            changeDeviceInfo.getDeviceNo().equals(bindDeviceBean.getDevice_no()) &&
                            changeDeviceInfo.getDeviceSn().equals(bindDeviceBean.getDevice_sn())){//将符合条件的数据从sp中删除
                        spUtil.remove(SP_BLOOD_SELECT_TIZHI);
                    }
                }

                break;
            case device_tiwen :
                changeDeviceInfo  = spUtil.getObj(SP_BLOOD_SELECT_TIWEN, null);
                if(changeDeviceInfo != null &&
                        changeDeviceInfo.getDeviceNo().equals(bindDeviceBean.getDevice_no()) &&
                        changeDeviceInfo.getDeviceSn().equals(bindDeviceBean.getDevice_sn())){//将符合条件的数据从sp中删除
                    spUtil.remove(SP_BLOOD_SELECT_TIWEN);
                }
                break;
            case device_shouhuan :
                for (String s : memberIdList) {
                    changeDeviceInfo  = spUtil.getObj(s+SP_BLOOD_SELECT_SHUIMIAN, null);
                    if(changeDeviceInfo != null &&
                            changeDeviceInfo.getDeviceNo().equals(bindDeviceBean.getDevice_no()) &&
                            changeDeviceInfo.getDeviceSn().equals(bindDeviceBean.getDevice_sn())){//将符合条件的数据从sp中删除
                        spUtil.remove(s+SP_BLOOD_SELECT_SHUIMIAN);
                    }
                    if(spUtil.getObj(s+SP_BLOOD_SELECT_YUNDONG, null) != null){
                        changeDeviceInfo = spUtil.getObj(s+SP_BLOOD_SELECT_YUNDONG, null);
                        if(changeDeviceInfo != null &&
                                changeDeviceInfo.getDeviceNo().equals(bindDeviceBean.getDevice_no()) &&
                                changeDeviceInfo.getDeviceSn().equals(bindDeviceBean.getDevice_sn())){//将符合条件的数据从sp中删除
                            spUtil.remove(s+SP_BLOOD_SELECT_YUNDONG);
                        }

                    }
                }

                break;
            case device_xueyaji :
                changeDeviceInfo  = spUtil.getObj(SP_BLOOD_SELECT_XUEYA, null);
                if(changeDeviceInfo != null &&
                        changeDeviceInfo.getDeviceNo().equals(bindDeviceBean.getDevice_no()) &&
                        changeDeviceInfo.getDeviceSn().equals(bindDeviceBean.getDevice_sn())){//将符合条件的数据从sp中删除
                    spUtil.remove(SP_BLOOD_SELECT_XUEYA);
                }
                break;
            case device_xuetangyi :
                if(bindDeviceBean.getType_id() == device_xuetangyi){
                    changeDeviceInfo  = spUtil.getObj(SP_BLOOD_SELECT_XUETANG, null);
                    if(changeDeviceInfo != null &&
                            changeDeviceInfo.getDeviceNo().equals(bindDeviceBean.getDevice_no()) &&
                            changeDeviceInfo.getDeviceSn().equals(bindDeviceBean.getDevice_sn())){//将符合条件的数据从sp中删除
                        spUtil.remove(SP_BLOOD_SELECT_XUETANG);
                    }
                }
                break;
            case device_shoubiao:
                changeDeviceInfo  = spUtil.getObj(SP_BLOOD_SELECT_YUNDONG, null);
                if(changeDeviceInfo != null &&
                        changeDeviceInfo.getDeviceNo().equals(bindDeviceBean.getDevice_no()) &&
                        changeDeviceInfo.getDeviceSn().equals(bindDeviceBean.getDevice_sn())){//将符合条件的数据从sp中删除
                    spUtil.remove(SP_BLOOD_SELECT_YUNDONG);
                }
                break;
        }

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        SwipeMenuLayout swipeMenuLayout = new SwipeMenuLayout(mContext);
        swipeMenuLayout.quickClose();
    }
}

package com.sinosoft.fhcs.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.entity.DoctorBean;
import com.sinosoft.fhcs.android.util.LoadLocalImageUtil;

public class DoctorAdapter extends BaseAdapter{
    

    private Context context;
    private LayoutInflater inflater;
    private List<DoctorBean> mDatas;
    private LoadLocalImageUtil instance;

    public DoctorAdapter(Context context,List<DoctorBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
        instance = LoadLocalImageUtil.getInstance();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDatas.size();
    }

    @Override
    public DoctorBean getItem(int position) {
        // TODO Auto-generated method stub
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_department_details_doctor_list, null);
            holder=new ViewHolder(convertView); 
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        DoctorBean doctorBean = mDatas.get(position);
        holder.setData(doctorBean);
        return convertView;
    }
    
    class ViewHolder{
        ImageView ivHead;
        TextView tvDoctorName;
        TextView tvDoctorJob;
        TextView tvDoctorGood;
        
        public ViewHolder(View view) {
            // TODO Auto-generated constructor stub
            ivHead = (ImageView) view.findViewById(R.id.iv_doctor_head);
            tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
            tvDoctorJob = (TextView) view.findViewById(R.id.tv_doctor_job);
            tvDoctorGood = (TextView) view.findViewById(R.id.tv_doctor_good_body);
        }

        public void setData(DoctorBean doctorBean) {
            // TODO Auto-generated method stub
            tvDoctorName.setText(doctorBean.getDoctorName());
            tvDoctorJob.setText(doctorBean.getDoctorJob());
            tvDoctorGood.setText(doctorBean.getDoctorGood());
            switch (doctorBean.getdId()) {
            case 0:
                instance.displayFromDrawable(R.drawable.head_1, ivHead);
                break;
            case 1:
                instance.displayFromDrawable(R.drawable.head_2, ivHead);
                break;
            case 2:
                instance.displayFromDrawable(R.drawable.head_3, ivHead);
                break;
            case 3:
                instance.displayFromDrawable(R.drawable.head_4, ivHead);
                break;
            case 4:
                instance.displayFromDrawable(R.drawable.head_5, ivHead);
                break;
            case 5:
                instance.displayFromDrawable(R.drawable.head_6, ivHead);
                break;
            default:
                break;
            }
        }

    }

}

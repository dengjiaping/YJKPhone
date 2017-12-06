package com.sinosoft.fhcs.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;

import com.sinosoft.fhcs.android.activity.devicframgne.DeviceListFragment;

import java.util.ArrayList;
import java.util.List;

import cn.miao.lib.model.DeviceTypeBean;

/**
 * 作者：shuiq_000 on 2017/9/24 12:45
 * 邮箱：2028318192@qq.com
 */

public class DeviceFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "MyFragmentPagerAdapter";
    private final FragmentManager fm;
    // private ArrayList<OpilistItem> lists;
    private List<Fragment> mList;
    private Context mContext;
    private ArrayList<DeviceTypeBean> deviceTypes;

    public DeviceFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        this.mContext = context;
        mList = new ArrayList<Fragment>();
    }

    public void setLists(List<Fragment> lists, ArrayList<DeviceTypeBean> deviceTypes) {
        if(this.mList != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.mList){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }

        this.mList = lists;
        this.deviceTypes = deviceTypes;
    }

    public void UpdateList(List<DeviceListFragment> arrayList) {
        this.mList.clear();
        this.mList.addAll(arrayList);

        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return deviceTypes.get(position).getType_name();
    }

    @Override
    public Fragment getItem(int arg0) {
        // Fragment ft = null;
        //
        // ft = new DateShow(mContext);
        // for (int i = 0; i < lists.get(arg0).openplayitem.size(); i++) {
        // Log.i(TAG,
        // "arg0=" + arg0 + "name="
        // + lists.get(arg0).openplayitem.get(i).moviename);
        // }
        //
        // Bundle args = new Bundle();
        // args.putSerializable("date_title", lists.get(arg0).openplayitem);
        // ft.setArguments(args);
        // return ft;
        return mList.get(arg0);
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }




}

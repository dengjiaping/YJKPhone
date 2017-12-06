package com.sinosoft.fhcs.android.activity.bottomfragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.MainActivity;
import com.sinosoft.fhcs.android.activity.devicframgne.DeviceListFragment;
import com.sinosoft.fhcs.android.adapter.DeviceFragmentPagerAdapter;
import com.sinosoft.fhcs.android.customview.ChildViewPager;
import com.sinosoft.fhcs.android.entity.TabEntity;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.FRToast;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.miao.lib.MiaoApplication;
import cn.miao.lib.listeners.MiaoDeviceTypeListener;
import cn.miao.lib.model.DeviceTypeBean;

/**
 * 作者：shuiq_000 on 2017/9/22 16:31
 * 邮箱：2028318192@qq.com
 */

public class DeviceFragment extends Fragment {

    @BindView(R.id.titlebar_btn_back)
    Button titlebarBtnBack;
    @BindView(R.id.titlebar_tv_title)
    TextView titlebarTvTitle;
    @BindView(R.id.dev_ctb)
    SlidingTabLayout devCtb;
    Unbinder unbinder;
    @BindView(R.id.vp_device)
    ChildViewPager vpDevice;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<DeviceTypeBean> deviceTypes;
    private MainActivity mActivity;
    private DeviceFragmentPagerAdapter deviceFragmentPagerAdapter;
    private View view;
    private ProgressDialog progressDialog;
    List<Fragment> deviceListFragmentList = new ArrayList<>();


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null && activity instanceof MainActivity){
            this.mActivity = (MainActivity) activity;
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("TAG", "handleMessage: " + msg.toString());
            switch (msg.what) {
                case 0:
                    FRToast.showToastSafe(String.valueOf(msg.obj));
                    break;
                case 1:
                    // FRToast.showToastSafe("DeviceFragment设备类型列表获取成功" + deviceTypes);
                    if (mTabEntities != null) {
                        mTabEntities.clear();
                    }
                    if(deviceListFragmentList != null){
                        deviceListFragmentList.clear();
                    }
                    DeviceListFragment deviceListFragment = null;
                    for (int i = 0; i < deviceTypes.size(); i++) {
                        mTabEntities.add(new TabEntity(deviceTypes.get(i).getType_name(), 0, 0));
                        deviceListFragment = new DeviceListFragment(deviceTypes.get(i).getId());
                        deviceListFragmentList.add(deviceListFragment);
                    }
                    if(devCtb == null){
                        return;
                    }
                    initViewpager(deviceListFragmentList);
//                    vpDevice.setAdapter(new DeviceFragmentPagerAdapter(getActivity().getSupportFragmentManager()));
                    devCtb.setViewPager(vpDevice);
                    devCtb.setOnTabSelectListener(new OnTabSelectListener() {
                        @Override
                        public void onTabSelect(int position) {
                            vpDevice.setCurrentItem(position);
                        }

                        @Override
                        public void onTabReselect(int position) {
                        }
                    });
                    /*adapter = new GvDeviceTypeAdapter(deviceTypes,DeviceTypeListActivity.this);
                    if( gv_device_type != null) {
                        gv_device_type.setAdapter(adapter);
                    }*/
                    break;
                case 2:
                    break;
            }

        }


    };
    /*private class DeviceFragmentPagerAdapter extends FragmentPagerAdapter {
        public DeviceFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return deviceListFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return deviceTypes.get(position).getType_name();
        }

        @Override
        public Fragment getItem(int position) {
            return deviceListFragmentList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }
    }*/

    private void initViewpager(List<Fragment> deviceListFragmentList) {
        if(deviceListFragmentList != null){
            if(deviceFragmentPagerAdapter != null){
                deviceFragmentPagerAdapter.setLists(deviceListFragmentList,deviceTypes);
                deviceFragmentPagerAdapter.notifyDataSetChanged();
            }
             deviceFragmentPagerAdapter = new DeviceFragmentPagerAdapter(getActivity().getSupportFragmentManager(), mActivity);
            deviceFragmentPagerAdapter.setLists(deviceListFragmentList,deviceTypes);
            deviceFragmentPagerAdapter.notifyDataSetChanged();
            vpDevice.setAdapter(deviceFragmentPagerAdapter);
            vpDevice.setCurrentItem(0);
            vpDevice.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    changeSlidingMenuTOUCHMODE(position);
                    devCtb.setCurrentTab(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }
    /** 解决ViewPager和侧滑冲突 */
    public void changeSlidingMenuTOUCHMODE(int arg0) {
        switch (arg0) {
            case 0:// 当当天条目是0的时候，设置可以在任意位置拖拽出SlidingMenu
                if (getActivity() instanceof SlidingActivity) {
                    SlidingActivity activity = (SlidingActivity) getActivity();
                    activity.getSlidingMenu().setTouchModeAbove(
                            SlidingMenu.TOUCHMODE_FULLSCREEN);
                }
                break;
            default:// 当在其他位置的时候，设置不可以拖拽出来(SlidingMenu.TOUCHMODE_NONE)，或只有在边缘位置才可以拖拽出来TOUCHMODE_MARGIN
                if (getActivity() instanceof SlidingActivity) {
                    SlidingActivity activity = (SlidingActivity) getActivity();
                    activity.getSlidingMenu().setTouchModeAbove(
                            SlidingMenu.TOUCHMODE_NONE);
                }
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //vpDevice.setCurrentItem(0);
        MobclickAgent.onPageEnd("设备列表页");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        /*if(deviceTypes != null){
            deviceTypes.clear();
        }
        if(deviceFragmentPagerAdapter != null){
            deviceFragmentPagerAdapter = null;
        }*/
    }

    private void initView() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserInfo",
                Context.MODE_PRIVATE);
        String phoneStr = prefs.getString("phoneNumber", "");
        if(phoneStr == null){
            FRToast.showToast(getActivity().getApplicationContext(),"用户未绑定手机号，暂不能使用设备测量");
            return;
        }
        progressDialog = new ProgressDialog(getActivity());
        Constant.showProgressDialog(progressDialog);
        titlebarTvTitle.setText(R.string.equipmentlist_tv1);
        MiaoApplication.getMiaoHealthManager().fetchDeviceTypeList(new MiaoDeviceTypeListener() {
            @Override
            public void onDeviceTyapeResponse(final ArrayList<DeviceTypeBean> arrayList) {
                Constant.exitProgressDialog(progressDialog);
                //设备类型列表获取成功
                if (deviceTypes != null) {
                    deviceTypes.clear();
                }

                if (arrayList != null) {
                    deviceTypes = arrayList;
                }
                sendMessage(1, "");
            }

            @Override
            public void onError(int code, String s) {
                Constant.exitProgressDialog(progressDialog);
                //设备类型列表获取成功
                FRToast.showToastSafe("DeviceFragment设备类型列表获取成功");
                sendMessage(0, "设备类型列表获取失败 code：" + code + " msg:" + s);
            }
        });
    }

    private void sendMessage(int i, String s) {
        Message message = new Message();
        message.what = i;
        message.obj = s;
        handler.sendMessage(message);
    }

    @OnClick(R.id.titlebar_btn_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {

        }
    }

}

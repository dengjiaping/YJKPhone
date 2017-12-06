package com.sinosoft.fhcs.android.activity.devicframgne;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.BindDeviceActivity;
import com.sinosoft.fhcs.android.adapter.DeviceListAdapter;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.view.PullToRefreshBase;
import com.sinosoft.fhcs.android.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.miao.lib.MiaoApplication;
import cn.miao.lib.listeners.MiaoDeviceListListener;
import cn.miao.lib.model.DeviceBean;
import cn.miao.lib.model.DeviceListBean;

/**
 * 作者：shuiq_000 on 2017/9/22 16:31
 * 邮箱：2028318192@qq.com
 */

public class DeviceListFragment extends Fragment implements AdapterView.OnItemClickListener{

    @BindView(R.id.lv_device_type_list)
    PullToRefreshListView pullListView;
    Unbinder unbinder;
    private int type_id;
    private int page_no = 1;
    protected DeviceListBean deviceListBean;
    private int totalPage = 0;
    private ArrayList<DeviceBean> mList;
    private DeviceListAdapter deviceListAdapter;
    private ListView lvList;
    private View view;
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private int selection;//下拉位置记录

    public DeviceListFragment() {
    }

    @SuppressLint("ValidFragment")
    public DeviceListFragment(int id) {
        this.type_id = id;
    }
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("TAG", "handleMessage: " + msg.toString());
            switch (msg.what) {
                case 0:
                    FRToast.showToast(getActivity(), String.valueOf(msg.obj));
                    break;
                case 1:
                    if (deviceListBean != null)
                        deviceListAdapter.setList(mList);
                    deviceListAdapter.notifyDataSetChanged();
                    break;
               /* case 2:
                    Intent intent = new Intent(DeviceListActivity.this, CaptureActivity.class);
                    intent.putExtra("device_sn", String.valueOf(msg.obj));
                    startActivityForResult(intent, RESULT_CODE_SCAN_CODE);
                    break;*/
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if( view == null){
            view = inflater.inflate(R.layout.fragment_device_list, container, false);
        }
        /**
         * 防止预加载下一页出现空指针异常
         */
     /*   if (mListItems != null) {
            Log.i("TAG", mListItems.size()+"");
            for (int i = 0; i < mListItems.size(); i++) {
                Log.i("TAG", mListItems.get(i).moviename);
            }
            mAdapter = new DeviceListAdapter(this.getActivity().getApplicationContext(),mListItems,R.layout.item_list);
            mListView.setAdapter(mAdapter);
            mAdapter.UpdateOpenPlays(mListItems);
            setListViewHeightBasedOnChildren(mListView);
        }*/
        unbinder = ButterKnife.bind(this, view);
        initAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageEnd("设备列表页详细页");
    }

    private void initAdapter() {
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 设置时间显示的格式
                String format = FORMAT_DATE_TIME;
                pullListView.setLastUpdatedLabel(getCurrentTime(format));
                page_no = 1;
                getDeviceList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_no++;
                if(mList != null){
                    selection = mList.size();
                }
                Log.e("TAG", "page_no: " +page_no+"      totalPage: " +totalPage);
                if(page_no > totalPage){
                    FRToast.showToastSafe("已加载全部");
                    pullListView.onPullUpRefreshComplete();
                }else {
                    getDeviceList();
                }
            }
        });
        lvList = pullListView.getRefreshableView();
        lvList.setVerticalScrollBarEnabled(false);
        lvList.setCacheColorHint(0);
        lvList.setDivider(getResources().getDrawable(R.color.transparent));
        lvList.setSelector(R.color.transparent);
        lvList.setDividerHeight(15);
        lvList.setOnItemClickListener(this);
        mList = new ArrayList<DeviceBean>();
        deviceListAdapter = new DeviceListAdapter(getActivity(),mList,R.layout.item_list);
        lvList.setAdapter(deviceListAdapter);
        getDeviceList();
    }
    public String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    /**
     * 根据类型获取设备列表
     */
    private void getDeviceList() {
        if (MiaoApplication.getMiaoHealthManager() == null) return;
        MiaoApplication.getMiaoHealthManager().fetchDeviceList(type_id, page_no, new MiaoDeviceListListener() {
            @Override
            public void onDeviceLisResponse(DeviceListBean deviceList) {
                if (deviceList != null)
                    deviceListBean = deviceList;
                ArrayList<DeviceBean> list = deviceListBean.getData();
                totalPage = deviceListBean.getTotal_page();
                if (list != null || list.size() > 0) {
                    if (page_no == 1) {
                        mList.clear();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        mList.add(list.get(i));
                    }
                }
                if(pullListView != null){
                    pullListView.onPullDownRefreshComplete();
                    pullListView.onPullUpRefreshComplete();

                }
                sendMessage(1, "");
            }

            @Override
            public void onError(int code, String msg) {
                sendMessage(0, "设备类型列表获取失败 code：" + code + " msg:" + msg);
                if(pullListView != null) {
                    pullListView.onPullDownRefreshComplete();
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        /*if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }*/
    }
    private void sendMessage(int what, String msg) {
        Message message = new Message();
        message.what = what;
        message.obj = msg;
        handler.sendMessage(message);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), BindDeviceActivity.class);
        intent.putExtra("deviceBean",deviceListAdapter.getItem(position));
        startActivity(intent);
    }
}

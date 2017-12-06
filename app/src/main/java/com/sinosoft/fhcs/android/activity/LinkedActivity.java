package com.sinosoft.fhcs.android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.BindDeviceListAdapter;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.view.PullToRefreshBase;
import com.sinosoft.fhcs.android.view.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.miao.lib.MiaoApplication;
import cn.miao.lib.listeners.MiaoUserDeviceListListener;
import cn.miao.lib.model.BindDeviceBean;
import cn.miao.lib.model.BindDeviceListBean;

import static com.sinosoft.fhcs.android.activity.devicframgne.DeviceListFragment.FORMAT_DATE_TIME;
import static com.sinosoft.fhcs.android.util.CommonUtil.getCurrentTime;

/**
 * 已绑定设备管理
 */
public class LinkedActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.titlebar_btn_back)
    Button titlebarBtnBack;
    @BindView(R.id.titlebar_tv_title)
    TextView titlebarTvTitle;
    @BindView(R.id.lv_device_type_list)
    PullToRefreshListView pullListView;
    private ListView lvList;
    private int page_no = 1;
    private int totalPage = 0;
    protected BindDeviceListBean bindDeviceListBean;
    private ArrayList<BindDeviceBean> mList;
    private BindDeviceListAdapter bindDeviceListAdapter;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("tag", "handleMessage: " + msg.toString());
            switch (msg.what) {
                case 0:
                    FRToast.showToast(LinkedActivity.this.getApplicationContext(),String.valueOf(msg.obj));
                    break;
                case 1:
                    if (bindDeviceListBean != null)
                        bindDeviceListAdapter.setBindDeviceBeans(mList);
                    bindDeviceListAdapter.notifyDataSetChanged();
                    break;
                case 2://解除绑定成功后刷新
                    FRToast.showToast(LinkedActivity.this.getApplicationContext(),String.valueOf(msg.obj));
                    page_no = 1;
                    getMyDeviceList(page_no);
                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setUpViewAndData() {
        setContentView(R.layout.activity_linked);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
        getMyDeviceList(page_no);
        MobclickAgent.onPageStart("已绑定设备列表页面"); // 统计页面
    }
    private void initData() {
        mList = new ArrayList<BindDeviceBean>();
        bindDeviceListAdapter = new BindDeviceListAdapter(LinkedActivity.this, mList, R.layout.item_isbind_list,handler) {
            @Override
            public void refresh(final SwipeMenuLayout view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(view != null){
                            view.quickClose();
                        }
                    }
                });
            }
        };
        lvList.setAdapter(bindDeviceListAdapter);
    }

    private void initView() {
        titlebarTvTitle.setText(getString(R.string.set_tv7));
        titlebarTvTitle.setVisibility(View.VISIBLE);
        titlebarBtnBack.setVisibility(View.VISIBLE);
        lvList = pullListView.getRefreshableView();
        lvList.setVerticalScrollBarEnabled(false);
        lvList.setCacheColorHint(0);
        lvList.setDivider(getResources().getDrawable(R.color.transparent));
        lvList.setSelector(R.color.transparent);
        lvList.setDividerHeight(15);
        lvList.setOnItemClickListener(this);

        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 设置时间显示的格式
                String format = FORMAT_DATE_TIME;
                pullListView.setLastUpdatedLabel(getCurrentTime(format));
                page_no = 1;
                getMyDeviceList(page_no);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                page_no++;
                Log.e("tag", "page_no: " +page_no+"      totalPage: " +totalPage);
                if(page_no > totalPage){
                    FRToast.showToast(LinkedActivity.this,"已加载全部");
                    pullListView.onPullUpRefreshComplete();
                }else {
                    getMyDeviceList(page_no);
                }
            }
        });
    }
    private void getMyDeviceList(final int page_no) {
        if(!CommonUtil.isEnabledNetWork(LinkedActivity.this)){
            FRToast.showToast(LinkedActivity.this,"网络连接异常，请稍后再试");
            return;
        }
        if(MiaoApplication.getMiaoHealthManager()==null)return;
        MiaoApplication.getMiaoHealthManager().fetchUserDeviceList(page_no, new MiaoUserDeviceListListener() {
            @Override
            public void onUserDeviceListResponse(BindDeviceListBean bindDeviceList) {
                if (bindDeviceList != null)
                    bindDeviceListBean = bindDeviceList;
                ArrayList<BindDeviceBean> list = bindDeviceListBean.getData();
                totalPage = bindDeviceListBean.getTotal_page();
                if(list != null || list.size() > 0){
                    if(page_no == 1){
                        mList.clear();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        mList.add(list.get(i));
                    }
                }
                pullListView.onPullDownRefreshComplete();
                pullListView.onPullUpRefreshComplete();
                sendMessage(1, "");
            }

            @Override
            public void onError(int code, String msg) {
                sendMessage(0, "设备类型列表获取失败 code：" + code + " msg:" + msg);

            }
        });
    }

    private void sendMessage(int what, String msg) {
        Message message = new Message();
        message.what = what;
        message.obj = msg;
        handler.sendMessage(message);
    }
    @OnClick(R.id.titlebar_btn_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

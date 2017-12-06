package com.sinosoft.fhcs.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.ChangeDeviceAdapter;
import com.sinosoft.fhcs.android.customview.YesOrNoDialog;
import com.sinosoft.fhcs.android.entity.ChangeDeviceInfo;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.util.JsonUtils;
import com.sinosoft.fhcs.android.util.SPUtil;
import com.sinosoft.fhcs.android.util.Util;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.miao.lib.MiaoApplication;
import cn.miao.lib.listeners.MiaoUserDeviceListListener;
import cn.miao.lib.model.BindDeviceBean;
import cn.miao.lib.model.BindDeviceListBean;

import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_SHUIMIAN;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_TIWEN;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_TIZHI;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_TIZHONG;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_XUETANG;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_XUEYA;
import static com.sinosoft.fhcs.android.util.Constant.SP_BLOOD_SELECT_YUNDONG;
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


public class ChangeDeviceListActivity extends BaseActivity implements
		OnClickListener,OnItemClickListener,OnItemLongClickListener{

	private TextView tvTitle;
	private Button btnBack;
	private ListView listview;

	//	private List<ChangeDeviceInfo> deviceInfos;
	private SPUtil spInstance;
	public static int request_change_device_list_activity=10001;
	protected BindDeviceListBean bindDeviceListBean;
	private ChangeDeviceAdapter bindDeviceListAdapter;
	private ArrayList<BindDeviceBean> mList;
	private int page_no = 1;
	private int flags = -1;
	private int totalPage;
	private ChangeDeviceInfo currentDevice;//当前选择的默认设备


	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.e("tag", "handleMessage: " + msg.toString());
			switch (msg.what) {
				case 0:
					FRToast.showToast(ChangeDeviceListActivity.this.getApplicationContext(),String.valueOf(msg.obj));
					break;
				case 1:
					if (bindDeviceListBean != null){
						bindDeviceListAdapter.setcurrentDevice(currentDevice);
						ArrayList<BindDeviceBean> bindDeviceBeenList = new ArrayList<>();
						switch (flags) {
							case  device_tizhong :
								for (BindDeviceBean bindDeviceBean : mList) {
									if(bindDeviceBean.getType_id() == device_tizhicheng){
										bindDeviceBeenList.add(bindDeviceBean);
									}
								}
								break;
							case device_tiwen :
								for (BindDeviceBean bindDeviceBean : mList) {
									if(bindDeviceBean.getType_id() == device_tiwen){
										bindDeviceBeenList.add(bindDeviceBean);
									}
								}
								break;
							case device_shuimian :
								for (BindDeviceBean bindDeviceBean : mList) {
									if(bindDeviceBean.getType_id() == device_shouhuan){
										bindDeviceBeenList.add(bindDeviceBean);
									}
								}

								break;
							case device_xueya :
								for (BindDeviceBean bindDeviceBean : mList) {
									if(bindDeviceBean.getType_id() == device_xueyaji){
										bindDeviceBeenList.add(bindDeviceBean);
									}
								}
								break;
							case device_xuetang :
								for (BindDeviceBean bindDeviceBean : mList) {
									if(bindDeviceBean.getType_id() == device_xuetangyi){
										bindDeviceBeenList.add(bindDeviceBean);
									}
								}
								break;
							case device_yundong:
								for (BindDeviceBean bindDeviceBean : mList) {
									if(bindDeviceBean.getType_id() == device_shouhuan){
										bindDeviceBeenList.add(bindDeviceBean);
									}
								}
								break;
							case device_tizhi :
								for (BindDeviceBean bindDeviceBean : mList) {
									if(bindDeviceBean.getType_id() == device_tizhicheng){
										bindDeviceBeenList.add(bindDeviceBean);
									}
								}
								break;
						}
						if(bindDeviceBeenList != null){
							bindDeviceListAdapter.setBindDeviceBeans(bindDeviceBeenList);
						}
					}
//					bindDeviceListAdapter.notifyDataSetChanged();
					Constant.setListViewHeightBasedOnChildren(listview);// 解决ScrollView和ListView在嵌套使用时冲突的问题
					break;
				case 2:
					break;
			}

		}
	};
	private String memberId;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_change_device_list);
		spInstance = SPUtil.getInstance(this);
		memberId = (String) getIntent().getExtras().get("memberId");
		initView();
		initData();
		showFisrtRun();
	}

	private void initData() {
		mList = new ArrayList<>();
		bindDeviceListAdapter = new ChangeDeviceAdapter(ChangeDeviceListActivity.this.getApplicationContext(),mList,R.layout.item_list);
		bindDeviceListAdapter.setFlages(flags);
		listview.setAdapter(bindDeviceListAdapter);
		Constant.setListViewHeightBasedOnChildren(listview);// 解决ScrollView和ListView在嵌套使用时冲突的问题

	}
	protected void showFisrtRun() {
		// TODO Auto-generated method stub
		boolean appIsFisrstRun = Util.appIsFisrstRun(getApplicationContext(),Constant.APP_IS_FIRSR_RUN_CHANGE);
		if(appIsFisrstRun){
			Intent intent = new Intent(this,MaskingActivity.class);
			intent.putExtra("strTitle", "设备切换");
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		deviceInfos = Constant.getDeviceInfos(this);
//		adapter.setDevices(deviceInfos);
		getMyDeviceList(page_no);
//		Constant.setListViewHeight(listview);// 解决ScrollView和ListView在嵌套使用时冲突的问题
		// ,在setAdapter后调用。
		MobclickAgent.onPageStart("切换设备页"); // 统计页面
	}

	private void initView() {
		flags = getIntent().getFlags();
		// TODO Auto-generated method stub
		ChangeDeviceInfo changeDeviceInfo = null;
		switch (flags) {
			case  device_tizhong :
				changeDeviceInfo = spInstance.getObj(SP_BLOOD_SELECT_TIZHONG,null);
				break;
			case device_tiwen :
				changeDeviceInfo = spInstance.getObj(SP_BLOOD_SELECT_TIWEN,null);
				break;
			case device_shuimian :
				changeDeviceInfo = spInstance.getObj(memberId+SP_BLOOD_SELECT_SHUIMIAN,null);

				break;
			case device_xueya :
				changeDeviceInfo = spInstance.getObj(SP_BLOOD_SELECT_XUEYA,null);
				break;
			case device_xuetang :
				changeDeviceInfo = spInstance.getObj(SP_BLOOD_SELECT_XUETANG,null);

				break;
			case device_yundong:
				changeDeviceInfo = spInstance.getObj(memberId+SP_BLOOD_SELECT_YUNDONG,null);

				break;
			case device_tizhi :
				changeDeviceInfo = spInstance.getObj(SP_BLOOD_SELECT_TIZHI,null);
				break;
		}
		if(changeDeviceInfo != null){
			currentDevice = changeDeviceInfo;
		}

		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_equipmentlist_change));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);

		listview = (ListView) findViewById(R.id.equiplist_listview_change);
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
//		adapter = new ChangeDeviceListAdapter(this, deviceInfos);
//		listview.setAdapter(adapter);
//		Constant.setListViewHeight(listview);// 解决ScrollView和ListView在嵌套使用时冲突的问题
		// ,在setAdapter后调用。
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				setResult(Activity.RESULT_CANCELED, null);
				finish();
				break;

			default:
				break;
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,
								   long arg3) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
		// TODO Auto-generated method stub
		//先判断以前有没有设置过默认,有设置过就先置为0
		/*for(ChangeDeviceInfo info : deviceInfos){
			int isSelect = info.getIsSelect();
			if(isSelect == 1){
				info.setIsSelect(0);
			}
		}*/

		YesOrNoDialog.Builder builder = new YesOrNoDialog.Builder(ChangeDeviceListActivity.this);
		builder.setTitle("切换默认设备");
		builder.setMessage("是否将"+bindDeviceListAdapter.getItem(arg2).getDevice_name()+"设置为默认设备？");
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				BindDeviceBean item = bindDeviceListAdapter.getItem(arg2);
				ChangeDeviceInfo changeDeviceInfo = new ChangeDeviceInfo();
				changeDeviceInfo.setConnectId(item.getDevcieId());
				changeDeviceInfo.setDeviceName(item.getDevice_name());
				changeDeviceInfo.setDeviceSn(item.getDevice_sn());
				changeDeviceInfo.setDeviceNo(item.getDevice_no());
				String toJson = JsonUtils.toJson(item.getFunction_info());
				changeDeviceInfo.setFunction_info(toJson);
				if(item != null ){
					System.out.println("已选择的默认设备是=="+item.toString());
					switch (flags) {
						case  device_tizhong :
							spInstance.putObj(SP_BLOOD_SELECT_TIZHONG, changeDeviceInfo);
							break;
						case device_tiwen :
							spInstance.putObj(SP_BLOOD_SELECT_TIWEN, changeDeviceInfo);
							break;
						case device_shuimian :
							spInstance.putObj(memberId+SP_BLOOD_SELECT_SHUIMIAN, changeDeviceInfo);

							break;
						case device_xueya :
							spInstance.putObj(SP_BLOOD_SELECT_XUEYA, changeDeviceInfo);
							break;
						case device_xuetang :
							spInstance.putObj(SP_BLOOD_SELECT_XUETANG, changeDeviceInfo);

							break;
						case device_yundong:
							spInstance.putObj(memberId+SP_BLOOD_SELECT_YUNDONG, changeDeviceInfo);

							break;
						case device_tizhi :
							spInstance.putObj(SP_BLOOD_SELECT_TIZHI, changeDeviceInfo);
							break;
					}
				}else{
					System.out.println("未选中默认设备");
				}
				setResult(Activity.RESULT_OK, null);
				ChangeDeviceListActivity.this.finish();
				dialog.dismiss();
			}
		});
		builder.create().show();
	}


	private void getMyDeviceList(final int page_no) {
		if(!CommonUtil.isEnabledNetWork(ChangeDeviceListActivity.this.getApplicationContext())){
			FRToast.showToast(ChangeDeviceListActivity.this.getApplicationContext(),"网络连接异常，请稍后再试");
			return;
		}
		progressDialog = new ProgressDialog(ChangeDeviceListActivity.this);
		Constant.showProgressDialog(progressDialog);
		if(MiaoApplication.getMiaoHealthManager()==null)return;
		MiaoApplication.getMiaoHealthManager().fetchUserDeviceList(page_no, new MiaoUserDeviceListListener() {
			@Override
			public void onUserDeviceListResponse(BindDeviceListBean bindDeviceList) {
				Constant.exitProgressDialog(progressDialog);
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
				CommonUtil.sendMessage(handler,1, "");
			}

			@Override
			public void onError(int code, String msg) {
				Constant.exitProgressDialog(progressDialog);
				CommonUtil.sendMessage(handler,0, "设备类型列表获取失败 code：" + code + " msg:" + msg);

			}
		});
	}


}

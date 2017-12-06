package com.sinosoft.fhcs.android.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jwsd.libzxing.OnQRCodeListener;
import com.jwsd.libzxing.QRCodeManager;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.customview.DeviceSearchDialog;
import com.sinosoft.fhcs.android.customview.YesOrNoDialog;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.sinosoft.fhcs.android.util.FRToast;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.miao.lib.listeners.MiaoBindListener;
import cn.miao.lib.listeners.MiaoCheckBindListener;
import cn.miao.lib.listeners.MiaoScanBleListener;
import cn.miao.lib.model.DeviceBean;

import static com.sinosoft.fhcs.android.ExitApplicaton.miaoHealthManager;
import static com.sinosoft.fhcs.android.util.Constant.PERMISSION_SUCCESS;

/**
 * 描述：绑定设备
 * 作者：shuiq_000
 * 邮箱：2028318192@qq.com
 * @time: 2017/12/5 14:23
 * @version 1.0
 */
public class BindDeviceActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.titlebar_btn_back)
    Button titlebarBtnBack;
    @BindView(R.id.titlebar_tv_title)
    TextView titlebarTvTitle;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.btn_bind)
    Button btnBind;
    @BindView(R.id.wv_dev_bind)
    WebView webview;
    @BindView(R.id.rl_parent_device)
    RelativeLayout rlParentDevice;
    private DeviceBean deviceBean;
    private DeviceSearchDialog deviceSearchDialog;
    private ArrayList<HashMap<String, String>> arraylist;
    private static final int RESULT_CODE_SCAN_CODE = 1001;
    protected final int AUTH2RESULT = 1002;


    private AlertDialog alertDialog;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            Log.e("tag", "handleMessage: " + msg.toString());
            switch (msg.what) {
                case 0:
                    FRToast.showToast(BindDeviceActivity.this, String.valueOf(msg.obj));
                    break;
                case 1:
                    FRToast.showToast(BindDeviceActivity.this, String.valueOf(msg.obj));
                    break;
                case 2:
                    AndPermission.with(getApplicationContext())
                            .requestCode(PERMISSION_SUCCESS)
                            .permission(android.Manifest.permission.CAMERA)
                            .rationale(rationaleListener)
                            .callback(new PermissionListener() {
                                @Override
                                public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                                    if(requestCode == PERMISSION_SUCCESS){
                                        startScanQRcode();
                                    }
                                }

                                @Override
                                public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                    if(requestCode == PERMISSION_SUCCESS){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                FRToast.showToast(getApplicationContext(),"无相机权限，无法扫描二维码");
                                            }
                                        });
                                    }
                                }
                            })
                            .start();
                    /*Intent intent = new Intent(BindDeviceActivity.this, CaptureActivity.class);
                    intent.putExtra("device_sn", String.valueOf(msg.obj));
                    startActivityForResult(intent, RESULT_CODE_SCAN_CODE);*/
                    break;
                case 3:
                    btnBind.setVisibility(View.GONE);
                    break;
                case 4:
                    btnBind.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    arraylist = (ArrayList<HashMap<String, String>>) msg.obj;
                    if (deviceSearchDialog != null) {
                        deviceSearchDialog.setReslutData(arraylist);
                    } else {
                        return;
                    }
                    if (arraylist.size() <= 0) {
                        FRToast.showToast(BindDeviceActivity.this, "未扫描到设备");
                        if (deviceSearchDialog != null) {
                            deviceSearchDialog.dismiss();
                        }
                    }
                    break;
                case 6:
                    final String device_no = String.valueOf(msg.obj);
                    alertDialog = new AlertDialog.Builder(BindDeviceActivity.this,R.style.alterDialogCustom).
                            setTitle("检查绑定").
                            setMessage("设备已被其他人绑定").setNegativeButton("绑定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.e("tag", "handleMessage: " + String.valueOf(msg.obj));
                            bindDevice(deviceBean.getDevice_sn(), device_no);
                            alertDialog.dismiss();
                        }
                    }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    }).create();
                    alertDialog.show();
                    break;
                case 7:
                    showBindTrueDialog();
                    break;
            }
        }

        private void startScanQRcode() {
            QRCodeManager.getInstance().with(BindDeviceActivity.this)
                    .setReqeustType(0)
                    .scanningQRCode(new OnQRCodeListener() {
                        @Override
                        public void onCompleted(final String s) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    FRToast.showToast(getApplicationContext(),"扫描结果=="+s);
                                    checkDeviceBind(deviceBean.getDevice_sn(),s);
                                }
                            });
                        }

                        @Override
                        public void onError(final Throwable throwable) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    FRToast.showToast(getApplicationContext(),"扫描出错=="+throwable.toString());

                                }
                            });

                        }

                        @Override
                        public void onCancel() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    FRToast.showToast(getApplicationContext(),"扫描取消");
                                }
                            });

                        }

                        /**
                         * 当点击手动添加时回调
                         * @param requestCode
                         * @param resultCode
                         * @param data
                         */
                        @Override
                        public void onManual(int requestCode, int resultCode, Intent data) {
                            super.onManual(requestCode, resultCode, data);
                        }
                    });
        }
    };

    /**
     * Rationale支持，这里自定义对话框。
     */
    public static RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            com.yanzhenjie.alertdialog.AlertDialog.newBuilder(mContext)
                    .setTitle("友好提醒")
                    .setMessage("扫描二维码需要使用相机权限")
                    .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.resume();
                        }
                    })
                    .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.cancel();
                        }
                    }).show();
        }
    };
    private String deviceNo;
    private String currentBluName;//选择连接的蓝牙名称
    private static BindDeviceActivity mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void setUpViewAndData() {
        setContentView(R.layout.activity_bind_device);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webview != null){
            webview.resumeTimers();
        }
        initData();
        initView();
        MobclickAgent.onPageEnd("设备绑定页");
    }

    @Override
    protected void onStop() {
        super.onStop();
        webview.stopLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webview != null) {
            rlParentDevice.removeView(webview);
            webview.removeAllViews();
            webview.destroy();
        }
        if(miaoHealthManager != null){
            miaoHealthManager.stopScanBLEDevice();
        }
    }

    private void initView() {
        if (deviceBean == null || deviceBean.getDes_url() == null) {
            return;
        }

       /* WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        if(width > 650)
        {
            webview.setInitialScale(190);
        }else if(width > 520)
        {
            webview.setInitialScale(160);
        }else if(width > 450)
        {
            webview.setInitialScale(140);
        }else if(width > 300)
        {
            webview.setInitialScale(120);
        }else
        {
            webview.setInitialScale(100);
        }*/

//        webview.setInitialScale(25);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.requestFocus();
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setDomStorageEnabled(true);
        String dir = webview.getContext().getDir("database", this.MODE_PRIVATE).getPath();
        webview.getSettings().setDatabasePath(dir);
//        webview.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webview.setWebViewClient(new MyWebViewClient());
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setDefaultTextEncodingName("UTF-8");
        webview.loadUrl(deviceBean.getDes_url());
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onReceivedTitle(WebView view, String title) {
                view.setBackgroundColor(R.color.about_text);
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel:
                if (deviceSearchDialog != null) {
                    miaoHealthManager.stopScanBLEDevice();
                    deviceSearchDialog.dismiss();
                    deviceSearchDialog = null;
                }
                break;
            case R.id.button_agin://重新搜索
                if (deviceSearchDialog != null) {
                    miaoHealthManager.stopScanBLEDevice();
                    deviceSearchDialog.dismiss();
                    deviceSearchDialog = null;
                }
                LinkedBlu();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (deviceSearchDialog != null) {
            deviceSearchDialog.dismiss();
            deviceSearchDialog = null;
        }
        if (arraylist != null && arraylist.size() > position) {
            currentBluName = arraylist.get(position).get("name");
            checkDeviceBind(deviceBean.getDevice_sn(), arraylist.get(position).get("mac"));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


    }

    private void checkDeviceBind(final String device_sn, final String device_no) {
        sendMessage(1, "开始检测是被是否被绑定...");
        if (!CommonUtil.isEnabledNetWork(BindDeviceActivity.this)) {
            FRToast.showToast(BindDeviceActivity.this, "网络连接异常，请稍后再试");
            return;
        }
        miaoHealthManager.checkDevice(device_sn, device_no, new MiaoCheckBindListener() {
            @Override
            public void onCheckBindRespone(int bindState) {
                switch (bindState) {
                    case 1://1-设备未被绑定
                        sendMessage(1, "设备未被绑定");
                        bindDevice(device_sn, device_no);
                        break;
                    case 2://2-设备已被其他人绑定
                        sendMessage(1, "设备已被其他人绑定");
                        sendMessage(6, device_no);

                        break;
                    case 3: //3-设备已被自己绑定
                        sendMessage(1, "设备已被自己绑定");
                        deviceNo = device_no;
                        sendMessage(3, "");
                        break;
                }
            }

            @Override
            public void onError(int code, String msg) {
                sendMessage(1, "检查绑定失败 code：" + code + " msg:" + msg);
            }
        });
    }

    final class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //TLog.e("test", "url Override =====" + url + "  time: " + sDateFormat.format(new java.util.Date()));
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        // 页面出错
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            FRToast.showToast(getApplicationContext(), "页面出错了");
        }
    }

    private void initData() {
        titlebarBtnBack.setVisibility(View.VISIBLE);
        titlebarTvTitle.setText(getString(R.string.title_bindequipment));
        deviceBean = (DeviceBean) getIntent().getExtras().get("deviceBean");
    }

    @OnClick({R.id.titlebar_btn_back, R.id.btn_buy, R.id.btn_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titlebar_btn_back:
                finish();
                break;
            case R.id.btn_buy://来一个
                break;
            case R.id.btn_bind://绑定
                switch (deviceBean.getIsbind()) {
                    case 1: //蓝牙链接
                        switch (deviceBean.getLink_type()) {
                            case 1: //蓝牙链接
                                LinkedBlu();
                                break;
                            case 2: //扫描二维码
                                sendMessage(1, "扫描二维码");
                                sendMessage(2, deviceBean.getDevice_sn());
                                break;
                            case 3:// TODO: 2016/12/21 授权
                                sendMessage(1, "开始授权");
                                startAuth2Activity(deviceBean.getDevice_sn());
                                break;
                        }
                        break;
                    case 2: //设备不支持绑定
                        sendMessage(1, "设备不支持绑定");
                        break;
                    case 3: //设备即将上线
                        sendMessage(1, "设备即将上线");
                        break;
                    case 4:  //设备下线
                        sendMessage(1, "设备下线");
                        break;
                }
                break;
        }
    }

    private void LinkedBlu() {
        deviceSearchDialog = new DeviceSearchDialog(BindDeviceActivity.this, this, this);
        deviceSearchDialog.show();
        scanBLEDevice(deviceBean.getDevcieId(), deviceBean.getDevice_sn());
    }

    /**
     * 打开授权认证页面
     */
    private void startAuth2Activity(String deviceSn){
        Intent intent = new Intent(BindDeviceActivity.this,AuthWebActivity.class);
        intent.putExtra("deviceSn", deviceSn);
        startActivityForResult(intent,AUTH2RESULT);
    }
    private void sendMessage(int what, Object msg) {
        Message message = new Message();
        message.what = what;
        message.obj = msg;
        handler.sendMessage(message);
    }

    private void scanBLEDevice(String deviceId, String device_sn) {
//        sendMessage(1, "开始扫描周边蓝牙...");
        if(miaoHealthManager == null){
            FRToast.showToast(getApplicationContext(),"请先初始化妙健康");
            return;
        }
        miaoHealthManager.scanBLEDevice(deviceId, device_sn, 1000 * 10, new MiaoScanBleListener() {
            @Override
            public void onScanbleResponse(ArrayList<HashMap<String, String>> arraylist) {
                sendMessage(5, arraylist);
            }
        });
    }

    private void bindDevice(String device_sn, final String device_no) {
        sendMessage(1, "开始绑定设备...");
        if (!CommonUtil.isEnabledNetWork(BindDeviceActivity.this)) {
            FRToast.showToast(BindDeviceActivity.this, "网络连接异常，请稍后再试");
            return;
        }
        miaoHealthManager.bindDevice(device_sn, device_no, new MiaoBindListener() {
            @Override
            public void onBindDeviceSuccess(String device_no) {
                deviceNo = device_no;
//                sendMessage(1, "绑定成功 " + device_no);
                sendMessage(3, "");
                sendMessage(7, "");
            }

            @Override
            public void onError(int code, String msg) {
                sendMessage(1, "绑定失败 code：" + code + " msg:" + msg);
            }
        });
    }

    //sho
    // w一个绑定成功的dialog
    private void showBindTrueDialog() {
        YesOrNoDialog.Builder builder = new YesOrNoDialog.Builder(BindDeviceActivity.this);
        if(deviceBean.getDevice_name() != null && currentBluName != null){
            builder.setTitle(deviceBean.getDevice_name());
            builder.setMessage("与“"+currentBluName+"”绑定成功");
        }
        builder.setNegativeButton("完成", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 410){
            QRCodeManager.getInstance().with(this).onActivityResult(requestCode, resultCode, data);
        }else
        if(requestCode == AUTH2RESULT && requestCode == 2){
            int result = data.getIntExtra("result", 0);
            switch (result) {
                case 1://绑定成功
                    sendMessage(7, "");
                    break;
                case -1://绑定失败
                    sendMessage(1, "绑定失败");
                break;
                case -2://绑定页面出错
                    sendMessage(1, "绑定页面出错");
                    break;
            }
        }

    }

}

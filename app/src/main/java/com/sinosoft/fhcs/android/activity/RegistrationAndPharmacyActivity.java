package com.sinosoft.fhcs.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.FRToast;
import com.sinosoft.fhcs.android.util.HttpManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sinosoft.fhcs.android.util.Constant.information_illness;
import static com.sinosoft.fhcs.android.util.Constant.information_pharmacy;
import static com.sinosoft.fhcs.android.util.Constant.information_registration;

/**
 * 预约挂号和用药评估页面
 */
public class RegistrationAndPharmacyActivity extends BaseActivity {

    @BindView(R.id.titlebar_btn_back)
    Button titlebarBtnBack;
    @BindView(R.id.titlebar_tv_title)
    TextView titlebarTvTitle;
    @BindView(R.id.wv_rap)
    WebView wvRap;
    @BindView(R.id.myProgressBar)
    ProgressBar progressBar;
    private RegistrationAndPharmacyActivity mActivity;
    private ProgressDialog progressDialog;
    private int flags = -1;
    private String titleName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setUpViewAndData() {
        setContentView(R.layout.activity_registration_and_pharmacy);
        ButterKnife.bind(this);
        mActivity = this;
        getData();
        initView();
//        verifyUser();
    }

    private void getData() {
        flags = getIntent().getFlags();
        titleName = (String) getIntent().getExtras().get("titleName");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initView() {
        View viewById = findViewById(R.id.registertwo_include);
        titlebarBtnBack.setVisibility(View.VISIBLE);
        titlebarTvTitle.setText("" + titleName);
        // 从首选项获取信息
        SharedPreferences prefs = getSharedPreferences("UserInfo",
                Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("phoneNumber", null);
        if (phoneNumber == null || phoneNumber.length() == 0) {
            FRToast.showToast(mActivity.getApplicationContext(), "用户未绑定手机号，暂不能使用服务");
            return;
        }
        String miaoUrl = null;
        switch (flags) {
            case information_registration://预约挂号
                miaoUrl = HttpManager.miao_registration_url + "?user_id=" + phoneNumber + "&open_appid=mphmhch7a3zgcvbce0&product_code=H01347683716";
                break;
            case information_pharmacy://用药评估
                viewById.setVisibility(View.GONE);
                miaoUrl = HttpManager.miao_medicine_url + "?user_id=" + phoneNumber + "&open_appid=mphmhch7a3zgcvbce0&product_code=H01347683716";
                break;
            case information_illness://疾病评估
                viewById.setVisibility(View.GONE);
                miaoUrl = HttpManager.miao_diseaseEvaluate_url + "?user_id=" + phoneNumber + "&open_appid=mphmhch7a3zgcvbce0&product_code=H01347683716";
                break;
        }

        wvRap.setInitialScale(25);
        WebSettings settings = wvRap.getSettings();
        // 适应屏幕
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSavePassword(true);
        settings.setSaveFormData(true);
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        //设置进度条
        wvRap.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && !mActivity.isFinishing()) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (View.INVISIBLE == progressBar.getVisibility() && !mActivity.isFinishing()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        // 覆盖webView默认通过系统或者第三方浏览器打开网页的行为
        // 如果为false调用系统或者第三方浏览器打开网页的行为
        wvRap.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // webView加载web资源
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        if (miaoUrl != null) {
            wvRap.loadUrl(miaoUrl);
        }
//        wvRap.loadUrl("https://healthapitest.miaohealth.net/health-service-rest/v1/link/registration?user_id=18600798984&open_appid=mphmhch7a3zgcvbce0&product_code=H01347683716");
    }

    @OnClick(R.id.titlebar_btn_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titlebar_btn_back:
                if(flags == information_registration && wvRap.canGoBack()){
                    wvRap.goBack();
                }else{
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && wvRap.canGoBack()){
            wvRap.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

package com.sinosoft.fhcs.android.activity;
/**
 * 授权webview
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.HttpManager;
public class OauthWebViewActivity extends BaseActivity {
    private String url_aouth_codoon="";
    private static final String TAG = OauthWebViewActivity.class.getSimpleName();
    private String accessCode;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setUpViewAndData() {
        setContentView(R.layout.oauth_webview);
        url_aouth_codoon=HttpManager.urlOauth_codoon();
        // webview
        WebView webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String accessCodeFragment = "&code=";
                Log.e(TAG, "oauth response from server: " + url);

                int start = url.indexOf(accessCodeFragment);
                if(start > -1) {
                    Log.d(TAG, "user accepted, url is :" + url);
                    accessCode = url.substring(start + accessCodeFragment.length(), url.length());
                    Log.d(TAG, "user accepted, code is :" + accessCode);

                    view.clearCache(true);
                    clearCookies(OauthWebViewActivity.this);
                    Intent i = getIntent();
                    i.putExtra(HttpManager.ACCESS_CODE, accessCode);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });
        webview.loadUrl(url_aouth_codoon);
    }

    //清楚cookies 防止下次登录记住账号和密码
    public static void clearCookies(Context context) {
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr =
                CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
}

package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:套餐网页展示页
 * @Author: wangshuangshuang.
 * @Create: 2015年11月2日.
 */
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.Constant;
import com.umeng.analytics.MobclickAgent;

public class WebViewActivity extends BaseActivity implements OnClickListener {
	private final static int FILECHOOSER_RESULTCODE = 1;
	private String url = "http://www.yjkang.cn/app/help/jkgajctc.html";
	private String name = "";
	private WebView cWebView;
	private TextView tvTitle;
	private Button btnBack;
	private ValueCallback<Uri> mUploadMessage;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_webview);
		ExitApplicaton.getInstance().addActivity(this);
		progressDialog = new ProgressDialog(this);
		url = this.getIntent().getExtras().getString("url");
		name = this.getIntent().getExtras().getString("name");
		init();
		initWebView();
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(name + "服务");
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("套餐网页展示界面"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("套餐网页展示界面"); // 保证 onPageEnd 在onPause 之前调用,因为
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			default:
				break;
		}

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		// 设计进度条
		// 获得WebView组件
		cWebView = (WebView) findViewById(R.id.webview_healthservice);
		LinearLayout.LayoutParams mWebViewLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		cWebView.setLayoutParams(mWebViewLP);
		cWebView.setInitialScale(25);
		WebSettings settings = cWebView.getSettings();
		// 适应屏幕
		settings.setUseWideViewPort(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setJavaScriptEnabled(true);
		cWebView.loadUrl(url);
		// 设置视图客户端
		cWebView.setDownloadListener(new MyWebViewDownLoadListener());
		cWebView.setWebViewClient(new MyWebViewClient());
		cWebView.setWebChromeClient(new MyWebChromeClient());
	}

	// 设置文件下载
	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
									String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}

	}

	// 设置文件上传
	class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
								 JsResult result) {
			// TODO Auto-generated method stub
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
								   JsResult result) {
			// TODO Auto-generated method stub
			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
								  String defaultValue, JsPromptResult result) {
			// TODO Auto-generated method stub
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}

		// Android > 4.1.1 调用这个方法
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
									String acceptType, String capture) {
			mUploadMessage = uploadMsg;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(
					Intent.createChooser(intent, "File Chooser"),
					FILECHOOSER_RESULTCODE);

		}

		// 3.0 + 调用这个方法
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
									String acceptType) {
			mUploadMessage = uploadMsg;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(
					Intent.createChooser(intent, "File Chooser"),
					FILECHOOSER_RESULTCODE);
		}

		// Android < 3.0 调用这个方法
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mUploadMessage = uploadMsg;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(
					Intent.createChooser(intent, "File Chooser"),
					FILECHOOSER_RESULTCODE);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
									Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = intent == null || resultCode != RESULT_OK ? null
					: intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			// 开始加载进度条
			Constant.showProgressDialog(progressDialog);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// 退出进度条
			Constant.exitProgressDialog(progressDialog);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl) {
			// 加载失败页面
			// cWebView.loadUrl("file:///android_asset/1111.html");
		}
	}

	// 回退处理
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK) && cWebView.canGoBack()) {
			cWebView.goBack();
			return true;
		} else {
			if ((keyCode == KeyEvent.KEYCODE_BACK) && !cWebView.canGoBack()) {
				// 退出
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}

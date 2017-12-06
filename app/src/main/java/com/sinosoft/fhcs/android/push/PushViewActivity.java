package com.sinosoft.fhcs.android.push;

/**
 * @CopyRight: SinoSoft.
 * @Description: 推送展示页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月15日.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.gesture.GestureImageView;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

@SuppressLint("SetJavaScriptEnabled")
public class PushViewActivity extends Activity {
	private String strContent = "";// 内容
	private String type = "";// 1==>服药提醒 2==>健康消息 3==>系统消息
	private String msg_id;// 消息id
	private String strImgUrl = "";// 图片
	private String strVideo = "";
	private String strHtml = "";
	private String heathInformationType = "";// "3000101"; 文本 "3000102";报告
	// html"3000103";图片 imageText
	// "3000104";视频 video
	private TextView title;
	private Button btnClosed;// 关闭
	private EditText edtContent;// 文本
	private String userId;// 用户id
	private WebView cWebView;// 报告
	private LinearLayout layout;
	private GestureImageView imageView;// 图片
	private DisplayImageOptions options;
	private TextView tvVideo;// 视频

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pushview);
		initOptions();
		// 获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		initData();
		title = (TextView) findViewById(R.id.pushview_title);
		// 1==>服药提醒 2==>健康消息 3==>系统消息
		if (type.equals("1")) {
			title.setText("服药提醒");
			initRequest(HttpManager.urlMsgReadOne(userId, msg_id.trim()));
		} else if (type.equals("2")) {
			title.setText("健康资讯");
			initRequest(HttpManager.urlInfoIsOneRead(userId, msg_id.trim()));
		} else if (type.equals("3")) {
			title.setText("系统消息");
		} else {
			title.setText("服药提醒");
			initRequest(HttpManager.urlMsgReadOne(userId, msg_id.trim()));
		}
		btnClosed = (Button) findViewById(R.id.pushview_closed);
		btnClosed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PushViewActivity.this.finish();
			}
		});
		cWebView = (WebView) findViewById(R.id.pushview_webview);
		layout = (LinearLayout) findViewById(R.id.pushview_layout);
		edtContent = (EditText) findViewById(R.id.pushview_content);
		imageView = (GestureImageView) findViewById(R.id.pushview_img);
		tvVideo = (TextView) findViewById(R.id.pushview_video);
		if (!type.equals("2")) {
			edtContent.setVisibility(View.VISIBLE);
			edtContent.setText(strContent);
		} else {
			// 资讯信息
			if (heathInformationType.trim().equals("3000101")) {
				// 文本
				edtContent.setVisibility(View.VISIBLE);
				imageView.setVisibility(View.GONE);
				layout.setVisibility(View.GONE);
				tvVideo.setVisibility(View.GONE);
				edtContent.setText(strContent);
			} else if (heathInformationType.trim().equals("3000102")) {
				// 报告
				edtContent.setVisibility(View.GONE);
				imageView.setVisibility(View.GONE);
				layout.setVisibility(View.VISIBLE);
				tvVideo.setVisibility(View.GONE);
				cWebView.requestFocus();// 使页面获得焦点
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
				if(strHtml.startsWith("http")){
					cWebView.loadUrl(strHtml);
				}else{
					cWebView.loadUrl(HttpManager.m_serverAddress + strHtml);
				}

			} else if (heathInformationType.trim().equals("3000103")) {
				// 图片
				edtContent.setVisibility(View.GONE);
				imageView.setVisibility(View.VISIBLE);
				layout.setVisibility(View.GONE);
				tvVideo.setVisibility(View.GONE);
				ImageLoader.getInstance().displayImage(strImgUrl, imageView, options);
			} else if (heathInformationType.trim().equals("3000104")) {
				// 视频
				edtContent.setVisibility(View.GONE);
				imageView.setVisibility(View.GONE);
				layout.setVisibility(View.GONE);
				tvVideo.setVisibility(View.VISIBLE);
				tvVideo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!strVideo.trim().equals("")) {
							Constant.playUrl(PushViewActivity.this,
									strVideo.trim());
						} else {
							Toast.makeText(PushViewActivity.this, "无法观看该视频！",
									Toast.LENGTH_SHORT).show();
						}

					}
				});
			}
		}
	}
	private void initOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.img_ing)
				.showImageForEmptyUri(R.drawable.img_xx)
				.showImageOnFail(R.drawable.img_xx)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

	}
	// 更新已读未读
	private void initRequest(String url) {

		if (HttpManager.isNetworkAvailable(PushViewActivity.this)) {
			ReadRequest re = new ReadRequest();
			re.execute(url);
		} else {
			System.out.println("检查网络是否连接");
		}

	}

	// 解析数据
	private void initData() {
		Intent intent = getIntent();
		if (null != intent) {
			Bundle bundle = getIntent().getExtras();
			String title = bundle
					.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String title2 = bundle.getString(JPushInterface.EXTRA_ALERT);
			String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
			try {
				JSONObject jo = new JSONObject(json);
				type = jo.optString("type");
				if (!type.trim().equals("3")) {
					msg_id = jo.optString("msg_id");
				}
				if (!type.trim().equals("2")) {
					strContent = jo.optString("notice");
				} else {
					heathInformationType = jo.optString("heathInformationType");
					if (heathInformationType.trim().equals("3000101")) {
						strContent = jo.optString("notice");
					} else if (heathInformationType.trim().equals("3000102")) {
						strHtml = jo.optString("html");
					} else if (heathInformationType.trim().equals("3000103")) {
						strImgUrl = jo.optString("image");
					} else if (heathInformationType.trim().equals("3000104")) {
						strVideo = jo.optString("video");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Title : " + title + "  " + "Content : "
					+ title2);
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("消息推送页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("消息推送页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	// 更新已读
	private class ReadRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("readInfoUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				System.out.println("超时");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						System.out.println("更新成功");
					} else {
						System.out.println("更新失败");
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					e.printStackTrace();
				}
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}
}

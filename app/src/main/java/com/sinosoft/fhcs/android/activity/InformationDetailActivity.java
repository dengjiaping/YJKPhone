package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:资讯详情页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.entity.InformationChild;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class InformationDetailActivity extends BaseActivity implements
		OnClickListener {
	private TextView includeTitle;
	private Button btnBack;// 返回
	private TextView tvTitle, tvDate, tvMember;// 标题，日期，成员
	private EditText tvContent;// 文字内容
	private WebView cWebView;// html展示
	private ImageView img;// 图片展示
	private Button btnVideo;// 视频展示
	private ImageView ivVideo;//视频缩略图
	private LinearLayout layout;
	private RelativeLayout layout2;
	private InformationChild item = new InformationChild();
	// 标记已读
	private ProgressDialog progressDialog;
	private String userId;
	private DisplayImageOptions options;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_informationdetail);
		ExitApplicaton.getInstance().addActivity(this);
		item = (InformationChild) this.getIntent().getExtras().get("entity");
		initOptions();
		init();
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		if (HttpManager.isNetworkAvailable(InformationDetailActivity.this)) {
			ReadRequest re = new ReadRequest();
			re.execute(HttpManager.urlInfoIsOneRead(userId, item.getId()));
		} else {
			Toast.makeText(InformationDetailActivity.this, "检查网络是否连接",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void initOptions() {
		if(item.getInformationType().toString().trim()
				.equals("3000104")){
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.img_ing)
					.showImageForEmptyUri(R.drawable.blackbg)
					.showImageOnFail(R.drawable.blackbg)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
		}else{
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
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("资讯详情页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("资讯详情页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		includeTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		includeTitle.setText(getResources().getString(
				R.string.title_informationdetail));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.infodetail_title);
		tvDate = (TextView) findViewById(R.id.infodetail_date);
		tvMember = (TextView) findViewById(R.id.infodetail_member);
		tvContent = (EditText) findViewById(R.id.infodetail_content);
		cWebView = (WebView) findViewById(R.id.infodetail_webview);
		img = (ImageView) findViewById(R.id.infodetail_img_imgtext);
		btnVideo = (Button) findViewById(R.id.infodetail_btn_video);
		ivVideo=(ImageView) findViewById(R.id.infodetail_iv_video);
		layout=(LinearLayout) findViewById(R.id.infodetail_layout);
		layout2=(RelativeLayout) findViewById(R.id.infodetail_layout2);
		// 资讯信息
		if (item.getInformationType().toString().trim().equals("3000101")) {
			// 文本
			layout.setVisibility(View.VISIBLE);
			cWebView.setVisibility(View.GONE);
			img.setVisibility(View.GONE);
			layout2.setVisibility(View.GONE);
			tvContent.setText(item.getInformationContent().trim());

		} else if (item.getInformationType().toString().trim()
				.equals("3000102")) {
			// 报告Html
			layout.setVisibility(View.GONE);
			cWebView.setVisibility(View.VISIBLE);
			img.setVisibility(View.GONE);
			layout2.setVisibility(View.GONE);
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
			if(item.getHtml().toString().trim().equals("")){
				Toast.makeText(this, "地址不可用！", Toast.LENGTH_SHORT).show();
			}else{
				String strHtml=item.getHtml().toString().trim();
				if(strHtml.startsWith("http")){
					cWebView.loadUrl(strHtml);
				}else{
					cWebView.loadUrl(HttpManager.m_serverAddress + strHtml);
				}
			}
		} else if (item.getInformationType().toString().trim()
				.equals("3000103")) {
			// 图片
			layout.setVisibility(View.GONE);
			cWebView.setVisibility(View.GONE);
			img.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.GONE);
			img.setOnClickListener(this);
			String strImg=item.getImageText().toString().trim();
			if(strImg.startsWith("http")){
				ImageLoader.getInstance().displayImage(strImg, img, options);
			}else{
				ImageLoader.getInstance().displayImage(HttpManager.m_serverAddress + strImg, img, options);
			}

		} else if (item.getInformationType().toString().trim()
				.equals("3000104")) {
			// 视频
			layout.setVisibility(View.GONE);
			cWebView.setVisibility(View.GONE);
			img.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			btnVideo.setOnClickListener(this);

			String strImg=item.getImageText().toString().trim();
			if(strImg.startsWith("http")){
				ImageLoader.getInstance().displayImage(strImg, ivVideo, options);
			}else{
				ImageLoader.getInstance().displayImage(HttpManager.m_serverAddress + strImg, ivVideo, options);
			}
		} else {
			// 文本
			tvContent.setText(item.getInformationContent());
		}
		tvTitle.setText(item.getTitle());
		tvDate.setText(Constant.getDateFormat4(item.getPubdate()));
		tvMember.setText(item.getFamilyMemberRoleName().trim());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.infodetail_btn_video:
				String strVideo=item.getVideo().toString().trim();
				if(!strVideo.equals("")){
					if(item.getCooperateIdentify().trim().equals("xk")){
						String s1[] = strVideo.split("\\.");
						String strVideo2 = "";
						if (s1.length == 2) {
							strVideo2 = s1[0] + "-mobile." + s1[1];
							if(strVideo.startsWith("http")){
								Constant.playUrl(this,strVideo2 );
								Log.e("熙康视频链接1", strVideo2);
							}else{
								Constant.playUrl(this,HttpManager.m_serverAddress+strVideo2);
								Log.e("熙康视频链接2", HttpManager.m_serverAddress+strVideo2);
							}
						} else {
							Toast.makeText(this, "无法观看该视频！", Toast.LENGTH_SHORT).show();
						}
					}else{
						if(strVideo.startsWith("http")){
							Constant.playUrl(this,strVideo );
							Log.e("其他视频链接1", strVideo);
						}else{
							Constant.playUrl(this,HttpManager.m_serverAddress+strVideo);
							Log.e("其他视频链接2", strVideo);
						}
					}
				}else{
					Toast.makeText(this, "无法观看该视频！", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.infodetail_img_imgtext:
				if(item.getImageText().trim().equals("")){
					Toast.makeText(this, "图片链接无效！", Toast.LENGTH_SHORT).show();
				}else{
					Intent intent=new Intent(this,ImageActivity.class);
					intent.putExtra("imgUrl", item.getImageText().trim());
					startActivity(intent);
				}

				break;
			default:
				break;
		}

	}

	// 更新已读
	private class ReadRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(InformationDetailActivity.this);
			Constant.showProgressDialog(progressDialog);
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
			Constant.exitProgressDialog(progressDialog);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}
}

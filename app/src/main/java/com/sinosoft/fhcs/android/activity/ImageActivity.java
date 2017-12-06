package com.sinosoft.fhcs.android.activity;
/**
 * @CopyRight: SinoSoft.
 * @Description: 资讯大图页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月26日.
 */
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.gesture.GestureImageView;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

public class ImageActivity extends BaseActivity implements OnClickListener{
	private TextView tvTitle;
	private Button btnBack;
	private GestureImageView imageView;
	private DisplayImageOptions options;
	private String strUrl="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_image);
		ExitApplicaton.getInstance().addActivity(this);
		initOptions();
		strUrl=this.getIntent().getExtras().getString("imgUrl");
		init();
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
	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_image));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		imageView=(GestureImageView) findViewById(R.id.image_bigimage);
		if(strUrl.startsWith("http")){
			ImageLoader.getInstance().displayImage(strUrl, imageView, options);
		}else{
			ImageLoader.getInstance().displayImage(HttpManager.m_serverAddress + strUrl, imageView, options);
		}

	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("资讯大图页"); //统计页面
		MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("资讯大图页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				finish();
				break;
			default:
				break;
		}

	}
}

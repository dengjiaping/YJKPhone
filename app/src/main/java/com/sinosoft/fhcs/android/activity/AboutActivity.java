package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description: 关于翼家康页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.Constant;
import com.umeng.analytics.MobclickAgent;

public class AboutActivity extends BaseActivity implements OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private TextView tvCode, tvWebsite, tvEmail, tvPhone;// 版本号，网址，邮箱，电话
	private TextView tvService;// 用户协议

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_about);
		ExitApplicaton.getInstance().addActivity(this);
		init();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("关于翼家康页"); //统计页面
		MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("关于翼家康页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_about));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		tvCode = (TextView) findViewById(R.id.about_tv_code);
		tvCode.setText(Constant.getAppVersionName(this));
		tvWebsite = (TextView) findViewById(R.id.about_tv_website);
		tvWebsite.setText("网址：http://www.yjkang.cn");
		tvEmail = (TextView) findViewById(R.id.about_tv_email);
		tvEmail.setText("邮箱：ctbriyjkang@126.com");
		tvPhone = (TextView) findViewById(R.id.about_tv_phone);
		tvPhone.setText("电话：010-5855 2118");
		tvService = (TextView) findViewById(R.id.about_tv_service);
		tvService.setText(Html
				.fromHtml("<a href=\"http://www.yjkang.cn/protocol.jsp\">翼家康用户协议</a>"));
		tvService.setMovementMethod(LinkMovementMethod.getInstance());
		CharSequence text = tvService.getText();
		if (text instanceof Spannable) {

			int end = text.length();
			Spannable sp = (Spannable) tvService.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);

			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans(); // should clear old spans
			for (URLSpan url : urls) {
				URLSpan myURLSpan = new URLSpan(url.getURL());
				style.setSpan(myURLSpan, sp.getSpanStart(url),
						sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				style.setSpan(new ForegroundColorSpan(this.getResources()
								.getColor(R.color.about_text)), sp
								.getSpanStart(url), sp.getSpanEnd(url),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置前景色
			}
			tvService.setText(style);
		}
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

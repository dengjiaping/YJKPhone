package com.sinosoft.fhcs.android.activity;
/**
 * @CopyRight: SinoSoft.
 * @Description:设备帮助页
 * @Author: wangshuangshuang.
 * @Create: 20145年5月28日.
 */
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

public class SheBeiHelpActivity extends BaseActivity implements OnClickListener{
	private String strTitle;
	private String strUrlBuy="";//购买链接
	private TextView tvTitle;
	private Button btnBack;
	private TextView tv11,tv2,tv3;
	private String deviceName;
	private TextView tv12;
	private TextView tv13;
	private LinearLayout liner1, liner2,liner3;
	private ImageView iv2, iv1,iv3;
	private LinearLayout liner4;
	private TextView tv14;
	private ImageView iv4;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_shebeihelp);
		ExitApplicaton.getInstance().addActivity(this);
		try {
			strTitle = this.getIntent().getExtras().getString("strTitle");
			deviceName = this.getIntent().getExtras().getString("deviceName");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			deviceName = null;
			e.printStackTrace();
		}
		init();
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_help2));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);

		liner1 = (LinearLayout)findViewById(R.id.shebei_help_title_layout_1);
		liner2 = (LinearLayout)findViewById(R.id.shebei_help_title_layout_2);
		liner3 = (LinearLayout)findViewById(R.id.shebei_help_title_layout_3);
		liner4 = (LinearLayout)findViewById(R.id.shebei_help_title_layout_4);


		tv11=(TextView) findViewById(R.id.shebei_help_title_1);
		tv12=(TextView) findViewById(R.id.shebei_help_title_2);
		tv13=(TextView) findViewById(R.id.shebei_help_title_3);
		tv14=(TextView) findViewById(R.id.shebei_help_title_4);

		iv1 = (ImageView)findViewById(R.id.shebei_help_iv_1);
		iv2 = (ImageView)findViewById(R.id.shebei_help_iv_2);
		iv3 = (ImageView)findViewById(R.id.shebei_help_iv_3);
		iv4 = (ImageView)findViewById(R.id.shebei_help_iv_4);


		tv3=(TextView) findViewById(R.id.shebei_help_title3);
		tv2=(TextView) findViewById(R.id.shebei_help_title2);
		tv3.setOnClickListener(this);
		tv3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		if(strTitle.equals("血压")){
			if(deviceName == null){
				liner2.setVisibility(View.GONE);
				liner3.setVisibility(View.GONE);
				liner4.setVisibility(View.GONE);
				tv11.setVisibility(View.VISIBLE);
				tv11.setText("提示：请先点击“设备选择” 按钮选择一款血压计");
				tv2.setVisibility(View.GONE);
				tv3.setVisibility(View.GONE);
			}else if(deviceName.equals("康康血压计")){
				strUrlBuy="http://www.kang.cn/user/store_product_detail.php?id=1";
				tv11.setVisibility(View.VISIBLE);
				tv11.setText("1.首先请绑好血压计的绑带后，点击【设备测量】按钮");
				imageLoader().displayImage("drawable://" + R.drawable.shebei_celiang, iv1);
				tv12.setText("2.请打开康康血压计开关");
				imageLoader().displayImage("drawable://" + R.drawable.start_kangkang, iv2);
				tv13.setText("3.连接成功后，等待测量结果");
				//imageLoader().displayImage("drawable://" + R.drawable.shebei_celiang, iv2);
				tv14.setText("提示：若要重新测量，请直接点击【设备测量】按钮");
				tv2.setText("您还没有康康血压计？请点击");
				//tv2.setVisibility(View.GONE);
				//tv3.setVisibility(View.GONE);
			}else if(deviceName.equals("天天血压计")){
				//strUrlBuy="http://www.kang.cn/user/store_product_detail.php?id=1";
				tv11.setVisibility(View.VISIBLE);
				tv11.setText("1.首先请绑好血压计的绑带后，点击【设备测量】按钮");
				imageLoader().displayImage("drawable://" + R.drawable.shebei_celiang, iv1);
				tv12.setText("2.请打开天天血压计开关");
				imageLoader().displayImage("drawable://" + R.drawable.start_tiantian, iv2);
				tv13.setText("3.连接成功后，等待测量结果");
				tv14.setText("提示：若要重新测量，请直接点击【设备测量】按钮");
				tv2.setText("您还没有天天血压计？请点击");
				//tv2.setVisibility(View.GONE);
				//tv3.setVisibility(View.GONE);
			}else if(deviceName.equals("鱼跃血压计")){

				tv11.setVisibility(View.VISIBLE);
				tv11.setText("1.首先请绑好血压计的绑带后，点击【设备测量】按钮");
				imageLoader().displayImage("drawable://" + R.drawable.shebei_celiang, iv1);
				tv12.setText("2.请打开鱼跃血压计开关");
				imageLoader().displayImage("drawable://" + R.drawable.start_yuwell_2, iv2);
				tv13.setText("3.连接成功后，等待测量结果");
				//imageLoader().displayImage("drawable://" + R.drawable.start_yuwell_2, iv3);
				tv14.setText("提示：若要重新测量，请直接点击【设备测量】按钮");
				tv2.setText("您还没有鱼跃血压计？请点击");
				//tv2.setVisibility(View.GONE);
				//tv3.setVisibility(View.GONE);
			}

		}else if(strTitle.equals("体重")){
			tv11.setVisibility(View.GONE);
			strUrlBuy="http://www.yolanda.hk/buy";
			//liner3.setVisibility(View.GONE);
			tv11.setVisibility(View.VISIBLE);
			tv11.setText("1.点击【设备测量】按钮");
			imageLoader().displayImage("drawable://" + R.drawable.shebei_celiang, iv1);
			tv12.setText("2.请站上体重秤");
			imageLoader().displayImage(null, iv2);
			tv13.setText("3.连接成功后，等待测量结果");
			imageLoader().displayImage(null, iv3);
			tv14.setText("提示：若要重新测量，请重新操作一次");
			tv2.setText("您还没有云康宝体重秤？请点击");
			tv2.setTextSize(16f);
			tv3.setTextSize(16f);
		}else if(strTitle.equals("血糖")){
			liner4.setVisibility(View.GONE);
			tv11.setVisibility(View.VISIBLE);
			tv11.setText("1.首先点击【设备测量】按钮");
			imageLoader().displayImage("drawable://" + R.drawable.shebei_celiang, iv1);
			tv12.setText("2.请在血糖仪上插入测试条");
			imageLoader().displayImage("drawable://" + R.drawable.start_sinao, iv2);
			tv13.setText("3.连接成功后吸取血样，等待10秒，去的测量结果");
			imageLoader().displayImage("drawable://" + R.drawable.start_sino_2, iv3);
			tv2.setText("您还没有三诺血糖仪？请点击");
			//tv2.setVisibility(View.GONE);
			//tv3.setVisibility(View.GONE);
		}

	}
	public ImageLoader imageLoader() {
		return ImageLoader.getInstance();
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("设备帮助页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("设备帮助页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				finish();
				break;
			case R.id.shebei_help_title3:
				if(strUrlBuy.equals("")){
					Toast.makeText(this, "暂时无法购买！", Toast.LENGTH_SHORT).show();
				}else{
					Intent intent= new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(strUrlBuy);
					intent.setData(content_url);
					startActivity(intent);
				}
			default:
				break;
		}

	}
}

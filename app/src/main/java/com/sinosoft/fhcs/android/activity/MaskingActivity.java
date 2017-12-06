package com.sinosoft.fhcs.android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;

public class MaskingActivity extends BaseActivity {

	private String strTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_masking);
		ExitApplicaton.getInstance().addActivity(this);
		strTitle = getIntent().getExtras().getString("strTitle");
		ImageView iv = (ImageView) findViewById(R.id.masking_iv);
		if(strTitle.equals("血压")){
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.bg_shebeiceliang, iv);
		}else if(strTitle.equals("血糖")){
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.masking_sannuo, iv);
		}else if(strTitle.equals("体重")){
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.masking_tizhong, iv);
		}else if(strTitle.equals("设备切换")){
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.masking_change_device, iv);
		}
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}

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

public class MaskingDeviceActivity extends BaseActivity {

	private String bloodName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_masking);
		ExitApplicaton.getInstance().addActivity(this);
		bloodName = getIntent().getExtras().getString("bloodName");
		ImageView iv = (ImageView) findViewById(R.id.masking_iv);
		if(bloodName.equals("康康血压计")){
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.masking_kangkang, iv);
		}else if(bloodName.equals("天天血压计")){
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.masking_tiantian, iv);
		}else if(bloodName.equals("鱼跃血压计")){
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.masking_yuwell, iv);
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

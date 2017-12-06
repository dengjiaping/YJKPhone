package com.sinosoft.fhcs.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterTwoActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private TextView tvTitle;
	private Button btnBack;
	private Button btnArea;// 区域
	private String strArea = "";
	private EditText edtNickName;// 昵称
	private CheckBox cbtnAccept;// 是否同意条款
	private TextView tvService;// 服务
	private Button btnCommit;
	private String strPhoneNum, strPass;
	private ViewFlipper flipperFamilymember;
	private Button btnLeftF, btnRightF;// 家庭成员左右选择
	private int mCurrPos_F = 0;// 家庭成员当前
	private List<FamilyMember> getFamilyMemberList = new ArrayList<FamilyMember>();
	// 提交
	private ProgressDialog progressDialog;// 进度条
	private static final int FailServer = 1001;// 连接超时
	private static final int OKRegister = 1004;// 注册成功
	private static final int FailRegister = 1005;// 注册失败
	private int PF = 1000;
	private String province = "";
	private String city = "";
	private String district = "";
	private String provinceId = "";
	private String cityId = "";
	private String districtId = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_registertwo);
		ExitApplicaton.getInstance().addActivity(this);
		strPhoneNum = this.getIntent().getExtras().getString("strPhoneNum");
		strPass = this.getIntent().getExtras().getString("strPass");

		init();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("注册页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("注册页"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause
		// 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void initData() {
		getFamilyMemberList=Constant.getYzList();
		setViewFamily(mCurrPos_F, 0);

	}

	private void init() {
		// titlebar
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_register));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		edtNickName = (EditText) findViewById(R.id.regtwo_edt_nickname);
		cbtnAccept = (CheckBox) findViewById(R.id.regtwo_cbtn_accept);
		btnCommit = (Button) findViewById(R.id.regtwo_btn_finish);
		btnCommit.setOnClickListener(this);
		cbtnAccept.setOnCheckedChangeListener(this);
		cbtnAccept.setChecked(true);
		btnArea = (Button) findViewById(R.id.regtwo_edt_area);
		btnArea.setOnClickListener(this);
		// 家庭成员
		flipperFamilymember = (ViewFlipper) this
				.findViewById(R.id.regtwo_flipper_familymember);
		btnLeftF = (Button) findViewById(R.id.regtwo_btn_leftF);
		btnRightF = (Button) findViewById(R.id.regtwo_btn_rightF);
		btnLeftF.setOnClickListener(this);
		btnRightF.setOnClickListener(this);
		initData();
		// 服务
		tvService = (TextView) findViewById(R.id.regtwo_tv_service);
		tvService
				.setText(Html
						.fromHtml("<a href=\"http://www.yjkang.cn/protocol.jsp\">用户协议</a>"));
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
								.getColor(R.color.text_reg_black)), sp
								.getSpanStart(url), sp.getSpanEnd(url),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置前景色
			}
			tvService.setText(style);
		}
	}

	// 家庭成员View
	private void setViewFamily(int curr, int next) {
		View v = (View) LayoutInflater.from(this).inflate(
				R.layout.registertwo_flipper_item, null);
		ImageView iv = (ImageView) v.findViewById(R.id.regtwoflipper_img);
		TextView tv = (TextView) v.findViewById(R.id.regtwoflipper_tv);
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		if (curr < next && next > getFamilyMemberList.size() - 1)
			next = 0;
		else if (curr > next && next < 0)
			next = getFamilyMemberList.size() - 1;
		iv.setImageResource(Constant.ImageId(
				((FamilyMember) getFamilyMemberList.get(next))
						.getFamilyRoleName(),
				((FamilyMember) getFamilyMemberList.get(next)).getGender()));
		tv.setText(((FamilyMember) getFamilyMemberList.get(next))
				.getFamilyRoleName());
		if (flipperFamilymember.getChildCount() > 1) {
			flipperFamilymember.removeViewAt(0);
		}

		flipperFamilymember.addView(v, flipperFamilymember.getChildCount());
		mCurrPos_F = next;

	}

	// 家庭成员左
	private void movePreviousF() {
		setViewFamily(mCurrPos_F, mCurrPos_F - 1);
		flipperFamilymember.setInAnimation(this, R.anim.push_left_in);
		flipperFamilymember.setOutAnimation(this, R.anim.push_left_out);
		flipperFamilymember.showPrevious();
	}

	// 家庭成员右
	private void moveNextF() {
		setViewFamily(mCurrPos_F, mCurrPos_F + 1);
		flipperFamilymember.setInAnimation(this, R.anim.push_right_in);
		flipperFamilymember.setOutAnimation(this, R.anim.push_right_out);
		flipperFamilymember.showNext();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.regtwo_btn_leftF:
				// 家庭成员左
				if (getFamilyMemberList.size() != 0) {
					movePreviousF();
				}
				break;
			case R.id.regtwo_btn_rightF:
				// 家庭成员右
				if (getFamilyMemberList.size() != 0) {
					moveNextF();
				}
				break;
			case R.id.regtwo_edt_area:
				// 区域选择
				Intent intent = new Intent(this, ChoiceCityActivity.class);
				intent.putExtra("flag", "register");
				intent.putExtra("province", province);
				intent.putExtra("city", city);
				intent.putExtra("district", district);
				intent.putExtra("provinceId", provinceId);
				intent.putExtra("cityId", cityId);
				intent.putExtra("districtId", districtId);
				startActivityForResult(intent, 0);
				break;
			case R.id.regtwo_btn_finish:
				// 提交
				if (HttpManager.isNetworkAvailable(this)) {
					String strNickName = edtNickName.getText().toString().trim();
					if (strNickName.equals("")) {
						Toast.makeText(this, "昵称不能为空！", Toast.LENGTH_SHORT).show();
						return;
					}

					if (strArea.equals("")) {
						Toast.makeText(this, "请选择地区！", Toast.LENGTH_SHORT).show();
						return;
					}

					// 验证码上一步验证完成


					if(province.equals("")){
						provinceId="";
					}
					if(city.equals("")){
						cityId="";
					}
					if(district.equals("")){
						districtId="";
					}
					RegisterRequest req = new RegisterRequest();
					req.execute(HttpManager.urlRegister(strPhoneNum,
							strNickName, strPass,provinceId,
							cityId, districtId,getFamilyMemberList.get(mCurrPos_F).getGender(),
							getFamilyMemberList.get(mCurrPos_F).getBirthday(),getFamilyMemberList.get(mCurrPos_F).getFamilyRoleName(),
							getFamilyMemberList.get(mCurrPos_F).getWeight()+"",getFamilyMemberList.get(mCurrPos_F).getHeight()+"",
							getFamilyMemberList.get(mCurrPos_F).getStepSize()+"",
							getFamilyMemberList.get(mCurrPos_F).getWaist()+""));
				} else {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
				}

				break;
		}

	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked == true) {
			btnCommit.setEnabled(true);
		} else {
			btnCommit.setEnabled(false);
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode) {
			if (data != null) {
				province = data.getStringExtra("province");
				city = data.getStringExtra("city");
				district = data.getStringExtra("district");
				provinceId = data.getStringExtra("provinceId");
				cityId = data.getStringExtra("cityId");
				districtId = data.getStringExtra("districtId");

				strArea = province + city + district;
				if (strArea.trim().equals("")) {
					btnArea.setText("");
				} else {
					btnArea.setText(strArea);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	// 注册请求结果
	private void initResultReg(String errormsg) {
		if (PF == FailServer) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailRegister) {
			if (errormsg.equals("")) {
				Toast.makeText(this, "注册失败!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show();
			}
		} else if (PF == OKRegister) {
			Toast.makeText(this, "注册成功!", Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(this,LoginActivity.class);
			startActivity(intent);
		}

	}

	// 注册请求
	private class RegisterRequest extends AsyncTask<Object, Void, String> {
		private String urlReg;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(RegisterTwoActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			urlReg = (String) params[0];
			Log.e("urlReg", urlReg + "");
			result = HttpManager.phoneSessionFromGet(urlReg,
					Constant.sessionIdPhone);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResultReg("");
			} else {
				try {
					String errormsg = "";
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OKRegister;
						initResultReg("");
					} else {
						if (!jo.isNull("errormsg")) {
							errormsg = jo.optString("errormsg");
						}
						PF = FailRegister;
						initResultReg(errormsg);
					}

				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = FailRegister;
					initResultReg("");
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

package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:添加家庭成员页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.MemberGridViewAdapter;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class AddFamilyMemberActivity extends BaseActivity implements
		OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private Button btnSure;// 确定
	private EditText edtPhone;// 手机号
	private EditText edtCode;// 验证码
	private Button btnGetCode;// 获取验证码
	public static Button btnHeight, btnWeight, btnStepSize, btnWaist;;// 身高，体重，步长，腰围
	private EditText edtRoleName;// 角色名
	private Button btnRoleName;// 预置的角色
	private RelativeLayout btnBirth;// 选择日期
	private ImageView add_iv_man, add_iv_woman;// 男，女
	public static TextView tvBirth;// 出生日期
	private Calendar calendar;
	// 自定义的弹出框类
	private FamilyMemberDialog2 menuDialog;
	private List<FamilyMember> fmList = new ArrayList<FamilyMember>();
	/**
	 * 网络请求
	 */
	private ProgressDialog progressDialog;// 进度条
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private static final int OKGetYZM = 1004;// 验证码获取成功
	private static final int FailGetYZM = 1005;// 验证码获取失败
	private static final int OKJiaoYanYZM = 1006;// 验证码校验成功
	private static final int FailJiaoYanYZM = 1007;// 验证码校验失败
	private int PF = 1000;
	private String userId = "";
	private String sex = "男";
	private String roleName = "";
	private String birth = "";
	private String weight = "";
	private String height = "";
	private String stepSize = "";
	private String waist = "";
	private String gender = "1";
	private String currentTime = "";

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_addmember);
		ExitApplicaton.getInstance().addActivity(this);
		fmList = (List<FamilyMember>) this.getIntent().getExtras().get("list");
		init();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("添加家庭成员页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("添加家庭成员页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_addmember));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		// 手机号
		edtPhone = (EditText) findViewById(R.id.add_edt_phone);
		edtCode = (EditText) findViewById(R.id.add_edt_yzm);
		btnGetCode = (Button) findViewById(R.id.add_btn_getcode);
		btnGetCode.setOnClickListener(this);
		// 性别
		add_iv_man = (ImageView) findViewById(R.id.add_iv_man);
		add_iv_woman = (ImageView) findViewById(R.id.add_iv_woman);
		add_iv_man.setOnClickListener(this);
		add_iv_woman.setOnClickListener(this);
		// 确定
		btnSure = (Button) findViewById(R.id.add_btn_sure);
		btnSure.setOnClickListener(this);
		// 角色
		btnRoleName = (Button) findViewById(R.id.add_btn_rolename);
		btnRoleName.setOnClickListener(this);
		edtRoleName = (EditText) findViewById(R.id.add_edt_rolename);
		// 身高体重步长腰围
		btnHeight = (Button) findViewById(R.id.add_edt_height);
		btnWeight = (Button) findViewById(R.id.add_edt_weight);
		btnStepSize = (Button) findViewById(R.id.add_edt_stepsize);
		btnWaist = (Button) findViewById(R.id.add_edt_waist);
		btnHeight.setOnClickListener(this);
		btnWeight.setOnClickListener(this);
		btnStepSize.setOnClickListener(this);
		btnWaist.setOnClickListener(this);
		// 日期
		tvBirth = (TextView) findViewById(R.id.add_tv_birth);
		btnBirth = (RelativeLayout) findViewById(R.id.add_btn_birth);
		btnBirth.setOnClickListener(this);
		calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		tvBirth.setText(simpleDateFormat.format(calendar.getTime()));
		currentTime=simpleDateFormat.format(calendar.getTime());
		initMan(true);// 初始化性别
	}

	// 男
	private void initMan(boolean first) {
		add_iv_man.setBackgroundResource(R.drawable.man2);
		add_iv_woman.setBackgroundResource(R.drawable.woman1);
		sex = "男";
		if (first) {
			btnHeight.setText("175");
			btnWeight.setText("60");
			btnStepSize.setText("65");
			btnWaist.setText("74");
		}

	}

	// 女
	private void initWoman(boolean first) {
		add_iv_man.setBackgroundResource(R.drawable.man1);
		add_iv_woman.setBackgroundResource(R.drawable.woman2);
		sex = "女";
		if (first) {
			btnHeight.setText("160");
			btnWeight.setText("50");
			btnStepSize.setText("55");
			btnWaist.setText("66");
		}
	}

	private void updateDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		tvBirth.setText(simpleDateFormat.format(calendar.getTime()));
	}

	// 返回
	private void goback() {
		if (edtRoleName.getText().toString().trim().equals("")) {
			finish();
		} else {
			Constant.showDialog2(this, "尚未完成添加，是否要离开本页面？");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			goback();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				goback();
				break;
			case R.id.add_edt_height:
				// 身高
				Constant.showNum(this, btnHeight, btnHeight.getText().toString()
						.trim(), "height", "addMember", 0, 500);
				break;
			case R.id.add_edt_weight:
				// 体重
				Constant.showNum(this, btnWeight, btnWeight.getText().toString()
						.trim(), "weight", "addMember", 0, 500);
				break;
			case R.id.add_edt_stepsize:
				// 步长
				Constant.showNum(this, btnStepSize, btnStepSize.getText()
						.toString().trim(), "stepSize", "addMember", 0, 500);
				break;
			case R.id.add_edt_waist:
				// 腰围
				Constant.showNum(this, btnWaist, btnWaist.getText().toString()
						.trim(), "waist", "addMember", 0, 500);
				break;
			case R.id.add_btn_getcode:
				// 获取验证码
				if (!HttpManager.isNetworkAvailable(this)) {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
					return;
				}
				String strPhone = edtPhone.getText().toString().trim();
				if (strPhone.equals("")) {
					Toast.makeText(this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!Constant.isPhone(strPhone)) {
					Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
					return;
				}
				GetYZMRequest request = new GetYZMRequest();
				request.execute(HttpManager.urlYZMMember(strPhone));
				break;
			case R.id.add_btn_sure:
				// 确定
				// 从首选项获取userId
				SharedPreferences prefs = getSharedPreferences("UserInfo",
						Context.MODE_PRIVATE);
				userId = prefs.getString("userId", "");
				roleName = edtRoleName.getText().toString().trim();
				birth = tvBirth.getText().toString().trim();
				weight = btnWeight.getText().toString().trim();
				height = btnHeight.getText().toString().trim();
				stepSize = btnStepSize.getText().toString().trim();
				waist = btnWaist.getText().toString().trim();
				gender = "1";
				if (sex.equals("男")) {
					gender = "1";
				} else {
					gender = "0";
				}
				if (roleName.equals("") || birth.equals("") || weight.equals("")
						|| height.equals("") || stepSize.equals("")
						|| waist.equals("") || gender.equals("")) {
					Toast.makeText(this, "昵称是必填项，请填写！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				int result1 = currentTime.compareTo(birth);
				if (result1 < 0) {
					Toast.makeText(this, "出生年月日不能大于当前日期！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (edtPhone.getText().toString().trim().equals("")) {
					if (HttpManager.isNetworkAvailable(this)) {
						AddMemberRequest re = new AddMemberRequest();
						re.execute(HttpManager.urlAddFamilyMember(userId, gender,
								birth, roleName, weight, height, stepSize, waist,
								edtPhone.getText().toString().trim()));
					} else {
//					Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
						Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
					}
				} else {
					if (!Constant.isPhone(edtPhone.getText().toString().trim())) {
						Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					if (edtCode.getText().toString().trim().equals("")) {
						Toast.makeText(this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
						return;
					}
					if (HttpManager.isNetworkAvailable(this)) {
						JiaoYanYZMRequest re = new JiaoYanYZMRequest();
						re.execute(HttpManager.urlJiaoYanYZM(edtPhone.getText()
								.toString().trim(), edtCode.getText().toString()
								.trim()));
					} else {
//					Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
						Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
					}
				}

				break;
			case R.id.add_btn_rolename:
				// 选择角色
				menuDialog = new FamilyMemberDialog2(AddFamilyMemberActivity.this,
						fmList, R.style.MyDialogStyle);
				Window window = menuDialog.getWindow();
				WindowManager.LayoutParams l = window.getAttributes();
				l.x = 0;
				l.y = 25;
				window.setAttributes(l);
				menuDialog.show();
				break;
			case R.id.add_btn_birth:
				// 选择日期
				// new DatePickerDialog(AddFamilyMemberActivity.this, listener,
				// calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				// calendar.get(Calendar.DAY_OF_MONTH)).show();
				Constant.showDate(this, tvBirth, tvBirth.getText().toString()
						.trim(), "addMember");
				break;
			case R.id.add_iv_man:
				// 男
				if (edtRoleName.getText().toString().equals("爷爷")
						|| edtRoleName.getText().toString().equals("奶奶")
						|| edtRoleName.getText().toString().equals("外公")
						|| edtRoleName.getText().toString().equals("外婆")
						|| edtRoleName.getText().toString().equals("爸爸")
						|| edtRoleName.getText().toString().equals("妈妈")
						|| edtRoleName.getText().toString().equals("儿子")
						|| edtRoleName.getText().toString().equals("女儿")
						|| edtRoleName.getText().toString().equals("小儿子")
						|| edtRoleName.getText().toString().equals("小女儿")
						|| edtRoleName.getText().toString().equals("来宾男")
						|| edtRoleName.getText().toString().equals("来宾女")) {
					initMan(false);
				} else {
					initMan(true);
				}

				break;
			case R.id.add_iv_woman:
				// 女
				if (edtRoleName.getText().toString().equals("爷爷")
						|| edtRoleName.getText().toString().equals("奶奶")
						|| edtRoleName.getText().toString().equals("外公")
						|| edtRoleName.getText().toString().equals("外婆")
						|| edtRoleName.getText().toString().equals("爸爸")
						|| edtRoleName.getText().toString().equals("妈妈")
						|| edtRoleName.getText().toString().equals("儿子")
						|| edtRoleName.getText().toString().equals("女儿")
						|| edtRoleName.getText().toString().equals("小儿子")
						|| edtRoleName.getText().toString().equals("小女儿")
						|| edtRoleName.getText().toString().equals("来宾男")
						|| edtRoleName.getText().toString().equals("来宾女")) {
					initWoman(false);
				} else {
					initWoman(true);
				}
				break;
			default:
				break;
		}

	}

	// 预置家庭成员对话框
	public class FamilyMemberDialog2 extends Dialog {
		private Context context;
		private GridView gridview;
		private List<FamilyMember> yzList = new ArrayList<FamilyMember>();// 预置家庭列表
		private List<FamilyMember> getList = new ArrayList<FamilyMember>();// 已置家庭列表
		private MemberGridViewAdapter adapter;

		public FamilyMemberDialog2(Context context, List<FamilyMember> list,
								   int theme) {
			super(context, theme);
			this.context = context;
			this.getList = list;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			FamilyMemberDialog2.this.dismiss();
			return super.onTouchEvent(event);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			this.setContentView(R.layout.member_dialog);
			gridview = (GridView) findViewById(R.id.member_dialog_gridview);
			initYZData();
			adapter = new MemberGridViewAdapter(context, yzList);
			gridview.setAdapter(adapter);
			gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
										int arg2, long arg3) {
					edtRoleName.setText(yzList.get(arg2).getFamilyRoleName());
					btnHeight.setText((int) yzList.get(arg2).getHeight() + "");
					btnWeight.setText((int) yzList.get(arg2).getWeight() + "");
					btnStepSize.setText((int) yzList.get(arg2).getStepSize()
							+ "");
					btnWaist.setText((int) yzList.get(arg2).getWaist() + "");
					if (yzList.get(arg2).getGender().trim().equals("1")) {
						initMan(false);
					} else {
						initWoman(false);
					}
					calendar.set(
							Calendar.YEAR,
							Integer.valueOf(Constant.getSplitDate(yzList.get(
									arg2).getBirthday())[0]));
					calendar.set(
							Calendar.MONTH,
							Integer.valueOf(Constant.getSplitDate(yzList.get(
									arg2).getBirthday())[1]) - 1);
					calendar.set(
							Calendar.DAY_OF_MONTH,
							Integer.valueOf(Constant.getSplitDate(yzList.get(
									arg2).getBirthday())[2]));
					updateDate();
					FamilyMemberDialog2.this.dismiss();
				}
			});
		}

		// 预置信息
		private void initYZData() {
			yzList = Constant.getYzList();
			if (getList.size() != 0) {
				for (int i = 0; i < getList.size(); i++) {
					for (int j = 0; j < yzList.size(); j++) {
						if (getList.get(i).getFamilyRoleName()
								.equals(yzList.get(j).getFamilyRoleName())) {
							yzList.remove(j);

						}
					}
				}
			}
		}
	}

	// 添加
	private class AddMemberRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(AddFamilyMemberActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("AddMemberUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OK;
						initResult();
					} else {
						PF = Fail;
						initResult();
					}
				} catch (JSONException e) {
					PF = Fail;
					initResult();
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

	// 获取验证码
	private class GetYZMRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(AddFamilyMemberActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("GetCodeUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						PF = OKGetYZM;
						initResult();
					} else {
						PF = FailGetYZM;
						initResult();
					}
				} catch (JSONException e) {
					PF = FailGetYZM;
					initResult();
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

	// 校验验证码
	private class JiaoYanYZMRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(AddFamilyMemberActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("JiaoYanCodeUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initResult();
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("status");
					if (resultCode.equals("0000")) {
						PF = OKJiaoYanYZM;
						initResult();
					} else {
						PF = FailJiaoYanYZM;
						initResult();
					}
				} catch (JSONException e) {
					PF = FailJiaoYanYZM;
					initResult();
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

	private void initResult() {
		if (PF == FailServer) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			Toast.makeText(this, "添加失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
			finish();
		} else if (PF == OKGetYZM) {
			Toast.makeText(this, "验证码已发送，请注意查收!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailGetYZM) {
			Toast.makeText(this, "验证码发送失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKJiaoYanYZM) {
			// Toast.makeText(this, "验证码校验成功!", Toast.LENGTH_SHORT).show();
			if (HttpManager.isNetworkAvailable(this)) {
				AddMemberRequest re = new AddMemberRequest();
				re.execute(HttpManager.urlAddFamilyMember(userId, gender,
						birth, roleName, weight, height, stepSize, waist,
						edtPhone.getText().toString().trim()));
			} else {
//				Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
				Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			}
		} else if (PF == FailJiaoYanYZM) {
			Toast.makeText(this, "验证码输入有误!", Toast.LENGTH_SHORT).show();
		}
	}

}

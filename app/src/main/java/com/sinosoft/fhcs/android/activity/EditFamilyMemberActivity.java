package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:修改家庭成员页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class EditFamilyMemberActivity extends BaseActivity implements
		OnClickListener {
	private TextView tvTitle;
	private Button btnBack;
	private Button btnDelete;
	private Button btnSure;// 确定
	private CircleImageView ivRole;// 角色
	private TextView tvRole;
	private EditText edtPhone;// 手机号
	private EditText edtCode;// 验证码
	private Button btnGetCode;// 获取验证码
	public static Button btnHeight, btnWeight, btnStepSize, btnWaist;// 身高，体重，步长，腰围
	private RelativeLayout btnBirth;// 选择日期
	public static TextView tvBirth;// 出生日期
	private Calendar calendar;
	private FamilyMember member = new FamilyMember();
	/**
	 * 网络请求
	 */
	private ProgressDialog progressDialog;// 进度条
	private ProgressDialog progressDialog2;
	private static final int OK = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int Fail = 1003;// 失败
	private static final int OKGetYZM = 1004;// 验证码获取成功
	private static final int FailGetYZM = 1005;// 验证码获取失败
	private static final int OKJiaoYanYZM = 1006;// 验证码校验成功
	private static final int FailJiaoYanYZM = 1007;// 验证码校验失败
	private static final int OKDelete = 2001;// 删除成功
	private static final int FailDelete = 2002;// 删除失败
	private int PF = 1000;
	private String userId = "";
	private long roleId = 0;
	private String birth = "";
	private String weight = "";
	private String height = "";
	private String stepSize = "";
	private String waist = "";
	private String currentTime = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_editmember);
		ExitApplicaton.getInstance().addActivity(this);
		member = (FamilyMember) this.getIntent().getExtras().get("member");
		// 从首选项获取userId
		SharedPreferences prefs = this.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		init();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("修改家庭成员页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("修改家庭成员页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_editmember));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		btnDelete = (Button) findViewById(R.id.titlebar_btn_delete);
		btnDelete.setVisibility(View.VISIBLE);
		btnDelete.setOnClickListener(this);
		// 手机号
		edtPhone = (EditText) findViewById(R.id.edit_edt_phone);
		edtCode = (EditText) findViewById(R.id.edit_edt_yzm);
		btnGetCode = (Button) findViewById(R.id.edit_btn_getcode);
		btnGetCode.setOnClickListener(this);
		// 角色
		ivRole = (CircleImageView) findViewById(R.id.edit_iv_role);
		tvRole = (TextView) findViewById(R.id.edit_tv_role);
		tvRole.setText(member.getFamilyRoleName());
		ivRole.setImageResource(Constant.ImageId(member.getFamilyRoleName(),
				member.getGender()));
		// 确定
		btnSure = (Button) findViewById(R.id.edit_btn_sure);
		btnSure.setOnClickListener(this);
		// 身高体重步长腰围
		btnHeight = (Button) findViewById(R.id.edit_edt_height);
		btnWeight = (Button) findViewById(R.id.edit_edt_weight);
		btnStepSize = (Button) findViewById(R.id.edit_edt_stepsize);
		btnWaist = (Button) findViewById(R.id.edit_edt_waist);
		btnHeight.setOnClickListener(this);
		btnWeight.setOnClickListener(this);
		btnStepSize.setOnClickListener(this);
		btnWaist.setOnClickListener(this);
		if (member.getMobile() == null || member.getMobile().equals("")) {
			edtPhone.setText("");
		} else {
			edtPhone.setText(member.getMobile());
		}
		btnHeight.setText((int) member.getHeight() + "");
		btnWeight.setText((int) member.getWeight() + "");
		btnWaist.setText((int) member.getWaist() + "");
		btnStepSize.setText((int) member.getStepSize() + "");
		// 日期
		tvBirth = (TextView) findViewById(R.id.edit_tv_birth);
		btnBirth = (RelativeLayout) findViewById(R.id.edit_btn_birth);
		btnBirth.setOnClickListener(this);
		calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		currentTime = simpleDateFormat.format(calendar.getTime());
		calendar.set(Calendar.YEAR,
				Integer.valueOf(Constant.getSplitDate(member.getBirthday())[0]));
		calendar.set(
				Calendar.MONTH,
				Integer.valueOf(Constant.getSplitDate(member.getBirthday())[1]) - 1);
		calendar.set(Calendar.DAY_OF_MONTH,
				Integer.valueOf(Constant.getSplitDate(member.getBirthday())[2]));
		updateDate();
	}

	// 返回
	private void goback() {
		if (tvBirth
				.getText()
				.toString()
				.trim()
				.equals(Constant.getSplitDate(member.getBirthday())[0] + "-"
						+ Constant.getSplitDate(member.getBirthday())[1] + "-"
						+ Constant.getSplitDate(member.getBirthday())[2])
				&& btnWeight.getText().toString().trim()
				.equals((int) member.getWeight() + "")
				&& btnHeight.getText().toString().trim()
				.equals((int) member.getHeight() + "")
				&& btnStepSize.getText().toString().trim()
				.equals((int) member.getStepSize() + "")
				&& btnWaist.getText().toString().trim()
				.equals((int) member.getWaist() + "")
				&& edtPhone.getText().toString().trim()
				.equals(member.getMobile() + "")) {
			finish();
		} else {
			Constant.showDialog2(this, "尚未完成修改，是否要离开本页面？");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (PF == OK) {
				finish();
			} else {
				goback();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				if (PF == OK) {
					finish();
				} else {
					goback();
				}
				break;
			case R.id.edit_btn_getcode:
				// 获取验证码
				if (!HttpManager.isNetworkAvailable(this)) {
					// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
					Toast.makeText(this, "您的网络没连接好，请检查后重试！",

							Toast.LENGTH_SHORT).show();
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
			case R.id.edit_edt_height:
				// 身高
				Constant.showNum(this, btnHeight, btnHeight.getText().toString()
						.trim(), "height", "editMember", 0, 500);
				break;
			case R.id.edit_edt_weight:
				// 体重
				Constant.showNum(this, btnWeight, btnWeight.getText().toString()
						.trim(), "weight", "editMember", 0, 500);
				break;
			case R.id.edit_edt_stepsize:
				// 步长
				Constant.showNum(this, btnStepSize, btnStepSize.getText()
						.toString().trim(), "stepSize", "editMember", 0, 500);
				break;
			case R.id.edit_edt_waist:
				// 腰围
				Constant.showNum(this, btnWaist, btnWaist.getText().toString()
						.trim(), "waist", "editMember", 0, 500);
				break;

			case R.id.titlebar_btn_delete:
				// 删除
				deleteDialog();
				break;
			case R.id.edit_btn_sure:
				// 确定
				roleId = member.getId();
				birth = tvBirth.getText().toString().trim();
				weight = btnWeight.getText().toString().trim();
				height = btnHeight.getText().toString().trim();
				stepSize = btnStepSize.getText().toString().trim();
				waist = btnWaist.getText().toString().trim();
				int result1 = currentTime.compareTo(birth);
				if (result1 < 0) {
					Toast.makeText(this, "出生年月日不能大于当前日期！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (edtPhone.getText().toString().trim().equals("")
						|| edtPhone.getText().toString().trim()
						.equals(member.getMobile().toString().trim())) {
					if (HttpManager.isNetworkAvailable(this)) {
						EditMemberRequest re = new EditMemberRequest();
						re.execute(HttpManager.urlEditFamilyMember(userId, roleId,
								member.getGender(), birth, weight, height,
								stepSize, waist, edtPhone.getText().toString()
										.trim()));
					} else {
						// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
						Toast.makeText(this, "您的网络没连接好，请检查后重试！",

								Toast.LENGTH_SHORT).show();
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
						// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
						Toast.makeText(this, "您的网络没连接好，请检查后重试！",

								Toast.LENGTH_SHORT).show();
					}

				}

				break;
			case R.id.edit_btn_birth:
				// 选择日期
				// new DatePickerDialog(this, listener,
				// calendar.get(Calendar.YEAR),
				// calendar.get(Calendar.MONTH),
				// calendar.get(Calendar.DAY_OF_MONTH)).show();
				Constant.showDate(this, tvBirth, tvBirth.getText().toString()
						.trim(), "editMember");

				break;
			default:
				break;
		}
	}

	private void updateDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		tvBirth.setText(simpleDateFormat.format(calendar.getTime()));
	}

	private void deleteDialog() {
		new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
				.setTitle("温馨提示")
				.setMessage("您确定要删除该角色吗？")
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (HttpManager
								.isNetworkAvailable(EditFamilyMemberActivity.this)) {
							DeleteMemberRequest re = new DeleteMemberRequest();
							re.execute(HttpManager.urlDeleteFamilyMember(
									userId, member.getId()));
						} else {
							Toast.makeText(EditFamilyMemberActivity.this,
									"您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT)
									.show();
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	// 修改
	private class EditMemberRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(EditFamilyMemberActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("EditMemberUrl", url + "");
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
			progressDialog = new ProgressDialog(EditFamilyMemberActivity.this);
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
			progressDialog2 = new ProgressDialog(EditFamilyMemberActivity.this);
			Constant.showProgressDialog(progressDialog2);
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
			Constant.exitProgressDialog(progressDialog2);
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}

	// 删除
	private class DeleteMemberRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(EditFamilyMemberActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("DeleteMemberUrl", url + "");
			result = HttpManager.getStringContent(url);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.toString().trim().equals("ERROR")) {
				PF = FailServer;
				initDeleteResult("");
			} else {
				try {
					JSONObject jo = new JSONObject(result);
					String resultCode = jo.optString("resultCode");
					String message = jo.optString("message");
					if (resultCode.equals("1")) {
						PF = OKDelete;
						initDeleteResult("");
					} else {
						PF = FailDelete;
						initDeleteResult(message);
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = FailDelete;
					initDeleteResult("");
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

	private void initDeleteResult(String message) {
		if (PF == FailServer) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKDelete) {
			Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
			finish();
		} else if (PF == FailDelete) {
			if (message.equals("")) {
				Toast.makeText(this, "删除失败！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			}

		}

	}

	private void initResult() {
		if (PF == FailServer) {
			// Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == Fail) {
			Toast.makeText(this, "修改失败！", Toast.LENGTH_SHORT).show();
		} else if (PF == OK) {
			Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
		} else if (PF == OKGetYZM) {
			Toast.makeText(this, "验证码已发送，请注意查收!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailGetYZM) {
			Toast.makeText(this, "验证码发送失败!", Toast.LENGTH_SHORT).show();
		} else if (PF == OKJiaoYanYZM) {
			// Toast.makeText(this, "验证码校验成功!", Toast.LENGTH_SHORT).show();
			if (HttpManager.isNetworkAvailable(this)) {
				EditMemberRequest re = new EditMemberRequest();
				re.execute(HttpManager.urlEditFamilyMember(userId, roleId,
						member.getGender(), birth, weight, height, stepSize,
						waist, edtPhone.getText().toString().trim()));
			} else {
				// Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
				Toast.makeText(this, "您的网络没连接好，请检查后重试！",

						Toast.LENGTH_SHORT).show();
			}
		} else if (PF == FailJiaoYanYZM) {
			Toast.makeText(this, "验证码输入有误!", Toast.LENGTH_SHORT).show();
		}
	}
}

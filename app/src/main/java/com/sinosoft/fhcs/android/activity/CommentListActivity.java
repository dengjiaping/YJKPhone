package com.sinosoft.fhcs.android.activity;
/**
 * @CopyRight: SinoSoft.
 * @Description:评论列表页
 * @Author: wangshuangshuang.
 * @Create: 2015年1月22日.
 */
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.CommentListviewAdapter;
import com.sinosoft.fhcs.android.entity.CommentInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CommentListActivity extends BaseActivity implements OnClickListener{
	private String userId="";
	private String nickName="";//user昵称
	private String imgUrl="";//user图片
	private String raceId="";
	private TextView tvTitle;
	private Button btnBack;// 返回
	private ListView listView;
	private List<CommentInfo>getList=new ArrayList<CommentInfo>();
	private List<CommentInfo>allList=new ArrayList<CommentInfo>();
	private List<CommentInfo>sendList=new ArrayList<CommentInfo>();
	private CommentListviewAdapter adapter;
	private EditText edtContent;//内容
	private Button btnSend;//发送
	/**
	 * 网络请求
	 */
	private ProgressDialog progressDialog;// 进度条
	private static final int OKGet = 1001;// 成功
	private static final int FailServer = 1002;// 连接超时
	private static final int FailGet = 1003;// 失败
	private static final int FailNoData = 1004;// 没有数据
	private static final int OKSend = 1005;// 成功
	private static final int FailSend = 1006;// 失败
	private int PF = 1000;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_commentlist);
		ExitApplicaton.getInstance().addActivity(this);
		raceId=this.getIntent().getExtras().getString("raceId");
		// 从首选项获取userId
		SharedPreferences prefs = getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		userId = prefs.getString("userId", "");
		nickName = prefs.getString("nickName", "");
		imgUrl = prefs.getString("imgUrl", "");
		init();// 初始化控件
		if (!HttpManager.isNetworkAvailable(this)) {
//			Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
			Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
			return;
		}
		GetCommentListRequest re = new GetCommentListRequest();
		re.execute(HttpManager.urlGetCommentList(userId, raceId));
	}

	private void init() {
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_commentlist));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		edtContent=(EditText) findViewById(R.id.commentlist_edt_content);
		btnSend=(Button) findViewById(R.id.commentlist_btn_add);
		btnSend.setOnClickListener(this);
		listView=(ListView) findViewById(R.id.commentlist_listview);
		adapter=new CommentListviewAdapter(this, allList);
		listView.setAdapter(adapter);
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("评论列表页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("评论列表页"); // 保证 onPageEnd 在onPause 之前调用
		MobclickAgent.onPause(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				finish();
				break;
			case R.id.commentlist_btn_add:
				//发送评论
				if(edtContent.getText().toString().trim().equals("")){
					Toast.makeText(this, "评论内容不能为空！", Toast.LENGTH_SHORT).show();
				}else{
					if (!HttpManager.isNetworkAvailable(this)) {
//					Constant.showDialog(this, "您的网络没连接好，请检查后重试！");
						Toast.makeText(this, "您的网络没连接好，请检查后重试！", Toast.LENGTH_SHORT).show();
						return;
					}
					SendRequest request=new SendRequest();
					request.execute(HttpManager.urlSendComment(userId, raceId, edtContent.getText().toString().trim()));
				}
				break;
			default:
				break;
		}

	}
	//请求结果
	@SuppressLint("SimpleDateFormat")
	private void initResult() {
		if (PF == FailServer) {
//			Constant.showDialog(this, "服务器响应超时!");
			Toast.makeText(this, "服务器响应超时!", Toast.LENGTH_SHORT).show();
		} else if (PF == FailGet) {
			Toast.makeText(this, "获取数据失败!", Toast.LENGTH_SHORT).show();
		} else if(PF==FailNoData){
//			Toast.makeText(this, "目前还没有评论数据，请添加!", Toast.LENGTH_SHORT).show();
		}else if (PF == OKGet) {
			allList.addAll(getList);
			adapter.notifyDataSetChanged();
		}else if(PF==OKSend){
			Calendar c=Calendar.getInstance();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			CommentInfo info=new CommentInfo("", HttpManager.m_imageUrl+imgUrl, nickName, sf.format(c.getTime()), edtContent.getText().toString().trim());
			sendList.add(info);
			Collections.reverse(sendList);// 将ArrayLista中的元素进行倒序
			allList.clear();
			allList.addAll(sendList);
			allList.addAll(getList);
			adapter.notifyDataSetChanged();
			edtContent.setText("");
		}else if(PF==FailSend){
			Toast.makeText(this, "发送失败!", Toast.LENGTH_SHORT).show();
		}

	}
	// 网络请求
	private class GetCommentListRequest extends
			AsyncTask<Object, Void, String> {
		private String url;
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(CommentListActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("getCommentListUrl", url + "");
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
						JSONObject jo2 = jo.getJSONObject("data");
						JSONArray ja = jo2.getJSONArray("data");
						if (ja.length() == 0) {
							PF = FailNoData;
							initResult();
						} else {
							allList.clear();
							getList.clear();
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jo4 = ja.getJSONObject(i);
								String commentTime = jo4.optString("commentTime");
								String nickName = jo4.optString("nickName");
								String comment = jo4.optString("comment");
								String avatarPath = jo4.optString("avatarPath");
								CommentInfo info=new CommentInfo("", HttpManager.m_imageUrl+avatarPath, nickName, commentTime, comment);
								getList.add(info);
							}
							PF = OKGet;
							initResult();
						}
					} else {
						PF = FailNoData;
						initResult();
					}
				} catch (JSONException e) {
					System.out.println("解析错误");
					PF = FailGet;
					initResult();
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
	//发送
	private class SendRequest extends AsyncTask<Object, Void, String> {
		private String url;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(CommentListActivity.this);
			Constant.showProgressDialog(progressDialog);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			String result = "";
			url = (String) params[0];
			Log.e("AddCommentUrl", url + "");
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
					// System.err.println(result);
					String resultCode = jo.optString("resultCode");
					if (resultCode.equals("1")) {
						PF = OKSend;
						initResult();
					} else {
						PF = FailSend;
						initResult();
					}
				} catch (JSONException e) {
					PF = FailSend;
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
}

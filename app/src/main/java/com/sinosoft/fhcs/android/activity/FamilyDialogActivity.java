package com.sinosoft.fhcs.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.FamilylistGridviewAdapter;
import com.sinosoft.fhcs.android.entity.FamilyMember;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.HttpManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FamilyDialogActivity extends BaseActivity implements OnItemClickListener,OnClickListener{
    private List<Object> fmList = new ArrayList<Object>();// 包括添加按钮的数据
    private List<Object> getList = new ArrayList<Object>();// 不包括添加按钮的数据
    private boolean flag;
    private GridView gridview;
    private FamilylistGridviewAdapter adapter;
    private FamilyDialogActivity mActivity;

    // 家庭列表网络请求
    private ProgressDialog progressDialog;// 进度条
    private static final int OK = 1001;// 成功
    private static final int FailServer = 1002;// 连接超时
    private static final int Fail = 1003;// 失败
    private static final int FailNoData = 1004;// 没有数据
    private static final int OKDelete = 1005;// 删除成功
    private static final int FailDelete = 1006;// 删除失败

    private int PF = 1000;
    private String userId = "";
    private Button btnBack;
    private TextView tvTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setUpViewAndData() {
        setContentView(R.layout.activity_family_dialog);
        mActivity = this;
        initView();
    }

    /**
     *
     */
    private void initView() {
        btnBack = (Button) findViewById(R.id.titlebar_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
        tvTitle.setText("请选择家庭成员的历史数据");
        gridview = (GridView)findViewById(R.id.familylist_gridview);
        gridview.setOnItemClickListener(this);
    }
    @Override
    public void onResume() {
        fmList.clear();
        getList.clear();
        flag = false;
        /*FamilyMember member = new FamilyMember(7, "", 0, "", 0, 0, 0, "",
                0, R.drawable.add, "", false,4000101);*/
        //fmList.add(member);
        adapter = new FamilylistGridviewAdapter(this, fmList, flag);
        gridview.setAdapter(adapter);

        // 从首选项获取userId
        SharedPreferences prefs = mActivity.getSharedPreferences(
                "UserInfo", Context.MODE_PRIVATE);
        userId = prefs.getString("userId", "");
        if (HttpManager.isNetworkAvailable(mActivity)) {
            GetFamilyListRequest re = new GetFamilyListRequest();
            re.execute(HttpManager.urlFamilyList(userId));
        } else {
            // Constant.showDialog(getActivity(), "您的网络没连接好，请检查后重试！");
            Toast.makeText(mActivity, "您的网络没连接好，请检查后重试！",
                    Toast.LENGTH_SHORT).show();
        }
        super.onResume();
        MobclickAgent.onPageStart("选择家庭成员的档案的家庭成员界面"); // 统计页面

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(mActivity,HealthRecordActivity.class);
        intent.putExtra("roleName", ((FamilyMember) getList.get(position)).getFamilyRoleName().trim());
        intent.putExtra("deviceId", ((FamilyMember) getList.get(position)).getDevice_name());
        intent.setFlags(Constant.department_erke);
        startActivityForResult(intent, Constant.Json_Request_Alltask);
        //finish();
    }


    // 网络请求
    private class GetFamilyListRequest extends AsyncTask<Object, Void, String> {
        private String url;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mActivity);
            Constant.showProgressDialog(progressDialog);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... params) {
            String result = "";
            url = (String) params[0];
            Log.e("getListUrl", url + "");
            result = HttpManager.getStringContent(url);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.toString().trim().equals("ERROR")) {
                PF = FailServer;
                initResult("");
            } else {
                try {
                    JSONObject jo = new JSONObject(result);
                    String resultCode = jo.optString("resultCode");
                    if (resultCode.equals("1")) {
                        JSONObject jo2 = jo.getJSONObject("data");
                        JSONArray ja = jo2.getJSONArray("FamilyMemberList");
                        if (ja.length() == 0) {
                            PF = FailNoData;
                            initResult("");
                        } else {
                            getList.clear();
                            getList = HttpManager.getFamilyList(ja);
                            PF = OK;
                            initResult("");
                        }
                    } else {
                        PF = Fail;
                        initResult("");
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
    // 请求结果
    private void initResult(String message) {
        if(mActivity ==null){
            System.out.println("HistoryFragment隐藏了");
        }else{
            if (PF == FailServer) {
                // Constant.showDialog(getActivity(), "服务器响应超时!");
                Toast.makeText(mActivity, "服务器响应超时!", Toast.LENGTH_SHORT)
                        .show();
            } else if (PF == Fail) {
                Toast.makeText(mActivity, "获取数据失败!", Toast.LENGTH_SHORT).show();
            } else if (PF == OK) {
                // Toast.makeText(this, "获取成功！", Toast.LENGTH_SHORT).show();
                initData();
            } else if (PF == FailNoData) {
                Toast.makeText(mActivity, "目前还没有家庭成员，请添加!", Toast.LENGTH_SHORT)
                        .show();
            } else if (PF == OKDelete) {
                Toast.makeText(mActivity, "删除成功！", Toast.LENGTH_SHORT).show();
               /* fmList.remove(position);
                adapter = new FamilylistGridviewAdapter(mActivity, fmList, flag);
                gridview.setAdapter(adapter);*/
            } else if (PF == FailDelete) {
                if (message.equals("")) {
                    Toast.makeText(mActivity, "删除失败！", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT)
                            .show();
                }

            }
        }
    }
    private void initData() {
        fmList.clear();
       /* FamilyMember member = new FamilyMember(7, "", 0, "", 0, 0, 0, "",
                0, R.drawable.add, "", false,4000101);*/
        fmList.addAll(getList);
        //fmList.add(member);
        adapter = new FamilylistGridviewAdapter(mActivity, fmList, flag);
        gridview.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constant.Json_Request_Alltask && resultCode == Constant.Json_Request_Onetask ){
            setResult(resultCode, data);
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.titlebar_btn_back:
                finish();
                break;

            default:
                break;
        }
    }

}

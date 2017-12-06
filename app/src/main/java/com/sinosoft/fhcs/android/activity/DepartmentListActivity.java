package com.sinosoft.fhcs.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.util.Constant;

/**
 *
 * @author xyd
 *
 */
public class DepartmentListActivity extends BaseActivity implements OnClickListener{

    private LinearLayout llErke,llFuke,llZhongyi,llPifu,llXinli,llXiaohua,llPuwai;
    private TextView tvTitle;
    private Button btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setUpViewAndData() {
        setContentView(R.layout.activity_department_list);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        // TODO Auto-generated method stub
        btnBack = (Button) findViewById(R.id.titlebar_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
        tvTitle.setText("科室问诊");
        llErke = (LinearLayout)findViewById(R.id.frag_measure_layout_erke);
        llFuke = (LinearLayout)findViewById(R.id.frag_measure_layout_fuke);
        llZhongyi = (LinearLayout)findViewById(R.id.frag_measure_layout_zhongyi);
        llPifu = (LinearLayout)findViewById(R.id.frag_measure_layout_pifu);
        llXinli = (LinearLayout)findViewById(R.id.frag_measure_layout_xinli);
        llXiaohua = (LinearLayout)findViewById(R.id.frag_measure_layout_xiaohua);
        llPuwai = (LinearLayout)findViewById(R.id.frag_measure_layout_puwai);

        llErke.setOnClickListener(this);
        llFuke.setOnClickListener(this);
        llZhongyi.setOnClickListener(this);
        llPifu.setOnClickListener(this);
        llXinli.setOnClickListener(this);
        llXiaohua.setOnClickListener(this);
        llPuwai.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this,DepartmentDetailsActivity.class);
        switch (v.getId()) {
            case R.id.titlebar_btn_back:
                // 返回
                finish();
                break;
            case R.id.frag_measure_layout_erke:
                intent.putExtra("departmentName", getString(R.string.title_activity_department_erke));
                intent.setFlags(Constant.department_erke);
                startActivity(intent);
                break;
            case R.id.frag_measure_layout_fuke:
                intent.putExtra("departmentName", getString(R.string.title_activity_department_fuke));
                intent.setFlags(Constant.department_fuke);
                startActivity(intent);
                break;
            case R.id.frag_measure_layout_zhongyi:
                intent.putExtra("departmentName", getString(R.string.title_activity_department_zhongyi));
                intent.setFlags(Constant.department_zhongyike);
                startActivity(intent);
                break;
            case R.id.frag_measure_layout_pifu:
                intent.putExtra("departmentName", getString(R.string.title_activity_department_pifu));
                intent.setFlags(Constant.department_pifuke);
                startActivity(intent);
                break;
            case R.id.frag_measure_layout_xinli:
                intent.putExtra("departmentName", getString(R.string.title_activity_department_xinli));
                intent.setFlags(Constant.department_xinlike);
                startActivity(intent);
                break;
            case R.id.frag_measure_layout_xiaohua:
                intent.putExtra("departmentName", getString(R.string.title_activity_department_xiahua));
                intent.setFlags(Constant.department_xiaohuake);
                startActivity(intent);
                break;
            case R.id.frag_measure_layout_puwai:
                intent.putExtra("departmentName", getString(R.string.title_activity_department_puwai));
                intent.setFlags(Constant.department_puwaike);
                startActivity(intent);
                break;

            default:
                break;
        }

    }


}

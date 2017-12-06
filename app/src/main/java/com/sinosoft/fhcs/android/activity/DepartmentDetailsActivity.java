package com.sinosoft.fhcs.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.DoctorAdapter;
import com.sinosoft.fhcs.android.entity.Department;
import com.sinosoft.fhcs.android.entity.DoctorBean;
import com.sinosoft.fhcs.android.util.TestData;

import java.util.List;

public class DepartmentDetailsActivity extends BaseActivity implements OnClickListener{

    private TextView tvTitle;
    private String departmentName;
    private ListView lvDoctor;
    private DoctorAdapter doctorAdapter;
    private Button btnBack;
    private DepartmentDetailsActivity mInstance;
    private int departmentFlage;
    private TextView tvDepartmentDetails;
    private TextView tvDepartmentName;
    private ImageView ivDepartment;
    private List<DoctorBean> doctors;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setUpViewAndData() {
        setContentView(R.layout.activity_department_details);
        mInstance = this;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        departmentFlage = getIntent().getFlags();
        Department departmentData = TestData.departmentData(departmentFlage);
        doctors = TestData.doctorData(departmentFlage);
        if(departmentData != null){
            initView(departmentData);
        }
    }

    private void initView(Department departmentData) {
        // TODO Auto-generated method stub
        btnBack = (Button) findViewById(R.id.titlebar_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        departmentName = getIntent().getExtras().getString("departmentName");
        if(departmentName != null ) {
            tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
            tvTitle.setText(departmentName);
        }else {
            return;
        }
        lvDoctor = (ListView)findViewById(R.id.lv_department_doctor_list);
        tvDepartmentDetails = (TextView)findViewById(R.id.tv_d_details_details);
        tvDepartmentName = (TextView)findViewById(R.id.tv_d_details_name);
        ivDepartment = (ImageView)findViewById(R.id.iv_d_details);
        tvDepartmentName.setText(departmentData.getName());
        tvDepartmentDetails.setText(departmentData.getAbstracts());
        ivDepartment.setBackgroundResource(departmentData.getDrawable());

        doctorAdapter = new DoctorAdapter(this.getApplicationContext(),doctors);
        lvDoctor.setAdapter(doctorAdapter);
        lvDoctor.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mInstance , DoctorDetailsActivity.class);
                DoctorBean item = doctorAdapter.getItem(position);
                intent.putExtra("doctor", item);
                intent.setFlags(departmentFlage);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.titlebar_btn_back:
                // 返回
                finish();
                break;
        }
    }

}

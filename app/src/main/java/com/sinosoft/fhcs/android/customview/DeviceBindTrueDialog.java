package com.sinosoft.fhcs.android.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sinosoft.fhcs.android.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 绑定成功的dialog
 * Created by shule on 2016/3/31.
 */
public class DeviceBindTrueDialog extends Dialog {

    private Context context;
    private Button button_complete;
    private ArrayList<HashMap<String, String>> arraylist;
    private View.OnClickListener onClickListener;
    private LinearLayout llSearch;

    public DeviceBindTrueDialog(Context context) {
        super(context);
        this.context = context;

    }

    public DeviceBindTrueDialog(Context context, View.OnClickListener onClickListener){
        super(context, R.style.mycenter_ms_dialog);
        this.context = context;
        this.onClickListener = onClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.link_device_dialog_layout);
        initView();
    }

    private void initView(){
        button_complete = (Button)findViewById(R.id.button_complete);
        button_complete.setOnClickListener(onClickListener);
        llSearch = (LinearLayout) findViewById(R.id.ll_searching);
    }


    public void setReslutData(ArrayList<HashMap<String, String>> arraylist){
        llSearch.setVisibility(View.GONE);
        this.arraylist = arraylist;
    }
}
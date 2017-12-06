package com.sinosoft.fhcs.android.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sinosoft.fhcs.android.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.sinosoft.fhcs.android.R.id.button_agin;


/**
 * 搜索弹出框
 * Created by shule on 2016/3/31.
 */
public class DeviceSearchDialog extends Dialog {

    private Context context;
    private Button button_cancel;
    private ListView serchList;
    private ArrayList<HashMap<String, String>> arraylist;
    private View.OnClickListener onClickListener;
    private AdapterView.OnItemClickListener mItemOnClickListener;
    private ImageView ivSearch;
    private AnimationDrawable ivSearchDrawable;
    private LinearLayout llSearch;
    private Button buttonAgin;//重新搜索

    public DeviceSearchDialog(Context context) {
        super(context);
        this.context = context;

    }

    public DeviceSearchDialog(Context context, View.OnClickListener onClickListener, AdapterView.OnItemClickListener onItemClickListener){
        super(context, R.style.mycenter_ms_dialog);
        this.context = context;
        this.onClickListener = onClickListener;
        this.mItemOnClickListener = onItemClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.search_device_dialog_layout);
        initView();
    }

    private void initView(){
        button_cancel = (Button)findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(onClickListener);
        buttonAgin = (Button)findViewById(button_agin);//重新搜索
        buttonAgin.setOnClickListener(onClickListener);
        serchList = (ListView) findViewById(R.id.search_list);
        serchList.setOnItemClickListener(mItemOnClickListener);
        ivSearch = (ImageView) findViewById(R.id.iv_search_device);
        llSearch = (LinearLayout) findViewById(R.id.ll_searching);
        ivSearch.setImageResource(R.drawable.iv_search_device_anim);
        ivSearchDrawable = (AnimationDrawable) ivSearch.getDrawable();
    }

    @Override
    public void show() {
        super.show();
        ivSearchDrawable.start();
    }

    public void setReslutData(ArrayList<HashMap<String, String>> arraylist){
        if(ivSearchDrawable != null){
            ivSearchDrawable.stop();
        }
        buttonAgin.setVisibility(View.VISIBLE);
        llSearch.setVisibility(View.GONE);
        serchList.setVisibility(View.VISIBLE);
        this.arraylist = arraylist;
        SimpleAdapter adapter = new SimpleAdapter(context, arraylist, R.layout.device_item,new String[]{"name"}, new int[]{R.id.device_name});
        if(serchList != null){
            serchList.setAdapter(adapter);
        }
    }
}
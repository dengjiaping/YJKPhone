package com.sinosoft.fhcs.android.activity;

/**
 * @CopyRight: SinoSoft.
 * @Description:城市选择页
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.ChoiceCityAdapter;
import com.sinosoft.fhcs.android.database.DB_City;
import com.sinosoft.fhcs.android.entity.CityListItem;
import com.sinosoft.fhcs.android.util.Constant;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class ChoiceCityActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private TextView tvTitle;
	private Button btnBack;
	private GridView gridview;
	private ChoiceCityAdapter adapter;
	private RadioGroup rGroup;
	private RadioButton rbProvince, rbCity, rbDistrict;// 省市区
	private DB_City dbm;
	private SQLiteDatabase db;
	private String strProvince = "";
	private String strCity = "";
	private String strDistrict = "";
	private String pCode = "";
	private String cCode = "";
	private String dCode = "";
	private String flag = "register";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void setUpViewAndData() {
		setContentView(R.layout.activity_choicecity);
		ExitApplicaton.getInstance().addActivity(this);
		flag = this.getIntent().getExtras().getString("flag");
		if (flag.equals("register")) {
			strProvince = this.getIntent().getStringExtra("province");
			strCity = this.getIntent().getStringExtra("city");
			strDistrict = this.getIntent().getStringExtra("district");
			pCode = this.getIntent().getStringExtra("provinceId");
			cCode = this.getIntent().getStringExtra("cityId");
			dCode = this.getIntent().getStringExtra("districtId");
		}
		init();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("城市选择页"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("城市选择页"); // 保证 onPageEnd 在onPause 之前调用,因为
		// onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	private void init() {
		// titlebar
		tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		tvTitle.setText(getResources().getString(R.string.title_city));
		btnBack = (Button) findViewById(R.id.titlebar_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);
		// 城市选择
		rGroup = (RadioGroup) findViewById(R.id.choice_rq);
		rGroup.setOnCheckedChangeListener(this);
		rbProvince = (RadioButton) findViewById(R.id.choice_rb_province);
		rbCity = (RadioButton) findViewById(R.id.choice_rb_city);
		rbDistrict = (RadioButton) findViewById(R.id.choice_rb_district);
		gridview = (GridView) findViewById(R.id.gridview);
		rbProvince.setEnabled(true);
		rbCity.setEnabled(false);
		rbDistrict.setEnabled(false);
		initProvince();
	}

	private void initProvince() {
		dbm = new DB_City(this);
		dbm.openDatabase();
		db = dbm.getDatabase();
		List<CityListItem> list = new ArrayList<CityListItem>();

		try {
			String sql = "select * from area where pcode='0' order by code ";
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				byte bytes[] = cursor.getBlob(1);
				String name = new String(bytes, "utf-8");
				CityListItem myListItem = new CityListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			byte bytes[] = cursor.getBlob(1);
			String name = new String(bytes, "utf-8");
			CityListItem myListItem = new CityListItem();
			myListItem.setName(name);
			myListItem.setPcode(code);
			list.add(myListItem);

		} catch (Exception e) {
			e.printStackTrace();
		}
		dbm.closeDatabase();
		db.close();
		if (list.size() != 0) {
			if(pCode.equals("")){
				pCode = list.get(0).getPcode();
			}

		} else {
			Toast.makeText(this, "没有可以选择的省", Toast.LENGTH_SHORT).show();
		}
		adapter = new ChoiceCityAdapter(this, list);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
									int position, long id) {
				strProvince = ((CityListItem) adapterView
						.getItemAtPosition(position)).getName();
				pCode = ((CityListItem) adapterView.getItemAtPosition(position))
						.getPcode();
				rbProvince.setText(strProvince);
				rbCity.setText(getResources().getString(
						R.string.reg_tv_address2));
				rbDistrict.setText(getResources().getString(
						R.string.reg_tv_address3));
				rbProvince.setEnabled(true);
				rbCity.setEnabled(true);
				rbDistrict.setEnabled(false);
				strCity="";
				cCode="";
				strDistrict="";
				dCode="";
				// Toast.makeText(ChoiceCityActivity.this, strProvince,
				// Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void initSpinCity(String pcode) {
		dbm = new DB_City(this);
		dbm.openDatabase();
		db = dbm.getDatabase();
		List<CityListItem> list = new ArrayList<CityListItem>();

		try {
			String sql = "select * from area where pcode='" + pcode + "' order by code";
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				byte bytes[] = cursor.getBlob(1);
				String name = new String(bytes, "utf-8");
				CityListItem myListItem = new CityListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			byte bytes[] = cursor.getBlob(1);
			String name = new String(bytes, "utf-8");
			CityListItem myListItem = new CityListItem();
			myListItem.setName(name);
			myListItem.setPcode(code);
			list.add(myListItem);

		} catch (Exception e) {
			e.printStackTrace();
		}
		dbm.closeDatabase();
		db.close();
		if (list.size() != 0) {
			if(cCode.equals("")){
				cCode = list.get(0).getPcode();
			}

		} else {
			Toast.makeText(this, "没有可以选择的市", Toast.LENGTH_SHORT).show();
		}
		adapter = new ChoiceCityAdapter(this, list);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
									int position, long id) {
				strCity = ((CityListItem) adapterView
						.getItemAtPosition(position)).getName();
				cCode = ((CityListItem) adapterView.getItemAtPosition(position))
						.getPcode();
				rbCity.setText(strCity);
				rbDistrict.setText(getResources().getString(
						R.string.reg_tv_address3));
				rbProvince.setEnabled(true);
				rbCity.setEnabled(true);
				rbDistrict.setEnabled(true);
				strDistrict="";
				dCode="";
				// Toast.makeText(ChoiceCityActivity.this, strCity,
				// Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void initSpinDistrict(String pcode) {
		dbm = new DB_City(this);
		dbm.openDatabase();
		db = dbm.getDatabase();
		List<CityListItem> list = new ArrayList<CityListItem>();

		try {
			String sql = "select * from area where pcode='" + pcode + "' order by code";
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				byte bytes[] = cursor.getBlob(1);
				String name = new String(bytes, "utf-8");
				CityListItem myListItem = new CityListItem();
				myListItem.setName(name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			byte bytes[] = cursor.getBlob(1);
			String name = new String(bytes, "utf-8");
			CityListItem myListItem = new CityListItem();
			myListItem.setName(name);
			myListItem.setPcode(code);
			list.add(myListItem);

		} catch (Exception e) {
			e.printStackTrace();
		}
		dbm.closeDatabase();
		db.close();
		if (list.size() == 0) {
			Toast.makeText(this, "没有可以选择的区", Toast.LENGTH_SHORT).show();
		}
		adapter = new ChoiceCityAdapter(this, list);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
									int position, long id) {
				strDistrict = ((CityListItem) adapterView
						.getItemAtPosition(position)).getName();
				dCode = ((CityListItem) adapterView.getItemAtPosition(position))
						.getPcode();
				rbDistrict.setText(strDistrict);
				Log.e("地区", strProvince + " " + strCity + " " + strDistrict);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (flag.equals("register")) {
			Intent in = getIntent();
			in.putExtra("province", strProvince.trim());
			in.putExtra("city", strCity.trim());
			in.putExtra("district", strDistrict.trim());
			in.putExtra("provinceId", pCode);
			in.putExtra("cityId", cCode);
			in.putExtra("districtId", dCode);
			setResult(RESULT_OK, in);
			finish();
		} else {
			Constant.province = strProvince.trim();
			Constant.city = strCity.trim();
			Constant.district = strDistrict.trim();
			Constant.provinceId = pCode;
			Constant.cityId = cCode;
			Constant.districtId = dCode;
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titlebar_btn_back:
				// 返回
				exit();
				break;
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.choice_rb_province:
				initProvince();
				break;

			case R.id.choice_rb_city:
				initSpinCity(pCode);
				// initSpinDistrict(pCode);
				break;
			case R.id.choice_rb_district:
				initSpinDistrict(cCode);
				break;
		}
	}
}

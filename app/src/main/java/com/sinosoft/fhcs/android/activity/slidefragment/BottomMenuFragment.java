package com.sinosoft.fhcs.android.activity.slidefragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.MainActivity;
import com.sinosoft.fhcs.android.activity.bottomfragment.DeviceFragment;
import com.sinosoft.fhcs.android.activity.bottomfragment.InformationFragment;
import com.sinosoft.fhcs.android.activity.bottomfragment.MeasurementFragment;
import com.sinosoft.fhcs.android.activity.bottomfragment.SetFragment;
import com.sinosoft.fhcs.android.activity.bottomfragment.ShopFragment;
import com.slidingmenu.lib.SlidingMenu;
/**
 * menu fragment ，主要是用于显示menu菜单
 *
 * @author <a href="mailto:kris@krislq.com">Kris.lee</a>
 * @since Mar 12, 2013
 * @version 1.0.0
 */
public class BottomMenuFragment extends Fragment implements
		OnCheckedChangeListener {
	private RadioGroup mRadGrp;
	private int mIndex = -1;
	private static final int INDEX_MEASUREMENT = 0x01;
	private static final int INDEX_HISTORY = 0x02;
	private static final int INDEX_INFORMATION = 0x03;
	private static final int INDEX_SET = 0x04;
	private static final int INDEX_DEVICE = 0x05;
	private InformationFragment mInformationFg;
//	private HistoryFragment mHistoryFg;
	private ShopFragment shopFragment;
	private MeasurementFragment mMeasureFg;
	private SetFragment mSetFg;
	private DeviceFragment mDevFg;
	private Fragment mFragment;
	private MainActivity mActivity = null;
	private FragmentManager mFragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mActivity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frame_viewpager, null);
		mRadGrp = (RadioGroup) view.findViewById(R.id.tab_rg_menu);
		mRadGrp.setOnCheckedChangeListener(this);
		initTabs();
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initTabs() {
		mFragmentManager = ((MainActivity)getActivity()).getSupportFragmentManager();
		mRadGrp.check(R.id.tab_rb_measurement);
		switchFragment(INDEX_MEASUREMENT);
		((MainActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	private void switchFragment(int index) {
		if(index == mIndex)
			return;
		switch (index) {
			case INDEX_MEASUREMENT:
				if(null == mMeasureFg)
					mMeasureFg = new MeasurementFragment(mActivity);
				mFragment = mMeasureFg;
				break;
			case INDEX_HISTORY:
				if(null == shopFragment)
					shopFragment = new ShopFragment();
				mFragment = shopFragment;
				break;
			case INDEX_INFORMATION:
				if(null == mInformationFg)
					mInformationFg = new InformationFragment();
				mFragment = mInformationFg;
				break;
			case INDEX_SET:
				if(null == mSetFg)
					mSetFg = new SetFragment();
				mFragment = mSetFg;
				break;
			case INDEX_DEVICE://设备
//			if(null == mDevFg)
				mDevFg = new DeviceFragment();
				mFragment = mDevFg;
				break;
			default:
				break;
		}
		mFragmentManager.beginTransaction().replace(R.id.bottom_view, mFragment).commit();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkId) {
		// TODO Auto-generated method stub
		switch (checkId) {
			case R.id.tab_rb_measurement:
				switchFragment(INDEX_MEASUREMENT);
				break;
			case R.id.tab_rb_history:
				switchFragment(INDEX_HISTORY);
				break;
			case R.id.tab_rb_info:
				switchFragment(INDEX_INFORMATION);
				break;
			case R.id.tab_rb_set:
				switchFragment(INDEX_SET);
				break;
			case R.id.tab_rb_device:
				switchFragment(INDEX_DEVICE);
				break;
			default:
				break;
		}
	}

}

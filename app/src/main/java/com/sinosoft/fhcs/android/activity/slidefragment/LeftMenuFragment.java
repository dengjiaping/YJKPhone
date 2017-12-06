package com.sinosoft.fhcs.android.activity.slidefragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.MainActivity;

/**
 * menu fragment ，主要是用于显示menu菜单
 *
 * @author <a href="mailto:kris@krislq.com">Kris.lee</a>
 * @since Mar 12, 2013
 * @version 1.0.0
 */
public class LeftMenuFragment extends Fragment implements
		OnCheckedChangeListener, OnClickListener {
	private MainActivity mActivity = null;
	private static final int LEFT_MAIN = 0x01;
	private static final int LEFT_SPORT = 0x02;
	private static final int LEFT_COMPETITION = 0x03;
	private static final int LEFT_FRIENDSPK = 0x04;
	private static final int LEFT_SETGOAL = 0x05;
	private RadioButton mRadMain;
	private RadioButton mRadSport;
	private RadioButton mRadCompetition;
	private RadioButton mRadFriendsPk;
	private RadioButton mRadSetGoal;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.left_sliding_layout, null);
		mRadMain = (RadioButton) rootView.findViewById(R.id.radio_tab_main);
		mRadSport = (RadioButton) rootView.findViewById(R.id.radio_tab_sport);
		mRadCompetition = (RadioButton) rootView.findViewById(R.id.radio_tab_competition);
		mRadFriendsPk = (RadioButton) rootView.findViewById(R.id.radio_tab_friendspk);
		mRadSetGoal = (RadioButton) rootView.findViewById(R.id.radio_tab_setgoal);
		mRadMain.setOnCheckedChangeListener(this);
		mRadSport.setOnCheckedChangeListener(this);
		mRadCompetition.setOnCheckedChangeListener(this);
		mRadFriendsPk.setOnCheckedChangeListener(this);
		mRadSetGoal.setOnCheckedChangeListener(this);
		mRadMain.setOnClickListener(this);
		mRadSport.setOnClickListener(this);
		mRadCompetition.setOnClickListener(this);
		mRadFriendsPk.setOnClickListener(this);
		mRadSetGoal.setOnClickListener(this);
		mRadMain.setChecked(true);
		return rootView;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void onCheckChange(int check) {
		android.support.v4.app.FragmentManager fragmentManager = ((MainActivity) getActivity())
				.getSupportFragmentManager();
		Fragment contentFragment;
		switch (check) {
			case LEFT_MAIN:
				contentFragment = (BottomMenuFragment) fragmentManager
						.findFragmentByTag("main");
				fragmentManager
						.beginTransaction()
						.replace(
								R.id.content,
								contentFragment == null ? new BottomMenuFragment()
										: contentFragment, "main").commit();
				break;

			case LEFT_SPORT:
				contentFragment = (SportFragment) fragmentManager
						.findFragmentByTag("sport");
				fragmentManager
						.beginTransaction()
						.replace(
								R.id.content,
								contentFragment == null ? new SportFragment(mActivity)
										: contentFragment, "sport").commit();
				break;

			case LEFT_COMPETITION:
				contentFragment = (CompetitionFragment) fragmentManager
						.findFragmentByTag("competition");
				fragmentManager
						.beginTransaction()
						.replace(
								R.id.content,
								contentFragment == null ? new CompetitionFragment(mActivity)
										: contentFragment, "competition").commit();
				break;
			case LEFT_FRIENDSPK:
				contentFragment = (FriendsPkFragment) fragmentManager
						.findFragmentByTag("friendspk");
				fragmentManager
						.beginTransaction()
						.replace(
								R.id.content,
								contentFragment == null ? new FriendsPkFragment(mActivity)
										: contentFragment, "friendspk").commit();
				break;
			case LEFT_SETGOAL:
				contentFragment = (SetGoalFragment) fragmentManager
						.findFragmentByTag("setgoal");
				fragmentManager
						.beginTransaction()
						.replace(
								R.id.content,
								contentFragment == null ? new SetGoalFragment(mActivity)
										: contentFragment, "setgoal").commit();
				break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton btn, boolean check) {
		// TODO Auto-generated method stub
		switch (btn.getId()) {
			case R.id.radio_tab_main:
				if (check)
					onCheckChange(LEFT_MAIN);
				break;

			case R.id.radio_tab_sport:
				if (check)
					onCheckChange(LEFT_SPORT);
				break;

			case R.id.radio_tab_competition:
				if (check)
					onCheckChange(LEFT_COMPETITION);
				break;
			case R.id.radio_tab_friendspk:
				if (check)
					onCheckChange(LEFT_FRIENDSPK);
				break;
			case R.id.radio_tab_setgoal:
				if (check)
					onCheckChange(LEFT_SETGOAL);
				break;
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		((MainActivity) getActivity()).getSlidingMenu().toggle();
	}
}

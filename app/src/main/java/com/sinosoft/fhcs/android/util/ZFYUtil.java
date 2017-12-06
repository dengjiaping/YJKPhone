package com.sinosoft.fhcs.android.util;

import java.util.HashMap;
import java.util.Map;

public class ZFYUtil {
	/**
	 * 男性正常临界值,key为年龄 eg: 13.6 < BMI < 17.4
	 */
	public static final Map<Integer, String> MALE_NORMAL_STANDARD = new HashMap<Integer, String>();
	static {
		MALE_NORMAL_STANDARD.put(5, "6#25");
		MALE_NORMAL_STANDARD.put(6, "6#25");
		MALE_NORMAL_STANDARD.put(7, "6#25");
		MALE_NORMAL_STANDARD.put(8, "6#26");
		MALE_NORMAL_STANDARD.put(9, "6#26");
		MALE_NORMAL_STANDARD.put(10, "6#26");
		MALE_NORMAL_STANDARD.put(11, "6#26");
		MALE_NORMAL_STANDARD.put(12, "6#25");
		MALE_NORMAL_STANDARD.put(13, "6#25");
		MALE_NORMAL_STANDARD.put(14, "6#25");
		MALE_NORMAL_STANDARD.put(15, "7#24");
		MALE_NORMAL_STANDARD.put(16, "7#24");
		MALE_NORMAL_STANDARD.put(17, "8#23");
		MALE_NORMAL_STANDARD.put(18, "10#17");
		MALE_NORMAL_STANDARD.put(40, "11#18");
		MALE_NORMAL_STANDARD.put(60, "13#20");
	}
	/**
	 * 女性正常临界值,key为年龄 eg: 13.2 < BMI < 17.2
	 */
	public static final Map<Integer, String> FEMALE_NORMAL_STANDARD = new HashMap<Integer, String>();
	static {
		FEMALE_NORMAL_STANDARD.put(5, "7#25");
		FEMALE_NORMAL_STANDARD.put(6, "7#25");
		FEMALE_NORMAL_STANDARD.put(7, "8#25");
		FEMALE_NORMAL_STANDARD.put(8, "9#26");
		FEMALE_NORMAL_STANDARD.put(9, "9#28");
		FEMALE_NORMAL_STANDARD.put(10, "10#29");
		FEMALE_NORMAL_STANDARD.put(11, "12#31");
		FEMALE_NORMAL_STANDARD.put(12, "13#32");
		FEMALE_NORMAL_STANDARD.put(13, "14#34");
		FEMALE_NORMAL_STANDARD.put(14, "16#35");
		FEMALE_NORMAL_STANDARD.put(15, "17#36");
		FEMALE_NORMAL_STANDARD.put(16, "18#37");
		FEMALE_NORMAL_STANDARD.put(17, "19#37");
		FEMALE_NORMAL_STANDARD.put(18, "20#28");
		FEMALE_NORMAL_STANDARD.put(40, "21#29");
		FEMALE_NORMAL_STANDARD.put(60, "22#30");
	}
	public static String fetchBMIState(int age, int gender,double fatPercentage) {

		String state = null;
		String[] temp = null;
		if (age <= 5) {
			age = 5;
		}
		if(18<=age&&age<=39){
			age=18;
		}
		if(40<=age&&age<=59){
			age=40;
		}
		if (age >= 60) {
			age = 60;
		}
		double min=0;
		double max=0;
		switch (gender) {
			case 0:// female
				temp = FEMALE_NORMAL_STANDARD.get(age).split("#");
				min=Double.parseDouble(temp[0]);
				max=Double.parseDouble(temp[1]);
				if(fatPercentage<=min){
					state = "消瘦";
				}else if(fatPercentage>=max){
					state = "肥胖";
				}else{
					state = "正常";
				}
				break;

			case 1: // make
				temp = MALE_NORMAL_STANDARD.get(age).split("#");
				min=Double.parseDouble(temp[0]);
				max=Double.parseDouble(temp[1]);
				if(fatPercentage<=min){
					state = "消瘦";
				}else if(fatPercentage>=max){
					state = "肥胖";
				}else{
					state = "正常";
				}
				break;
			default:
				state = "未知";
		}
		System.out.println("age="+age+"\ngender="+gender+"\nmin="+min+"\nmax="+max+"\nfatPercentage="+fatPercentage);
		return state;
	}
}

package com.sinosoft.fhcs.android.util;


import java.util.HashMap;
import java.util.Map;

public class BMIUtil {

	/**
	 * 男性肥胖临界值,key为年龄 eg: BMI >= 19.2
	 */
	private static final Map<Integer, Double> MALE_FAT_STANDARD = new HashMap<Integer, Double>();
	static {
		MALE_FAT_STANDARD.put(7, 19.2);
		MALE_FAT_STANDARD.put(8, 20.3);
		MALE_FAT_STANDARD.put(9, 21.4);
		MALE_FAT_STANDARD.put(10, 22.5);
		MALE_FAT_STANDARD.put(11, 23.6);
		MALE_FAT_STANDARD.put(12, 24.7);
		MALE_FAT_STANDARD.put(13, 25.7);
		MALE_FAT_STANDARD.put(14, 26.4);
		MALE_FAT_STANDARD.put(15, 26.9);
		MALE_FAT_STANDARD.put(16, 27.4);
		MALE_FAT_STANDARD.put(17, 27.8);
		MALE_FAT_STANDARD.put(18, 28.0);
	}

	/**
	 * 女性肥胖临界值,key为年龄 eg: BMI >= 18.9
	 */
	private static final Map<Integer, Double> FEMALE_FAT_STANDARD = new HashMap<Integer, Double>();//
	static {
		FEMALE_FAT_STANDARD.put(7, 18.9);
		FEMALE_FAT_STANDARD.put(8, 19.9);
		FEMALE_FAT_STANDARD.put(9, 21.0);
		FEMALE_FAT_STANDARD.put(10, 22.1);
		FEMALE_FAT_STANDARD.put(11, 23.3);
		FEMALE_FAT_STANDARD.put(12, 24.5);
		FEMALE_FAT_STANDARD.put(13, 25.6);
		FEMALE_FAT_STANDARD.put(14, 26.3);
		FEMALE_FAT_STANDARD.put(15, 26.9);
		FEMALE_FAT_STANDARD.put(16, 27.4);
		FEMALE_FAT_STANDARD.put(17, 27.7);
		FEMALE_FAT_STANDARD.put(18, 28.0);
	}
	/**
	 * 男性超重临界值,key为年龄 eg: 17.4 <= BMI <= 19.2
	 */
	private static final Map<Integer, String> MALE_OVER_WEIGHT_STANDARD = new HashMap<Integer, String>();
	static {
		MALE_OVER_WEIGHT_STANDARD.put(7, "17.4#19.2");
		MALE_OVER_WEIGHT_STANDARD.put(8, "18.1#20.3");
		MALE_OVER_WEIGHT_STANDARD.put(9, "18.9#21.4");
		MALE_OVER_WEIGHT_STANDARD.put(10, "19.6#22.5");
		MALE_OVER_WEIGHT_STANDARD.put(11, "20.3#23.6");
		MALE_OVER_WEIGHT_STANDARD.put(12, "21#24.7");
		MALE_OVER_WEIGHT_STANDARD.put(13, "21.9#25.7");
		MALE_OVER_WEIGHT_STANDARD.put(14, "22.6#26.4");
		MALE_OVER_WEIGHT_STANDARD.put(15, "23.1#26.9");
		MALE_OVER_WEIGHT_STANDARD.put(16, "23.5#27.4");
		MALE_OVER_WEIGHT_STANDARD.put(17, "23.8#27.8");
		MALE_OVER_WEIGHT_STANDARD.put(18, "24.0#28.0");
	}
	/**
	 * 女性超重临界值,key为年龄 eg: 17.2 <= BMI <= 18.9
	 */
	private static final Map<Integer, String> FEMALE_OVER_WEIGHT_STANDARD = new HashMap<Integer, String>();
	static {
		FEMALE_OVER_WEIGHT_STANDARD.put(7, "17.2#18.9");
		FEMALE_OVER_WEIGHT_STANDARD.put(8, "18.1#19.9");
		FEMALE_OVER_WEIGHT_STANDARD.put(9, "19.0#21.0");
		FEMALE_OVER_WEIGHT_STANDARD.put(10, "20.0#22.1");
		FEMALE_OVER_WEIGHT_STANDARD.put(11, "21.1#23.3");
		FEMALE_OVER_WEIGHT_STANDARD.put(12, "21.9#24.5");
		FEMALE_OVER_WEIGHT_STANDARD.put(13, "22.6#25.6");
		FEMALE_OVER_WEIGHT_STANDARD.put(14, "23.0#26.3");
		FEMALE_OVER_WEIGHT_STANDARD.put(15, "23.4#26.9");
		FEMALE_OVER_WEIGHT_STANDARD.put(16, "23.7#27.4");
		FEMALE_OVER_WEIGHT_STANDARD.put(17, "23.8#27.7");
		FEMALE_OVER_WEIGHT_STANDARD.put(18, "24.0#28.0");
	}
	/**
	 * 男性正常临界值,key为年龄 eg: 13.6 < BMI < 17.4
	 */
	public static final Map<Integer, String> MALE_NORMAL_STANDARD = new HashMap<Integer, String>();
	static {
		MALE_NORMAL_STANDARD.put(7, "13.6#17.4");
		MALE_NORMAL_STANDARD.put(8, "13.8#18.1");
		MALE_NORMAL_STANDARD.put(9, "14#18.9");
		MALE_NORMAL_STANDARD.put(10, "14.3#19.6");
		MALE_NORMAL_STANDARD.put(11, "14.7#20.3");
		MALE_NORMAL_STANDARD.put(12, "15.1#21");
		MALE_NORMAL_STANDARD.put(13, "15.7#21.9");
		MALE_NORMAL_STANDARD.put(14, "16.3#22.6");
		MALE_NORMAL_STANDARD.put(15, "16.8#23.1");
		MALE_NORMAL_STANDARD.put(16, "17.3#23.5");
		MALE_NORMAL_STANDARD.put(17, "17.7#23.8");
		MALE_NORMAL_STANDARD.put(18, "18.1#24.0");
	}
	/**
	 * 女性正常临界值,key为年龄 eg: 13.2 < BMI < 17.2
	 */
	public static final Map<Integer, String> FEMALE_NORMAL_STANDARD = new HashMap<Integer, String>();
	static {
		FEMALE_NORMAL_STANDARD.put(7, "13.2#17.2");
		FEMALE_NORMAL_STANDARD.put(8, "13.4#18.1");
		FEMALE_NORMAL_STANDARD.put(9, "13.7#19.0");
		FEMALE_NORMAL_STANDARD.put(10, "14.1#20.0");
		FEMALE_NORMAL_STANDARD.put(11, "14.6#21.1");
		FEMALE_NORMAL_STANDARD.put(12, "15.2#21.9");
		FEMALE_NORMAL_STANDARD.put(13, "15.8#22.6");
		FEMALE_NORMAL_STANDARD.put(14, "16.3#23.0");
		FEMALE_NORMAL_STANDARD.put(15, "16.7#23.4");
		FEMALE_NORMAL_STANDARD.put(16, "16.9#23.7");
		FEMALE_NORMAL_STANDARD.put(17, "17.1#23.8");
		FEMALE_NORMAL_STANDARD.put(18, "17.2#24.0");
	}
	/**
	 * 男性消瘦临界值,key为年龄 eg: BMI <= 13.6
	 */
	private static final Map<Integer, Double> MALE_WEAK_STANDARD = new HashMap<Integer, Double>();
	static {
		MALE_WEAK_STANDARD.put(7, 13.6);
		MALE_WEAK_STANDARD.put(8, 13.8);
		MALE_WEAK_STANDARD.put(9, 14.0);
		MALE_WEAK_STANDARD.put(10, 14.3);
		MALE_WEAK_STANDARD.put(11, 14.7);
		MALE_WEAK_STANDARD.put(12, 15.1);
		MALE_WEAK_STANDARD.put(13, 15.7);
		MALE_WEAK_STANDARD.put(14, 16.3);
		MALE_WEAK_STANDARD.put(15, 16.8);
		MALE_WEAK_STANDARD.put(16, 17.3);
		MALE_WEAK_STANDARD.put(17, 17.7);
		MALE_WEAK_STANDARD.put(18, 18.1);
	}
	/**
	 * 女性消瘦临界值,key为年龄 eg: BMI <= 13.2
	 */
	private static final Map<Integer, Double> FEMALE_WEAK_STANDARD = new HashMap<Integer, Double>();
	static {
		FEMALE_WEAK_STANDARD.put(7, 13.2);
		FEMALE_WEAK_STANDARD.put(8, 13.4);
		FEMALE_WEAK_STANDARD.put(9, 13.7);
		FEMALE_WEAK_STANDARD.put(10, 14.1);
		FEMALE_WEAK_STANDARD.put(11, 14.6);
		FEMALE_WEAK_STANDARD.put(12, 15.2);
		FEMALE_WEAK_STANDARD.put(13, 15.8);
		FEMALE_WEAK_STANDARD.put(14, 16.3);
		FEMALE_WEAK_STANDARD.put(15, 16.7);
		FEMALE_WEAK_STANDARD.put(16, 16.9);
		FEMALE_WEAK_STANDARD.put(17, 17.1);
		FEMALE_WEAK_STANDARD.put(18, 17.2);
	}

	/**
	 * 根据年龄,BMI,性别返回健康状态
	 *
	 * @param age
	 * @param BMI
	 * @param gender
	 * @return
	 */
	public static String fetchBMIState(int age, double BMI, int gender) {

		String state = null;
		Double standard = 0.0;
		String[] temp = null;
		if (age <= 7) {
			age = 7;
		}
		if (age >= 18) {
			age = 18;
		}

		switch (gender) {
			case 0:// female
				standard = FEMALE_FAT_STANDARD.get(age);
				if (BMI >= standard) {
					state = "肥胖";
					break;
				}

				temp = FEMALE_OVER_WEIGHT_STANDARD.get(age).split("#");
				if (Double.parseDouble(temp[0]) <= BMI
						&& BMI < Double.parseDouble(temp[1])) {
					state = "超重";
					break;
				}
				temp = FEMALE_NORMAL_STANDARD.get(age).split("#");
				if (Double.parseDouble(temp[0]) < BMI
						&& BMI < Double.parseDouble(temp[1])) {
					state = "正常";
					break;
				}
				standard = FEMALE_WEAK_STANDARD.get(age);
				if (BMI <= standard) {
					state = "消瘦";
					break;
				}
				state = "未知";
				break;

			case 1: // make
				standard = MALE_FAT_STANDARD.get(age);
				if (BMI >= standard) {
					state = "肥胖";
					break;
				}

				temp = MALE_OVER_WEIGHT_STANDARD.get(age).split("#");
				if (Double.parseDouble(temp[0]) <= BMI
						&& BMI < Double.parseDouble(temp[1])) {
					state = "超重";
					break;
				}

				temp = MALE_NORMAL_STANDARD.get(age).split("#");
				if (Double.parseDouble(temp[0]) < BMI
						&& BMI < Double.parseDouble(temp[1])) {
					state = "正常";
					break;
				}

				standard = MALE_WEAK_STANDARD.get(age);
				if (BMI <= standard) {
					state = "消瘦";
					break;
				}
				state = "未知";
				break;
			default:
				state = "未知";
		}
		return state;
	}
}

package com.sinosoft.fhcs.android.util;

/**
 * @CopyRight: SinoSoft.
 * @Description: 工具类
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.customview.DateWheelPop;
import com.sinosoft.fhcs.android.customview.NumberWheelPop;
import com.sinosoft.fhcs.android.customview.OnlyTimeWheelPop;
import com.sinosoft.fhcs.android.customview.TimeWheelPop;
import com.sinosoft.fhcs.android.entity.FamilyMember;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant {
	// 友盟
	public static String appKey = "5403dcdffd98c508d90027d7";
	public static String HOST = "http://www.yjkang.cn";
	// public static String HOST = "http://www.yjkang.cn:9090/portal";
	// 版本更新
	public static File conapk = null;
	public static String progress = null;
	// 省市区
	public static String province = "";
	public static String city = "";
	public static String district = "";
	public static String provinceId = "";
	public static String cityId = "";
	public static String districtId = "";
	public static String sessionIdPhone = "";
	public static String[] remindByMeal = { "餐前", "餐中", "餐后" };
	public static String[] gender = { "男", "女" };
	public static String[] type = { "健康秤", "脂肪仪", "血糖仪", "血压计", "耳温枪", "计步器" };
	public static String[] type1 = { "脂肪率", "水分率", "肌肉量", "内脏脂肪等级" };
	public static String[] type2 = { "低压高压", "脉搏" };
	public static String[] type3 = { "步数", "距离", "卡路里" };
	public static String[] InputByMeal = { "空腹血糖", "午餐前血糖", "晚餐前血糖", "睡前血糖",
			"早餐后2小时血糖", "午餐后2小时血糖", "晚餐后2小时血糖" };
	// 资讯信息
	public static final int Json_Request_Alltask = 1;
	public static final int Json_Request_Onetask = 2;
	public static final int Json_Return_Success = 999;
	public static final int Json_Return_Fail = 998;


	//默认连接选择的设备
	public static final String defaultDeviceWeight = "defaultDeviceWeight";//体重秤默认设备
	public static final String defaultDeviceBloodPress= "defaultDeviceBloodPress";//血压默认设备
	public static final String defaultDeviceBloodSugar= "defaultDeviceBloodSugar";//血糖仪默认设备

	// 显示提示框
	// public static void showDialog(Context context, String title) {
	// new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
	// .setCancelable(false).setMessage(title)
	// .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.cancel();
	// }
	// }).show();
	//
	// }

	public static void showDialog2(final Activity context, String title) {
		new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
				.setCancelable(false).setMessage(title)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						context.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();

					}
				}).show();

	}

	/**
	 * 折线图的时间
	 *
	 * @param strDate
	 * @return
	 */
	public static String getViewDate(String strDate) {
		String sub = "T";
		String str = "";
		int a = strDate.indexOf(sub);
		String ss1 = strDate.substring(0, a + sub.length() - 1);
		String ss2 = strDate.substring(a + sub.length(), strDate.length() - 3);
		String[] ss = ss1.split("-");
		str = ss[1] + "/" + ss[2] + "\n" + ss2;
		return str;
	}

	/**
	 * 显示进度条
	 *
	 * @param progressDialog
	 */
	public static void showProgressDialog(ProgressDialog progressDialog) {
		progressDialog.setMessage("加载数据请稍后");
		progressDialog.setCancelable(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

	}

	/**
	 * 根据出生日期获取年龄
	 *
	 * @param dateBirth
	 * @return
	 */
	public static int getAge(String dateBirth) {
		String year = dateBirth.substring(0, 4);
		int yearInt = Calendar.getInstance().get(Calendar.YEAR);
		return yearInt - Integer.valueOf(year);

	}

	/**
	 * 隐藏进度条
	 *
	 * @param progressDialog
	 */
	public static void exitProgressDialog(ProgressDialog progressDialog) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 邮箱的验证
	 *
	 * @param str
	 * @return true 为验证正确，false 为失败
	 */
	public static boolean isEmail(String str) {
		Pattern pattern = Pattern
				.compile("^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
		Matcher mat = pattern.matcher(str);
		return mat.matches();
	}

	/**
	 * 手机号码判断
	 *
	 * @param phone
	 * @return true 为验证正确，false 为失败
	 */
	public static boolean isPhone(String phone) {
		Pattern p = Pattern.compile("^1[34578]\\d{9}$");
		Matcher m = p.matcher(phone);
		return m.matches();

	}

	/**
	 * 手机号码和邮箱的判断
	 *
	 * @param phone
	 * @return true 为验证正确，false 为失败
	 */
	public static boolean isEmailOrPhone(String phone) {
		Pattern p = Pattern.compile("^1[34578]\\d{9}$");
		// Pattern p1 = Pattern
		// .compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Pattern p1 = Pattern
				.compile("^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
		Matcher m = p.matcher(phone);
		Matcher m1 = p1.matcher(phone);
		if (m.matches()) {
			return true;
		} else if (m1.matches()) {
			return true;
		}
		return false;

	}

	/**
	 * 指定格式的日期当前日期字符串
	 *
	 * @param formate
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getNowDateStrByFormate(String formate) {
		return new SimpleDateFormat(formate).format(Calendar.getInstance()
				.getTime());
	}

	/**
	 * 根据getFamilyRoleName 获取图片ID
	 *
	 * @param strImage
	 * @return
	 */
	public static int ImageId(String strImage, String sex) {
		if (strImage.equals("爷爷")) {
			return R.drawable.yeye;
		} else if (strImage.equals("奶奶")) {
			return R.drawable.nainai;
		} else if (strImage.equals("外公")) {
			return R.drawable.grandpa;
		} else if (strImage.equals("外婆")) {
			return R.drawable.grandmother;
		} else if (strImage.equals("爸爸")) {
			return R.drawable.father;
		} else if (strImage.equals("妈妈")) {
			return R.drawable.mother;
		} else if (strImage.equals("儿子")) {
			return R.drawable.son;
		} else if (strImage.equals("小儿子")) {
			return R.drawable.littleson;
		} else if (strImage.equals("女儿")) {
			return R.drawable.daughter;
		} else if (strImage.equals("小女儿")) {
			return R.drawable.littledaughter;
		} else if (strImage.equals("来宾男")) {
			return R.drawable.laibinboy;
		} else if (strImage.equals("来宾女")) {
			return R.drawable.laibingirl;
		} else {
			if (sex.trim().equals("男") || sex.trim().equals("1")) {
				return R.drawable.laibinboy;
			} else if (sex.trim().equals("女") || sex.trim().equals("0")) {
				return R.drawable.laibingirl;
			} else {
				return R.drawable.add;
			}

		}
	}

	/**
	 * 带背景的头像
	 *
	 * @param strImage
	 * @param sex
	 * @return
	 */
	public static int ImageIdbg(String strImage, String sex) {
		if (strImage.equals("爷爷")) {
			return R.drawable.yeye2;
		} else if (strImage.equals("奶奶")) {
			return R.drawable.nainai2;
		} else if (strImage.equals("外公")) {
			return R.drawable.grandpa2;
		} else if (strImage.equals("外婆")) {
			return R.drawable.grandmother2;
		} else if (strImage.equals("爸爸")) {
			return R.drawable.father2;
		} else if (strImage.equals("妈妈")) {
			return R.drawable.mother2;
		} else if (strImage.equals("儿子")) {
			return R.drawable.son2;
		} else if (strImage.equals("小儿子")) {
			return R.drawable.littleson2;
		} else if (strImage.equals("女儿")) {
			return R.drawable.daughter2;
		} else if (strImage.equals("小女儿")) {
			return R.drawable.littledaughter2;
		} else if (strImage.equals("来宾男")) {
			return R.drawable.laibinboy2;
		} else if (strImage.equals("来宾女")) {
			return R.drawable.laibingirl2;
		} else {
			if (sex.trim().equals("男") || sex.trim().equals("1")) {
				return R.drawable.laibinboy2;
			} else if (sex.trim().equals("女") || sex.trim().equals("0")) {
				return R.drawable.laibingirl2;
			} else {
				return R.drawable.add;
			}

		}
	}

	/**
	 * 获取年月日字符串数组
	 *
	 * @param strDate
	 * @return
	 */
	public static String[] getSplitDate(String strDate) {
		String sub = "T";
		String[] str;
		int a = strDate.indexOf(sub);
		if (a >= 0) {
			String ss1 = strDate.substring(0, a + sub.length() - 1);
			str = ss1.split("-");
		} else {
			str = strDate.split("-");
		}
		return str;
	}

	/**
	 * 获取年月日字符串
	 *
	 * @param strDate
	 * @return
	 */

	public static String getDateFormat(String strDate) {
		String sub = "T";
		String str = "";
		int a = strDate.indexOf(sub);
		if (a >= 0) {
			String ss1 = strDate.substring(0, a + sub.length() - 1);
			String[] ss = ss1.split("-");
			str = ss[0] + "/" + ss[1] + "/" + ss[2];
		} else {
			str = strDate;
		}
		return str;
	}

	/**
	 * 获取年月日 时分秒字符串
	 *
	 * @param strDate
	 * @return
	 */
	public static String getDateFormat2(String strDate) {
		String sub = "T";
		String str = "";
		int a = strDate.indexOf(sub);
		if (a >= 0) {
			String ss1 = strDate.substring(0, a + sub.length() - 1);
			String ss2 = strDate.substring(a + sub.length(), strDate.length());
			String[] ss = ss1.split("-");
			str = ss[0] + "/" + ss[1] + "/" + ss[2] + " " + ss2;
		} else {
			str = strDate;
		}
		return str;
	}

	public static String getDateFormat4(String strDate) {
		String sub = "T";
		String str = "";
		int a = strDate.indexOf(sub);
		if (a >= 0) {
			String ss1 = strDate.substring(0, a + sub.length() - 1);
			String ss2 = strDate.substring(a + sub.length(), strDate.length());
			String[] ss = ss1.split("-");
			str = ss[0] + "-" + ss[1] + "-" + ss[2] + " " + ss2;
		} else {
			str = strDate;
		}
		return str;
	}

	/**
	 * 获取月日 时分字符串
	 *
	 * @param strDate
	 * @return
	 */
	public static String getDateFormat3(String strDate) {
		String sub = "T";
		String str = "";
		int a = strDate.indexOf(sub);
		if (a >= 0) {
			String ss1 = strDate.substring(0, a + sub.length() - 1);
			String ss2 = strDate.substring(a + sub.length(), strDate.length());
			String[] ss = ss1.split("-");
			String[] ss3 = ss2.split(":");
			str = ss[1] + "/" + ss[2] + " " + ss3[1] + ":" + ss3[2];
		} else {
			str = strDate;
		}
		System.out.println(str);
		return str;
	}

	/**
	 * 血糖时段
	 *
	 * @param meal
	 * @return 1 空腹血糖, 2 早餐后2小时血糖, 3 午餐前血糖, 4 午餐后2小时血糖, 5 晚餐前血糖, 6 晚餐后2小时血糖, 7
	 *         睡前血糖,
	 */
	public static int putXueTangMeal(String meal) {
		// 餐前 60002101 //餐中 60002102 //餐后 60002103
		int number = 1;
		if (meal.trim().equals("空腹血糖")) {
			number = 1;
		} else if (meal.trim().equals("早餐后2小时血糖")) {
			number = 2;
		} else if (meal.trim().equals("午餐前血糖")) {
			number = 3;
		} else if (meal.trim().equals("午餐后2小时血糖")) {
			number = 4;
		} else if (meal.trim().equals("晚餐前血糖")) {
			number = 5;
		} else if (meal.trim().equals("晚餐后2小时血糖")) {
			number = 6;
		} else if (meal.trim().equals("睡前血糖")) {
			number = 7;
		}
		return number;
	}

	/**
	 * 餐前还是餐后
	 *
	 * @param number
	 * @return
	 */
	public static String getMeal(String number) {
		// 餐前 60002101 //餐中 60002102 //餐后 60002103
		String meal = "";
		if (number.trim().equals("60002101")) {
			meal = "餐前";
		} else if (number.trim().equals("60002102")) {
			meal = "餐中";
		} else if (number.trim().equals("60002103")) {
			meal = "餐后";
		}
		return meal;
	}

	/**
	 * 餐前还是餐后
	 *
	 * @param meal
	 * @return
	 */
	public static String putMeal(String meal) {
		// 餐前 60002101 //餐中 60002102 //餐后 60002103
		String number = "";
		if (meal.trim().equals("餐前")) {
			number = "60002101";
		} else if (meal.trim().equals("餐中")) {
			number = "60002102";
		} else if (meal.trim().equals("餐后")) {
			number = "60002103";
		}
		return number;
	}

	/**
	 * 提醒方式
	 *
	 * @param number
	 * @return
	 */
	public static String getMethods(String number) {
		// 手机客户端 60001101 //手机短信 60001102
		String methods = "";
		if (number.trim().equals("60001101")) {
			methods = "手机客户端";
		} else if (number.trim().equals("60001102")) {
			methods = "手机短信";
		} else if (number.trim().equals("60001101,60001102")) {
			methods = "手机客户端,手机短信";
		} else if (number.trim().equals("60001102,60001101")) {
			methods = "手机客户端,手机短信";
		}
		return methods;
	}

	/**
	 * 录入设备
	 *
	 * @param device
	 * @return
	 */
	public static int putSheBei(String device) {
		// 体重 4000101体脂 4000102 血压 4000103 血糖 4000104 体温 4000105 睡眠
		// 4000107
		int number = 4000101;
		if (device.trim().equals("体重")) {
			number = 4000101;
		} else if (device.trim().equals("体脂")) {
			number = 4000102;
		} else if (device.trim().equals("血压")) {
			number = 4000103;
		} else if (device.trim().equals("血糖")) {
			number = 4000104;
		} else if (device.trim().equals("体温")) {
			number = 4000105;
		} else if (device.trim().equals("睡眠")) {
			number = 4000107;
		} else if (device.trim().equals("运动")) {
			number = 4000106;
		}
		return number;
	}

	/**
	 * 获取结束日期
	 *
	 * @param dateStr
	 * @param count
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate(String dateStr, int count) {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = sim.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, count);

		String reStr = sim.format(cal.getTime());
		return reStr;

	}

	/**
	 * 获得版本号
	 *
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		if (context == null)
			return "";
		String versionName = "1.0";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		// return "V" + versionName;
		return versionName;
	}

	/**
	 * 根据月份获取日期
	 *
	 * @param dou
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDate(int num) {
		if (num != 2) {
			Calendar c = Calendar.getInstance(); // 当时的日期和时间
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			int month = c.get(Calendar.MONTH) - num;
			c.set(Calendar.MONTH, month);
			return simpleDateFormat.format(c.getTime());
		} else {
			return "";
		}

	}

	/**
	 * 根据天获取日期
	 *
	 * @param dou
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDay(int num) {
		Calendar c = Calendar.getInstance(); // 当时的日期和时间
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int day = c.get(Calendar.DAY_OF_MONTH) - num;
		c.set(Calendar.DAY_OF_MONTH, day);
		return simpleDateFormat.format(c.getTime());

	}

	/**
	 * 隐藏软键盘
	 *
	 * @param context
	 */
	@SuppressWarnings("static-access")
	public static void hiddenInput(Activity context) {
		((InputMethodManager) context
				.getSystemService(context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(context.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

	}

	/**
	 * double 按四舍五入的方式转换成int
	 *
	 * @param dou
	 * @return
	 */
	public static int getIntData(Double dou) {
		int aa = Integer.parseInt(new java.text.DecimalFormat("0").format(dou));
		return aa;

	}

	/**
	 * 展示Number
	 *
	 * @param activity
	 * @param btn
	 *            控件
	 * @param text
	 *            内容
	 * @param type
	 *            "height","weight","waist","stepSize"
	 * @param flag
	 *            添加成员，修改成员
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 */
	public static void showNum(Activity activity, Button btn, String text,
							   String type, String flag, int min, int max) {
		Double d = 0.0;
		if (!text.equals("")) {
			d = Double.valueOf(text);
		}

		NumberWheelPop menuWindow = new NumberWheelPop(activity, type, flag,
				min, max, getIntData(d));
		menuWindow.showAtLocation(btn, Gravity.BOTTOM, 0, 0);

	}

	/**
	 * 展示日期
	 *
	 * @param activity
	 * @param tv
	 *            控件
	 * @param text
	 *            内容
	 * @param flag
	 *            添加成员，修改成员，添加提醒，修改提醒
	 */
	public static void showDate(Activity activity, TextView tv, String text,
								String flag) {
		int curYear = 0;
		int curMonth = 0;
		int curDay = 0;
		if (!text.equals("")) {
			curYear = Integer.valueOf(getWheelDate(text)[0]);
			curMonth = Integer.valueOf(getWheelDate(text)[1]);
			curDay = Integer.valueOf(getWheelDate(text)[2]);
		}
		DateWheelPop menuWindow = new DateWheelPop(activity, curYear, curMonth,
				curDay, flag);
		menuWindow.showAtLocation(tv, Gravity.BOTTOM, 0, 0);

	}

	/**
	 * 展示时间
	 *
	 * @param activity
	 * @param tv
	 * @param text
	 * @param flag
	 */
	public static void showOnlyTime(Activity activity, TextView tv,
									String text, String flag) {
		int curHour = 0;
		int curMinute = 0;
		if (!text.equals("")) {
			curHour = Integer.valueOf(getWheelOnlyTime(text)[0]);
			curMinute = Integer.valueOf(getWheelOnlyTime(text)[1]);
		}
		OnlyTimeWheelPop menuWindow = new OnlyTimeWheelPop(activity, curHour,
				curMinute, flag);
		menuWindow.showAtLocation(tv, Gravity.BOTTOM, 0, 0);

	}

	/**
	 * 获取年月日字符串数组
	 *
	 * @param strDate
	 * @return
	 */
	public static String[] getWheelDate(String strDate) {
		String[] str = strDate.split("-");
		return str;
	}

	/**
	 * 获取时间字符串数组
	 *
	 * @param strDate
	 * @return
	 */
	public static String[] getWheelOnlyTime(String strDate) {
		String[] str = strDate.split(":");
		return str;
	}

	/**
	 * 展示日期+时间
	 *
	 * @param activity
	 * @param btn
	 * @param text
	 * @param flag
	 */
	public static void showTime(Activity activity, Button btn, String text,
								String flag) {
		int curYear = 0;
		int curMonth = 0;
		int curDay = 0;
		int curHour = 0;
		int curMinute = 0;
		if (!text.equals("")) {
			curYear = Integer.valueOf(getWheelDate2(text)[0]);
			curMonth = Integer.valueOf(getWheelDate2(text)[1]);
			curDay = Integer.valueOf(getWheelDate2(text)[2]);
			curHour = Integer.valueOf(getWheelDate2(text)[3]);
			curMinute = Integer.valueOf(getWheelDate2(text)[4]);
		}
		TimeWheelPop menuWindow = new TimeWheelPop(activity, curYear, curMonth,
				curDay, curHour, curMinute, flag);
		menuWindow.showAtLocation(btn, Gravity.BOTTOM, 0, 0);

	}

	/**
	 * 获取年月日 时分秒数组
	 *
	 * @param strDate
	 * @return
	 */
	public static String[] getWheelDate2(String strDate) {
		String sub = " ";
		String str[] = new String[6];
		;
		int a = strDate.indexOf(sub);
		if (a >= 0) {
			String ss1 = strDate.substring(0, a + sub.length() - 1);
			String ss2 = strDate.substring(a + sub.length(), strDate.length());
			String[] sss1 = ss1.split("-");
			String[] sss2 = ss2.split(":");
			for (int i = 0; i < sss1.length; i++) {
				str[i] = sss1[i];
			}
			for (int i = 0; i < sss2.length; i++) {
				str[i + 3] = sss2[i];
			}
		} else {
			str = null;
		}
		return str;
	}

	/**
	 * 在线播放视频
	 *
	 * @param activity
	 * @param url
	 */
	public static void playUrl(Context activity, String url) {
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
				extension);
		Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
		mediaIntent.setDataAndType(Uri.parse(url), mimeType);
		activity.startActivity(mediaIntent);

	}

	// 判断网络是否是WIFI
	public static boolean isWifiActive(Context icontext) {
		Context context = icontext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info;
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 两数相除，保留两位小数(用于计算)
	public static float divideF(float i, float j) {
		float d = i / j;
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(10);
		// System.out.println(nf.format(d));
		float t = Float.valueOf(nf.format(d));
		// Log.e("CommonUtil", "CommonUtil" + t);
		return t;

	}

	/**
	 * 附近展示距离
	 *
	 * @param num
	 * @return
	 */
	public static String NearDistance(double num) {
		String diatance = "";
		if (num <= 100) {
			diatance = "100米以内";
		} else if (num > 100 && num <= 200) {
			diatance = "200米以内";
		} else if (num > 200 && num <= 300) {
			diatance = "300米以内";
		} else if (num > 300 && num <= 400) {
			diatance = "400米以内";
		} else if (num > 400 && num <= 500) {
			diatance = "500米以内";
		} else if (num > 500 && num <= 600) {
			diatance = "600米以内";
		} else if (num > 600 && num <= 700) {
			diatance = "700米以内";
		} else if (num > 700 && num <= 800) {
			diatance = "800米以内";
		} else if (num > 800 && num <= 900) {
			diatance = "900米以内";
		} else if (num > 900 && num <= 1000) {
			diatance = "1000米以内";
		} else if (num > 1000 && num <= 1500) {
			diatance = "1500米以内";
		} else if (num > 1500 && num <= 2000) {
			diatance = "2000米以内";
		} else if (num > 2000 && num <= 2500) {
			diatance = "2500米以内";
		} else if (num > 2500 && num <= 3000) {
			diatance = "3000米以内";
		} else {
			diatance = "3000米以外";
		}
		return diatance;

	}

	/**
	 * 分钟转换为几小时几分
	 *
	 * @param minute
	 * @return
	 */
	public static String getHourFromMinute(int minute) {
		String str = "";
		if (minute < 60) {
			str = minute + "'";
		} else {
			int H = minute / 60;
			minute = minute % 60;
			int M = minute;
			if (M == 0) {
				str = H + "h";
			} else {
				str = H + "h" + M + "'";
			}
		}
		return str;
	}
	/**
	 *秒转换为几小时几分
	 *
	 * @param minute
	 * @return
	 */
	public static String getHourFromS(int s) {
		int minute = s/60;
		String str = "";
		if (minute < 60) {
			str = minute + "'";
		} else {
			int H = minute / 60;
			minute = minute % 60;
			int M = minute;
			if (M == 0) {
				str = H + "h";
			} else {
				str = H + "h" + M + "'";
			}
		}
		return str;
	}

	/**
	 * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
	 *
	 * @param listView
	 *            在设置LIstView的Adapter后调用此方法便可。
	 */
	public static void setListViewHeight(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {


		// 获取ListView对应的Adapter

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {

			return;

		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(0, 0); // 计算子项View 的宽高

			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		// listView.getDividerHeight()获取子项间分隔符占用的高度

		// params.height最后得到整个ListView完整显示需要的高度

		listView.setLayoutParams(params);

	}

	/**
	 * 预置信息
	 *
	 * @return
	 */
	public static List<FamilyMember> getYzList() {
		List<FamilyMember> yzList = new ArrayList<FamilyMember>();// 预置家庭列表
		FamilyMember member = new FamilyMember();
		member = new FamilyMember(1, "爷爷", Constant.getAge("1950-01-01"), "1",
				60, 170, 74, "1950-01-01", 65, R.drawable.yeye, "", false,
				4000101);
		yzList.add(member);
		member = new FamilyMember(2, "奶奶", Constant.getAge("1950-01-01"), "0",
				50, 160, 66, "1950-01-01", 55, R.drawable.nainai, "", false,
				4000101);
		yzList.add(member);
		member = new FamilyMember(3, "外公", Constant.getAge("1950-01-01"), "1",
				60, 170, 74, "1950-01-01", 65, R.drawable.grandpa, "", false,
				4000101);
		yzList.add(member);
		member = new FamilyMember(4, "外婆", Constant.getAge("1950-01-01"), "0",
				50, 160, 66, "1950-01-01", 55, R.drawable.grandmother, "",
				false, 4000101);
		yzList.add(member);
		member = new FamilyMember(5, "爸爸", Constant.getAge("1980-01-01"), "1",
				60, 175, 76, "1980-01-01", 70, R.drawable.father, "", false,
				4000101);
		yzList.add(member);
		member = new FamilyMember(6, "妈妈", Constant.getAge("1980-01-01"), "0",
				50, 165, 68, "1980-01-01", 65, R.drawable.mother, "", false,
				4000101);
		yzList.add(member);
		member = new FamilyMember(7, "儿子", Constant.getAge("2005-01-01"), "1",
				40, 130, 54, "2005-01-01", 50, R.drawable.son, "", false,
				4000101);
		yzList.add(member);
		member = new FamilyMember(11, "女儿", Constant.getAge("2005-01-01"), "0",
				20, 110, 41, "2005-01-01", 40, R.drawable.daughter, "", false,
				4000101);
		yzList.add(member);
		member = new FamilyMember(10, "小儿子", Constant.getAge("2010-01-01"),
				"1", 30, 120, 49, "2010-01-01", 40, R.drawable.littleson, "",
				false, 4000101);
		yzList.add(member);
		member = new FamilyMember(14, "小女儿", Constant.getAge("2010-01-01"),
				"0", 20, 100, 36, "2010-01-01", 30, R.drawable.littledaughter,
				"", false, 4000101);
		yzList.add(member);
		member = new FamilyMember(15, "来宾男", Constant.getAge("1980-01-01"),
				"1", 60, 175, 74, "1980-01-01", 65, R.drawable.laibinboy, "",
				false, 4000101);
		yzList.add(member);
		member = new FamilyMember(16, "来宾女", Constant.getAge("1980-01-01"),
				"0", 50, 160, 66, "1980-01-01", 55, R.drawable.laibingirl, "",
				false, 4000101);
		yzList.add(member);
		return yzList;

	}

	/**
	 * 天气预报
	 *
	 * @param strImage
	 * @return
	 */
	public static int ImageWeather(String strImage) {
		if (strImage.equals("晴")) {
			// 晴
			return R.drawable.sun;
		} else if (strImage.equals("多云")) {
			// 阴
			return R.drawable.yin;
		} else if (strImage.equals("阴")) {
			return R.drawable.yin;
		} else if (strImage.equals("阵雨")) {
			// 雨
			return R.drawable.rain;
		} else if (strImage.equals("雷阵雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("雷阵雨伴有冰雹")) {
			return R.drawable.rain;
		} else if (strImage.equals("雨夹雪")) {
			return R.drawable.rain;
		} else if (strImage.equals("小雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("中雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("大雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("暴雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("大暴雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("特大暴雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("冻雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("小到中雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("中到大雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("大到暴雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("暴雨到大暴雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("大暴雨到特大暴雨")) {
			return R.drawable.rain;
		} else if (strImage.equals("阵雪")) {
			// 雪
			return R.drawable.snow;
		} else if (strImage.equals("小雪")) {
			return R.drawable.snow;
		} else if (strImage.equals("中雪")) {
			return R.drawable.snow;
		} else if (strImage.equals("大雪")) {
			return R.drawable.snow;
		} else if (strImage.equals("暴雪")) {
			return R.drawable.snow;
		} else if (strImage.equals("小到中雪")) {
			return R.drawable.snow;
		} else if (strImage.equals("中到大雪")) {
			return R.drawable.snow;
		} else if (strImage.equals("大到暴雪")) {
			return R.drawable.snow;
		} else if (strImage.equals("雾")) {
			// 雾霾
			return R.drawable.wu;
		} else if (strImage.equals("沙尘暴")) {
			return R.drawable.wu;
		} else if (strImage.equals("浮尘")) {
			return R.drawable.wu;
		} else if (strImage.equals("扬沙")) {
			return R.drawable.wu;
		} else if (strImage.equals("强沙尘暴")) {
			return R.drawable.wu;
		} else if (strImage.equals("霾")) {
			return R.drawable.wu;
		} else {
			return R.drawable.sun;

		}
	}
	/**
	 * 设置hint文字大小
	 * @param hintText
	 * @param textSize
	 * @return editText.setHint(new SpannedString(ss));
	 */
	public static  SpannedString textHint(String hintText, int textSize) {
		// 新建一个可以添加属性的文本对象<br />
		SpannableString ss = new SpannableString(hintText);
		// 新建一个属性对象,设置文字的大小<br />
		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
		// 附加属性到文本<br />
		ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置hint<br />
		return new SpannedString(ss);

	}


	public static final int BLUETOOTH_FLAG_WEIGHT=30000;//体重仪
	public static final int BLUETOOTH_FLAG_BLOOD=30001;//血压计
	public static final int BLUETOOTH_FLAG_BLOOD_SUGAR=30002;//血糖仪

	//log
	public static String FOLDER_ROOT = Environment
			.getExternalStorageDirectory().getPath() + "/YiJiaKang/";
	public static String FOLDER_EXCEPTION = FOLDER_ROOT + "yijiakangException_log/";


	//初始化所有血压设备

	public static final String SP_BLOOD_KANGKANG = "SP_BLOOD_KANGKANG";
	public static final String SP_BLOOD_TIANTIAN = "SP_BLOOD_TIANTIAN";
	public static final String SP_BLOOD_YUWELL = "SP_BLOOD_YUWELL";

	public static final String SP_BLOOD_SELECT_TIZHONG = "SP_BLOOD_SELECT_TIZHONG";//默认选择
	public static final String SP_BLOOD_SELECT_TIWEN = "SP_BLOOD_SELECT_TIWEN";//默认选择
	public static final String SP_BLOOD_SELECT_SHUIMIAN = "SP_BLOOD_SELECT_SHUIMIAN";//默认选择
	public static final String SP_BLOOD_SELECT_XUEYA = "SP_BLOOD_SELECT_XUEYA";//默认选择
	public static final String SP_BLOOD_SELECT_XUETANG = "SP_BLOOD_SELECT_XUETANG";//默认选择
	public static final String SP_BLOOD_SELECT_YUNDONG = "SP_BLOOD_SELECT_YUNDONG";//默认选择
	public static final String SP_BLOOD_SELECT_TIZHI = "SP_BLOOD_SELECT_TIZHI";//默认选择

	public static final String SP_BLOOD_ALL = "SP_BLOOD_ALL";


	//判断程序是否是第一次运行的标示
	public  static final String APP_IS_FIRSR_RUN_TIZHONG = "app_is_first_run_tizhong"; //体重
	public  static final String APP_IS_FIRSR_RUN_XUEYA = "app_is_first_run_xueya";//血压
	public  static final String APP_IS_FIRSR_RUN_XUETANG = "app_is_first_run_xuetang"; //血糖
	public  static final String APP_IS_FIRSR_RUN_CHANGE = "app_is_first_run_change"; //设备切换

	//打开蓝牙
	public static final int OPEN_CODE = 2000;

	//云康宝体重秤appid
//		public  static final String YUNLOAD_APP_ID = "ade2e33efefdvdf";
	public  static final String YUNLOAD_APP_ID = "zgdxbjyjy2017090521";
//		public  static final String YUNLOAD_APP_ID = "123456789";

	public static final int department_erke = 30001;
	public static final int department_fuke = 30002;
	public static final int department_zhongyike = 30003;
	public static final int department_pifuke = 30004;
	public static final int department_xinlike = 30005;
	public static final int department_xiaohuake = 30006;
	public static final int department_puwaike = 30007;


	public static final int device_tizhong = 0x0001;
	public static final int device_tiwen = 0x0002;
	public static final int device_shuimian = 0x0003;
	public static final int device_xueya = 0x0004;
	public static final int device_xuetang = 0x0005;
	public static final int device_yundong = 0x0006;
	public static final int device_tizhi = 0x0007;
	public static final int device_result = 0x0008;

	public static final int device_shouhuan = 10069;	//	手环
	public static final int device_shoubiao = 10070;	//	手表
	public static final int device_tizhicheng = 10071;  //	体脂秤
	public static final int device_xueyaji = 10072;		//	血压计
	public static final int device_xuetangyi = 10073;	//	血糖仪
	public static final int device_xueyangji = 10074;	//	血氧机

	//预约挂号
	public static final int information_registration = 0x1001;
	//用药评估
	public static final int information_pharmacy = 0x1002;
	//疾病评估
	public static final int information_illness = 0x1003;

	public static final String KEY_HOME_ACTION = "key_home_action";//返回到主页面
	public static final int ACTION_RESTART_APP = 1;//被强杀
	public static final int STATUS_FORCE_KILLED = -1;//应用放在后台被强杀了
	public static final int STATUS_NORMAL = 2; //APP正常态//intent到MainActivity区分跳转目的

	public static final int PERMISSION_SUCCESS = 20001;//获取权限成功
	public static final int PERMISSION_FEILD = 20002;//获取权限失败



}

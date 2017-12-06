package com.sinosoft.fhcs.android.util;

import java.math.BigDecimal;

import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.PowerManager;

public class Util {
	/**
	 * 折线图设置
	 * @param renderer
	 * @param yNum
	 * @param xNum
	 * @return
	 */
	public static XYMultipleSeriesRenderer setRenderer(XYMultipleSeriesRenderer renderer,int yNum, int xNum) {
		// 设置背景颜色
		renderer.setBackgroundColor(Color.TRANSPARENT);
		renderer.setApplyBackgroundColor(true); // 使背景色生效
		renderer.setShowGrid(true);// 是否显示网格
		// renderer.setXLabels(12);//设置X轴显示的刻度标签的个数
		//		renderer.setYLabels(yNum);// 设置Y轴显示的刻度标签的个数
		renderer.setXLabels(xNum); // 设置X轴不显示数字（改用我们手动添加的文字标签）
		//		renderer.setXLabelsAlign(Align.RIGHT);// 刻度线与刻度标注之间的相对位置关系
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomButtonsVisible(false);
		//		renderer.setZoomEnabled(false, false);// 不能放大缩小
		//		renderer.setPanEnabled(true, false);// 不能上下拖动
		// renderer.setPanLimits(new double[] {0, (dates.size()) * 2, 0, 1000});
		return renderer;
	}
	/**
	 * 折线图设置
	 * @param colors
	 * @param styles
	 * @param color
	 * @param marginTop
	 * @return
	 */
	public static XYMultipleSeriesRenderer buildRenderer(int[] colors,
														 PointStyle[] styles,int goal) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer2(renderer, colors, styles,goal);
		return renderer;
	}
	public static void setRenderer2(XYMultipleSeriesRenderer renderer,
									int[] colors, PointStyle[] styles,int goal) {
		renderer.setAxisTitleTextSize(16);// 设置轴标题文字的大小
		renderer.setChartTitleTextSize(20);// 设置整个图表标题文字的大小
		renderer.setLabelsTextSize(25);// 设置轴刻度文字的大小
		renderer.setLegendTextSize(15);// 设置图例文字大小
		renderer.setPointSize(8f);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		renderer.setMargins(new int[] { 40, 50, 40, 50 });// 设置图表的外边框(上/左/下/右)
		renderer.setShowLegend(false); // 是否显示图例
		renderer.setMarginsColor(Color.argb(0, 0xff, 0, 0));// 边框外侧颜色 // 穿透背景色
		//		renderer.setMarginsColor(Color.BLUE);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			// 显示线上文字
			r.setLineWidth(3);
			r.setChartValuesTextSize(25);// 字号
			r.setChartValuesSpacing(15);// 距点间距
			if(goal==0){
				if(i==1){
					r.setDisplayChartValues(false);
					r.setDisplayChartValuesDistance(10);
					renderer.addSeriesRenderer(r);
				}else{
					r.setDisplayChartValues(true);
					r.setDisplayChartValuesDistance(10);
					renderer.addSeriesRenderer(r);
				}
			}else if(goal==2){
				if(i==2){
					r.setDisplayChartValues(false);
					r.setDisplayChartValuesDistance(10);
					renderer.addSeriesRenderer(r);
				}else{
					r.setDisplayChartValues(true);
					r.setDisplayChartValuesDistance(10);
					renderer.addSeriesRenderer(r);
				}
			}else{
				r.setDisplayChartValues(true);
				r.setDisplayChartValuesDistance(10);
				renderer.addSeriesRenderer(r);
			}

		}
	}
	/**
	 * 折线图设置
	 * @param renderer
	 * @param title
	 * @param xTitle
	 * @param yTitle
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @param axesColor
	 * @param labelsColor
	 */
	public static void setChartSettings(XYMultipleSeriesRenderer renderer,
										String title, String xTitle, String yTitle, double xMin,
										double xMax, double yMin, double yMax, int axesColor,
										int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setXLabelsColor(labelsColor);
		renderer.setYLabelsColor(0,labelsColor);
	}
	/**
	 * float转double
	 */
	public static float doubleToFloat(double d){
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(d));
		float f = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		return f;
	}
	/**
	 * double转float
	 */
	public static double FloatTodouble(float f){
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(f));
		double d = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return d;
	}

	/**
	 * 判断程序是否是第一次运行(某一个页面是否是第一次运行)
	 */

	public static boolean appIsFisrstRun(Context context,String Cons){
		SPUtil instance = SPUtil.getInstance(context);
		//int isFirstRun = instance.getInt(Constant.APP_IS_FIRSR_RUN, -1);
		int isFirstRun = instance.getInt(Cons, -1);
		if (isFirstRun == -1){ //0 不是第一次运行 -1 第一次运行
			System.out.println("程序第一次运行");
			instance.putInt(Cons, 0);
			return true;
		} else{
			System.out.println("程序不是第一次运行");
			return false;
		}
	}

	//判断屏幕是否息屏了
	public static boolean getScreen(Context c){
		PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	//判断Android 系统的版本
	public static boolean isSupportBlueVersion(int supportVersion){
		int currentapiVersion=android.os.Build.VERSION.SDK_INT;
		System.out.println("当前系统的版本为=="+currentapiVersion);
		if(currentapiVersion > supportVersion){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 获取渠道名
	 * @param ctx 此处习惯性的设置为activity，实际上context就可以
	 * @return 如果没有获取成功，那么返回值为空
	 */
	public static String getChannelName(Activity ctx) {
		if (ctx == null) {
			return null;
		}
		String channelName = null;
		try {
			PackageManager packageManager = ctx.getPackageManager();
			if (packageManager != null) {
				//注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						channelName = applicationInfo.metaData.getString("");
					}
				}

			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return channelName;
	}

}

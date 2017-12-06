package com.sinosoft.fhcs.android;

/**
 * @CopyRight: SinoSoft.
 * @Description: 退出Application
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sinosoft.fhcs.android.entity.MeasureMainInfo;
import com.sinosoft.fhcs.android.entity.UserInfo;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.FRToast;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import cn.jpush.android.api.JPushInterface;
import cn.miao.lib.MiaoApplication;
import cn.miao.lib.MiaoHealth;
import cn.miao.lib.listeners.MiaoInitListener;

public class ExitApplicaton extends MultiDexApplication implements
		UncaughtExceptionHandler {
	private static List<Activity> activityList = new LinkedList<Activity>();
	private static ExitApplicaton instance;
	private static String appId="ade2e33efefdvdf";//Yolanda体重仪

	/**
	 * 主线程ID
	 */
	public static int mMainThreadId = -1;
	/**
	 * 主线程ID
	 */
	public static Thread mMainThread;
	/**
	 * 主线程Handler
	 */
	public static Handler mMainThreadHandler;
	/**
	 * 主线程Looper
	 */
	public static Looper mMainLooper;
	public static UserInfo userInfo;

	public static MiaoHealth miaoHealthManager;


	//初始化

	{
		//微信 wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
		PlatformConfig.setWeixin("wxb34fea6d3ef789ba", "c17099f9a5ceb33bb1c7d261f0e9cef4");
		//豆瓣RENREN平台目前只能在服务器端配置
		//新浪微博
		PlatformConfig.setSinaWeibo("1152174270", "ee4b741693db33fe9b3e71d9fbf19940");
		//易信
		PlatformConfig.setYixin("yx53bb9325dd0549efa72e8ad3e91c5761");
		PlatformConfig.setQQZone("1104733416", "drOJideTroCBvmRS");

	}
	public static List<MeasureMainInfo> getList = new ArrayList<>();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		mMainThreadId = android.os.Process.myTid();
		mMainThread = Thread.currentThread();
		mMainThreadHandler = new Handler();
		mMainLooper = getMainLooper();
		/*QNApiManager.getApi(this).initSDK(Constant.YUNLOAD_APP_ID, true, new QNResultCallback(){
			public void onCompete(int arg0) {
				Log.d("onCompete error:", String.valueOf(arg0));
			}
		});*/
		//友盟统计
		MobclickAgent.setDebugMode(true);
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);
		//Jpush
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush
		initImageLoader(getApplicationContext());
		Thread.setDefaultUncaughtExceptionHandler(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				MiaoApplication.init(getInstance(), "mpxv9fv1lvit9vdbxm", "d7fb69a48023a33e0f2b519ce191bbe6", new MiaoInitListener() {
					@Override
					public void onSuccess() {
//						FRToast.showToastSafe("application初始化成功");
					}

					@Override
					public void onError(int i, String s) {
						FRToast.showToastSafe("妙健康初始化失败");
					}
				});
			}
		}).start();
	}
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}

	// 单例模式中获取唯一的ExitApplication实例
	public static ExitApplicaton getInstance() {
		if (null == instance) {
			instance = new ExitApplicaton();
		}
		return instance;

	}

	public static void removes(Activity activity) {
		activityList.remove(activity);

	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish

	public void exit() {

		for (Activity activity : activityList) {
			activity.finish();
		}

	}

	public void forceQuit() {
		for (Activity activity : activityList) {
			String className = activity.getClass().getName();
			System.out.println("className = " + className);
			activity.finish();
		}
	}

	/**
	 *
	 * @param ex
	 */
	private void saveErrorLog2Sdcard(Throwable ex) {
		try {
			Calendar cal = Calendar.getInstance(TimeZone
					.getTimeZone("GMT+08:00"));
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			int second = cal.get(Calendar.SECOND);
			File dir = new File(Constant.FOLDER_EXCEPTION);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, "yijiakangexception.log");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file, true);
			String exceptionHeader ="*************************yijiakang************";
			exceptionHeader=exceptionHeader+"********************at " + year + "/"
					+ month + "/" + day + "  " + hour + ":" + minute + ":"
					+ second + "  ***************\r\n</br>";
			fos.write(exceptionHeader.getBytes());
			ex.printStackTrace(new PrintStream(fos));
			fos.flush();
			fos.close();
			Log.e("yijiakang", "********************at " + year + "/" + month
					+ "/" + day + "  " + hour + ":" + minute + ":" + second
					+ "  ***************\n");
			try {
				PackageInfo pi = getPackageManager().getPackageInfo(
						this.getPackageName(), 0);
				exceptionHeader = exceptionHeader + "version:" + pi.versionCode
						+ ",versionName:" + pi.versionName + "\n";
			} catch (Exception e) {
				e.printStackTrace();
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ex.printStackTrace();
			ex.printStackTrace(new PrintStream(baos));
			exceptionHeader = exceptionHeader.replaceAll("\n", "</br>");
			/*Api.getInstance(getApplicationContext()).requestNet(ApiIdConstants.APIID_UPLOADCREASHINFO, exceptionHeader,
					"cuihao@sinosoft.com.cn,wang_rui@sinosoft.com.cn");*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void uncaughtException(Thread arg0, final Throwable ex) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				saveErrorLog2Sdcard(ex);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}).start();

	}
	public static DisplayImageOptions getImageLoaderOptions(boolean isRound, int emptyResId, int onFailResId) {
		int circleValue = 0;
		if(isRound){
			circleValue = 20;
		}
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)// 是否緩存都內存中
				.cacheOnDisc(true)// 是否緩存到sd卡上
				.displayer(new RoundedBitmapDisplayer(circleValue)).
						showImageForEmptyUri(emptyResId).
						showImageOnFail(onFailResId).build();
		return options;
	}
	public static DisplayImageOptions getImageLoaderOptionsOfCircle() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)// 是否緩存都內存中
				.cacheOnDisc(true)// 是否緩存到sd卡上
				.displayer(new CircleBitmapDisplayer()).build();
		return options;
	}

	public static void setInstance(ExitApplicaton instance) {
		ExitApplicaton.instance = instance;
	}

	public static int getmMainThreadId() {
		return mMainThreadId;
	}

	public static void setmMainThreadId(int mMainThreadId) {
		ExitApplicaton.mMainThreadId = mMainThreadId;
	}

	public static Thread getmMainThread() {
		return mMainThread;
	}

	public static void setmMainThread(Thread mMainThread) {
		ExitApplicaton.mMainThread = mMainThread;
	}

	public static Handler getmMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static void setmMainThreadHandler(Handler mMainThreadHandler) {
		ExitApplicaton.mMainThreadHandler = mMainThreadHandler;
	}

	public static Looper getmMainLooper() {
		return mMainLooper;
	}

	public static void setmMainLooper(Looper mMainLooper) {
		ExitApplicaton.mMainLooper = mMainLooper;
	}
}

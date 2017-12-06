package com.sinosoft.fhcs.android.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinosoft.fhcs.android.ExitApplicaton;
import com.sinosoft.fhcs.android.entity.ChangeDeviceInfo;
import com.sinosoft.fhcs.android.entity.EquipmentListInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.miao.lib.MiaoApplication;
import cn.miao.lib.enums.DataTypeEnum;
import cn.miao.lib.listeners.MiaoConnectBleListener;
import cn.miao.lib.listeners.MiaoRegisterListener;
import cn.miao.lib.model.BloodGlucoseBean;
import cn.miao.lib.model.BloodPressureBean;
import cn.miao.lib.model.DataBean;
import cn.miao.lib.model.HeartBean;
import cn.miao.lib.model.SleepBean;
import cn.miao.lib.model.SlimmingBean;
import cn.miao.lib.model.Spo2Bean;
import cn.miao.lib.model.SportBean;
import cn.miao.lib.model.TemperatureBean;

import static com.sinosoft.fhcs.android.ExitApplicaton.miaoHealthManager;
import static com.sinosoft.fhcs.android.activity.devicframgne.DeviceListFragment.FORMAT_DATE_TIME;
import static com.sinosoft.fhcs.android.util.Constant.device_result;

public class CommonUtil {
	/**
	 * 获取默认的设备
	 */
	public static  EquipmentListInfo getDefaultDevice(Context context,String strTitle){
		EquipmentListInfo result = null;
		String spKey = null;
		if(strTitle.equals("体重")){
			spKey = Constant.defaultDeviceWeight;
		}else if(strTitle.equals("血压")){
			spKey = Constant.defaultDeviceBloodPress;
		}else if(strTitle.equals("血糖")){
			spKey = Constant.defaultDeviceBloodSugar;
		}
		result = SPUtil.getInstance(context).getObj(spKey, null);
		return result;
	}
	/**
	 * 设置默认设备
	 */
	public static void goToSetDefaultDevice(Activity context,String strTitle,Class settingDefaultClass){
		String spKey = null;
		if(strTitle.equals("体重")){
			spKey = Constant.defaultDeviceWeight;
		}else if(strTitle.equals("血压")){
			spKey = Constant.defaultDeviceBloodPress;
		}else if(strTitle.equals("血糖")){
			spKey = Constant.defaultDeviceBloodSugar;
		}
		Intent intent = new Intent(context, settingDefaultClass);
		intent.putExtra("type", spKey);
		context.startActivityForResult(intent, 1);
	}
	/**
	 * 弹出确认框是否设置默认设备
	 */
	public static void showDialogSettingDefaultDevice(final Activity activity,final String strTitle,final Class settingDefaultClass){

		new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT)
				.setCancelable(false)
				.setTitle("温馨提示")
				.setMessage("您还未设置默认的测量设备，是否现在就去设置?")
				.setPositiveButton("设置",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int which) {
								CommonUtil.goToSetDefaultDevice(activity, strTitle, settingDefaultClass);
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();
								activity.finish();
							}
						}).show();
	}
	/**
	 * 第三方登录参数：
	 * platform：登录平台platform == SHARE_MEDIA.QQ，platform == SHARE_MEDIA.WEIXIN
	 * data：获取用户信息源头
	 */
	public static Map<String,String> getThirdLoginParam(SHARE_MEDIA platform, Map<String, String> data){
		Map<String,String> result = new HashMap<String, String>();
		String platformName = " ";//平台名字：qq或weixin
		String nickName = "";//第三方登录的昵称
		String headIconUrl = "";//第三方登录的头像地址
		if(platform == SHARE_MEDIA.QQ){//通过qq登录，获取qq用户的信息封装
			platformName = "qq";
			nickName = data.get("screen_name");
			headIconUrl = data.get("profile_image_url");
		}else if(platform == SHARE_MEDIA.WEIXIN){//通过微信登录，获取微信用户的信息封装
			platformName = "weixin";
			nickName = data.get("screen_name");
			headIconUrl = data.get("headimgurl");
		}
		result.put("platformName", platformName);
		result.put("nickName", nickName);
		result.put("headIconUrl", headIconUrl);
		return result;
	}
	public static boolean isEnableBluetooth(){
		boolean result = false;
		try {
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			if (mBluetoothAdapter != null) {
				if(mBluetoothAdapter.isEnabled()){
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void openBluetooth(){
		try {
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			if(mBluetoothAdapter != null&&!mBluetoothAdapter.isEnabled()){
				mBluetoothAdapter.enable();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 判断网络状态
	 */
	public static boolean isEnabledNetWork(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * 读取图片的旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	private int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 *
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}
	//隐藏软键盘
	public static void hideInputWindow(Activity context){
		if(context==null){
			return;
		}
		final View v = ((Activity) context).getWindow().peekDecorView();
		if (v != null && v.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}
	/**
	 * 下载一个图片为直角的图片
	 *
	 * @param url
	 * @param imageView
	 */
	public static void downloadIcon2View(String url, ImageView imageView, int emptyResId, int onFailResId) {
		ImageLoader.getInstance().displayImage(url, imageView, ExitApplicaton.getImageLoaderOptions(false, emptyResId, onFailResId));
	}

	/**
	 * 下载一个图片为圆角形
	 *
	 * @param url
	 * @param imageView
	 */
	public static void downloadIcon2ViewRound(String url, ImageView imageView, int emptyResId, int onFailResId) {
		ImageLoader.getInstance().displayImage(url, imageView,ExitApplicaton.getImageLoaderOptions(true,emptyResId,onFailResId));
	}

	/**
	 * 下载一个图片格式为纯圆形的
	 *
	 * @param url
	 * @param imageView
	 */
	public static void downloadIcon2ViewCircle(String url, ImageView imageView) {
		ImageLoader.getInstance().displayImage(url, imageView,ExitApplicaton.getImageLoaderOptionsOfCircle());
	}

	public static String getCurrentTime(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		if (format == null || format.trim().equals("")) {
			sdf.applyPattern(FORMAT_DATE_TIME);
		} else {
			sdf.applyPattern(format);
		}
		return sdf.format(new Date());
	}

	public static void sendMessage(Handler handler, int what, String msg) {
		Message message = new Message();
		message.what = what;
		message.obj = msg;
		handler.sendMessage(message);
	}
	public static void sendMessage(Handler handler, int what, Object msg) {
		Message message = new Message();
		message.what = what;
		message.obj = msg;
		handler.sendMessage(message);
	}


	public static void getBleDeviceData(final ChangeDeviceInfo changeDeviceInfo, HashMap map, final Handler handler) {
		sendMessage(handler,0, "开始连接蓝牙获取数据...");
		miaoHealthManager.fetchBLEConnect(changeDeviceInfo.getConnectId(), changeDeviceInfo.getDeviceSn(), changeDeviceInfo.getDeviceNo(), map, new MiaoConnectBleListener() {
			@Override
			public void onBleStatusChange(int code, String msg) {
				sendMessage(handler,code, "蓝牙状态改变 code：" + code + " msg:" + msg);
				/*if(code == 1){
					sendMessage(handler,0, "蓝牙状态改变 code：" + code + " msg:" + msg);
				}
				if(code == 5 || code == 2){
					sendMessage(handler,0, msg);
				}*/
			}

			@Override
			public void onBleDeviceMsg(int i, String s) {
				sendMessage(handler,0, i + " : " + s);
			}

			@Override
			public <T extends DataBean> void onBleDataResponse(DataTypeEnum dataTypeEnum, T bleDataBean) {
				Log.i("tga", "onBleDataResponse===" + dataTypeEnum +"    bleDataBean==="+bleDataBean);
				if (dataTypeEnum == DataTypeEnum.DATA_BLOOD_GLUCOSE) {
					BloodGlucoseBean boodglucoseBean = (BloodGlucoseBean) bleDataBean;
					if (boodglucoseBean != null)
						sendMessage(handler,device_result, boodglucoseBean);
//						sendMessage(handler,1, "血糖值： " + boodglucoseBean.getGlucose_value());
					else
						sendMessage(handler,0, "获取血糖数据失败");
				} else if (dataTypeEnum == DataTypeEnum.DATA_TEMPERATURE) {
					TemperatureBean temperatureBean = (TemperatureBean) bleDataBean;
					if (temperatureBean != null)
						sendMessage(handler,device_result,  temperatureBean);
//						sendMessage(handler,1, "体温: " + temperatureBean.getTemperature());
					else
						sendMessage(handler,0, "获取蓝牙体温数据失败");
				} else if (dataTypeEnum == DataTypeEnum.DATA_SLIMMING) {
					SlimmingBean slimmingBean = (SlimmingBean) bleDataBean;
					if (slimmingBean != null) {
						sendMessage(handler,device_result, slimmingBean);
//						sendMessage(handler,1, "体重:" + slimmingBean.getWeight() +"kg"
//								+"\n体脂率:" + slimmingBean.getFat_ratio()+"%"
//								+ "\n肌肉量:" + slimmingBean.getMuscle()+"kg"
//								+ "\n骨重:" + slimmingBean.getBone_mass()+"kg"
//								+ "\n基础代谢:" + slimmingBean.getMetabolism()+"kcal"
//								+ "\n体水分:" + slimmingBean.getMoisture() + "%"
//								+ "\nbmi:" + slimmingBean.getBmi());
					} else {
						sendMessage(handler,0, "获取蓝牙瘦身数据失败");
					}
				} else if (dataTypeEnum == DataTypeEnum.DATA_BLOOD_PRESSURE) {
					BloodPressureBean blood = (BloodPressureBean) bleDataBean;
					if (blood != null) {
						sendMessage(handler,device_result,blood);
//						sendMessage(handler,1,"高压:" + blood.getHigh_press()
//								+ "\n低压:" + blood.getLow_press()
//								+ "\n心率：" + blood.getHeart_rate());
					} else {
						sendMessage(handler,0, "获取蓝血压瘦身数据失败");
					}
				} else if (dataTypeEnum == DataTypeEnum.DATA_SLEEP) {
					SleepBean sleepBean = (SleepBean) bleDataBean;
					if (sleepBean != null) {
						sendMessage(handler,device_result, sleepBean);
//						sendMessage(handler,1, "有效睡眠:" + sleepBean.getEffect_duration() + "秒"
//										+ "\n总睡眠:" + sleepBean.getDuration() + "秒"
//										+ "\n深睡:" + sleepBean.getDeep_time()+ "秒"
//										+ "\n浅睡:" + sleepBean.getLight_time() + "秒"
//										+ "\n时间:" + sleepBean.getDate_time()
////                                + "开始测量时间:" + sleepBean.getMeasure_time()
//						);
						//+ "睡眠质量数据:" + sleepBean.getQuality()
					} else {
						sendMessage(handler,0, "获取蓝牙睡眠数据失败");
					}
				} else if (dataTypeEnum == DataTypeEnum.DATA_SPORT) {
					SportBean sportBean = (SportBean) bleDataBean;
					if (sportBean != null) {
						sendMessage(handler,device_result, sportBean);
//						sendMessage(handler,1, "步数:" + sportBean.getSteps()+"步"
//								+ "\n卡路里:" + sportBean.getCalories()+"cal"
//								+ "\n距离里:" + sportBean.getDistance()+"米"
//								+ "\n时间:" + sportBean.getDate_time());
					} else {
						sendMessage(handler,0, "获取蓝牙运动数据失败");
					}
				} else if (dataTypeEnum == DataTypeEnum.DATA_HEART) {
					HeartBean heartBean = (HeartBean) bleDataBean;
					if (heartBean != null) {
						sendMessage(handler,device_result, heartBean);
//						sendMessage(handler,1, "心率:" + heartBean.getHeart_rate());
					} else {
						sendMessage(handler,0, "获取蓝牙心率数据失败");
					}
				} else if(dataTypeEnum == DataTypeEnum.DATA_SPO2){
					Spo2Bean spo2Bean = (Spo2Bean) bleDataBean;
					if (spo2Bean != null) {
						sendMessage(handler,device_result, spo2Bean);
//						sendMessage(handler,1, "血氧:" + spo2Bean.getBlood_oxygen()
//								+ "\n心率:" + spo2Bean.getHeart_rate());
					} else {
						sendMessage(handler,0, "获取蓝牙血氧数据失败");
					}
				}
			}

			@Override
			public void onBleDataErr() {
				sendMessage(handler,0, "获取蓝牙数据失败");
			}
		});
	}

	/**
	 * 返回当前的应用是否处于前台显示状态
	 * @param $packageName
	 * @return
	 */
	public static boolean isTopActivity(Context _context,String $packageName)
	{
		//_context是一个保存的上下文
		ActivityManager __am = (ActivityManager) _context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> __list = __am.getRunningAppProcesses();
		if(__list.size() == 0) return false;
		for(ActivityManager.RunningAppProcessInfo __process:__list)
		{
			Log.d("tag",Integer.toString(__process.importance));
			Log.d("tag",__process.processName);
			if(__process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
					__process.processName.equals($packageName))
			{
				return true;
			}
		}
		return false;
	}

	public static void initMiao(Context context) {//初始化妙健康
		miaoHealthManager = MiaoApplication.getMiaoHealthManager();
		if (MiaoApplication.getMiaoHealthManager() == null) {
			FRToast.showToastSafe("请先初始化妙健康");
			return;
		}
		SharedPreferences prefs = context.getSharedPreferences("UserInfo",
				Context.MODE_PRIVATE);
		String phoneStr = prefs.getString("phoneNumber", "");
		if(phoneStr != null){
			miaoHealthManager.registerUserIdentity(phoneStr, new MiaoRegisterListener() {
				@Override
				public void onSuccess() {
//					FRToast.showToastSafe("妙健康MainActivity注册成功");
				}
				@Override
				public void onError(int i, String s) {
					FRToast.showToastSafe("妙健康注册失败==" + s);
				}
			});
		}


	}

}

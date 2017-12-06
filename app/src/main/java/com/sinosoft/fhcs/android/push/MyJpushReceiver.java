package com.sinosoft.fhcs.android.push;

/**
 * @CopyRight: SinoSoft.
 * @Description: 极光推送接收器
 * @Author: wangshuangshuang.
 * @Create: 2015年1月15日.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sinosoft.fhcs.android.activity.InformationDetailActivity;
import com.sinosoft.fhcs.android.activity.MedicineMessageActivity;
import com.sinosoft.fhcs.android.activity.SysMessageActivity;
import com.sinosoft.fhcs.android.entity.InformationChild;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyJpushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			//send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			//打开自定义的Activity
//        	Intent i = new Intent(context, PushViewActivity.class);
//        	i.putExtras(bundle);
//        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);
			String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
			String type="1";// 1==>服药提醒 2==>健康消息 3==>系统消息
			try {
				JSONObject jo = new JSONObject(json);
				type = jo.optString("type");
				if(type.trim().equals("1")){
					Intent i1 = new Intent(context, MedicineMessageActivity.class);
					i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
					context.startActivity(i1);
				}else if(type.trim().equals("2")){
					String id =jo.optString("id");
					String title =jo.optString("title");
					String pubdate =jo.optString("pubdate");
					boolean isRead=jo.optBoolean("isRead");
					String memberName =jo.optString("familyMemberRoleName");
					String informationType =jo.optString("heathInformationType");
					String content =jo.optString("informationContent");
					String image =jo.optString("image");
					String html =jo.optString("html");
					String video =jo.optString("video");
					String voice =jo.optString("voice");
					String cooperateIdentify=jo.optString("cooperateIdentify");
					InformationChild item=new InformationChild(id, html, image, video, voice, "", memberName, content, informationType, pubdate, title, isRead, "0",cooperateIdentify);
					Intent i2 = new Intent(context, InformationDetailActivity.class);
					i2.putExtra("entity", item);
					i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
					context.startActivity(i2);
				}else{
					Intent i3 = new Intent(context, SysMessageActivity.class);
					i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
					context.startActivity(i3);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}


		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

		} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			}
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
}

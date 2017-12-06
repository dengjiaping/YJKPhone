package com.sinosoft.fhcs.android.service;

/**
 * @CopyRight: SinoSoft.
 * @Description: 版本更新。。下载安装 服务
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sinosoft.fhcs.android.activity.TishiActivity;
import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.util.Constant;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

@SuppressLint({ "ShowToast", "HandlerLeak" })
public class VersionService extends Service {
	File apk = null;
	long progress = 0;
	long j;
	NotificationManager m_NotificationManager;
	// 声明Notification对象
	Notification m_Notification;
	RemoteViews remoteView;
	Broadcastdeleate br;
	Thread thread;
	String versionUrl = "";
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					Toast.makeText(VersionService.this, "下载出现问题，请稍后重试！",
							Toast.LENGTH_SHORT).show();
					break;
			}
			super.handleMessage(msg);
		}
	};

	class Broadcastdeleate extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			m_NotificationManager.cancel(0);
			System.out.println("广播");
			if (thread.isAlive())
				thread.interrupt();
			VersionService.this.stopSelf();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences prefs = getSharedPreferences("NewVersion",
				Context.MODE_PRIVATE);
		versionUrl = prefs.getString("url", "");
		System.out.println("启动service,下载连接是：" + versionUrl);
		notificationss();
		thread = new Thread() {
			@Override
			public void run() {
				try {
					downloadApk(versionUrl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("下载", "出错了");
					Message msg = new Message();
					msg.what = 1;
					mHandler.sendMessage(msg);
				}
			}
		};
		thread.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		br = new Broadcastdeleate();
		IntentFilter filter = new IntentFilter("guanbinotification");
		registerReceiver(br, filter);
	}

	@SuppressWarnings("deprecation")
	private void notificationss() {
		// TODO Auto-generated method stub
		m_NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		m_Notification = new Notification(R.drawable.stat_downloading,
				getString(R.string.load_soft_new_version), 20000);
		m_Notification.flags = Notification.FLAG_INSISTENT;// 可以删除 clear
		remoteView = new RemoteViews(this.getPackageName(), R.layout.upload);
		remoteView.setImageViewResource(R.id.image, R.drawable.banbenicon);

		Intent intent2 = new Intent(VersionService.this, TishiActivity.class);// 134217728
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				VersionService.this, 0, intent2, 0);
		m_Notification.contentIntent = pendingIntent;
		m_Notification.contentView = remoteView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(br);
		System.out.println("服务销毁");
	}

	private void downloadApk(String paramString) throws IOException {
		System.out.println(paramString + "paramString");
		File localFile = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/fhcs/");
		localFile.mkdir();
		this.apk = File.createTempFile("fhcs", ".apk", localFile);
		System.out.println(apk.getAbsolutePath() + "---dddd");
		Constant.conapk = apk;

		FileOutputStream localFileOutputStream = new FileOutputStream(this.apk);
		BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(
				localFileOutputStream);
		HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
				paramString).openConnection();
		localHttpURLConnection.connect();
		long i = 0;
		j = localHttpURLConnection.getContentLength();
		System.out.println(j);
		InputStream localInputStream = localHttpURLConnection.getInputStream();
		byte[] arrayOfByte = new byte[10242];
		int k;
		while (true) {
			k = localInputStream.read(arrayOfByte);
			if (k < 0)
				break;
			else {
				i += k;
				this.progress = (i * 100 / j);
				System.out.println("进度:" + progress + " 得到" + k + " 累计:" + i
						+ " 总体积:" + j + " 公式：" + (i * 100 / j));
				Message localMessage = new Message();
				localMessage.what = 1;
				localMessage.arg1 = (int) progress;
				localBufferedOutputStream.write(arrayOfByte, 0, k);
				if (progress % 5 == 0) {
					this.msgHandler.sendMessage(localMessage);
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		localBufferedOutputStream.close();
		localFileOutputStream.close();
	}

	private Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				remoteView.setProgressBar(R.id.progresss, 100, msg.arg1, false);
				remoteView.setTextViewText(R.id.texts,
						getString(R.string.load_progress) + "   " + msg.arg1
								+ "%");
				m_NotificationManager.notify(0, m_Notification);// 开始通知
			}

			if (msg.arg1 == 100) {
				Constant.progress = progress + "";
				m_Notification.icon = R.drawable.icon;
				// remoteView.setTextViewText(R.id.texts, "点击进行安装");
				remoteView.setTextViewText(R.id.texts,
						getString(R.string.installation_soft));
				m_NotificationManager.notify(0, m_Notification);// 开始通知
				// Toast.makeText(VersionService.this, "下载完成，请到后台安装最新版本",
				// 1).show();
				Toast.makeText(VersionService.this,
						getString(R.string.load_seccess), 1).show();
			}
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
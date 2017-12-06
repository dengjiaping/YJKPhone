package com.sinosoft.fhcs.android.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BluetoothUtils {

	private BroadcastReceiver bluetoothReceive;
	private Context context;
	//private ArrayList<BluetoothDevice> btList = new ArrayList<BluetoothDevice>(); // 用于保存获取到的蓝牙
	private List<String> names = new ArrayList<String>();
	private IntentFilter mFilter;
	private Handler han;

	//private String[] btNames = {"Yolanda"  ,"RBP1603010001", "Sinocare" ,"WIN-KLEE" , "YMETECH"};
	private String[] btWeightNames = {"Yolanda"};
	private String[] btBloodNames = {"YMETECH","RBP1603010001","Yuwell BloodPressure"};//康康、天天、鱼跃

	private String[] btBloodSugarNames = {"Sinocare"};
	private BluetoothAdapter mBluetoothAdapter;

	private int flag;
	public BluetoothUtils(Context context,Handler han, int flag) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.flag =flag;
		//btList.clear();
		names.clear();
		this.han = han;
		initBT();
		startSearch();


	}

	public static boolean isBluetoothDeviceOpen(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		return bluetoothAdapter.isEnabled();

	}
	public static boolean enableBluetoothDevice(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		return bluetoothAdapter.enable();
	}
	public static boolean enableBluetoothDevice2(Activity activity){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		return bluetoothAdapter.enable();
	}

	private void startSearch() {
		// TODO Auto-generated method stub
		// 如果正在搜索，就先取消搜索
		/*Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        if(devices.size()>0){
            for(Iterator<BluetoothDevice> it = devices.iterator();it.hasNext();){
                BluetoothDevice device = (BluetoothDevice)it.next();
                //打印出远程蓝牙设备的物理地址
                switch (flag) {
				case Constant.BLUETOOTH_FLAG_WEIGHT:
					getNames(device, btWeightNames);
					break;
				case Constant.BLUETOOTH_FLAG_BLOOD:
					getNames(device, btBloodNames);
					break;
				case Constant.BLUETOOTH_FLAG_BLOOD_SUGAR:
					getNames(device, btBloodSugarNames);
					break;

				default:
					break;
				}
            }
        }else{
            System.out.println("还没有已配对的远程蓝牙设备！");
        }*/
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}
		// 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
		mBluetoothAdapter.startDiscovery();

	}

	private void initBT() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null)
		{
			// 设备不支持蓝牙

		}
		// 打开蓝牙
		if (!mBluetoothAdapter.isEnabled())
		{
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// 设置蓝牙可见性，最多300秒
			intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			context.startActivity(intent);
		}
		// 注册用以接收到已搜索到的蓝牙设备的receiver
		IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		/*if(flag == Constant.BLUETOOTH_FLAG_WEIGHT){
			bluetoothReceive = new BluetoothReciever();
		}else */if(flag == Constant.BLUETOOTH_FLAG_BLOOD){

			bluetoothReceive = new BluetoothBloodReciever();
		}/*else if(flag == Constant.BLUETOOTH_FLAG_BLOOD_SUGAR){
			bluetoothReceive = new BluetoothBloodSugarReciever();

		}*/

		// 注册搜索完时的receiver
		mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		context.registerReceiver(bluetoothReceive, mFilter);

		context.registerReceiver(bluetoothReceive, intentFilter);

	}

	/*	private class BluetoothReciever extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String action = intent.getAction();
                // 获得已经搜索到的蓝牙设备
                if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                    BluetoothDevice device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // 搜索到的不是已经绑定的蓝牙设备
                    //if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        // 显示在TextView上
                        getNames(device,btWeightNames);

                        //btList.add(device);
                    //}
                    // 搜索完成
                } else if (action
                        .equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

                    if(names != null && names.size() != 0){
                        deleteStr(names);
                        System.out.println("搜索完成："+names.toString());
                        Message msg = new Message();
                        msg.what = 10001;
                        msg.obj = names;
                        han.sendMessage(msg);

                    }else{
                        System.out.println("没有匹配到蓝牙设备");
                    }
                    onDestroy();
                }
            }




        }*/
	private class BluetoothBloodReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			// 获得已经搜索到的蓝牙设备
			if (action.equals(BluetoothDevice.ACTION_FOUND)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 搜索到的不是已经绑定的蓝牙设备
				//if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
				getNames(device, btBloodNames);

				//}
				// 搜索完成
			} else if (action
					.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

				if(names != null && names.size() != 0){
					deleteStr(names);
					System.out.println("搜索完成："+names.toString());
					Message msg = new Message();
					msg.what = 10002;
					msg.obj = names;
					han.sendMessage(msg);

				}else{
					System.out.println("没有匹配到蓝牙设备");
				}
				onDestroy();
			}
		}


	}
/*	private class BluetoothBloodSugarReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			// 获得已经搜索到的蓝牙设备
			if (action.equals(BluetoothDevice.ACTION_FOUND)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 搜索到的不是已经绑定的蓝牙设备
				//if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

					getNames(device, btBloodSugarNames);

					//btList.add(device);
				//}
				// 搜索完成
			} else if (action
					.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

				if(names != null && names.size() != 0){
					deleteStr(names);
					System.out.println("搜索完成："+names.toString());
					Message msg = new Message();
					msg.what = 10003;
					msg.obj = names;
					han.sendMessage(msg);

				}else{
					System.out.println("没有匹配到蓝牙设备");
				}
				onDestroy();
			}
		}


	}*/


	public void onDestroy() {
		context.unregisterReceiver(bluetoothReceive);
	}


	//去掉重复字符串
	private List<String> deleteStr(List<String> list){
		List<String> listTemp= new ArrayList<String>();
		Iterator<String> it=list.iterator();
		while(it.hasNext()){
			String a=it.next();
			if(listTemp.contains(a)){
				it.remove();
			}else{
				listTemp.add(a);
			}
		}
		return listTemp;
	}

	public void getNames(BluetoothDevice device, String[] btWeightNames) {
		String name = device.getName();//"Yolanda"  "RBP1603010001" "Sinocare" "WIN-KLEE"  "YMETECH"
		if(name != null && name.length() !=0){
			Log.i("KKTAG", name);
			for (int i = 0; i < btWeightNames.length; i++) {
				Pattern pattern = Pattern.compile(btWeightNames[i]);
				Matcher matcher = pattern.matcher(name);
				if(matcher.find()){
					names.add(btWeightNames[i]);
				}
			}
		}
	}


}

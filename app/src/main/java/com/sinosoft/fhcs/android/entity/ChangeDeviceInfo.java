package com.sinosoft.fhcs.android.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 切换设备的list使用的实体类
 * @author doc
 *
 */
public class ChangeDeviceInfo implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public int isSelect;//是否是置为了默认 0 非默认，  1  默认
	public String deviceName;//名称
	public boolean isFisrt; //true 是首次使用 false 不是首次使用
	public Date lastTime;//上次使用时间
	public String connectId;//设备实例id
	public String deviceSn, deviceNo;//设备号和sn号
	public String function_info;//获取设备功能详情

	public ChangeDeviceInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getFunction_info() {
		return function_info;
	}

	public void setFunction_info(String function_info) {
		this.function_info = function_info;

	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getConnectId() {
		return connectId;
	}

	public void setConnectId(String connectId) {
		this.connectId = connectId;
	}

	public boolean isFisrt() {
		return isFisrt;
	}



	public void setFisrt(boolean isFisrt) {
		this.isFisrt = isFisrt;
	}



	public int getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(int isSelect) {
		this.isSelect = isSelect;
	}

	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	@Override
	public String toString() {
		return "ChangeDeviceInfo [isSelect=" + isSelect + ", deviceName="
				+ deviceName + ", isFisrt=" + isFisrt + ", lastTime="
				+ lastTime + "]";
	}





}

package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:绑定设备实体
 * @Author: wangshuangshuang.
 * @Create: 2015年1月7日.
 */
public class BindEquipment {
	private int id;
	private String imgUrl;
	private String deviceName;
	private String appCode; //01=咕咚手环 ，02=Jawbone UP24 ， 03=小米手环
	public BindEquipment() {
		super();
	}
	public BindEquipment(int id, String imgUrl, String deviceName,
						 String appCode) {
		super();
		this.id = id;
		this.imgUrl = imgUrl;
		this.deviceName = deviceName;
		this.appCode = appCode;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	@Override
	public String toString() {
		return "BindEquipment [id=" + id + ", imgUrl=" + imgUrl
				+ ", deviceName=" + deviceName + ", appCode=" + appCode + "]";
	}


}

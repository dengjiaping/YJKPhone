package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:设备列表实体
 * @Author: wangshuangshuang.
 * @Create: 2015年1月9日.
 */
public class EquipmentListInfo {
	private String deviceId;
	private String deviceName;
	private String familyMember_id;
	private String familyMemberRoleName;
	private String appCode;
	private String imgUrl;
	private String syncDataTime;
	private String accessToken;

	public EquipmentListInfo() {
		super();
	}

	public EquipmentListInfo(String deviceId, String deviceName,
							 String familyMember_id, String familyMemberRoleName,
							 String appCode, String imgUrl, String syncDataTime,
							 String accessToken) {
		super();
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.familyMember_id = familyMember_id;
		this.familyMemberRoleName = familyMemberRoleName;
		this.appCode = appCode;
		this.imgUrl = imgUrl;
		this.syncDataTime = syncDataTime;
		this.accessToken = accessToken;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getFamilyMember_id() {
		return familyMember_id;
	}

	public void setFamilyMember_id(String familyMember_id) {
		this.familyMember_id = familyMember_id;
	}

	public String getFamilyMemberRoleName() {
		return familyMemberRoleName;
	}

	public void setFamilyMemberRoleName(String familyMemberRoleName) {
		this.familyMemberRoleName = familyMemberRoleName;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSyncDataTime() {
		return syncDataTime;
	}

	public void setSyncDataTime(String syncDataTime) {
		this.syncDataTime = syncDataTime;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String toString() {
		return "EquipmentListInfo [deviceId=" + deviceId + ", deviceName="
				+ deviceName + ", familyMember_id=" + familyMember_id
				+ ", familyMemberRoleName=" + familyMemberRoleName
				+ ", appCode=" + appCode + ", imgUrl=" + imgUrl
				+ ", syncDataTime=" + syncDataTime + ", accessToken="
				+ accessToken + "]";
	}



}

package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description: 用户信息实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
public class UserInfo {
	private String userId;
	private String deviceNum;
	private String userName;
	private String sex;
	private String area;
	private String phone;
	private String email;
	private String imgUrl;
	private String sign;
	private String familyId;

	private String loginType;
	
/*	public UserInfo(String userId, String deviceNum, String userName,
			String sex, String area, String phone, String email, String imgUrl,String sign,String familyId) {
		super();
		this.userId = userId;
		this.deviceNum = deviceNum;
		this.userName = userName;
		this.sex = sex;
		this.area = area;
		this.phone = phone;
		this.email = email;
		this.imgUrl = imgUrl;
		this.sign=sign;
		this.familyId=familyId;
	}
	*/

	public UserInfo(String userId, String deviceNum, String userName,
					String sex, String area, String phone, String email, String imgUrl,
					String sign, String familyId, String loginType) {
		super();
		this.userId = userId;
		this.deviceNum = deviceNum;
		this.userName = userName;
		this.sex = sex;
		this.area = area;
		this.phone = phone;
		this.email = email;
		this.imgUrl = imgUrl;
		this.sign = sign;
		this.familyId = familyId;
		this.loginType = loginType;
	}


	public UserInfo() {
		super();
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getFamilyId() {
		return familyId;
	}
	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getLoginType() {
		return loginType;
	}


	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}


	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", deviceNum=" + deviceNum
				+ ", userName=" + userName + ", sex=" + sex + ", area=" + area
				+ ", phone=" + phone + ", email=" + email + ", imgUrl="
				+ imgUrl + ", sign=" + sign + ", familyId=" + familyId
				+ ", loginType=" + loginType + "]";
	}


}

package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description: 好友信息实体类
 * @Author: wangshuangshuang.
 * @Create: 2015年2月4日.
 */
public class FriendsPkDetailInfo {
	private String userId;//好友用户ID
	private String name;//好友昵称
	private String avatarPath;//好友头像地址
	private String gender;//好友性别  1-男  0-女
	private String provinceRegion;//省份
	private String cityRegion;//市
	private String areaRegion;//地区
	private String sign;//个性签名
	private String activityMetersMax;//好友的个人最好成绩
	private String activityMetersDiffer;//当前用户和当前好友的日均运动量的差
	private String isFriend;//是否为好友 0：不是好友 1：是好友
	private String distance;//昨天的数据
	private double near;//附近
	public FriendsPkDetailInfo() {
		super();
	}

	public FriendsPkDetailInfo(String userId, String name, String avatarPath,
							   String gender, String provinceRegion, String cityRegion,
							   String areaRegion, String sign, String activityMetersMax,
							   String activityMetersDiffer, String isFriend, String distance,
							   double near) {
		super();
		this.userId = userId;
		this.name = name;
		this.avatarPath = avatarPath;
		this.gender = gender;
		this.provinceRegion = provinceRegion;
		this.cityRegion = cityRegion;
		this.areaRegion = areaRegion;
		this.sign = sign;
		this.activityMetersMax = activityMetersMax;
		this.activityMetersDiffer = activityMetersDiffer;
		this.isFriend = isFriend;
		this.distance = distance;
		this.near = near;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatarPath() {
		return avatarPath;
	}
	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getProvinceRegion() {
		return provinceRegion;
	}
	public void setProvinceRegion(String provinceRegion) {
		this.provinceRegion = provinceRegion;
	}
	public String getCityRegion() {
		return cityRegion;
	}
	public void setCityRegion(String cityRegion) {
		this.cityRegion = cityRegion;
	}
	public String getAreaRegion() {
		return areaRegion;
	}
	public void setAreaRegion(String areaRegion) {
		this.areaRegion = areaRegion;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getActivityMetersMax() {
		return activityMetersMax;
	}
	public void setActivityMetersMax(String activityMetersMax) {
		this.activityMetersMax = activityMetersMax;
	}

	public String getActivityMetersDiffer() {
		return activityMetersDiffer;
	}

	public void setActivityMetersDiffer(String activityMetersDiffer) {
		this.activityMetersDiffer = activityMetersDiffer;
	}

	public double getNear() {
		return near;
	}

	public void setNear(double near) {
		this.near = near;
	}

	public String getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "FriendsPkDetailInfo [userId=" + userId + ", name=" + name
				+ ", avatarPath=" + avatarPath + ", gender=" + gender
				+ ", provinceRegion=" + provinceRegion + ", cityRegion="
				+ cityRegion + ", areaRegion=" + areaRegion + ", sign=" + sign
				+ ", activityMetersMax=" + activityMetersMax
				+ ", activityMetersDiffer=" + activityMetersDiffer
				+ ", isFriend=" + isFriend + ", distance=" + distance
				+ ", near=" + near + "]";
	}

}

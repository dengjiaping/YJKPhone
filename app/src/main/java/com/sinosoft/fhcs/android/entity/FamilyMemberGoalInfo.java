package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:配件-运动实体
 * @Author:pikai.
 * @Create: 2015年2月12日.
 */
import java.io.Serializable;

public class FamilyMemberGoalInfo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String roleName;//家庭成员名称
	private String deviceName;//设备名称
	private String deepSleepMinutes;//深睡眠时长
	private String totalSleepMinutes;//睡眠总时长
	private String lightSleepMinutes;//浅睡眠时长
	private String activityCalories;//运动卡路里
	private String activitySteps;//运动步数
	private String activityMinutes;//运动时长
	private String goalDeepMinutes;//目标睡眠时长
	private String goalSteps;//目标步数
	private String imgUrl;//图片地址


	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeepSleepMinutes() {
		return deepSleepMinutes;
	}
	public void setDeepSleepMinutes(String deepSleepMinutes) {
		this.deepSleepMinutes = deepSleepMinutes;
	}
	public String getTotalSleepMinutes() {
		return totalSleepMinutes;
	}
	public void setTotalSleepMinutes(String totalSleepMinutes) {
		this.totalSleepMinutes = totalSleepMinutes;
	}
	public String getLightSleepMinutes() {
		return lightSleepMinutes;
	}
	public void setLightSleepMinutes(String lightSleepMinutes) {
		this.lightSleepMinutes = lightSleepMinutes;
	}
	public String getActivityCalories() {
		return activityCalories;
	}
	public void setActivityCalories(String activityCalories) {
		this.activityCalories = activityCalories;
	}
	public String getActivitySteps() {
		return activitySteps;
	}
	public void setActivitySteps(String activitySteps) {
		this.activitySteps = activitySteps;
	}
	public String getActivityMinutes() {
		return activityMinutes;
	}
	public void setActivityMinutes(String activityMinutes) {
		this.activityMinutes = activityMinutes;
	}
	public String getGoalDeepMinutes() {
		return goalDeepMinutes;
	}
	public void setGoalDeepMinutes(String goalDeepMinutes) {
		this.goalDeepMinutes = goalDeepMinutes;
	}
	public String getGoalSteps() {
		return goalSteps;
	}
	public void setGoalSteps(String goalSteps) {
		this.goalSteps = goalSteps;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}



}

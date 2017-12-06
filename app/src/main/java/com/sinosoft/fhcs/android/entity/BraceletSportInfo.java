package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:运动实体
 * @Author: wangshuangshuang.
 * @Create: 2015年4月21日.
 */
public class BraceletSportInfo {
	private int goalStepNum;//目标步数
	private int stepNum;//步数
	private int calorie;//卡路里
	private int sportTime;//运动时长
	private int num;//超过多少位 如63%
	private int heartRate;//心率 如63次/分
	private double distance;//运动距离

	public BraceletSportInfo() {
		super();
	}

	public BraceletSportInfo(int goalStepNum, int stepNum, int calorie, int sportTime, int num, int heartRate, double distance) {
		this.goalStepNum = goalStepNum;
		this.stepNum = stepNum;
		this.calorie = calorie;
		this.sportTime = sportTime;
		this.num = num;
		this.heartRate = heartRate;
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public int getGoalStepNum() {
		return goalStepNum;
	}
	public void setGoalStepNum(int goalStepNum) {
		this.goalStepNum = goalStepNum;
	}
	public int getStepNum() {
		return stepNum;
	}
	public void setStepNum(int stepNum) {
		this.stepNum = stepNum;
	}
	public int getCalorie() {
		return calorie;
	}
	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
	public int getSportTime() {
		return sportTime;
	}
	public void setSportTime(int sportTime) {
		this.sportTime = sportTime;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "BraceletSportInfo{" +
				"goalStepNum=" + goalStepNum +
				", stepNum=" + stepNum +
				", calorie=" + calorie +
				", sportTime=" + sportTime +
				", num=" + num +
				", heartRate=" + heartRate +
				", distance=" + distance +
				'}';
	}

}

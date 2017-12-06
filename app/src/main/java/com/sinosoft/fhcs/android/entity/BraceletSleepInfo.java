package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:睡眠实体
 * @Author: wangshuangshuang.
 * @Create: 2015年4月21日.
 */

public class BraceletSleepInfo {
	private int goalTime;//目标
	private int deepTime;//深睡
	private int lightTime;//浅睡
	private int sleepTime;//睡眠
	private int num;//超过多少位 如63%
	private int heartRate;//心率 如67次/分

	public BraceletSleepInfo() {
		super();
	}

	public BraceletSleepInfo(int goalTime, int deepTime, int lightTime, int sleepTime, int num, int heartRate) {
		this.goalTime = goalTime;
		this.deepTime = deepTime;
		this.lightTime = lightTime;
		this.sleepTime = sleepTime;
		this.num = num;
		this.heartRate = heartRate;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public int getGoalTime() {
		return goalTime;
	}
	public void setGoalTime(int goalTime) {
		this.goalTime = goalTime;
	}
	public int getDeepTime() {
		return deepTime;
	}
	public void setDeepTime(int deepTime) {
		this.deepTime = deepTime;
	}
	public int getLightTime() {
		return lightTime;
	}
	public void setLightTime(int lightTime) {
		this.lightTime = lightTime;
	}
	public int getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "BraceletSleepInfo [goalTime=" + goalTime + ", deepTime="
				+ deepTime + ", lightTime=" + lightTime + ", sleepTime="
				+ sleepTime + ", num=" + num + "]";
	}



}

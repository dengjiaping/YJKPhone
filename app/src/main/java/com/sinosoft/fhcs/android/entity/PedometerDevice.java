package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:计步器实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import java.io.Serializable;

public class PedometerDevice implements Serializable {
	private static final long serialVersionUID = 1L;
	private int stepNum;// 步数
	private double distance;// 距离
	private double calorie;// 卡路里
	private String measureTime;

	public PedometerDevice(int stepNum, double distance, double calorie,
						   String measureTime) {
		super();
		this.stepNum = stepNum;
		this.distance = distance;
		this.calorie = calorie;
		this.measureTime = measureTime;
	}

	public PedometerDevice() {
		super();
	}

	public int getStepNum() {
		return stepNum;
	}

	public void setStepNum(int stepNum) {
		this.stepNum = stepNum;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getCalorie() {
		return calorie;
	}

	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}

	public String getMeasureTime() {
		return measureTime;
	}

	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "PedometerDevice [stepNum=" + stepNum + ", distance=" + distance
				+ ", calorie=" + calorie + ", measureTime=" + measureTime + "]";
	}

}

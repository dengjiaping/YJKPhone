package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:脂肪仪实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import java.io.Serializable;

public class FatMonitorDevice implements Serializable {
	private static final long serialVersionUID = -8662049764159744780L;
	private double fatPercentage; // 脂肪率
	private double visceralFatRating;// 内脏脂肪等级
	private double moistureRate;// 水分率
	private double muscleVolume;// 肌肉量
	private String measureTime;
	private double goalFatPercentage;

	public FatMonitorDevice(String measureTime, double fatPercentage,
							double visceralFatRating, double moistureRate, double muscleVolume,double goalFatPercentage) {

		this.fatPercentage = fatPercentage;
		this.visceralFatRating = visceralFatRating;
		this.moistureRate = moistureRate;
		this.muscleVolume = muscleVolume;
		this.measureTime = measureTime;
		this.goalFatPercentage=goalFatPercentage;
	}

	public FatMonitorDevice(double fatPercentage, double visceralFatRating,
							double moistureRate, double muscleVolume) {

		this.fatPercentage = fatPercentage;
		this.visceralFatRating = visceralFatRating;
		this.moistureRate = moistureRate;
		this.muscleVolume = muscleVolume;
	}

	public double getFatPercentage() {
		return fatPercentage;
	}

	public void setFatPercentage(double fatPercentage) {
		this.fatPercentage = fatPercentage;
	}

	public double getVisceralFatRating() {
		return visceralFatRating;
	}

	public void setVisceralFatRating(double visceralFatRating) {
		this.visceralFatRating = visceralFatRating;
	}

	public double getMoistureRate() {
		return moistureRate;
	}

	public void setMoistureRate(double moistureRate) {
		this.moistureRate = moistureRate;
	}

	public double getMuscleVolume() {
		return muscleVolume;
	}

	public double getGoalFatPercentage() {
		return goalFatPercentage;
	}

	public void setGoalFatPercentage(double goalFatPercentage) {
		this.goalFatPercentage = goalFatPercentage;
	}

	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}

	public void setMuscleVolume(double muscleVolume) {
		this.muscleVolume = muscleVolume;
	}

	public String getMeasureTime() {
		// TODO Auto-generated method stub
		return measureTime;
	}

}

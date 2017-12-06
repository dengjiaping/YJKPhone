package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:血糖仪实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import java.io.Serializable;

public class BloodGlucoseDevice implements Serializable {

	private static final long serialVersionUID = 7278190674630312194L;
	private double bloodGlucose;// 血糖值
	private String measureTime;
	private double goalBloodGlucose;
	public BloodGlucoseDevice(String measureTime, double bloodGlucose,double goalBloodGlucose) {
		this.measureTime = measureTime;
		this.bloodGlucose = bloodGlucose;
		this.goalBloodGlucose=goalBloodGlucose;
	}

	public BloodGlucoseDevice(double bloodGlucose) {
		super();
		this.bloodGlucose = bloodGlucose;
	}

	public double getBloodGlucose() {
		return bloodGlucose;
	}

	public void setBloodGlucose(double bloodGlucose) {
		this.bloodGlucose = bloodGlucose;
	}

	public String getMeasureTime() {
		// TODO Auto-generated method stub
		return measureTime;
	}

	public double getGoalBloodGlucose() {
		return goalBloodGlucose;
	}

	public void setGoalBloodGlucose(double goalBloodGlucose) {
		this.goalBloodGlucose = goalBloodGlucose;
	}

	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}

}

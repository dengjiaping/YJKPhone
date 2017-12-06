package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:血压计实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */

import java.io.Serializable;

public class BloodPressureDevice implements Serializable {
	private static final long serialVersionUID = 7259554020663884047L;
	private String measureTime;
	private double pulse;// 脉搏
	private double diastolicPressure;// 舒张压（低压）
	private double systolicPressure;// 收缩压（高压）
	private double goalSystolicPressure;

	public BloodPressureDevice(String measureTime, double pulse,
							   double diastolicPressure, double systolicPressure,double goalSystolicPressure) {
		this.measureTime = measureTime;
		this.pulse = pulse;
		this.diastolicPressure = diastolicPressure;
		this.systolicPressure = systolicPressure;
		this.goalSystolicPressure=goalSystolicPressure;
	}

	public BloodPressureDevice(double pulse, double diastolicPressure,
							   double systolicPressure) {
		super();
		this.pulse = pulse;
		this.diastolicPressure = diastolicPressure;
		this.systolicPressure = systolicPressure;
	}

	public double getPulse() {
		return pulse;
	}

	public void setPulse(double pulse) {
		this.pulse = pulse;
	}

	public double getDiastolicPressure() {
		return diastolicPressure;
	}

	public void setDiastolicPressure(double diastolicPressure) {
		this.diastolicPressure = diastolicPressure;
	}

	public double getSystolicPressure() {
		return systolicPressure;
	}

	public void setSystolicPressure(double systolicPressure) {
		this.systolicPressure = systolicPressure;
	}

	public String getMeasureTime() {
		// TODO Auto-generated method stub
		return measureTime;
	}

	public double getGoalSystolicPressure() {
		return goalSystolicPressure;
	}

	public void setGoalSystolicPressure(double goalSystolicPressure) {
		this.goalSystolicPressure = goalSystolicPressure;
	}

	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}

}

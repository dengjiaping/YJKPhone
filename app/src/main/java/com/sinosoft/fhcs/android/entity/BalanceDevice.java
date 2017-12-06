package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:健康秤实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import java.io.Serializable;

public class BalanceDevice implements Serializable {

	private static final long serialVersionUID = 1L;
	private double weight;// 体重
	private double goalWeight;//目标体重
	private String measureTime;

	public BalanceDevice(double weight, double goalWeight, String measureTime) {
		super();
		this.weight = weight;
		this.goalWeight = goalWeight;
		this.measureTime = measureTime;
	}

	public BalanceDevice(double weight) {
		super();
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getMeasureTime() {
		// TODO Auto-generated method stub
		return measureTime;
	}

	public double getGoalWeight() {
		return goalWeight;
	}

	public void setGoalWeight(double goalWeight) {
		this.goalWeight = goalWeight;
	}

	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}

}

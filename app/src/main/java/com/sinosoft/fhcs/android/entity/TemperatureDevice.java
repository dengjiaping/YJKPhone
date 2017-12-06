package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description: 耳温枪实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import java.io.Serializable;

public class TemperatureDevice implements Serializable {
	private static final long serialVersionUID = -3375987369836839696L;
	private double temperature;// 体温
	private String measureTime;

	public TemperatureDevice(String measureTime, double temperature) {
		this.temperature = temperature;
		this.measureTime = measureTime;

	}

	public TemperatureDevice(double temperature) {
		super();
		this.temperature = temperature;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public String getMeasureTime() {
		// TODO Auto-generated method stub
		return measureTime;
	}

}

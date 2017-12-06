package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:机器测量实体
 * @Author: wangshuangshuang.
 * @Create: 2015年4月21日.
 */
public class ShebeiMeaInfo {
	private String measureTime;
	private double weight;//体重//测量时间
	private double pulse;// 脉搏
	private double diastolicPressure;// 舒张压（低压）
	private double systolicPressure;// 收缩压（高压）
	private double bloodGlucose;// 血糖值
	private double fatPercentage; // 脂肪率
	private double visceralFatRating;// 内脏脂肪等级
	private double moistureRate;// 水分率
	private double muscleVolume;// 肌肉量
	private double temperature;// 体温
	private double  NBMI;
	public ShebeiMeaInfo(String measureTime, double weight, double pulse,
						 double diastolicPressure, double systolicPressure,
						 double bloodGlucose, double fatPercentage,
						 double visceralFatRating, double moistureRate, double muscleVolume,
						 double temperature) {
		super();
		this.measureTime = measureTime;
		this.weight = weight;
		this.pulse = pulse;
		this.diastolicPressure = diastolicPressure;
		this.systolicPressure = systolicPressure;
		this.bloodGlucose = bloodGlucose;
		this.fatPercentage = fatPercentage;
		this.visceralFatRating = visceralFatRating;
		this.moistureRate = moistureRate;
		this.muscleVolume = muscleVolume;
		this.temperature = temperature;
	}

	public double getNBMI() {
		return NBMI;
	}

	public void setNBMI(double NBMI) {
		this.NBMI = NBMI;
	}

	public String getMeasureTime() {
		return measureTime;
	}
	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
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
	public double getBloodGlucose() {
		return bloodGlucose;
	}
	public void setBloodGlucose(double bloodGlucose) {
		this.bloodGlucose = bloodGlucose;
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
	public void setMuscleVolume(double muscleVolume) {
		this.muscleVolume = muscleVolume;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	@Override
	public String toString() {
		return "ShebeiMeaInfo [measureTime=" + measureTime + ", weight="
				+ weight + ", pulse=" + pulse + ", diastolicPressure="
				+ diastolicPressure + ", systolicPressure=" + systolicPressure
				+ ", bloodGlucose=" + bloodGlucose + ", fatPercentage="
				+ fatPercentage + ", visceralFatRating=" + visceralFatRating
				+ ", moistureRate=" + moistureRate + ", muscleVolume="
				+ muscleVolume + ", temperature=" + temperature + "]";
	}


}

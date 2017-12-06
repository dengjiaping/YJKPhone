package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:最近测量健康数据实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
public class HealthRecord {
	private double weight;
	private double bloodGlucose;// 血糖值
	private double pulse;// 脉搏
	private double diastolicPressure;// 舒张压（低压）
	private double systolicPressure;// 收缩压（高压）
	private double fatPercentage; // 脂肪率
	private double visceralFatRating;// 内脏脂肪等级
	private double moistureRate;// 水分率
	private double muscleVolume;// 肌肉量
	private int stepNum;// 步数
	private double distance;// 距离
	private double calorie;// 卡路里
	private double temperature;// 体温
	private String dateJKC;
	private String dateXYJ;
	private String dateXTY;
	private String dateJBQ;
	private String dateEWQ;
	private String dateZFY;

	public HealthRecord() {
		super();
	}

	public HealthRecord(double weight, double bloodGlucose, double pulse,
						double diastolicPressure, double systolicPressure,
						double fatPercentage, double visceralFatRating,
						double moistureRate, double muscleVolume, int stepNum,
						double distance, double calorie, double temperature,
						String dateJKC, String dateXYJ, String dateXTY, String dateJBQ,
						String dateEWQ, String dateZFY) {
		super();
		this.weight = weight;
		this.bloodGlucose = bloodGlucose;
		this.pulse = pulse;
		this.diastolicPressure = diastolicPressure;
		this.systolicPressure = systolicPressure;
		this.fatPercentage = fatPercentage;
		this.visceralFatRating = visceralFatRating;
		this.moistureRate = moistureRate;
		this.muscleVolume = muscleVolume;
		this.stepNum = stepNum;
		this.distance = distance;
		this.calorie = calorie;
		this.temperature = temperature;
		this.dateJKC = dateJKC;
		this.dateXYJ = dateXYJ;
		this.dateXTY = dateXTY;
		this.dateJBQ = dateJBQ;
		this.dateEWQ = dateEWQ;
		this.dateZFY = dateZFY;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getBloodGlucose() {
		return bloodGlucose;
	}

	public void setBloodGlucose(double bloodGlucose) {
		this.bloodGlucose = bloodGlucose;
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

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public String getDateJKC() {
		return dateJKC;
	}

	public void setDateJKC(String dateJKC) {
		this.dateJKC = dateJKC;
	}

	public String getDateXYJ() {
		return dateXYJ;
	}

	public void setDateXYJ(String dateXYJ) {
		this.dateXYJ = dateXYJ;
	}

	public String getDateXTY() {
		return dateXTY;
	}

	public void setDateXTY(String dateXTY) {
		this.dateXTY = dateXTY;
	}

	public String getDateJBQ() {
		return dateJBQ;
	}

	public void setDateJBQ(String dateJBQ) {
		this.dateJBQ = dateJBQ;
	}

	public String getDateEWQ() {
		return dateEWQ;
	}

	public void setDateEWQ(String dateEWQ) {
		this.dateEWQ = dateEWQ;
	}

	public String getDateZFY() {
		return dateZFY;
	}

	public void setDateZFY(String dateZFY) {
		this.dateZFY = dateZFY;
	}

	@Override
	public String toString() {
		return "HealthRecord [weight=" + weight + ", bloodGlucose="
				+ bloodGlucose + ", pulse=" + pulse + ", diastolicPressure="
				+ diastolicPressure + ", systolicPressure=" + systolicPressure
				+ ", fatPercentage=" + fatPercentage + ", visceralFatRating="
				+ visceralFatRating + ", moistureRate=" + moistureRate
				+ ", muscleVolume=" + muscleVolume + ", stepNum=" + stepNum
				+ ", distance=" + distance + ", calorie=" + calorie
				+ ", temperature=" + temperature + ", dateJKC=" + dateJKC
				+ ", dateXYJ=" + dateXYJ + ", dateXTY=" + dateXTY
				+ ", dateJBQ=" + dateJBQ + ", dateEWQ=" + dateEWQ
				+ ", dateZFY=" + dateZFY + "]";
	}

}

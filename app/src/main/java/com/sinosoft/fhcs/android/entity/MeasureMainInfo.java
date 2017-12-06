package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:测量首页实体
 * @Author: wangshuangshuang.
 * @Create: 2015年2月2日.
 */
public class MeasureMainInfo {
	private String memberId;
	private String gender;
	private String nickName;
	private String weight;
	private String sleep;
	private String stepNum;
	private String temperature;
	private double diya;
	private double gaoya;
	private double bloodGlucose;
	private String tizhi;
	private double height;
	private double memberWeight;
	private String deviceName;
	private String accessToken;
	private String appCode;
	private int age;
	private String birth;
	public MeasureMainInfo() {
		super();
	}

	public MeasureMainInfo(String memberId, String gender, String nickName,
						   String weight, String sleep, String stepNum, String temperature,
						   double diya, double gaoya, double bloodGlucose, String tizhi,
						   double height, double memberWeight, String deviceName,
						   String accessToken,String appCode,int age,String birth) {
		super();
		this.memberId = memberId;
		this.gender = gender;
		this.nickName = nickName;
		this.weight = weight;
		this.sleep = sleep;
		this.stepNum = stepNum;
		this.temperature = temperature;
		this.diya = diya;
		this.gaoya = gaoya;
		this.bloodGlucose = bloodGlucose;
		this.tizhi = tizhi;
		this.height = height;
		this.memberWeight = memberWeight;
		this.deviceName = deviceName;
		this.accessToken = accessToken;
		this.appCode=appCode;
		this.age=age;
		this.birth=birth;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setSleep(String sleep) {
		this.sleep = sleep;
	}

	public String getWeight() {
		return weight;
	}

	public String getSleep() {
		return sleep;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getStepNum() {
		return stepNum;
	}

	public void setStepNum(String stepNum) {
		this.stepNum = stepNum;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemprature(String temperature) {
		this.temperature = temperature;
	}

	public double getDiya() {
		return diya;
	}

	public void setDiya(double diya) {
		this.diya = diya;
	}

	public double getGaoya() {
		return gaoya;
	}

	public void setGaoya(double gaoya) {
		this.gaoya = gaoya;
	}

	public double getBloodGlucose() {
		return bloodGlucose;
	}

	public void setBloodGlucose(double bloodGlucose) {
		this.bloodGlucose = bloodGlucose;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getTizhi() {
		return tizhi;
	}

	public void setTizhi(String tizhi) {
		this.tizhi = tizhi;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getMemberWeight() {
		return memberWeight;
	}

	public void setMemberWeight(double memberWeight) {
		this.memberWeight = memberWeight;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	@Override
	public String toString() {
		return "MeasureMainInfo [memberId=" + memberId + ", gender=" + gender
				+ ", nickName=" + nickName + ", weight=" + weight + ", sleep="
				+ sleep + ", stepNum=" + stepNum + ", temperature="
				+ temperature + ", diya=" + diya + ", gaoya=" + gaoya
				+ ", bloodGlucose=" + bloodGlucose + ", tizhi=" + tizhi
				+ ", height=" + height + ", memberWeight=" + memberWeight
				+ ", deviceName=" + deviceName + ", accessToken=" + accessToken
				+ ", appCode=" + appCode + ", age=" + age + ", birth=" + birth
				+ "]";
	}





}

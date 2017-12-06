package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:服药提醒实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import java.io.Serializable;

public class RemindListInfo implements Serializable {
	private static final long serialVersionUID = -7570928425278298334L;
	private String id;
	private String startTime;// 服药提醒开始时间
	private String endTime;// 服药提醒结束时间
	private Integer reminderPeriod;// 提醒周期,多少天,多少周
	private int reminderPeriodType;// 提醒类型按天还是按周
	private String reminderTime;// 提醒时间 1~24小时
	private String fmId;//角色Id
	private String fmName;// 角色名
	private String gender;// 性别
	private String medicineName;// 药品名称
	private String dosage;// 剂量
	private String reminderWay;// 提醒方式//手机客户端60001101//手机短信60001102
	private String phoneNumber;// 手机号
	private String reminderByMeal;// 提醒方式 -->餐前/餐后/餐中
	private boolean isNew;
	private String pageCount;
	private boolean expired;// true-过期,false-没过期

	public RemindListInfo(String id, String startTime, String endTime,
						  Integer reminderPeriod, int reminderPeriodType,
						  String reminderTime, String fmId, String fmName, String gender,
						  String medicineName, String dosage, String reminderWay,
						  String phoneNumber, String reminderByMeal, boolean isNew,
						  String pageCount, boolean expired) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.reminderPeriod = reminderPeriod;
		this.reminderPeriodType = reminderPeriodType;
		this.reminderTime = reminderTime;
		this.fmId = fmId;
		this.fmName = fmName;
		this.gender = gender;
		this.medicineName = medicineName;
		this.dosage = dosage;
		this.reminderWay = reminderWay;
		this.phoneNumber = phoneNumber;
		this.reminderByMeal = reminderByMeal;
		this.isNew = isNew;
		this.pageCount = pageCount;
		this.expired = expired;
	}

	public RemindListInfo(String id, String startTime, String endTime,
						  String reminderTime, String fmId, String fmName, String gender,
						  String medicineName, String dosage, String reminderWay,
						  String phoneNumber, String reminderByMeal, String pageCount,boolean expired) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.reminderTime = reminderTime;
		this.fmId = fmId;
		this.fmName = fmName;
		this.gender = gender;
		this.medicineName = medicineName;
		this.dosage = dosage;
		this.reminderWay = reminderWay;
		this.phoneNumber = phoneNumber;
		this.reminderByMeal = reminderByMeal;
		this.pageCount = pageCount;
		this.expired = expired;
	}

	public RemindListInfo(String id, String startTime, String endTime,
						  String reminderTime, String fmId,String fmName, String gender,
						  String medicineName, String dosage, String reminderWay,
						  String phoneNumber, String reminderByMeal, boolean isNew,boolean expired) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.reminderTime = reminderTime;
		this.fmId=fmId;
		this.fmName = fmName;
		this.gender = gender;
		this.medicineName = medicineName;
		this.dosage = dosage;
		this.reminderWay = reminderWay;
		this.phoneNumber = phoneNumber;
		this.reminderByMeal = reminderByMeal;
		this.isNew = isNew;
		this.expired = expired;
	}

	public RemindListInfo() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getReminderPeriod() {
		return reminderPeriod;
	}

	public void setReminderPeriod(Integer reminderPeriod) {
		this.reminderPeriod = reminderPeriod;
	}

	public int getReminderPeriodType() {
		return reminderPeriodType;
	}

	public void setReminderPeriodType(int reminderPeriodType) {
		this.reminderPeriodType = reminderPeriodType;
	}

	public String getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(String reminderTime) {
		this.reminderTime = reminderTime;
	}

	public String getFmId() {
		return fmId;
	}

	public void setFmId(String fmId) {
		this.fmId = fmId;
	}

	public String getFmName() {
		return fmName;
	}

	public void setFmName(String fmName) {
		this.fmName = fmName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getReminderWay() {
		return reminderWay;
	}

	public void setReminderWay(String reminderWay) {
		this.reminderWay = reminderWay;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getReminderByMeal() {
		return reminderByMeal;
	}

	public void setReminderByMeal(String reminderByMeal) {
		this.reminderByMeal = reminderByMeal;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	@Override
	public String toString() {
		return "RemindListInfo [id=" + id + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", reminderPeriod=" + reminderPeriod
				+ ", reminderPeriodType=" + reminderPeriodType
				+ ", reminderTime=" + reminderTime + ", fmId=" + fmId
				+ ", fmName=" + fmName + ", gender=" + gender
				+ ", medicineName=" + medicineName + ", dosage=" + dosage
				+ ", reminderWay=" + reminderWay + ", phoneNumber="
				+ phoneNumber + ", reminderByMeal=" + reminderByMeal
				+ ", isNew=" + isNew + ", pageCount=" + pageCount
				+ ", expired=" + expired + "]";
	}
}

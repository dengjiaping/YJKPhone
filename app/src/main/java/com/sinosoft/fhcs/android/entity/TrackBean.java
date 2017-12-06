package com.sinosoft.fhcs.android.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TrackBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	String motionTrailId;
	String start_time;
	String end_time;
	String burnCalories;
	String motionMinutes;
	String km;
	String motionType;
	public String getMotionTrailId() {
		return motionTrailId;
	}
	public void setMotionTrailId(String motionTrailId) {
		this.motionTrailId = motionTrailId;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getBurnCalories() {
		return burnCalories;
	}
	public void setBurnCalories(String burnCalories) {
		this.burnCalories = burnCalories;
	}
	public String getMotionMinutes() {
		return motionMinutes;
	}
	public void setMotionMinutes(String motionMinutes) {
		this.motionMinutes = motionMinutes;
	}
	public String getKm() {
		return km;
	}
	public void setKm(String km) {
		this.km = km;
	}
	public String getMotionType() {
		return motionType;
	}
	public void setMotionType(String motionType) {
		this.motionType = motionType;
	}
	
	
}

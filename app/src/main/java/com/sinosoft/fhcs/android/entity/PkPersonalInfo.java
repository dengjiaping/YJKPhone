package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:好友比拼-个人信息实体
 * @Author: wangshuangshuang.
 * @Create: 2015年1月27日.
 */
public class PkPersonalInfo {
	private String id;
	private String imgAvatar;
	private String address;
	private String near;
	private String signature;
	private String distanceYesterday;
	private String diatanceBest;
	private String diatanceAdd;
	public PkPersonalInfo(String id, String imgAvatar, String address,
						  String near, String signature, String distanceYesterday,
						  String diatanceBest, String diatanceAdd) {
		super();
		this.id = id;
		this.imgAvatar = imgAvatar;
		this.address = address;
		this.near = near;
		this.signature = signature;
		this.distanceYesterday = distanceYesterday;
		this.diatanceBest = diatanceBest;
		this.diatanceAdd = diatanceAdd;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImgAvatar() {
		return imgAvatar;
	}
	public void setImgAvatar(String imgAvatar) {
		this.imgAvatar = imgAvatar;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNear() {
		return near;
	}
	public void setNear(String near) {
		this.near = near;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getDistanceYesterday() {
		return distanceYesterday;
	}
	public void setDistanceYesterday(String distanceYesterday) {
		this.distanceYesterday = distanceYesterday;
	}
	public String getDiatanceBest() {
		return diatanceBest;
	}
	public void setDiatanceBest(String diatanceBest) {
		this.diatanceBest = diatanceBest;
	}
	public String getDiatanceAdd() {
		return diatanceAdd;
	}
	public void setDiatanceAdd(String diatanceAdd) {
		this.diatanceAdd = diatanceAdd;
	}
	@Override
	public String toString() {
		return "PkPersonalInfo [id=" + id + ", imgAvatar=" + imgAvatar
				+ ", address=" + address + ", near=" + near + ", signature="
				+ signature + ", distanceYesterday=" + distanceYesterday
				+ ", diatanceBest=" + diatanceBest + ", diatanceAdd="
				+ diatanceAdd + "]";
	}

}

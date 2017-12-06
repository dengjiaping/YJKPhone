package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description: 好友比拼实体类
 * @Author: wangshuangshuang.
 * @Create: 2015年1月27日.
 */
public class FriendsPKInfo {
	private String id;
	private String imgUrl;
	private String nickName;
	private String distance;//运动距离
	private double near;//离我距离
	private String gender;
	public FriendsPKInfo(String id, String imgUrl, String nickName,
						 String distance, double near, String gender) {
		super();
		this.id = id;
		this.imgUrl = imgUrl;
		this.nickName = nickName;
		this.distance = distance;
		this.near = near;
		this.gender = gender;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}

	public double getNear() {
		return near;
	}
	public void setNear(double near) {
		this.near = near;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Override
	public String toString() {
		return "FriendsPKInfo [id=" + id + ", imgUrl=" + imgUrl + ", nickName="
				+ nickName + ", distance=" + distance + ", near=" + near
				+ ", gender=" + gender + "]";
	}

}

package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:资讯厂商实体
 * @Author: wangshuangshuang.
 * @Create: 2015年2月13日.
 */
public class InformationThird {
	private String id;
	private int notReadCount;
	private String minIcon;
	private String facilitatorName;
	private String cooperateIdentify;
	private boolean isBuy;

	public InformationThird(String id, int notReadCount, String minIcon,
							String facilitatorName, String cooperateIdentify, boolean isBuy) {
		super();
		this.id = id;
		this.notReadCount = notReadCount;
		this.minIcon = minIcon;
		this.facilitatorName = facilitatorName;
		this.cooperateIdentify = cooperateIdentify;
		this.isBuy = isBuy;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getNotReadCount() {
		return notReadCount;
	}
	public void setNotReadCount(int notReadCount) {
		this.notReadCount = notReadCount;
	}
	public String getMinIcon() {
		return minIcon;
	}
	public void setMinIcon(String minIcon) {
		this.minIcon = minIcon;
	}
	public String getFacilitatorName() {
		return facilitatorName;
	}
	public void setFacilitatorName(String facilitatorName) {
		this.facilitatorName = facilitatorName;
	}
	public String getCooperateIdentify() {
		return cooperateIdentify;
	}
	public void setCooperateIdentify(String cooperateIdentify) {
		this.cooperateIdentify = cooperateIdentify;
	}

	public boolean isBuy() {
		return isBuy;
	}
	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}
	@Override
	public String toString() {
		return "InformationThird [id=" + id + ", notReadCount=" + notReadCount
				+ ", minIcon=" + minIcon + ", facilitatorName="
				+ facilitatorName + ", cooperateIdentify=" + cooperateIdentify
				+ ", isBuy=" + isBuy + "]";
	}


}
 
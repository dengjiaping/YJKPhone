package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:竞赛列表实体
 * @Author: wangshuangshuang.
 * @Create: 2015年1月19日.
 */
public class CompetitionListInfo {
	private String id;
	private String session;//场次
	private String countdown;//倒计时 (正在进行中为1,已经结束为2,尚未开始也没结束的计算倒计时，格式为 0h27')
	private String peopleNum;//人数
	private String peopleNowNum;//现在人数
	private String model;//模型      多人 01  两人 02
	private String type;//类型     跑步  01   骑行  02
	public CompetitionListInfo(String id, String session, String countdown,
							   String peopleNum, String peopleNowNum, String model, String type) {
		super();
		this.id = id;
		this.session = session;
		this.countdown = countdown;
		this.peopleNum = peopleNum;
		this.peopleNowNum = peopleNowNum;
		this.model = model;
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public String getCountdown() {
		return countdown;
	}
	public void setCountdown(String countdown) {
		this.countdown = countdown;
	}
	public String getPeopleNum() {
		return peopleNum;
	}
	public void setPeopleNum(String peopleNum) {
		this.peopleNum = peopleNum;
	}
	public String getPeopleNowNum() {
		return peopleNowNum;
	}
	public void setPeopleNowNum(String peopleNowNum) {
		this.peopleNowNum = peopleNowNum;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "CompetitionListInfo [id=" + id + ", session=" + session
				+ ", countdown=" + countdown + ", peopleNum=" + peopleNum
				+ ", peopleNowNum=" + peopleNowNum + ", model=" + model
				+ ", type=" + type + "]";
	}


}

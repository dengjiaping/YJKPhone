package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:竞赛详情实体
 * @Author: wangshuangshuang.
 * @Create: 2015年1月21日.
 */
public class CompetitionDetail {
	private String id;//id
	private String session;//场次
	private String model;//模式  多人 01  两人 02
	private String type;//类型  跑步  01   骑行  02
	private String content;//宣言
	private String countdown;//倒计时
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String nowNumber;//加入人数
	private String number;//总人数
	private String commentNumber;//评论数
	private String isJoin;//1==已经加入，0==没有加入
	public CompetitionDetail(String id, String session, String model,
							 String type, String content, String countdown, String startTime,
							 String endTime, String nowNumber, String number,
							 String commentNumber, String isJoin) {
		super();
		this.id = id;
		this.session = session;
		this.model = model;
		this.type = type;
		this.content = content;
		this.countdown = countdown;
		this.startTime = startTime;
		this.endTime = endTime;
		this.nowNumber = nowNumber;
		this.number = number;
		this.commentNumber = commentNumber;
		this.isJoin = isJoin;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCountdown() {
		return countdown;
	}
	public void setCountdown(String countdown) {
		this.countdown = countdown;
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
	public String getNowNumber() {
		return nowNumber;
	}
	public void setNowNumber(String nowNumber) {
		this.nowNumber = nowNumber;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(String commentNumber) {
		this.commentNumber = commentNumber;
	}
	public String getIsJoin() {
		return isJoin;
	}
	public void setIsJoin(String isJoin) {
		this.isJoin = isJoin;
	}
	@Override
	public String toString() {
		return "CompetitionDetail [id=" + id + ", session=" + session
				+ ", model=" + model + ", type=" + type + ", content="
				+ content + ", countdown=" + countdown + ", startTime="
				+ startTime + ", endTime=" + endTime + ", nowNumber="
				+ nowNumber + ", number=" + number + ", commentNumber="
				+ commentNumber + ", isJoin=" + isJoin + "]";
	}


}

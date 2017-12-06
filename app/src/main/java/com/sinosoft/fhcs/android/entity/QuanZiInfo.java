package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:圈子消息实体
 * @Author: wangshuangshuang.
 * @Create: 2015年4月3日.
 */
public class QuanZiInfo {
	private String id;
	private String content;
	private String friendUserId;// 好友的用户ID
	private String friendName;// 好友昵称
	private String avatarPath;// 好友头像地址
	private String messageType;// 消息类型：留言消息-01；加好友消息-02；邀请竞赛消息-03
	private String messageTime;// 消息时间
	private boolean isRead;// 是否已读，用于留言的消息，true：已读 false ：未读
	// 当messageType为“02”时有以下参数
	private double near;// 当前用户和好友的距离，用于添加好友的消息，带到好友详情页面
	private String distance;// 好友前天的运动量，用于添加好友消息，带到好友详情页面
	// 当messageType为“03”时有以下参数
	private String raceId;// 竞赛id
	private String changCi;//场次


	public QuanZiInfo(String id, String content, String friendUserId,
					  String friendName, String avatarPath, String messageType,
					  String messageTime, boolean isRead, double near, String distance,
					  String raceId, String changCi) {
		super();
		this.id = id;
		this.content = content;
		this.friendUserId = friendUserId;
		this.friendName = friendName;
		this.avatarPath = avatarPath;
		this.messageType = messageType;
		this.messageTime = messageTime;
		this.isRead = isRead;
		this.near = near;
		this.distance = distance;
		this.raceId = raceId;
		this.changCi = changCi;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFriendUserId() {
		return friendUserId;
	}
	public void setFriendUserId(String friendUserId) {
		this.friendUserId = friendUserId;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getAvatarPath() {
		return avatarPath;
	}
	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public double getNear() {
		return near;
	}
	public void setNear(double near) {
		this.near = near;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getRaceId() {
		return raceId;
	}
	public void setRaceId(String raceId) {
		this.raceId = raceId;
	}

	public String getChangCi() {
		return changCi;
	}
	public void setChangCi(String changCi) {
		this.changCi = changCi;
	}
	@Override
	public String toString() {
		return "QuanZiInfo [id=" + id + ", content=" + content
				+ ", friendUserId=" + friendUserId + ", friendName="
				+ friendName + ", avatarPath=" + avatarPath + ", messageType="
				+ messageType + ", messageTime=" + messageTime + ", isRead="
				+ isRead + ", near=" + near + ", distance=" + distance
				+ ", raceId=" + raceId + ", changCi=" + changCi + "]";
	}

}

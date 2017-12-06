package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:留言列表实体
 * @Author: wangshuangshuang.
 * @Create: 2015年2月6日.
 */
public class LeaveMessageInfo {
	private String id;//消息ID
	private String name;//昵称
	private String message;//消息内容
	private String time;//时间
	private String avatarPath;//头像地址
	public LeaveMessageInfo(String id, String name, String message,
							String time, String avatarPath) {
		super();
		this.id = id;
		this.name = name;
		this.message = message;
		this.time = time;
		this.avatarPath = avatarPath;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAvatarPath() {
		return avatarPath;
	}
	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
	@Override
	public String toString() {
		return "LeaveMessageInfo [id=" + id + ", name=" + name + ", message="
				+ message + ", time=" + time + ", avatarPath=" + avatarPath
				+ "]";
	}

}

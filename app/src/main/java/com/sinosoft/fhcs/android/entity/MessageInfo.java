package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:系统消息实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
public class MessageInfo {
	private boolean isRead;
	private String id;
	private String date;
	private String content;
	public MessageInfo(boolean isRead, String id, String date, String content) {
		super();
		this.isRead = isRead;
		this.id = id;
		this.date = date;
		this.content = content;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "MessageInfo [isRead=" + isRead + ", id=" + id + ", date="
				+ date + ", content=" + content + "]";
	}

}

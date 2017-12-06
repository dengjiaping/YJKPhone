package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:评论实体
 * @Author: wangshuangshuang.
 * @Create: 2015年1月22日.
 */
public class CommentInfo {
	private String id;
	private String url;
	private String name;
	private String date;
	private String content;
	public CommentInfo(String id, String url, String name, String date,
					   String content) {
		super();
		this.id = id;
		this.url = url;
		this.name = name;
		this.date = date;
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return "CommentInfo [id=" + id + ", url=" + url + ", name=" + name
				+ ", date=" + date + ", content=" + content + "]";
	}

}

package com.sinosoft.fhcs.android.entity;

public class OrderServiceInfo {
	private String id;
	private String type;//xk  tgj lx
	private String title;
	private String content;
	private String price;
	private boolean isOrder;
	private boolean isTuiGuang;
	private String date;
	private String url;
	public OrderServiceInfo(String id, String type, String title,
			String content, String price, boolean isOrder, boolean isTuiGuang,
			String date, String url) {
		super();
		this.id = id;
		this.type = type;
		this.title = title;
		this.content = content;
		this.price = price;
		this.isOrder = isOrder;
		this.isTuiGuang = isTuiGuang;
		this.date = date;
		this.url = url;
	}
	public OrderServiceInfo() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public boolean isOrder() {
		return isOrder;
	}
	public void setOrder(boolean isOrder) {
		this.isOrder = isOrder;
	}
	public boolean isTuiGuang() {
		return isTuiGuang;
	}
	public void setTuiGuang(boolean isTuiGuang) {
		this.isTuiGuang = isTuiGuang;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "OrderServiceInfo [id=" + id + ", type=" + type + ", title="
				+ title + ", content=" + content + ", price=" + price
				+ ", isOrder=" + isOrder + ", isTuiGuang=" + isTuiGuang
				+ ", date=" + date + ", url=" + url + "]";
	}
	
	
}

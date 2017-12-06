package com.sinosoft.fhcs.android.entity;

import java.util.List;

/**
 * @CopyRight: SinoSoft.
 * @Description:资讯列表/一级实体（类别）
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
public class InformationGroup {
	private String s_id;
	private String s_title;
	private int unreadCount;
	private List<InformationChild> list_child;
	private boolean thirdPartyOrder;//是否订购
	public InformationGroup(String s_id, String s_title, int unreadCount,
							List<InformationChild> list_child,boolean thirdPartyOrder) {
		super();
		this.s_id = s_id;
		this.s_title = s_title;
		this.unreadCount = unreadCount;
		this.list_child = list_child;
		this.thirdPartyOrder=thirdPartyOrder;
	}

	public InformationGroup(String s_id, String s_title, int unreadCount,boolean thirdPartyOrder) {
		super();
		this.s_id = s_id;
		this.s_title = s_title;
		this.unreadCount = unreadCount;
		this.thirdPartyOrder=thirdPartyOrder;
	}

	public String getS_id() {
		return s_id;
	}

	public void setS_id(String s_id) {
		this.s_id = s_id;
	}

	public String getS_title() {
		return s_title;
	}

	public void setS_title(String s_title) {
		this.s_title = s_title;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public List<InformationChild> getList_child() {
		return list_child;
	}

	public void setList_child(List<InformationChild> list_child) {
		this.list_child = list_child;
	}

	public boolean isThirdPartyOrder() {
		return thirdPartyOrder;
	}

	public void setThirdPartyOrder(boolean thirdPartyOrder) {
		this.thirdPartyOrder = thirdPartyOrder;
	}

	@Override
	public String toString() {
		return "InformationGroup [s_id=" + s_id + ", s_title=" + s_title
				+ ", unreadCount=" + unreadCount + ", list_child=" + list_child
				+ ", thirdPartyOrder=" + thirdPartyOrder + "]";
	}



}

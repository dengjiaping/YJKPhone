package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:参赛人员实体
 * @Author: wangshuangshuang.
 * @Create: 2015年1月21日.
 */
public class CompetitionPeople {
	private String id;
	private String name;
	private String url;
	private String distance;
	public CompetitionPeople(String id, String name, String url, String distance) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.distance = distance;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	@Override
	public String toString() {
		return "CompetitionPeople [id=" + id + ", name=" + name + ", url="
				+ url + ", distance=" + distance + "]";
	}

}

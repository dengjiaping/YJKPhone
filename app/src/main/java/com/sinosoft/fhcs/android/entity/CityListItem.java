package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:城市选择实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
public class CityListItem {
	private String name;
	private String pcode;
	public String getName(){
		return name;
	}
	public String getPcode(){
		return pcode;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setPcode(String pcode){
		this.pcode=pcode;
	}
}

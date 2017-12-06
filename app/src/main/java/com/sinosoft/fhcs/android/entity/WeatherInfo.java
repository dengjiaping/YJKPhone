package com.sinosoft.fhcs.android.entity;

import java.io.Serializable;

/**
 * @CopyRight: SinoSoft.
 * @Description:天气信息 百度api  http://apistore.baidu.com/astore/serviceinfo/1798.html
 * @Author:wangshuangshuang.
 * @Create: 2015年4月16日.
 */
public class WeatherInfo implements Serializable{
	private static final long serialVersionUID = -7750080144744283376L;
	private String city;// "北京",
	private String pinyin;// "beijing",
	private String citycode;// "101010100",
	private String date;// "15-04-16",
	private String time;// "11;//00",
	private String postCode;// "100000",
	private String longitude;// 116.391,
	private String latitude;// 39.904,
	private String altitude;// "33",
	private String weather;// "晴",
	private String temp;// "21",
	private String l_tmp;// "8",
	private String h_tmp;// "21",
	private String WD;// "北风",
	private String WS;// "3-4级(10~17m/h)",
	private String sunrise;// "05:35",
	private String sunset;// "18:53"

	public WeatherInfo() {
		super();
	}
	public WeatherInfo(String city, String pinyin, String citycode,
					   String date, String time, String postCode, String longitude,
					   String latitude, String altitude, String weather, String temp,
					   String l_tmp, String h_tmp, String wD, String wS, String sunrise,
					   String sunset) {
		super();
		this.city = city;
		this.pinyin = pinyin;
		this.citycode = citycode;
		this.date = date;
		this.time = time;
		this.postCode = postCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.weather = weather;
		this.temp = temp;
		this.l_tmp = l_tmp;
		this.h_tmp = h_tmp;
		WD = wD;
		WS = wS;
		this.sunrise = sunrise;
		this.sunset = sunset;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getL_tmp() {
		return l_tmp;
	}
	public void setL_tmp(String l_tmp) {
		this.l_tmp = l_tmp;
	}
	public String getH_tmp() {
		return h_tmp;
	}
	public void setH_tmp(String h_tmp) {
		this.h_tmp = h_tmp;
	}
	public String getWD() {
		return WD;
	}
	public void setWD(String wD) {
		WD = wD;
	}
	public String getWS() {
		return WS;
	}
	public void setWS(String wS) {
		WS = wS;
	}
	public String getSunrise() {
		return sunrise;
	}
	public void setSunrise(String sunrise) {
		this.sunrise = sunrise;
	}
	public String getSunset() {
		return sunset;
	}
	public void setSunset(String sunset) {
		this.sunset = sunset;
	}
	@Override
	public String toString() {
		return "WeatherInfo [city=" + city + ", pinyin=" + pinyin
				+ ", citycode=" + citycode + ", date=" + date + ", time="
				+ time + ", postCode=" + postCode + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", altitude=" + altitude
				+ ", weather=" + weather + ", temp=" + temp + ", l_tmp="
				+ l_tmp + ", h_tmp=" + h_tmp + ", WD=" + WD + ", WS=" + WS
				+ ", sunrise=" + sunrise + ", sunset=" + sunset + "]";
	}
}

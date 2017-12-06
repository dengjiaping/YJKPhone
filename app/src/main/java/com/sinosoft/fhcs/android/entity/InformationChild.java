package com.sinosoft.fhcs.android.entity;

/**
 * @CopyRight: SinoSoft.
 * @Description:资讯列表/二级实体（详情）
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import java.io.Serializable;

/**
 * HEALTHINFORMATION_TYPE_TXT_KEY = "3000101"; 文本 informationContent
 * HEALTHINFORMATION_TYPE_REPORT_KEY = "3000102";报告 html
 * HEALTHINFORMATION_TYPE_IMAGE_KEY = "3000103";图片 imageText
 * HEALTHINFORMATION_TYPE_VIDEO_KEY = "3000104";视频 video
 *
 */
public class InformationChild implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String html;
	private String imageText;
	private String video;
	private String voice;
	private String syncInformationSerialNo;
	private String familyMemberRoleName;// 发布对象
	private String informationContent;// 资讯内容
	private String informationType;// 资讯类型
	private String pubdate;// 发布日期
	private String title;// 资讯标题
	private boolean isRead;// 未读，已读
	private String gender;
	private String cooperateIdentify;
	public InformationChild() {
		super();
	}

	public InformationChild(String id, String html, String imageText,
							String video, String voice, String syncInformationSerialNo,
							String familyMemberRoleName, String informationContent,
							String informationType, String pubdate, String title, boolean isRead,String gender,String cooperateIdentify) {
		super();
		this.id = id;
		this.html = html;
		this.imageText = imageText;
		this.video = video;
		this.voice = voice;
		this.syncInformationSerialNo = syncInformationSerialNo;
		this.familyMemberRoleName = familyMemberRoleName;
		this.informationContent = informationContent;
		this.informationType = informationType;
		this.pubdate = pubdate;
		this.title = title;
		this.isRead = isRead;
		this.gender=gender;
		this.cooperateIdentify=cooperateIdentify;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getImageText() {
		return imageText;
	}

	public void setImageText(String imageText) {
		this.imageText = imageText;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getSyncInformationSerialNo() {
		return syncInformationSerialNo;
	}

	public void setSyncInformationSerialNo(String syncInformationSerialNo) {
		this.syncInformationSerialNo = syncInformationSerialNo;
	}

	public String getFamilyMemberRoleName() {
		return familyMemberRoleName;
	}

	public void setFamilyMemberRoleName(String familyMemberRoleName) {
		this.familyMemberRoleName = familyMemberRoleName;
	}

	public String getInformationContent() {
		return informationContent;
	}

	public void setInformationContent(String informationContent) {
		this.informationContent = informationContent;
	}

	public String getInformationType() {
		return informationType;
	}

	public void setInformationType(String informationType) {
		this.informationType = informationType;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCooperateIdentify() {
		return cooperateIdentify;
	}

	public void setCooperateIdentify(String cooperateIdentify) {
		this.cooperateIdentify = cooperateIdentify;
	}

	@Override
	public String toString() {
		return "InformationChild [id=" + id + ", html=" + html + ", imageText="
				+ imageText + ", video=" + video + ", voice=" + voice
				+ ", syncInformationSerialNo=" + syncInformationSerialNo
				+ ", familyMemberRoleName=" + familyMemberRoleName
				+ ", informationContent=" + informationContent
				+ ", informationType=" + informationType + ", pubdate="
				+ pubdate + ", title=" + title + ", isRead=" + isRead
				+ ", gender=" + gender + ", cooperateIdentify="
				+ cooperateIdentify + "]";
	}
}

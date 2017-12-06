package com.sinosoft.fhcs.android.entity;
/**
 * @CopyRight: SinoSoft.
 * @Description:家庭成员实体
 * @Author: wangshuangshuang.
 * @Create: 2014年8月15日.
 */
import java.io.Serializable;
public class FamilyMember implements Serializable {
	private static final long serialVersionUID = -7750080144744283376L;
	private long id;
	private String gender;// 性别 0-女,1-男
	private String birthday;// 出生日期
	private String familyRoleName;// 家庭角色名称
	private double weight;// 体重
	private double height;// 身高
	private double stepSize;// 步长
	private double waist;// 腰围
	private String addTime;// 添加时间
	private int age;
	private int ImageId;
	private String mobile;
	private boolean masterFamilyMember;
	private int device_name;
	public FamilyMember() {
		super();
	}

	public FamilyMember(long id, String gender, String birthday,
						String familyRoleName, double weight, double height,
						double stepSize, double waist, String addTime, int age,String mobile,boolean masterFamilyMember,int device_name) {
		super();
		this.id = id;
		this.gender = gender;
		this.birthday = birthday;
		this.familyRoleName = familyRoleName;
		this.weight = weight;
		this.height = height;
		this.stepSize = stepSize;
		this.waist = waist;
		this.addTime = addTime;
		this.age = age;
		this.mobile=mobile;
		this.masterFamilyMember=masterFamilyMember;
		this.device_name=device_name;
	}

	public FamilyMember(long id, String roleName, int age, String gender,
						double weight, double height, double waist, String birthday,
						double stepSize, int imageId,String mobile,boolean masterFamilyMember,int device_name) {
		this.id = id;
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.ImageId = imageId;
		this.familyRoleName = roleName;
		this.stepSize = stepSize;
		this.gender = gender;
		this.birthday = birthday;
		this.waist = waist;
		this.mobile=mobile;
		this.masterFamilyMember=masterFamilyMember;
		this.device_name=device_name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getFamilyRoleName() {
		return familyRoleName;
	}
	public void setFamilyRoleName(String familyRoleName) {
		this.familyRoleName = familyRoleName;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getStepSize() {
		return stepSize;
	}
	public void setStepSize(double stepSize) {
		this.stepSize = stepSize;
	}
	public double getWaist() {
		return waist;
	}
	public void setWaist(double waist) {
		this.waist = waist;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getImageId() {
		return ImageId;
	}
	public void setImageId(int imageId) {
		ImageId = imageId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isMasterFamilyMember() {
		return masterFamilyMember;
	}

	public void setMasterFamilyMember(boolean masterFamilyMember) {
		this.masterFamilyMember = masterFamilyMember;
	}

	public int getDevice_name() {
		return device_name;
	}

	public void setDevice_name(int device_name) {
		this.device_name = device_name;
	}

	@Override
	public String toString() {
		return "FamilyMember [id=" + id + ", gender=" + gender + ", birthday="
				+ birthday + ", familyRoleName=" + familyRoleName + ", weight="
				+ weight + ", height=" + height + ", stepSize=" + stepSize
				+ ", waist=" + waist + ", addTime=" + addTime + ", age=" + age
				+ ", ImageId=" + ImageId + ", mobile=" + mobile
				+ ", masterFamilyMember=" + masterFamilyMember
				+ ", device_name=" + device_name + "]";
	}

}

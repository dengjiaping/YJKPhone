package com.sinosoft.fhcs.android.entity;

import java.io.Serializable;

public class DoctorBean implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private int dId;
    private String doctorUrl;
    private String doctorName;
    private String doctorJob;
    private String doctorGood;
    private String doctorAbstrack;
    
    
    public String getDoctorAbstrack() {
        return doctorAbstrack;
    }
    public void setDoctorAbstrack(String doctorAbstrack) {
        this.doctorAbstrack = doctorAbstrack;
    }
    public int getdId() {
        return dId;
    }
    public void setdId(int dId) {
        this.dId = dId;
    }
    public String getDoctorUrl() {
        return doctorUrl;
    }
    public void setDoctorUrl(String doctorUrl) {
        this.doctorUrl = doctorUrl;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public String getDoctorJob() {
        return doctorJob;
    }
    public void setDoctorJob(String doctorJob) {
        this.doctorJob = doctorJob;
    }
    public String getDoctorGood() {
        return doctorGood;
    }
    public void setDoctorGood(String doctorGood) {
        this.doctorGood = doctorGood;
    }
    @Override
    public String toString() {
        return "DoctorBean [dId=" + dId + ", doctorUrl=" + doctorUrl + ", doctorName=" + doctorName + ", doctorJob="
                + doctorJob + ", doctorGood=" + doctorGood + "]";
    }
    
    
    

}

package com.sinosoft.fhcs.android.entity;

/**
 * 科室详情
 * @author shuiq_000
 *
 */
public class Department{
    public int drawable;
    public String name;
    public String abstracts;//简介
    public int getDrawable() {
        return drawable;
    }
    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAbstracts() {
        return abstracts;
    }
    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }
    @Override
    public String toString() {
        return "Department [drawable=" + drawable + ", name=" + name + ", abstracts=" + abstracts + "]";
    }




}


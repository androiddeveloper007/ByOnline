package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Created by zzp on 2018/5/15.
 * 医生评价bean
 */

public class DoctorComment implements Serializable{

    private String img;
    private String name;
    private String level;
    private String hospitalName;
    private String time;
    private String comment;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DoctorComment(String img, String name, String level, String hospitalName, String time, String comment) {
        this.img = img;
        this.name = name;
        this.level = level;
        this.hospitalName = hospitalName;
        this.time = time;
        this.comment = comment;
    }
}

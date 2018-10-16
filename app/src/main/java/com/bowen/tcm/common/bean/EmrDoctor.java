package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Created by zzp on 2018/5/15.
 */

public class EmrDoctor implements Serializable{
    private String doctorId;
    private String name;
    private String positionStr;
    private String fileLink;
    private String serviceSwitchStr;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPositionStr() {
        return positionStr;
    }

    public void setPositionStr(String positionStr) {
        this.positionStr = positionStr;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getServiceSwitchStr() {
        return serviceSwitchStr;
    }

    public void setServiceSwitchStr(String serviceSwitchStr) {
        this.serviceSwitchStr = serviceSwitchStr;
    }
}

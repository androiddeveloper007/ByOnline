package com.bowen.tcm.common.bean.network;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/23.
 */
public class AppointmentPeriod{
    /**
     * appointmentId :
     * typeName :
     * applyNum :
     * maxNum :
     */

    private String appointmentId;
    private String typeName;
    private int applyNum;
    private int maxNum;
    private int appStatus;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
}

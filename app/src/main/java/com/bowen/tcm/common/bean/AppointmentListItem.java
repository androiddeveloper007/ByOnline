package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Created by zzp on 2018/5/15.
 */

public class AppointmentListItem implements Serializable{
    private long appointmentDate;//日期
    private String typeStr;//上午下午晚上
    private int applyNum;//预约申请人数
    private int maxNum;//最大预约人数
    private String isCureStr;//是否接诊
    private String isFull;//是否预约满
    private String weekStatusStr;//周状态:上一周，本周，下一周

    public long getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(long appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
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

    public String getIsCureStr() {
        return isCureStr;
    }

    public void setIsCureStr(String isCureStr) {
        this.isCureStr = isCureStr;
    }

    public String getIsFull() {
        return isFull;
    }

    public void setIsFull(String isFull) {
        this.isFull = isFull;
    }

    public String getWeekStatusStr() {
        return weekStatusStr;
    }

    public void setWeekStatusStr(String weekStatusStr) {
        this.weekStatusStr = weekStatusStr;
    }
}

package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.Appointment;

import java.util.ArrayList;

/**
 * Describe:预约信息
 * Created by AwenZeng on 2018/7/16.
 */
public class AppointmentInfo {
    /**
     * appointmentDate :
     * typeStr :
     * week :
     * appStatus :
     */

    private long appointmentDate;
    private String type;
    private String week;
    private String appStatus;

    public long getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(long appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTypeStr() {
        return type;
    }

    public void setTypeStr(String type) {
        this.type = type;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }
}

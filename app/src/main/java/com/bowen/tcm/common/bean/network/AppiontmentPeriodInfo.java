package com.bowen.tcm.common.bean.network;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:预约时间段信息
 * Created by AwenZeng on 2018/7/23.
 */
public class AppiontmentPeriodInfo{
    private String positionStr;
    private String name;
    private String registerFee;
    private String appointmentPeriod;

    private AppointmentDoctor emrAppointment;
    private ArrayList<AppointmentPeriod> emrAppointmentList;

    public AppointmentDoctor getEmrAppointment() {
        return emrAppointment;
    }

    public void setEmrAppointment(AppointmentDoctor emrAppointment) {
        this.emrAppointment = emrAppointment;
    }

    public ArrayList<AppointmentPeriod> getEmrAppointmentList() {
        return emrAppointmentList;
    }

    public void setEmrAppointmentList(ArrayList<AppointmentPeriod> emrAppointmentList) {
        this.emrAppointmentList = emrAppointmentList;
    }

    public String getPositionStr() {
        return positionStr;
    }

    public void setPositionStr(String positionStr) {
        this.positionStr = positionStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisterFee() {
        return registerFee;
    }

    public void setRegisterFee(String registerFee) {
        this.registerFee = registerFee;
    }

    public String getAppointmentPeriod() {
        return appointmentPeriod;
    }

    public void setAppointmentPeriod(String appointmentPeriod) {
        this.appointmentPeriod = appointmentPeriod;
    }
}

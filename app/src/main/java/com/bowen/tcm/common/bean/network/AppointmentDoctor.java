package com.bowen.tcm.common.bean.network;

import java.io.Serializable;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/23.
 */
public class AppointmentDoctor{


    /**
     * doctorId :
     * appointmentDate :
     * week :
     * typeStr :
     * hospital :
     * address :
     */

    private String doctorId;
    private long appointmentDate;
    private String week;
    private String typeStr;
    private String hospital;
    private String address;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public long getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(long appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

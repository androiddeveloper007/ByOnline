package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/23.
 */
public class SubmitAppointmentInfo implements Serializable{
    private String doctorId;
    private String appointmentId;
    private String doctorName;
    private String doctorPosRank;
    private String clinicName;
    private String clinicAddress;
    private String productPrice;
    private long appointmentDate;//预约日期
    private String timePeriod;//就诊时段

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPosRank() {
        return doctorPosRank;
    }

    public void setDoctorPosRank(String doctorPosRank) {
        this.doctorPosRank = doctorPosRank;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public long getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(long appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
}

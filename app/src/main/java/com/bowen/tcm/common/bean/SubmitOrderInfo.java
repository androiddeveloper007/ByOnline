package com.bowen.tcm.common.bean;

/**
 * Describe:提交订单信息
 * Created by AwenZeng on 2018/7/20.
 */
public class SubmitOrderInfo {
    private String doctorId;
    private String orderType;
    private String price;
    private String appointmentId;
    private String familyId;
    private String caseId;
    private String illDesc;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getIllDesc() {
        return illDesc;
    }

    public void setIllDesc(String illDesc) {
        this.illDesc = illDesc;
    }
}

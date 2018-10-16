package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Created by zzp on 2018/5/15.
 * 门诊预约列表bean
 */

public class Appointment implements Serializable{

    private String appointmentOrderId;//用户预约订单Id
    private String appointmentId;//医生预约排期id
    private String orderId;//订单支付id
    private String appointmentType;//预约类型
    private String appointmentStatus;//预约状态
    private String visitTimeStr;//就诊时间
    private long appointmentDate;//预约日期
    private String type;//预上午中午晚上
    private String typeName;//时间段名称
    private double totalAmount;//订单总金额
    private double realAmount;//订单实际支付金额
    private String doctorId;//医生id
    private String name;//医生姓名
    private String hospitalDepartments;//科室
    private String positionStr;//职称
    private String hospital;//所在医院
    private String fileLink;//医生头像
    private long payTime;//医生头像

    public String getAppointmentOrderId() {
        return appointmentOrderId;
    }

    public void setAppointmentOrderId(String appointmentOrderId) {
        this.appointmentOrderId = appointmentOrderId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAppointmentTypeStr() {
        return appointmentType;
    }

    public void setAppointmentTypeStr(String appointmentTypeStr) {
        this.appointmentType = appointmentTypeStr;
    }

    public String getAppointmentStatusStr() {
        return appointmentStatus;
    }

    public void setAppointmentStatusStr(String appointmentStatusStr) {
        this.appointmentStatus = appointmentStatusStr;
    }

    public String getVisitTime() {
        return visitTimeStr;
    }

    public void setVisitTime(String visitTime) {
        this.visitTimeStr = visitTime;
    }

    public long getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(long appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(double realAmount) {
        this.realAmount = realAmount;
    }

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

    public String getHospitalDepartments() {
        return hospitalDepartments;
    }

    public void setHospitalDepartments(String hospitalDepartments) {
        this.hospitalDepartments = hospitalDepartments;
    }

    public String getPositionStr() {
        return positionStr;
    }

    public void setPositionStr(String positionStr) {
        this.positionStr = positionStr;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }
}

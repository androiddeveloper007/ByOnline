package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Created by zzp on 2018/5/15.
 * 订单的bean
 */

public class MyOrder implements Serializable{
    private String orderId;
    private int orderStatus;
    private int orderType;
    private String totalAmount;
    private String realAmount;
    private String reduceAmount;
    private String doctorId;
    private String name;
    private String hospitalDepartments;
    private String positionStr;
    private String hospital;
    private String fileLink;
    private boolean isEvaluate;
    private String payChannel;
    private String appointmentOrderId;
    private long payTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(String reduceAmount) {
        this.reduceAmount = reduceAmount;
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

    public String getHostipal() {
        return hospital;
    }

    public void setHostipal(String hostipal) {
        this.hospital = hostipal;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public boolean isEvaluate() {
        return isEvaluate;
    }

    public void setEvaluate(boolean evaluate) {
        isEvaluate = evaluate;
    }

    public String getAppointmentOrderId() {
        return appointmentOrderId;
    }

    public void setAppointmentOrderId(String appointmentOrderId) {
        this.appointmentOrderId = appointmentOrderId;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }
}

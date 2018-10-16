package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.network.Doctor;

import java.io.Serializable;

/**
 * Describe:支付订单信息
 * Created by AwenZeng on 2018/7/20.
 */
public class PayOrderInfo implements Serializable {
    /**
     * orderType : 1
     * amount : 0.0
     * emrDoctor : {"fileLink":"http://192.168.0.241/tcm/image/5b47289906b963fbd2221600","address":"黄兴广场1号","hospitalDepartments":"更年期综合症","positionStr":"主任医师","name":"神医华佗","hospital":"长沙第一医院"}
     * appointmentOrderId : 5e680f3f17224331bcc38907b23900b3
     */

    private int orderType;
    private String amount;
    private Doctor emrDoctor;
    private String appointmentOrderId;
    private String orderId;
    private String payChannel;
    private boolean isPay; //0元商品支付成功返回true
    /**
     * 支付结果需要
     */
    private boolean isPaySuccess;


    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Doctor getEmrDoctor() {
        return emrDoctor;
    }

    public void setEmrDoctor(Doctor emrDoctor) {
        this.emrDoctor = emrDoctor;
    }

    public String getAppointmentOrderId() {
        return appointmentOrderId;
    }

    public void setAppointmentOrderId(String appointmentOrderId) {
        this.appointmentOrderId = appointmentOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isPaySuccess() {
        return isPaySuccess;
    }

    public void setPaySuccess(boolean paySuccess) {
        isPaySuccess = paySuccess;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }
}

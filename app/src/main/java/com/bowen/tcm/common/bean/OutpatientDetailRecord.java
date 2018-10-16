package com.bowen.tcm.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 * 订单详情的bean
 */

public class OutpatientDetailRecord implements Serializable{
    private MyOrder traOrder;
    private String clinic;//坐诊医馆
    private String clinicAddress;//坐诊地址
    private String visitTimeStr;//就诊时间区间
    private int appointmentStatus;//1：待支付 ，2：待就诊 ，3：已取消 ，4：已就诊 ，5：已过期

    public MyOrder getOrderList() {
        return traOrder;
    }

    public void setOrderList(MyOrder orderList) {
        this.traOrder = orderList;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getVisitTimeStr() {
        return visitTimeStr;
    }

    public void setVisitTimeStr(String visitTimeStr) {
        this.visitTimeStr = visitTimeStr;
    }

    public int getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(int appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}

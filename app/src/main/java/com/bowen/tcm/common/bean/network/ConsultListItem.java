package com.bowen.tcm.common.bean.network;

import java.io.Serializable;

/**
 * Describe:门诊预约
 * Created by AwenZeng on 2018/4/4.
 */

public class ConsultListItem implements Serializable{
    private String doctorId;
    private String name;
    private String headImgUrl;
    private boolean isRead;
    private String positionStr;
    private long lastConsultTime;
    private String content;
    private String orderId;

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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getPositionStr() {
        return positionStr;
    }

    public void setPositionStr(String positionStr) {
        this.positionStr = positionStr;
    }

    public long getLastConsultTime() {
        return lastConsultTime;
    }

    public void setLastConsultTime(long lastConsultTime) {
        this.lastConsultTime = lastConsultTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

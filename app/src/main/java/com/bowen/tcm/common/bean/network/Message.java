package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.DrugVolume;

import java.util.List;

/**
 * Describe: 消息
 * Created by AwenZeng on 2018/5/25.
 */
public class Message {

    /**
     * msgHandDate : 1527207060000
     * remindTime : 29460000
     * msgType : 1
     * remindFamilyType : 3
     * msgId : 35872ef646994871954061c0f5328f14
     * emrMedicineList : [{"dosage":"理工","drugName":"up王者荣耀"},{"dosage":"咯喔","drugName":"你要相信我"}]
     * msgStatus : 1
     * msgTypeStr : 用药提醒
     * userId : 8aac18214ce0430d9a9af0ba2d6d12a7
     */

    private long msgHandDate;
    private int remindTime;
    private String remindPhone;
    private int msgType;
    private String remindNickname;
    private String remindFamilyType;
    private String msgContent;
    private String msgId;
    private String msgStatus;
    private String msgTypeStr;
    private String userId;
    private List<DrugVolume> emrMedicineList;

    public long getMsgHandDate() {
        return msgHandDate;
    }

    public void setMsgHandDate(long msgHandDate) {
        this.msgHandDate = msgHandDate;
    }

    public int getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(int remindTime) {
        this.remindTime = remindTime;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getRemindFamilyType() {
        return remindFamilyType;
    }

    public void setRemindFamilyType(String remindFamilyType) {
        this.remindFamilyType = remindFamilyType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getMsgTypeStr() {
        return msgTypeStr;
    }

    public void setMsgTypeStr(String msgTypeStr) {
        this.msgTypeStr = msgTypeStr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<DrugVolume> getEmrMedicineList() {
        return emrMedicineList;
    }

    public void setEmrMedicineList(List<DrugVolume> emrMedicineList) {
        this.emrMedicineList = emrMedicineList;
    }

    public String getRemindNickname() {
        return remindNickname;
    }

    public void setRemindNickname(String remindNickname) {
        this.remindNickname = remindNickname;
    }

    public String getRemindPhone() {
        return remindPhone;
    }

    public void setRemindPhone(String remindPhone) {
        this.remindPhone = remindPhone;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}

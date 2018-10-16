package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.DrugVolume;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/11.
 */

public class Alarm implements Serializable{
    /**
     * drugCycle : 2
     * remindFamilyTypeStr : 本人
     * remindTime : 30360000
     * remindId : 536175123d0042ed82530a233d83b2e3
     * remindUserId : 9a12e04bd21d41ff84fe0b3d9271ec32
     * remindFamilyType : 0
     * remindDate : 1525968000000
     * emrMedicineList : [{"dosage":"咯","medicineId":"3b963525c7254a2fbdc4a3986001c7d9","remindId":"536175123d0042ed82530a233d83b2e3","drugName":"雍正妆","id":3}]
     * statusBool : true
     * userId : 8aac18214ce0430d9a9af0ba2d6d12a7
     * isShockBool : true
     */
    private int id;
    private String drugCycle;
    private String remindFamilyTypeStr;
    private String remindNickname;
    private long remindTime;
    private String remindId;
    private String ringtone;
    private String remindUserId;
    private String remindFamilyType;
    private long remindDate;
    private boolean statusBool;
    private String userId;
    private boolean isShockBool;
    private ArrayList<DrugVolume> emrMedicineList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrugCycle() {
        return drugCycle;
    }

    public void setDrugCycle(String drugCycle) {
        this.drugCycle = drugCycle;
    }

    public String getRemindFamilyTypeStr() {
        return remindFamilyTypeStr;
    }

    public void setRemindFamilyTypeStr(String remindFamilyTypeStr) {
        this.remindFamilyTypeStr = remindFamilyTypeStr;
    }

    public long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(long remindTime) {
        this.remindTime = remindTime;
    }

    public String getRemindId() {
        return remindId;
    }

    public void setRemindId(String remindId) {
        this.remindId = remindId;
    }

    public String getRemindUserId() {
        return remindUserId;
    }

    public void setRemindUserId(String remindUserId) {
        this.remindUserId = remindUserId;
    }

    public String getRemindFamilyType() {
        return remindFamilyType;
    }

    public void setRemindFamilyType(String remindFamilyType) {
        this.remindFamilyType = remindFamilyType;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    public long getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(long remindDate) {
        this.remindDate = remindDate;
    }

    public boolean isStatusBool() {
        return statusBool;
    }

    public void setStatusBool(boolean statusBool) {
        this.statusBool = statusBool;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isIsShockBool() {
        return isShockBool;
    }

    public void setIsShockBool(boolean isShockBool) {
        this.isShockBool = isShockBool;
    }

    public ArrayList<DrugVolume> getEmrMedicineList() {
        return emrMedicineList;
    }

    public void setEmrMedicineList(ArrayList<DrugVolume> emrMedicineList) {
        this.emrMedicineList = emrMedicineList;
    }

    public String getRemindNickname() {
        return remindNickname;
    }

    public void setRemindNickname(String remindNickname) {
        this.remindNickname = remindNickname;
    }

}

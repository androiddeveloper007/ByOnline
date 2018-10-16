package com.bowen.tcm.common.bean;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/5/11.
 */

public class AddAlarmInfo {

    private long remindTime;
    private long remindDate;
    private String repeatPeriod;
    private String remindRingUri;
    private boolean isShake;
    private String remindPersonId;
    private String remindPersonType;
    private boolean isStartAlarm;
    private ArrayList<DrugVolume> drugList;

    public long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(long remindTime) {
        this.remindTime = remindTime;
    }

    public long getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(long remindDate) {
        this.remindDate = remindDate;
    }

    public String getRepeatPeriod() {
        return repeatPeriod;
    }

    public void setRepeatPeriod(String repeatPeriod) {
        this.repeatPeriod = repeatPeriod;
    }

    public String getRemindRingUri() {
        return remindRingUri;
    }

    public void setRemindRingUri(String remindRingUri) {
        this.remindRingUri = remindRingUri;
    }

    public boolean isShake() {
        return isShake;
    }

    public void setShake(boolean shake) {
        isShake = shake;
    }

    public String getRemindPersonId() {
        return remindPersonId;
    }

    public void setRemindPersonId(String remindPersonId) {
        this.remindPersonId = remindPersonId;
    }

    public String getRemindPersonType() {
        return remindPersonType;
    }

    public void setRemindPersonType(String remindPersonType) {
        this.remindPersonType = remindPersonType;
    }

    public boolean isStartAlarm() {
        return isStartAlarm;
    }

    public void setStartAlarm(boolean startAlarm) {
        isStartAlarm = startAlarm;
    }

    public ArrayList<DrugVolume> getDrugList() {
        return drugList;
    }

    public void setDrugList(ArrayList<DrugVolume> drugList) {
        this.drugList = drugList;
    }
}

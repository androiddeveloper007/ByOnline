package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Created by zzp on 2018/5/15.
 * 偏方的bean
 */

public class FolkPrescription implements Serializable{

    /*"prescriptionId": "1019a8cd251641a1baa229d30d96c8a9",
    ║         "usageDosage": "一日2次，饭前服用，一次一粒",
    ║         "prescriptionName": "雷贝拉挫钠肠溶片",
    ║         "applyCrowdStr": "男性\/女性",
    ║         "prescriptionSectionStr": "骨科\/男科\/皮肤科\/消化科",
    ║         "applyDisease": "胃痛"
    ║         "isCollect": "是否被收藏",
    ║         "auditStatus": "审核状态"*/
    private String prescriptionId;
    private String prescriptionName;
    private String applyCrowdStr;
    private String applyDisease;
    private String prescriptionSectionStr;
    private String usageDosage;
    private String isCollect;
    private String auditStatus;//0:未审核 1: 审核通过 2:审核拒绝
    private String prescriptionSource;
    private String preSourceType;//0:用户 1:医生 2:后台
    private boolean isFromMine;//是否是我的偏方

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPrescriptionName() {
        return prescriptionName;
    }

    public void setPrescriptionName(String prescriptionName) {
        this.prescriptionName = prescriptionName;
    }

    public String getApplyCrowdStr() {
        return applyCrowdStr;
    }

    public void setApplyCrowdStr(String applyCrowdStr) {
        this.applyCrowdStr = applyCrowdStr;
    }

    public String getApplyDisease() {
        return applyDisease;
    }

    public void setApplyDisease(String applyDisease) {
        this.applyDisease = applyDisease;
    }

    public String getPrescriptionSectionStr() {
        return prescriptionSectionStr;
    }

    public void setPrescriptionSectionStr(String prescriptionSectionStr) {
        this.prescriptionSectionStr = prescriptionSectionStr;
    }

    public String getUsageDosage() {
        return usageDosage;
    }

    public void setUsageDosage(String usageDosage) {
        this.usageDosage = usageDosage;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getPrescriptionSource() {
        return prescriptionSource;
    }

    public void setPrescriptionSource(String prescriptionSource) {
        this.prescriptionSource = prescriptionSource;
    }

    public String getPreSourceType() {
        return preSourceType;
    }

    public void setPreSourceType(String preSourceType) {
        this.preSourceType = preSourceType;
    }

    public boolean isFromMine() {
        return isFromMine;
    }

    public void setFromMine(boolean fromMine) {
        isFromMine = fromMine;
    }
}

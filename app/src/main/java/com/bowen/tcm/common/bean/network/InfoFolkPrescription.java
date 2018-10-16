package com.bowen.tcm.common.bean.network;

/**
 * Describe:单个偏方的详情bean
 * Created by zzp on 2018/6/1.
 */

public class InfoFolkPrescription {
    private String prescriptionId;
    private String prescriptionName;
    private String applyCrowdStr;
    private String applyDisease;
    private String prescriptionSectionStr;
    private String usageDosage;
    private String shareUrl;
    private int id;
    private String prescriptionSource;
    private String preSourceType;//0:用户 1:医生 2:后台
    private String isCollect;//0：否 1：是

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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    private String shareContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }
}

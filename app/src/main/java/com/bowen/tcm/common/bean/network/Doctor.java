package com.bowen.tcm.common.bean.network;

import java.io.Serializable;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public class Doctor implements Serializable {
    /**
     * doctorId :
     * name :
     * fileLink :
     * hospital :
     * positionStr :
     * hospitalDepartments :
     * diseases :
     * introduction :
     * feedbackRate :
     * reversionRate :
     * complaintsNumber :
     * followNumber :
     * serviceNumber :
     * recommended :
     */
    private String doctorId;
    private String name;
    private String headImgUrl;
    private String fileLink;
    private String hospital;
    private String positionStr;
    private String hospitalDepartments;
    private String diseases;
    private String introduction;
    private float feedbackRate;//好评率
    private float reversionRate;//回复率
    private long complaintsNumber;
    private String followNumber;
    private long serviceNumber;
    private int recommended;
    private String consultFee;
    private String registerFee;
    private boolean isAsk;
    private String orderId;
    private boolean showConsult;
    private String shareUrl;

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

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getPositionStr() {
        return positionStr;
    }

    public void setPositionStr(String positionStr) {
        this.positionStr = positionStr;
    }

    public String getHospitalDepartments() {
        return hospitalDepartments;
    }

    public void setHospitalDepartments(String hospitalDepartments) {
        this.hospitalDepartments = hospitalDepartments;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public float getFeedbackRate() {
        return feedbackRate;
    }

    public void setFeedbackRate(float feedbackRate) {
        this.feedbackRate = feedbackRate;
    }

    public float getReversionRate() {
        return reversionRate;
    }

    public void setReversionRate(float reversionRate) {
        this.reversionRate = reversionRate;
    }

    public long getComplaintsNumber() {
        return complaintsNumber;
    }

    public void setComplaintsNumber(long complaintsNumber) {
        this.complaintsNumber = complaintsNumber;
    }

    public String getFollowNumber() {
        return followNumber;
    }

    public void setFollowNumber(String followNumber) {
        this.followNumber = followNumber;
    }

    public long getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(long serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public int getRecommended() {
        return recommended;
    }

    public void setRecommended(int recommended) {
        this.recommended = recommended;
    }

    public String getConsultFee() {
        return consultFee;
    }

    public void setConsultFee(String consultFee) {
        this.consultFee = consultFee;
    }

    public String getRegisterFee() {
        return registerFee;
    }

    public void setRegisterFee(String registerFee) {
        this.registerFee = registerFee;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public boolean isAsk() {
        return isAsk;
    }

    public void setAsk(boolean ask){
        isAsk = ask;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isShowConsult() {
        return showConsult;
    }

    public void setShowConsult(boolean showConsult) {
        this.showConsult = showConsult;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}

package com.bowen.tcm.common.bean.network;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public
class EmrDoctorListBean {
    /**
     * doctorId :
     * name :
     * headImgUrl :
     * hospital :
     * positionStr :
     * hospitalDepartments :
     * diseases :
     * introduction :
     */

    private String doctorId;
    private String name;
    private String headImgUrl;
    private String hospital;
    private String positionStr;
    private String hospitalDepartments;
    private String diseases;
    private String introduction;
    private String recommended;//是否推荐1:是 2:否
    private String isAsk;//true可咨询 false 不可咨询
    private String consultFee;//图文咨询价格

    public String getIsAsk() {
        return isAsk;
    }

    public void setIsAsk(String isAsk) {
        this.isAsk = isAsk;
    }

    public String getConsultFee() {
        return consultFee;
    }

    public void setConsultFee(String consultFee) {
        this.consultFee = consultFee;
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


    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }
}
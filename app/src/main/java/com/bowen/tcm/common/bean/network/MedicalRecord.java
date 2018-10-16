package com.bowen.tcm.common.bean.network;

import java.io.Serializable;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/4.
 */

public class MedicalRecord implements Serializable{

    /**
     * clinicName : 可以找我
     * illTime : 1522771200000
     * userPhone : 18665859332
     * userId : 5b5edea81582489cbcee3bdc1c50d631
     * doctorResult : 咯喔
     * doctorStage : 0
     * rightType : 1
     * familyId : b58c0aea698f4668958b4f24337f03a6
     * doctorName : 同学找我
     * comFileInfoList : [{"fileLink":"http://192.168.0.242/tcm/image/5ac49e0114a851ada1c98cea","businessId":"811ac99f40f44fd382b78015ceb46ef7","fileCode":"2","fileType":"0","fileId":"5ac49e0114a851ada1c98cea"},{"fileLink":"http://192.168.0.242/tcm/image/5ac49e0114a851ada1c98ced","businessId":"811ac99f40f44fd382b78015ceb46ef7","fileCode":"2","fileType":"0","fileId":"5ac49e0114a851ada1c98ced"},{"fileLink":"http://192.168.0.242/tcm/image/5ac49e0114a851ada1c98cf0","businessId":"811ac99f40f44fd382b78015ceb46ef7","fileCode":"2","fileType":"0","fileId":"5ac49e0114a851ada1c98cf0"}]
     * caseId : 811ac99f40f44fd382b78015ceb46ef7
     * doctorTime : 1522771200000
     * caseDetails : 具体位置咿呀咿呀哟
     * caseName : 发烧
     * courseId : 811ac99f40f44fd382b78015ceb46ef7
     */

    private String clinicName;
    private long illTime;
    private String userPhone;
    private String userId;
    private String doctorResult;
    private String doctorStage;
    private int rightType;
    private String familyId;
    private String doctorName;
    private String caseId;
    private long doctorTime;
    private String caseDetails;
    private String caseName;
    private String courseId;
    private String seePhone;
    private List<PhotoFile> comFileInfoList;
    private boolean isChoose;


    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public long getIllTime() {
        return illTime;
    }

    public void setIllTime(long illTime) {
        this.illTime = illTime;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorResult() {
        return doctorResult;
    }

    public void setDoctorResult(String doctorResult) {
        this.doctorResult = doctorResult;
    }

    public String getDoctorStage() {
        return doctorStage;
    }

    public void setDoctorStage(String doctorStage) {
        this.doctorStage = doctorStage;
    }

    public int getRightType() {
        return rightType;
    }

    public void setRightType(int rightType) {
        this.rightType = rightType;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public long getDoctorTime() {
        return doctorTime;
    }

    public void setDoctorTime(long doctorTime) {
        this.doctorTime = doctorTime;
    }

    public String getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(String caseDetails) {
        this.caseDetails = caseDetails;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public List<PhotoFile> getComFileInfoList() {
        return comFileInfoList;
    }

    public void setComFileInfoList(List<PhotoFile> comFileInfoList) {
        this.comFileInfoList = comFileInfoList;
    }

    public String getSeePhone() {
        return seePhone;
    }

    public void setSeePhone(String seePhone) {
        this.seePhone = seePhone;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}

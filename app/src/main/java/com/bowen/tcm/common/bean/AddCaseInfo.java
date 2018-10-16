package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/4.
 */

public class AddCaseInfo implements Serializable {

    private String courseId;
    private String caseId;
    private String userPhone;
    private String familyId;
    private String caseName;
    private String caseDetails;
    private long illTime;
    private long diagnoseTime;
    private int diagnoseStage;
    private String diagnoseResult;
    private String diagnoseClinic;
    private String diagnoseDoctorName;
    private String seePhone;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(String caseDetails) {
        this.caseDetails = caseDetails;
    }

    public long getIllTime() {
        return illTime;
    }

    public void setIllTime(long illTime) {
        this.illTime = illTime;
    }

    public long getDiagnoseTime() {
        return diagnoseTime;
    }

    public void setDiagnoseTime(long diagnoseTime) {
        this.diagnoseTime = diagnoseTime;
    }

    public int getDiagnoseStage() {
        return diagnoseStage;
    }

    public void setDiagnoseStage(int diagnoseStage) {
        this.diagnoseStage = diagnoseStage;
    }

    public String getDiagnoseResult() {
        return diagnoseResult;
    }

    public void setDiagnoseResult(String diagnoseResult) {
        this.diagnoseResult = diagnoseResult;
    }

    public String getDiagnoseClinic() {
        return diagnoseClinic;
    }

    public void setDiagnoseClinic(String diagnoseClinic) {
        this.diagnoseClinic = diagnoseClinic;
    }

    public String getDiagnoseDoctorName() {
        return diagnoseDoctorName;
    }

    public void setDiagnoseDoctorName(String diagnoseDoctorName) {
        this.diagnoseDoctorName = diagnoseDoctorName;
    }

    public String getSeePhone() {
        return seePhone;
    }

    public void setSeePhone(String seePhone) {
        this.seePhone = seePhone;
    }
}

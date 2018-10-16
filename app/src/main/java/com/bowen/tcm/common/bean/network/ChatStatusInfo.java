package com.bowen.tcm.common.bean.network;


/**
 * Describe:
 * Created by AwenZeng on 2018/8/2.
 */
public class ChatStatusInfo{
    /**
     * isServiceEnd : false
     * showAppointment : false
     * caseId :
     * familyId :
     * illDesc :
     * familyNickname :
     * age :
     */
    private boolean isServiceEnd;
    private boolean showAppointment;
    private String caseId;
    private String familyId;
    private String illDesc;
    private String familyNickname;
    private String age;
    private int isFirstConsult;

    public boolean isServiceEnd() {
        return isServiceEnd;
    }

    public void setServiceEnd(boolean serviceEnd) {
        isServiceEnd = serviceEnd;
    }

    public boolean isShowAppointment() {
        return showAppointment;
    }

    public void setShowAppointment(boolean showAppointment) {
        this.showAppointment = showAppointment;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getIllDesc() {
        return illDesc;
    }

    public void setIllDesc(String illDesc) {
        this.illDesc = illDesc;
    }

    public String getFamilyNickname() {
        return familyNickname;
    }

    public void setFamilyNickname(String familyNickname) {
        this.familyNickname = familyNickname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getIsFirstConsult() {
        return isFirstConsult;
    }

    public void setIsFirstConsult(int isFirstConsult) {
        this.isFirstConsult = isFirstConsult;
    }
}

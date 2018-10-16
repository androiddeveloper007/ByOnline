package com.bowen.tcm.common.bean.network;

/**
 * Describe:用户心意
 * Created by AwenZeng on 2018/7/11.
 */
public class DoctorGiftInfo {
    /**
     * userId :
     * doctorId :
     * doctorGiftId :
     * giftName :
     * userNickname :
     * userHeadUrl :
     */
    private String userId;
    private String doctorId;
    private String doctorGiftId;
    private String giftName;
    private String userNickname;
    private String userHeadUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorGiftId() {
        return doctorGiftId;
    }

    public void setDoctorGiftId(String doctorGiftId) {
        this.doctorGiftId = doctorGiftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }
}

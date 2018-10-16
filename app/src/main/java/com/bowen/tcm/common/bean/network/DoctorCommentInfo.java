package com.bowen.tcm.common.bean.network;

/**
 * Describe:医生评论
 * Created by AwenZeng on 2018/7/11.
 */
public class DoctorCommentInfo {
    /**
     * userName :
     * userImg :
     * appointmentTypeStr :
     * userEvaluate :
     * userEvaluateTime :
     * score :
     * answerCost :
     */

    private String userName;
    private String userImg;
    private String appointmentTypeStr;
    private String userEvaluate;
    private long userEvaluateTime;
    private float score;
    private String amount;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getAppointmentTypeStr() {
        return appointmentTypeStr;
    }

    public void setAppointmentTypeStr(String appointmentTypeStr) {
        this.appointmentTypeStr = appointmentTypeStr;
    }

    public String getUserEvaluate() {
        return userEvaluate;
    }

    public void setUserEvaluate(String userEvaluate) {
        this.userEvaluate = userEvaluate;
    }

    public long getUserEvaluateTime() {
        return userEvaluateTime;
    }

    public void setUserEvaluateTime(long userEvaluateTime) {
        this.userEvaluateTime = userEvaluateTime;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

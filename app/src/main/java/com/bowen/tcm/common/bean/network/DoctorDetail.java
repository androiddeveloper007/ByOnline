package com.bowen.tcm.common.bean.network;

import java.io.Serializable;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/11.
 */
public class DoctorDetail{
    private Doctor emrDoctor;
    private boolean isFollow;
    private DoctorCommentInfo emrComment;
    private boolean showConsult;
    private boolean showAppointment;
    private int commentCount;
    private List<DoctorGiftInfo> doctorGiftList;
    private List<DoctorTalkInfo> articleList;


    public Doctor getEmrDoctor() {
        return emrDoctor;
    }

    public void setEmrDoctor(Doctor emrDoctor) {
        this.emrDoctor = emrDoctor;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public DoctorCommentInfo getEmrComment() {
        return emrComment;
    }

    public void setEmrComment(DoctorCommentInfo emrComment) {
        this.emrComment = emrComment;
    }

    public boolean isShowConsult() {
        return showConsult;
    }

    public void setShowConsult(boolean showConsult) {
        this.showConsult = showConsult;
    }

    public boolean isShowAppointment() {
        return showAppointment;
    }

    public void setShowAppointment(boolean showAppointment) {
        this.showAppointment = showAppointment;
    }

    public List<DoctorGiftInfo> getDoctorGiftList() {
        return doctorGiftList;
    }

    public void setDoctorGiftList(List<DoctorGiftInfo> doctorGiftList) {
        this.doctorGiftList = doctorGiftList;
    }

    public List<DoctorTalkInfo> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<DoctorTalkInfo> articleList) {
        this.articleList = articleList;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}

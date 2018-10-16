package com.bowen.tcm.common.bean.network;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/17.
 */
public class UserDoctorComment {
    private ArrayList<DoctorCommentInfo> doctorCommentList;
    private Page page;

    public ArrayList<DoctorCommentInfo> getDoctorCommentList() {
        return doctorCommentList;
    }

    public void setDoctorCommentList(ArrayList<DoctorCommentInfo> doctorCommentList) {
        this.doctorCommentList = doctorCommentList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

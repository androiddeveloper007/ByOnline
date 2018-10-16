package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.DoctorPrescriptionComment;
import com.bowen.tcm.common.bean.UserPrescriptionComment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:医生偏方评论
 * Created by zhuzhipeng on 2018/7/4.
 */

public class PrescriptionDoctorCommentRecord implements Serializable{
    private ArrayList<DoctorPrescriptionComment> prescriptionCommentList;
    private Page page;

    public ArrayList<DoctorPrescriptionComment> getPrescriptionCommentList() {
        return prescriptionCommentList;
    }

    public void setPrescriptionCommentList(ArrayList<DoctorPrescriptionComment> prescriptionCommentList) {
        this.prescriptionCommentList = prescriptionCommentList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

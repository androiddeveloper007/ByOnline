package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.FolkPrescription;
import com.bowen.tcm.common.bean.UserPrescriptionComment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:用户偏方评论
 * Created by zhuzhipeng on 2018/7/4.
 */

public class PrescriptionUserCommentRecord implements Serializable{
    private ArrayList<UserPrescriptionComment> prescriptionCommentList;
    private Page page;

    public ArrayList<UserPrescriptionComment> getPrescriptionCommentList() {
        return prescriptionCommentList;
    }

    public void setPrescriptionCommentList(ArrayList<UserPrescriptionComment> prescriptionCommentList) {
        this.prescriptionCommentList = prescriptionCommentList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

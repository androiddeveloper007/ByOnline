package com.bowen.tcm.common.bean.network;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/4.
 */

public class HomeMedicalRecord {
    private ArrayList<MedicalRecord> emrCaseInfoList;
    private Page page;

    public ArrayList<MedicalRecord> getEmrCaseInfoList() {
        return emrCaseInfoList;
    }

    public void setEmrCaseInfoList(ArrayList<MedicalRecord> emrCaseInfoList) {
        this.emrCaseInfoList = emrCaseInfoList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

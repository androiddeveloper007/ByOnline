package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.FolkPrescription;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/4.
 */

public class FolkPrescriptionRecord {
    private ArrayList<FolkPrescription> folkPrescriptionList;
    private Page page;

    public ArrayList<FolkPrescription> getFolkPrescriptionList() {
        return folkPrescriptionList;
    }

    public void setFolkPrescriptionList(ArrayList<FolkPrescription> emrCaseInfoList) {
        this.folkPrescriptionList = emrCaseInfoList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

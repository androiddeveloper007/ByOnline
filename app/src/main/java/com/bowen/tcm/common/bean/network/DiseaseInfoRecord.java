package com.bowen.tcm.common.bean.network;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:门诊预约分页实体类
 * Created by zhuzhipeng on 2018/4/4.
 */

public class DiseaseInfoRecord implements Serializable {

    private ArrayList<DiseaseInfo> diseaseDeptList;
    private Page page;

    public ArrayList<DiseaseInfo> getDiseaseInfoList() {
        return diseaseDeptList;
    }

    public void setDiseaseInfoList(ArrayList<DiseaseInfo> diseaseInfoList) {
        this.diseaseDeptList = diseaseInfoList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

package com.bowen.tcm.common.bean.network;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:我的关注
 * Created by zhuzhipeng on 2018/7/9
 */

public class DoctorFanRecord implements Serializable{

    private ArrayList<Doctor> emrDoctorFanList;
    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public ArrayList<Doctor> getEmrDoctorFanList() {
        return emrDoctorFanList;
    }

    public void setEmrDoctorFanList(ArrayList<Doctor> emrDoctorFanList) {
        this.emrDoctorFanList = emrDoctorFanList;
    }

}

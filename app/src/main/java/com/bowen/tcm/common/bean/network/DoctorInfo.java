package com.bowen.tcm.common.bean.network;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public class DoctorInfo {

    private ArrayList<Doctor>  doctorList;
    private Page page;

    public ArrayList<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(ArrayList<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

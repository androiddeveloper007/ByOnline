package com.bowen.tcm.common.bean.network;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public class CliniclInfo {

    private ArrayList<Clinic>  HospitalList;
    private ArrayList<Clinic>  emrHospitalList;
    private ArrayList<Clinic> mapHospitalList;
    private Page page;

    public ArrayList<Clinic> getHospitalList() {
        return HospitalList;
    }

    public void setHospitalList(ArrayList<Clinic> hospitalList) {
        HospitalList = hospitalList;
    }

    public ArrayList<Clinic> getMapHospitalList() {
        return mapHospitalList;
    }

    public void setMapHospitalList(ArrayList<Clinic> mapHospitalList) {
        this.mapHospitalList = mapHospitalList;
    }

    public ArrayList<Clinic> getEmrHospitalList() {
        return emrHospitalList;
    }

    public void setEmrHospitalList(ArrayList<Clinic> emrHospitalList) {
        this.emrHospitalList = emrHospitalList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

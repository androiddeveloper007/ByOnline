package com.bowen.tcm.common.bean.network;

import java.util.ArrayList;

/**
 * Describe:寻医首页Bean
 * Created by AwenZeng on 2018/7/31.
 */
public class FindDoctorInfo {
    private String inquiryCount;
    private ArrayList<FindDoctorItem> hospitalDeptList;
    private ArrayList<FindDoctorItem> diseaseList;
    private ArrayList<String> headImgList;

    public String getInquiryCount() {
        return inquiryCount;
    }

    public void setInquiryCount(String inquiryCount) {
        this.inquiryCount = inquiryCount;
    }

    public ArrayList<FindDoctorItem> getHospitalDeptList() {
        return hospitalDeptList;
    }

    public void setHospitalDeptList(ArrayList<FindDoctorItem> hospitalDeptList) {
        this.hospitalDeptList = hospitalDeptList;
    }

    public ArrayList<FindDoctorItem> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(ArrayList<FindDoctorItem> diseaseList) {
        this.diseaseList = diseaseList;
    }

    public ArrayList<String> getHeadImgList() {
        return headImgList;
    }

    public void setHeadImgList(ArrayList<String> headImgList) {
        this.headImgList = headImgList;
    }
}

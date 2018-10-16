package com.bowen.tcm.common.bean.network;

import java.io.Serializable;
import java.util.List;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public class ClinicDetailInfo implements Serializable{

    /**
     * emrHospital : {"hospitalId":"","doctorId":"","hospitalName":"","provinceName":"","recommended":"","cityName":"","areaName":"","address":"","phone":"","introduction":"","hospitalImgUrl":""}
     * emrDoctorList : [{"doctorId":"","name":"","headImgUrl":"","hospital":"","positionStr":"","hospitalDepartments":"","diseases":"","introduction":""}]
     * page : {"prePage":"1","pageNo":"1","nextPage":"1","totalPages":"1","pageSize":"10","totalCount":"1"}
     */

    private EmrHospitalBean emrHospital;
    private Page page;
    private List<EmrDoctorListBean> emrDoctorList;

    public EmrHospitalBean getEmrHospital() {
        return emrHospital;
    }

    public void setEmrHospital(EmrHospitalBean emrHospital) {
        this.emrHospital = emrHospital;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<EmrDoctorListBean> getEmrDoctorList() {
        return emrDoctorList;
    }

    public void setEmrDoctorList(List<EmrDoctorListBean> emrDoctorList) {
        this.emrDoctorList = emrDoctorList;
    }


}

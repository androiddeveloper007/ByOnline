package com.bowen.tcm.common.model;


import com.bowen.commonlib.event.LocationEvent;
import com.bowen.tcm.common.bean.network.BannerInfo;
import com.bowen.tcm.common.bean.network.Category;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.common.bean.network.Department;
import com.bowen.tcm.common.bean.network.DoctorRank;
import com.bowen.tcm.common.bean.network.Tips;
import com.bowen.tcm.common.bean.network.VisiablePerson;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用配置信息
 * <p>
 * Created by AwenZeng on 2017/5/6.<p>
 */

public class AppConfigInfo {
    private static AppConfigInfo instance = null;

    private String warmWishes;

    private String licenseUrl;

    private String customerServicePhone;

    private Tips smallTips;

    private LocationEvent locationEvent;

    private ArrayList<Column> columnList;

    private ArrayList<BannerInfo> bannerList;

    private ArrayList<Department> departmentList;

    private ArrayList<DoctorRank> doctorRankList;

    private List<Category> familyUserRelationList;

    private List<Category> diseaseNameList;

    private List<Category> diagnoseStageList;

    private List<VisiablePerson> visiablePersonList;

    public static AppConfigInfo getInstance() {
        if (instance == null) {
            synchronized (UserInfo.class) {
                if (instance == null) {
                    instance = new AppConfigInfo();
                }
            }
        }
        return instance;
    }

    public List<Category> getFamilyUserRelationList() {
        return familyUserRelationList;
    }

    public void setFamilyUserRelationList(List<Category> familyUserRelationList) {
        this.familyUserRelationList = familyUserRelationList;
    }

    public List<Category> getDiseaseNameList() {
        return diseaseNameList;
    }

    public void setDiseaseNameList(List<Category> diseaseNameList) {
        this.diseaseNameList = diseaseNameList;
    }

    public List<Category> getDiagnoseStageList() {
        return diagnoseStageList;
    }

    public void setDiagnoseStageList(List<Category> diagnoseStageList) {
        this.diagnoseStageList = diagnoseStageList;
    }

    public List<VisiablePerson> getVisiablePersonList() {
        return visiablePersonList;
    }

    public void setVisiablePersonList(List<VisiablePerson> visiablePersonList) {
        this.visiablePersonList = visiablePersonList;
    }

    public String getWarmWishes() {
        return warmWishes;
    }

    public void setWarmWishes(String warmWishes) {
        this.warmWishes = warmWishes;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public Tips getSmallTips() {
        return smallTips;
    }

    public void setSmallTips(Tips smallTips) {
        this.smallTips = smallTips;
    }

    public ArrayList<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(ArrayList<Column> columnList) {
        this.columnList = columnList;
    }

    public ArrayList<BannerInfo> getBannerList() {
        return bannerList;
    }

    public void setBannerList(ArrayList<BannerInfo> bannerList) {
        this.bannerList = bannerList;
    }

    public ArrayList<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(ArrayList<Department> departmentList) {
        this.departmentList = departmentList;
    }


    public ArrayList<DoctorRank> getDoctorRankList() {
        return doctorRankList;
    }

    public void setDoctorRankList(ArrayList<DoctorRank> doctorRankList) {
        this.doctorRankList = doctorRankList;
    }
    public String getCustomerServicePhone() {
        return customerServicePhone;
    }

    public void setCustomerServicePhone(String customerServicePhone) {
        this.customerServicePhone = customerServicePhone;
    }

    public LocationEvent getLocationEvent() {
        return locationEvent;
    }

    public void setLocationEvent(LocationEvent locationEvent) {
        this.locationEvent = locationEvent;
    }
}

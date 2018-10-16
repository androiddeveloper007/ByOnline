package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Describe:搜索条件
 * Created by AwenZeng on 2018/7/9.
 */
public class SearchField implements Serializable{
    private String proviceCode = "";
    private String cityCode = "";
    private String departmentsId = "";
    private String diseaseId = "";
    private String position = "";
    private String searchInfo = "";
    private String sortType = "";
    private String areaCode = "";

    public String getProviceCode() {
        return proviceCode;
    }

    public void setProviceCode(String proviceCode) {
        this.proviceCode = proviceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDepartmentsId() {
        return departmentsId;
    }

    public void setDepartmentsId(String departmentsId) {
        this.departmentsId = departmentsId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(String searchInfo) {
        this.searchInfo = searchInfo;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public void clearAll(){
        proviceCode = "";
        areaCode = "";
        cityCode = "";
        departmentsId = "";
        diseaseId = "";
        position = "";
        searchInfo = "";
        sortType = "";
    }
}

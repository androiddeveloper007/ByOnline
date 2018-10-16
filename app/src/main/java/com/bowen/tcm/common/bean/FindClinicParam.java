package com.bowen.tcm.common.bean;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/25.
 */
public class FindClinicParam {

    private String provinceCode;
    private String cityCode;
    private String areaCode;
    private String longitude;
    private String latitude;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public  void clearAll(){
        provinceCode = "";
        cityCode = "";
        areaCode = "";
        longitude = "";
        latitude = "";
    }
}

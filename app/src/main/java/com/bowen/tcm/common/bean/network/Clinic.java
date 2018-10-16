package com.bowen.tcm.common.bean.network;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public class Clinic implements Serializable{

    /**
     * hospitalName :
     * hospitalId :
     * phone :
     * addressStr :
     * hospitalImgUrl :
     * distance :
     */

    private String hospitalName;
    private String hospitalId;
    private String phone;
    private String addressStr;
    private String hospitalImgUrl;
    private int distance;
    private String recommended;
    private double longitudeValue;
    private double latitudeValue;
    private ArrayList<String> imgUrlList;
    private int hospitalSource;


    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public String getHospitalImgUrl() {
        return hospitalImgUrl;
    }

    public void setHospitalImgUrl(String hospitalImgUrl) {
        this.hospitalImgUrl = hospitalImgUrl;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getLongitudeValue() {
        return longitudeValue;
    }

    public void setLongitudeValue(double longitudeValue) {
        this.longitudeValue = longitudeValue;
    }

    public double getLatitudeValue() {
        return latitudeValue;
    }

    public void setLatitudeValue(double latitudeValue) {
        this.latitudeValue = latitudeValue;
    }

    public ArrayList<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(ArrayList<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public int getHospitalSource() {
        return hospitalSource;
    }

    public void setHospitalSource(int hospitalSource) {
        this.hospitalSource = hospitalSource;
    }

    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }
}

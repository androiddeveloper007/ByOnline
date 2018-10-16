package com.bowen.commonlib.event;


import com.bowen.commonlib.base.BaseEvent;

/**
 * Created by AwenZeng on 2016/12/16.
 */

public class LocationEvent extends BaseEvent {
    private String province;
    private String city;
    private String address;
    private String area;
    private String cityCode;
    private String areaCode;
    private double longitude;
    private double latitude;
    public LocationEvent(){
    }

    public LocationEvent(int eventType) {
        super(eventType);
    }

    public LocationEvent(int eventType, Object data) {
        super(eventType, data);
    }

    public LocationEvent(int eventType, String typeString, Object data) {
        super(eventType, typeString, data);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

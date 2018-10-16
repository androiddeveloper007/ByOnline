package com.bowen.tcm.common.bean.location;

import java.util.List;

/**
 * 城市
 */
public class City {

    private String city;

    private String code;

    private List<County> county_list;

    public String getCity() {
        return city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<County> getCounty_list() {
        return county_list;
    }

    public void setCounty_list(List<County> county_list) {
        this.county_list = county_list;
    }
}

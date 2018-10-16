package com.bowen.tcm.common.bean.network;

import java.io.Serializable;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/10.
 */

public class ThirdLogin implements Serializable{

    private String openId;
    private String token;
    private String name;
    private String picUrl;
    private String platfrom;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPlatfrom() {
        return platfrom;
    }

    public void setPlatfrom(String platfrom) {
        this.platfrom = platfrom;
    }
}

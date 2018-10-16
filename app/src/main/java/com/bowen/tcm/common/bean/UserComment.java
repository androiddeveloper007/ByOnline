package com.bowen.tcm.common.bean;

import java.io.Serializable;

/**
 * Created by zzp on 2018/5/15.
 * 医生评价bean
 */

public class UserComment implements Serializable{

    private String img;
    private String name;
    private String time;
    private String comment;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserComment(String img, String name, String time, String comment) {
        this.img = img;
        this.name = name;
        this.time = time;
        this.comment = comment;
    }
}

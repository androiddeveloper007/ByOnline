package com.bowen.tcm.common.bean;

/**
 * Describe:
 * Created by AwenZeng on 2018/6/26.
 */
public class DateNum {
    private int dayType;
    private long dateTime;
    private String week;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getDayType() {
        return dayType;
    }

    public void setDayType(int dayType) {
        this.dayType = dayType;
    }
}

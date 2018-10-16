package com.bowen.tcm.common.bean.network;

import java.util.ArrayList;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/27.
 */
public class HomePageInfo {
    private ArrayList<BannerInfo> bannerlist;
    private ArrayList<Doctor> doctorList;
    private ArrayList<Clinic> hospitalList;
    private ArrayList<NewsTop> topNewsList;
    private ArrayList<News> infoQueryBeanList;
    private ArrayList<News> homeNewsList;

    private Tips infoQueryBean;

    public ArrayList<BannerInfo> getBannerlist() {
        return bannerlist;
    }

    public void setBannerlist(ArrayList<BannerInfo> bannerlist) {
        this.bannerlist = bannerlist;
    }

    public ArrayList<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(ArrayList<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public ArrayList<Clinic> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(ArrayList<Clinic> hospitalList) {
        this.hospitalList = hospitalList;
    }

    public ArrayList<NewsTop> getTopNewsList() {
        return topNewsList;
    }

    public void setTopNewsList(ArrayList<NewsTop> topNewsList) {
        this.topNewsList = topNewsList;
    }

    public ArrayList<News> getInfoQueryBeanList() {
        return infoQueryBeanList;
    }

    public void setInfoQueryBeanList(ArrayList<News> infoQueryBeanList) {
        this.infoQueryBeanList = infoQueryBeanList;
    }

    public Tips getInfoQueryBean() {
        return infoQueryBean;
    }

    public void setInfoQueryBean(Tips infoQueryBean) {
        this.infoQueryBean = infoQueryBean;
    }

    public ArrayList<News> getHomeNewsList() {
        return homeNewsList;
    }

    public void setHomeNewsList(ArrayList<News> homeNewsList) {
        this.homeNewsList = homeNewsList;
    }
}

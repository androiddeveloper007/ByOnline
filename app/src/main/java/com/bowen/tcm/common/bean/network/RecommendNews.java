package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.network.News;
import com.bowen.tcm.common.bean.network.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 * 推荐news
 */

public class RecommendNews implements Serializable {

    private ArrayList<NewsTop> topNewsList;

    private ArrayList<News> infoQueryBeanList;

    private Page page;

    public ArrayList<NewsTop> getTopNewsList() {
        return topNewsList;
    }

    public void setTopNewsList(ArrayList<NewsTop> topNewsList) {
        this.topNewsList = topNewsList;
    }

    public List<News> getInfoQueryBeanList() {
        return infoQueryBeanList;
    }

    public void setInfoQueryBeanList(ArrayList<News> infoQueryBeanList) {
        this.infoQueryBeanList = infoQueryBeanList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

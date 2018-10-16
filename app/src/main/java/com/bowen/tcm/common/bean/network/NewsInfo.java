package com.bowen.tcm.common.bean.network;

import java.util.ArrayList;

/**
 * Describe: 新闻信息
 * Created by AwenZeng on 2018/5/23.
 */
public class NewsInfo {
    private ArrayList<News> newsList;
    private Page page;

    public ArrayList<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(ArrayList<News> newsList) {
        this.newsList = newsList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

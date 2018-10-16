package com.bowen.tcm.common.bean.network;

import java.io.Serializable;
import java.util.List;

/**
 * 栏目News
 *
 * Created by zzp on 2018/5/15.
 */

public class ColumnNews implements Serializable {

    private List<News> infoQueryBeanList;

    private Page page;

    public List<News> getInfoQueryBeanList() {
        return infoQueryBeanList;
    }

    public void setInfoQueryBeanList(List<News> infoQueryBeanList) {
        this.infoQueryBeanList = infoQueryBeanList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}

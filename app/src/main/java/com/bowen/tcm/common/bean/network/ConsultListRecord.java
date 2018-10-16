package com.bowen.tcm.common.bean.network;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:我的咨询
 * Created by zhuzhipeng on 2018/7/9
 */

public class ConsultListRecord implements Serializable{

    private ArrayList<ConsultListItem> consultList;
    private Page page;

    public ArrayList<ConsultListItem> getConsultList() {
        return consultList;
    }

    public void setConsultList(ArrayList<ConsultListItem> list) {
        this.consultList = list;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

}

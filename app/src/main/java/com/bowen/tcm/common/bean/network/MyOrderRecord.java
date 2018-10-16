package com.bowen.tcm.common.bean.network;

import com.bowen.tcm.common.bean.FolkPrescription;
import com.bowen.tcm.common.bean.MyOrder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Describe:我的订单
 * Created by AwenZeng on 2018/4/4.
 */

public class MyOrderRecord implements Serializable{

    private ArrayList<MyOrder> traOrderList;
    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public ArrayList<MyOrder> getTraOrderList() {
        return traOrderList;
    }

    public void setTraOrderList(ArrayList<MyOrder> traOrderList) {
        this.traOrderList = traOrderList;
    }
}

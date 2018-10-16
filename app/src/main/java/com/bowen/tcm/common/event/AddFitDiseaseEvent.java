package com.bowen.tcm.common.event;

import com.bowen.commonlib.base.BaseEvent;
import com.bowen.tcm.common.bean.network.DiseaseInfo;

import java.util.List;

/**
 * Describe:添加病症，从搜索页传递到详情页
 * Created by zhuzhipeng on 2018/6/26.
 */

public class AddFitDiseaseEvent extends BaseEvent {
    private String id;
    private List<DiseaseInfo> list;
    public AddFitDiseaseEvent() {
    }
    public AddFitDiseaseEvent(String id) {
        this.id = id;
    }
    public AddFitDiseaseEvent(List<DiseaseInfo> list) {
        this.list = list;
    }
    public String getId() {
        return id;
    }
    public List<DiseaseInfo> getList() {
        return list;
    }
}
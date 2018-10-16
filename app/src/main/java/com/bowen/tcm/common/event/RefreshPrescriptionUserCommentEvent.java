package com.bowen.tcm.common.event;

import com.bowen.commonlib.base.BaseEvent;

/**
 * Describe:刷新偏方详情页中用户评价列表
 * Created by zhuzhipeng on 2018/6/26.
 */

public class RefreshPrescriptionUserCommentEvent extends BaseEvent {
    private String id;
    public RefreshPrescriptionUserCommentEvent() {
    }
    public RefreshPrescriptionUserCommentEvent(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
}

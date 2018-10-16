package com.bowen.tcm.common.event;

import com.bowen.commonlib.base.BaseEvent;

/**
 * Describe:偏方收藏的状态传递，从详情页传递到上一页列表页
 * Created by zhuzhipeng on 2018/6/26.
 */

public class FolkPrescriptionCollectEvent extends BaseEvent {
    private int position;

    public FolkPrescriptionCollectEvent() {

    }

    public FolkPrescriptionCollectEvent(int pos) {
        this.position = pos;
    }

    public int getPosition() {
        return position;
    }
}

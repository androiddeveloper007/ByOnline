package com.bowen.tcm.common.event;

import com.bowen.commonlib.base.BaseEvent;

/**
 * Describe:
 * Created by AwenZeng on 2018/8/10.
 */
public class PayResultEvent extends BaseEvent{
    private boolean isPaySuccess;

    public PayResultEvent(boolean isPaySuccess) {
        this.isPaySuccess = isPaySuccess;
    }

    public boolean isPaySuccess() {
        return isPaySuccess;
    }
}

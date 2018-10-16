package com.bowen.tcm.common.bean.network;

/**
 * Describe:
 * Created by AwenZeng on 2018/8/9.
 */
public class PayInfo {
    private PayData payResult;
    private String payMsg;

    public PayData getPayResult() {
        return payResult;
    }

    public void setPayResult(PayData payResult) {
        this.payResult = payResult;
    }

    public String getPayMsg() {
        return payMsg;
    }

    public void setPayMsg(String payMsg) {
        this.payMsg = payMsg;
    }
}

package com.bowen.tcm.common.base;

/**
 * Created by AwenZeng on 2016/9/7.
 * Describe:
 */
public abstract  class BasePay {
    public String productId;//产品id
    public String productName;//产品名字
    public String productDes;//产品描述
    public String orderNum;//订单号
    public String notify_url;//回调地址
    public String price;//价格
    public abstract void pay();

}

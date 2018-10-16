package com.bowen.tcm.common.pay;

import android.content.Context;

import com.bowen.tcm.common.bean.network.PayData;


/**
 * Created by AwenZeng on 2016/9/5.
 * Describe:支付管理
 */
public class PayManager {

    private Context mContext;
    private int mPayType;//支付类型
    private PayData mPayData;
    public static final int TYPE_PAYMENT_NO = 0;//无
    public static final int TYPE_PAYMENT_ALIPAY = 1;//支付宝
    public static final int TYPE_PAYMENT_WECHAPAY = 2;//微信支付

    public PayManager(Context context, int payType, PayData payData) {
        mContext = context;
        mPayType = payType;
        mPayData = payData;
    }

    public void pay() {
        switch (mPayType) {
            case TYPE_PAYMENT_WECHAPAY:
                new WechatPay(mContext, mPayData).pay();
                break;
            case TYPE_PAYMENT_ALIPAY:
                new AlipayPay(mContext, mPayData.getOrderInfo()).pay();
                break;
        }
    }

}

package com.bowen.tcm.common.pay;

import android.content.Context;

import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.BuildConfig;
import com.bowen.tcm.common.base.BasePay;
import com.bowen.tcm.common.bean.network.PayData;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Describe:微信支付
 * Created by AwenZeng on 2016/9/7.
 */
public class WechatPay extends BasePay {
    private Context mContext;
    private IWXAPI wxapi;
    private PayData mPayData;

    public WechatPay(Context context,PayData payData) {
        mContext = context;
        wxapi = WXAPIFactory.createWXAPI(mContext, BuildConfig.APPID_WECHAT);
        wxapi.registerApp(BuildConfig.APPID_WECHAT);
        mPayData = payData;
    }

    @Override
    public void pay() {
        boolean sIsWXAppInstalledAndSupported = wxapi.isWXAppInstalled();
        if(!sIsWXAppInstalledAndSupported){
            ToastUtil.getInstance().toast("微信客户端未安装，请确认");
            return;
        }
        PayReq payReq = new PayReq();
        payReq.appId = mPayData.getAppid();
        payReq.partnerId = mPayData.getPartnerid();
        payReq.prepayId = mPayData.getPrepayid();
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = mPayData.getNoncestr();
        payReq.timeStamp = mPayData.getTimestamp();
        payReq.sign = mPayData.getSign();
        wxapi.sendReq(payReq);
    }
}

package com.bowen.tcm.common.pay;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;


import com.alipay.sdk.app.PayTask;
import com.bowen.commonlib.model.encrypt.RSAUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.base.BasePay;
import com.bowen.tcm.common.event.PayResultEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by AwenZeng on 2016/9/7.
 * Describe:支付宝支付
 */
public class AlipayPay extends BasePay {
    private Context mContext;
    private String mOrderInfo;

    public AlipayPay(Context context,String orderInfo) {
        mContext = context;
        mOrderInfo = orderInfo;
    }

    @Override
    public void pay() {
        Observable.just(mOrderInfo).map(new Func1<String, String>() {
            @Override
            public String call(String orderInfo) {
                PayTask alipay = new PayTask((Activity) mContext);
                String result = alipay.pay(orderInfo, true);
                LogUtil.androidLog("payInfo---:" + orderInfo);
                return result;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        payResult(result);
                        LogUtil.androidLog("result---:" + result);
                    }
                });
    }

    /**
     * 开始支付
     */
    private static void payResult(String result) {
        PayResult payResult = new PayResult(result);

        String resultInfo = payResult.getResult();// 同步返回需要验证的信息

        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        LogUtil.androidLog("resultStatus----:" + resultStatus);
        if (TextUtils.equals(resultStatus, "9000")) {
            EventBus.getDefault().post(new PayResultEvent(true));
            ToastUtil.getInstance().toast("支付成功");
        } else {
            EventBus.getDefault().post(new PayResultEvent(false));
            // 判断resultStatus 为非"9000"则代表可能支付失败
            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                ToastUtil.getInstance().toast("支付结果确认中");
            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                ToastUtil.getInstance().toast("支付失败");
            }
        }
    }
}

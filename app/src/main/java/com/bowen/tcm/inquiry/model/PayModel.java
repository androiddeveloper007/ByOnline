package com.bowen.tcm.inquiry.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.commonlib.utils.IpAdressUtils;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.PayInfo;
import com.bowen.tcm.common.bean.network.PayOrderInfo;
import com.bowen.tcm.common.bean.SubmitOrderInfo;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.pay.PayManager;
import com.bowen.tcm.inquiry.activity.PayDetaitActivity;

import java.util.Map;

import static com.bowen.tcm.common.model.Constants.TYPE_PAYMENT_ALIPAY;
import static com.bowen.tcm.common.model.Constants.TYPE_PAYMENT_WECHAPAY;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/20.
 */
public class PayModel extends BaseModel {

    public PayModel(Context mContext) {
        super(mContext);
    }

    /**
     * 创建支付订单
     * @param orderInfo
     * @param mListener
     */
    public void createPayOrder(SubmitOrderInfo orderInfo, final HttpTaskCallBack<PayOrderInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("doctorId", orderInfo.getDoctorId());
        networkRequest.setParam("amount", orderInfo.getPrice());
        networkRequest.setParam("orderType", orderInfo.getOrderType());
        networkRequest.setParam("appointmentId", orderInfo.getAppointmentId());
        networkRequest.setParam("familyId", orderInfo.getFamilyId());
        networkRequest.setParam("caseId", orderInfo.getCaseId());
        networkRequest.setParam("illDesc", orderInfo.getIllDesc());
        networkRequest.requestSRV(HttpContants.CMD_CREATE_PAY_ORDER, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<PayOrderInfo> result = new HttpResult<PayOrderInfo>();
                PayOrderInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), PayOrderInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<PayOrderInfo> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void getPayOrderInfo(int payType,PayOrderInfo payOrderInfo, final HttpTaskCallBack<PayInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("orderType",payOrderInfo.getOrderType()+"");
        if(payType == TYPE_PAYMENT_WECHAPAY){
            networkRequest.setParam("payChannel","WECHAT");
        }else if(payType == TYPE_PAYMENT_ALIPAY){
            networkRequest.setParam("payChannel","ALIPAY");
        }
        networkRequest.setParam("totalAmount",payOrderInfo.getAmount());
        networkRequest.setParam("doctorId",payOrderInfo.getEmrDoctor().getDoctorId());
        networkRequest.setParam("ip", IpAdressUtils.getIpAddress(mContext));
        if(EmptyUtils.isNotEmpty(payOrderInfo.getAppointmentOrderId())){
            networkRequest.setParam("appointmentOrderId",payOrderInfo.getAppointmentOrderId());
        }
        if(EmptyUtils.isNotEmpty(payOrderInfo.getOrderId())){
            networkRequest.setParam("orderId",payOrderInfo.getOrderId());
        }
        networkRequest.requestSRV(HttpContants.CMD_GET_PAY_ORDER_INFO, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<PayInfo> result = new HttpResult<PayInfo>();
                PayInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), PayInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<PayInfo> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }
}

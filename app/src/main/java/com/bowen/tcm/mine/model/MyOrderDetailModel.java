package com.bowen.tcm.mine.model;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.CheckStringUtl;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.commonlib.utils.StringUtil;
import com.bowen.tcm.common.bean.TraOrder;
import com.bowen.tcm.common.bean.network.MyOrderRecord;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describe:
 * Created by zzp on 2018/5/15.
 */

public class MyOrderDetailModel extends BaseModel {

    public MyOrderDetailModel(Context mContext) {
        super(mContext);
    }

    public void getTraOrderById(String orderId, final HttpTaskCallBack<TraOrder> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        if(CheckStringUtl.isAllNumber(orderId)){
            networkRequest.setParam("orderId", orderId);
        }else{
            networkRequest.setParam("appointmentOrderId", orderId);
        }
        networkRequest.requestSRV(HttpContants.CMD_GET_TRA_ORDER_BY_ID, "", new HttpTaskCallBack<TraOrder>() {
            @Override
            public void onSuccess(HttpResult<TraOrder> httpResult) {
                HttpResult<TraOrder> result = new HttpResult<>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObj = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    TraOrder record = GsonUtil.fromJson(jsonObj.getString("traOrder"), TraOrder.class);
                    if (record != null) {
                        result.setData(record);
                    }
                    if (mListener != null) {
                        mListener.onSuccess(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(HttpResult<TraOrder> httpResult) {
                HttpResult<TraOrder> result = new HttpResult<>();
                result.copy(httpResult);
                TraOrder record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), TraOrder.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /***
     * 取消订单
     * @param orderId
     * @param mListener
     */
    public void cancelTraOrder(String orderId, String status, final HttpTaskCallBack<Object> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("orderId", orderId);//意向支付订单id
        networkRequest.setParam("status", status);//预约状态:2取消 ， 6删除
        networkRequest.requestSRV(HttpContants.CMD_CANCEL_TRA_ORDER, TextUtils.equals("2",status) ?"取消订单中":"删除订单中", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Object> result = new HttpResult<>();
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<Object> result = new HttpResult<>();
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }
}

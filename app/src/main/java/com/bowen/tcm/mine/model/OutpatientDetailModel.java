package com.bowen.tcm.mine.model;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.OutpatientDetailRecord;
import com.bowen.tcm.common.bean.TraOrder;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describe:
 * Created by zzp on 2018/5/15.
 */

public class OutpatientDetailModel extends BaseModel {

    public OutpatientDetailModel(Context mContext) {
        super(mContext);
    }

    public void getTraOrderById(String orderId, final HttpTaskCallBack<OutpatientDetailRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("orderId", orderId);
        networkRequest.requestSRV(HttpContants.CMD_GET_TRA_ORDER_BY_ID, "", new HttpTaskCallBack<OutpatientDetailRecord>() {
            @Override
            public void onSuccess(HttpResult<OutpatientDetailRecord> httpResult) {
                HttpResult<OutpatientDetailRecord> result = new HttpResult<>();
                result.copy(httpResult);
                OutpatientDetailRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), OutpatientDetailRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<OutpatientDetailRecord> httpResult) {
                HttpResult<OutpatientDetailRecord> result = new HttpResult<>();
                result.copy(httpResult);
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

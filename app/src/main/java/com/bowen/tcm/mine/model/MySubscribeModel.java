package com.bowen.tcm.mine.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.DoctorFanRecord;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

/**
 * Describe:我的关注
 * Created by zzp on 2018/5/15.
 */

public class MySubscribeModel extends BaseModel {

    public MySubscribeModel(Context mContext) {
        super(mContext);
    }

    /***
     * 用户关注医生列表
     * @param index
     * @param pageSize
     * @param mListener
     */
    public void loadData(int index, int pageSize, final HttpTaskCallBack<DoctorFanRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", index);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.requestSRV(HttpContants.CMD_GET_USER_FEN_PAGE, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<DoctorFanRecord> result = new HttpResult<>();
                result.copy(httpResult);
                DoctorFanRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), DoctorFanRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<DoctorFanRecord> result = new HttpResult<>();
                result.copy(httpResult);
                DoctorFanRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), DoctorFanRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void userFenDoctor(String doctorId, String isFen, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("doctorId", doctorId);
        networkRequest.setParam("isFen", isFen);
        networkRequest.requestSRV(HttpContants.CMD_USER_FEN_DOCTOR, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                if (mListener != null) {
                    mListener.onSuccess(httpResult);
                }
            }
            @Override
            public void onFail(HttpResult<Object> httpResult) {
                if (mListener != null) {
                    mListener.onFail(httpResult);
                }
            }
        });
    }
}
package com.bowen.tcm.mine.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.OutpatientAppointRecord;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

/**
 * Describe:获取我的门诊预约列表
 * Created by zzp on 2018/5/15.
 */

public class OutpatientAppointmentModel extends BaseModel {

    public OutpatientAppointmentModel(Context mContext) {
        super(mContext);
    }

    public void loadData(int index, int pageSize, final HttpTaskCallBack<OutpatientAppointRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", index);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.requestSRV(HttpContants.CMD_GET_EMR_APPOINT_PAGE, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<OutpatientAppointRecord> result = new HttpResult<>();
                result.copy(httpResult);
                OutpatientAppointRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), OutpatientAppointRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<OutpatientAppointRecord> result = new HttpResult<>();
                result.copy(httpResult);
                OutpatientAppointRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), OutpatientAppointRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }
}

package com.bowen.tcm.mine.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.common.bean.network.ConsultListItem;
import com.bowen.tcm.common.bean.network.ConsultListRecord;
import com.bowen.tcm.common.bean.network.OutpatientAppointRecord;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe:我的咨询
 * Created by zzp on 2018/5/15.
 */

public class MyConsultModel extends BaseModel {

    public MyConsultModel(Context mContext) {
        super(mContext);
    }

    public void loadData(int index, int pageSize, final HttpTaskCallBack<ConsultListRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", index);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.requestSRV(HttpContants.CMD_GET_MY_CONSULT, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<ConsultListRecord> result = new HttpResult<>();
                result.copy(httpResult);
                ConsultListRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), ConsultListRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<ConsultListRecord> result = new HttpResult<>();
                result.copy(httpResult);
                if(mListener!=null){
                    mListener.onFail(result);
                }
            }
        });
    }


    /**
     * 删除聊天信息
     * @param sendUserId
     */
    public void removeConsultInfo(String sendUserId,final HttpTaskCallBack<String> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("sendUserId", sendUserId);
        networkRequest.requestSRV(HttpContants.CMD_REMOVE_CONSULT_INFO, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<String> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<String> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }
}

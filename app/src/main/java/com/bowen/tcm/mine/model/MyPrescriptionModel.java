package com.bowen.tcm.mine.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describe:
 * Created by zzp on 2018/5/15.
 */

public class MyPrescriptionModel extends BaseModel {

    public MyPrescriptionModel(Context mContext) {
        super(mContext);
    }

    public void loadData(int index, int pageSize, final HttpTaskCallBack<FolkPrescriptionRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", index);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.requestSRV(HttpContants.CMD_GET_MY_PRESCRIPTION_LIST, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<FolkPrescriptionRecord> result = new HttpResult<>();
                result.copy(httpResult);
                FolkPrescriptionRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), FolkPrescriptionRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<FolkPrescriptionRecord> result = new HttpResult<>();
                result.copy(httpResult);
                FolkPrescriptionRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), FolkPrescriptionRecord.class);
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
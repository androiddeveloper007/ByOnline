package com.bowen.tcm.folkprescription.model;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.InfoFolkPrescription;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describe:
 * Created by zzp on 2018/5/15.
 */

public class AddFolkPrescriptionModel extends BaseModel {

    public AddFolkPrescriptionModel(Context mContext) {
        super(mContext);
    }

    public void loadData(String id, final HttpTaskCallBack<InfoFolkPrescription> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        if (!TextUtils.isEmpty(id)) {
            networkRequest.setParam("prescriptionId", id);
        }
        networkRequest.requestSRV(HttpContants.CMD_GET_INFO_FOLK_PRESCRIPTION, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<InfoFolkPrescription> result = new HttpResult<>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    InfoFolkPrescription info = GsonUtil.fromJson(jsonObject.optString("infoFolkPrescription"), InfoFolkPrescription.class);
                    if (info != null) {
                        result.setData(info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<InfoFolkPrescription> result = new HttpResult<>();
                result.copy(httpResult);
                InfoFolkPrescription info = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), InfoFolkPrescription.class);
                if (info != null) {
                    result.setData(info);
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void uploadPrescription(String prescriptionName, String usageDosage,String applyCrowd,
                                   String applyDisease,final HttpTaskCallBack<Object> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("prescriptionName", prescriptionName);
        networkRequest.setParam("usageDosage", usageDosage);
        networkRequest.setParam("applyCrowd", applyCrowd);
        networkRequest.setParam("applyDisease", applyDisease);
        networkRequest.requestSRV(HttpContants.CMD_ADD_INFO_FOLK_PRESCRIPTION, "上传中..", new HttpTaskCallBack<Object>() {
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

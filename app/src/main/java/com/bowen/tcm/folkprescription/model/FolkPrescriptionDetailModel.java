package com.bowen.tcm.folkprescription.model;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.DoctorPrescriptionComment;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.common.bean.network.InfoFolkPrescription;
import com.bowen.tcm.common.bean.network.PrescriptionDoctorCommentRecord;
import com.bowen.tcm.common.bean.network.PrescriptionUserCommentRecord;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Describe:
 * Created by zzp on 2018/5/15.
 */

public class FolkPrescriptionDetailModel extends BaseModel {

    public FolkPrescriptionDetailModel(Context mContext) {
        super(mContext);
    }

    public void loadData(String id, final HttpTaskCallBack<InfoFolkPrescription> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
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

    public void getUserPrescriptionCommentList(int pageNo, int pageSize, String prescriptionId, final HttpTaskCallBack<PrescriptionUserCommentRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.setParam("prescriptionId", prescriptionId);
        networkRequest.requestSRV(HttpContants.CMD_GET_USER_PRESCRIPTION_COMMENT_LIST, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<PrescriptionUserCommentRecord> result = new HttpResult<>();
                result.copy(httpResult);
                PrescriptionUserCommentRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), PrescriptionUserCommentRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<PrescriptionUserCommentRecord> result = new HttpResult<>();
                result.copy(httpResult);
                PrescriptionUserCommentRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), PrescriptionUserCommentRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void getDoctorPrescriptionCommentList(int pageNo, int pageSize, String prescriptionId, final HttpTaskCallBack<PrescriptionDoctorCommentRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", pageNo);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.setParam("prescriptionId", prescriptionId);
        networkRequest.requestSRV(HttpContants.CMD_GET_DOCTOR_PRESCRIPTION_COMMENT_LIST, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<PrescriptionDoctorCommentRecord> result = new HttpResult<>();
                result.copy(httpResult);
                PrescriptionDoctorCommentRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), PrescriptionDoctorCommentRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<PrescriptionDoctorCommentRecord> result = new HttpResult<>();
                result.copy(httpResult);
                PrescriptionDoctorCommentRecord record = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), PrescriptionDoctorCommentRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void addPrescriptionComment(String prescriptionId, String comment,final HttpTaskCallBack<Object> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("prescriptionId", prescriptionId);
        networkRequest.setParam("comment", comment);
        networkRequest.requestSRV(HttpContants.CMD_ADD_PRESCRIPTION_COMMENT, "提交中..", new HttpTaskCallBack<Object>() {
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

package com.bowen.tcm.folkprescription.model;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.FolkPrescription;
import com.bowen.tcm.common.bean.HospitalDepartments;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Describe:
 * Created by zzp on 2018/5/15.
 */

public class FolkPrescriptionModel extends BaseModel {

    public FolkPrescriptionModel(Context mContext) {
        super(mContext);
    }

    public void loadData(String searchInfo, int index, int pageSize, final HttpTaskCallBack<FolkPrescriptionRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", index);
        networkRequest.setParam("pageSize", pageSize);
        if (!TextUtils.isEmpty(searchInfo)) {
            networkRequest.setParam("searchInfo", searchInfo);//搜索内容
        }
        networkRequest.requestSRV(HttpContants.CMD_GET_FOLK_PRESCRIPTION_LIST, "", new HttpTaskCallBack<Object>() {
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

    /*根据适应人群,所属科室搜索偏方*/
    public void getListByApplyDepartments(String hospitalDepartments, String applyCrowd, int index, int pageSize, final HttpTaskCallBack<FolkPrescriptionRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("hospitalDepartments", hospitalDepartments);
        networkRequest.setParam("applyCrowd", applyCrowd);
        networkRequest.setParam("pageNo", index);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.requestSRV(HttpContants.CMD_GET_LIST_BY_APPLY_DEPARTMENTS, "", new HttpTaskCallBack<Object>() {
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

    public void loadRightDrawerLists(final HttpTaskCallBack<List<HospitalDepartments>> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_SHOW_PRESCRIPTION_SECTION_LIST, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<List<HospitalDepartments>> result = new HttpResult<>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typeList = new TypeToken<List<HospitalDepartments>>() {
                    }.getType();
                    List<HospitalDepartments> beanList = GsonUtil.getGson().fromJson(jsonObject.getString("hospitalDepartmentsList"), typeList);
                    if (beanList != null) {
                        result.setData(beanList);
                    }
                    if (mListener != null) {
                        mListener.onSuccess(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                if (mListener != null) {

                }
            }
        });
    }

    public void loadRightDrawerLists1(final HttpTaskCallBack<List<ShowApplyCrowd>> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_SHOWAPPLYCROWDLIST, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<List<ShowApplyCrowd>> result = new HttpResult<>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typeList = new TypeToken<List<ShowApplyCrowd>>() {
                    }.getType();
                    List<ShowApplyCrowd> beanList = GsonUtil.getGson().fromJson(jsonObject.getString("applyCrowdList"), typeList);
                    if (beanList != null) {
                        result.setData(beanList);
                    }
                    if (mListener != null) {
                        mListener.onSuccess(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                if (mListener != null) {

                }
            }
        });
    }

    /***
     * 收藏偏方
     * @param mListener
     */
    public void folkPrescriptionCollectOrNot(boolean isCollect, String prescriptionId, final HttpTaskCallBack<String> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("prescriptionId", prescriptionId);
        networkRequest.requestSRV( isCollect ? HttpContants.CMD_ADD_INFO_USER_PRESCRIPTION :
                        HttpContants.CMD_DELETE_INFO_USER_PRESCRIPTION, "",new HttpTaskCallBack<Object>() {
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

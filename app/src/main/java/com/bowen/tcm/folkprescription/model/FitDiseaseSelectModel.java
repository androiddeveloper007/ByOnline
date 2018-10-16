package com.bowen.tcm.folkprescription.model;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.DiseaseInfo;
import com.bowen.tcm.common.bean.network.DiseaseInfoRecord;
import com.bowen.tcm.common.bean.network.InfoFolkPrescription;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Describe:模糊搜索常见疾病
 * Created by zzp on 2018/5/15.
 */

public class FitDiseaseSelectModel extends BaseModel {

    public FitDiseaseSelectModel(Context mContext) {
        super(mContext);
    }

    public void loadDataByStr(String id, int page, int pageSize, final HttpTaskCallBack<DiseaseInfoRecord> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("pageNo", page);
        networkRequest.setParam("pageSize", pageSize);
        if (!TextUtils.isEmpty(id)) {
            networkRequest.setParam("diseaseName", id);
        }
        networkRequest.requestSRV(HttpContants.CMD_LIKE_INFO_DISEASE_DEPT, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<DiseaseInfoRecord> result = new HttpResult<>();
                result.copy(httpResult);
                DiseaseInfoRecord record = GsonUtil.getGson().fromJson(GsonUtil.toJson(httpResult.getData()), DiseaseInfoRecord.class);
                if (record != null) {
                    result.setData(record);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<DiseaseInfoRecord> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }
}

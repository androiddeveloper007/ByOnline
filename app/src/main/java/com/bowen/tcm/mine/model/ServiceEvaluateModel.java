package com.bowen.tcm.mine.model;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.BannerInfo;
import com.bowen.tcm.common.bean.network.MyOrderRecord;
import com.bowen.tcm.common.bean.network.VoListBean;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.AppConfigInfo;
import com.bowen.tcm.common.model.UserInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * Created by zzp on 2018/5/15.
 */

public class ServiceEvaluateModel extends BaseModel {

    public ServiceEvaluateModel(Context mContext) {
        super(mContext);
    }

    public void userEvaluateVoList(final HttpTaskCallBack<List<VoListBean>> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_USER_EVALUATE_VO_LIST, "", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<List<VoListBean>> result = new HttpResult<>();
                result.copy(httpResult);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    Type typelist = new TypeToken<List<VoListBean>>() {
                    }.getType();
                    List<VoListBean> voList = GsonUtil.getGson().fromJson(jsonObject.getString("voList"), typelist);
                    if (voList != null) {
                        result.setData(voList);
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
                HttpResult<List<VoListBean>> result = new HttpResult<>();
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void saveTraOrderById(String orderId, double score, double doctorAttitude,
                                 double replySpeed, double serviceLevel, String userEvaluate,
                                 final HttpTaskCallBack<Object> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("orderId", orderId);
        networkRequest.setParam("score", score);
        networkRequest.setParam("doctorAttitude", doctorAttitude);
        networkRequest.setParam("replySpeed", replySpeed);
        networkRequest.setParam("serviceLevel", serviceLevel);
        networkRequest.setParam("userEvaluate", userEvaluate);
        networkRequest.requestSRV(HttpContants.CMD_SAVE_TRA_ORDER_BY_ID, "正在提交数据", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Object> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<Object> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }
}

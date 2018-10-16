package com.bowen.tcm.inquiry.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.ClinicDetailInfo;
import com.bowen.tcm.common.bean.network.HospitalDetailInfo;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

/**
 * Describe:根据省市区搜索医馆分页(用户端)
 * Created by zhuzhipeng on 2018/7/9.
 */
public class HospitalDetailModel extends BaseModel {

    public HospitalDetailModel(Context mContext) {
        super(mContext);
    }

    public void getHospitalDetail(String hospitalId, int page, int pageSize, final HttpTaskCallBack<HospitalDetailInfo> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("hospitalId", hospitalId);
        networkRequest.setParam("page", page);
        networkRequest.setParam("pageSize", pageSize);
        networkRequest.requestSRV(HttpContants.CMD_GET_HOSPITAL_DETAIL, new HttpTaskCallBack<HospitalDetailInfo>() {
            @Override
            public void onSuccess(HttpResult<HospitalDetailInfo> httpResult) {
                HttpResult<HospitalDetailInfo> result = new HttpResult<>();
                result.copy(httpResult);
                HospitalDetailInfo data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), HospitalDetailInfo.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<HospitalDetailInfo> httpResult) {
                HttpResult<HospitalDetailInfo> result = new HttpResult<>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }
}

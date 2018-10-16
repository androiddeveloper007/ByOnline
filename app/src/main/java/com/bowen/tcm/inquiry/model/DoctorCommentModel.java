package com.bowen.tcm.inquiry.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.bean.network.UserDoctorComment;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.UserInfo;

/**
 * Describe:评论Model
 * Created by AwenZeng on 2018/7/17.
 */
public class DoctorCommentModel extends BaseModel {
    public DoctorCommentModel(Context mContext) {
        super(mContext);
    }

    /**
     * 获取医生评论列表
     */
    public void getDoctorCommentList(String doctorId,final HttpTaskCallBack<UserDoctorComment> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.setParam("doctorId", doctorId);
        networkRequest.requestSRV(HttpContants.CMD_GET_DOCTOR_COMMENT_LIST, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<UserDoctorComment> result = new HttpResult<>();
                result.copy(httpResult);
                UserDoctorComment data = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), UserDoctorComment.class);
                if (data != null) {
                    result.setData(data);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }

        }

        @Override
        public void onFail(HttpResult<Object> httpResult) {
            HttpResult<UserDoctorComment> result = new HttpResult<UserDoctorComment>();
            result.copy(httpResult);
            if (mListener != null) {
                mListener.onFail(result);
            }
        }
    });
    }
}

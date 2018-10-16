package com.bowen.tcm.login.model;

import android.content.Context;

import com.bowen.commonlib.http.BaseNetworkRequest;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;

/**
 * Created by AwenZeng on 2016/12/24.
 */

public class ForgetPasswordModel {
    private Context mContext;

    public ForgetPasswordModel(Context context) {
        mContext = context;
    }

    /**
     * 找回登录密码
     *
     * @param phoneNum
     * @param mListener
     */
    public void findSetPassword(String phoneNum, String authcode,String password, final HttpTaskCallBack mListener) {
        NetworkRequest request = new NetworkRequest(mContext);
        request.setParam("userMobile", phoneNum);
        request.setParam("password", password);
        request.setParam("verifyCode", authcode);
        request.requestSRV(HttpContants.CMD_FIND_SET_PASSWORD, new HttpTaskCallBack<Object>() {
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

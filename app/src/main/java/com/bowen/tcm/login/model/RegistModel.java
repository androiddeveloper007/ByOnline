package com.bowen.tcm.login.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AwenZeng on 2017/6/1.
 */

public class RegistModel extends BaseModel {

    public static final int AUTHCODE_MESSAGE = 0;//短信
    public static final int AUTHCODE_SOUND = 1;//语音

    public RegistModel(Context mContext) {
        super(mContext);
    }

    /**
     * 注册
     *
     * @param userAcount
     * @param userPassword
     * @param mAuthCode
     * @param mListener
     */
    public void regist(String userAcount, String userPassword, String mAuthCode, final HttpTaskCallBack mListener) {
        NetworkRequest request = new NetworkRequest(mContext);
        request.setParam("userMobile", userAcount);
//        request.setParam("userPassword", RSAUtil.getInstance().encode(userPassword));
        request.setParam("userPassword", userPassword);
        request.setParam("verifyCode", mAuthCode);
        request.setParam("userType", "0");//0:普通用户 1: 医生
        request.requestSRV(HttpContants.CMD_REGIST, "正在注册,请稍后...", new HttpTaskCallBack<Object>() {
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

    /**
     * 校验注册账号
     *
     * @param userAcount 手机号/wxOpenid/qqOpenid
     * @param checkType 0:后台 1:微信 2:QQ
     * @param mListener
     */
    public void checkAccount(String userAcount,String checkType,final HttpTaskCallBack<Boolean> mListener) {
        NetworkRequest request = new NetworkRequest(mContext);
        request.setParam("userAccount", userAcount);
        request.setParam("checkType", checkType);
        request.setParam("userType", "0");//0:普通用户 1: 医生
        request.requestSRV(HttpContants.CMD_CHECK_ACCOUNT, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Boolean> result = new HttpResult<Boolean>();
                result.copy(httpResult);
                try{
                    JSONObject jsonObject = new JSONObject(GsonUtil.toJson(httpResult.getData()));
                    boolean isRegist = jsonObject.optBoolean("isReg",false);
                    result.setData(isRegist);
                    if (mListener != null) {
                        mListener.onSuccess(result);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    if (mListener != null) {
                        mListener.onFail(result);
                    }
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                HttpResult<Boolean> result = new HttpResult<Boolean>();
                result.copy(httpResult);
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    /**
     * 通过手机号获取验证码
     *
     * @param mPhoneNum
     * @param codeType     验证码类型：0语音,1短信
     * @param businessType 业务类型：0注册,1修改密码,2找回密码,3设置交易密码,4.找回交易密码
     * @param mListener
     */
    public void getAuthCode(String mPhoneNum, int codeType, int businessType, final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("codeType", codeType);
        networkRequest.setParam("businessType", businessType);
        networkRequest.setParam("userMobile", mPhoneNum);
        networkRequest.requestSRV(HttpContants.CMD_GET_AUTHCODE, "验证码获取中,请稍后...", new HttpTaskCallBack<Object>() {
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

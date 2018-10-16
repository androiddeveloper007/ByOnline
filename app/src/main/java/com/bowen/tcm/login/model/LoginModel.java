package com.bowen.tcm.login.model;

import android.content.Context;

import com.bowen.commonlib.base.BaseModel;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.DeviceUtil;
import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.GsonUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.Login;
import com.bowen.tcm.common.bean.network.ThirdLogin;
import com.bowen.tcm.common.http.HttpContants;
import com.bowen.tcm.common.http.NetworkRequest;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.inquiry.model.chat.ChatModel;
import com.bowen.tcm.inquiry.model.chat.ChatServerManager;
import com.bowen.tcm.inquiry.model.chat.ChatServerManager.ChatServerLoginListener;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

/**
 * Describe:
 * Created by AwenZeng on 2018/3/19.
 */

public class LoginModel extends BaseModel {


    public LoginModel(Context mContext) {
        super(mContext);
    }


    public void login(final String userAcount, String userPassword, final HttpTaskCallBack<Login> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("userMobile", userAcount);
//        networkRequest.setParam("userPassword", RSAUtil.getInstance().encode(userPassword));
        networkRequest.setParam("userPassword", userPassword);
        networkRequest.setParam("userType", 0);
        networkRequest.setParam("phoneMode", DeviceUtil.getModel());
        networkRequest.setParam("phoneName", DeviceUtil.getBrand());
        networkRequest.setParam("userType", "0");//0:普通用户 1: 医生
        networkRequest.requestSRV(HttpContants.CMD_LOGIN, "正在登录,请稍后...", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Login> result = new HttpResult<Login>();
                result.copy(httpResult);
                Login loginBean = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Login.class);
                if (loginBean != null) {
                    result.setData(loginBean);
                    setLoginData(loginBean);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                Login loginBean = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Login.class);
                HttpResult<Login> result = new HttpResult<Login>();
                result.copy(httpResult);
                if (loginBean != null) {
                    result.setData(loginBean);
                    LoginStatusUtil.getInstance().setLoginErroCount(loginBean.getFailCount());
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }


    public void loginAuthCode(final String userAcount, String verifyCode, final HttpTaskCallBack<Login> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("userMobile", userAcount);
        networkRequest.setParam("verifyCode", verifyCode);
        networkRequest.setParam("userType", 0);
        networkRequest.setParam("phoneMode", DeviceUtil.getModel());
        networkRequest.setParam("phoneName", DeviceUtil.getBrand());
        networkRequest.requestSRV(HttpContants.CMD_AUTHCODE_LOGIN, "正在登录,请稍后...", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Login> result = new HttpResult<Login>();
                result.copy(httpResult);
                Login loginBean = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Login.class);
                if (loginBean != null) {
                    result.setData(loginBean);
                    setLoginData(loginBean);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                Login loginBean = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Login.class);
                HttpResult<Login> result = new HttpResult<Login>();
                result.copy(httpResult);
                if (loginBean != null) {
                    result.setData(loginBean);
                    LoginStatusUtil.getInstance().setLoginErroCount(loginBean.getFailCount());
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void thirdLogin(int loginType, final String accessToken, String openId, final HttpTaskCallBack<Login> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("loginType", loginType);
        networkRequest.setParam("accessToken", accessToken);
        networkRequest.setParam("openid", openId);
        networkRequest.setParam("phoneMode", DeviceUtil.getModel());
        networkRequest.setParam("phoneName", DeviceUtil.getBrand());
        networkRequest.setParam("userType", "0");//0:普通用户 1: 医生
        networkRequest.requestSRV(HttpContants.CMD_THIRD_LOGIN, "正在登录,请稍后...", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Login> result = new HttpResult<Login>();
                result.copy(httpResult);
                Login loginBean = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Login.class);
                if (loginBean != null) {
                    result.setData(loginBean);
                    setLoginData(loginBean);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                Login loginBean = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Login.class);
                HttpResult<Login> result = new HttpResult<Login>();
                result.copy(httpResult);
                if (loginBean != null) {
                    result.setData(loginBean);
                    LoginStatusUtil.getInstance().setLoginErroCount(loginBean.getFailCount());
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }

    public void quickLogin(String token, final HttpTaskCallBack<Login> mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", token);
        networkRequest.setParam("userType", 0);
        networkRequest.setParam("phoneMode", DeviceUtil.getModel());
        networkRequest.setParam("phoneName", DeviceUtil.getBrand());
        networkRequest.requestSRV(HttpContants.CMD_QUICK_LOGIN, "正在登录,请稍后...", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Login> result = new HttpResult<Login>();
                result.copy(httpResult);
                Login loginBean = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Login.class);
                if (loginBean != null) {
                    result.setData(loginBean);
                    setLoginData(loginBean);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                Login loginBean = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Login.class);
                HttpResult<Login> result = new HttpResult<Login>();
                result.copy(httpResult);
                if (loginBean != null) {
                    result.setData(loginBean);
                    LoginStatusUtil.getInstance().setLoginErroCount(loginBean.getFailCount());
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }


    private void setLoginData(Login loginBean) {
        UserInfo.getInstance().setToken(loginBean.getToken());
        UserInfo.getInstance().setUserId(loginBean.getUserId());
        UserInfo.getInstance().setUserLoginName(loginBean.getUserLoginName());
        UserInfo.getInstance().setUserMobile(loginBean.getUserMobile());
        UserInfo.getInstance().setPicUrl(loginBean.getPicUrl());
        UserInfo.getInstance().setUserNickname(loginBean.getUserNickname());
        UserInfo.getInstance().setFamilyId(loginBean.getFamilyId());
        UserInfo.getInstance().setBindPhone(true);
        UserInfo.getInstance().setSex(loginBean.getSex());
        UserInfo.getInstance().setBirthday(loginBean.getBirthday());
        LoginStatusUtil.getInstance().setLogin(true);
        DataCacheUtil.getInstance().putBoolean(DataCacheUtil.LOGIN_AGAIN, true);
        DataCacheUtil.getInstance().putString(DataCacheUtil.LOGIN_ACCOUNT, loginBean.getUserMobile());
        DataCacheUtil.getInstance().putString(DataCacheUtil.LOGIN_TOKEN, loginBean.getToken());

        if (loginBean.isHasUndoneOrder()) {//有咨询单，就登录聊天服务器
            //登录聊天服务器
            loginChatServer();
        }
    }


    public void loginOut(final HttpTaskCallBack mListener) {
        NetworkRequest networkRequest = new NetworkRequest(mContext);
        networkRequest.setParam("token", UserInfo.getInstance().getToken());
        networkRequest.setParam("userId", UserInfo.getInstance().getUserId());
        networkRequest.requestSRV(HttpContants.CMD_LOGIN_OUT, "正在退出,请稍后...", new HttpTaskCallBack<Object>() {
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
     * platform有：Wechat.NAME、QQ.NAME、SinaWeibo.NAME
     *
     * @param platformType
     */
    public void login(final String platformType, final HttpTaskCallBack<ThirdLogin> mListener) {
        final Platform platfrom = ShareSDK.getPlatform(platformType);
        if (platfrom.isClientValid()) {
            //客户端可用
        }

        if (platfrom.isAuthValid()) {
            platfrom.removeAccount(true);
        }

        platfrom.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
                PlatformDb db = platform.getDb();
                ThirdLogin login = new ThirdLogin();
                login.setName(db.getUserName());
                login.setOpenId(db.getUserId());
                login.setPicUrl(db.getUserIcon());
                login.setToken(db.getToken());
                login.setPlatfrom(platformType);
                HttpResult<ThirdLogin> httpResult = new HttpResult<>();
                if (EmptyUtils.isNotEmpty(login)) {
                    httpResult.setData(login);
                    if (mListener != null) {
                        mListener.onSuccess(httpResult);
                    }
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                HttpResult<ThirdLogin> httpResult = new HttpResult<>();
                if (mListener != null) {
                    mListener.onSuccess(httpResult);
                }
                ToastUtil.getInstance().toast("登录失败：" + throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
        platfrom.SSOSetting(false);
        platfrom.showUser(null);
    }

    /**
     * 注册并登陆
     *
     * @param userAcount
     * @param userPassword
     * @param mAuthCode
     * @param mListener
     */
    public void registAndLogin(String userAcount, String userPassword, String mAuthCode, final HttpTaskCallBack mListener) {
        ThirdLogin login = LoginStatusUtil.getInstance().getThirdLogin();
        NetworkRequest request = new NetworkRequest(mContext);
        request.setParam("userMobile", userAcount);
//        request.setParam("userPassword", RSAUtil.getInstance().encode(userPassword));
        request.setParam("userPassword", userPassword);
        request.setParam("verifyCode", mAuthCode);
        if (login.getPlatfrom().equals(QQ.NAME)) {
            request.setParam("loginType", Constants.TYPE_LOGIN_ACCOUNT_QQ);
        } else {
            request.setParam("loginType", Constants.TYPE_LOGIN_ACCOUNT_WECHAT);
        }
        request.setParam("accessToken", login.getToken());
        request.setParam("openid", login.getOpenId());
        request.setParam("userType", 0);
        request.setParam("picUrl", UserInfo.getInstance().getPicUrl());
        request.setParam("userNickname", UserInfo.getInstance().getUserNickname());
        request.setParam("phoneMode", DeviceUtil.getModel());
        request.setParam("phoneName", DeviceUtil.getBrand());
        request.setParam("userType", "0");//0:普通用户 1: 医生
        request.requestSRV(HttpContants.CMD_REGIST_AND_LOGIN, "正在验证,请稍后...", new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                HttpResult<Login> result = new HttpResult<Login>();
                result.copy(httpResult);
                Login loginBean = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Login.class);
                if (loginBean != null) {
                    result.setData(loginBean);
                    setLoginData(loginBean);
                }
                if (mListener != null) {
                    mListener.onSuccess(result);
                }
            }

            @Override
            public void onFail(HttpResult<Object> httpResult) {
                Login loginBean = GsonUtil.fromJson(GsonUtil.toJson(httpResult.getData()), Login.class);
                HttpResult<Login> result = new HttpResult<Login>();
                result.copy(httpResult);
                if (loginBean != null) {
                    result.setData(loginBean);
                    LoginStatusUtil.getInstance().setLoginErroCount(loginBean.getFailCount());
                }
                if (mListener != null) {
                    mListener.onFail(result);
                }
            }
        });
    }


    private void loginChatServer() {
        if(!UserInfo.getInstance().isChatServerLoginSuccess()){
            ChatServerManager.login(new ChatServerLoginListener() {
                @Override
                public void backLoginSucessStatus(boolean isSuccess) {
                    if (isSuccess) {
                        ChatModel chatModel = new ChatModel(mContext);
                        chatModel.addChatListener();
                    }
                }
            });
        }
    }
}

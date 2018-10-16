package com.bowen.tcm.login.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.DataCacheUtil;
import com.bowen.commonlib.utils.LogUtil;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.Login;
import com.bowen.tcm.common.bean.network.ThirdLogin;
import com.bowen.tcm.common.model.Constants;
import com.bowen.tcm.common.model.UserInfo;
import com.bowen.tcm.common.util.CheckFieldUtil;
import com.bowen.tcm.common.util.LoginStatusUtil;
import com.bowen.tcm.login.activity.LoginActivity;
import com.bowen.tcm.login.contract.LoginContract;
import com.bowen.tcm.login.model.LoginModel;
import com.bowen.tcm.login.model.RegistModel;
import com.bowen.tcm.healthcare.model.ChooseColumnsModel;

/**
 * Created by AwenZeng on 2017/5/27.
 */

public class LoginPresenter extends BasePresenter implements LoginContract.Presenter {
    private LoginModel mLoginModel;
    private RegistModel mRegistModel;
    private LoginContract.View mView;

    public LoginPresenter(Context context, LoginContract.View view) {
        super(context);
        mLoginModel = new LoginModel(context);
        mRegistModel = new RegistModel(context);
        mView = view;
    }
    /**
     * 检测输入的字段
     *
     * @param phoneNum
     * @param password
     * @return
     */
    public boolean checkContent(int type,String phoneNum, String password) {

        if (!CheckFieldUtil.checkPhoneNum(phoneNum)) {
            return false;
        }
        if(type == LoginActivity.TYPE_AUTHCODE_LOGIN){
            if (!CheckFieldUtil.checkAuthCode(password)) {
                return false;
            }
        }else{
            if (!CheckFieldUtil.checkPassword(password)) {
                return false;
            }
        }
        return true;
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }

    @Override
    public void login(int loginType,String phone, String password) {
        if(loginType == LoginActivity.TYPE_AUTHCODE_LOGIN){
            mLoginModel.loginAuthCode(phone, password, new HttpTaskCallBack<Login>() {
                @Override
                public void onSuccess(HttpResult<Login> result) {
                    mView.onLoginSuccess();
                }

                @Override
                public void onFail(HttpResult<Login> result) {
                    showToast(result.getMsg());
                }
            });
        }else{
            mLoginModel.login(phone, password, new HttpTaskCallBack<Login>() {
                @Override
                public void onSuccess(HttpResult<Login> result) {
                    mView.onLoginSuccess();
                }

                @Override
                public void onFail(HttpResult<Login> result) {
                    showToast(result.getMsg());
                }
            });
        }
    }

    @Override
    public void login(String platform) {
        mLoginModel.login(platform, new HttpTaskCallBack<ThirdLogin>() {
            @Override
            public void onSuccess(HttpResult<ThirdLogin> result) {
                 final ThirdLogin login = result.getData();
                UserInfo.getInstance().setUserNickname(login.getName());
                UserInfo.getInstance().setPicUrl(login.getPicUrl());
                mView.onGetThirdDataSuccess(login);
            }

            @Override
            public void onFail(HttpResult<ThirdLogin> result) {

            }
        });
    }

    public void thirdLogin(final ThirdLogin login, final String type){
            mRegistModel.checkAccount(login.getOpenId(), type, new HttpTaskCallBack<Boolean>() {
                @Override
                public void onSuccess(HttpResult<Boolean> result) {
                    if(result.getData()){
                        int tempType;
                        if(type.equals(Constants.TYPE_CHECK_ACCOUNT_QQ)){
                            tempType = Constants.TYPE_LOGIN_ACCOUNT_QQ;
                        }else{
                            tempType = Constants.TYPE_LOGIN_ACCOUNT_WECHAT;
                        }
                        mLoginModel.thirdLogin(tempType, login.getToken(), login.getOpenId(), new HttpTaskCallBack<Login>() {
                            @Override
                            public void onSuccess(HttpResult<Login> result) {
                                mView.onLoginSuccess();
                            }

                            @Override
                            public void onFail(HttpResult<Login> result) {

                            }
                        });
                    }else{
                        LoginStatusUtil.getInstance().setLogin(true);
                        LoginStatusUtil.getInstance().setPlatfrom(login.getPlatfrom());
                        LoginStatusUtil.getInstance().setThirdLogin(login);
                        UserInfo.getInstance().setBindPhone(false);
                        mView.onThirdLoginSuccess();
                    }
                }

                @Override
                public void onFail(HttpResult<Boolean> result) {

                }
            });
    }


    @Override
    public void getAuthCode(String phone, int codeType, int businessType) {
        mRegistModel.getAuthCode(phone, codeType, businessType, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                showToast(result.getMsg());
            }

            @Override
            public void onFail(HttpResult result) {
                showToast(result.getMsg());
                mView.onGetAuthCodeFailed();
            }
        });
    }

    @Override
    public void checkAccount(String account,String checkType) {
        mRegistModel.checkAccount(account,checkType, new HttpTaskCallBack<Boolean>() {
            @Override
            public void onSuccess(HttpResult<Boolean> result) {
                mView.onCheckAccountSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<Boolean> result) {

            }
        });
    }
}

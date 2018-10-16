package com.bowen.tcm.login.contract;

import com.bowen.tcm.common.bean.network.ThirdLogin;

/**
 * Created by AwenZeng on 2017/5/27.
 */

public interface LoginContract {
    interface View {
        void onLoginSuccess();
        void onThirdLoginSuccess();
        void onGetThirdDataSuccess(ThirdLogin login);
        void onGetAuthCodeFailed();
        void onCheckAccountSuccess(boolean isRegist);
    }

    interface Presenter{
        void login(int loginType,String phone, String password);
        void login(String platform);
        void getAuthCode(String phone, int codeType, int businessType);
        void checkAccount(String account,String checkType);
    }
}

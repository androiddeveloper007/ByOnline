package com.bowen.tcm.login.contract;

/**
 * Created by AwenZeng on 2017/6/2.
 */

public interface ForgetPasswordContract {
    interface View {
        void onFindPswSuccess();
        void onGetAuthCodeFailed();
        void onCheckAccountSuccess(boolean isRegist);
    }

    interface Presenter{
        void findSetPassword(String phone, String authCode,String password);
        void getAuthCode(String phone, int codeType, int businessType);
        void checkAccount(String account,String checkType);
    }
}

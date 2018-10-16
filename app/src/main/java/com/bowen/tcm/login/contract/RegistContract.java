package com.bowen.tcm.login.contract;

/**
 * Created by AwenZeng on 2017/6/1.
 */

public interface RegistContract {
    interface View {
        void onCheckAccountSuccess(boolean isRegist);
        void onRegistSuccess();
        void onGetAuthCodeFailed();
    }

    interface Presenter{
        void regist(String phone, String password, String authCode);
        void getAuthCode(String phone, int codeType, int businessType);
        void checkAccount(String account,String checkType);
    }
}

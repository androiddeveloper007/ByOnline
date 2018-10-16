package com.bowen.tcm.login.contract;

/**
 * Created by AwenZeng on 2017/6/1.
 */

public interface VerifiPhoneContract {
    interface View {
        void onVerifiSuccess();
        void onGetAuthCodeFailed();
    }

    interface Presenter{
        void regist(String phone, String password, String authCode);
        void getAuthCode(String phone, int codeType, int businessType);
    }
}

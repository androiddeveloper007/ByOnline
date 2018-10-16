package com.bowen.tcm.login.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.util.CheckFieldUtil;
import com.bowen.tcm.login.contract.RegistContract;
import com.bowen.tcm.login.contract.VerifiPhoneContract;
import com.bowen.tcm.login.model.LoginModel;
import com.bowen.tcm.login.model.RegistModel;

/**
 * Describe:
 * Created by AwenZeng on 2018/4/10.
 */

public class VerifiPhonePresenter extends BasePresenter implements VerifiPhoneContract.Presenter{

    private RegistModel mRegistModel;
    private LoginModel mLoginModel;
    private VerifiPhoneContract.View mView;

    public VerifiPhonePresenter(Context mContext,VerifiPhoneContract.View view) {
        super(mContext);
        mRegistModel = new RegistModel(mContext);
        mLoginModel = new LoginModel(mContext);
        mView = view;
    }

    @Override
    public void regist(String phone, String password, String authCode) {
        mLoginModel.registAndLogin(phone, password, authCode, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onVerifiSuccess();
            }

            @Override
            public void onFail(HttpResult result) {
                showToast(result.getMsg());
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

    /**
     * 检测输入的字段
     *
     * @param password
     * @return
     */
    public boolean checkContent(String password, String authCode,boolean isChooseProtocol) {
        if (!CheckFieldUtil.checkAuthCode(authCode)) {
            return false;
        }
        if (!CheckFieldUtil.checkPassword(password)) {
            return false;
        }
        if(!isChooseProtocol){
            showToast("请选择我已阅读并同意博闻《服务条款》");
            return false;
        }
        return true;
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }

}

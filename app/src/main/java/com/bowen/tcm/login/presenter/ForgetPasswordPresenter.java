package com.bowen.tcm.login.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.CheckStringUtl;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.login.contract.ForgetPasswordContract;
import com.bowen.tcm.login.model.ForgetPasswordModel;
import com.bowen.tcm.login.model.RegistModel;

/**
 * Created by AwenZeng on 2017/6/2.
 */

public class ForgetPasswordPresenter extends BasePresenter implements ForgetPasswordContract.Presenter {
    private ForgetPasswordModel mPasswordModel;
    private RegistModel mRegistModel;
    private ForgetPasswordContract.View mView;
    public ForgetPasswordPresenter(Context mContext, ForgetPasswordContract.View view) {
        super(mContext);
        mPasswordModel = new ForgetPasswordModel(mContext);
        mRegistModel = new RegistModel(mContext);
        mView = view;
    }


    /**
     * 检测输入的字段
     *
     * @param phoneNum
     * @return
     */
    public boolean checkContent(String phoneNum, String authCode,String password) {
        if (TextUtils.isEmpty(phoneNum)) {
            showToast("请输入手机号码");
            return false;
        } else if (!CheckStringUtl.isMobileNum(phoneNum)) {
            showToast("请输入11位数字的手机号码");
            return false;
        }
        if (TextUtils.isEmpty(authCode)) {
            showToast("请输入验证码");
            return false;
        }else if(CheckStringUtl.isAuthCode(authCode)){
            showToast("请输入正确格式的验证码");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("请输入登录密码");
            return false;
        } else if (!CheckStringUtl.isPassword(password)) {
            showToast("请输入正确格式的登录密码");
            return false;
        }
        return true;
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }

    @Override
    public void findSetPassword(String phone, String authCode, String password) {
        mPasswordModel.findSetPassword(phone, authCode,password, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.onFindPswSuccess();
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

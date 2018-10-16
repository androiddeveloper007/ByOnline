package com.bowen.tcm.login.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.CheckStringUtl;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.util.CheckFieldUtil;
import com.bowen.tcm.login.contract.RegistContract;
import com.bowen.tcm.login.model.RegistModel;

/**
 * Created by AwenZeng on 2017/6/1.
 */

public class RegistPresenter extends BasePresenter implements RegistContract.Presenter {

    private RegistModel mRegistModel;
    private RegistContract.View mView;


    public RegistPresenter(Context context, RegistContract.View view) {
        super(context);
        mRegistModel = new RegistModel(mContext);
        mView = view;
    }


    /**
     * 检测输入的字段
     *
     * @param phoneNum
     * @param password
     * @return
     */
    public boolean checkContent(String phoneNum, String password, String authCode,boolean isChooseProtocol) {
        if (!CheckFieldUtil.checkPhoneNum(phoneNum)) {
            return false;
        }
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

    @Override
    public void regist(String phone, String password, String authCode) {
       mRegistModel.regist(phone, password, authCode, new HttpTaskCallBack() {
           @Override
           public void onSuccess(HttpResult result) {
               mView.onRegistSuccess();
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
}

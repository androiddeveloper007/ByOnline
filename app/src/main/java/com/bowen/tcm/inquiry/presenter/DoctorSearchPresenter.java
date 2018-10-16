package com.bowen.tcm.inquiry.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.inquiry.contract.DoctorSearchContract;
import com.bowen.tcm.inquiry.model.DoctorSearchModel;

import java.util.List;

/**
 * 名医搜索的数据提供者
 * Created by zzp on 2017/5/21.
 */

public class DoctorSearchPresenter extends BasePresenter {
    private DoctorSearchModel famousDoctorSearchModel;
    private DoctorSearchContract.View mView;

    public DoctorSearchPresenter(Context context, DoctorSearchContract.View view) {
        super(context);
        famousDoctorSearchModel = new DoctorSearchModel(context);
        mContext = context;
        mView = view;
    }

    /**
     * 获取热搜
     */
    public void showHotSearchList() {
        famousDoctorSearchModel.showHotSearchList(new HttpTaskCallBack<List<String>>() {
            @Override
            public void onSuccess(HttpResult<List<String>> result) {
                mView.onHotSearchLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<List<String>> result) {
                showToast(result.getMsg());
            }
        });
    }

    /**
     * 获取搜索记录
     */
    public void showUserSearchLog() {
        famousDoctorSearchModel.showUserSerachLog(new HttpTaskCallBack<List<String>>() {
            @Override
            public void onSuccess(HttpResult<List<String>> result) {
                mView.onSearchLogLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<List<String>> result) {
                showToast(result.getMsg());
            }
        });
    }

    public void deleteUserSearchLog() {
        famousDoctorSearchModel.deleteUserSearchLog(new HttpTaskCallBack<List<String>>() {
            @Override
            public void onSuccess(HttpResult<List<String>> result) {
                mView.deleteUserSearchLogSuccess();
            }

            @Override
            public void onFail(HttpResult<List<String>> result) {
                showToast(result.getMsg());
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}

package com.bowen.tcm.folkprescription.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.FolkPrescription;
import com.bowen.tcm.common.bean.HospitalDepartments;
import com.bowen.tcm.common.bean.ShowApplyCrowd;
import com.bowen.tcm.folkprescription.contract.FolkPrescriptionContract;
import com.bowen.tcm.folkprescription.contract.FolkPrescriptionSearchContract;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionModel;
import com.bowen.tcm.folkprescription.model.FolkPrescriptionSearchModel;

import java.util.List;

/**
 * 偏方搜索页的数据提供者
 *
 * Created by zzp on 2017/5/21.
 *
 */

public class FolkPrescriptionSearchPresenter extends BasePresenter {
    private FolkPrescriptionSearchModel folkPrescriptionSearchModel;
    private FolkPrescriptionSearchContract.View mView;

    public FolkPrescriptionSearchPresenter(Context context, FolkPrescriptionSearchContract.View view) {
        super(context);
        folkPrescriptionSearchModel = new FolkPrescriptionSearchModel(context);
        mContext = context;
        mView = view;
    }

    /**
     * 获取热搜
     */
    public void showHotSearchList() {
        folkPrescriptionSearchModel.showHotSearchList(new HttpTaskCallBack<List<String>>() {
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
        folkPrescriptionSearchModel.showUserSerachLog(new HttpTaskCallBack<List<String>>() {
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

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }

    public void deleteUserSearchLog() {
        folkPrescriptionSearchModel.deleteUserSearchLog(new HttpTaskCallBack<List<String>>() {
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
}

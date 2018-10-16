package com.bowen.tcm.healthcare.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.Column;
import com.bowen.tcm.healthcare.contract.HealthCareContract;
import com.bowen.tcm.healthcare.model.HealthCareModel;

import java.util.ArrayList;


public class HealthCareFragmentPresenter extends BasePresenter implements HealthCareContract.Presenter{
    private HealthCareModel mHealthCareModel;
    private HealthCareContract.View mView;

    public HealthCareFragmentPresenter(Context context, HealthCareContract.View view) {
        super(context);
        mHealthCareModel = new HealthCareModel(context);
        mContext = context;
        mView = view;
    }

    @Override
    public void loadColumnsList() {
        mHealthCareModel.getColumnsList(new HttpTaskCallBack<ArrayList<Column>>() {
            @Override
            public void onSuccess(HttpResult<ArrayList<Column>> result) {
                mView.onLoadColumnsListSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<ArrayList<Column>> result) {
                ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });
    }
}

package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.mine.contract.CollectPrescriptionContract;
import com.bowen.tcm.mine.contract.MyPrescriptionContract;
import com.bowen.tcm.mine.model.CollectPrescriptionModel;
import com.bowen.tcm.mine.model.MyPrescriptionModel;

/**
 * Created by zzp on 2017/5/21.
 * 我的偏方数据提供类
 */
public class MyPrescriptionPresenter extends BasePresenter {

    private MyPrescriptionModel myPrescriptionModel;
    private MyPrescriptionContract.View mView;

    public MyPrescriptionPresenter(Context context, MyPrescriptionContract.View view) {
        super(context);
        myPrescriptionModel = new MyPrescriptionModel(context);
        mContext = context;
        mView = view;
    }

    /**
     * 获取我的偏方
     */
    public void loadData(int index, int pageSize) {
        myPrescriptionModel.loadData(index, pageSize, new HttpTaskCallBack<FolkPrescriptionRecord>() {
            @Override
            public void onSuccess(HttpResult<FolkPrescriptionRecord> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<FolkPrescriptionRecord> result) {
                showToast(result.getMsg());
                mView.onLoadFail(result.getData());
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}

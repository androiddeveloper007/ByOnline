package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.OutpatientDetailRecord;
import com.bowen.tcm.mine.contract.OutpatientDetailContract;
import com.bowen.tcm.mine.model.OutpatientDetailModel;

/**
 * Created by zzp on 2017/5/21.
 * 预约详情数据提供类
 */
public class OutpatientDetailPresenter extends BasePresenter {

    private OutpatientDetailModel outpatientDetailModel;
    private OutpatientDetailContract.View mView;

    public OutpatientDetailPresenter(Context context, OutpatientDetailContract.View view) {
        super(context);
        outpatientDetailModel = new OutpatientDetailModel(context);
        mContext = context;
        mView = view;
    }

    public void getTraOrderById(String orderId) {
        outpatientDetailModel.getTraOrderById(orderId,new HttpTaskCallBack<OutpatientDetailRecord>() {
            @Override
            public void onSuccess(HttpResult<OutpatientDetailRecord> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<OutpatientDetailRecord> result) {
                showToast(result.getMsg());
                mView.onLoadFail(result.getData());
            }
        });
    }

    public void cancelTraOrder(String orderId, String status) {
        outpatientDetailModel.cancelTraOrder(orderId, status, new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                mView.cancelOrderSucc();
            }

            @Override
            public void onFail(HttpResult<Object> result) {
                showToast(result.getMsg());
                mView.cancelOrderFail();
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}

package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.common.bean.network.MyOrderRecord;
import com.bowen.tcm.mine.contract.CollectPrescriptionContract;
import com.bowen.tcm.mine.contract.MyOrderContract;
import com.bowen.tcm.mine.model.CollectPrescriptionModel;
import com.bowen.tcm.mine.model.MyOrderModel;

/**
 * Created by zzp on 2017/5/21.
 * 我的订单数据提供类
 */
public class MyOrderPresenter extends BasePresenter {

    private MyOrderModel myOrderModel;
    private MyOrderContract.View mView;

    public MyOrderPresenter(Context context, MyOrderContract.View view) {
        super(context);
        myOrderModel = new MyOrderModel(context);
        mContext = context;
        mView = view;
    }

    /**
     * 获取订单列表
     */
    public void loadData(int index, int pageSize,int orderStatus) {
        myOrderModel.loadData(index, pageSize, orderStatus,new HttpTaskCallBack<MyOrderRecord>() {
            @Override
            public void onSuccess(HttpResult<MyOrderRecord> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<MyOrderRecord> result) {
                showToast(result.getMsg());
                mView.onLoadFail(result.getData());
            }
        });
    }

    public void cancelTraOrder(String orderId, String status) {
        myOrderModel.cancelTraOrder(orderId, status, new HttpTaskCallBack<Object>() {
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

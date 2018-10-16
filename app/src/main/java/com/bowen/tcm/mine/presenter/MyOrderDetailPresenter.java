package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.TraOrder;
import com.bowen.tcm.common.bean.network.MyOrderRecord;
import com.bowen.tcm.mine.contract.MyOrderContract;
import com.bowen.tcm.mine.contract.MyOrderDetailContract;
import com.bowen.tcm.mine.model.MyOrderDetailModel;
import com.bowen.tcm.mine.model.MyOrderModel;

/**
 * Created by zzp on 2017/5/21.
 * 我的订单数据提供类
 */
public class MyOrderDetailPresenter extends BasePresenter {

    private MyOrderDetailModel myOrderDetailModel;
    private MyOrderDetailContract.View mView;

    public MyOrderDetailPresenter(Context context, MyOrderDetailContract.View view) {
        super(context);
        myOrderDetailModel = new MyOrderDetailModel(context);
        mContext = context;
        mView = view;
    }

    /**
     * 获取订单列表
     */
    public void getTraOrderById(String orderId) {
        myOrderDetailModel.getTraOrderById(orderId,new HttpTaskCallBack<TraOrder>() {
            @Override
            public void onSuccess(HttpResult<TraOrder> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<TraOrder> result) {
                showToast(result.getMsg());
                mView.onLoadFail(result.getData());
            }
        });
    }

    public void cancelTraOrder(String orderId, String status) {
        myOrderDetailModel.cancelTraOrder(orderId, status, new HttpTaskCallBack<Object>() {
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

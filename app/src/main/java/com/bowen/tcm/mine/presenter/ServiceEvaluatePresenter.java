package com.bowen.tcm.mine.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.VoListBean;
import com.bowen.tcm.mine.contract.ServiceEvaluateContract;
import com.bowen.tcm.mine.model.ServiceEvaluateModel;

import java.util.List;

/**
 * Created by zzp on 2017/5/21.
 * 服务评价提供类
 */
public class ServiceEvaluatePresenter extends BasePresenter {

    private ServiceEvaluateModel serviceEvaluateModel;
    private ServiceEvaluateContract.View mView;

    public ServiceEvaluatePresenter(Context context, ServiceEvaluateContract.View view) {
        super(context);
        serviceEvaluateModel = new ServiceEvaluateModel(context);
        mContext = context;
        mView = view;
    }

    public void userEvaluateVoList() {
        serviceEvaluateModel.userEvaluateVoList(new HttpTaskCallBack<List<VoListBean>>() {
            @Override
            public void onSuccess(HttpResult<List<VoListBean>> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<List<VoListBean>> result) {
                showToast(result.getMsg());
                mView.onLoadFail(result.getData());
            }
        });
    }

    public void saveTraOrderById(String orderId, double score,
                                 double doctorAttitude, double replySpeed,
                                 double serviceLevel, String userEvaluate) {
        if(TextUtils.isEmpty(orderId)){
            showToast("订单号为空，请检查网络后重试");
            return;
        }
        if(TextUtils.isEmpty(userEvaluate)){
            showToast("请选择或输入评价语");
            return;
        }
        serviceEvaluateModel.saveTraOrderById(orderId, score, doctorAttitude, replySpeed, serviceLevel, userEvaluate,
                new HttpTaskCallBack<Object>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                mView.saveEvaluateSucc();
            }

            @Override
            public void onFail(HttpResult<Object> result) {
                showToast(result.getMsg());
                mView.saveEvaluateFail();
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}

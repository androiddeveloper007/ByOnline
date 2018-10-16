package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.mine.contract.CollectPrescriptionContract;
import com.bowen.tcm.mine.model.CollectPrescriptionModel;

/**
 * Created by zzp on 2017/5/21.
 * 收藏偏方数据提供类
 */
public class CollectPrescriptionPresenter extends BasePresenter {

    private CollectPrescriptionModel collectPrescriptionModel;
    private CollectPrescriptionContract.View mView;

    public CollectPrescriptionPresenter(Context context, CollectPrescriptionContract.View view) {
        super(context);
        collectPrescriptionModel = new CollectPrescriptionModel(context);
        mContext = context;
        mView = view;
    }

    /**
     * 获取收藏偏方
     */
    public void loadData(int index, int pageSize) {
        collectPrescriptionModel.loadData(index, pageSize, new HttpTaskCallBack<FolkPrescriptionRecord>() {
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

    /***
     * 收藏偏方
     * @param isCollect true表示收藏， false表示取消收藏
     */
    public void folkPrescriptionCollectOrNot(boolean isCollect, String prescriptionId, final int position) {
        collectPrescriptionModel.folkPrescriptionCollectOrNot(isCollect, prescriptionId, new HttpTaskCallBack<String>() {
            @Override
            public void onSuccess(HttpResult<String> result) {
                mView.setCollectSuccess(position);
            }

            @Override
            public void onFail(HttpResult<String> result) {
                mView.setCollectFail();
                showToast(result.getData());
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}

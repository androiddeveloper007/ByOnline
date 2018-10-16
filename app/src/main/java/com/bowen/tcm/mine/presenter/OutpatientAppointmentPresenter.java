package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.OutpatientAppointRecord;
import com.bowen.tcm.mine.contract.OutpatientAppointmentContract;
import com.bowen.tcm.mine.model.OutpatientAppointmentModel;

/**
 * Created by zzp on 2017/5/21.
 * 门诊预约数据提供类
 */
public class OutpatientAppointmentPresenter extends BasePresenter {

    private OutpatientAppointmentModel outpatientAppointmentModel;
    private OutpatientAppointmentContract.View mView;

    public OutpatientAppointmentPresenter(Context context, OutpatientAppointmentContract.View view) {
        super(context);
        outpatientAppointmentModel = new OutpatientAppointmentModel(context);
        mContext = context;
        mView = view;
    }

    /**
     * 获取门诊预约列表
     */
    public void loadData(int index, int pageSize) {
        outpatientAppointmentModel.loadData(index, pageSize, new HttpTaskCallBack<OutpatientAppointRecord>() {
            @Override
            public void onSuccess(HttpResult<OutpatientAppointRecord> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<OutpatientAppointRecord> result) {
                showToast(result.getMsg());
                mView.onLoadFail(result.getData());
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}

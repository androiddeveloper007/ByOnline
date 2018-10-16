package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.ConsultListItem;
import com.bowen.tcm.common.bean.network.ConsultListRecord;
import com.bowen.tcm.common.bean.network.OutpatientAppointRecord;
import com.bowen.tcm.mine.contract.MyConsultContract;
import com.bowen.tcm.mine.contract.OutpatientAppointmentContract;
import com.bowen.tcm.mine.model.MyConsultModel;
import com.bowen.tcm.mine.model.OutpatientAppointmentModel;

import java.util.ArrayList;

/**
 * Created by zzp on 2017/5/21.
 * 我的咨询数据提供类
 */
public class MyConsultPresenter extends BasePresenter {

    private MyConsultModel myConsultModel;
    private MyConsultContract.View mView;

    public MyConsultPresenter(Context context, MyConsultContract.View view) {
        super(context);
        myConsultModel = new MyConsultModel(context);
        mContext = context;
        mView = view;
    }

    public void loadData(int index, int pageSize) {
        myConsultModel.loadData(index, pageSize, new HttpTaskCallBack<ConsultListRecord>() {
            @Override
            public void onSuccess(HttpResult<ConsultListRecord> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<ConsultListRecord> result) {
                showToast(result.getMsg());
                mView.onLoadFailed();
            }
        });
    }

    public void removeConsultInfo(String sendUserId) {
        myConsultModel.removeConsultInfo(sendUserId, new HttpTaskCallBack<String>() {
            @Override
            public void onSuccess(HttpResult<String> result) {
                 mView.onRemoveConsultInfo();
            }

            @Override
            public void onFail(HttpResult<String> result) {

            }
        });
    }


    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}

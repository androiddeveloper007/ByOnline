package com.bowen.tcm.mine.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorFanRecord;
import com.bowen.tcm.common.bean.network.OutpatientAppointRecord;
import com.bowen.tcm.inquiry.model.DoctorModel;
import com.bowen.tcm.mine.contract.MyConsultContract;
import com.bowen.tcm.mine.contract.MySubscribeContract;
import com.bowen.tcm.mine.model.MyConsultModel;
import com.bowen.tcm.mine.model.MySubscribeModel;

/**
 * Created by zzp on 2017/5/21.
 * 我的关注
 */
public class MySubscribePresenter extends BasePresenter {

    private MySubscribeModel mySubscribeModel;
    private DoctorModel mDoctorModel;
    private MySubscribeContract.View mView;

    public MySubscribePresenter(Context context, MySubscribeContract.View view) {
        super(context);
        mySubscribeModel = new MySubscribeModel(context);
        mContext = context;
        mDoctorModel = new DoctorModel(mContext);
        mView = view;
    }

    public void loadData(int index, int pageSize) {
        mySubscribeModel.loadData(index, pageSize, new HttpTaskCallBack<DoctorFanRecord>() {
            @Override
            public void onSuccess(HttpResult<DoctorFanRecord> result) {
                mView.onLoadSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<DoctorFanRecord> result) {
                showToast(result.getMsg());
                mView.onLoadFail(result.getData());
            }
        });
    }

    public void userFenDoctor(String doctorId, String isFen) {
        mySubscribeModel.userFenDoctor(doctorId, isFen, new HttpTaskCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                mView.userFenDoctorSuccess();
            }

            @Override
            public void onFail(HttpResult result) {
                showToast(result.getMsg());
            }
        });
    }


    public void checkImmediateConsult(String doctorId){
        mDoctorModel.checkImmediateConsult(doctorId, new HttpTaskCallBack<Doctor>() {
            @Override
            public void onSuccess(HttpResult<Doctor> result) {
                mView.onCheckImmediateConsultSuccess(result.getData());

            }

            @Override
            public void onFail(HttpResult<Doctor> result) {
                 showToast(result.getMsg());
            }
        });
    }

    private void showToast(String erro) {
        ToastUtil.getInstance().showToastDialog(erro);
    }
}

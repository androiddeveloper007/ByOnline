package com.bowen.tcm.inquiry.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.SearchField;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorDetail;
import com.bowen.tcm.common.bean.network.DoctorInfo;
import com.bowen.tcm.inquiry.contract.DoctorDetailContract;
import com.bowen.tcm.inquiry.contract.FindDoctorContract;
import com.bowen.tcm.inquiry.model.DoctorModel;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public class DoctorDetailPresenter extends BasePresenter implements DoctorDetailContract.Presenter{
    private DoctorModel mDoctorModel;
    private DoctorDetailContract.View mView;

    public DoctorDetailPresenter(Context mContext, DoctorDetailContract.View view) {
        super(mContext);
        mView = view;
        mDoctorModel = new DoctorModel(mContext);
    }

    @Override
    public void getDoctorDetail(String doctorId) {
        mDoctorModel.getDoctorDetail(doctorId, new HttpTaskCallBack<DoctorDetail>() {
            @Override
            public void onSuccess(HttpResult<DoctorDetail> result) {
               mView.onGetDoctorDetailSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<DoctorDetail> result) {
                ToastUtil.getInstance().toast(result.getMsg());
            }
        });
    }

    @Override
    public void changeAttentionStatus(String doctorId, boolean attentionStatus) {
        mDoctorModel.changeAttentionStatus(doctorId, attentionStatus, new HttpTaskCallBack<Boolean>() {
            @Override
            public void onSuccess(HttpResult<Boolean> result) {
                mView.onChangeAttentionStatus(result.getData());
            }

            @Override
            public void onFail(HttpResult<Boolean> result) {

            }
        });
    }


    @Override
    public void checkImmediateConsult(String doctorId){
        mDoctorModel.checkImmediateConsult(doctorId, new HttpTaskCallBack<Doctor>() {
            @Override
            public void onSuccess(HttpResult<Doctor> result) {
                mView.onCheckImmediateConsultSuccess(result.getData());

            }

            @Override
            public void onFail(HttpResult<Doctor> result) {
                ToastUtil.getInstance().showToastDialog(result.getMsg());
            }
        });
    }
}

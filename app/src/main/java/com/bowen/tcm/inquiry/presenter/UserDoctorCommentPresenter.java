package com.bowen.tcm.inquiry.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.network.AppointmentInfo;
import com.bowen.tcm.common.bean.network.DoctorCommentInfo;
import com.bowen.tcm.common.bean.network.UserDoctorComment;
import com.bowen.tcm.inquiry.contract.OutpatientAppointmentContract;
import com.bowen.tcm.inquiry.contract.UserDoctorCommentContract;
import com.bowen.tcm.inquiry.model.DoctorCommentModel;
import com.bowen.tcm.inquiry.model.DoctorModel;

import java.util.ArrayList;

/**
 * 门诊预约的数据提供者
 * Created by zzp on 2017/5/21.
 */

public class UserDoctorCommentPresenter extends BasePresenter implements UserDoctorCommentContract.Presenter{
    private DoctorCommentModel mDoctorCommentModel;
    private UserDoctorCommentContract.View mView;

    public UserDoctorCommentPresenter(Context context, UserDoctorCommentContract.View view) {
        super(context);
        mDoctorCommentModel = new DoctorCommentModel(context);
        mView = view;
    }


    @Override
    public void getDoctorCommentList(String doctorId) {
        mDoctorCommentModel.getDoctorCommentList(doctorId, new HttpTaskCallBack<UserDoctorComment>() {
            @Override
            public void onSuccess(HttpResult<UserDoctorComment> result) {
                mView.onGetDoctorCommentListSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<UserDoctorComment> result) {
                mView.onGetDoctorCommentListFailed();
            }
        });
    }
}

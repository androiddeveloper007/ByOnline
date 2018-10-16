package com.bowen.tcm.inquiry.presenter;

import android.content.Context;

import com.bowen.commonlib.base.BasePresenter;
import com.bowen.commonlib.http.HttpResult;
import com.bowen.commonlib.http.HttpTaskCallBack;
import com.bowen.commonlib.utils.ToastUtil;
import com.bowen.tcm.common.bean.Appointment;
import com.bowen.tcm.common.bean.network.AppointmentInfo;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.OutpatientAppointmentRecord;
import com.bowen.tcm.inquiry.contract.OutpatientAppointmentContract;
import com.bowen.tcm.inquiry.model.DoctorModel;
import com.bowen.tcm.inquiry.model.OutpatientAppointmentModel;

import java.util.ArrayList;

/**
 * 门诊预约的数据提供者
 * Created by zzp on 2017/5/21.
 */

public class OutpatientAppointmentPresenter extends BasePresenter implements OutpatientAppointmentContract.Presenter{
    private OutpatientAppointmentModel mOutpatientAppointmentModel;
    private OutpatientAppointmentContract.View mView;

    public OutpatientAppointmentPresenter(Context context, OutpatientAppointmentContract.View view) {
        super(context);
        mOutpatientAppointmentModel = new OutpatientAppointmentModel(context);
        mView = view;
    }


    @Override
    public void getDoctorAppointmentInfo(String doctorId, String weekStatus) {
        mOutpatientAppointmentModel.getDoctorAppointmentInfo(doctorId, weekStatus, new HttpTaskCallBack<ArrayList<AppointmentInfo>>() {
            @Override
            public void onSuccess(HttpResult<ArrayList<AppointmentInfo>> result) {
                mView.onGetDoctorAppointMentInfoSuccess(result.getData());
            }

            @Override
            public void onFail(HttpResult<ArrayList<AppointmentInfo>> result) {
                ToastUtil.getInstance().toast(result.getMsg());
            }
        });
    }

}

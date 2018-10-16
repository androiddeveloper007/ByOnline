package com.bowen.tcm.inquiry.contract;

import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorDetail;
import com.bowen.tcm.common.bean.network.DoctorInfo;

/**
 * Describe:
 * Created by AwenZeng on 2018/7/9.
 */
public interface DoctorDetailContract {

    interface View {
        void onGetDoctorDetailSuccess(DoctorDetail doctorDetail);
        void onChangeAttentionStatus(boolean isSuccess);
        void onCheckImmediateConsultSuccess(Doctor doctor);
    }

    interface Presenter{
        void getDoctorDetail(String doctorId);
        void changeAttentionStatus(String doctorId,boolean attentionStatus);
        void checkImmediateConsult(String doctorId);
    }
}

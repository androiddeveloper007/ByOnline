package com.bowen.tcm.inquiry.contract;

import com.bowen.tcm.common.bean.network.UserDoctorComment;

/**
 * Created by zzp on 2018/5/15.
 */

public interface UserDoctorCommentContract {

    interface View {
        void onGetDoctorCommentListSuccess(UserDoctorComment userDoctorComment);
        void onGetDoctorCommentListFailed();
    }

    interface Presenter {
        void getDoctorCommentList(String doctorId);
    }
}

package com.bowen.tcm.folkprescription.contract;

import com.bowen.tcm.common.bean.network.PrescriptionDoctorCommentRecord;

/**
 * Created by zzp on 2018/5/15.
 */

public interface DoctorCommentContract {

    interface View {

        void getDoctorCommentSuccess(PrescriptionDoctorCommentRecord list);

        void getDoctorCommentFail(PrescriptionDoctorCommentRecord list);

    }

    interface Presenter {

    }
}

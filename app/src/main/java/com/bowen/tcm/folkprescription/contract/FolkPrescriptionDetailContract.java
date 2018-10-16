package com.bowen.tcm.folkprescription.contract;

import com.bowen.tcm.common.bean.DoctorPrescriptionComment;
import com.bowen.tcm.common.bean.network.InfoFolkPrescription;
import com.bowen.tcm.common.bean.network.PrescriptionDoctorCommentRecord;
import com.bowen.tcm.common.bean.network.PrescriptionUserCommentRecord;

/**
 * Created by zzp on 2018/5/15.
 */

public interface FolkPrescriptionDetailContract {

    interface View {
        void onLoadSuccess(InfoFolkPrescription list);

        void onLoadFail(InfoFolkPrescription list);

        void getDoctorCommentSuccess(PrescriptionDoctorCommentRecord list);

        void getDoctorCommentFail(PrescriptionDoctorCommentRecord list);

        void getUserCommentSuccess(PrescriptionUserCommentRecord list);

        void getUserCommentFail(PrescriptionUserCommentRecord list);
    }

    interface Presenter {

    }
}

package com.bowen.tcm.folkprescription.contract;

import com.bowen.tcm.common.bean.network.InfoFolkPrescription;
import com.bowen.tcm.common.bean.network.PrescriptionDoctorCommentRecord;
import com.bowen.tcm.common.bean.network.PrescriptionUserCommentRecord;

/**
 * Created by zzp on 2018/5/15.
 */

public interface UserCommentContract {

    interface View {

        void getUserCommentSuccess(PrescriptionUserCommentRecord list);

        void getUserCommentFail(PrescriptionUserCommentRecord list);
    }

    interface Presenter {

    }
}

package com.bowen.tcm.inquiry.contract;

import com.bowen.tcm.common.bean.network.ClinicDetailInfo;
import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.HospitalDetailInfo;

/**
 * Created by zzp on 2018/5/15.
 */

public interface HospitalDetailContract {

    interface View {
        void onLoadSuccess(HospitalDetailInfo info);
        void onLoadFail(HospitalDetailInfo info);
        void onCheckImmediateConsultSuccess(Doctor doctor);
    }

    interface Presenter {

    }
}

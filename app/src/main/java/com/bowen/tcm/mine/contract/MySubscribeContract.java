package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.network.Doctor;
import com.bowen.tcm.common.bean.network.DoctorFanRecord;
import com.bowen.tcm.common.bean.network.OutpatientAppointRecord;

/**
 * Created by zzp on 2018/5/15.
 */

public interface MySubscribeContract {

    interface View {
        void onLoadSuccess(DoctorFanRecord list);
        void onLoadFail(DoctorFanRecord list);
        void onCheckImmediateConsultSuccess(Doctor doctor);

        void userFenDoctorSuccess();
    }

    interface Presenter {

    }
}

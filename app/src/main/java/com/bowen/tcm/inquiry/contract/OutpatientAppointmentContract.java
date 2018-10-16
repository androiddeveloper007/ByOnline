package com.bowen.tcm.inquiry.contract;

import com.bowen.tcm.common.bean.Appointment;
import com.bowen.tcm.common.bean.network.AppointmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 */

public interface OutpatientAppointmentContract {

    interface View {
        void onGetDoctorAppointMentInfoSuccess(ArrayList<AppointmentInfo> appointments);
    }

    interface Presenter {
        void getDoctorAppointmentInfo(String doctorId,String weekStatus);
    }
}

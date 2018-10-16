package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.network.OutpatientAppointRecord;

/**
 * Created by zzp on 2018/5/15.
 */

public interface OutpatientAppointmentContract {

    interface View {
        void onLoadSuccess(OutpatientAppointRecord list);

        void onLoadFail(OutpatientAppointRecord list);

    }

    interface Presenter {

    }
}

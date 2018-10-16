package com.bowen.tcm.inquiry.contract;

import com.bowen.tcm.common.bean.network.CliniclInfo;

/**
 * Created by zzp on 2018/5/15.
 */

public interface FamousHospitalContract {

    interface View {
        void onLoadSuccess(CliniclInfo info);
        void onLoadFail(CliniclInfo info);
    }

    interface Presenter {

    }
}

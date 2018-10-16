package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.network.ConsultListRecord;

/**
 * Created by zzp on 2018/5/15.
 */

public interface MyConsultContract {

    interface View {
        void onLoadSuccess(ConsultListRecord list);
        void onLoadFailed();
        void onRemoveConsultInfo();
    }

    interface Presenter {

    }
}

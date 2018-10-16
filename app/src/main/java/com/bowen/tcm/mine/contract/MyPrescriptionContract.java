package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;

/**
 * Created by zzp on 2018/5/15.
 */

public interface MyPrescriptionContract {

    interface View {
        void onLoadSuccess(FolkPrescriptionRecord list);

        void onLoadFail(FolkPrescriptionRecord list);

    }

    interface Presenter {

    }
}

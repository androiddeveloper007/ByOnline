package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.network.FolkPrescriptionRecord;
import com.bowen.tcm.common.bean.network.MyOrderRecord;

/**
 * Created by zzp on 2018/5/15.
 */

public interface MyOrderContract {

    interface View {
        void onLoadSuccess(MyOrderRecord list);

        void onLoadFail(MyOrderRecord list);

        void cancelOrderSucc();

        void cancelOrderFail();

    }

    interface Presenter {

    }
}

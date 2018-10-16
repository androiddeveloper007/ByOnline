package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.OutpatientDetailRecord;

/**
 * Created by zzp on 2018/5/15.
 */

public interface OutpatientDetailContract {

    interface View {
        void onLoadSuccess(OutpatientDetailRecord list);
        void onLoadFail(OutpatientDetailRecord list);
        void cancelOrderSucc();
        void cancelOrderFail();
    }

    interface Presenter {

    }
}

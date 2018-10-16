package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.TraOrder;

/**
 * Created by zzp on 2018/5/15.
 */

public interface MyOrderDetailContract {

    interface View {
        void onLoadSuccess(TraOrder list);

        void onLoadFail(TraOrder list);

        void cancelOrderSucc();

        void cancelOrderFail();

    }

    interface Presenter {

    }
}

package com.bowen.tcm.mine.contract;

import com.bowen.tcm.common.bean.network.MyOrderRecord;
import com.bowen.tcm.common.bean.network.VoListBean;

import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 */

public interface ServiceEvaluateContract {

    interface View {
        void onLoadSuccess(List<VoListBean> list);
        void onLoadFail(List<VoListBean> list);
        void saveEvaluateSucc();
        void saveEvaluateFail();
    }

    interface Presenter {

    }
}

package com.bowen.tcm.healthcare.contract;

import com.bowen.tcm.common.bean.network.Column;

import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 */

public interface ChooseColumnsContract {
    interface View {
        void onChooseSuccess();
        void onLoadSuccess(List<Column> list);
    }

    interface Presenter{

    }
}

package com.bowen.tcm.healthcare.contract;

import com.bowen.tcm.common.bean.network.Column;

import java.util.ArrayList;

/**
 * Created by zzp on 2018/5/15.
 */

public interface HealthCareContract {

    interface View {
        void onLoadColumnsListSuccess(ArrayList<Column> columns);
    }

    interface Presenter{
        void loadColumnsList();
    }
}

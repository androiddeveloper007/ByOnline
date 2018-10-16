package com.bowen.tcm.inquiry.contract;

import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 */

public interface DoctorSearchContract {

    interface View {
        void onHotSearchLoadSuccess(List<String> list);
        void onSearchLogLoadSuccess(List<String> list);
        void deleteUserSearchLogSuccess();
    }

    interface Presenter {

    }
}

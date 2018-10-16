package com.bowen.tcm.folkprescription.contract;

import com.bowen.tcm.common.bean.FolkPrescription;
import com.bowen.tcm.common.bean.HospitalDepartments;
import com.bowen.tcm.common.bean.ShowApplyCrowd;

import java.util.List;

/**
 * Created by zzp on 2018/5/15.
 */

public interface FolkPrescriptionSearchContract {

    interface View {
        void onHotSearchLoadSuccess(List<String> list);
        void onSearchLogLoadSuccess(List<String> list);
        void deleteUserSearchLogSuccess();
    }

    interface Presenter {

    }
}
